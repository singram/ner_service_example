package ner_service.service;

import org.springframework.stereotype.Service;

@Service
public class StanfordNlpNer3ClassService extends StanfordNlpNerService {

	private static final String serializedClassifier = "/edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";

	public StanfordNlpNer3ClassService() {
		super(serializedClassifier);
	}

}
