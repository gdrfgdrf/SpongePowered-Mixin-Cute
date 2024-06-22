SpongePowered-Mixin-Cute
===
__[English](https://github.com/gdrfgdrf/SpongePowered-Mixin-Cute/blob/master/README.md)__ | [简体中文](https://github.com/gdrfgdrf/SpongePowered-Mixin-Cute/blob/master/README_ChineseSimplified.md)
- A bootstrap for the [Mixin](https://github.com/SpongePowered/Mixin) system
- Part of it uses code from Fabric

For Users
------------------------
If the project that uses the project has a new update,  
you can use the "java -jar filename -update" to the jar file  
which has been published by the developer of that project.  
The project will be updated after the command is run.  
After the update, the old local main program file will be overwritten   
as the new main program file.

Getting Started
------------------------
- Latest Version: [![Maven Central](https://img.shields.io/maven-central/v/io.github.gdrfgdrf/spongepowered-mixin-cute.svg)](https://search.maven.org/search?q=g:io.github.gdrfgdrf%20a:spongepowered-mixin-cute)

## There are two scenarios
### Needs to be modified by the Mixin system
The project that needs to use this project in this way  
is referred to as the main program.

You need to add a dependency to your project
```xml
<dependency>
    <groupId>io.github.gdrfgdrf</groupId>
    <artifactId>spongepowered-mixin-cute-launcher</artifactId>
    <version>latest version</version>
    <scope>provided</scope>
</dependency>
```
Note:  
If you want to facilitate the test scope can not be provided,  
But if it is to be released to the public, the scope must be provided.

---

In this case, your project's main class must implement  
io.github.gdrfgdrf.spongepowered.mixin.launcher.base.ProgramProvider.  

You need to create a program_description.json file  
in your project's resource folder.  

If your project supports plugins using the mixin system  

The format of program_description.json needs to be as follows  
```json
{
  "main-class": "A class that implements ProgramProvider",
  "plugin-folder": "The folder where the plugins are stored",
  "plugin-description-file-name": "It says below"
}
```
If your project does not support plugins using the mixin system,  
"plugin-folder" and "plugin-description-file-name" are not required

---

After that, you need to package your project as a jar  
and rename it "program.jar"  

Then download the jar file from the Release page of the project,  
And put program.jar directly into the root of the jar file of this project.

The user needs to run this project's jar file.

### The "plugin-description-file-name"
In the specification,  
the plugin's resource folder must have a file  
with the same name as the value of the "plugin-description-file-name".

The "plugin-description-file-name" file must have the following contents
```json
{
  "mixins": [
    "test.mixins.json"
  ]
}
```
The "mixins" key must store all mixin files for the plugin.

### Needs to use the Mixin system
Because your project works as a plugin for other project,
so you need to add "other project" as a dependency

You need to add the following dependencies to your project
```xml
<dependencies>
    <dependency>
        <groupId>other project's group id</groupId>
        <artifactId>other project's artifact id</artifactId>
        <version>other project's version</version>
    </dependency>

    <dependency>
        <groupId>io.github.gdrfgdrf</groupId>
        <artifactId>spongepowered-mixin-cute-core</artifactId>
        <version>it says below</version>
    </dependency>
</dependencies>
```

### The version of spongepowered-mixin-cute-core
You can use the latest version or the same version as "other project",  
but for stability, the same version as "other project" is recommended.  

Then, you need to follow this [specification](#the-plugin-description-file-name)  
The plugin-description-file-name value is provided by "other project".

Then you can use the Mixin system normally.

---
Again, this project uses code from [Fabric](https://github.com/FabricMC/fabric),  
You need to follow the license of the [Fabric](https://github.com/FabricMC/fabric) project


