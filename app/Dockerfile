FROM gradle:8.2-jdk20.0.2

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./build/install/app/bin/app