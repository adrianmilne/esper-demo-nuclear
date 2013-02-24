corsoft-esper-demo
==================

Simple project that illustrates the use of the Esper Complex Event Processing Engine. Purposefully left Unit Tests out to reduce size of code.

When the demo runs it will just simulate sending random temperature events through the processing engine. It will print debug messages to the console when it detects a sequence of events matching any of the 3 criteria statements we have defined (Critical, Warning, Monitor). 


Requirements
============

You will need Maven installed and working.


Setup
=====

To run demo:

1. Open a terminal window

2.ÊNavigate to the root directory of the project (where the pom.xml is)

3. Type 'mvn exec:java' (this will start sending random temperature events)
	
