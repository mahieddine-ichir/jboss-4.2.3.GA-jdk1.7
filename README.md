# jboss-4.2.3.GA-jdk1.7
## Description
A patch to be able to run JBoss 4.2.3 on JDK 1.7 or higher

## Who is concerned?
If you have encoutered
	
	java.lang.IllegalStateException: Cannot build JAXB context ... java.lang.StackTraceElement does not have a no-arg default constructor ...

on JBoss 4.2.3 when trying to run on a JDK 1.7 or higher, then this can help you.

## How-To?
Using a ZIP tool (Z-zip or else):
1. Open the jar `jbossws-core.jar` located in the `deploy/jbossws.sar` folder of your server instance
2. Navigate (within the ZIP tool) to `org/jboss/ws/core/jaxws/` folder and replace the `AbstractWrapperGenerator.class` class with the provided one wihtin the jar file (rememeber that a jar is a nothing but a zip file!)
3. Restart your server

## Important notes
1. You may have also to place the following libraries within the JBoss `endorsed` folder
- jboss-saaj.jar
- jboss-jaxrpc.jar
- jboss-jaxws.jar
- jboss-jaxws-ext.jar
- jaxb-api.jar 

2. It it important to compile the *AbstractWrapperGenerator.java* with a Java 1.6 compliance level, otherwiser you will encounter problems of type `java.lang.UnsupportedClassVersionError`
