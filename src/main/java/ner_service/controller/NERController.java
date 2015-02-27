package ner_service.controller;

import java.io.IOException;

import ner_service.model.Document;
import ner_service.model.SampleText;
import ner_service.service.NerService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NERController {

	// http://stackoverflow.com/questions/16514501/spring-3-form-with-2-buttons-sending-2-parameters-to-controller-method

	@Autowired
	private Document document;
	
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(NERController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String documentSumbissionForm(Model model) {
		model.addAttribute("document", document);
		model.addAttribute("hasSamples", SampleText.hasSamples());
		model.addAttribute("firstSample", SampleText.firstName());
		return "index";
	}

	@RequestMapping(value = "/entities.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Document getNamedEntities(@RequestBody String input) {
		Document document = new Document(input);
		return document;
	}

	@RequestMapping(value = "/entities", method = RequestMethod.POST)
	public String getNamedEntitiesPOST(@ModelAttribute Document document,
			Model model, @RequestParam(required = false) final String sample) throws IOException {
		model.addAttribute("document", document);
		if (sample!=null)
		{
			document.setText(SampleText.textFor(sample));
			model.addAttribute("prevSample", SampleText.previousName(sample));
			model.addAttribute("nextSample", SampleText.nextName(sample));
		}
		return "document_entities";
	}

	@RequestMapping(value = "/entities", method = RequestMethod.GET)
	public String getNamedEntitiesGET(Model model, @RequestParam final String sample, @RequestParam("service") NerService service) throws IOException {
		document.setService(service);
		document.setText(SampleText.textFor(sample));
		model.addAttribute("document", document);
		model.addAttribute("prevSample", SampleText.previousName(sample));
		model.addAttribute("nextSample", SampleText.nextName(sample));
		return "document_entities";
	}
	
}
