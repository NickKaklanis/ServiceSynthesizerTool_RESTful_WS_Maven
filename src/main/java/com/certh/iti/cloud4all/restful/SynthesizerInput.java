package com.certh.iti.cloud4all.restful;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author nkak
 */
public class SynthesizerInput
{
    @JsonProperty("input")
    private ArrayList<ServiceInputTemplate> input;
    
    @JsonProperty("mappedVariables")
    private ArrayList<mappedVariable> mappedVariables;
    
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("input")
    public ArrayList<ServiceInputTemplate> getInput() {
        return input;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("input")
    public void setInput(ArrayList<ServiceInputTemplate> input) {
        this.input = input;
    }
    
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mappedVariables")
    public ArrayList<mappedVariable> getMappedVariables() {
        return mappedVariables;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mappedVariables")
    public void setMappedVariables(ArrayList<mappedVariable> mappedVariables) {
        this.mappedVariables = mappedVariables;
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
}
