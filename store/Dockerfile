FROM moxm/java:1.8-full
ARG JAR_FILE=target/trivialfs-store.jar
LABEL maintainer="errorfatal89@gmail.com"
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms2048m -Xmx2048m -Djava.security.egd=file:/dev/./urandom"
WORKDIR /trickyfs/store
CMD java $JAVA_OPTS -jar $JAR_FILE


