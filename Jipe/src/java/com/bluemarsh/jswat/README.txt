JSWAT README
======================================================================
$Id: README.txt,v 1.1.1.1 2002-01-11 08:50:27 paul-h Exp $

WHAT IS IT?
----------------------------------------------------------------------
JSwat is a graphical Java debugger front-end written in Java, using the
Java 2 Platform. This program is under constant development, though it
can be considered fairly stable and somewhat feature complete.


REQUIREMENTS
----------------------------------------------------------------------
JSwat requires the Java Platform Debugger Architecture (JPDA), also
known as Jbug or JDI, which provides the debugger back-end functionality.
The JPDA is available separately from the JavaSoft web site:

  http://java.sun.com/products/jpda/

JPDA is included in JDK1.3 in the lib/tools.jar file. It also comes in
the J2SE 1.2.2 (and above) for Linux. See the notes on the Java Virtual
Machines below for more information on using JSwat with JDK 1.3.


INSTALLING JSWAT
----------------------------------------------------------------------
Visit the project web site for the latest and greatest documentation:

  http://www.bluemarsh.com/java/jswat/docs/index.html


VIRTUAL MACHINES AND JSWAT
----------------------------------------------------------------------
HotSpot
-------
This has several pitfalls which make it undesirable for debugging use.
Thread states are "Unknown" and setting breakpoints often fails. This
may only be a temporary problem, but somehow I think not. To disable
the HotSpot VM, simply modify the <java_home>/jre/lib/jvm.cfg file,
putting the "-classic" line before the "-hotspot" line.

JDK 1.2.1
---------
Stepping and displaying of local variables seems to work correctly in
JDK 1.2.1. All known bugs for JPDA apply to JSwat.

JDK 1.2.2
---------
Stepping is handled differently in 1.2.2 under Win32 versus 1.2.1 and so
there are problems with viewing local variables while stepping. Thus you
may want to disable the refresh code in the locals panel source file.
Under Linux the JDPA seems to work fine.

JDK 1.3
-------
This version of the JDK comes with the HotSpot VM built-in, thus it can
suffer from the same problems listed above under "HotSpot".
The Java Platform Debugger Architecture (JPDA) is a part of JDK 1.3 and
is found in the <java_home>/lib/tools.jar file.


AUTHORS
----------------------------------------------------------------------
Matt Conway
- Contributed numerous bug fixes and enhancements.

Nathan Fiedler
- Original author. Designed and implemented most of the classes within
  JSwat and its dependent libraries.

David Lum
- Wrote the panel.LocalsTreePanel class.

Some parts of the code are derived from the early versions of the JPDA
example debugger. Without the sample it would have been rather difficult
to understand how to use JPDA.


KNOWN PROBLEMS
----------------------------------------------------------------------
- Local variables table shows tooltip positioned incorrectly such that
  it's often impossible to read the tooltip message.
  - Only seems to happen on Linux JDK 1.2.2.
