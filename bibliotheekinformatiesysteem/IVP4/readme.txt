
-- compileert, bouwt, test.
gradle build 

-- maakt html directory met codecoverage results in Calculator\build\jacocoHtml
gradle jacocoTestReport 

-- Runnen:
java -jar mvn-calculator-0.1.0.jar

-- toon HTML testrapport :-)
gradle showReport