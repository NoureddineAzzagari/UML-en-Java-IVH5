# IVH5 Java worked example - RMI Bibliotheek Informatie Systeem #

Dit project bevat de Java code voor het bibliotheek informatie systeem zoals dat in de periode IVH5 als worked example beschikbaar is.

## Requirements ##
1. Java JDK1.8
2. Maven
3. IDE [Eclipse Luna](http://www.eclipse.org/), NetBeans, [SublimeText](http://www.sublimetext.com/)
4. Webserver, bv. Apache via XAMPP
5. MySql database

Daarnaast:

6. [GIT](http://www.git-scm.com/) versie beheer software
7. Jenkins
8. [Sonar](http://www.sonar.com/) code quality tool

## Get the code ##
Download het project, of, beter nog, open een commandvenster en clone dit project:

        git clone https://rschellius@bitbucket.org/AEI-informatica/worked-example.git

Een andere handige optie is om een fork te maken van dit project. Je hebt dan een copie in je eigen repository, waar je rechten hebt om code in te checken. Daarna clone je je eigen fork op dezelfde wijze als hierboven.

## How do I get set up? ##
Een opmerking vooraf: de scripts in het worked example gaan uit van een Windows omgeving. De scripts zijn nog niet getest op Apple MacOS.

1. Installeer Java 1.8, als je dat nog niet gedaan hebt. Voeg het pad naar de Java map toe aan je PATH omgevingsvariabele. Voeg ook de systeemvariabele JAVA_HOME toe; deze wijst naar de map waar Java 1.8 staat. Test dit door in een commandvenster `java -version` uit te voeren.
2. Installeer Maven. Voeg het pad naar maven\bin ook toe aan je PATH omgevingsvariable (op Windows). Test dit door het commando `mvn -version` in een commandvenster uit te voeren.
3. Zorg dat je XAMPP hebt geïnstalleerd. Java RMI gebruikt in ons geval Apache als webserver om code te delen. Alle code komt straks in de map 'IVH5/target' te staan, die we beschikbaar moeten maken via de URL 'localhost/classes'. We voegen daarom een ALIAS toe aan het 'xampp/apache/conf/httpd.conf' bestand. Zoek in dat bestand naar '<IfModule alias_module>' en voeg daaronder toe:

        Alias /classes "C:/dev/workspace/worked-example/IVH5/target"

We moeten ook een Virtual Directory maken. Voeg onderaan 'httpd.conf' toe:

       <Directory "C:/dev/workspace/worked-example/IVH5/target">
              Options All
              AllowOverride All
              Order allow,deny
              Allow from all
              Require all granted
       </Directory>

Let op, pas de directories aan aan je eigen installatie!

4. Start MySQL en importeer het IVH5\server\resources\data\library.sql script.
5. Open een commandvenster en navigeer naar de map 'worked-example\IVH5' via `cd <pad naar IVH5>`.
6. Bouw de code via `mvn package`. In de map IVH5 moet nu een map 'target' beschikbaar zijn met alle JARs, dependencies en resources.

## Running ##
4. Start Apache en de MySQL database server. Via de URL 'localhost\classes' moeten rmiregistry, server en client de classbestanden kunnen bereiken. Controleer dus in de browser dat je daar de JARs, dependencies en resources kunt zien.
5. Start de rmiregistry via het script `1-start_rmiregistry.bat`.
6. Start de servers via het script `2-startserver.bat`.
7. Start de client via het script `3-startclient.bat`.

## Additional information ##
### Database configuration ###
Het database script met de structuur en data van de voorbeelddatabase vind je in de map server\resources\data. Het script creeert ook de benodigde gebruiker om vanuit het worked example een verbinding met de database te maken.

### Deployment instructions ###
Voor deployment is het nodig dat de server, client en registry toegang hebben tot de gedeelde classbestanden, en dat de securitysettings correct ingesteld zijn. Toegang tot de gedeelde classes gaat via http. De classes worden, in ons geval, beschikbaar gesteld via een  directory op de webserver die via http gevonden kan worden. In het voorbeeld is dat de map 'classes' direct onder de root van de webserver. Deze directory is van belang, omdat deze door client en server gevonden moet kunnen worden, en omdat beide hier access permissions moeten hebben. Deze directory wordt de codebase genoemd, en komt terug in de instellingen en in de Java code van client en server. Je kunt hier een andere directory kiezen, maar deze moet dan wel in alle codebase properties en in de security permissions ingesteld worden. Het handigste is om deze te laten zoals ze zijn ingesteld.

### Maven ###
Je kunt met Maven werken vanuit Eclipse of NetBeans, maar vanaf de commandline heb je meer controle. 

> Als je op Windows werkt is het handig om het pad naar maven\bin in je PATH environment 
> variabele op te nemen, via
> My Computer > Properties > Advanced system settings > Environment variables. 
> Je kunt dan vanaf de commandline direct `mvn` aanroepen.

### Maven commands ###
In de volgende tabel vind je een overzicht van de belangrijkste Gradle commando's. Gebruik: `gradle command`. 

| Command | Actie                    |
| ------------- | ------------------------------ |
| `clean` | Verwijdert alle gegenereerde bestanden. |
| `compile`   | Compileert de Java code en genereert classbestanden.     |
| `package` | Compileert de code, genereert JARs, én kopiëert ze naar de juiste locatie voor RMI. |

Om het project te compileren voer je dus in een command venster uit:

         mvn compile

Om het project te compileren én alle benodigde bestanden beschikbaar te maken:

         mvn package

### Java Properties ###
De client en server maken gebruik van Java properties om een aantal belangrijke opstartinstellingen te laden. Deze staan in de bestanden met de uitgang .properties in de resource directories van de server en de client. 

## Who do I talk to? ##

* Repo owner or admin
* De Java docenten in IVH5
* Je klasgenoten

Have fun!