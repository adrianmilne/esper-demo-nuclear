<a href="https://travis-ci.org/corsoft/esper-demo-nuclear"><img src="https://travis-ci.org/corsoft/esper-demo-nuclear.svg"/></a>

corsoft-esper-demo
==================

<img src="http://www.adrianmilne.com/wp-content/uploads/2013/02/feature-image-template-esper-cep.png"/>

Blog write up available at:
http://www.adrianmilne.com/complex-event-processing-made-easy/

Also featured on JavaLobby:
http://java.dzone.com/articles/complex-event-processing-made


Simple project that illustrates the use of the Esper Complex Event Processing Engine. Purposefully left Unit Tests out to reduce size of code.

When the demo runs it will just simulate sending random temperature events through the processing engine. It will print debug messages to the console when it detects a sequence of events matching any of the 3 criteria statements we have defined (Critical, Warning, Monitor). 


requirements
============

You will need Maven installed and working.


setup
=====

To run demo:

1. Open a terminal window

2. Navigate to the root directory of the project (where the pom.xml is)

3. 'mvn clean install' (this will compile and build the project)

4. 'mvn exec:java' (this will start running the demo - sending random temperature events)
	
