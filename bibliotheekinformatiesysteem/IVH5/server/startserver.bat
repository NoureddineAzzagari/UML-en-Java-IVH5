::
:: Batchbestand voor het starten van de IVH5 LibraryServer.
:: Je start de server het handigst vanuit de directory waar de webserver
:: de classes kan vinden. Pas deze zo nodig hieronder aan.
::
cd \dev\xampp\htdocs\classes

:: Start java met het juiste classpath
start java -cp .;log4j-1.2.17.jar;mysql-connector-java-5.1.31.jar library.main.LibraryServer -properties breda.properties

:: Start tweede server voor inter-server communicatie.
start java -cp .;log4j-1.2.17.jar;mysql-connector-java-5.1.31.jar library.main.LibraryServer -properties oosterhout.properties
 
:: Wanneer je securityproblemen wilt oplossen, voeg dan onderstaande optie aan het command toe.
:: Hiermee krijg je inzicht in alle security instellingen.
::
:: 		-Djava.security.debug=access,failure

@cd C:\dev\workspace\workspace-team\bibliotheekinformatiesysteem\IVH5\server