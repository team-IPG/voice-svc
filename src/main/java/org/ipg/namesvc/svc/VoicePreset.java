package org.ipg.namesvc.svc;

import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;

public enum VoicePreset {

    //TODO: CURATE OUR PRESETS
    PRESET_1("en-US","en-US-Wavenet-F", SsmlVoiceGender.FEMALE, AudioEncoding.MP3),
    PRESET_2("en-US", "en-US-Wavenet-F", SsmlVoiceGender.MALE, AudioEncoding.MP3),
    PRESET_3("en-US","en-US-Wavenet-J", SsmlVoiceGender.MALE, AudioEncoding.MP3),
    PRESET_4("en-US","en-US-Wavenet-J", SsmlVoiceGender.NEUTRAL, AudioEncoding.MP3);

    public final static String DEFAULT_PRESET = "PRESET_1";
    public final static String DEFAULT_RATE = "1.0";

    //NOTE: Rate options for UI: SLOW, MEDIUM, FAST
    public enum Rate {
        SLOW(.6d), MEDIUM(1.0d), FAST(1.8d);
        private final double speed;
        Rate(double speed) {
            this.speed = speed;
        }
    }

    private final String languageCode;
    private final String voiceName;
    private final SsmlVoiceGender voiceGender;
    private final AudioEncoding encoding;

    VoicePreset(String languageCode, String voiceName, SsmlVoiceGender voiceGender, AudioEncoding encoding) {
        this.languageCode = languageCode;
        this.voiceName = voiceName;
        this.voiceGender = voiceGender;
        this.encoding = encoding;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public SsmlVoiceGender getVoiceGender() {
        return voiceGender;
    }

    public AudioEncoding getEncoding() {
        return encoding;
    }
}

