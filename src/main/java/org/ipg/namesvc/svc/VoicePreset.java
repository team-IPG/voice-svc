package org.ipg.namesvc.svc;

import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;

public enum VoicePreset {

    // Default Presets for V1.0

    PRESET_1("Arabic - Female", "ar-XA","ar-XA-Wavenet-A", SsmlVoiceGender.FEMALE, AudioEncoding.MP3),
    PRESET_2("Spanish - Male", "es-US", "es-US-Wavenet-B", SsmlVoiceGender.MALE, AudioEncoding.MP3),
    PRESET_3("Hindi - Female", "hi-IN","hi-IN-Wavenet-A", SsmlVoiceGender.FEMALE, AudioEncoding.MP3),
    PRESET_4("Chinese - Male", "cmn-CN","cmn-CN-Wavenet-B", SsmlVoiceGender.MALE, AudioEncoding.MP3),
    PRESET_5("German - Female", "de-DE","de-DE-Wavenet-A", SsmlVoiceGender.FEMALE, AudioEncoding.MP3),
    PRESET_6("French - Male", "fr-FR", "fr-FR-Wavenet-B", SsmlVoiceGender.MALE, AudioEncoding.MP3),
    PRESET_7("Russian - Female", "ru-RU","ru-RU-Wavenet-A", SsmlVoiceGender.FEMALE, AudioEncoding.MP3),
    PRESET_8("English-US - Male", "en-US","en-US-Wavenet-A", SsmlVoiceGender.MALE, AudioEncoding.MP3),
    PRESET_9("Tamil - Female", "ta-IN","ta-IN-Wavenet-A", SsmlVoiceGender.FEMALE, AudioEncoding.MP3);

    public final static String DEFAULT_PRESET = "PRESET_8";
    public final static String DEFAULT_RATE = "1.0";

    private final String displayName;
    private final String languageCode;
    private final String voiceName;
    private final SsmlVoiceGender voiceGender;
    private final AudioEncoding encoding;

    VoicePreset(String displayName, String languageCode, String voiceName, SsmlVoiceGender voiceGender, AudioEncoding encoding) {
        this.displayName = displayName;
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

    // Rate options for UI: SLOW, MEDIUM, FAST
    public enum Rate {
        SLOW(.6d), MEDIUM(1.0d), FAST(1.8d);
        private final double speed;
        Rate(double speed) {
            this.speed = speed;
        }
    }
}
