"""Synthesizes speech from the input string of text or ssml.
Make sure to be working in a virtual environment.

Note: ssml must be well-formed according to:
    https://www.w3.org/TR/speech-synthesis/
"""
from google.cloud import texttospeech

# Instantiates a client
client = texttospeech.TextToSpeechClient.from_service_account_json("/path_to_key.json")

LANGUAGE = "language"
GENDER = "gender"
ENCODING = "encoding"
SPEED = "speed"
PITCH = "pitch"
TEXT_TYPE = "text_type"

SUPPORTED_LANGUAGES = [
    "af-ZA",
    "ar-XA",
    "bg-BG",
    "bn-IN",
    "ca-ES",
    "cmn-CN",
    "cmn-TW",
    "cs-CZ",
    "da-DK",
    "de-DE",
    "el-GR",
    "en-AU",
    "en-GB",
    "en-IN",
    "en-US",
    "es-ES",
    "es-US",
    "fi-FI",
    "fil-PH",
    "fr-CA",
    "fr-FR",
    "gu-IN",
    "hi-IN",
    "hu-HU",
    "id-ID",
    "is-IS",
    "it-IT",
    "ja-JP",
    "kn-IN",
    "ko-KR",
    "lv-LV",
    "ml-IN",
    "ms-MY",
    "nb-NO",
    "nl-BE",
    "nl-NL",
    "pa-IN",
    "pl-PL",
    "pt-BR",
    "pt-PT",
    "ro-RO",
    "ru-RU",
    "sk-SK",
    "sr-RS",
    "sv-SE",
    "ta-IN",
    "te-IN",
    "th-TH",
    "tr-TR",
    "uk-UA",
    "vi-VN",
    "yue-HK",
]

SUPPORTED_OPTIONS = [
    LANGUAGE,
    GENDER,
    ENCODING,
    SPEED,
    PITCH,
    TEXT_TYPE
]

DEFAULT_LANG = "en-US"
DEFAULT_GENDER = "NEUTRAL"
DEFAULT_ENCODING = "MP3"

MIN_SPEED = 0.25
MAX_SPEED = 4.0
DEFAULT_SPEED = 1.0

MIN_PITCH = -20.0
MAX_PITCH = 20.0
DEFAULT_PITCH = 0

SUPPORTED_TEXT_TYPES = ["text", "ssml"]
DEFAULT_TEXT_TYPE = "text"

language = "fr-FR"

# Set the text input to be synthesized
synthesis_input = texttospeech.SynthesisInput(text="Myrtheunjayan")

# Build the voice request, select the language code ("en-US") and the ssml
# voice gender ("neutral")
voice = texttospeech.VoiceSelectionParams(
    language_code=language, ssml_gender=texttospeech.SsmlVoiceGender.NEUTRAL
)

# Select the type of audio file you want returned
audio_config = texttospeech.AudioConfig(
    audio_encoding=texttospeech.AudioEncoding.MP3
)

# Perform the text-to-speech request on the text input with the selected
# voice parameters and audio file type
response = client.synthesize_speech(
    input=synthesis_input, voice=voice, audio_config=audio_config
)

# The response's audio_content is binary.
with open("output.mp3", "wb") as out:
    # Write the response to the output file.
    out.write(response.audio_content)
    print('Audio content written to file "output.mp3"')
