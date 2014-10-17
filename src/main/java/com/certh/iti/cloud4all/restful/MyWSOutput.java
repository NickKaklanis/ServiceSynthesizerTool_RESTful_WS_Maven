package com.certh.iti.cloud4all.restful;

import java.util.ArrayList;

/**
 *
 * @author nkak
 */
public class MyWSOutput 
{
    String URL;
    ArrayList<debugMsg> debugMessages;
    
    public String getURL()
    {
        return URL;
    }
    
    public void setURL(String tmpURL)
    {
        this.URL = tmpURL;
    }
    
    public ArrayList<debugMsg> getDebugMsg()
    {
        return debugMessages;
    }
    
    public void setDebugMsg(ArrayList<debugMsg> tmpDebugMessages)
    {
        for(int i=0; i<tmpDebugMessages.size(); i++)
            this.debugMessages.add(tmpDebugMessages.get(i));
    }
}
