#!/usr/bin/env bash

allJars=`find . -name '*.jar' | tr "\n" ":"`;
java -classpath $allJars md.dev.Application -d $1
