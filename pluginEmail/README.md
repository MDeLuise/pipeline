# pluginEmail
Prefix used: `mail`
## Triggers
<table style='text-align: center'>
<tr>
<th>Identifier</th>
<th>Periodic</th>
<th>Type sent</th>
<th>Options</th>
<th>Description</th>
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
<td>gSend</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>to</td>
<td>String</td>
<td>null</td>
<td>Recipient of the email, separated by comma.</td>
</tr>
<tr>
<td>from</td>
<td>String</td>
<td>null</td>
<td>Sender of the email.</td>
</tr>
<tr>
<td>subject</td>
<td>String</td>
<td>null</td>
<td>Subject of the email.</td>
</tr>
<tr>
<td>text</td>
<td>String</td>
<td>null</td>
<td>Body of the email, if empty use passed triggerOutput.</td>
</tr>
<tr>
<td>clientIdVar</td>
<td>String</td>
<td>null</td>
<td>Global var containing the client id of the application.</td>
</tr>
<tr>
<td>clientSecretVar</td>
<td>String</td>
<td>null</td>
<td>Global var containing the client secret of the application.</td>
</tr>
</table></td>
<td>Send an email using Gmail (see <a href='src/main/resources/gmail-config.md'>configuration</a>).</td>
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
