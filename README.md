# voice-svc

![Coverage](.github/badges/jacoco.svg)

provice services for generating and retrieving voice recordings

## pre-requisite

Download [GCP credentials json file](https://github.com/team-IPG/foundation/blob/main/key.json) and store on your machine

```bash
# example
/Users/john/key.json
```

## running locally

Set the environment variable for GCP credentials
```bash
export GOOGLE_APPLICATION_CREDENTIALS=/Users/john/key.json
```

Execute the gradle wrapper bootRun task
```bash
./gradlew bootRun
```

The bootRun task will build the project using Gradle and start the Spring boot application on port **8081** (or whatever is specified in application.properties for server.port)


## API: convert text to audio

### example: default preset & speed
https://voice-svc-mh6ib2ntwq-uc.a.run.app/voice/johnny

### example: optional preset & default speed
https://voice-svc-mh6ib2ntwq-uc.a.run.app/voice/johnny?preset=PRESET2

### example: optional preset & custom speed
https://voice-svc-mh6ib2ntwq-uc.a.run.app/voice/johnny?preset=PRESET2&speed=1.2

https://voice-svc-mh6ib2ntwq-uc.a.run.app/voice/ho-say?preset=PRESET_4&rate=1.8
