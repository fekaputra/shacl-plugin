# protege-plugin-examples

This repository contains example code for developing a tab, view, or menu plug-in for the Protege Desktop ontology editor (*versions 4.3 and higher*).  The Maven POM file in the top-level directory demonstrates one possible method for packaging plug-in code into the required OSGi bundle format using the [Maven Bundle Plugin](http://felix.apache.org/site/apache-felix-maven-bundle-plugin-bnd.html).

If you're developing a plug-in for the earlier Protege Desktop 3.x series, please see the [p3-plugin-example-code](https://github.com/protegeproject/p3-plugin-example-code) repository instead.

#### Prerequisites

To build and run the examples, you must have the following items installed:

+ Apache's [Maven](http://maven.apache.org/index.html).
+ A tool for checking out a [Git](http://git-scm.com/) repository.
+ A Protege distribution (4.3 or higher).  The Protege 4.3.0 release is [available](http://protege.stanford.edu/products.php#desktop-protege) from the main Protege website.  Alternatively, any of the [Protege 5.0.0 beta SNAPSHOT builds](http://protege.stanford.edu/download/protege/5.0/snapshots/) are compatible with this example code.

#### Build and install example plug-ins

1. Get a copy of the example code:

        git clone https://github.com/protegeproject/protege-plugin-examples.git protege-plugin-examples
    
2. Change into the protege-plugin-examples directory.

3. Type mvn clean package.  On build completion, the "target" directory will contain a protege.plugin.examples-${version}.jar file.

4. Copy the JAR file from the target directory to the "plugins" subdirectory of your Protege distribution.
 
#### View example plug-ins

Launch your Protege distribution.  Select About from the Help menu to verify successful installation:

![Protege Desktop About box](http://jvendetti.github.io/img/protege%20about%20box.png)

The examples bundle contains:

+ Two custom tabs - "Example Tab" and "Example Tab (2)".  Enable either tab via the Window | Tabs menu.
+ One custom view - "Example view component".  If you enabled the Example Tab in the previous step, the Example view component will be visible on the right-hand side.  Alternatively, you can enable the view via Window | Views | Ontology views.
+ Several custom menu items.  Expand the Tools menu to see the custom menu items.
+ A custom top-level menu - "Example Menu".  The custom top-level menu appears in the main menu bar between the Server and Tools menus.  Select Example Menu to see several submenu items.
 

