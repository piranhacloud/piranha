## CRaC integration

Make sure you are using a CRaC enabled JDK available from 
https://github.com/CRaC/openjdk-builds/releases

To create the checkpoint:

1. java -XX:CRaCCheckpointTo=cr -jar piranha-dist-servlet.jar --enable-crac --write-pid --war-file your.war &
2. jcmd piranha-dist-servlet.jar JDK.checkpoint
3. files should be in the cr directory

To restore from checkpoint:

1. java -XX:CRaCRestoreFrom=cr
