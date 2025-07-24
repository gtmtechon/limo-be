#skip test & run
mvn clean package -Dmaven.test.skip=true

#skip test only, 
mvn clean package -DskipTests 
