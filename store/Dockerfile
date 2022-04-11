FROM moxm/java:1.8-full
ARG JAR_FILE=target/trivialfs-store.jar
LABEL maintainer="errorfatal89@gmail.com"
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms4096m -Xmx4096m -XX:+UseConcMarkSweepGC -Djava.security.egd=file:/dev/./urandom"
WORKDIR /trickyfs/store
CMD java $JAVA_OPTS -jar $JAR_FILE


