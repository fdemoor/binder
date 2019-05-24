#!/bin/bash

mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile=/home/lib/mahout/mahout-mr-0.13.1-SNAPSHOT.jar \
  -DgroupId=org.apache \
  -DartifactId=mahout.mr \
  -Dversion=0.13.1.1 \
  -Dpackaging=jar \
  -DlocalRepositoryPath=lib

mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile=/home/lib/mahout/mahout-math-0.13.1-SNAPSHOT.jar \
  -DgroupId=org.apache \
  -DartifactId=mahout.math \
  -Dversion=0.13.1.1 \
  -Dpackaging=jar \
  -DlocalRepositoryPath=lib

mvn dependency:purge-local-repository
