# IVH5 Java worked example - RMI Bibliotheek Informatie Systeem #

Dit project bevat de Java code voor het bibliotheek informatie systeem zoals dat in de periode IVH5 als worked example voor de studenten beschikbaar is.

## Requirements ##
1. Java JDK
3. IDE [Eclipse Luna](http://www.eclipse.org/), NetBeans, [Notepad++](http://www.notepad-plus-plus.org/)
2. Webserver
1. MySql database

Daarnaast eventueel:

2. [Gradle](http://www.gradle.org/) build automation tool 
3. [GIT](http://www.git-scm.com/) versie beheer software
4. [Sonar](http://www.sonar.com/) code quality tool

## How do I get set up? ##
1. Installeer Gradle. Voeg het pad naar gradle\bin ook toe aan je Path omgevingsvariable (op Windows).
2. Maak een directory `C:\dev\xampp\htdocs\classes`. 
3. Open een command venster en run, vanuit directory worked-example\ivh5, het commando `gradle`.
4. Start de webserver en de MySQL database. 
5. Start de rmiregistry via het script `1-start_rmiregistry.bat`.
6. Start de servers via het script `2-startserver.bat`.
7. Start de client via het script `3-startclient.bat`.

### Dependencies ###
1. log4j logger
2. junit
3. mysql driver
4. xmldom

Wanneer je gebruik maakt van gradle worden de dependencies automatisch uit de Maven Central repository opgehaald.
### Database configuration ###
Het database script met de structuur en data van de voorbeelddatabase vind je in de map server\resources\data. Het script creeert ook de benodigde gebruiker om vanuit het worked example een verbinding met de database te maken.
### Deployment instructions ###
Voor deployment is het nodig dat de server, client en registry toegang hebben tot de gedeelde classbestanden, en dat de securitysettings correct ingesteld zijn. Toegang tot de gedeelde classes gaat via http. De classes worden, in ons geval, beschikbaar gesteld via een  directory op de webserver die via http gevonden kan worden. In het voorbeeld is dat de map 'classes' direct onder de root van de webserver. Deze directory is van belang, omdat deze door client en server gevonden moet kunnen worden, en omdat beide hier access permissions moeten hebben. Deze directory wordt de codebase genoemd, en komt terug in de instellingen en in de Java code van client en server. Je kunt hier een andere directory kiezen, maar deze moet dan wel in de codebase properties en in de security permissions ingesteld worden.

## Running the code ##
Er zijn een aantal vereisten waaraan voldaan moet zijn voordat een Java RMI server en client gestart kunnen worden en met elkaar kunnen communiceren. Een gedetailleerde handleiding voor het werken met Java RMI wordt nog beschikbaar gesteld. Een aantal korte richtlijnen vind je hieronder.

1. Start de webserver. In dit voorbeeld wisselen de server en client hun runnable code uit via http. De classbestanden die de server en client met elkaar delen moeten beschikbaar zijn in de map classes.

	`start apache`	// of een andere vergelijkbare webserver

2. Start de Java RMI registry. Deze vind je in de bin-directory van je JDK.

	`rmiregistry -J-Djava.rmi.server.codebase="http://localhost/classes/bin/"`

3. Start de server.

	`java -cp .\bin;.\libs\log4j-1.2.17.jar;.\libs\mysql-connector-java-5.1.32.jar edu.avans.aei.ivh5.model.main.LibraryServer -properties resources/breda.properties`

4. Start de client.

	`java -cp .\client\build\deploy;.\client\build\deploy\log4j-1.2.17.jar edu.avans.aei.ivh5.view.main.LibraryClient -properties ./client/resources/client.properties`

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

	`gradle buildJava`

### Java Properties ###
De client en server maken gebruik van Java properties om een aantal belangrijke opstartinstellingen te laden. Deze staan in de bestanden met de uitgang .properties in de resource directories van de server en de client. 

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact