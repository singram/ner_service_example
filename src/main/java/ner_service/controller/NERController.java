package ner_service.controller;

import ner_service.model.Document;
import ner_service.service.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NERController {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(NERController.class);

	@Autowired
//	private StanfordNlpNer7ClassService nerService;
	private RegExNerService nerService;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String documentSumbissionForm(Model model) {
        model.addAttribute("document", new Document());
    	return "index";
    }
	
	@RequestMapping(value = "/entities.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Document getNamedEntities( @RequestBody String input) {
		Document document = new Document(input);
	    document.setEntities(nerService);
	    return document;
	}

	@RequestMapping(value = "/entities", method = RequestMethod.POST)
	public String getNamedEntities(@ModelAttribute Document document, Model model) {
		model.addAttribute("document", document);
	    document.setEntities(nerService);
	    return "document_entities";
	}

}
