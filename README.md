# proxy
![Build Status](https://travis-ci.org/cunhazera/proxy.svg?branch=master)

# What must be done to run this project locally
 - Clone the project
 - `cd proxy`
 - `mvn clean install`
 - Define an env var called `INSTANCE_IP` with the IP from the local computer
   - There are some commands that you can use to find out your local IP address:
     - `hostname -I | awk '{print $1}'`
     - `ip route get 8.8.8.8 | sed -n '/src/{s/.*src *\([^ ]*\).*/\1/p;q}'`
 - Run:
   - `java -jar target/app-0.0.1-SNAPSHOT.jar`
   - `docker-compose up`
   - Or you can use `docker build` to build your own image from the Dockerfile
