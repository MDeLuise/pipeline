# pluginYoutube
Prefix used: `yt`
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
<td>video</td>
<td>true</td>
<td>YoutubeVideo</td>

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
<td>channelId</td>
<td>String</td>
<td>null</td>
<td>List of channels ids to check (separated by comma) (<a href='https://vabs.github.io/youtube-channel-name-converter/'>convert username to id</a>)</td>
</tr>
<tr>
<td>file</td>
<td>String</td>
<td>null</td>
<td>File's path containing channels ids to check (one per line)</td>
</tr>
<tr>
<td>onChange</td>
<td>Boolean</td>
<td>true</td>
<td>if true trigger only on new videos.</td>
</tr>
</table></td>
<td>Send in the pipeline the uploaded videos.</td>
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
</table>

## Filters
<table style='text-align: center'>
<tr>
<th>Identifier</th>
<th>Type used</th>
<th>Options</th>
<th>Description</th>
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
</table>
