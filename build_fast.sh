#!/bin/bash

# Fast builder for Piranha. Excludes tests and test modules specifically incompatible with fast building.

mvn clean install -T8 -DskipTests -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -pl -:piranha-test-server,-:piranha-test-server-slim

