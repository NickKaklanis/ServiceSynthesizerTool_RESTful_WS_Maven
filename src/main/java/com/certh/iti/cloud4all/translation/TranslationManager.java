package com.certh.iti.cloud4all.translation;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author nkak
 */
public class TranslationManager 
{
    //Language codes supported by BING Translator
    public static final String BING_ARABIC = "ar";
    public static final String BING_BULGARIAN = "bg";
    public static final String BING_CATALAN = "ca";
    public static final String BING_CHINESE_SIMPLIFIED = "zh-CHS";
    public static final String BING_CHINESE_TRADITIONAL = "zh-CHT";
    public static final String BING_CZECH = "cs";
    public static final String BING_DANISH = "da";
    public static final String BING_DUTCH = "nl";
    public static final String BING_ENGLISH = "en";
    public static final String BING_ESTONIAN = "et";
    public static final String BING_FINNISH = "fi";
    public static final String BING_FRENCH = "fr";
    public static final String BING_GERMAN = "de";
    public static final String BING_GREEK = "el";
    public static final String BING_HAITIAN_CREOLE = "ht";
    public static final String BING_HEBREW = "he";
    public static final String BING_HINDI = "hi";
    public static final String BING_HMONG_DAW = "mww";
    public static final String BING_HUNGARIAN = "hu";
    public static final String BING_INDONESIAN = "id";
    public static final String BING_ITALIAN = "it";
    public static final String BING_JAPANESE = "ja";
    public static final String BING_KLINGON = "tlh";
    public static final String BING_KLINGON_PIQAD = "tlh-Qaak";
    public static final String BING_KOREAN = "ko";
    public static final String BING_LATVIAN = "lv";
    public static final String BING_LITHUANIAN = "lt";
    public static final String BING_MALAY = "ms";
    public static final String BING_MALTESE = "mt";
    public static final String BING_NORWEGIAN = "no";
    public static final String BING_PERSIAN = "fa";
    public static final String BING_POLISH = "pl";
    public static final String BING_PORTUGUESE = "pt";
    public static final String BING_ROMANIAN = "ro";
    public static final String BING_RUSSIAN = "ru";
    public static final String BING_SLOVAK = "sk";
    public static final String BING_SLOVENIAN = "sl";
    public static final String BING_SPANISH = "es";
    public static final String BING_SWEDISH = "sv";
    public static final String BING_THAI = "th";
    public static final String BING_TURKISH = "tr";
    public static final String BING_UKRAINIAN = "uk";
    public static final String BING_URDU = "ur";
    public static final String BING_VIETNAMESE = "vi";
    public static final String BING_WELSH = "cy";
    
    //Language codes supported by WebAnywhere
    public static final String WEBANYWHERE_AFRIKANNS = "af";
    public static final String WEBANYWHERE_BOSNIAN = "bs";
    public static final String WEBANYWHERE_CATALAN = "ca";
    public static final String WEBANYWHERE_CHINESE_SIMPLIFIED = "zh_CN";
    public static final String WEBANYWHERE_CHINESE_TRADITIONAL = "zh_TW";
    public static final String WEBANYWHERE_CROATIAN = "hr";
    public static final String WEBANYWHERE_CZECH = "cs";
    public static final String WEBANYWHERE_ENGLISH = "en";
    public static final String WEBANYWHERE_ESPERANTO = "eo";
    public static final String WEBANYWHERE_FINNISH = "fi";
    public static final String WEBANYWHERE_FRENCH = "fr";
    public static final String WEBANYWHERE_GERMAN = "de";
    public static final String WEBANYWHERE_GREEK = "el";
    public static final String WEBANYWHERE_HUNGARIAN = "hu";
    public static final String WEBANYWHERE_ITALIAN = "it";
    public static final String WEBANYWHERE_KURDISH = "ku";
    public static final String WEBANYWHERE_LATVIAN = "lv";
    public static final String WEBANYWHERE_PORTUGUESE_BRAZIL = "pt";
    public static final String WEBANYWHERE_PORTUGUESE_EUROPEAN = "pt-pt";
    public static final String WEBANYWHERE_ROMANIAN = "ro";
    public static final String WEBANYWHERE_SERBIAN = "sr";
    public static final String WEBANYWHERE_SLOVAK = "sk";
    public static final String WEBANYWHERE_SPANISH = "es";
    public static final String WEBANYWHERE_SWAHILI = "sw";
    public static final String WEBANYWHERE_SWEDISH = "sv";
    public static final String WEBANYWHERE_TAMIL = "ta";
    public static final String WEBANYWHERE_TURKISH = "tr";
    
    //Language codes supported by GPII
    public static final String GPII_CATALAN = "ca";
    public static final String GPII_CHINESE_SIMPLIFIED = "zh_CN";
    public static final String GPII_CHINESE_TRADITIONAL = "zh_TW";
    public static final String GPII_CZECH = "cs";
    public static final String GPII_ENGLISH = "en";
    public static final String GPII_FINNISH = "fi";
    public static final String GPII_FRENCH = "fr";
    public static final String GPII_GERMAN = "de";
    public static final String GPII_GREEK = "el";
    public static final String GPII_HUNGARIAN = "hu";
    public static final String GPII_ITALIAN = "it";
    public static final String GPII_LATVIAN = "lv";
    public static final String GPII_PORTUGUESE = "pt-pt";
    public static final String GPII_ROMANIAN = "ro";
    public static final String GPII_SLOVAK = "sk";
    public static final String GPII_SPANISH = "es";
    public static final String GPII_SWEDISH = "sv";
    public static final String GPII_TURKISH = "tr";
    
    public Gson gson;
        
    
    private static TranslationManager instance = null;
    
    private TranslationManager() 
    {
        gson = new Gson();
        //gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
    
    public static TranslationManager getInstance() 
    {
        if(instance == null) 
            instance = new TranslationManager();
        return instance;
    }
}