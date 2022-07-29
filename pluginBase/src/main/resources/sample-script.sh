#!/usr/bin/env bash

printf "%s\n" "Arguments passed to the script: $@"
printf "%s" "# Quote of the day: "
curl https://quotes.rest/qod -s | grep -o '"quote": "[^"]*' | grep -o '[^"]*$'
