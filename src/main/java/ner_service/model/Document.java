package ner_service.model;

import java.util.HashMap;
import java.util.HashSet;

import ner_service.service.NerService;

public class Document {

	public String text;
	public HashMap<String, HashSet<String>> entities;
	
	public Document(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
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
