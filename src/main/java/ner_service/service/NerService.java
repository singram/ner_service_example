package ner_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

@Service
public class NerService {
    
    private final String serializedClassifier = "/edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
	private final CRFClassifier<CoreLabel> classifier;
    
    public NerService() {
    	this.classifier = CRFClassifier.getJarClassifier(serializedClassifier, null);
    }
    
    public synchronized List<List<CoreLabel>> classify(String text) {
    	return classifier.classify(text);
    }
 
}
