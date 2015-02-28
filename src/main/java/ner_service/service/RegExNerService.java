package ner_service.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RegExNerService implements NerService {

	private static final Map<Pattern, String> regExRules;

	private static final int defaultFlags = Pattern.CASE_INSENSITIVE;

	// http://www.regexplanet.com/advanced/java/index.html
	// http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
	
	private static String NAME_FORMAT1 = "\\b(\\w+(?: \\w\\.?)? \\w+)\\b";  // John L. Smith
	private static String NAME_FORMAT2 = "\\b(\\w+, *\\w+(?: +\\w\\.?)?)\\b"; // Smith, John L
	
	private static String rules[][] = {
			{ "DATE", "DOB:\\s+(\\d{1,2}/\\d{1,2}/\\d{2,4})" },
			{ "DATE", "\\Won\\s+(\\d{1,2}/\\d{1,2}/\\d{2,4})\\W" },
			{ "DATE", "\\W(\\d{1,2}/\\d{1,2}/\\d{2,4})\\W" },
			{ "AGE", "AGE:\\s+(\\d{1,2})\\W" },
			{ "AGE", "(\\d{1,2}) years? old" },
			{ "AGE", "(\\d{1,2})-years?-old" },
//			{ "PERSON1", "Patient(?: name): +(\\w+, +\\w+(?: +\\w)?)" },
//			{ "PERSON2", "Author:\\s+(\\w+,\\s+\\w+(?:\\s+\\w)?)" },
//			{ "PERSON3", "by ?:? ?(\\w+, +\\w+(?:\\s+\\w)?)\\W" },
//			{ "PERSON4", "by ?:? ?(\\w+(?: \\w\\.)? \\w+)," },
			
			{ "PERSON", "(?:name|physician|by|patient|author|md) *: *" + NAME_FORMAT1 },
			{ "PERSON", "(?:name|physician|by|patient|author|md) *: *" + NAME_FORMAT2 },
			{ "PERSON", NAME_FORMAT1 + " *, *(?:LPN|CNM)"},
			{ "PERSON", NAME_FORMAT2 + " *, *(?:LPN|CNM)"},
			{ "PERSON", "(?:completed|requested) by +" + NAME_FORMAT2 },
			
			{ "PERSON", "\\b(Dr\\. +\\w+)\\b" },
			{ "IDENTIFIER", "\\W(\\d{6,})\\W" },
			{ "PHONE", "(\\d{3}-\\d{3}-\\d{4})" },
			{
					"RELATION",
					"\\b(father|mother|son|daughter|cousin|nephew|girlfriend|boyfriend|spouce|wife|husband|brother|brothers|sister|sisters)\\b" },
			{ "LOCATION", "Pharmacy Name & Phone: (.*)$" } };
	
	private static String lineRules[][] = {
//		{ "PERSON", "^" + NAME_FORMAT1 + "$"},
		{ "PERSON", "^" + NAME_FORMAT2 + "$"},
		{ "IDENTIFIER", "^ *Case number *: *(\\S+)\\s" },
		{ "IDENTIFIER", "^ *ROOM # *: *(\\S+)\\s" }
	};

	static {
		regExRules = new HashMap<Pattern, String>();
		for (String[] rule : rules) {
			regExRules.put(Pattern.compile(rule[1], defaultFlags), rule[0]);
		}
		for (String[] rule : lineRules) {
			regExRules.put(Pattern.compile(rule[1], defaultFlags|Pattern.MULTILINE), rule[0]);
		}
	}

	public HashMap<String, HashSet<String>> getEntitiesFor(String text) {
		HashMap<String, HashSet<String>> entities = new HashMap<String, HashSet<String>>();
		for (Map.Entry<Pattern, String> entry : regExRules.entrySet()) {
			String classification = entry.getValue();
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
//		case "PERSON":
//			text = deIdentifyName(text, match);
//			break;
		default:
			String replacement = StringUtils.repeat('X', match.length());
			replacement = "<span data-toggle=\"tooltip\" data-original-title=\""
					+ match + "\">" + replacement + "</span>";
			//text = StringUtils.replace(text, match, replacement);
			text = text.replaceAll("(?i)\\b" + match + "\\b",
					replacement);
		}
		return text;
	}

	private String deIdentifyName(String text, String name) {
		String replacement;
		for (String namePart : StringUtils.split(name)) {
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
