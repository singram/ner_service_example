package ner_service.controller;

import java.util.Collection;

import ner_service.model.Document;
import ner_service.service.NerService;

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

	// http://stackoverflow.com/questions/16514501/spring-3-form-with-2-buttons-sending-2-parameters-to-controller-method

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(NERController.class);

	@Autowired
	private Collection<NerService> nerServices;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String documentSumbissionForm(Model model) {
		model.addAttribute("document", new Document());
		model.addAttribute("services", nerServices);
		return "index";
	}

	@RequestMapping(value = "/entities.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Document getNamedEntities(@RequestBody String input) {
		Document document = new Document(input);
		setService(document);
		return document;
	}

	@RequestMapping(value = "/entities", method = RequestMethod.POST)
	public String getNamedEntities(@ModelAttribute Document document,
			Model model) {
		model.addAttribute("document", document);
		setService(document);
		return "document_entities";
	}

	private boolean setService(Document document) {
		for (NerService service : nerServices) {
			if (document.getServiceName().equals(service.toString())) {
				document.setService(service);
			}
		}
		return (document.getService() != null);
	}

}
