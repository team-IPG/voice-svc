package org.ipg.namesvc.svc;

import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;

public enum VoicePreset {

    //TODO: TINKER WITH SOME PRESETS
    PRESET_1("en-US","en-US-Wavenet-F", SsmlVoiceGender.FEMALE,.8d,AudioEncoding.MP3),
    PRESET_2("en-US", "en-US-Wavenet-F", SsmlVoiceGender.MALE,1.2d,AudioEncoding.MP3),
    PRESET_3("en-US","en-US-Wavenet-J", SsmlVoiceGender.MALE,1.3d,AudioEncoding.MP3);

    private final String languageCode;
    private final String voiceName;
    private final SsmlVoiceGender voiceGender;
    private final double rate;
    private final AudioEncoding encoding;

    VoicePreset(String languageCode, String voiceName, SsmlVoiceGender voiceGender, double rate, AudioEncoding encoding) {
        this.languageCode = languageCode;
        this.voiceName = voiceName;
        this.voiceGender = voiceGender;
        this.rate = rate;
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

    public double getRate() {
        return rate;
    }

    public AudioEncoding getEncoding() {
        return encoding;
    }
}

