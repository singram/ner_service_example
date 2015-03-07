package ner_service.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class RegExNerService implements NerService {

	private Logger log = Logger.getLogger(RegExNerService.class);

	private static final Map<Pattern, String> regExRules;

	private static final int defaultFlags = Pattern.CASE_INSENSITIVE;

	// http://www.regexplanet.com/advanced/java/index.html
	// http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html

	// John L. Smith
	private static final String NAME_FORMAT1 = "\\b(\\w+(?:\\p{Blank}\\w\\.?)?\\p{Blank}\\w+)\\b"; 

	// Smith, John L
	private static final  String NAME_FORMAT2 = "\\b(\\w+,\\p{Blank}*\\w+(?:\\p{Blank}+\\w\\.?)?)\\b"; 

	private static final  String DATE_FORMAT = "\\b(\\d{1,2}(?:/|-)\\d{1,2}(?:/|-)\\d{2,4})\\b";

	private static final String rules[][] = {
			{ "DATE", DATE_FORMAT },
			{ "DATE", "\\W(\\d{1,2}(?:/|-| )"
					+ "(?:jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)"
					+ "(?:/|-| )\\d{2,4})\\W" },
			{ "AGE", "AGE:\\s+(\\d{1,2})\\W" },
			{ "AGE", "(\\d{1,2}) years? old" },
			{ "AGE", "(\\d{1,2})-years?-old" },
			{ "AGE", "(\\d{1,2}) yrs?" },
			{ "PERSON",
					"(?:name|physician|by|patient|author|md|technician|pathologist) *: *"
							+ NAME_FORMAT1 },
			{ "PERSON",
					"(?:name|physician|by|patient|author|md|technician|pathologist) *: *"
							+ NAME_FORMAT2 },
			{ "PERSON",
					NAME_FORMAT1 + " *(?:, *)?(?:LPN|CNM|MD|\\(MD\\)|M\\.D\\.)" },
			{ "PERSON",
					NAME_FORMAT2 + " *(?:, *)?(?:LPN|CNM|MD|\\(MD\\)|M\\.D\\.)" },
			{ "PERSON", "(?:case attendee)(?:\\p{Blank}+" + NAME_FORMAT2 + ")+" },
			{ "PERSON", "(?:completed|requested) by +" + NAME_FORMAT2 },

			{ "PERSON", "\\b(Dr\\. +\\w+)\\b" },
			{ "IDENTIFIER", "\\W(\\d{6,})\\W" },
			{ "PHONE", "(\\d{3}-\\d{3}-\\d{4})" },
			{ "RELATION", "\\b(father|mother|spouce|wife|husband|son|daughter|"
				+ "cousin|nephew|girlfriend|boyfriend|brother|brothers|sister|sisters)\\b" },
			{ "LOCATION", "Pharmacy Name & Phone: (.*)$" } };

	private static final String lineRules[][] = {
			{ "PERSON", "^" + NAME_FORMAT2 + "$" },
			{ "IDENTIFIER", "^ *Case number *: *(\\S+)\\s" },
			{ "LOCATION", "^ *ROOM # *: *(\\S+)\\s" },
			{ "LOCATION", "^ *(?:location|department) *: *(\\S+)\\s" } };

	static {
		regExRules = new HashMap<Pattern, String>();
		for (String[] rule : rules) {
			regExRules.put(Pattern.compile(rule[1], defaultFlags), rule[0]);
		}
		for (String[] rule : lineRules) {
			regExRules.put(
					Pattern.compile(rule[1], defaultFlags | Pattern.MULTILINE),
					rule[0]);
		}
	}

	public HashMap<String, HashSet<String>> getEntitiesFor(String text) {
		HashMap<String, HashSet<String>> entities = new HashMap<String, HashSet<String>>();
		for (Map.Entry<Pattern, String> entry : regExRules.entrySet()) {
			String classification = entry.getValue();
			log.debug(entry.getKey().pattern());
			Matcher m = entry.getKey().matcher(text);
			HashSet<String> existingClassification = entities
					.get(classification);
			if (existingClassification == null) {
				existingClassification = new HashSet<String>();
			}
			while (m.find()) {
				// Skip the group 0 match
				for (int grp = 1; grp <= m.groupCount(); grp++) {
					existingClassification.add(m.group(grp).toLowerCase());
				}
			}
			entities.put(classification, existingClassification);
		}
		return entities;
	}

	@Override
	public String toString() {
		return "RegEx";
	}

	public String deIdentify(String text) {
		HashMap<String, HashSet<String>> entities = getEntitiesFor(text);
		for (String classification : entities.keySet()) {
			for (String match : entities.get(classification)) {
				text = deIdentifyPart(text, classification, match);
			}
		}
		return text;
	}

	private String deIdentifyPart(String text, String classification,
			String match) {
		switch (classification) {
		// case "PERSON":
		// text = deIdentifyName(text, match);
		// break;
		default:
			text = deIdentifyGeneral(text, match);
			break;
		}
		return text;
	}

	private String deIdentifyGeneral(String text, String match) {
		String replacement = StringUtils.repeat('X', match.length());
		replacement = "<span data-toggle=\"tooltip\" data-original-title=\""
				+ match + "\">" + replacement + "</span>";
		// text = StringUtils.replace(text, match, replacement);
		text = text.replaceAll("(?i)\\b" + match + "\\b", replacement);
		return text;
	}

	private String deIdentifyName(String text, String nameMatch) {
		String replacement;
		for (String namePart : StringUtils.split(nameMatch)) {
			namePart = StringUtils.remove(namePart, ",");
			namePart = StringUtils.remove(namePart, ".");
			if (namePart.length() > 1) {
				replacement = StringUtils.repeat('X', namePart.length());
				// Case insensitive replacement with forcing word boundaries
				text = text.replaceAll("(?i)\\b" + namePart + "\\b",
						replacement);
			}
		}
		return text;
	}
}
