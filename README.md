# Java Bibliotheek Informatie Systeem #

Dit project bevat de Java code voor het bibliotheek informatie systeem dat in periodes IVP3 en IVP4 als worked example voor de studenten beschikbaar is.

## Vereisten ##
1. Java JDK
2. Werkende en draaiende database
3. Eventueel [Eclipse Luna](http://www.eclipse.org/), NetBeans, [Notepad++](http://www.notepad-plus-plus.org/)
4. [Gradle](http://www.gradle.org/) build automation tool 
5. [GIT](http://www.git-scm.com/) versie beheer software

## Gradle ##
Download en installeer Gradle. Je kunt met Gradle werken vanuit Eclipse of NetBeans, maar vanaf de commandline heb je meer controle. 

> Als je op Windows werkt is het handig om het pad naar gradle\bin in je PATH environment 
> variabele op te nemen, via
> My Computer > Properties > Advanced system settings > Environment variables. 
> Je kunt dan direct `gradle` aanroepen.

Handige informatie vind je in de [User Guide](http://www.gradle.org/docs/current/userguide/userguide.html). [Hoofdstuk 7](http://www.gradle.org/docs/current/userguide/tutorial_java_projects.html) behandelt Java projecten.

### Gradle commands ###
In de volgende tabel vind je een overzicht van de belangrijkste Gradle commando's. Gebruik: `gradle command`. 

| Command | Actie                    |
| ------------- | ------------------------------ |
| `build`      | Compileert het project en voert de Junit tests uit.   |
| `test`   | Voert de testcases uit.     |
| `compileJava` | Compileert. |
| `clean` | Verwijdert alle gegenereerde bestanden. |
| `eclipse` | Genereert projectbestanden, zoals .classpath, .project en .settings\, waarmee je dit gradle project kunt openen als Eclipse project. |
| `cleanEclipse` | Verwijdert alle gegenereerde Eclipse bestanden. |
| `showJunit` | Opent het jUnit test rapport in je browser. |
| `showJacoco` | Opent het Jacoco test coverage rapport in je browser. |
| `sonarRunner` | Voert een analyse uit van de codekwaliteit en maakt een Sonar rapport. Zie het gradle.build script voor de properties, en pas deze zo nodig aan. |
| `properties` | Toont alle properties die Gradle binnen dit project kent. |

Om het project te compileren voer je dus in een command venster uit:

	gradle buildJava

## Running the code ###

     java -jar IVP4-1.0.jar

### What is this repository for? ###

* Quick summary
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact