# voice-svc

![Coverage](.github/badges/jacoco.svg)

provice services for generating and retrieving voice recordings

## REST APIs

### Convert text to audio

`GET /voice/{text}/?preset={presetName}&rate={rate}`

- {text} : input text you would like converted to audio
- {presetName} : one of 9 available custom voice presets (see below)
- {rate}: decimal number between .5 and 1.5

Example: default `preset` & default `rate` for text `johnny`

https://voice-svc-mh6ib2ntwq-uc.a.run.app/voice/johnny

Example: voice `PRESET_2` & default `rate` for text `johnny`

https://voice-svc-mh6ib2ntwq-uc.a.run.app/voice/johnny?preset=PRESET2

Example: voice `PRESET_4` &  `rate` of `.8` for text `johnny`

https://voice-svc-mh6ib2ntwq-uc.a.run.app/voice/ho-say?preset=PRESET_4&rate=.8

### Available Presets in v1.0
```json
{
  "Arabic - Female":    "PRESET_1",
  "Spanish - Male":     "PRESET_2",
  "Hindi - Female":     "PRESET_3",
  "Chinese - Male":     "PRESET_4",
  "German - Female":    "PRESET_5",
  "French - Male":      "PRESET_6",
  "Russian - Female":   "PRESET_7",
  "English-US - Male":  "PRESET_8",
  "Tamil - Female":     "PRESET_9"
}
```

## Local Run: Pre-Requisites

Download [GCP credentials json file](https://github.com/team-IPG/foundation/blob/main/key.json) and store on your machine

```bash
# example
/Users/john/key.json
```
## Local Run: Gradle Wrapper & terminal

Set the environment variable for GCP credentials
```bash
export GOOGLE_APPLICATION_CREDENTIALS=/Users/john/key.json
```

Execute the gradle wrapper bootRun task
```bash
./gradlew bootRun
```

The bootRun task will build the project using Gradle and start the Spring boot application on port **8081** (or whatever is specified in application.properties for server.port)
