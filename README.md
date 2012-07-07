===========
iwtr-import

This command-line tool is part of the IWTR project.

iwtr-import analyses one or more given projects, generating a graph containing all
the project modules and their dependencies, as specified in the respective ivy.xml files.

Upon completion, it's possible to run other iwtr commands to get information about
the dependency structure.

===================
How to make it work

1. Download the code
2. Open a terminal and go to the project root directory
3a. On a Linux machine:
    ./gradlew installApp
    cd build/install/iwtr-import
    ./bin/iwtr-import
    
3b. On a Windows machine:
    gradlew.bat installApp
    cd build\install\iwtr-import
    bin\iwtr-import
4. Follow the instructions
5. The command won't give you any feedback if everything will work as expected.

===================
Future enhancements 

. Customisation via command-line parameters
.. User can change the repository location
.. User can reset the database before importing the projects
.. User can choose the type of the build files. Currently only ivy type is supported, but
   there are plans to support maven type
.. Testing? Probably not much unit tests but most likely some integration tests for
   avoiding regressions