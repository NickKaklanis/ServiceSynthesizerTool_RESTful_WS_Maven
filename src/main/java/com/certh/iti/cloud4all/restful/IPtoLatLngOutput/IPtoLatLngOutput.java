
package com.certh.iti.cloud4all.restful.IPtoLatLngOutput;

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
    "areacode",
    "city",
    "countryFullName",
    "country",
    "ip",
    "lat",
    "lng",
    "stateFullName",
    "state",
    "zip"
})
public class IPtoLatLngOutput {

    /**
     * 
     */
    @JsonProperty("areacode")
    private Object areacode;
    /**
     * 
     */
    @JsonProperty("city")
    private String city;
    /**
     * 
     */
    @JsonProperty("countryFullName")
    private String countryFullName;
    /**
     * 
     */
    @JsonProperty("country")
    private String country;
    /**
     * 
     */
    @JsonProperty("ip")
    private String ip;
    /**
     * 
     */
    @JsonProperty("lat")
    private Double lat;
    /**
     * 
     */
    @JsonProperty("lng")
    private Double lng;
    /**
     * 
     */
    @JsonProperty("stateFullName")
    private String stateFullName;
    /**
     * 
     */
    @JsonProperty("state")
    private String state;
    /**
     * 
     */
    @JsonProperty("zip")
    private String zip;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("areacode")
    public Object getAreacode() {
        return areacode;
    }

    /**
     * 
     */
    @JsonProperty("areacode")
    public void setAreacode(Object areacode) {
        this.areacode = areacode;
    }

    /**
     * 
     */
    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    /**
     * 
     */
    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 
     */
    @JsonProperty("countryFullName")
    public String getCountryFullName() {
        return countryFullName;
    }

    /**
     * 
     */
    @JsonProperty("countryFullName")
    public void setCountryFullName(String countryFullName) {
        this.countryFullName = countryFullName;
    }

    /**
     * 
     */
    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    /**
     * 
     */
    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 
     */
    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }

    /**
     * 
     */
    @JsonProperty("ip")
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 
     */
    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

    /**
     * 
     */
    @JsonProperty("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * 
     */
    @JsonProperty("lng")
    public Double getLng() {
        return lng;
    }

    /**
     * 
     */
    @JsonProperty("lng")
    public void setLng(Double lng) {
        this.lng = lng;
    }

    /**
     * 
     */
    @JsonProperty("stateFullName")
    public String getStateFullName() {
        return stateFullName;
    }

    /**
     * 
     */
    @JsonProperty("stateFullName")
    public void setStateFullName(String stateFullName) {
        this.stateFullName = stateFullName;
    }

    /**
     * 
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * 
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 
     */
    @JsonProperty("zip")
    public String getZip() {
        return zip;
    }

    /**
     * 
     */
    @JsonProperty("zip")
    public void setZip(String zip) {
        this.zip = zip;
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
