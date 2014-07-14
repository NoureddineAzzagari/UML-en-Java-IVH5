# IVH5 Java RMI Bibliotheek Informatie Systeem #

Dit project bevat de Java code voor het bibliotheek informatie systeem zoals dat in de periode IVH5 als worked example voor de studenten beschikbaar is.

## Requirements ##
1. Java JDK
2. RMI registry
3. IDE [Eclipse Luna](http://www.eclipse.org/), NetBeans, [Notepad++](http://www.notepad-plus-plus.org/)
4. Een draaiende webserver, zoals Apache httpd.

Daarnaast eventueel:
2. Draaiende database met voorbeelddata, indien de server hier gebruik van maakt
4. [Gradle](http://www.gradle.org/) build automation tool 
5. [GIT](http://www.git-scm.com/) versie beheer software
6. [Sonar](http://www.sonar.com/) code quality tool

## How do I get set up? ##

* Summary of set up
* Configuration
* Dependencies
log4j logger
junit
mysql drivers
xmldom jar
* Database configuration
* How to run tests
* Deployment instructions
Voor deployment is het nodig dat de server, client en registry toegang hebben tot de gedeelde classbestanden, en dat de securitysettings correct ingesteld zijn. Toegang tot de gedeelde classes gaat via http. De classes worden, in ons geval, beschikbaar gesteld via een  directory op de webserver die via http gevonden kan worden. In het voorbeeld is dat de map 'classes' direct onder de root van de webserver. Deze directory is van belang, omdat deze door client en server gevonden moet kunnen worden, en omdat beide hier access permissions moeten hebben. Deze directory wordt de codebase genoemd, en komt terug in de instellingen en in de Java code van client en server. Je kunt hier een andere directory kiezen, maar deze moet dan wel in de codebase properties en in de security permissions ingesteld worden.

## Running the code ##
Er zijn een aantal vereisten waaraan voldaan moet zijn voordat een Java RMI server en client gestart kunnen worden en met elkaar kunnen communiceren. Een gedetailleerde handleiding voor het werken met Java RMI wordt nog beschikbaar gesteld. Een aantal korte richtlijnen vind je hieronder.

Start de webserver. In dit voorbeeld wisselen de server en client hun runnable code uit via http. De classbestanden die de server en client met elkaar delen moeten beschikbaar zijn in de map classes.

	start apache	// of een andere vergelijkbare webserver

Start de Java RMI registry. Deze vind je in de bin-directory van je JDK.

	rmiregistry -J-Djava.rmi.server.codebase="http://localhost/classes/"

Start de server.

	java -cp \dev\xampp\htdocs\classes;\dev\xampp\htdocs\jar\log4j-1.2.17.jar library.main.LibraryServer

Start de client.

	java -cp .\build\libs\client-1.0.jar;..\api\build\libs\api-1.0.jar;..\shared\build\libs\shared-1.0.jar;\dev\xampp\htdocs\jar\log4j-1.2.17.jar library.main.LibraryClient

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
| `properties` | Toont alle properties die Gradle binnen dit project kent. |

Om het project te compileren voer je dus in een command venster uit:

	gradle buildJava

### What is this repository for? ###

* Quick summary
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)


### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact