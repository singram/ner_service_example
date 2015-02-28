# ner_service_example

# What
A simple web service to exercise the qualities & characteristics of various NER (Named Entity Recognition) detection approaches with the goal of identifying and removing individually identifying peices of data from an unstructured data source.

## Dependencies

- Java SDK 1.7+

# Installation instructions

1. clone repository

  ```
git@github.com:singram/ner_service_example.git
cd ner_service_example
  ```

2. Start webservice

 All dependancies will be automatically downloaded via the gradle tool.

 ```
.\gradlew clean bootRun
 ```

4. Open up a web browser and navigate to http://localhost:8080

5. Sample corpus (optional)

 Optionally put your sample documents as individual files in the ./sample_text directory in the root of the project.  The service will pick up the presence of samples and offer you the option to navigate through them easily so that you can visualize and trial the quality of each approach.

# TODO
 - Cleanup interface
 - Correct controller logging
 - Train NER engines on document corpus
 - Add dictionaries to reduce false positive occurance
 - Perform quantative study of results

## References

 - http://en.wikipedia.org/wiki/Named-entity_recognition
 - http://nlp.stanford.edu/
 - http://opennlp.apache.org/documentation/manual/opennlp.html
 - https://code.google.com/p/dkpro-core-asl/wiki/StanfordCoreComponents
 - https://www.cs.cmu.edu/~rcwang/papers/emnlp-2005.pdf
 - http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 - http://www.regexplanet.com/advanced/java/index.html
 - http://www.bytemining.com/2010/10/lists-of-english-words/

## Spring resources I've found useful

 - http://stackoverflow.com/questions/19896870/why-is-my-spring-autowired-field-null
 - http://www.thymeleaf.org/doc/articles/standardurlsyntax.html
 - http://docs.spring.io/spring/docs/current/spring-framework-reference/html/validation.html

