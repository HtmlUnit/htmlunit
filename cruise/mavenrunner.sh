#!/bin/bash

export JAVA_HOME=/usr/java/jdk1.6.0_45

export M2_HOME=/opt/cruisecontrol/maven/apache-maven-3.1.1

$M2_HOME/bin/mvn $*
