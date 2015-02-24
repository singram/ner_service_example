package ner_service.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

@Service
public class StanfordNlpNerService implements NerService{
    
    private final String serializedClassifier = "/edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
	private final CRFClassifier<CoreLabel> classifier;
    
    public StanfordNlpNerService() {
    	this.classifier = CRFClassifier.getJarClassifier(serializedClassifier, null);
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
	    		String answer = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
	    		if(!"O".equals(answer)){
	    			HashSet<String> existingClassification = entities.get(word);
	    			if (existingClassification!=null) {
	    				existingClassification.add(answer);
		    			entities.put(word, existingClassification);
	    			} else {
	    				HashSet<String> newSet = new HashSet<String>();
	    				newSet.add(answer);
		    			entities.put(word, newSet);
	    			}
	    		}
	    	}
	    }
	    return entities;
    }
}
