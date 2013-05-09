#!/bin/bash 
# Installs necessary jar files into local maven repository
# 
# Note: change your user account name before running
#
USER=nick

mvn install:install-file -Dfile=/home/$USER/SurgeryAssist/extraJars/sqljdbc4.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=3.0 -Dpackaging=jar