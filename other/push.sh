#!/bin/bash

DAISYVM=192.168.56.101

mvn clean; mvn install && scp daisy-ear/target/daisy-ear-1.0-SNAPSHOT.ear daisy@${DAISYVM}:/opt/wildfly/standalone/deployments/
