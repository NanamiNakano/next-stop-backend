FROM gradle:8.3.0-jdk17-jammy AS base

# setup workdir
WORKDIR /app

FROM base as builder

# build application
COPY . .
RUN gradle shadowJar && cp ./build/libs/backend-all.jar /app

FROM azul/zulu-openjdk-alpine:18-latest  AS deps

# resolve dependencies
COPY --from=builder /app/backend-all.jar /app/backend-all.jar
RUN mkdir /app/unpacked && \
    cd /app/unpacked && \
    unzip ../backend-all.jar && \
    cd .. && \
    $JAVA_HOME/bin/jdeps \
    --ignore-missing-deps \
    --print-module-deps \
    -q \
    --recursive \
    --multi-release 17 \
    --class-path="./unpacked/BOOT-INF/lib/*" \
    --module-path="./unpacked/BOOT-INF/lib/*" \
    ./backend-all.jar > /deps.info

FROM azul/zulu-openjdk-alpine:17-latest AS zulu-jdk17

# required to use jlink
RUN apk add --no-cache binutils

# get dependencies info
COPY --from=deps /deps.info /deps.info

# build slim jre
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules $(cat /deps.info) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /zulu-jre17

FROM alpine:latest AS runtime
LABEL org.opencontainers.image.authors="nanaminakano"

# setup jre
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=zulu-jdk17 /zulu-jre17 $JAVA_HOME

# add app user
ARG APPLICATION_USER=appuser
RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER

# configure working directory
RUN mkdir /app && \
    chown -R $APPLICATION_USER /app

USER 1000

# setup application
COPY --chown=1000:1000 --from=builder /app/backend-all.jar /app/backend-all.jar
COPY --chown=1000:1000 ./psw4j.properties /app/psw4j.properties
COPY --chown=1000:1000 ./keystore.jks /app/keystore.jks

WORKDIR /app

# ssl port
EXPOSE 8443
ENTRYPOINT ["/jre/bin/java", "-Dpsw4j.configuration=psw4j.properties", "-jar", "backend-all.jar"]