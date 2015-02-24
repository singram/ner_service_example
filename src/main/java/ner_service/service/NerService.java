package ner_service.service;

import java.util.HashMap;
import java.util.HashSet;

public interface NerService {

	public HashMap<String, HashSet<String>> getEntitiesFor(String text);
}
