## How to configure the plugin (gmail version)
Since gmail uses <a href="https://developers.google.com/identity/protocols/oauth2">OAuth 2.0</a> protocol to authenticate applications,
a few configurations steps are needed the first time the plugin is used:
1. Register a new application in the <a href="https://console.developers.google.com/">Google API Console</a>.
   * allow all gmail APIs in the created application
1. When the app run, follow the instruction in the stdout in order to grant access to gmail account.