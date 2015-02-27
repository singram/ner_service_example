package ner_service.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

// https://opennlp.apache.org/documentation/1.5.3/manual/opennlp.html#tools.namefind.recognition.api

@Service("openNlpNerService")
public class OpenNlpNerService implements NerService {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(OpenNlpNerService.class);

	private static TokenizerModel tokenModel;
	private static SentenceModel sentenceModel;
	private static TokenNameFinderModel personModel;

	static {
		InputStream modelIn = null;
		try {
			modelIn = new FileInputStream("opennlp_models/en-ner-person.bin");
			personModel = new TokenNameFinderModel(modelIn);
			modelIn = new FileInputStream("opennlp_models/en-sent.bin");
			sentenceModel = new SentenceModel(modelIn);
			modelIn = new FileInputStream("opennlp_models/en-token.bin");
			tokenModel = new TokenizerModel(modelIn);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public HashMap<String, HashSet<String>> getEntitiesFor(String text) {
		HashMap<String, HashSet<String>> entities = new HashMap<String, HashSet<String>>();

		String classification = "NAME";
		entities.put(classification, (HashSet<String>) nameMatches(text));

		return entities;
	}
	
	private Set<String> nameMatches(String text) {
		Set<String> names = new HashSet<String>();
		NameFinderME nameFinder = new NameFinderME(personModel);
		String tokens[];
		Span matches[];
		for (String sentence : convertToSentences(text)) {
			tokens = convertToTokens(sentence);
			matches = nameFinder.find(tokens);
			List<String> s = Arrays.asList(Span.spansToStrings(matches, tokens));
			names.addAll(s);
		}
		nameFinder.clearAdaptiveData();
		return names;
	}
	
	private String[] convertToSentences(String text) {
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
		return sentenceDetector.sentDetect(text);
	}
	
	private String[] convertToTokens(String sentence) {
		Tokenizer tokenizer = new TokenizerME(tokenModel);
		return tokenizer.tokenize(sentence);
	}

	@Override
	public String toString() {
		return "Open NLP";
	}

	public String deIdentify(String text) {
		for(String match : nameMatches(text)) {
			String replacement = StringUtils.repeat('X', match.length());
			text = StringUtils.replace(text, match, replacement);
		}
		return text;
	}

}
