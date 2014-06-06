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
    # cp webapp/target/SafeSeed.war <tomcat_dir>/webapps/



## Setup Database

1. Update "base/src/non-packaged-resources/database-postgres.properties", the the correct Database URL, username, and password
1. $ cd base
1. mvn -p BuildDatabase install




