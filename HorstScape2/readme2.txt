copy webwindow.jar from... 

http://home.earthlink.net/~hheister/webwindow/webwindowDemo_Java2.zip

...to this lib directory

1) Bear in mind that this the above is commercial software and connot be distributed with Jesktop.
The version in that zip also has a 15 minute run limit.

2) The build.sh and the bin\ant.sh for this app, have been modified to use ant.jar and xerces.jar from higher directories (saves having them in the source tree more than once).

3) The dymanic download of the webwindow.jar (which gets around the distribution, but not the compilation issue) is not working yet.  See http://developer.java.sun.com/developer/bugParade/bugs/4388202.html and vote for it please.  Se applications.xml for this app and DektopKernelIpml ("additonal-jars").

- Paul H