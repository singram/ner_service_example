package ner_service.service;

import java.util.HashMap;
import java.util.HashSet;

public abstract interface NerService {

	public HashMap<String, HashSet<String>> getEntitiesFor(String text);

	public String deIdentify(String text);

	public String toString();

}
