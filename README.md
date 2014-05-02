# protege-plugin-examples

This repository contains example code for developing a tab, view, or menu plug-in for the Protege Desktop ontology editor (versions 4.3 and higher).  The Maven POM file in the top-level directory demonstrates one possible method for packaging plug-in code into the required OSGi bundle format.

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
 
#### View example plug-ins in Protege

Launch your Protege distribution.  To verify successful installation of the protege.plugin.example bundle, check Protege's console window:

![Protege Desktop console window](http://jvendetti.github.io/img/protege%20console.png)

... or the About Box:

![Protege Desktop About box](http://jvendetti.github.io/img/protege%20about%20box.png)
