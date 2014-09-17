# IVH5 Java worked example - RMI Bibliotheek Informatie Systeem #

Dit project bevat de Java code voor het bibliotheek informatie systeem zoals dat in de periode IVH5 als worked example beschikbaar is.

## Requirements ##
1. Java JDK
3. IDE [Eclipse Luna](http://www.eclipse.org/), NetBeans, [Notepad++](http://www.notepad-plus-plus.org/)
2. Apache Webserver, bv. via XAMPP
1. MySql database

Daarnaast eventueel:

2. [Gradle](http://www.gradle.org/) build automation tool 
3. [GIT](http://www.git-scm.com/) versie beheer software
4. [Sonar](http://www.sonar.com/) code quality tool

## How do I get set up? ##
Een opmerking vooraf: de scripts in het worked example gaan uit van een Windows omgeving. De scripts zijn nog niet getest op Apple MacOS.

1. Installeer Gradle. Voeg het pad naar gradle\bin ook toe aan je Path omgevingsvariable (op Windows).
2. Maak een directory `C:\dev\xampp\htdocs\classes`. Hierin zal gradle de classes zetten die de client en server zullen gebruiken. Java RMI zal de URL 'localhost\classes' nodig hebben. Het kan daarom handig zijn om XAMPP te installeren in directory C:\dev\xampp. Je kunt ook de DocumentRoot aanpassen in Apache httpd.conf.
3. Start MySQL en importeer het IVH5\server\resources\data\library.sql script.

## Running ##
4. Start Apache en de MySQL database server. Via de URL 'localhost\classes' moeten rmiregistry, server en client de classbestanden kunnen bereiken.  
5. Start de rmiregistry via het script `1-start_rmiregistry.bat`.
6. Start de servers via het script `2-startserver.bat`.
7. Start de client via het script `3-startclient.bat`.

### Database configuration ###
Het database script met de structuur en data van de voorbeelddatabase vind je in de map server\resources\data. Het script creeert ook de benodigde gebruiker om vanuit het worked example een verbinding met de database te maken.

### Deployment instructions ###
Voor deployment is het nodig dat de server, client en registry toegang hebben tot de gedeelde classbestanden, en dat de securitysettings correct ingesteld zijn. Toegang tot de gedeelde classes gaat via http. De classes worden, in ons geval, beschikbaar gesteld via een  directory op de webserver die via http gevonden kan worden. In het voorbeeld is dat de map 'classes' direct onder de root van de webserver. Deze directory is van belang, omdat deze door client en server gevonden moet kunnen worden, en omdat beide hier access permissions moeten hebben. Deze directory wordt de codebase genoemd, en komt terug in de instellingen en in de Java code van client en server. Je kunt hier een andere directory kiezen, maar deze moet dan wel in de codebase properties en in de security permissions ingesteld worden.


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
| `properties` | Toont alle properties die Gradle binnen dit project kent. |

Om het project te compileren voer je dus in een command venster uit:

         gradle build

### Java Properties ###
De client en server maken gebruik van Java properties om een aantal belangrijke opstartinstellingen te laden. Deze staan in de bestanden met de uitgang .properties in de resource directories van de server en de client. 

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact