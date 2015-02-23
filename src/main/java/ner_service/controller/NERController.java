package ner_service.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ner_service.service.NerService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

@Controller
public class NERController {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(NERController.class);

	@Autowired
	private NerService nerService;

	@RequestMapping(value = "/entities", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, HashSet<String>> getNamedEntities( @RequestBody String input) {
		List<List<CoreLabel>> classify = nerService.classify(input);
	    HashMap<String, HashSet<String>> results = new HashMap<String, HashSet<String>>();
	    for (List<CoreLabel> coreLabels : classify) {
	    	for (CoreLabel coreLabel : coreLabels) {
	    		String word = coreLabel.word();
	    		String answer = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
	    		if(!"O".equals(answer)){
	    			HashSet<String> existingClassification = results.get(word);
	    			if (existingClassification!=null) {
	    				existingClassification.add(answer);
		    			results.put(word, existingClassification);	    				
	    			} else {
	    				HashSet<String> newSet = new HashSet<String>();
	    				newSet.add(answer);
		    			results.put(word, newSet);	    					    				
	    			}
	    		}
	    	}
	    }
	    return results;
	}

}
