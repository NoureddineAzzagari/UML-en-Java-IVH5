::
:: Startscript for the ShowRMIRegistry application
::
:: Required parameters, provided with -D :
::   java.security.policy=showrmiregistry.policy              -- the security policy file
::   java.rmi.server.codebase=http://localhost/classes/bin/   -- the SERVER codebase; modify to match your own
::   org.sump.showrmiregistry.defaultregistry=localhost       -- the default host for rmi registry
::

:: Start java
java -cp .\bin -Djava.security.policy=showrmiregistry.policy -Djava.rmi.server.codebase=http://localhost/classes/bin/ -Dorg.sump.showrmiregistry.defaultregistry=localhost org.sump.showrmiregistry.ShowRMIRegistry

:: To get insight into all security settings, add the following optional parameter to the above
:: -Djava.security.debug=access,failure 

:: Wait for keystroke before closing this window
@pause