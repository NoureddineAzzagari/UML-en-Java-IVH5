::
:: IVH5 startscript voor de RMI registry.
::
:: Voeg eventueel het pad naar java/bin toe aan je PATH-environment variabele,
:: via 'Deze computer > Eigenschappen > Advanced settings'.
:: Pas eventueel ook het pad naar de codebase hieronder aan 
:: wanneer je een andere directory gebruikt.
::

rmiregistry -J-Djava.rmi.server.codebase="http://localhost/classes/"