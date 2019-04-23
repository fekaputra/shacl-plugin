# SHACL4P Plugin - SHACL Constraint Validation plugin for Protégé Desktop [1]

Contact: **fajar.ekaputra [at] tuwien.ac.at**

The plugin is tried and tested using Protege 5.5.0 on Debian 10 (Buster) with OpenJDK 11, Windows 10 (1809) with the bundled JRE 8, and Mac OSX Mojave with JDK 8.
Developed using TopBraid SHACL Engine 1.1.0 [2] and Apache Jena 3.10.0 [3].   

Special thanks to André Wolski (@locke) for his efforts on cleaning up the old code and making the plugin works again. 

----

**Instruction to use:**
1. Download the jar file from [the latest release](https://github.com/fekaputra/shacl-plugin/releases)
    * Copy it into the /plugins folder of your Protégé Desktop installation
1. Open Protégé Desktop
1. (optional) Download the owl file from the latest release
    * open it in Protégé Desktop
1. Open Window -> Tabs -> SHACL Editor
    * Click the "Validate" button below the SHACL editor to validate the loaded ontology against the shapes defined in the SHACL editor
1. (optional) Open Window -> Tabs -> Minimal SHACL Editor
    * Click the "Validate" button below the SHACL editor to validate the loaded ontology against the shapes defined in the SHACL editor

[1] https://protege.stanford.edu/

[2] https://github.com/TopQuadrant/shacl

[3] https://github.com/apache/jena
