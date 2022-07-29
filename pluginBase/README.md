# pluginBase
Prefix used: ` `(_no prefix_)
## Triggers
<table style='text-align: center'>
<tr>
<th>Identifier</th>
<th>Periodic</th>
<th>Type sent</th>
<th>Options</th>
<th>Description</th>
</tr>
<tr>
<td>echo</td>
<td>true</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>delay</td>
<td>Integer</td>
<td>0</td>
<td>Delay the start of the trigger by the given amount of seconds.</td>
</tr>
<tr>
<td>stateId</td>
<td>String</td>
<td>null</td>
<td>ID of the state to use in the trigger.</td>
</tr>
<tr>
<td>period</td>
<td>Integer</td>
<td>5</td>
<td>Perform action every given amount of seconds.</td>
</tr>
<tr>
<td>repeat</td>
<td>Integer</td>
<td>null</td>
<td>Perform action given amount of time.</td>
</tr>
<tr>
<td>echo</td>
<td>String</td>
<td>default echo message</td>
<td>value to send when triggered.</td>
</tr>
</table></td>
<td>Send a given String in the pipeline.</td>
</tr>
<tr>
<td>element</td>
<td>true</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>delay</td>
<td>Integer</td>
<td>0</td>
<td>Delay the start of the trigger by the given amount of seconds.</td>
</tr>
<tr>
<td>stateId</td>
<td>String</td>
<td>null</td>
<td>ID of the state to use in the trigger.</td>
</tr>
<tr>
<td>period</td>
<td>Integer</td>
<td>5</td>
<td>Perform action every given amount of seconds.</td>
</tr>
<tr>
<td>repeat</td>
<td>Integer</td>
<td>null</td>
<td>Perform action given amount of time.</td>
</tr>
<tr>
<td>url</td>
<td>String</td>
<td>null</td>
<td>URL to check for changes.</td>
</tr>
<tr>
<td>onChange</td>
<td>Boolean</td>
<td>true</td>
<td>if true trigger only on element change.</td>
</tr>
<tr>
<td>selector</td>
<td>String</td>
<td>null</td>
<td>value used to select the element in the page (<a href='https://jsoup.org/cookbook/extracting-data/selector'>syntax</a>).</td>
</tr>
</table></td>
<td>Send in the pipeline the element selected from an html page.</td>
</tr>
<tr>
<td>ip</td>
<td>true</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>delay</td>
<td>Integer</td>
<td>0</td>
<td>Delay the start of the trigger by the given amount of seconds.</td>
</tr>
<tr>
<td>stateId</td>
<td>String</td>
<td>null</td>
<td>ID of the state to use in the trigger.</td>
</tr>
<tr>
<td>period</td>
<td>Integer</td>
<td>5</td>
<td>Perform action every given amount of seconds.</td>
</tr>
<tr>
<td>repeat</td>
<td>Integer</td>
<td>null</td>
<td>Perform action given amount of time.</td>
</tr>
<tr>
<td>onChange</td>
<td>Boolean</td>
<td>true</td>
<td>if true trigger only on ip change.</td>
</tr>
</table></td>
<td>Send the current IP in the pipeline.</td>
</tr>
<tr>
<td>oneTrue</td>
<td>false</td>
<td>Boolean</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>delay</td>
<td>Integer</td>
<td>0</td>
<td>Delay the start of the trigger by the given amount of seconds.</td>
</tr>
<tr>
<td>stateId</td>
<td>String</td>
<td>null</td>
<td>ID of the state to use in the trigger.</td>
</tr>
</table></td>
<td>Send one single True value in the pipeline.</td>
</tr>
<tr>
<td>page</td>
<td>true</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>delay</td>
<td>Integer</td>
<td>0</td>
<td>Delay the start of the trigger by the given amount of seconds.</td>
</tr>
<tr>
<td>stateId</td>
<td>String</td>
<td>null</td>
<td>ID of the state to use in the trigger.</td>
</tr>
<tr>
<td>period</td>
<td>Integer</td>
<td>5</td>
<td>Perform action every given amount of seconds.</td>
</tr>
<tr>
<td>repeat</td>
<td>Integer</td>
<td>null</td>
<td>Perform action given amount of time.</td>
</tr>
<tr>
<td>url</td>
<td>String</td>
<td>null</td>
<td>URL to check for changes.</td>
</tr>
<tr>
<td>onChange</td>
<td>Boolean</td>
<td>true</td>
<td>if true trigger only on page change.</td>
</tr>
</table></td>
<td>Send hash of one web page in the pipeline.</td>
</tr>
<tr>
<td>perTrue</td>
<td>true</td>
<td>Boolean</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>delay</td>
<td>Integer</td>
<td>0</td>
<td>Delay the start of the trigger by the given amount of seconds.</td>
</tr>
<tr>
<td>stateId</td>
<td>String</td>
<td>null</td>
<td>ID of the state to use in the trigger.</td>
</tr>
<tr>
<td>period</td>
<td>Integer</td>
<td>5</td>
<td>Perform action every given amount of seconds.</td>
</tr>
<tr>
<td>repeat</td>
<td>Integer</td>
<td>null</td>
<td>Perform action given amount of time.</td>
</tr>
</table></td>
<td>Send True values in the pipeline.</td>
</tr>
<tr>
<td>rand</td>
<td>true</td>
<td>Integer</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>delay</td>
<td>Integer</td>
<td>0</td>
<td>Delay the start of the trigger by the given amount of seconds.</td>
</tr>
<tr>
<td>stateId</td>
<td>String</td>
<td>null</td>
<td>ID of the state to use in the trigger.</td>
</tr>
<tr>
<td>period</td>
<td>Integer</td>
<td>5</td>
<td>Perform action every given amount of seconds.</td>
</tr>
<tr>
<td>repeat</td>
<td>Integer</td>
<td>null</td>
<td>Perform action given amount of time.</td>
</tr>
<tr>
<td>min</td>
<td>Integer</td>
<td>0</td>
<td>minimum random value.</td>
</tr>
<tr>
<td>max</td>
<td>Integer</td>
<td>100</td>
<td>maximum random value.</td>
</tr>
</table></td>
<td>Send random number in the pipeline.</td>
</tr>
</table>

## Actions
<table style='text-align: center'>
<tr>
<th>Identifier</th>
<th>Type used</th>
<th>Options</th>
<th>Description</th>
</tr>
<tr>
<td>bScript</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>script</td>
<td>String</td>
<td></td>
<td>Path of the script to run.</td>
</tr>
<tr>
<td>args</td>
<td>String</td>
<td></td>
<td>Parameters passed to the script.</td>
</tr>
</table></td>
<td>Run a local bash script and print in the stdout.</td>
</tr>
<tr>
<td>print</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>text</td>
<td>String</td>
<td>null</td>
<td>If not null print this text, otherwise print the action's input.</td>
</tr>
<tr>
<td>filePath</td>
<td>String</td>
<td>null</td>
<td>If not null print to given file.</td>
</tr>
</table></td>
<td>Print string on stdout or in a file.</td>
</tr>
</table>

## Filters
<table style='text-align: center'>
<tr>
<th>Identifier</th>
<th>Type used</th>
<th>Options</th>
<th>Description</th>
</tr>
<tr>
<td>eq</td>
<td>Comparable</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>compareValue</td>
<td>Comparable</td>
<td>null</td>
<td>Value to compare the filter with.</td>
</tr>
</table></td>
<td>Filter out triggerOutput with value not equals to the given one.</td>
</tr>
<tr>
<td>gt</td>
<td>Comparable</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>compareValue</td>
<td>Comparable</td>
<td>null</td>
<td>Value to compare the filter with.</td>
</tr>
</table></td>
<td>Filter out triggerOutput with value less then the given one.</td>
</tr>
<tr>
<td>lt</td>
<td>Comparable</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>compareValue</td>
<td>Comparable</td>
<td>null</td>
<td>Value to compare the filter with.</td>
</tr>
</table></td>
<td>Filter out triggerOutput with value greater then the given one.</td>
</tr>
</table>

## Transformers
<table style='text-align: center'>
<tr>
<th>Identifier</th>
<th>Input type</th>
<th>Output type</th>
<th>Options</th>
<th>Description</th>
</tr>
<tr>
<td>intThr</td>
<td>Integer</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>compareValue</td>
<td>Integer</td>
<td>0</td>
<td>Value to compare with.</td>
</tr>
<tr>
<td>lt</td>
<td>String</td>
<td>value %s less than threshold</td>
<td>String to use if value is less than compareValue.</td>
</tr>
<tr>
<td>gt</td>
<td>String</td>
<td>value %s greater than threshold</td>
<td>String to use if value is greater than compareValue.</td>
</tr>
<tr>
<td>eq</td>
<td>String</td>
<td>value %s equals than threshold</td>
<td>String to use if value is equals than compareValue.</td>
</tr>
</table></td>
<td>Transform an Integer into a String, depending on the value of the integer.</td>
</tr>
<tr>
<td>jsonExt</td>
<td>String</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>field</td>
<td>String</td>
<td></td>
<td>field to extract (separate by space for inner extraction).</td>
</tr>
</table></td>
<td>Extract a field from a json string.</td>
</tr>
<tr>
<td>objToStr</td>
<td>Object</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
</table></td>
<td>Transforms an object into a string calling toString() method.</td>
</tr>
<tr>
<td>regexExt</td>
<td>String</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>pattern</td>
<td>String</td>
<td>.*</td>
<td>Pattern to use to match regex.</td>
</tr>
</table></td>
<td>Extract matching regex from a string.</td>
</tr>
<tr>
<td>strToInt</td>
<td>String</td>
<td>Integer</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
</table></td>
<td>Transform a string into Integer (useful used previous to a compare filter).</td>
</tr>
</table>
