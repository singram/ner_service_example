package ner_service.service;

import org.springframework.stereotype.Service;

@Service
public class StanfordNlpNer4ClassService extends StanfordNlpNerService {

	private static final String serializedClassifier = "english.conll.4class.distsim.crf.ser.gz";

	public StanfordNlpNer4ClassService() {
		super(serializedClassifier);
	}

	public String toString() {
		return "Stanford NLP 4x CONLL classifier";
	}

}
