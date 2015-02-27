package ner_service.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public final class StringToNerService implements Converter<String, NerService> {

	@Autowired
	private Collection<NerService> nerServices;

    public NerService convert(String name) {
		for (NerService service : nerServices) {
			if (name.equals(service.toString())) {
				return service;
			}
		}
		return null;
	}
    
}
