# pluginTelegram
Prefix used: `tg`
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
<td>receive</td>
<td>true</td>
<td>TelegramUpdate</td>

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
<td>tokenVar</td>
<td>String</td>
<td>null</td>
<td>Name of the global var containing the bot access token.</td>
</tr>
<tr>
<td>chatId</td>
<td>Long</td>
<td>null</td>
<td>Filter messages only sent by this chatId.</td>
</tr>
</table></td>
<td>Send in the pipeline telegram messages received by the bot (see <a href='src/main/resources/config.md'>configuration</a>).</td>
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
<td>send</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
<tr>
<td>tokenVar</td>
<td>String</td>
<td>null</td>
<td>Name of the global var containing the bot access token.</td>
</tr>
<tr>
<td>chatIdVar</td>
<td>String</td>
<td>null</td>
<td>Name of the global var containing the chat id to whom the bot will sent messages.</td>
</tr>
</table></td>
<td>Send telegram messages via bot (see <a href='src/main/resources/config.md'>configuration</a>).</td>
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
<tr>
<td>upToStr</td>
<td>TelegramUpdate</td>
<td>String</td>

<td><table style='text-align: center'>
<tr>
<th>Name</th>
<th>Type</th>
<th>Default</th>
<th>Description</th>
</tr>
</table></td>
<td>Convert a TelegramUpdate object to String.</td>
</tr>
</table>
