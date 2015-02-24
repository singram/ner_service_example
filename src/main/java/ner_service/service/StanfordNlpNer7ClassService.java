package ner_service.service;

import org.springframework.stereotype.Service;

@Service
public class StanfordNlpNer7ClassService extends StanfordNlpNerService {

	private static final String serializedClassifier = "/edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";

	public StanfordNlpNer7ClassService() {
		super(serializedClassifier);
	}

}
