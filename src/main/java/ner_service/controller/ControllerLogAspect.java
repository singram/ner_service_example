package ner_service.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Component
public class ControllerLogAspect {

	private Logger log = Logger.getLogger(ControllerLogAspect.class);

	@InitBinder
	private void initBinder(WebDataBinder binder, WebRequest webRequest) {
		// log the request and data here .
		log.info(webRequest.toString());
	}

}
