#!/usr/bin/env bash

allJars=`find . -name '*.jar' -not -path './target/*' -not -iregex './plugin.*/.*log.*' -a -not -iregex './releaseModule.*' | tr "\n" ":"`;
java -classpath $allJars md.dev.Application $@
