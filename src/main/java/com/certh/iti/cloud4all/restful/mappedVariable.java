package com.certh.iti.cloud4all.restful;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author nkak
 */
public class mappedVariable 
{
    @JsonProperty("fromServiceName")
    private String fromServiceName;
    
    @JsonProperty("fromVariableName")
    private String fromVariableName;
    
    @JsonProperty("toServiceName")
    private String toServiceName;
    
    @JsonProperty("toVariableName")
    private String toVariableName;
    
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("fromServiceName")
    public String getFromServiceName() {
        return fromServiceName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("fromServiceName")
    public void setFromServiceName(String fromServiceName) {
        this.fromServiceName = fromServiceName;
    }
    
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("fromVariableName")
    public String getFromVariableName() {
        return fromVariableName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("fromVariableName")
    public void setFromVariableName(String fromVariableName) {
        this.fromVariableName = fromVariableName;
    }
    
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("toServiceName")
    public String getToServiceName() {
        return toServiceName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("toServiceName")
    public void setToServiceName(String toServiceName) {
        this.toServiceName = toServiceName;
    }
    
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("toVariableName")
    public String getToVariableName() {
        return toVariableName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("toVariableName")
    public void setToVariableName(String toVariableName) {
        this.toVariableName = toVariableName;
    }
}
