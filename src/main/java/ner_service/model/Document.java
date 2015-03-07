package ner_service.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import ner_service.service.NerService;
import ner_service.service.StringToNerServiceConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//http://stackoverflow.com/questions/19896870/why-is-my-spring-autowired-field-null
@Component
public class Document {

	@Autowired
	private Collection<NerService> nerServices;

	@Autowired
	@Qualifier("openNlpNerService")
	private NerService defaultService;

	@Autowired
	StringToNerServiceConverter stringToNerService;

	private String text;
	private HashMap<String, HashSet<String>> entities;
	private NerService service;

	public Document() {
	}

	public Document(NerService service) {
		this.service = service;
	}

	public Document(String text) {
		this.text = text;
	}

	public String getText() {
		return cleanText();
	}

	private String cleanText() {
		String text = null;
		if (this.text != null) {
			text = this.text.replaceAll("bmk", "");
			text = this.text.replaceAll("Courier New;Arial;Symbol;;;", "");
		}
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.entities = null;
	}

	public void setService(NerService service) {
		this.service = service;
	}

	public void setService(String name) {
		setService(stringToNerService.convert(name));
	}

	public NerService getService() {
		if (this.service == null) {
			setService(defaultService);
		}
		return this.service;
	}

	public Collection<NerService> getServices() {
		return this.nerServices;
	}

	public String getDeIdentifiedText() {
		return getService().deIdentify(this.getText());
	}

	public HashMap<String, HashSet<String>> getEntities() {
		if (this.entities == null) {
			this.entities = getService().getEntitiesFor(this.getText());
		}
		return this.entities;
	}

}
