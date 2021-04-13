# inherit from oracle jdk 11
FROM store/oracle/jdk:11

MAINTAINER Ritesh Sangwan <ritesh@local-macbook-pro>

# define the volumes
VOLUME /tmp
VOLUME /var/log/mmt

# define the environment constants
ENV INSTALLATION_ROOT /usr/share/mmt

# define the additional environment variables
ENV EXTRA_JAVA_OPTS ""

ARG JAR_FILE

RUN echo "Copying $JAR_FILE to container"

# Add the main jar file
COPY ${JAR_FILE} $INSTALLATION_ROOT/planner.jar

# define the entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS $EXTRA_JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=$SPRING_ACTIVE_PROFILES -Dspring.application.json=$SPRING_APPLICATION_JSON -jar /usr/share/mmt/planner.jar"]