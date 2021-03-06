package ner_service.service;

import org.springframework.stereotype.Service;

@Service
public class StanfordNlpNer7ClassService extends StanfordNlpNerService {

	private static final String serializedClassifier = "english.muc.7class.distsim.crf.ser.gz";

	public StanfordNlpNer7ClassService() {
		super(serializedClassifier);
	}

	public String toString() {
		return "Stanford NLP 7x MUC classifier";
	}

}
