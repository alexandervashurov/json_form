# About this project

You need to create a stateless-application with REST-interface for validation Json files.

### Requirements: 
1) source code is hosted in the public git repository
2) the project runs in the Docker directly from the repository
3) a service running in the Docker does not store state

### Non-functional requirements:
1) Strictly follow the Java Convention when naming classes, methods, and the formation of Java doc
2) To conduct development iteratively, i.e. make commits in git as the new feature is implemented.

### Samples
##### Build and run with docker
    docker run -d -p 80:80 github.com/alexandervashurov/json_form
##### Build and run with gradle and docker
    ./gradlew docker
    docker run -d -p 80:80 --rm validation-server:0.1

##### Send file to validation
    curl -s --data-binary @filename.json http://localhost

##### Example of service response in case of error
    {
       "errorCode"  : 12345,
        "errorMessage" : ["verbose, plain language description of the problem with hints about how to fix it]",
        "errorPlace" : ["highlight the point where error has occurred"],
        "resource"   : ["filename"],
        "request-id" : ["the request id generated by the API for easier tracking of errors"],
    }
