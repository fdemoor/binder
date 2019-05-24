#!/bin/bash

OPT="/tmp"
CONFIG="src/main/resources/default_config.yml"

java -DOPT="$OPT" -jar "target/binder-0.0.1-SNAPSHOT.jar" --config="$CONFIG"
