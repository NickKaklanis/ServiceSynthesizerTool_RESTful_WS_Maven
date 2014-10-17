package com.certh.iti.cloud4all.restful;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author nkak
 */
public class ServiceInputTemplate<T> 
{
    @JsonProperty("serviceName")
    private String serviceName;
    
    @JsonProperty("serviceInput")
    private T serviceInput;
    
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceName")
    public String getServiceName() {
        return serviceName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceName")
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
     /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceInput")
    public T getServiceInput() {
        return serviceInput;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceInput")
    public void setServiceInput(T serviceInput) 
    {
        this.serviceInput = serviceInput;
    }
}
