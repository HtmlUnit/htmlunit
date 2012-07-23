#!/bin/bash

export JAVA_HOME=/usr/java/jdk1.6.0_33

export M2_HOME=/opt/cruisecontrol/maven/apache-maven-3.0.4

$M2_HOME/bin/mvn $*
