# proxy
![Build Status](https://travis-ci.org/cunhazera/proxy.svg?branch=master)

# Live Demo
- This app is running here: `http://13.59.118.201:8080`
- POST `http://13.59.118.201:8080/short` with URL in body as param to create a shorted code.
- GET `http://13.59.118.201:8080/{code}` to redirect to the original URL.


# What must be done to run this project locally
 - Define an env var called `INSTANCE_IP` with the IP from the local computer
   - There are some commands that you can use to find out your local IP address:
     - `hostname -I | awk '{print $1}'`
     - `ip route get 8.8.8.8 | sed -n '/src/{s/.*src *\([^ ]*\).*/\1/p;q}'`
 ## Via Docker compose
 - `docker-compose up` (pull the image from dockerhub)

## Building locally
 - Clone the project
 - `cd proxy`
 - `mvn clean install`
 - Run:
   - `java -jar target/app-0.0.1-SNAPSHOT.jar`
   - Or you can use `docker build` to build your own image from the Dockerfile
     - `docker run -p 8080:8080 your-image`

# How to test it
  - Send a `POST` request to `http://your-ip-here:8080/short`, with the URL in the body of the request. It will return something like this:
```
{
"newUrl": "http://your-ip-here:8080/RANDOM_GENERATED_TEXT",
"expiresAt": 1595040333922
}
```
  - Try to use some http client to perform the POST request. If you paste a complex URL in terminal, your text will be corrupted (some chars will be replaced in terminal) and your redirect will fail.
  - Access `http://your-ip-here/RANDOM_GENERATED_TEXT` and you will be redirect to the recorded URL.

# And what about the database?
This app uses a database in memory :)
