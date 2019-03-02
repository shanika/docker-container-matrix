Build Application

Build maven artifact

```
mvn clean install
```

Build docker image

```
docker build -t shanika/docker-container-matrix .
```

Run docker image

```
docker run -v /var/run/docker.sock:/var/run/docker.sock -p 8080:8080 shanika/docker-container-matrix
```

Test

````
http://localhost:8080 Should return the docker version
