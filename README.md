flights planner
==

This is a spring boot application. It exposes only a single api. Find the api spec in `api` folder.


Pre requisites
--

1. Java 11
2. Maven 3.6 to build the source code
3. Docker if want to build a docker image


Build
--

To build the application `cd` into the root directory. Root directory is which contains `pom.xml` file.
Run following command
```
mvn clean package
```

This will build a jar file and put that into target directory. Target is standard maven directory for output artifacts.

Running
--

To run the application run following command
```
java -Dspring.profiles.active=prod -jar target/planner-1.0.0.jar
```

This command is simply running the build jar file.
Application can be configured profile wise.
dev profile has a different configuration than prod profile.

