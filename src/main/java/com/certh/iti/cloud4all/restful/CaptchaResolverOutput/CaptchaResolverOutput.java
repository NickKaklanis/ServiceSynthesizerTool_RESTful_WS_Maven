
package com.certh.iti.cloud4all.restful.CaptchaResolverOutput;

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
    "api-message",
    "captcha"
})
public class CaptchaResolverOutput {

    /**
     * 
     */
    @JsonProperty("api-message")
    private String apiMessage;
    /**
     * 
     */
    @JsonProperty("captcha")
    private String captcha;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("api-message")
    public String getApiMessage() {
        return apiMessage;
    }

    /**
     * 
     */
    @JsonProperty("api-message")
    public void setApiMessage(String apiMessage) {
        this.apiMessage = apiMessage;
    }

    /**
     * 
     */
    @JsonProperty("captcha")
    public String getCaptcha() {
        return captcha;
    }

    /**
     * 
     */
    @JsonProperty("captcha")
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
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
