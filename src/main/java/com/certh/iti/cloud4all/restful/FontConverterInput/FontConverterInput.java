
package com.certh.iti.cloud4all.restful.FontConverterInput;

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
    "inputUrl",
    "targetFontFamily",
    "targetFontSize",
    "targetColor",
    "targetBackground"
})
public class FontConverterInput {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputUrl")
    private String inputUrl;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetFontFamily")
    private String targetFontFamily;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetFontSize")
    private String targetFontSize;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetColor")
    private String targetColor;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetBackground")
    private String targetBackground;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputUrl")
    public String getInputUrl() {
        return inputUrl;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputUrl")
    public void setInputUrl(String inputUrl) {
        this.inputUrl = inputUrl;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetFontFamily")
    public String getTargetFontFamily() {
        return targetFontFamily;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetFontFamily")
    public void setTargetFontFamily(String targetFontFamily) {
        this.targetFontFamily = targetFontFamily;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetFontSize")
    public String getTargetFontSize() {
        return targetFontSize;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetFontSize")
    public void setTargetFontSize(String targetFontSize) {
        this.targetFontSize = targetFontSize;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetColor")
    public String getTargetColor() {
        return targetColor;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetColor")
    public void setTargetColor(String targetColor) {
        this.targetColor = targetColor;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetBackground")
    public String getTargetBackground() {
        return targetBackground;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("targetBackground")
    public void setTargetBackground(String targetBackground) {
        this.targetBackground = targetBackground;
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
