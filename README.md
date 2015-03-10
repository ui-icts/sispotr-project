#siSPOTR Project
===============
##With the siSPOTR tools you can: 
* Identify candidate siRNA/shRNA sequences for your gene of interest with a low potential for off-targeting.
* Evaluate the off-targeting potential for any RNAi sequence.
* View a list of all predicted off-targets for siRNA candidates, along with information on the site-type composition and probability of being off-targeted.
* BLAST your sequence to search for siRNA-like on/off-targeting


##Components
siSPOTR Project is made of two major components
* [base](https://github.com/ui-icts/sispotr-project/tree/master/base) - Database Access layer
* [webapp](https://github.com/ui-icts/sispotr-project/tree/master/webapp) - JAVA Spring/Hibernate Application



## Build Instructions

    # git clone git@github.com:ui-icts/sispotr-project.git
    # cd sispotr-project/
    # mvn install

## Setup Database

1. copy "webapp/src/main/resources/config/safeseed.properties.example" to webapp/src/main/resources/config/safeseed.properties
1. Update with your properties, provide the correct Database URL, username, and password


## Running Jetty Appserver

    # cd sispotr-project
    # mvn package
    # java -jar  webapp/target/dependency/jetty-runner.jar --path /sispotr --port 9090 webapp/target/*.war
    # in a browser goto: http://localhost:9090/sispotr
    # Default username: admin
    # Default password: safeseed



[![Build Status](https://travis-ci.org/ui-icts/sispotr-project.svg?branch=master)](https://travis-ci.org/ui-icts/sispotr-project/)


Now using [slack](https://uiowaicts.slack.com/messages/sispotr/)

