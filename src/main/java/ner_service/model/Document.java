package ner_service.model;

import java.util.HashMap;
import java.util.HashSet;

import ner_service.service.NerService;

public class Document {

	private String text;
	private HashMap<String, HashSet<String>> entities;
	private String serviceName;
	private NerService service;

	public Document() {
	}

	public Document(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setServiceName(String name) {
		this.serviceName = name;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setService(NerService service) {
		this.service = service;
	}

	public NerService getService() {
		return this.service;
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
