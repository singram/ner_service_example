package ner_service.model;

import java.util.HashMap;
import java.util.HashSet;

import ner_service.service.NerService;

public class Document {

	public String text;
	public HashMap<String, HashSet<String>> entities;
	public String serviceName;
	
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
	
	public void setEntities(HashMap<String, HashSet<String>> entities) {
		this.entities = entities;
	}

	public void setEntities(NerService service) {
	    this.setEntities(service.getEntitiesFor(this.getText()));
	}
	
	public HashMap<String, HashSet<String>> getEntities() {
		return this.entities;
	}
	

}
