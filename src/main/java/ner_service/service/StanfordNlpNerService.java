package ner_service.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

@Service
public abstract class StanfordNlpNerService implements NerService {

	private final CRFClassifier<CoreLabel> classifier;

	public StanfordNlpNerService(String serializedClassifier) {
		this.classifier = CRFClassifier.getJarClassifier(serializedClassifier,
				null);
	}

	public synchronized List<List<CoreLabel>> classify(String text) {
		return classifier.classify(text);
	}

	public HashMap<String, HashSet<String>> getEntitiesFor(String text) {
		List<List<CoreLabel>> classify = classify(text);
		HashMap<String, HashSet<String>> entities = new HashMap<String, HashSet<String>>();
		for (List<CoreLabel> coreLabels : classify) {
			for (CoreLabel coreLabel : coreLabels) {
				String word = coreLabel.word();
				String classification = coreLabel
						.get(CoreAnnotations.AnswerAnnotation.class);
				if (!"O".equals(classification)) {
					HashSet<String> words = entities.get(classification);
					if (words == null) {
						words = new HashSet<String>();
					}
					words.add(word);
					entities.put(classification, words);
				}
			}
		}
		return entities;
	}

	public String deIdentify(String text) {
		// FIXME: Yet to implement deidentification of text
		return text;
	}

}
