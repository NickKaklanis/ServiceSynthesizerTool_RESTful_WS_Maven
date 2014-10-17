
package com.certh.iti.cloud4all.restful.TelizeGeolocationOutput;

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
    "area_code",
    "asn",
    "city",
    "continent_code",
    "country_code3",
    "country_code",
    "country",
    "dma_code",
    "ip",
    "isp",
    "latitude",
    "longitude",
    "region_code",
    "region",
    "timezone"
})
public class TelizeGeolocationOutput {

    /**
     * 
     */
    @JsonProperty("area_code")
    private String areaCode;
    /**
     * 
     */
    @JsonProperty("asn")
    private String asn;
    /**
     * 
     */
    @JsonProperty("city")
    private String city;
    /**
     * 
     */
    @JsonProperty("continent_code")
    private String continentCode;
    /**
     * 
     */
    @JsonProperty("country_code3")
    private String countryCode3;
    /**
     * 
     */
    @JsonProperty("country_code")
    private String countryCode;
    /**
     * 
     */
    @JsonProperty("country")
    private String country;
    /**
     * 
     */
    @JsonProperty("dma_code")
    private String dmaCode;
    /**
     * 
     */
    @JsonProperty("ip")
    private String ip;
    /**
     * 
     */
    @JsonProperty("isp")
    private String isp;
    /**
     * 
     */
    @JsonProperty("latitude")
    private Double latitude;
    /**
     * 
     */
    @JsonProperty("longitude")
    private Double longitude;
    /**
     * 
     */
    @JsonProperty("region_code")
    private String regionCode;
    /**
     * 
     */
    @JsonProperty("region")
    private String region;
    /**
     * 
     */
    @JsonProperty("timezone")
    private String timezone;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("area_code")
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 
     */
    @JsonProperty("area_code")
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * 
     */
    @JsonProperty("asn")
    public String getAsn() {
        return asn;
    }

    /**
     * 
     */
    @JsonProperty("asn")
    public void setAsn(String asn) {
        this.asn = asn;
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
    @JsonProperty("continent_code")
    public String getContinentCode() {
        return continentCode;
    }

    /**
     * 
     */
    @JsonProperty("continent_code")
    public void setContinentCode(String continentCode) {
        this.continentCode = continentCode;
    }

    /**
     * 
     */
    @JsonProperty("country_code3")
    public String getCountryCode3() {
        return countryCode3;
    }

    /**
     * 
     */
    @JsonProperty("country_code3")
    public void setCountryCode3(String countryCode3) {
        this.countryCode3 = countryCode3;
    }

    /**
     * 
     */
    @JsonProperty("country_code")
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * 
     */
    @JsonProperty("country_code")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
    @JsonProperty("dma_code")
    public String getDmaCode() {
        return dmaCode;
    }

    /**
     * 
     */
    @JsonProperty("dma_code")
    public void setDmaCode(String dmaCode) {
        this.dmaCode = dmaCode;
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
    @JsonProperty("isp")
    public String getIsp() {
        return isp;
    }

    /**
     * 
     */
    @JsonProperty("isp")
    public void setIsp(String isp) {
        this.isp = isp;
    }

    /**
     * 
     */
    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    /**
     * 
     */
    @JsonProperty("latitude")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     */
    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    /**
     * 
     */
    @JsonProperty("longitude")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     */
    @JsonProperty("region_code")
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * 
     */
    @JsonProperty("region_code")
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * 
     */
    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    /**
     * 
     */
    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 
     */
    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }

    /**
     * 
     */
    @JsonProperty("timezone")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
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
