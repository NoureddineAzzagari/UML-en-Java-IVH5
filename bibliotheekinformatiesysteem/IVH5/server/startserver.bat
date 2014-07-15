::
:: Batch bestand voor het starten van de IVH5 LibraryServer.
:: Je start de server het handigst vanuit de directory waar de webserver
:: de classes kan vinden. Pas deze zo nodig hieronder aan.
::

cd \dev\xampp\htdocs\classes

:: Start java met het juiste classpath
java -cp .;log4j-1.2.17.jar;mysql-connector-java-5.1.31.jar library.main.LibraryServer -servicename BiebBreda

:: Wanneer je securityproblement wilt oplossen, voeg dan onderstaade optie aan het command toe.
:: Hiermee krijg je inzicht in alle security instellingen.
::
:: 		-Djava.security.debug=access,failure