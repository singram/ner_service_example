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

	private static final int flags = Pattern.CASE_INSENSITIVE;

	// http://www.regexplanet.com/advanced/java/index.html
	// http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
	private static String rules[][] = {
			{ "DOB", "DOB:\\s+(\\d{1,2}/\\d{1,2}/\\d{2,4})" },
			{ "AGE", "AGE:\\s+(\\d{1,2})\\W" },
			{ "AGE", "(\\d{1,2}) years? old" },
			{ "AGE", "(\\d{1,2})-years?-old" },
			{ "NAME", "Patient(?: name): +(\\w+, +\\w+(?: +\\w)?)" },
			{ "NAME", "Author:\\s+(\\w+,\\s+\\w+(?:\\s+\\w)?)" },
			{ "NAME", "by ?:? ?(\\w+, +\\w+(?:\\s+\\w)?)\\W" },
			{ "NAME", "by ?:? ?(\\w+(?: \\w\\.)? \\w+)," },
			{ "NAME", "\\W(Dr\\.\\s+\\w+)\\W" },
			{ "NAME", "Completed by\\s+(\\w+,\\s+\\w+(?:\\s+\\w)?)\\W" },
			{ "DATE", "\\Won\\s+(\\d{1,2}/\\d{1,2}/\\d{2,4})\\W" },
			{ "DATE", "\\W(\\d{1,2}/\\d{1,2}/\\d{4})\\W" },
			{ "IDENTIFIER", "\\W(\\d{6,})\\W" },
			{ "PHONE", "(\\d{3}-\\d{3}-\\d{4})" },
			{
					"RELATION",
					"\\W(father|mother|son|daughter|cousin|nephew|girlfriend|boyfriend|spouce|wife|husband|brother|brothers|sister|sisters)\\W" },
			{ "LOCATION", "Pharmacy Name & Phone: (.*)$" } };

	static {
		regExRules = new HashMap<Pattern, String>();
		for (String[] rule : rules) {
			regExRules.put(Pattern.compile(rule[1], flags), rule[0]);
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
					existingClassification.add(m.group(grp));
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
		for (Map.Entry<Pattern, String> entry : regExRules.entrySet()) {
			Matcher m = entry.getKey().matcher(text);
			while (m.find()) {
				// Skip the group 0 match
				for (int grp = 1; grp <= m.groupCount(); grp++) {
					text = deIdentifyPart(text, entry.getValue(), m.group(grp));
				}
			}
		}
		return text;
	}

	private String deIdentifyPart(String text, String type, String match) {
		switch (type) {
		case "NAME":
			text = deIdentifyName(text, match);
			break;
		default:
			String replacement = StringUtils.repeat('X', match.length());
			text = StringUtils.replace(text, match, replacement);
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
				text = text.replaceAll("(?i)\\b"+namePart+"\\b", replacement);
			}
		}
		return text;
	}
}