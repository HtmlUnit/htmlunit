#!/bin/bash

export JAVA_HOME=/usr/java/jdk1.6.0_45

export M2_HOME=/opt/cruisecontrol/maven/apache-maven-3.2.3

$M2_HOME/bin/mvn $*
