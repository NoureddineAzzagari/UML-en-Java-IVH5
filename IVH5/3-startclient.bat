::
:: Batch bestand voor het starten van de IVH5 LibraryClient.
::
:: Voer dit batchbestand uit vanuit de IVH5\client directory, of pas de paden naar
:: de classes hieronder aan.
:: Zorg ervoor dat de registry en de LibraryServer gestart zijn.
::

java -cp .\client\build\deploy;.\client\build\deploy\log4j-1.2.17.jar edu.avans.aei.ivh5.view.main.LibraryClient -properties ./client/resources/client.properties

:: @pause