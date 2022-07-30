# Pipeline [![emoji-log](https://cdn.rawgit.com/ahmadawais/stuff/ca97874/emoji-log/non-flat-round.svg)](https://github.com/ahmadawais/Emoji-Log/)

<b>Pipeline</b> is an open source expandable project that helps to create a flow of automations defining `trigger`, `action`, and `processor`. 

First, let's start with some simple examples in order to understand what this project does.
* Using the following config file it's possible to print, every 2 seconds for 3 times, the current IP:
  ```
  ---
  - triggers:
      - name: ip
        options:
          onChange: false
          delay: 5
          period: 2
          repeat: 3
    processors:
      - type: action
        name: print
        options:
          text: "### current ip: %s"
  ```
* If we want also to be notifier via telegram bot about the new IP everytime this changes, we can modify the previous config file as following:
  ```
  ---
  - triggers:
      - name: ip
        options:
          period: 5
          repeat: 2
          onChange: false
    processors:
      - type: action
        name: tg.send
        options:
          tokenVar: token
          chatIdVar: chId
      - type: action
        name: print
        options:
          text: "### current ip: %s"
  ```

* What about getting an update of the _stackoverflow_ basic plan price only if this fall off below 12$? It can be done this way:
  ```
  ---
  - triggers:
      - name: element
        options:
          url: https://stackoverflow.com/
          selector: .js-billed-annually
          onChange: false
          period: 10
    processors:
      - type: transformer
        name: regexExt
        options:
          pattern: "\\$\\d+"
      - type: transformer
        name: strToInt
      - type: filter
        name: lt
        options:
          compareValue: 12
      - type: transformer
        name: objToStr
      - type: action
        name: print
        options:
          text: "### Annual billing of stackoverflow basic plan: %s"
  
  ```

* If we are interested in new video from the Apple youtube channel, and we
want to be notifier about every new video both via telegram and email:
  ```
  ---
  - triggers:
      - name: yt.video
        options:
            channelId: UCE_M8A5yxnLfW0KghEeajjw
            stateId: trigger0
    processors:
      - type: transformer
        name: objToStr
      - type: transformer
        name: jsonExt
        options:
            field: url
      - type: action
        name: tg.send
        options:
            tokenVar: token
            chatIdVar: chId
      - type: action
        name: mail.gSend
        options:
          clientIdVar: clId
          clientSecretVar: clSr
          to: foo@bar.com
          subject: new video uploaded!
          text: "view the video here: %s"
          from: my.foo.mail@gmail.com
  ```

The project consist of the following main entities:
* `trigger` - this is the entity that checks if something happened (_e.g. a page changes_) and collects data (_e.g. get a value in the page_).
* `processor` - this is the entity that process the data sent by the `trigger`. It can be of two types:
    * `filter` - check if some required is met, if so continue the pipeline's computation, otherwise do nothing,
    * `transformer` - transform the data in some way (_e.g. converting it to other type_).
* `action` - this is the entity that do something (_e.g. print the received value_).
* `webapi` - this is the entity, used by `triggers` and `action`, representing the interaction with the web (_e.g. a class that send http requests_).

These entities can be combiner together in order to create a `pipeline`.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine not only for use it but also for development and testing purposes.

### Releases
If you want to run a release version of the software, you just needs to have JRE 8 or above installed.
Then:
```
$ cd release/
$ java -jar pipeline-{version}.jar {arguments}
```

### Snapshots
#### Prerequisites

The things you need before installing the software:

* JDK 17 or above
* Maven

#### Installation

In order to compile the project:

```
$ cd {directory-name}
$ mvn clean
$ mvn package
```


#### Usage

In order to run the project:
```
$ java -jar releaseModule/target/releaseModule-1.0-SNAPSHOT.jar -f {config-file-path}
```
The config file used can be a YAML, or a JSON file.

##### Launch script parameters
| Parameter | Description |
| :---: | :---: |
| -f or --from-file | load a config file from disk |
| -i or --inline | use the passed JSON config file |
| -v or --verbose | set the verbosity of the project run |

#### Syntax
The syntax of the config file is the following:
```
---
- triggers:                # list of triggers
    - name: ...            # name of the trigger
      options:             # if present
        ...                # options passed to the trigger
        ...
    ...
  processors:              # list of processor
    - type: ...            # type of processor (can be: action, filter, transformer, filterOperator)
      name: ...            # name of the processor
```
The syntax of filterOperator
```
- type: filterOperator
  name: ...                # name of the filterOperator (can be: and, or, not)
  filters:
    ...                    # list of filterOperators or filters
```
The syntax of processor:
```
- type: ...                # can be filter, transformer or action
  name: ...                # name of the processor
  options:                 # if present
    ...                    # options passed to the filter
```

## More examples
More examples can be found in the plugins folders.

## How to contribute
Fell free to contribute! Just a few useful information below.

### Convention used
These are the convention used inside the project.

#### Commit messages
Please use the emoji log standard in all commit messages.
More info about it can be discovered [here](https://github.com/ahmadawais/Emoji-Log), but a summary of the commit syntax is the following:

| Commit message | Description | Example |
| :---: | :---: | :---: |
| 📦 NEW: _{commit-message}_ | Use when you add something entirely new. | 📦 NEW: Add new triggers |
| 👌 IMPROVE: _{commit-message}_ | Use when you improve/enhance piece of code like refactoring etc. | 👌 IMPROVE: Removed unused action options |
| 🐛 FIX:  _{commit-message}_ | Use when you fix a bug. | 🐛 FIX: Action now works correctly |
| 📖 DOC: _{commit-message}_ | Use when you add documentation. | 📖 DOC: Create trigger documentation |
| 🚀 RELEASE: _{commit-message}_ | Use when you release a new version. | 🚀 RELEASE: Version 2.0.0 |
| 🤖 TEST: _{commit-message}_ | Use when it's related to testing. | 🤖 TEST: Add test for plugin |
| ‼️ BREAKING: _{commit-message}_ | Use when releasing a change that breaks previous versions. | ‼️ BREAKING: Change core interface |

If you want to set the above commit template for the project, in the project root type:
```
$ git config commit.template commit-template.txt
```

#### Branch name
Please use the following branch name standard:

| Name | Description | Example |
| :---: | :---: | :---: |
| feature/_{what}_ | When working on a new feature. | feature/add-telegram-plugin |
| fix/_{what}_ | When working on a fix | fix/action-not-working |
| release/_{version}_ | When working on a release | release/1.0.0 |

### Workflow
The workflow for contributing is the following:
1. create a new branch starting from _develop_ branch _(remember to use the [branch name convention](#branch-name))_,
1. work on that _(remember to use the [commit messages convention](#commit-messages))_,
   * if a new plugin is created remember to:
      * create unit tests for the new classes,
      * create integration tests for the new classes,
      * create the documentation for new classes _(see [here](#documentation))_.
      * provide some pipelines examples
1. create a pull request to develop.

An example of contribution:
```
$ git checkout develop
$ git checkout -b feature/foo-plugin

# create a new plugin using the instruction in the next section

# work on FooTrigger.java, FootriggerFactory.java, ..
$ git add FooTrigger.java FootriggerFactory.java ...
$ git commit -m "📦 NEW: Create triggers for plugin foo"

# work on test for FooTriggerTest.java ...
$ git add FooTriggerTest.java ...
$ git commit -m "🤖 TEST: Create tests for plugin foo triggers"

# work on documentation for FooTrigger.java ...
$ git add README.md ...
$ git commit -m "📖 DOC: Create documentation for new triggers"

$ git push origin feature/foo-plugin
```

### Create a new plugin
Given a name (in the example below _foo_), in order to create a new plugin the first step to take is to install the provided archetype used to generate plugins.
This can be done with the following commands:
```
$ cd build-tools/maven-archetype
$ mvn clean install
```
After a successful installation, you can create a new maven module using the installed archetype via IDE, or running the following command in the project root directory
(substitute all occurrences of _pluginFoo_ and _plugin.foo_ with the given name):
```
$ mvn archetype:generate \
     -DarchetypeGroupId=md.dev \
     -DarchetypeArtifactId=pluginArchetype \
     -DarchetypeVersion=1.0-SNAPSHOT \
     -DgroupId=plugin.foo \
     -DartifactId=pluginFoo \
     -Dversion=1.0-SNAPSHOT \
     -Dpackage=plugin.foo
```
Now the new plugin is created with some example of `triggers`, `actions`, `filters` and `transformers` inside it.
Take a look at the existing classes and read the comments inside them.

The last step consists in adding the new plugin as dependency in the `pom.xml` of the `releaseModule`.

Remember to:
* change the name of the classes according to your plugin name,
* change the name of the generated _foo-plugin-manifest.property_ file with _{name}-plugin-manifest.property_.

#### Structure of the plugins
The following is the structure of the plugin module:
```
pluginFoo/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  ├─ plugin.foo/
│  │  │  │  ├─ action/ ①
│  │  │  │  ├─ modifier/
│  │  │  │  │  ├─ filter/ ②
│  │  │  │  │  ├─ transformer/ ③
│  │  │  │  ├─ trigger/ ④
│  │  │  │  ├─ webapi/ ⑤
│  │  │  │  ├─ PluginFoo.java ⑥
│  │  ├─ resources/
│  │  │  ├─ examples/ ⑦
│  │  │  ├─ foo-plugin-manifest.property ⑧
│  ├─ test/ ⑨
│  │  ├─ java/
│  │  │  ├─ action/
│  │  │  ├─ modifier/
│  │  │  │  ├─ filter/
│  │  │  │  ├─ transformer/
│  │  │  ├─ trigger/
│  │  ├─ resources/
pom.xml
```
① place all the `actions` of the plugin here<br>
② place all the `filters` of the plugin here<br>
③ place all the `transformers` of the plugin here<br>
④ place all the `triggers` of the plugin here<br>
⑤ place all the `webapis` of the plugin here<br>
⑥ this is the class representing the plugin, it defines the factories needed to run the plugin<br>
⑦ place all the plugin examples here<br>
⑧ this is the file used to extract the information about the plugin<br>
⑨ place all the tests here<br>

#### Create a new trigger
The class representing the new `trigger` needs to extends one of the following classes, base on:
* the periodicity of the new `trigger` (i.e. _periodic_ if it's meant to run periodically, and _one time_ if it's meant to run only ones), 
* the interaction with the web (e.g. a `trigger` that send/receive https requests vs. a trigger that read changes in a local file).

| |  Don't interact with web | Interact with web |
| :---: | :---: | :---: |
| <b>Periodic</b> |  `AbstractPeriodicTrigger<T>` | `AbstractPeriodicWebTrigger<T>` |
| <b>One time</b> |  `AbstractOneTimeTrigger<T>` | `AbstractOneTimeWebTrigger<T>` |

The creation of a new `trigger` follow these steps:
1. Create the new class extending the right one chosen from the table above. Where `T` represent the class of
   the value sent in the pipeline by the `trigger`.
1. Override the following methods:
    * `Collection<OptionDescription> acceptedClassOptions()` - _Display which options are loaded by the action_.
    * `void initializeClassOptions()` - _Initialize options with default values_.
    * `void loadInstanceOptions(Options options)` - _Load used options_.
    * `void listen()` - _Perform the purpose of the trigger, set the value that will be sent in the pipeline in the inherited `triggerOutput` object, then call `triggerPipelines()`_.
    * `saveState()` - _Used in order to save the state of the trigger, call the inherited saveProperty() method to save one property_.
    * `loadState()` - _Used in order to load the previous state of the trigger, call the inherited getProperty() method to get one property_.
1. Add the `@Trigger` annotation to the class and provide the needed field for it.

#### Create a new action
The creation of a new `action` follow these steps:
1. Create the new class extending `AbstractAction<T>` (if the `action` is meant to don't interact with the web) or the
   `AbstractWebAction<T>` (if the `action` is meant to interact with the web). Where `T` represent the class of
   the value to receive in the pipeline.
1. Override the following methods:
    * `Collection<? extends OptionDescription> acceptedClassOptions()` - _Display which options are loaded by the action_.
    * `void initializeClassOptions()` - _Initialize options with default values_.
    * `void loadInstanceOptions(Options options)` - _Load used options_.
    * `void doAction(TriggerOutput<Object> triggerOutput)` - _Perform the purpose of the action_.
1. Add the `@Action` annotation to the class and provide the needed field for it.

#### Create a new filter
The creation of a new `filter` follow these steps:
1. Create the new class extending the `AbstractFilter<T>`. Where `T` represent the class of
   the value to receive in the pipeline.
1. Override the following methods:
    * `Collection<? extends OptionDescription> acceptedClassOptions()` - _Display which options are loaded by the action_.
    * `void initializeClassOptions()` - _Initialize options with default values_.
    * `void loadInstanceOptions(Options options)` - _Load used options_.
    * `boolean filter(TriggerOutput<T> triggerOutput)` - _Perform the purpose of the filter_.
1. Add the `@Filter` annotation to the class and provide the needed field for it.

#### Create a new transformer
The creation of a new `transformer` follow these steps:
1. Create the new class extending the `AbstractTransformer<T, E>`. Where `T` represents the class of
   the input value to transform in the pipeline, and `E` represents the output type.
1. Override the following methods:
    * `Collection<? extends OptionDescription> acceptedClassOptions()` - _Display which options are loaded by the action_.
    * `void initializeClassOptions()` - _Initialize options with default values_.
    * `void loadInstanceOptions(Options options)` - _Load used options_.
    * `void transform(TriggerOutput<String> triggerOutput)` - _Perform the purpose of the transformer, modifying directly the passed `triggerOutput` object_.
1. Add the `@Transformer` annotation to the class and provide the needed field for it.

#### Create a new webapi
The creation of a new `webapi` follow these steps:
1. Create the new class extending the `AbstractWebApi` class.
1. Implements the following methods:
    * `WebApiResponse perform()` - _Perform the purpose of the webapi_.
    * `void configure(WebApiConfiguration configuration)` - _Load used options_.
  
Using a `webapi` object, instead of performing the action directly in the `trigger` or `action` object,
made possible to create unit tests for `trigger` and `action` entities.
In fact, doing this way, it's possible to create `triggers` and `actions` that use a mocked `webapi` objects,
thus it's useful in order to perform some check about the entity behaviour. Apart from this, it's also useful
in order to group the same code in one shared class.

#### Plugin annotation
In order to use the plugins entities without names clashes, a class annotated with `@Plugin` needs to be loaded by the `core` of the project.
This class must be placed java sources root (_i.e. in the package plugin.{name}_) and tells the project which prefix use for the created plugin.
The chosen prefix must be unique in the project, and should be:
* easily recognisable,
* short.

For example, given a plugin named _Foo_, the class `FooPlugin` must have the following structure:
```
@Plugin(prefix = "pr")
public class FooPlugin {
}
```

To create any `trigger`, `action`, `filter`, and `transformer` in the _Foo_ plugin, use the name `pr.{entityId}`.

#### Documentation
Every plugin must have a `README.md` file containing a standard documentation about entities created in the plugin.
To generate the standard documentation, first [compile the project](#Installation), and then please use the provided tool as follows:
```
$ java -jar releaseModule/target/releaseModule-1.0-SNAPSHOT.jar -d {created-plugin-manifest-file-path}
# for example: java -jar releaseModule/target/releaseModule-1.0-SNAPSHOT.jar -d pluginBase/src/main/resources/base-plugin-manifest.property
```
The provided tool creates the documentation in the `README.md` file under the plugin module root.

#### Local testing
In order to test new plugins locally it's possible to execute `mvn clean install`.
This command runs for each module: all unit tests, all integration tests and checks if the code match the defined checkstyle for the project.

If some of these phases needs to be disabled consider that:
* passing `-Dcheckstyle.skip` disable the verification of the checkstyle,
* passing `-DskipUnitTests` disable the unit tests,
* passing `-DskipIntegrationTests` disable the integration tests,
* passing `-Dmaven.test.skip=true` disable even the compilation of the unit tests.

Consider also that if only one module is need to be compiled and tested, then it's possible to use the `-pl moduleName` option.

Even if disable locally, all the phases above need to be correctly completed in order to merge a pull request.
Some github action check the phases exit code for every pull request made.

#### Common advises
A few common advise follow:
* If one entity of the plugin needs to read sensitive information (_e.g. passwords, tokens, ..._), it's
better to not require them directly in the options used by the entity. Instead, you can:
  * require a _.property_ file path in which the information will be read,
  * require to store the information in a _global var_ and read them at runtime.
* If sensitive information needs to be saved locally, please use the method
  `saveSensitivePropertyInState` and `getSensitivePropertyInState` of `StateHandler` class
  (asking the user for a password if needed for encryption).


## Currently supported plugins
This is the list of the currently supported plugin:
* [pluginBase](pluginBase/README.md)
* [pluginTelegram](pluginTelegram/README.md)
* [pluginYoutube](pluginYoutube/README.md)
* [pluginEmail](pluginEmail/README.md)



## Future updates
- [x] Script to automatically generate plugin documentation
- [x] Introduce a _state_ for `tiggers`
- [ ] Implements an error handling mechanism
- [ ] Introduce _conditional pipeline_, i.e. the possibility of bifurcate a pipeline on the basis of some switch.
This could be used in this way:
  ```
  ---
  - triggers:
    - name: element
      options:
      url: https://stackoverflow.com/
      selector: .js-billed-annually
      onChange: false
      period: 10
      processors:
    - type: transformer
      name: regexExt
      options:
      pattern: "\\$\\d+"
    - type: pipelineSwitch
      name: intValThr
      value: 100
      less:
        # one pipeline: with some filters, transormers, actions ...
      greater:
        # another pipeline: with different filters, transormers, actions ...
  ```
- [ ] Define piece of pipeline by name, in order to reuse them without multiple copy.
  This could be used in this way:
  ```
  ---
  - definitions:
    - name: extract-num-less-12
      value:
        - type: transformer
          name: regexExt
          options:
            pattern: "\\$\\d+"
        - type: filter
          name: lt
          options:
            compareValue: 12
    - name: two-print
      value:
        - type: action
          name: print
          options:
            prepend: "### print 1: "
        - type: action
          name: print
          options:
            file: ./file-to-use.txt
            prepend: "### print 2: "
      
  
  - triggers:
    - name: element
      options:
      url: https://stackoverflow.com/
      selector: .js-billed-annually
      onChange: false
      period: 10
    processors:
      - type: def
        name: extract-num-less-12
      - type: def
        name: two-print
  
  - triggers:
    - name: ip
      options:
        onChange: false
        delay: 5
        period: 10
    processors:
     - type: def
        name: two-print
  ```
- [ ] Manage the pipeline with a GUI