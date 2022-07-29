#!/usr/bin/env bash

commitsMessages=`curl -H "Accept: application/vnd.github.v3+json" \
  https://api.github.com/repos/$1/$2/pulls/$3/commits | \
  grep -o '"message": "[^"]*' | grep -o '[^"]*$'`
illegalCommits=`echo $commitsMessages | \
                  grep -v "^📦 NEW: \|^👌 IMPROVE: \|^🤖 TEST: \|^🐛 FIX: \|^📖 DOC: \|^‼️ BREAKING: \|^🚀 RELEASE: "`
if [ $? -eq 0 ]; then
  echo "illegal commits:"
  echo "$illegalCommits"
  exit 1
fi