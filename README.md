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

## References

 - http://en.wikipedia.org/wiki/Named-entity_recognition
 - http://nlp.stanford.edu/
 - http://opennlp.apache.org/documentation/manual/opennlp.html
 - https://code.google.com/p/dkpro-core-asl/wiki/StanfordCoreComponents
 - https://www.cs.cmu.edu/~rcwang/papers/emnlp-2005.pdf
 - http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 - http://www.regexplanet.com/advanced/java/index.html
