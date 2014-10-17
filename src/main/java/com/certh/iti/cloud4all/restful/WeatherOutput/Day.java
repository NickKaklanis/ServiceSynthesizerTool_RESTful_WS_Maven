
package com.certh.iti.cloud4all.restful.WeatherOutput;

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
    "condition",
    "day_of_week",
    "high_celsius",
    "high",
    "low_celsius",
    "low"
})
public class Day {

    /**
     * 
     */
    @JsonProperty("condition")
    private String condition;
    /**
     * 
     */
    @JsonProperty("day_of_week")
    private String day_of_week;
    /**
     * 
     */
    @JsonProperty("high_celsius")
    private String high_celsius;
    /**
     * 
     */
    @JsonProperty("high")
    private String high;
    /**
     * 
     */
    @JsonProperty("low_celsius")
    private String low_celsius;
    /**
     * 
     */
    @JsonProperty("low")
    private String low;
    @JsonIgnore
    //private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("condition")
    public String getCondition() {
        return condition;
    }

    /**
     * 
     */
    @JsonProperty("condition")
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * 
     */
    @JsonProperty("day_of_week")
    public String getDayOfWeek() {
        return day_of_week;
    }

    /**
     * 
     */
    @JsonProperty("day_of_week")
    public void setDayOfWeek(String dayOfWeek) {
        this.day_of_week = dayOfWeek;
    }

    /**
     * 
     */
    @JsonProperty("high_celsius")
    public String getHighCelsius() {
        return high_celsius;
    }

    /**
     * 
     */
    @JsonProperty("high_celsius")
    public void setHighCelsius(String highCelsius) {
        this.high_celsius = highCelsius;
    }

    /**
     * 
     */
    @JsonProperty("high")
    public String getHigh() {
        return high;
    }

    /**
     * 
     */
    @JsonProperty("high")
    public void setHigh(String high) {
        this.high = high;
    }

    /**
     * 
     */
    @JsonProperty("low_celsius")
    public String getLowCelsius() {
        return low_celsius;
    }

    /**
     * 
     */
    @JsonProperty("low_celsius")
    public void setLowCelsius(String lowCelsius) {
        this.low_celsius = lowCelsius;
    }

    /**
     * 
     */
    @JsonProperty("low")
    public String getLow() {
        return low;
    }

    /**
     * 
     */
    @JsonProperty("low")
    public void setLow(String low) {
        this.low = low;
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

    /*@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

}
