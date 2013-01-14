#!/bin/bash 
# Installs necessary jar files into local maven repository
# 
# Note: change your user account name before running
#
USER=atyagi

mvn install:install-file -Dfile=/Users/$USER/projects/SurgeryAssist/extraJars/sqljdbc4.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=3.0 -Dpackaging=jar
