
package com.certh.iti.cloud4all.restful.CallWebAnywhereInput;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "urlToOpen",
    "voiceLanguage"
})
public class CallWebAnywhereInput {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("urlToOpen")
    private String urlToOpen;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("voiceLanguage")
    private String voiceLanguage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("urlToOpen")
    public String getUrlToOpen() {
        return urlToOpen;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("urlToOpen")
    public void setUrlToOpen(String urlToOpen) {
        this.urlToOpen = urlToOpen;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("voiceLanguage")
    public String getVoiceLanguage() {
        return voiceLanguage;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("voiceLanguage")
    public void setVoiceLanguage(String voiceLanguage) {
        this.voiceLanguage = voiceLanguage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
