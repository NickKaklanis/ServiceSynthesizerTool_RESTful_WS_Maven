
package com.certh.iti.cloud4all.restful.StaticImageMapOutput;

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
    "html",
    "imageUrl",
    "providerUrl",
    "rateLimitUrl",
    "success",
    "supported",
    "termsOfUseUrl"
})
public class StaticImageMapOutput {

    /**
     * 
     */
    @JsonProperty("html")
    private String html;
    /**
     * 
     */
    @JsonProperty("imageUrl")
    private String imageUrl;
    /**
     * 
     */
    @JsonProperty("providerUrl")
    private String providerUrl;
    /**
     * 
     */
    @JsonProperty("rateLimitUrl")
    private String rateLimitUrl;
    /**
     * 
     */
    @JsonProperty("success")
    private Boolean success;
    /**
     * 
     */
    @JsonProperty("supported")
    private Boolean supported;
    /**
     * 
     */
    @JsonProperty("termsOfUseUrl")
    private String termsOfUseUrl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("html")
    public String getHtml() {
        return html;
    }

    /**
     * 
     */
    @JsonProperty("html")
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * 
     */
    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 
     */
    @JsonProperty("imageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 
     */
    @JsonProperty("providerUrl")
    public String getProviderUrl() {
        return providerUrl;
    }

    /**
     * 
     */
    @JsonProperty("providerUrl")
    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    /**
     * 
     */
    @JsonProperty("rateLimitUrl")
    public String getRateLimitUrl() {
        return rateLimitUrl;
    }

    /**
     * 
     */
    @JsonProperty("rateLimitUrl")
    public void setRateLimitUrl(String rateLimitUrl) {
        this.rateLimitUrl = rateLimitUrl;
    }

    /**
     * 
     */
    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 
     */
    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 
     */
    @JsonProperty("supported")
    public Boolean getSupported() {
        return supported;
    }

    /**
     * 
     */
    @JsonProperty("supported")
    public void setSupported(Boolean supported) {
        this.supported = supported;
    }

    /**
     * 
     */
    @JsonProperty("termsOfUseUrl")
    public String getTermsOfUseUrl() {
        return termsOfUseUrl;
    }

    /**
     * 
     */
    @JsonProperty("termsOfUseUrl")
    public void setTermsOfUseUrl(String termsOfUseUrl) {
        this.termsOfUseUrl = termsOfUseUrl;
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
