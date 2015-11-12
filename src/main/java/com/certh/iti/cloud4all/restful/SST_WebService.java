package com.certh.iti.cloud4all.restful;

import com.cedarsoftware.util.io.JsonWriter;
import com.certh.iti.cloud4all.restful.mappedVariable;
import com.certh.iti.cloud4all.translation.TranslationManager;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.options.Options;
import com.sun.codemodel.JCodeModel;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;
import org.eclipse.persistence.oxm.JSONWithPadding;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.json.JSONObject;
import org.jsonschema2pojo.SchemaMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@Path("/SST")
public class SST_WebService
{

/**************************/
/* CORE FUNCTIONS - START */
/**************************/
    
    @POST
    @Path("/genClassesForWSInput/{tmpServiceName}")
    @Consumes("application/json")
    public Response genClassesForWSInput(@PathParam("tmpServiceName") String tempServiceName, InputStream tmpInput)
    {
        MyWSOutput tmpOutput = new MyWSOutput();
        tmpOutput.URL = "genClassesForWSInput";
        tmpOutput.debugMessages = new ArrayList<debugMsg>();

        try
        {
            //copy json schema to server
            copyInputStreamToFile(tmpInput, new File(SST_Manager.getInstance().HTDOCS_DIR + "/ServicesJsonSchemas/" + tempServiceName + "_Input.json"));
            //generate java class(es)
            URL source = new URL("http://" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/ServicesJsonSchemas/" + tempServiceName + "_Input.json");
            JCodeModel codeModel = new JCodeModel();
            new SchemaMapper().generate(codeModel, tempServiceName + "Input", "com.certh.iti.cloud4all.restful." + tempServiceName + "Input", source); 
            codeModel.build(new File(SST_Manager.getInstance().JAVA_SRC_PATH));
        }
        catch(Exception e)
        {
            debugMsg tmpDebugMsg = new debugMsg();
            tmpDebugMsg.info = "jsonschema2pojo - EXCEPTION";
            tmpDebugMsg.firstDebugMsg = e.getMessage();
            tmpOutput.debugMessages.add(tmpDebugMsg);
        }

        //output
        String tmpOutputStr = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
        } catch (IOException ex) {
            Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/genClassesForWSOutput/{tmpServiceName}")
    @Consumes("application/json")
    public Response genClassesForWSOutput(@PathParam("tmpServiceName") String tempServiceName, InputStream tmpInput)
    {
        MyWSOutput tmpOutput = new MyWSOutput();
        tmpOutput.URL = "genClassesForWSOutput";
        tmpOutput.debugMessages = new ArrayList<debugMsg>();

        try
        {
            //copy json schema to server
            copyInputStreamToFile(tmpInput, new File(SST_Manager.getInstance().HTDOCS_DIR + "/ServicesJsonSchemas/" + tempServiceName + "_Output.json"));
            //generate java class(es)
            URL source = new URL("http://" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/ServicesJsonSchemas/" + tempServiceName + "_Output.json");
            JCodeModel codeModel = new JCodeModel();
            new SchemaMapper().generate(codeModel, tempServiceName + "Output", "com.certh.iti.cloud4all.restful." + tempServiceName + "Output", source); 
            codeModel.build(new File(SST_Manager.getInstance().JAVA_SRC_PATH));
        }
        catch(Exception e)
        {
            debugMsg tmpDebugMsg = new debugMsg();
            tmpDebugMsg.info = "jsonschema2pojo - EXCEPTION";
            tmpDebugMsg.firstDebugMsg = e.getMessage();
            tmpOutput.debugMessages.add(tmpDebugMsg);
        }

        //output
        String tmpOutputStr = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
        } catch (IOException ex) {
            Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/getInputJsonSchema/{tmpServiceName}")
    public Response getInputJsonSchema(@PathParam("tmpServiceName") String tempServiceName)
    {
        String res = "";
        try
        {
            URL jsonSchemaURL = new URL("http://" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/ServicesJsonSchemas/" + tempServiceName + "_Input.json");
            URLConnection connection = jsonSchemaURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) 
                response.append(inputLine);
            in.close();
            res = response.toString();
        }
        catch(Exception e) 
        {
            System.out.println("getInputJsonSchema EXCEPTION! -> " + e.getMessage());
        }
        return Response.ok(res, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/getOutputJsonSchema/{tmpServiceName}")
    public Response getOutputJsonSchema(@PathParam("tmpServiceName") String tempServiceName)
    {
        /*String res = "";
        try
        {
            URL jsonSchemaURL = new URL("http://" + ApacheServerIP + "/" + dirInHtdocs + "/ServicesJsonSchemas/" + tempServiceName + "_Output.json");
            URLConnection connection = jsonSchemaURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) 
                response.append(inputLine);
            in.close();
            res = response.toString();
        }
        catch(Exception e) 
        {
            System.out.println("getOutputJsonSchema EXCEPTION! -> " + e.getMessage());
        }*/
        String res = "";
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/ServicesJsonSchemas/" + tempServiceName + "_Output.json").openStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) 
                res = res + line + "\n";
            reader.close();
        }
        catch(Exception e) 
        {
            System.out.println("getOutputJsonSchema EXCEPTION! -> " + e.getMessage());
        }
        
        return Response.ok(res, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/generateCallingFunction/{tmpServiceName}")
    public Response generateCallingFunction(@PathParam("tmpServiceName") String tempServiceName)
    {
        String res = "OK";
        
        //open SST_WebService.java
        String filepath = SST_Manager.getInstance().JAVA_SRC_PATH + "/com/certh/iti/cloud4all/restful/SST_WebService.java";
        String fileContentStr = "";
        try {
            fileContentStr = readFileToString(filepath);
        } catch (Exception e) {
            System.out.println("generateCallingFunction EXCEPTION! -> " + e.getMessage());
        }
        
        //find last "}" in the file
        int lastBracketIndex = fileContentStr.lastIndexOf("}");
        if(lastBracketIndex != -1)
        {
            String contentBeforeBracket = fileContentStr.substring(0, lastBracketIndex);
            String contentAfterBracket = fileContentStr.substring(lastBracketIndex);
            String functionBody = "\n" +
                    "\n\t@POST" +
                    "\n\t@Path(\"/call_" + tempServiceName + "\")" + 
                    "\n\t@Consumes(\"application/json\")" +
                    "\n\tpublic Response call_" + tempServiceName + "(com.certh.iti.cloud4all.restful." + tempServiceName + "Input." + tempServiceName + "Input tmpInput)" +
                    "\n\t{" +
                    "\n\t\tcom.certh.iti.cloud4all.restful." + tempServiceName + "Output." + tempServiceName + "Output tmpOutput = null;" +
                    "\n" + 
                    "\n\t\ttry" + 
                    "\n\t\t{" +
                    "\n\t\t\tHttpResponse<JsonNode> request = Unirest.get(TO_BE_COMPLETED_MANUALLY)" +
                    "\n\t\t\t\t.header(TO_BE_COMPLETED_MANUALLY)" +
                    "\n\t\t\t\t.asJson();" +
                    "\n" +
                    "\n\t\t\ttmpOutput = TranslationManager.getInstance().gson.fromJson(request.getBody().toString(), com.certh.iti.cloud4all.restful." + tempServiceName + "Output." + tempServiceName + "Output.class);" +
                    "\n" +
                    "\n\t\t}" +
                    "\n\t\tcatch(Exception e)" +
                    "\n\t\t{" +
                    "\n\t\t\tSystem.out.println(\"\\n * " + tempServiceName + " EXCEPTION: \" + e.getMessage() + \"\\n\");" +
                    "\n\t\t}" + 
                    "\n" +
                    "\n\t\t//output" +
                    "\n\t\tString tmpOutputStr = \"\";" +
                    "\n\t\tObjectMapper mapper = new ObjectMapper();" +
                    "\n\t\ttry {" +
                    "\n\t\t\ttmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);" +
                    "\n\t\t} catch (IOException ex) {" +
                    "\n\t\t\tLogger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);" +
                    "\n\t\t}" +   
                    "\n\t\treturn Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();" +
                    "\n\t}\n\n";
            String newContents = contentBeforeBracket + functionBody + contentAfterBracket;
            //write new contents to file
            writeStringToFile(filepath, newContents);
        }
        else
            res = "lastBracketIndex = -1!!!";    
        
        return Response.ok(res).build();
    }
    
    @POST
    @Path("/generateTestFunction/{tmpServiceName}")
    public Response generateTestFunction(@PathParam("tmpServiceName") String tempServiceName)
    {
        String res = "OK";
        
        //open SST_WebServiceIT.java
        String filepath = SST_Manager.getInstance().JAVA_SRC_PATH + "/../../test/java/com/certh/iti/cloud4all/restful/SST_WebServiceIT.java";
        String fileContentStr = "";
        try {
            fileContentStr = readFileToString(filepath);
        } catch (Exception e) {
            System.out.println("generateTestFunction EXCEPTION! -> " + e.getMessage());
        }
        
        //find last "}" in the file
        int lastBracketIndex = fileContentStr.lastIndexOf("}");
        if(lastBracketIndex != -1)
        {
            String contentBeforeBracket = fileContentStr.substring(0, lastBracketIndex);
            String contentAfterBracket = fileContentStr.substring(lastBracketIndex);
            
            String functionBody = "\n" +
                    "\n\tpublic void test_" + tempServiceName + "()" +
                    "\n\t{" +
                    "\n\tcom.certh.iti.cloud4all.restful." + tempServiceName + "Input." + tempServiceName + "Input tmpInput = new com.certh.iti.cloud4all.restful." + tempServiceName + "Input." + tempServiceName + "Input();" +
                    "\n\ttmpInput.setXXXX(\"XXXX\");" +
                    "\n\tString tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);" +
                    "\n" +
                    "\n\tClient client = Client.create();" +
                    "\n\tWebResource webResource = client.resource(\"http://localhost:8080/SST/call_" + tempServiceName + "\");" +
                    "\n\tClientResponse response = webResource.accept(\"application/json\").type(\"application/json\").post(ClientResponse.class, tmpInputJson);" +
                    "\n" +
                    "\n\t// check response status code" +
                    "\n\tif (response.getStatus() != 200) {" + 
                    "\n\t\tthrow new RuntimeException(\"Failed : HTTP error code : \" + response.getStatus());" +
                    "\n\t}" +
                    "\n\tString tmpOutputJson = response.getEntity(String.class);" +        
                    "\n" +
                    "\n\tSystem.out.println(\"Testing 'call_" + tempServiceName + "' web-service...\");" +
                    "\n\tSystem.out.println(\"'call_" + tempServiceName + "' Input:\");" +
                    "\n\tSystem.out.println(tmpInputJson);" +
                    "\n\tSystem.out.println(\"'call_" + tempServiceName + "' Output:\");" +
                    "\n\tSystem.out.println(tmpOutputJson);" +
                    "\n" +
                    "\n\tcom.certh.iti.cloud4all.restful." + tempServiceName + "Output." + tempServiceName + "Output tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful." + tempServiceName + "Output." + tempServiceName + "Output.class);" +
                    "\n" +
                    "\n\tassertEquals(tmpOutput.getXXXX(), \"XXXX\");" + 
                    "\n\t}\n\n";
            
            String newContents = contentBeforeBracket + functionBody + contentAfterBracket;
            //write new contents to file
            writeStringToFile(filepath, newContents);
        }
        else
            res = "lastBracketIndex = -1!!!";    
        
        return Response.ok(res).build();
    }
    
    private Object getOutputOfAService(Map<String, Object> tmpOutputs, String tmpPreferredService)
    {
        Set s=tmpOutputs.entrySet();
        Iterator it=s.iterator();
        while(it.hasNext())
        {
            Map.Entry m =(Map.Entry)it.next();
            String key=(String)m.getKey();
            Object value=(Object)m.getValue();
            if(key.equals(tmpPreferredService))
                return value;
        }
        return null;
    }
    
    private ArrayList<String[]> getMappingsBetweenCurAndPreviousServices(String tmpCurService, ArrayList<mappedVariable> tmpMappedVariables)
    {
        ArrayList<String[]> res = new ArrayList<String[]>();
        for(int i=0; i<tmpMappedVariables.size(); i++)
        {
            mappedVariable tmpMappedVariable = tmpMappedVariables.get(i);
            if(tmpMappedVariable.getToServiceName().equals(tmpCurService))
            {
                String[] tmpItem  = new String[3];
                tmpItem[0] = tmpMappedVariable.getFromServiceName();
                tmpItem[1] = tmpMappedVariable.getFromVariableName();
                tmpItem[2] = tmpMappedVariable.getToVariableName();
                res.add(tmpItem);
            }
        }
        return res;
    }
    
    /*private ArrayList<String> getNamesOfServicesToBeCalledSequentially(ArrayList<ServiceInputTemplate> tmpInput)
    {
        ArrayList<String> res = new ArrayList<String>();
        for(int i=0; i<tmpInput.size(); i++)
        {
            ServiceInputTemplate tmpServiceInputTemplate = tmpInput.get(i);
            if(existsInList(res, tmpServiceInputTemplate.getServiceName()) == false)
                res.add(tmpServiceInputTemplate.getServiceName());
        }
        return res;
    }
    
    private boolean existsInList(ArrayList<String> tmpList, String tmpStrToSearch)
    {
        for(int i=0; i<tmpList.size(); i++)
        {
            if(tmpList.get(i).equals(tmpStrToSearch))
                return true;                    
        }
        return false;
    }*/
    
    public int findIndexOfAServiceInInputArrayList(ArrayList<ServiceInputTemplate> tmpInputArrayList, String tmpServiceName)
    {
        for(int i=0; i<tmpInputArrayList.size(); i++)
        {
            ServiceInputTemplate tmpServiceInputTemplate = tmpInputArrayList.get(i);
            if(tmpServiceInputTemplate.getServiceName().equals(tmpServiceName))
                return i;
        }
        return -1;
    }
    
    //-------------------------//
    //SYNTHESIZER CORE FUNCTION//
    //-------------------------//    
    @POST
    @Path("/callCombinedServices")
    @Consumes("application/json")
    public Response callCombinedServices(SynthesizerInput tmpInput)
    {
//        String finalResultStr = "";
//        Map<String, Object> tmpOutput = new HashMap<String, Object>();
//        Object tempoOutput = null;
//        
//        //--------------------------//
//        //Call the first web service//
//        //--------------------------//
//        Response tmpFirstServiceResponse = null;
//        if(tmpInput.getFirstServiceName().equals("IPtoLatLng"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_IPtoLatLng((com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput.class);
//            
//            tmpOutput.put("IPtoLatLng", tempoOutput);
//            finalResultStr = finalResultStr + " \"IPtoLatLng\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("Weather"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_Weather((com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput)tmpInput2.getFirstServiceInput());
//        
//            Type listType = new TypeToken<List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>>() {}.getType();
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), listType);
//            
//            tmpOutput.put("Weather", tempoOutput);
//            finalResultStr = finalResultStr + " \"Weather\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("TelizeGeolocation"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_TelizeGeolocation((com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput.class);
//        
//            tmpOutput.put("TelizeGeolocation", tempoOutput);
//            finalResultStr = finalResultStr + " \"TelizeGeolocation\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("StaticImageMap"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_StaticImageMap((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput.class);
//        
//            tmpOutput.put("StaticImageMap", tempoOutput);
//            finalResultStr = finalResultStr + " \"StaticImageMap\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("URLScreenshotGenerator"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_URLScreenshotGenerator((com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput.class);
//        
//            tmpOutput.put("URLScreenshotGenerator", tempoOutput);
//            finalResultStr = finalResultStr + " \"URLScreenshotGenerator\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("Translatewebpage"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_Translatewebpage((com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput.class);
//        
//            tmpOutput.put("Translatewebpage", tempoOutput);
//            finalResultStr = finalResultStr + " \"Translatewebpage\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("Identifyimageswithoutaltattributeinwebpage"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_Identifyimageswithoutaltattributeinwebpage((com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageOutput.IdentifyimageswithoutaltattributeinwebpageOutput.class);
//        
//            tmpOutput.put("Identifyimageswithoutaltattributeinwebpage", tempoOutput);
//            finalResultStr = finalResultStr + " \"Identifyimageswithoutaltattributeinwebpage\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("CallWebAnywhere"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_CallWebAnywhere((com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.CallWebAnywhereOutput.CallWebAnywhereOutput.class);
//        
//            tmpOutput.put("CallWebAnywhere", tempoOutput);
//            finalResultStr = finalResultStr + " \"CallWebAnywhere\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("CaptchaResolver"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_CaptchaResolver((com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput.class);
//        
//            tmpOutput.put("CaptchaResolver", tempoOutput);
//            finalResultStr = finalResultStr + " \"CaptchaResolver\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("TextToSpeech"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_TextToSpeech((com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.TextToSpeechOutput.TextToSpeechOutput.class);
//        
//            tmpOutput.put("TextToSpeech", tempoOutput);
//            finalResultStr = finalResultStr + " \"TextToSpeech\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        else if(tmpInput.getFirstServiceName().equals("FontConverter"))
//        {
//            Type inputType = new TypeToken<SynthesizerInput<com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput>>() { }.getType();
//            SynthesizerInput tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput), inputType);
//            tmpFirstServiceResponse = call_FontConverter((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tmpInput2.getFirstServiceInput());
//            tempoOutput = TranslationManager.getInstance().gson.fromJson(tmpFirstServiceResponse.getEntity().toString(), com.certh.iti.cloud4all.restful.FontConverterOutput.FontConverterOutput.class);
//        
//            tmpOutput.put("FontConverter", tempoOutput);
//            finalResultStr = finalResultStr + " \"FontConverter\": " + TranslationManager.getInstance().gson.toJson(tempoOutput) + ",";
//        }
//        //----------------------------------------------------------//
//        //(Step 1 of 5) ADD CONTENT HERE WHEN A NEW SERVICE IS ADDED//
//        //----------------------------------------------------------//
//                
//        //if the first service fails -> return NULL
//        if(tmpFirstServiceResponse == null)
//            return Response.ok("NULL!").build();
//        //else
//        //    finalResult = "\"" + tempfirstServiceToBeCalled + "\": {\n" + tmpFirstServiceResponse.getEntity().toString().substring(tmpFirstServiceResponse.getEntity().toString().indexOf('"'), tmpFirstServiceResponse.getEntity().toString().lastIndexOf('"')+1) + "\n},";
//        
//        //find the name of the services that have to be executed sequentially
//        ArrayList<String> namesOfServicesToBeCalledSequentially = getNamesOfServicesToBeCalledSequentially(tmpInput.getMappedVariables());
//        
//        for(int i=1; i<namesOfServicesToBeCalledSequentially.size(); i++)
//        {
//            String tmpCurService = namesOfServicesToBeCalledSequentially.get(i);
//            
//            tempoOutput = null;
//            Object tempoInput = null;
//            
//            //Initialize the input for current service
//            if(tmpCurService.equals("IPtoLatLng"))
//                tempoInput = new com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput();
//            else if(tmpCurService.equals("Weather"))
//                tempoInput = new com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput();                        
//            else if(tmpCurService.equals("TelizeGeolocation"))
//                tempoInput = new com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput();
//            else if(tmpCurService.equals("StaticImageMap"))
//                tempoInput = new com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput();
//            else if(tmpCurService.equals("URLScreenshotGenerator"))
//                tempoInput = new com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput();
//            else if(tmpCurService.equals("Translatewebpage"))
//                tempoInput = new com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput();
//            else if(tmpCurService.equals("Identifyimageswithoutaltattributeinwebpage"))
//                tempoInput = new com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput();
//            else if(tmpCurService.equals("CallWebAnywhere"))
//                tempoInput = new com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput();
//            else if(tmpCurService.equals("CaptchaResolver"))
//                tempoInput = new com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput();
//            else if(tmpCurService.equals("TextToSpeech"))
//                tempoInput = new com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput();
//            else if(tmpCurService.equals("FontConverter"))
//                tempoInput = new com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput();
//            //----------------------------------------------------------//
//            //(Step 2 of 5) ADD CONTENT HERE WHEN A NEW SERVICE IS ADDED//
//            //----------------------------------------------------------//
//            
//            ArrayList<String[]> mappingsBetweenCurAndPreviousServices = getMappingsBetweenCurAndPreviousServices(tmpCurService, tmpInput.getMappedVariables());
//            if(mappingsBetweenCurAndPreviousServices.size() == 0)
//                return Response.ok("EXCEPTION -> getMappingsBetweenCurAndPreviousServices size is 0! tmpCurService:" + tmpCurService + ", tmpInput.mappedVariables.size():" + Integer.toString(tmpInput.getMappedVariables().size())).build();
//            
//            //Examine the mapped variables one-by-one
//            for(int j=0; j<mappingsBetweenCurAndPreviousServices.size(); j++)
//            {
//                String[] tmpMapping = mappingsBetweenCurAndPreviousServices.get(j);
//                String tmpServiceFrom = tmpMapping[0];      //the name of the first service
//                String tmpVariableFrom = tmpMapping[1];     //the name of the first variable
//                String tmpVariableTo = tmpMapping[2];       //the name of the second variable
//                String tmpVariableFrom_value = "";          //the common value (of the two variables)
//                
//                //Initialize the output of the first sevice
//                tempoOutput = getOutputOfAService(tmpOutput, tmpServiceFrom);
//                
//                //set the common value by examining the output of the first service
//                if(tmpServiceFrom.equals("IPtoLatLng"))
//                {
//                    if(tmpVariableFrom.equals("areacode"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getAreacode();
//                    else if(tmpVariableFrom.equals("city"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getCity();
//                    else if(tmpVariableFrom.equals("countryFullName"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getCountryFullName();
//                    else if(tmpVariableFrom.equals("country"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getCountry();
//                    else if(tmpVariableFrom.equals("ip"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getIp();
//                    else if(tmpVariableFrom.equals("lat"))
//                        tmpVariableFrom_value = Double.toString(((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getLat());
//                    else if(tmpVariableFrom.equals("lng"))
//                        tmpVariableFrom_value = Double.toString(((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getLng());
//                    else if(tmpVariableFrom.equals("stateFullName"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getStateFullName();
//                    else if(tmpVariableFrom.equals("state"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getState();
//                    else if(tmpVariableFrom.equals("zip"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getZip();
//                    
//                }
//                else if(tmpServiceFrom.equals("Weather"))
//                {
//                    if(tmpVariableFrom.equals("0"))
//                        tmpVariableFrom_value = "unknown";
//                    else if(tmpVariableFrom.equals("condition"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getCondition();
//                    else if(tmpVariableFrom.equals("day_of_week"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getDayOfWeek();
//                    else if(tmpVariableFrom.equals("high_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getHighCelsius();
//                    else if(tmpVariableFrom.equals("high"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getHigh();
//                    else if(tmpVariableFrom.equals("low_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getLowCelsius();
//                    else if(tmpVariableFrom.equals("low"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getLow();
//                    
//                    else if(tmpVariableFrom.equals("1"))
//                        tmpVariableFrom_value = "unknown";
//                    else if(tmpVariableFrom.equals("1_condition"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getCondition();
//                    else if(tmpVariableFrom.equals("1_day_of_week"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getDayOfWeek();
//                    else if(tmpVariableFrom.equals("1_high_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getHighCelsius();
//                    else if(tmpVariableFrom.equals("1_high"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getHigh();
//                    else if(tmpVariableFrom.equals("1_low_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getLowCelsius();
//                    else if(tmpVariableFrom.equals("1_low"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getLow();
//                    
//                    else if(tmpVariableFrom.equals("2"))
//                        tmpVariableFrom_value = "unknown";
//                    else if(tmpVariableFrom.equals("2_condition"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getCondition();
//                    else if(tmpVariableFrom.equals("2_day_of_week"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getDayOfWeek();
//                    else if(tmpVariableFrom.equals("2_high_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getHighCelsius();
//                    else if(tmpVariableFrom.equals("2_high"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getHigh();
//                    else if(tmpVariableFrom.equals("2_low_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getLowCelsius();
//                    else if(tmpVariableFrom.equals("2_low"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getLow();
//                    
//                    else if(tmpVariableFrom.equals("3"))
//                        tmpVariableFrom_value = "unknown";
//                    else if(tmpVariableFrom.equals("3_condition"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getCondition();
//                    else if(tmpVariableFrom.equals("3_day_of_week"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getDayOfWeek();
//                    else if(tmpVariableFrom.equals("3_high_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getHighCelsius();
//                    else if(tmpVariableFrom.equals("3_high"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getHigh();
//                    else if(tmpVariableFrom.equals("3_low_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getLowCelsius();
//                    else if(tmpVariableFrom.equals("3_low"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getLow();
//                    
//                    else if(tmpVariableFrom.equals("4"))
//                        tmpVariableFrom_value = "unknown";
//                    else if(tmpVariableFrom.equals("4_condition"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getCondition();
//                    else if(tmpVariableFrom.equals("4_day_of_week"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getDayOfWeek();
//                    else if(tmpVariableFrom.equals("4_high_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getHighCelsius();
//                    else if(tmpVariableFrom.equals("4_high"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getHigh();
//                    else if(tmpVariableFrom.equals("4_low_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getLowCelsius();
//                    else if(tmpVariableFrom.equals("4_low"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getLow();
//                    
//                    else if(tmpVariableFrom.equals("5"))
//                        tmpVariableFrom_value = "unknown";
//                    else if(tmpVariableFrom.equals("5_condition"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getCondition();
//                    else if(tmpVariableFrom.equals("5_day_of_week"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getDayOfWeek();
//                    else if(tmpVariableFrom.equals("5_high_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getHighCelsius();
//                    else if(tmpVariableFrom.equals("5_high"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getHigh();
//                    else if(tmpVariableFrom.equals("5_low_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getLowCelsius();
//                    else if(tmpVariableFrom.equals("5_low"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getLow();
//                    
//                    else if(tmpVariableFrom.equals("6"))
//                        tmpVariableFrom_value = "unknown";
//                    else if(tmpVariableFrom.equals("6_condition"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getCondition();
//                    else if(tmpVariableFrom.equals("6_day_of_week"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getDayOfWeek();
//                    else if(tmpVariableFrom.equals("6_high_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getHighCelsius();
//                    else if(tmpVariableFrom.equals("6_high"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getHigh();
//                    else if(tmpVariableFrom.equals("6_low_celsius"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getLowCelsius();
//                    else if(tmpVariableFrom.equals("6_low"))
//                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getLow();
//                }
//                else if(tmpServiceFrom.equals("TelizeGeolocation"))
//                {
//                    if(tmpVariableFrom.equals("area_code"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getAreaCode();
//                    else if(tmpVariableFrom.equals("asn"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getAsn();
//                    else if(tmpVariableFrom.equals("city"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getCity();
//                    else if(tmpVariableFrom.equals("continent_code"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getContinentCode();
//                    else if(tmpVariableFrom.equals("country_code3"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getCountryCode3();
//                    else if(tmpVariableFrom.equals("country_code"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getCountryCode();
//                    else if(tmpVariableFrom.equals("country"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getCountry();
//                    else if(tmpVariableFrom.equals("dma_code"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getDmaCode();
//                    else if(tmpVariableFrom.equals("ip"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getIp();
//                    else if(tmpVariableFrom.equals("isp"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getIsp();
//                    else if(tmpVariableFrom.equals("latitude"))
//                        tmpVariableFrom_value = Double.toString(((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getLatitude());
//                    else if(tmpVariableFrom.equals("longitude"))
//                        tmpVariableFrom_value = Double.toString(((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getLongitude());
//                    else if(tmpVariableFrom.equals("region_code"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getRegionCode();
//                    else if(tmpVariableFrom.equals("region"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getRegion();
//                    else if(tmpVariableFrom.equals("timezone"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getTimezone();
//                }
//                else if(tmpServiceFrom.equals("StaticImageMap"))
//                {
//                    if(tmpVariableFrom.equals("html"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getHtml();
//                    else if(tmpVariableFrom.equals("imageUrl"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getImageUrl();
//                    else if(tmpVariableFrom.equals("providerUrl"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getProviderUrl();
//                    else if(tmpVariableFrom.equals("rateLimitUrl"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getRateLimitUrl();
//                    else if(tmpVariableFrom.equals("success"))
//                        tmpVariableFrom_value = Boolean.toString(((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getSuccess());
//                    else if(tmpVariableFrom.equals("supported"))
//                        tmpVariableFrom_value = Boolean.toString(((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getSupported());
//                    else if(tmpVariableFrom.equals("termsOfUseUrl"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getTermsOfUseUrl();
//                }
//                else if(tmpServiceFrom.equals("URLScreenshotGenerator"))
//                {
//                    if(tmpVariableFrom.equals("message"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput)tempoOutput).getMessage();
//                    else if(tmpVariableFrom.equals("screenshot"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput)tempoOutput).getScreenshot();
//                }
//                else if(tmpServiceFrom.equals("Translatewebpage"))
//                {
//                    if(tmpVariableFrom.equals("urlOfTranslatedPage"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput)tempoOutput).getUrlOfTranslatedPage();
//                    else if(tmpVariableFrom.equals("targetLanguageCode"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput)tempoOutput).getTargetLanguageCode();
//                }
//                else if(tmpServiceFrom.equals("Identifyimageswithoutaltattributeinwebpage"))
//                {
//                    if(tmpVariableFrom.equals("urlOfGeneratedPage"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageOutput.IdentifyimageswithoutaltattributeinwebpageOutput)tempoOutput).getUrlOfGeneratedPage();
//                }
//                else if(tmpServiceFrom.equals("CallWebAnywhere"))
//                {
//                    if(tmpVariableFrom.equals("urlToBeCalled"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.CallWebAnywhereOutput.CallWebAnywhereOutput)tempoOutput).getUrlToBeCalled();
//                }
//                else if(tmpServiceFrom.equals("CaptchaResolver"))
//                {
//                    if(tmpVariableFrom.equals("captcha"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput)tempoOutput).getCaptcha();
//                    else if(tmpVariableFrom.equals("api-message"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput)tempoOutput).getApiMessage();
//                }
//                else if(tmpServiceFrom.equals("TextToSpeech"))
//                {
//                    if(tmpVariableFrom.equals("spokenTextMp3URL"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TextToSpeechOutput.TextToSpeechOutput)tempoOutput).getSpokenTextMp3URL();
//                }
//                else if(tmpServiceFrom.equals("FontConverter"))
//                {
//                    if(tmpVariableFrom.equals("fontConverterReturnedUrl"))
//                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.FontConverterOutput.FontConverterOutput)tempoOutput).getFontConverterReturnedUrl();
//                }
//                //----------------------------------------------------------//
//                //(Step 3 of 5) ADD CONTENT HERE WHEN A NEW SERVICE IS ADDED//
//                //----------------------------------------------------------//
//                
//                
//                
//
//                //set the common value to the input of the second web service
//                if(tmpCurService.equals("IPtoLatLng"))
//                {
//                    if(tmpVariableTo.equals("ip"))
//                        ((com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput)tempoInput).setIp(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("Weather"))
//                {
//                    if(tmpVariableTo.equals("location"))
//                        ((com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput)tempoInput).setLocation(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("TelizeGeolocation"))
//                {
//                    if(tmpVariableTo.equals("ipAddress"))
//                       ((com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput)tempoInput).setIpAddress(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("StaticImageMap"))
//                {
//                    if(tmpVariableTo.equals("latitude"))
//                       ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setLatitude(tmpVariableFrom_value);
//                    else if(tmpVariableTo.equals("longitude"))
//                       ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setLongitude(tmpVariableFrom_value);
//                    //else if(tmpVariableTo.equals("provider"))
//                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setProvider(tmpVariableFrom_value);
//                    //else if(tmpVariableTo.equals("height"))
//                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setHeight(tmpVariableFrom_value);
//                    //else if(tmpVariableTo.equals("key"))
//                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setKey(tmpVariableFrom_value);
//                    //else if(tmpVariableTo.equals("maptype"))
//                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setMaptype(tmpVariableFrom_value);
//                    //else if(tmpVariableTo.equals("width"))
//                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setWidth(tmpVariableFrom_value);
//                    //else if(tmpVariableTo.equals("zoom"))
//                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setZoom(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("URLScreenshotGenerator"))
//                {
//                    if(tmpVariableTo.equals("url"))
//                       ((com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput)tempoInput).setUrl(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("Translatewebpage"))
//                {
//                    if(tmpVariableTo.equals("urlToTranslate"))
//                       ((com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput)tempoInput).setUrlToTranslate(tmpVariableFrom_value);
//                    else if(tmpVariableTo.equals("targetLanguage"))
//                       ((com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput)tempoInput).setTargetLanguage(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("Identifyimageswithoutaltattributeinwebpage"))
//                {
//                    if(tmpVariableTo.equals("url"))
//                       ((com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput)tempoInput).setUrl(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("CallWebAnywhere"))
//                {
//                    if(tmpVariableTo.equals("urlToOpen"))
//                       ((com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput)tempoInput).setUrlToOpen(tmpVariableFrom_value);
//                    else if(tmpVariableTo.equals("voiceLanguage"))
//                       ((com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput)tempoInput).setVoiceLanguage(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("CaptchaResolver"))
//                {
//                    if(tmpVariableTo.equals("captchaImageUrl"))
//                       ((com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput)tempoInput).setCaptchaImageUrl(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("TextToSpeech"))
//                {
//                    if(tmpVariableTo.equals("textToSpeak"))
//                       ((com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput)tempoInput).setTextToSpeak(tmpVariableFrom_value);
//                }
//                else if(tmpCurService.equals("FontConverter"))
//                {
//                    if(tmpVariableTo.equals("urlToConvertFont"))
//                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setUrlToConvertFont(tmpVariableFrom_value);
//                    else if(tmpVariableTo.equals("targetFontFamily"))
//                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setTargetFontFamily(tmpVariableFrom_value);
//                    else if(tmpVariableTo.equals("targetFontSize"))
//                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setTargetFontSize(tmpVariableFrom_value);
//                    else if(tmpVariableTo.equals("targetColor"))
//                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setTargetColor(tmpVariableFrom_value);
//                    else if(tmpVariableTo.equals("targetBackground"))
//                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setTargetBackground(tmpVariableFrom_value);
//                }
//                //----------------------------------------------------------//
//                //(Step 4 of 5) ADD CONTENT HERE WHEN A NEW SERVICE IS ADDED//
//                //----------------------------------------------------------//
//            }
//            
//            //CALL THE CURRENT SERVICE
//            if(tmpCurService.equals("IPtoLatLng"))
//            {
//                Response tempRes = call_IPtoLatLng((com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput.class);
//                tmpOutput.put("IPtoLatLng", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"IPtoLatLng\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().lastIndexOf('"')+1) + "\n},";
//            }
//            else if(tmpCurService.equals("Weather"))
//            {
//                Response tempRes = call_Weather((com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput)tempoInput);
//                
//                Type listType = new TypeToken<List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>>() {}.getType();
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), listType);
//                tmpOutput.put("Weather", tempoOutput);
//                
//                String weatherResStr = tempRes.getEntity().toString();
//                
//                //condition
//                String strToSearch = "\"condition";
//                int firstIndex = weatherResStr.indexOf(strToSearch);
//                String firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
//                String secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
//                int dayCounter = 1;
//                while(secondPartStr.indexOf(strToSearch) != -1)
//                {
//                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_condition");
//                    dayCounter++;
//                }
//                weatherResStr = firstPartStr + secondPartStr;
//                
//                //high_celsius
//                strToSearch = "\"high_celsius";
//                firstIndex = weatherResStr.indexOf(strToSearch);
//                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
//                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
//                dayCounter = 1;
//                while(secondPartStr.indexOf(strToSearch) != -1)
//                {
//                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_high_celsius");
//                    dayCounter++;
//                }
//                weatherResStr = firstPartStr + secondPartStr;
//                
//                //high
//                strToSearch = "\"high\"";
//                firstIndex = weatherResStr.indexOf(strToSearch);
//                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
//                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
//                dayCounter = 1;
//                while(secondPartStr.indexOf(strToSearch) != -1)
//                {
//                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_high\"");
//                    dayCounter++;
//                }
//                weatherResStr = firstPartStr + secondPartStr;
//                
//                //low_celsius
//                strToSearch = "\"low_celsius";
//                firstIndex = weatherResStr.indexOf(strToSearch);
//                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
//                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
//                dayCounter = 1;
//                while(secondPartStr.indexOf(strToSearch) != -1)
//                {
//                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_low_celsius");
//                    dayCounter++;
//                }
//                weatherResStr = firstPartStr + secondPartStr;
//                
//                //low
//                strToSearch = "\"low\"";
//                firstIndex = weatherResStr.indexOf(strToSearch);
//                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
//                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
//                dayCounter = 1;
//                while(secondPartStr.indexOf(strToSearch) != -1)
//                {
//                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_low\"");
//                    dayCounter++;
//                }
//                weatherResStr = firstPartStr + secondPartStr;
//                
//                //day_of_week
//                strToSearch = "\"day_of_week";
//                firstIndex = weatherResStr.indexOf(strToSearch);
//                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
//                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
//                dayCounter = 1;
//                while(secondPartStr.indexOf(strToSearch) != -1)
//                {
//                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_day_of_week");
//                    dayCounter++;
//                }
//                weatherResStr = firstPartStr + secondPartStr;
//                
//                weatherResStr = weatherResStr.replaceAll("[{}]", "");       //remove all { and }
//                weatherResStr = weatherResStr.replaceAll("\\[|\\]", "");    //remove all [ and ]
//                
//                finalResultStr = finalResultStr + "\n\"Weather\": {\n" + weatherResStr + "\n},";
//            }
//            else if(tmpCurService.equals("TelizeGeolocation"))
//            {
//                Response tempRes = call_TelizeGeolocation((com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput.class);
//                tmpOutput.put("TelizeGeolocation", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"TelizeGeolocation\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().lastIndexOf('"')+1) + "\n},";
//            }
//            else if(tmpCurService.equals("StaticImageMap"))
//            {
//                Response tempRes = call_StaticImageMap((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput.class);
//                tmpOutput.put("StaticImageMap", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"StaticImageMap\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
//            }
//            else if(tmpCurService.equals("URLScreenshotGenerator"))
//            {
//                Response tempRes = call_URLScreenshotGenerator((com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput.class);
//                tmpOutput.put("URLScreenshotGenerator", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"URLScreenshotGenerator\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
//            }
//            else if(tmpCurService.equals("Translatewebpage"))
//            {
//                Response tempRes = call_Translatewebpage((com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput.class);
//                tmpOutput.put("Translatewebpage", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"Translatewebpage\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
//            }
//            else if(tmpCurService.equals("Identifyimageswithoutaltattributeinwebpage"))
//            {
//                Response tempRes = call_Identifyimageswithoutaltattributeinwebpage((com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageOutput.IdentifyimageswithoutaltattributeinwebpageOutput.class);
//                tmpOutput.put("Identifyimageswithoutaltattributeinwebpage", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"Identifyimageswithoutaltattributeinwebpage\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
//            }
//            else if(tmpCurService.equals("CallWebAnywhere"))
//            {
//                Response tempRes = call_CallWebAnywhere((com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.CallWebAnywhereOutput.CallWebAnywhereOutput.class);
//                tmpOutput.put("CallWebAnywhere", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"CallWebAnywhere\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
//            }
//            else if(tmpCurService.equals("CaptchaResolver"))
//            {
//                Response tempRes = call_CaptchaResolver((com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput.class);
//                tmpOutput.put("CaptchaResolver", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"CaptchaResolver\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
//            }
//            else if(tmpCurService.equals("TextToSpeech"))
//            {
//                Response tempRes = call_TextToSpeech((com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.TextToSpeechOutput.TextToSpeechOutput.class);
//                tmpOutput.put("TextToSpeech", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"TextToSpeech\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
//            }
//            else if(tmpCurService.equals("FontConverter"))
//            {
//                Response tempRes = call_FontConverter((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput);
//                
//                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.FontConverterOutput.FontConverterOutput.class);
//                tmpOutput.put("FontConverter", tempoOutput);
//                
//                finalResultStr = finalResultStr + "\n\"FontConverter\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
//            }
//            //----------------------------------------------------------//
//            //(Step 5 of 5) ADD CONTENT HERE WHEN A NEW SERVICE IS ADDED//
//            //----------------------------------------------------------//
//        }      
//        
//        finalResultStr = "{ " + finalResultStr.substring(0, finalResultStr.lastIndexOf(",")) + "\n }";
//        
//        //output
//        /*String tmpOutputStr = "";
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//                tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
//        } catch (IOException ex) {
//                Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
//        }*/
//        return Response.ok(finalResultStr, MediaType.APPLICATION_JSON).build();
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        String finalResultStr = "";
        Map<String, Object> tmpOutput = new HashMap<String, Object>();
        Object tempoOutput = null;

        //execute the services sequentially
        for(int i=0; i<tmpInput.getInput().size(); i++)
        {
            ServiceInputTemplate tmpServiceInputTemplate = tmpInput.getInput().get(i);
            String tmpCurService = tmpServiceInputTemplate.getServiceName();
            
            tempoOutput = null;
            Object tempoInput = null;
            
            //Initialize the input for current service
            if(tmpCurService.equals("IPtoLatLng"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("Weather"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("TelizeGeolocation"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("StaticImageMap"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("URLScreenshotGenerator"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("Translatewebpage"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("Identifyimageswithoutaltattributeinwebpage"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("CallWebAnywhere"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("CaptchaResolver"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("TextToSpeech"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput) tmpInput2.getServiceInput();
            }
            else if(tmpCurService.equals("FontConverter"))
            {
                tempoInput = new com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput();
                Type inputType = new TypeToken<ServiceInputTemplate<com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput>>() { }.getType();

                ServiceInputTemplate tmpInput2 = TranslationManager.getInstance().gson.fromJson(TranslationManager.getInstance().gson.toJson(tmpInput.getInput().get(findIndexOfAServiceInInputArrayList(tmpInput.getInput(), tmpCurService))), inputType);
                if(tmpInput2.getServiceInput() != null)
                    tempoInput = (com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput) tmpInput2.getServiceInput();
            }
            //----------------------------------------------------------//
            //(Step 1 of 4) ADD CONTENT HERE WHEN A NEW SERVICE IS ADDED//
            //----------------------------------------------------------//
            
            ArrayList<String[]> mappingsBetweenCurAndPreviousServices = getMappingsBetweenCurAndPreviousServices(tmpCurService, tmpInput.getMappedVariables());
                        
            //Examine the mapped variables one-by-one
            for(int j=0; j<mappingsBetweenCurAndPreviousServices.size(); j++)
            {
                String[] tmpMapping = mappingsBetweenCurAndPreviousServices.get(j);
                String tmpServiceFrom = tmpMapping[0];      //the name of the first service
                String tmpVariableFrom = tmpMapping[1];     //the name of the first variable
                String tmpVariableTo = tmpMapping[2];       //the name of the second variable
                String tmpVariableFrom_value = "";          //the common value (of the two variables)
                
                //Initialize the output of the previous service (whose output is used by the current service)
                tempoOutput = getOutputOfAService(tmpOutput, tmpServiceFrom);
                
                //set the common value by examining the output of the previous service (whose output is used by the current service)
                if(tmpServiceFrom.equals("IPtoLatLng"))
                {
                    if(tmpVariableFrom.equals("areacode"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getAreacode();
                    else if(tmpVariableFrom.equals("city"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getCity();
                    else if(tmpVariableFrom.equals("countryFullName"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getCountryFullName();
                    else if(tmpVariableFrom.equals("country"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getCountry();
                    else if(tmpVariableFrom.equals("ip"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getIp();
                    else if(tmpVariableFrom.equals("lat"))
                        tmpVariableFrom_value = Double.toString(((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getLat());
                    else if(tmpVariableFrom.equals("lng"))
                        tmpVariableFrom_value = Double.toString(((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getLng());
                    else if(tmpVariableFrom.equals("stateFullName"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getStateFullName();
                    else if(tmpVariableFrom.equals("state"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getState();
                    else if(tmpVariableFrom.equals("zip"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput)tempoOutput).getZip();
                }
                else if(tmpServiceFrom.equals("Weather"))
                {
                    if(tmpVariableFrom.equals("0"))
                        tmpVariableFrom_value = "unknown";
                    else if(tmpVariableFrom.equals("condition"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getCondition();
                    else if(tmpVariableFrom.equals("day_of_week"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getDayOfWeek();
                    else if(tmpVariableFrom.equals("high_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getHighCelsius();
                    else if(tmpVariableFrom.equals("high"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getHigh();
                    else if(tmpVariableFrom.equals("low_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getLowCelsius();
                    else if(tmpVariableFrom.equals("low"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(0).getLow();
                    
                    else if(tmpVariableFrom.equals("1"))
                        tmpVariableFrom_value = "unknown";
                    else if(tmpVariableFrom.equals("1_condition"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getCondition();
                    else if(tmpVariableFrom.equals("1_day_of_week"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getDayOfWeek();
                    else if(tmpVariableFrom.equals("1_high_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getHighCelsius();
                    else if(tmpVariableFrom.equals("1_high"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getHigh();
                    else if(tmpVariableFrom.equals("1_low_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getLowCelsius();
                    else if(tmpVariableFrom.equals("1_low"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(1).getLow();
                    
                    else if(tmpVariableFrom.equals("2"))
                        tmpVariableFrom_value = "unknown";
                    else if(tmpVariableFrom.equals("2_condition"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getCondition();
                    else if(tmpVariableFrom.equals("2_day_of_week"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getDayOfWeek();
                    else if(tmpVariableFrom.equals("2_high_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getHighCelsius();
                    else if(tmpVariableFrom.equals("2_high"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getHigh();
                    else if(tmpVariableFrom.equals("2_low_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getLowCelsius();
                    else if(tmpVariableFrom.equals("2_low"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(2).getLow();
                    
                    else if(tmpVariableFrom.equals("3"))
                        tmpVariableFrom_value = "unknown";
                    else if(tmpVariableFrom.equals("3_condition"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getCondition();
                    else if(tmpVariableFrom.equals("3_day_of_week"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getDayOfWeek();
                    else if(tmpVariableFrom.equals("3_high_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getHighCelsius();
                    else if(tmpVariableFrom.equals("3_high"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getHigh();
                    else if(tmpVariableFrom.equals("3_low_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getLowCelsius();
                    else if(tmpVariableFrom.equals("3_low"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(3).getLow();
                    
                    else if(tmpVariableFrom.equals("4"))
                        tmpVariableFrom_value = "unknown";
                    else if(tmpVariableFrom.equals("4_condition"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getCondition();
                    else if(tmpVariableFrom.equals("4_day_of_week"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getDayOfWeek();
                    else if(tmpVariableFrom.equals("4_high_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getHighCelsius();
                    else if(tmpVariableFrom.equals("4_high"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getHigh();
                    else if(tmpVariableFrom.equals("4_low_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getLowCelsius();
                    else if(tmpVariableFrom.equals("4_low"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(4).getLow();
                    
                    else if(tmpVariableFrom.equals("5"))
                        tmpVariableFrom_value = "unknown";
                    else if(tmpVariableFrom.equals("5_condition"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getCondition();
                    else if(tmpVariableFrom.equals("5_day_of_week"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getDayOfWeek();
                    else if(tmpVariableFrom.equals("5_high_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getHighCelsius();
                    else if(tmpVariableFrom.equals("5_high"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getHigh();
                    else if(tmpVariableFrom.equals("5_low_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getLowCelsius();
                    else if(tmpVariableFrom.equals("5_low"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(5).getLow();
                    
                    else if(tmpVariableFrom.equals("6"))
                        tmpVariableFrom_value = "unknown";
                    else if(tmpVariableFrom.equals("6_condition"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getCondition();
                    else if(tmpVariableFrom.equals("6_day_of_week"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getDayOfWeek();
                    else if(tmpVariableFrom.equals("6_high_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getHighCelsius();
                    else if(tmpVariableFrom.equals("6_high"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getHigh();
                    else if(tmpVariableFrom.equals("6_low_celsius"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getLowCelsius();
                    else if(tmpVariableFrom.equals("6_low"))
                        tmpVariableFrom_value = ((List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>)tempoOutput).get(6).getLow();
                }
                else if(tmpServiceFrom.equals("TelizeGeolocation"))
                {
                    if(tmpVariableFrom.equals("area_code"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getAreaCode();
                    else if(tmpVariableFrom.equals("asn"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getAsn();
                    else if(tmpVariableFrom.equals("city"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getCity();
                    else if(tmpVariableFrom.equals("continent_code"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getContinentCode();
                    else if(tmpVariableFrom.equals("country_code3"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getCountryCode3();
                    else if(tmpVariableFrom.equals("country_code"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getCountryCode();
                    else if(tmpVariableFrom.equals("country"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getCountry();
                    else if(tmpVariableFrom.equals("dma_code"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getDmaCode();
                    else if(tmpVariableFrom.equals("ip"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getIp();
                    else if(tmpVariableFrom.equals("isp"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getIsp();
                    else if(tmpVariableFrom.equals("latitude"))
                        tmpVariableFrom_value = Double.toString(((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getLatitude());
                    else if(tmpVariableFrom.equals("longitude"))
                        tmpVariableFrom_value = Double.toString(((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getLongitude());
                    else if(tmpVariableFrom.equals("region_code"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getRegionCode();
                    else if(tmpVariableFrom.equals("region"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getRegion();
                    else if(tmpVariableFrom.equals("timezone"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput)tempoOutput).getTimezone();
                }
                else if(tmpServiceFrom.equals("StaticImageMap"))
                {
                    if(tmpVariableFrom.equals("html"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getHtml();
                    else if(tmpVariableFrom.equals("imageUrl"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getImageUrl();
                    else if(tmpVariableFrom.equals("providerUrl"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getProviderUrl();
                    else if(tmpVariableFrom.equals("rateLimitUrl"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getRateLimitUrl();
                    else if(tmpVariableFrom.equals("success"))
                        tmpVariableFrom_value = Boolean.toString(((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getSuccess());
                    else if(tmpVariableFrom.equals("supported"))
                        tmpVariableFrom_value = Boolean.toString(((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getSupported());
                    else if(tmpVariableFrom.equals("termsOfUseUrl"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput)tempoOutput).getTermsOfUseUrl();
                }
                else if(tmpServiceFrom.equals("URLScreenshotGenerator"))
                {
                    if(tmpVariableFrom.equals("message"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput)tempoOutput).getMessage();
                    else if(tmpVariableFrom.equals("screenshot"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput)tempoOutput).getScreenshot();
                }
                else if(tmpServiceFrom.equals("Translatewebpage"))
                {
                    if(tmpVariableFrom.equals("finalUrl"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput)tempoOutput).getFinalUrl();
                    else if(tmpVariableFrom.equals("targetLanguageCode"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput)tempoOutput).getTargetLanguageCode();
                }
                else if(tmpServiceFrom.equals("Identifyimageswithoutaltattributeinwebpage"))
                {
                    if(tmpVariableFrom.equals("finalUrl"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageOutput.IdentifyimageswithoutaltattributeinwebpageOutput)tempoOutput).getFinalUrl();
                }
                else if(tmpServiceFrom.equals("CallWebAnywhere"))
                {
                    if(tmpVariableFrom.equals("finalUrl"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.CallWebAnywhereOutput.CallWebAnywhereOutput)tempoOutput).getFinalUrl();
                }
                else if(tmpServiceFrom.equals("CaptchaResolver"))
                {
                    if(tmpVariableFrom.equals("captcha"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput)tempoOutput).getCaptcha();
                    else if(tmpVariableFrom.equals("api-message"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput)tempoOutput).getApiMessage();
                }
                else if(tmpServiceFrom.equals("TextToSpeech"))
                {
                    if(tmpVariableFrom.equals("spokenTextMp3URL"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.TextToSpeechOutput.TextToSpeechOutput)tempoOutput).getSpokenTextMp3URL();
                }
                else if(tmpServiceFrom.equals("FontConverter"))
                {
                	
                    if(tmpVariableFrom.equals("finalUrl"))
                        tmpVariableFrom_value = (String)((com.certh.iti.cloud4all.restful.FontConverterOutput.FontConverterOutput)tempoOutput).getFinalUrl();
                }
                //----------------------------------------------------------//
                //(Step 2 of 4) ADD CONTENT HERE WHEN A NEW SERVICE IS ADDED//
                //----------------------------------------------------------//
                
                //set the common value to the input of the current web service
                if(tmpCurService.equals("IPtoLatLng"))
                {
                    if(tmpVariableTo.equals("ip"))
                        ((com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput)tempoInput).setIp(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("Weather"))
                {
                    if(tmpVariableTo.equals("location"))
                        ((com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput)tempoInput).setLocation(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("TelizeGeolocation"))
                {
                    if(tmpVariableTo.equals("ipAddress"))
                       ((com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput)tempoInput).setIpAddress(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("StaticImageMap"))
                {
                    if(tmpVariableTo.equals("latitude"))
                       ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setLatitude(tmpVariableFrom_value);
                    else if(tmpVariableTo.equals("longitude"))
                       ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setLongitude(tmpVariableFrom_value);
                    //else if(tmpVariableTo.equals("provider"))
                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setProvider(tmpVariableFrom_value);
                    //else if(tmpVariableTo.equals("height"))
                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setHeight(tmpVariableFrom_value);
                    //else if(tmpVariableTo.equals("key"))
                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setKey(tmpVariableFrom_value);
                    //else if(tmpVariableTo.equals("maptype"))
                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setMaptype(tmpVariableFrom_value);
                    //else if(tmpVariableTo.equals("width"))
                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setWidth(tmpVariableFrom_value);
                    //else if(tmpVariableTo.equals("zoom"))
                    //   ((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput).setZoom(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("URLScreenshotGenerator"))
                {
                    if(tmpVariableTo.equals("inputUrl"))
                       ((com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput)tempoInput).setInputUrl(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("Translatewebpage"))
                {
                    if(tmpVariableTo.equals("inputUrl"))
                       ((com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput)tempoInput).setInputUrl(tmpVariableFrom_value);
                    else if(tmpVariableTo.equals("targetLanguage"))
                       ((com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput)tempoInput).setTargetLanguage(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("Identifyimageswithoutaltattributeinwebpage"))
                {
                    if(tmpVariableTo.equals("inputUrl"))
                       ((com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput)tempoInput).setInputUrl(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("CallWebAnywhere"))
                {
                    if(tmpVariableTo.equals("inputUrl"))
                       ((com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput)tempoInput).setInputUrl(tmpVariableFrom_value);
                    else if(tmpVariableTo.equals("voiceLanguage"))
                       ((com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput)tempoInput).setVoiceLanguage(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("CaptchaResolver"))
                {
                	
                    if(tmpVariableTo.equals("inputUrl"))
                       ((com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput)tempoInput).setInputUrl(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("TextToSpeech"))
                {
                    if(tmpVariableTo.equals("textToSpeak"))
                       ((com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput)tempoInput).setTextToSpeak(tmpVariableFrom_value);
                }
                else if(tmpCurService.equals("FontConverter"))
                {
            
                    if(tmpVariableTo.equals("inputUrl"))
                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setInputUrl(tmpVariableFrom_value);
                    else if(tmpVariableTo.equals("targetFontFamily"))
                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setTargetFontFamily(tmpVariableFrom_value);
                    else if(tmpVariableTo.equals("targetFontSize"))
                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setTargetFontSize(tmpVariableFrom_value);
                    else if(tmpVariableTo.equals("targetColor"))
                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setTargetColor(tmpVariableFrom_value);
                    else if(tmpVariableTo.equals("targetBackground"))
                       ((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput).setTargetBackground(tmpVariableFrom_value);
                }
                //----------------------------------------------------------//
                //(Step 3 of 4) ADD CONTENT HERE WHEN A NEW SERVICE IS ADDED//
                //----------------------------------------------------------//
            }
            
            //CALL THE CURRENT SERVICE
            if(tmpCurService.equals("IPtoLatLng"))
            {
                Response tempRes = call_IPtoLatLng((com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput.class);
                tmpOutput.put("IPtoLatLng", tempoOutput);
                
                finalResultStr = finalResultStr + "\n\"IPtoLatLng\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().lastIndexOf('"')+1) + "\n},";
            }
            else if(tmpCurService.equals("Weather"))
            {
                Response tempRes = call_Weather((com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput)tempoInput);
                
                Type listType = new TypeToken<List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>>() {}.getType();
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), listType);
                tmpOutput.put("Weather", tempoOutput);
                
                String weatherResStr = tempRes.getEntity().toString();
                
                //condition
                String strToSearch = "\"condition";
                int firstIndex = weatherResStr.indexOf(strToSearch);
                String firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
                String secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
                int dayCounter = 1;
                while(secondPartStr.indexOf(strToSearch) != -1)
                {
                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_condition");
                    dayCounter++;
                }
                weatherResStr = firstPartStr + secondPartStr;
                
                //high_celsius
                strToSearch = "\"high_celsius";
                firstIndex = weatherResStr.indexOf(strToSearch);
                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
                dayCounter = 1;
                while(secondPartStr.indexOf(strToSearch) != -1)
                {
                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_high_celsius");
                    dayCounter++;
                }
                weatherResStr = firstPartStr + secondPartStr;
                
                //high
                strToSearch = "\"high\"";
                firstIndex = weatherResStr.indexOf(strToSearch);
                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
                dayCounter = 1;
                while(secondPartStr.indexOf(strToSearch) != -1)
                {
                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_high\"");
                    dayCounter++;
                }
                weatherResStr = firstPartStr + secondPartStr;
                
                //low_celsius
                strToSearch = "\"low_celsius";
                firstIndex = weatherResStr.indexOf(strToSearch);
                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
                dayCounter = 1;
                while(secondPartStr.indexOf(strToSearch) != -1)
                {
                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_low_celsius");
                    dayCounter++;
                }
                weatherResStr = firstPartStr + secondPartStr;
                
                //low
                strToSearch = "\"low\"";
                firstIndex = weatherResStr.indexOf(strToSearch);
                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
                dayCounter = 1;
                while(secondPartStr.indexOf(strToSearch) != -1)
                {
                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_low\"");
                    dayCounter++;
                }
                weatherResStr = firstPartStr + secondPartStr;
                
                //day_of_week
                strToSearch = "\"day_of_week";
                firstIndex = weatherResStr.indexOf(strToSearch);
                firstPartStr = weatherResStr.substring(0, firstIndex + strToSearch.length());
                secondPartStr = weatherResStr.substring(firstIndex + strToSearch.length());
                dayCounter = 1;
                while(secondPartStr.indexOf(strToSearch) != -1)
                {
                    secondPartStr = secondPartStr.replaceFirst(strToSearch, "\"" + Integer.toString(dayCounter) + "_day_of_week");
                    dayCounter++;
                }
                weatherResStr = firstPartStr + secondPartStr;
                
                weatherResStr = weatherResStr.replaceAll("[{}]", "");       //remove all { and }
                weatherResStr = weatherResStr.replaceAll("\\[|\\]", "");    //remove all [ and ]
                
                finalResultStr = finalResultStr + "\n\"Weather\": {\n" + weatherResStr + "\n},";
            }
            else if(tmpCurService.equals("TelizeGeolocation"))
            {
                Response tempRes = call_TelizeGeolocation((com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput.class);
                tmpOutput.put("TelizeGeolocation", tempoOutput);
                
                finalResultStr = finalResultStr + "\n\"TelizeGeolocation\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().lastIndexOf('"')+1) + "\n},";
            }
            else if(tmpCurService.equals("StaticImageMap"))
            {
                Response tempRes = call_StaticImageMap((com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput.class);
                tmpOutput.put("StaticImageMap", tempoOutput);
                
                finalResultStr = finalResultStr + "\n\"StaticImageMap\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
            }
            else if(tmpCurService.equals("URLScreenshotGenerator"))
            {
                Response tempRes = call_URLScreenshotGenerator((com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput.class);
                tmpOutput.put("URLScreenshotGenerator", tempoOutput);
                
				if (tempoOutput.toString().contains("finalUrl")) {
					String[] splitted = tempoOutput.toString().split(
							"finalUrl");
					finalUrl = splitted[1].substring(0,
							splitted[1].indexOf(",")).replace("=", "").trim();
				//	System.out.println("MY URL "+finalUrl);
				}
                
                finalResultStr = finalResultStr + "\n\"URLScreenshotGenerator\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
            }
            else if(tmpCurService.equals("Translatewebpage"))
            {
                Response tempRes = call_Translatewebpage((com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput.class);
                tmpOutput.put("Translatewebpage", tempoOutput);
                
                if (tempoOutput.toString().contains("finalUrl")) {
					String[] splitted = tempoOutput.toString().split(
							"finalUrl");
					finalUrl = splitted[1].substring(0,
							splitted[1].indexOf(",")).replace("=", "").trim();
				//	System.out.println("MY URL "+finalUrl);
				}
                
                finalResultStr = finalResultStr + "\n\"Translatewebpage\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
            }
            else if(tmpCurService.equals("Identifyimageswithoutaltattributeinwebpage"))
            {
                Response tempRes = call_Identifyimageswithoutaltattributeinwebpage((com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageOutput.IdentifyimageswithoutaltattributeinwebpageOutput.class);
                tmpOutput.put("Identifyimageswithoutaltattributeinwebpage", tempoOutput);
                
                if (tempoOutput.toString().contains("finalUrl")) {
                	String[] splitted = tempoOutput.toString().split(
							"finalUrl");
					finalUrl = splitted[1].substring(0,
							splitted[1].indexOf(",")).replace("=", "").trim();
				//	System.out.println("MY URL "+finalUrl);
				}
                
                finalResultStr = finalResultStr + "\n\"Identifyimageswithoutaltattributeinwebpage\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
            }
            else if(tmpCurService.equals("CallWebAnywhere"))
            {
                Response tempRes = call_CallWebAnywhere((com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.CallWebAnywhereOutput.CallWebAnywhereOutput.class);
                tmpOutput.put("CallWebAnywhere", tempoOutput);
                
                if (tempoOutput.toString().contains("finalUrl")) {
                	String[] splitted = tempoOutput.toString().split(
							"finalUrl");
					finalUrl = splitted[1].substring(0,
							splitted[1].indexOf(",")).replace("=", "").trim();
			//		System.out.println("MY URL "+finalUrl);
				}
                
                finalResultStr = finalResultStr + "\n\"CallWebAnywhere\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
            }
            else if(tmpCurService.equals("CaptchaResolver"))
            {
                Response tempRes = call_CaptchaResolver((com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput.class);
                tmpOutput.put("CaptchaResolver", tempoOutput);
                
                if (tempoOutput.toString().contains("finalUrl")) {
                	String[] splitted = tempoOutput.toString().split(
							"finalUrl");
					finalUrl = splitted[1].substring(0,
							splitted[1].indexOf(",")).replace("=", "").trim();
				//	System.out.println("MY URL "+finalUrl);
				}
                
                finalResultStr = finalResultStr + "\n\"CaptchaResolver\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
            }
            else if(tmpCurService.equals("TextToSpeech"))
            {
                Response tempRes = call_TextToSpeech((com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.TextToSpeechOutput.TextToSpeechOutput.class);
                tmpOutput.put("TextToSpeech", tempoOutput);
                
                finalResultStr = finalResultStr + "\n\"TextToSpeech\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
            }
            else if(tmpCurService.equals("FontConverter"))
            {
                Response tempRes = call_FontConverter((com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput)tempoInput);
                
                tempoOutput = TranslationManager.getInstance().gson.fromJson(tempRes.getEntity().toString(), com.certh.iti.cloud4all.restful.FontConverterOutput.FontConverterOutput.class);
                tmpOutput.put("FontConverter", tempoOutput);
                
                if (tempoOutput.toString().contains("finalUrl")) {
                	String[] splitted = tempoOutput.toString().split(
							"finalUrl");
					finalUrl = splitted[1].substring(0,
							splitted[1].indexOf(",")).replace("=", "").trim();
				//	System.out.println("MY URL "+finalUrl);
				}
                
                finalResultStr = finalResultStr + "\n\"FontConverter\": {\n" + tempRes.getEntity().toString().substring(tempRes.getEntity().toString().indexOf('"'), tempRes.getEntity().toString().length()-1) + "\n},";
            }
            //----------------------------------------------------------//
            //(Step 4 of 4) ADD CONTENT HERE WHEN A NEW SERVICE IS ADDED//
            //----------------------------------------------------------//
        }      
        
        finalResultStr = "{ \"finalUrl\": \""+finalUrl+"\",\n \"details\": {" + finalResultStr.substring(0, finalResultStr.lastIndexOf(",")) + "\n } \n }";

        return Response.ok(finalResultStr, MediaType.APPLICATION_JSON).build();
    }
    
/************************/
/* CORE FUNCTIONS - END */
/************************/
    
    public boolean urlIsValid(String tmpURL)
    {
        SST_Manager.getInstance().URLValidationAlreadyPerformed++;
        HttpURLConnection huc = null;
        int code = -1;
        try
        {
            //HttpURLConnection.setFollowRedirects(false);
            URL u = new URL (tmpURL);
            huc =  (HttpURLConnection)u.openConnection(); 
            //huc.setRequestMethod ("GET");  //OR  
            huc.setRequestMethod ("HEAD");
            huc.connect() ; 
            code = huc.getResponseCode() ;
            //System.out.println(code);
            //if(code != 404) //404: not found
            if(code ==  HttpURLConnection.HTTP_OK)
            {
                //System.out.println("...(" + SST_Manager.getInstance().URLValidationAlreadyPerformed + ") YES! urlIsValid function -> " + tmpURL + " IS valid!");
                huc.disconnect();
                return true;            
            }
        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
        }
        finally {
            if (huc != null) {
		huc.disconnect();
            }
	}  
        //System.out.println("......(" + SST_Manager.getInstance().URLValidationAlreadyPerformed + ") PROBLEM! urlIsValid function -> " + tmpURL + " is NOT valid! [code: " + SST_Manager.getInstance().HTTPStatusCodes.get(code) + "]");
        return false;        
    }
        
    public String getEncodingOfURL(String tmpURL)
    {
        String res = "UTF-8";
        try
        {
            URL url = new URL(tmpURL);
            System.out.println("TRYING TO OPEN: " + tmpURL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String allHTMLStr = "";
            String line = "";
            while ((line = reader.readLine()) != null) 
            {
                allHTMLStr = allHTMLStr + line;
                allHTMLStr = allHTMLStr + "\n";
            }
            Document doc = Jsoup.parse(allHTMLStr);

            Elements allElements = doc.getAllElements(); 
            for(Element el : allElements)
            {
                if(el.nodeName().toLowerCase().equals("meta"))
                {
                    if(el.hasAttr("content"))
                    {
                        String tmpContent = el.attr("content");
                        int charsetIndex = tmpContent.toLowerCase().indexOf("charset");
                        if(charsetIndex != -1)
                        {
                            String tmpCharset = tmpContent.substring(charsetIndex + 7);
                            int equalIndex = tmpCharset.indexOf("=");
                            res = tmpCharset.substring(equalIndex + 1);
                        }
                    }
                }
            }
            reader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    private String readFileToString(String filepath) throws IOException 
    {
        File file = new File(filepath);
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");
        try {
            while(scanner.hasNextLine()) {        
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    public void deleteOldTemporaryFiles()
    {
  if(SST_Manager.getInstance().USE_TIMESTAMP_IN_OUTPUT_FILENAMES)
        {
        Long curMilliseconds = System.currentTimeMillis();
        File directory = new File(SST_Manager.getInstance().HTDOCS_DIR);
 
        //get all the files from a directory
        File[] fList = directory.listFiles();
 
        for (File file : fList){
            if (file.isFile())
            {
                String tmpFilename = file.getName();
                if(tmpFilename.equals(".DS_Store") == false)
                {
                    int underscoreIndex = tmpFilename.indexOf("_");
                    int dotIndex = tmpFilename.indexOf(".");
                    
                    if(underscoreIndex!=-1 && dotIndex!=-1)
                    {
                        String timeOfCreationInMillisecondsStr = tmpFilename.substring(underscoreIndex+1, dotIndex);
                        Long timeOfCreationInMilliseconds = Long.parseLong(timeOfCreationInMillisecondsStr);
                        if( (curMilliseconds-timeOfCreationInMilliseconds) > (SST_Manager.getInstance().DELETE_FILES_OLDER_THAN_THESE_MINUTES*60*1000) )
                        {
                            File f = new File(SST_Manager.getInstance().HTDOCS_DIR + "//" + tmpFilename);
                            try{
                                f.delete();
                            }catch(Exception e){
                                // if any error occurs
                                e.printStackTrace();
}
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static int nthOccurrence(String s, char c, int occurrence) {
        return nthOccurrence(s, 0, c, 0, occurrence);
    }

    public static int nthOccurrence(String s, int from, char c, int curr, int expected) {
        final int index = s.indexOf(c, from);
        if(index == -1) return -1;
        return (curr + 1 == expected) ? index : 
            nthOccurrence(s, index + 1, c, curr + 1, expected);
    }
    
    public String combinedStringsWithSlashBetweenThem(String str1, String str2)
    {
        String res = "";
        
        boolean str1EndsWithSlash = false;
        if(str1.charAt(str1.length()-1) == '/')
            str1EndsWithSlash = true;
        boolean str2StartsWithSlash = false;
        if(str2.charAt(0) == '/')
            str2StartsWithSlash = true;
        
        if(str1EndsWithSlash==false && str2StartsWithSlash==false)
            res = str1 + "/" + str2;
        else if( (str1EndsWithSlash==true && str2StartsWithSlash==false)
                    || (str1EndsWithSlash==false && str2StartsWithSlash==true) )
            res = str1 + str2;
        else if(str1EndsWithSlash==true && str2StartsWithSlash==true)
            res = str1.substring(0, str1.length()-1) + str2;
        return res;
    }
    
    public String replaceRelativePathsWithFullPathsAndReturnHTMLAsString(String tmpURL, Long tmpCurMilliseconds)
    {
        String allHTMLStr = "";
        try
        {
            //if(tmpURL.charAt(tmpURL.length()-1) != '/')
            //    tmpURL = tmpURL + "/";

            URL url = new URL(tmpURL);
            String tmpURLEncoding = getEncodingOfURL(tmpURL);
            //System.out.println("\n\nENCODING for " + tmpURL + ": " + tmpURLEncoding);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName(tmpURLEncoding)));
            //original page (for debugging)
            String tempoFPath = "";
            if(SST_Manager.getInstance().USE_TIMESTAMP_IN_OUTPUT_FILENAMES)
                tempoFPath = SST_Manager.getInstance().HTDOCS_DIR + "/originalPage_" + Long.toString(tmpCurMilliseconds) + ".html";
            else
                tempoFPath = SST_Manager.getInstance().HTDOCS_DIR + "/originalPage.html";
            BufferedWriter writer_original = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(tempoFPath), Charset.forName(tmpURLEncoding))); //new BufferedWriter(new FileWriter("D:/Apache2.2/htdocs/data_original.html"));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                allHTMLStr = allHTMLStr + line;
                allHTMLStr = allHTMLStr + "\n";
            }
            
            //find base URL
            /*String baseURL = new String(tmpURL);
            int indexOfFirstSlashInURL = baseURL.substring(7).indexOf("/");
            if(indexOfFirstSlashInURL != -1)
            {
                baseURL = baseURL.substring(0, indexOfFirstSlashInURL+7);
            }*/

            //find baseURL
            String baseURL = new String(tmpURL);
            int indexOfThirdSlashInURL = nthOccurrence(baseURL, '/', 3);
            if(indexOfThirdSlashInURL != -1)
                baseURL = baseURL.substring(0, indexOfThirdSlashInURL);

            /********/
            /* HREF */
            /********/
            /*
            //replace all href="/blahblah with href="baseURL/blahblah
            while(allHTMLStr.toLowerCase().indexOf("href=\"/") != -1)
            {
                int hrefBeginIndex = allHTMLStr.toLowerCase().indexOf("href=\"/");
                allHTMLStr = allHTMLStr.substring(0, hrefBeginIndex) + "href=\"" + currentHTMLPath + "/" + allHTMLStr.substring(hrefBeginIndex+7);
            }
            //replace all href='/blahblah with href='baseURL/blahblah
            while(allHTMLStr.toLowerCase().indexOf("href='/") != -1)
            {
                int hrefBeginIndex = allHTMLStr.toLowerCase().indexOf("href='/");
                allHTMLStr = allHTMLStr.substring(0, hrefBeginIndex) + "href='" + currentHTMLPath + "/" + allHTMLStr.substring(hrefBeginIndex+7);
            }
            //replace all href=". with href="baseURL/.
            while(allHTMLStr.toLowerCase().indexOf("href=\".") != -1)
            {
                int hrefBeginIndex = allHTMLStr.toLowerCase().indexOf("href=\".");
                allHTMLStr = allHTMLStr.substring(0, hrefBeginIndex) + "href=\"" + currentHTMLPath + "/." + allHTMLStr.substring(hrefBeginIndex+7);
            }
            //replace all href='. with href='baseURL/.
            while(allHTMLStr.toLowerCase().indexOf("href='.") != -1)
            {
                int hrefBeginIndex = allHTMLStr.toLowerCase().indexOf("href='.");
                allHTMLStr = allHTMLStr.substring(0, hrefBeginIndex) + "href='" + currentHTMLPath + "/." + allHTMLStr.substring(hrefBeginIndex+7);
            }*/
            
            /*******/
            /* SRC */
            /*******/
            /*
            //replace all src="/blahblah with src="baseURL/blahblah
            while(allHTMLStr.toLowerCase().indexOf("src=\"/") != -1)
            {
                int srcBeginIndex = allHTMLStr.toLowerCase().indexOf("src=\"/");
                allHTMLStr = allHTMLStr.substring(0, srcBeginIndex) + "src=\"" + currentHTMLPath + "/" + allHTMLStr.substring(srcBeginIndex+6);
            }
            //replace all src='/blahblah with src='baseURL/blahblah
            while(allHTMLStr.toLowerCase().indexOf("src='/") != -1)
            {
                int srcBeginIndex = allHTMLStr.toLowerCase().indexOf("src='/");
                allHTMLStr = allHTMLStr.substring(0, srcBeginIndex) + "src='" + currentHTMLPath + "/" + allHTMLStr.substring(srcBeginIndex+6);
            }
            //replace all src=". with src="baseURL/.
            while(allHTMLStr.toLowerCase().indexOf("src=\".") != -1)
            {
                int srcBeginIndex = allHTMLStr.toLowerCase().indexOf("src=\".");
                allHTMLStr = allHTMLStr.substring(0, srcBeginIndex) + "src=\"" + currentHTMLPath + "/." + allHTMLStr.substring(srcBeginIndex+6);
            }
            //replace all src='. with src='baseURL/.
            while(allHTMLStr.toLowerCase().indexOf("src=\'.") != -1)
            {
                int srcBeginIndex = allHTMLStr.toLowerCase().indexOf("src=\'.");
                allHTMLStr = allHTMLStr.substring(0, srcBeginIndex) + "src=\'" + currentHTMLPath + "/." + allHTMLStr.substring(srcBeginIndex+6);
            }*/
            
            /********/
            /* DATA */
            /********/
            /*
            //replace all data="/blahblah with data="baseURL/blahblah
            while(allHTMLStr.toLowerCase().indexOf("data=\"/") != -1)
            {
                int dataBeginIndex = allHTMLStr.toLowerCase().indexOf("data=\"/");
                allHTMLStr = allHTMLStr.substring(0, dataBeginIndex) + "data=\"" + currentHTMLPath + "/" + allHTMLStr.substring(dataBeginIndex+7);
            }
            //replace all data='/blahblah with data='baseURL/blahblah
            while(allHTMLStr.toLowerCase().indexOf("data='/") != -1)
            {
                int dataBeginIndex = allHTMLStr.toLowerCase().indexOf("data='/");
                allHTMLStr = allHTMLStr.substring(0, dataBeginIndex) + "data='" + currentHTMLPath + "/" + allHTMLStr.substring(dataBeginIndex+7);
            }
            //replace all data=". with data="baseURL/.
            while(allHTMLStr.toLowerCase().indexOf("data=\".") != -1)
            {
                int dataBeginIndex = allHTMLStr.toLowerCase().indexOf("data=\".");
                allHTMLStr = allHTMLStr.substring(0, dataBeginIndex) + "data=\"" + currentHTMLPath + "/." + allHTMLStr.substring(dataBeginIndex+7);
            }
            //replace all data='. with data='baseURL/.
            while(allHTMLStr.toLowerCase().indexOf("data='.") != -1)
            {
                int dataBeginIndex = allHTMLStr.toLowerCase().indexOf("data='.");
                allHTMLStr = allHTMLStr.substring(0, dataBeginIndex) + "data='" + currentHTMLPath + "/." + allHTMLStr.substring(dataBeginIndex+7);
            }*/
            
            SST_Manager.getInstance().URLValidationAlreadyPerformed = 0;
            Document doc = Jsoup.parse(allHTMLStr);
        
            Elements allElements = doc.getAllElements(); 
            for(Element el : allElements)
            {
                //replace all href="blah with href="baseURL/blah
                if(el.hasAttr("href"))
                {
                    String tmpHref = el.attr("href");
                    if(tmpHref.startsWith("mailto:")==false)
                    {
                        if(tmpHref.length()>4 && tmpHref.startsWith("http")==false)
                        {
                            String tempStr1 = combinedStringsWithSlashBetweenThem(baseURL, tmpHref);
                            String tempStr2 = combinedStringsWithSlashBetweenThem(tmpURL, tmpHref);
                            if(SST_Manager.getInstance().URLValidationAlreadyPerformed<SST_Manager.getInstance().URLValidationLimitPerPage && urlIsValid(tempStr1))
                                el.attr("href", tempStr1);
                            else if(SST_Manager.getInstance().URLValidationAlreadyPerformed<SST_Manager.getInstance().URLValidationLimitPerPage && tmpURL.equals(baseURL)==false && urlIsValid(tempStr2))
                                el.attr("href", tempStr2);
                            else
                            {
                                //make a  guess...
                                el.attr("href", tempStr2);
                                //System.out.println("\n\n\n*Cannot properly transform URL for href -> " + tmpHref + "   -> guess: " + tempStr2 + "\n\n\n");
                            }
                        }
                    }
                }
                //replace all src="blah with src="baseURL/blah        
                if(el.hasAttr("src"))
                {
                    String tmpSrc = el.attr("src");
                    if(tmpSrc.length()>4 && tmpSrc.startsWith("http")==false)
                    {
                        String tempStr1 = combinedStringsWithSlashBetweenThem(baseURL, tmpSrc);
                        String tempStr2 = combinedStringsWithSlashBetweenThem(tmpURL, tmpSrc);
                        if(SST_Manager.getInstance().URLValidationAlreadyPerformed<SST_Manager.getInstance().URLValidationLimitPerPage && urlIsValid(tempStr1))
                            el.attr("src", tempStr1);
                        else if(SST_Manager.getInstance().URLValidationAlreadyPerformed<SST_Manager.getInstance().URLValidationLimitPerPage && tmpURL.equals(baseURL)==false && urlIsValid(tempStr2))
                            el.attr("src", tempStr2);
                        else
                        {
                            //make a  guess...
                            el.attr("src", tempStr2);
                            //System.out.println("\n\n\n*Cannot properly transform URL for src -> " + tmpSrc + "   -> guess: " + tempStr2 + "\n\n\n");
                        }
                    }
                }
                //replace all data="blah with data="baseURL/blah
                if(el.hasAttr("data"))
                {
                    String tmpData = el.attr("data");
                    if(tmpData.length()>4 && tmpData.startsWith("http")==false)
                    {        
                        String tempStr1 = combinedStringsWithSlashBetweenThem(baseURL, tmpData);
                        String tempStr2 = combinedStringsWithSlashBetweenThem(tmpURL, tmpData);
                        if(SST_Manager.getInstance().URLValidationAlreadyPerformed<SST_Manager.getInstance().URLValidationLimitPerPage && urlIsValid(tempStr1))
                            el.attr("data", tempStr1);
                        else if(SST_Manager.getInstance().URLValidationAlreadyPerformed<SST_Manager.getInstance().URLValidationLimitPerPage && tmpURL.equals(baseURL)==false && urlIsValid(tempStr2))
                            el.attr("data", tempStr2);
                        else
                        {
                            //make a  guess...
                            el.attr("data", tempStr2);
                            //System.out.println("\n\n\n*Cannot properly transform URL for data -> " + tmpData + "   -> guess: " + tempStr2 + "\n\n\n");
                        }
                    }
                }
            }        

            allHTMLStr = doc.outerHtml();
            
            writer_original.write(allHTMLStr);
            writer_original.close();
            reader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return allHTMLStr;
    }
    
    public String translate(String tmpURL, String tmpLang) throws Exception  
    {
        Long curMilliseconds = System.currentTimeMillis();
        deleteOldTemporaryFiles();

        String res = "";
        
        //translated page
        String tmpURLEncoding = getEncodingOfURL(tmpURL);
        String tmpFPath = "";
        if(SST_Manager.getInstance().USE_TIMESTAMP_IN_OUTPUT_FILENAMES)
        {
            res = "translatedPage_" + Long.toString(curMilliseconds) + ".html";
            tmpFPath = SST_Manager.getInstance().HTDOCS_DIR + "/translatedPage_" + Long.toString(curMilliseconds) + ".html";
        }
        else
        {    
            res = "translatedPage.html";
            tmpFPath = SST_Manager.getInstance().HTDOCS_DIR + "/translatedPage.html";
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(tmpFPath), Charset.forName(tmpURLEncoding))); //new BufferedWriter(new FileWriter("D:/Apache2.2/htdocs/data.html"));

        String allHTMLStr = replaceRelativePathsWithFullPathsAndReturnHTMLAsString(tmpURL, curMilliseconds);
        
        String langCode = "unknown lang code";
        if(tmpLang.toLowerCase().equals(TranslationManager.GPII_CATALAN.toLowerCase()))
            langCode = TranslationManager.BING_CATALAN;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_CHINESE_SIMPLIFIED.toLowerCase()))
            langCode = TranslationManager.BING_CHINESE_SIMPLIFIED;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_CHINESE_TRADITIONAL.toLowerCase()))
            langCode = TranslationManager.BING_CHINESE_TRADITIONAL;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_CZECH.toLowerCase()))
            langCode = TranslationManager.BING_CZECH;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_ENGLISH.toLowerCase()))
            langCode = TranslationManager.BING_ENGLISH;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_FINNISH.toLowerCase()))
            langCode = TranslationManager.BING_FINNISH;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_FRENCH.toLowerCase()))
            langCode = TranslationManager.BING_FRENCH;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_GERMAN.toLowerCase()))
            langCode = TranslationManager.BING_GERMAN;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_GREEK.toLowerCase()))
            langCode = TranslationManager.BING_GREEK;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_HUNGARIAN.toLowerCase()))
            langCode = TranslationManager.BING_HUNGARIAN;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_ITALIAN.toLowerCase()))
            langCode = TranslationManager.BING_ITALIAN;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_LATVIAN.toLowerCase()))
            langCode = TranslationManager.BING_LATVIAN;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_PORTUGUESE.toLowerCase()))
            langCode = TranslationManager.BING_PORTUGUESE;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_ROMANIAN.toLowerCase()))
            langCode = TranslationManager.BING_ROMANIAN;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_SLOVAK.toLowerCase()))
            langCode = TranslationManager.BING_SLOVAK;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_SPANISH.toLowerCase()))
            langCode = TranslationManager.BING_SPANISH;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_SWEDISH.toLowerCase()))
            langCode = TranslationManager.BING_SWEDISH;
        else if(tmpLang.toLowerCase().equals(TranslationManager.GPII_TURKISH.toLowerCase()))
            langCode = TranslationManager.BING_TURKISH;
        
        String translateDIV = "<script src=\"http://www.microsoftTranslator.com/ajax/v3/WidgetV3.ashx?siteData=ueOIGRSKkd965FeEGM5JtQ**\" type=\"text/javascript\"></script>" + "\n"
                                 + "<script type=\"text/javascript\">" + "\n"
                                 + "document.onreadystatechange = function () {" + "\n"
                                 + "       if (document.readyState == 'complete') {" + "\n"
                                 + "           Microsoft.Translator.Widget.Translate(null, '" + langCode + "', onProgress, onError, onComplete, onRestoreOriginal, null);" + "\n"
                                 + "       }" + "\n"
                                 + "   }" + "\n"
                                 + "   //You can use Microsoft.Translator.Widget.GetLanguagesForTranslate to map the language code with the language name" + "\n"
                                 + "   function onProgress(value) {" + "\n"
                                 + "       //document.getElementById('counter').innerHTML = Math.round(value);" + "\n"
                                 + "   }" + "\n"
                                 + "   function onError(error) {" + "\n"
                                 + "       alert(\"Translation Error: \" + error);" + "\n"
                                 + "   }" + "\n"
                                 + "   function onComplete() {" + "\n"
                                 + "       //document.getElementById('counter').style.color = 'green';" + "\n"
                                 + "   }" + "\n"
                                 + "   //fires when the user clicks on the exit box of the floating widget" + "\n"
                                 + "   function onRestoreOriginal() { " + "\n"
                                 + "       alert(\"The page was reverted to the original language. This message is not part of the widget.\");" + "\n"
                                 + "   }" + "\n"
                                + "</script>";
        
        int bodyIndex = allHTMLStr.toLowerCase().indexOf("</head>");
        if(bodyIndex != -1)
        {
            allHTMLStr = allHTMLStr.substring(0, bodyIndex) + "\n\n" + translateDIV + "\n\n" + allHTMLStr.substring(bodyIndex);
        }
        
        writer.write(allHTMLStr);
        writer.close();
        
        return res;
    }
    
    public String changeFont(String tmpURL, String tmpFontFamily, String tmpFontSize, String tmpColor, String tmpBackground) throws Exception 
    {
        Long curMilliseconds = System.currentTimeMillis();
        deleteOldTemporaryFiles();

        String res = "";
        
        //font converted page
        String tmpURLEncoding = getEncodingOfURL(tmpURL);
        String tmpFPath = "";
        if(SST_Manager.getInstance().USE_TIMESTAMP_IN_OUTPUT_FILENAMES)
        {
            res = "fontConvertedPage_" + Long.toString(curMilliseconds) + ".html";
            tmpFPath = SST_Manager.getInstance().HTDOCS_DIR + "/fontConvertedPage_" + Long.toString(curMilliseconds) + ".html";
        }
        else
        {
            res = "fontConvertedPage.html";
            tmpFPath = SST_Manager.getInstance().HTDOCS_DIR + "/fontConvertedPage.html";
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(tmpFPath), Charset.forName(tmpURLEncoding)));
        
        String allHTMLStr = replaceRelativePathsWithFullPathsAndReturnHTMLAsString(tmpURL, curMilliseconds);
        
        String fontFamilyStmt = "";
        if(tmpFontFamily.equals("NOT DEFINED") == false)
            fontFamilyStmt = "         font-family: " + tmpFontFamily + " !important;";
        String fontSizeStmt = "";
        if(tmpFontSize.equals("NOT DEFINED") == false)
            fontSizeStmt = "         font-size: " + tmpFontSize + "px !important;";
        String colorStmt = "";
        if(tmpColor.equals("NOT DEFINED") == false)
            colorStmt = "         color:" + tmpColor + " !important;";
        String backgroundStmt = "";
        if(tmpBackground.equals("NOT DEFINED") == false)
            backgroundStmt = "         background:" + tmpBackground + " !important;";
        
        String convertedFontDIV = "<style>" + "\n"
                                    + "    * {" + "\n"
                                    + fontFamilyStmt + "\n"
                                    + fontSizeStmt + "\n"
                                    + colorStmt + "\n"
                                    + backgroundStmt + "\n"
                                    + "    }" + "\n"
                                    + "</style>";
        
        int bodyIndex = allHTMLStr.toLowerCase().indexOf("</head>");
        if(bodyIndex != -1)
        {
            allHTMLStr = allHTMLStr.substring(0, bodyIndex) + "\n\n" + convertedFontDIV + "\n\n" + allHTMLStr.substring(bodyIndex);
        }
        
        writer.write(allHTMLStr);
        writer.close();
        
        return res;
    }
    
    public String identifyImagesAndAddAltTextWhereMissing(String tmpURL) throws Exception
    {
        Long curMilliseconds = System.currentTimeMillis();
        deleteOldTemporaryFiles();
        
        String resURL = "";

        //page with identified images
        String tmpURLEncoding = getEncodingOfURL(tmpURL);
        String tmpFPath = "";
        if(SST_Manager.getInstance().USE_TIMESTAMP_IN_OUTPUT_FILENAMES)
        {
            resURL = "pageWithIdentifiedImages_" + Long.toString(curMilliseconds) + ".html";
            tmpFPath = SST_Manager.getInstance().HTDOCS_DIR + "/pageWithIdentifiedImages_" + Long.toString(curMilliseconds) + ".html";
        }
        else
        {
            resURL = "pageWithIdentifiedImages.html";
            tmpFPath = SST_Manager.getInstance().HTDOCS_DIR + "/pageWithIdentifiedImages.html";
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(tmpFPath), Charset.forName(tmpURLEncoding)));
        
        String allHTMLStr = replaceRelativePathsWithFullPathsAndReturnHTMLAsString(tmpURL, curMilliseconds);
        
        Document doc = Jsoup.parse(allHTMLStr);
        
        //find all images without alternative text and try to identify their content
        Elements img = doc.getElementsByTag("img"); 
        for(Element el : img)
        {
            if(el.hasAttr("alt") == false
                    || (el.hasAttr("alt") == true && el.attr("alt").length()==0))
            {
                if(el.hasAttr("src") && el.attr("src").length()>0)
                {
                    try
                    {
                        //System.out.println("\nTrying to identify: " + el.attr("src"));
                        //http://face.eyedea.cz:8080/api/face/demo
                        //https://www.mashape.com/eyedea/eyeface#!documentation
                        /*HttpResponse<JsonNode> eyefacerequest = Unirest.post("https://eyedea-eyeface.p.mashape.com/v2/facedetect.json")
                            .header("X-Mashape-Authorization", SST_Manager.getInstance().X_MASHAPE_AUTHORIZATION_KEY)
                            .field("upload", new File("D:/samplePic.jpg"))
                            .field("url", el.attr("src"))
                            .asJson();*/
                        
                        /*HttpResponse<JsonNode> eyefacerequest = Unirest.post("https://eyedea-eyeface.p.mashape.com/v2/facedetect.json")
                        .header("X-Mashape-Key", "jSu9mZLa6smshJD24vezTPizSpAcp1KlaYhjsnuvTJZcEmlXw4")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "application/json")
                        .field("url", el.attr("src"))
                        .asJson();
                        System.out.println("\n\neyeface result:\n" + eyefacerequest.getBody().toString());*/
                        
                        String tmpReqStr = "http://face.eyedea.cz:8080/api/v2/facedetect.json?email=" 
/* REPLACE HARD-CODED WITH CONFIG! */                                + "nkak@iti.gr" //SST_Manager.getInstance().EYEFACE_EMAIL 
                                + "&password=" + "04317d6d7" //SST_Manager.getInstance().EYEFACE_PASSWORD 
                                + "&url=" + el.attr("src");
                        System.out.println("tmpReqStr: " + tmpReqStr);
                        HttpResponse<JsonNode> eyefacerequest = Unirest.get(tmpReqStr).asJson();
                        System.out.println("\n\neyeface result:\n" + eyefacerequest.getBody().toString());
                        EyefaceOutput tmpEyefaceOutput = TranslationManager.getInstance().gson.fromJson(eyefacerequest.getBody().toString(), EyefaceOutput.class);
                        //tmpDebugMsg.secondDebugMsg = JsonWriter.formatJson(eyefacerequest.getBody().toString());
                        el.attr("alt", getIdentifiedAltAttr(tmpEyefaceOutput));
                    }
                    catch(Exception e)
                    {
                        System.out.println("\n * Eyeface EXCEPTION: " + e.getMessage() + "\n");
                    }
                }
            } 
        }
        allHTMLStr = doc.outerHtml();

        writer.write(allHTMLStr);
        writer.close();        
        
        return resURL;
    }
    
    public String getIdentifiedAltAttr(EyefaceOutput tmpEyefaceOutput)
    {
        String res = "This image didn't have an alternative text. The \"cloud for all service synthesizer tool\" ";
        
        //String tmpOutputStr = TranslationManager.getInstance().gson.toJson(tmpEyefaceOutput);
        //System.out.println("tmpOutputStr: " + tmpOutputStr);
                
        if(tmpEyefaceOutput != null
                && tmpEyefaceOutput.status != null
                && tmpEyefaceOutput.status.toLowerCase().equals("success"))
        {
            if(tmpEyefaceOutput.photos != null
                    && tmpEyefaceOutput.photos.size() > 0
                    && tmpEyefaceOutput.photos.get(0) != null
                    && tmpEyefaceOutput.photos.get(0).tags != null
                    && tmpEyefaceOutput.photos.get(0).tags.size() > 0
                    && tmpEyefaceOutput.photos.get(0).tags.get(0) != null
                    && tmpEyefaceOutput.photos.get(0).tags.get(0).attributes != null
                    && tmpEyefaceOutput.photos.get(0).tags.get(0).attributes.gender != null
                    && tmpEyefaceOutput.photos.get(0).tags.get(0).attributes.gender.value != null
                    && tmpEyefaceOutput.photos.get(0).tags.get(0).attributes.age_est != null)
            {
                if(tmpEyefaceOutput.photos.get(0).tags.size() == 1)
                {
                    if(tmpEyefaceOutput.photos.get(0).tags.get(0).attributes.gender.value.equals("unknown") == false)
                        res = res + "identified that the picture shows a " + tmpEyefaceOutput.photos.get(0).tags.get(0).attributes.gender.value + " around " + Integer.toString(tmpEyefaceOutput.photos.get(0).tags.get(0).attributes.age_est.value) + " years old.";
                    else 
                        res = res + "identified that the picture shows a person around " + Integer.toString(tmpEyefaceOutput.photos.get(0).tags.get(0).attributes.age_est.value) + " years old whose gender could not be identified.";
                }
                else if(tmpEyefaceOutput.photos.get(0).tags.size() > 1)
                {
                    res = res + "identified that the picture shows " + Integer.toString(tmpEyefaceOutput.photos.get(0).tags.size()) + " people:";
                    for(int i=0; i<tmpEyefaceOutput.photos.get(0).tags.size(); i++)
                    {
                        if(i == 0)
                        {
                            if(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender.value.equals("unknown") == false)
                                res = res + " a " + tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender.value + " around " + Integer.toString(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.age_est.value) + " years old";
                            else
                                res = res + " a person around " + Integer.toString(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.age_est.value) + " years old whose gender could not be identified";
                        }
                        else if(i < tmpEyefaceOutput.photos.get(0).tags.size()-1)
                        {
                            if(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes != null)
                            {
                                if(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender != null
                                        && tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender.value != null
                                        && tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.age_est != null
                                        && tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender.value.equals("unknown") == false)
                                    res = res + ", a " + tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender.value + " around " + Integer.toString(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.age_est.value) + " years old";
                                else
                                    res = res + ", a person around " + Integer.toString(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.age_est.value) + " years old whose gender could not be identified";
                            }
                            else
                                res = res + ", a person whose age and gender could not be identified";
                        }
                        else if(i == tmpEyefaceOutput.photos.get(0).tags.size()-1)
                        {
                            if(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes != null)
                            {
                                if(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender != null
                                        && tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender.value != null
                                        && tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.age_est != null
                                        && tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender.value.equals("unknown") == false)
                                    res = res + " and a " + tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.gender.value + " around " + Integer.toString(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.age_est.value) + " years old.";
                                else
                                    res = res + " and a person around " + Integer.toString(tmpEyefaceOutput.photos.get(0).tags.get(i).attributes.age_est.value) + " years old whose gender could not be identified.";
                            }
                            else
                                res = res + " and a person whose age and gender could not be identified.";
                        }
                    }
                }
            }
            else
                res = res + "was not able to identify the content of the image.";
        }
        else
            res = res + "was not able to identify the content of the image.";
        return res;
    }
   
//    @POST
//    @Path("/translate")
//    @Consumes("application/json")
//    public Response translateWebPage(MyWSInput tmpInput)
//    {
//        String URLToReturn = "Connection error!";
//        String translatedWebPageURL = "";
//        try {
//            translatedWebPageURL = translate(tmpInput.URL, tmpInput.lang);
//            URLToReturn = "http://webanywhere.cs.washington.edu/beta/?starting_url=" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/" + translatedWebPageURL + "&locale=";
//        } catch (Exception ex) {
//            Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
//        }        
//        
//        if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_CATALAN.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_CATALAN;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_CHINESE_SIMPLIFIED.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_CHINESE_SIMPLIFIED;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_CHINESE_TRADITIONAL.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_CHINESE_TRADITIONAL;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_CZECH.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_CZECH;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_ENGLISH.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_ENGLISH;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_FINNISH.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_FINNISH;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_FRENCH.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_FRENCH;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_GERMAN.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_GERMAN;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_GREEK.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_GREEK;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_HUNGARIAN.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_HUNGARIAN;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_ITALIAN.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_ITALIAN;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_LATVIAN.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_LATVIAN;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_PORTUGUESE.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_PORTUGUESE_EUROPEAN;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_ROMANIAN.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_ROMANIAN;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_SLOVAK.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_SLOVAK;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_SPANISH.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_SPANISH;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_SWEDISH.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_SWEDISH;
//        else if(tmpInput.lang.toLowerCase().equals(TranslationManager.GPII_TURKISH.toLowerCase()))
//            URLToReturn = URLToReturn + TranslationManager.WEBANYWHERE_TURKISH;
//        else
//            URLToReturn = "Language code not supported! -> " + tmpInput.lang;
//        
//        MyWSOutput tmpOutput = new MyWSOutput();
//        tmpOutput.URL = URLToReturn;
//        tmpOutput.debugMessages = new ArrayList<debugMsg>();
//        
//        
//        
//        
//       /*HttpResponse<JsonNode> request_imgRecognition = null; 
//        try {
//            request_imgRecognition = Unirest.post("https://viscovery-image-recognition-web-service.p.mashape.com/image_requests")
//      .header("X-Mashape-Authorization", X_MASHAPE_AUTHORIZATION)
//      .field("device_name", "android")
//      .field("device_version", "4.1.1")
//      .field("uuid", "d8c47286963344c4a3b516057bd5125cc555dfe0")
//      .field("zone", "Europe")
//      .field("username", "demo")
//      .field("bundleid", "net.funwish.demo")
//      .field("userfile", new File("D:/samplePic.jpg"))
//      .field("latitude", "35.8714220766008")
//      .field("longitude", "14.3583203002251")
//      .asJson();
//            tmpOutput.debugMsg = "\n\n\nImage Recognition - Viscovery API:\n" + JsonWriter.formatJson(request_imgRecognition.getBody().toString());
//        } catch (Exception ex) {
//            tmpOutput.debugMsg = "Exception: " + ex.getMessage();
//        }
//        */
//        
//        
//       /*HttpResponse<JsonNode> request_adultContentDetection = null;
//        try {
//            //request_adultContentDetection = Unirest.get("https://sphirelabs-advanced-porn-nudity-and-adult-content-detection.p.mashape.com/pornhuh/index.php?url=http%3A%2F%2Fi.imgur.com%2F4hGni1I.jpg")
//            request_adultContentDetection = Unirest.get("https://sphirelabs-advanced-porn-nudity-and-adult-content-detection.p.mashape.com/pornhuh/index.php?url=http://www.accessible-eu.org/images/logos/accessible.gif")
//      .header("X-Mashape-Authorization", X_MASHAPE_AUTHORIZATION)
//      .asJson();
//            tmpOutputStr = tmpOutputStr + "\n\n\nAdult Content Detection - Sphirelabs:\n" + JsonWriter.formatJson(request_adultContentDetection.getBody().toString());
//        } catch (Exception ex) {
//            Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
//        }*/
//        
//        //HTMLUNIT
//        /*String tmpDebugStr = "";
//        try
//        {
//            Gson gson = new Gson();
//            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3);
//            webClient.setJavaScriptEnabled(false);
//            com.gargoylesoftware.htmlunit.html.HtmlPage page = (com.gargoylesoftware.htmlunit.html.HtmlPage) webClient.getPage("http://" + ApacheServerIP + "/" + dirInHtdocs + "/" + translatedWebPageURL);
//            List<HtmlImage> imageList = (List<HtmlImage>) page.getByXPath("//img");
//            if(imageList!=null && imageList.size()>0)
//            {
//                for (HtmlImage image : imageList) 
//                {
//                    if (image!=null 
//                            && image.getSrcAttribute()!=null 
//                            && image.getSrcAttribute().equals("")==false 
//                            && (image.getAltAttribute()==null || image.getAltAttribute().equals(""))) 
//                    {
//                        debugMsg tmpDebugMsg = new debugMsg();
//                        tmpDebugMsg.info = "HTMLUNIT - Image without alt";
//                        tmpDebugMsg.firstDebugMsg = image.getSrcAttribute();
//                        tmpDebugStr = tmpDebugMsg.firstDebugMsg;
//
//                        try
//                        {
//                            //http://face.eyedea.cz:8080/api/face/demo
//                            //https://www.mashape.com/eyedea/eyeface#!documentation
//                            HttpResponse<JsonNode> eyefacerequest = Unirest.post("https://eyedea-eyeface.p.mashape.com/v2/facedetect.json")
//                                .header("X-Mashape-Authorization", X_MASHAPE_AUTHORIZATION)
//                                //.field("url", "http://upload.wikimedia.org/wikipedia/commons/3/35/Ed_Norton_Shankbone_Metropolitan_Opera_2009.jpg")
//                                .field("url", tmpDebugMsg.firstDebugMsg)
//                                //.field("upload", new File("<file goes here>"))
//                                .asJson();
//                            EyefaceOutput tmpEyefaceOutput = gson.fromJson(eyefacerequest.getBody().toString(), EyefaceOutput.class);
//                            //tmpDebugMsg.secondDebugMsg = JsonWriter.formatJson(eyefacerequest.getBody().toString());
//                            tmpDebugMsg.secondDebugMsg = getIdentifiedAltAttr(tmpEyefaceOutput);
//                            tmpOutput.debugMessages.add(tmpDebugMsg);
//                        }
//                        catch(Exception e)
//                        {
//                            tmpDebugMsg.info = "eyedea - EXCEPTION";
//                            tmpDebugMsg.firstDebugMsg = tmpDebugStr + " -> " + e.getMessage();
//                            tmpOutput.debugMessages.add(tmpDebugMsg);
//                            
//                            continue;
//                        }
//                    } 
//                }
//            }
//            webClient.closeAllWindows();
//        }
//        catch(Exception e)
//        {
//            debugMsg tmpDebugMsg = new debugMsg();
//            tmpDebugMsg.info = "HTMLUNIT - EXCEPTION";
//            tmpDebugMsg.firstDebugMsg = tmpDebugStr + " -> " + e.getMessage();
//            tmpOutput.debugMessages.add(tmpDebugMsg);
//        }*/
//        
//        
//        //CAMFIND
//        //.......
//         
//        
//      
//        String tmpOutputStr = "";
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
//        } catch (IOException ex) {
//            Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
//        }   
//        return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
//    }
    
    
    private void copyInputStreamToFile(InputStream in, File file) 
    {
        try 
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String inStr = "";
            String line;
            while ((line = reader.readLine()) != null) 
                inStr = inStr + line + "\n";
            
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.write(inStr);
        
            reader.close();
            writer.flush();
            writer.close();
        } 
	catch (UnsupportedEncodingException e) 
	{
            System.out.println(e.getMessage());
	} 
	catch (IOException e) 
	{
            System.out.println(e.getMessage());
	}
        catch (Exception e)
	{
            System.out.println(e.getMessage());
	} 
        
        
        /*try 
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0)
            {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }*/        
    }
    
    public String jsonElementNamesToLowerCaseWithUnderscores(String tmpInput)
    {
        String res = "";
        int doubleDotsIndex = -1;
        int openingQuotesIndex = -1;
        for(int i=0; i<tmpInput.length(); i++)
        {
            char tmpChar = tmpInput.charAt(i);
            
            if(doubleDotsIndex==-1 && tmpChar!=':')
            {
                if(openingQuotesIndex==-1 && tmpChar=='"')
                {
                    openingQuotesIndex = i;
                    res = res + tmpChar;
                }
                else if(openingQuotesIndex!=-1 && tmpChar!='"')
                {
                    if(Character.isUpperCase(tmpChar))
                    {
                        res = res + '_';
                        res = res + Character.toLowerCase(tmpChar);
                    }
                    else
                        res = res + tmpChar;
                }
                else if(openingQuotesIndex!=-1 && tmpChar=='"')
                {
                    openingQuotesIndex = -1;
                    res = res + tmpChar;
                }
                else
                    res = res + tmpChar;
            }
            else if(doubleDotsIndex==-1 && tmpChar==':')
            {
                doubleDotsIndex = i;
                res = res + tmpChar;
            }
            else if(doubleDotsIndex != -1)
            {
                if(openingQuotesIndex==-1 && tmpChar=='"')
                {
                    openingQuotesIndex = i;
                    res = res + tmpChar;
                }
                else if(openingQuotesIndex!=-1 && tmpChar!='"')
                {
                    //leave the value of a json element as it is
                    res = res + tmpChar;
                }
                else if(openingQuotesIndex!=-1 && tmpChar=='"')
                {
                    openingQuotesIndex = -1;
                    doubleDotsIndex = -1;
                    res = res + tmpChar;
                }
                else
                    res = res + tmpChar;
            }
            else
                res = res + tmpChar;
        }
        return res;
    }
    
    // convert from internal Java String format -> UTF-8
    public String convertToUTF8(String s) 
    {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
    
    public void writeStringToFile(String tmpFilepath, String tmpNewContents) 
    {
        try {
            File newTextFile = new File(tmpFilepath);

            FileWriter fw = new FileWriter(newTextFile);
            fw.write(tmpNewContents);
            fw.close();

        } catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        }
    }
    

	@POST
	@Path("/call_IPtoLatLng")
	@Consumes("application/json")
	public Response call_IPtoLatLng(com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput tmpInput)
	{
		com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput tmpOutput = null;

		try
		{
			HttpResponse<JsonNode> request = Unirest.get("https://mgwhitfield-ip-to-lat-lng.p.mashape.com/index.php?ip=" + tmpInput.getIp() + "&type=json")
				.header("X-Mashape-Authorization", SST_Manager.getInstance().X_MASHAPE_AUTHORIZATION_KEY)
				.asJson();

			tmpOutput = TranslationManager.getInstance().gson.fromJson(request.getBody().toString(), com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput.class);

		}
		catch(Exception e)
		{
			System.out.println("\n * IPtoLatLng EXCEPTION: " + e.getMessage() + "\n");
		}
                
                //additional properties - input examples
                tmpOutput.setAdditionalProperty("urlToTranslate_example", new String("http://www.accessible-project.eu"));
                tmpOutput.setAdditionalProperty("targetLanguage_example", new String("el, es, fr, etc."));

		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}
        
        /*@POST
        @Path("/test_IPtoLatLng")
        public Response test_IPtoLatLng()
        {
             return Response.status(200).entity("This is a successful integration test!").build();
        }*/
        

	@POST
	@Path("/call_Weather")
	@Consumes("application/json")
	public Response call_Weather(com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput tmpInput)
	{
		ArrayList<com.certh.iti.cloud4all.restful.WeatherOutput.Day> tmpOutput = new ArrayList<com.certh.iti.cloud4all.restful.WeatherOutput.Day>();

		try
		{
			HttpResponse<JsonNode> request = Unirest.get("https://george-vustrey-weather.p.mashape.com/api.php?location=" + tmpInput.getLocation())
				.header("X-Mashape-Authorization", SST_Manager.getInstance().X_MASHAPE_AUTHORIZATION_KEY)
				.asJson();
                        
                        Type listType = new TypeToken<List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>>() {}.getType();
                        tmpOutput = TranslationManager.getInstance().gson.fromJson(request.getBody().toString(), listType);
		}
		catch(Exception e)
		{
			System.out.println("\n * Weather EXCEPTION: " + e.getMessage() + "\n");
		}

		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
                //transform camelCase to lowercase with underscore
                tmpOutputStr = jsonElementNamesToLowerCaseWithUnderscores(tmpOutputStr);
                
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/call_TelizeGeolocation")
	@Consumes("application/json")
	public Response call_TelizeGeolocation(com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput tmpInput)
	{
		com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput tmpOutput = null;

		try
		{
			HttpResponse<String> request = Unirest.get("https://community-telize-json-ip-and-geoip.p.mashape.com/geoip/" + tmpInput.getIpAddress() + "?callback=getgeoip")
				.header("X-Mashape-Authorization", SST_Manager.getInstance().X_MASHAPE_AUTHORIZATION_KEY)
				.asString();
                        String wsAnswerStr = request.getBody().toString();
                        int firstBracketIndex = wsAnswerStr.indexOf("{");
                        int lastBracketIndex = wsAnswerStr.lastIndexOf("}");
                        wsAnswerStr = wsAnswerStr.substring(firstBracketIndex, lastBracketIndex+1);
			tmpOutput = TranslationManager.getInstance().gson.fromJson(wsAnswerStr, com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput.class);
		}
		catch(Exception e)
		{
			System.out.println("\n * TelizeGeolocation EXCEPTION: " + e.getMessage() + "\n");
		}
                
                //additional properties - input examples
                tmpOutput.setAdditionalProperty("ipAddress_example", "160.40.50.130");

		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
                //transform camelCase to lowercase with underscore
                tmpOutputStr = jsonElementNamesToLowerCaseWithUnderscores(tmpOutputStr);
                
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/call_StaticImageMap")
	@Consumes("application/json")
	public Response call_StaticImageMap(com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput tmpInput)
	{
		com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput tmpOutput = null;

                //default values
                String defaultProvider = "Google";
                String defaultHeight = "400";
                String defaultKey = "%3Cnumeric or alphanumericstring%3E";
                String defaultMaptype = "roadmap";
                String defaultWidth = "400";
                String defaultZoom = "13";
                
                String reqStr = "";
		try
		{
                    //https://www.mashape.com/orfeomorello/static-map#!documentation
                    reqStr = "https://orfeomorello-static-map.p.mashape.com/mashape/staticimagemap/lat/" + tmpInput.getLatitude() + "/lng/" + tmpInput.getLongitude() + "/provider/" + defaultProvider + "?height=" + defaultHeight + "&key=" + defaultKey + "&maptype=" + defaultMaptype + "&width=" + defaultWidth + "&zoom=" + defaultZoom;
			HttpResponse<JsonNode> request = Unirest.get(reqStr)
				.header("X-Mashape-Authorization", SST_Manager.getInstance().X_MASHAPE_AUTHORIZATION_KEY)
				.asJson();

			tmpOutput = TranslationManager.getInstance().gson.fromJson(request.getBody().toString(), com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput.class);

		}
		catch(Exception e)
		{
			System.out.println("\n * StaticImageMap EXCEPTION: " + e.getMessage() + "\n");
		}
                
                //additional properties - input examples
                tmpOutput.setAdditionalProperty("latitude_example", "40.6403");
                tmpOutput.setAdditionalProperty("longitude_example", "22.9439");

		//output
		String tmpOutputStr = "";
                if(tmpOutput != null)
                {
                    try
                    {
                    if(tmpOutput.getHtml() != null)
                        tmpOutput.setHtml(URLEncoder.encode(StringEscapeUtils.escapeHtml(tmpOutput.getHtml()), "UTF-8"));
                    else
                        tmpOutput.setHtml("null");
                    if(tmpOutput.getImageUrl() != null)
                        tmpOutput.setImageUrl(URLEncoder.encode(tmpOutput.getImageUrl(), "UTF-8"));
                    else
                        tmpOutput.setImageUrl("null");
                    if(tmpOutput.getProviderUrl() != null)
                        tmpOutput.setProviderUrl(URLEncoder.encode(tmpOutput.getProviderUrl(), "UTF-8"));
                    else
                        tmpOutput.setProviderUrl("null");
                    if(tmpOutput.getRateLimitUrl() != null)
                        tmpOutput.setRateLimitUrl(URLEncoder.encode(tmpOutput.getRateLimitUrl(), "UTF-8"));
                    else
                        tmpOutput.setRateLimitUrl("null");
                    if(tmpOutput.getTermsOfUseUrl() != null)
                        tmpOutput.setTermsOfUseUrl(URLEncoder.encode(tmpOutput.getTermsOfUseUrl(), "UTF-8"));
                    else
                        tmpOutput.setTermsOfUseUrl("null");
                    }
                    catch(Exception e)
                    {
                        tmpOutputStr = "EXCEPTION -> " + reqStr + " -> " + e.getMessage();
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                            tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
                    } catch (IOException ex) {
                            Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    tmpOutputStr = "EXCEPTION -> " + reqStr;
                }
                
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/call_URLScreenshotGenerator")
	@Consumes("application/json")
	public Response call_URLScreenshotGenerator(com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput tmpInput)
	{
		com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput tmpOutput = null;

		try
		{
			HttpResponse<JsonNode> request = Unirest.get("https://jmillerdesign-url-screenshot.p.mashape.com/api?url=" + tmpInput.getInputUrl() + "&format=png&width=1024&height=800&delay=5")
				.header("X-Mashape-Authorization", SST_Manager.getInstance().X_MASHAPE_AUTHORIZATION_KEY)
				.asJson();

			tmpOutput = TranslationManager.getInstance().gson.fromJson(request.getBody().toString(), com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput.class);

		}
		catch(Exception e)
		{
			System.out.println("\n * URLScreenshotGenerator EXCEPTION: " + e.getMessage() + "\n");
		}
                
                //additional properties - input examples
                tmpOutput.setAdditionalProperty("url_example", "http://www.accessible-project.eu");

		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/call_Translatewebpage")
	@Consumes("application/json")
	public Response call_Translatewebpage(com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput tmpInput)
	{
		com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput tmpOutput = new com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput();

		try
		{
                    String URLToReturn = "Connection error!";
                    String translatedWebPageURL = "";
                    try {
                        translatedWebPageURL = translate(URLDecoder.decode(tmpInput.getInputUrl(), "UTF-8"), tmpInput.getTargetLanguage());
                        URLToReturn = "http://" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/" + translatedWebPageURL;
                    } catch (Exception ex) {
                        Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
                    }        

                    if(tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_CATALAN.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_CHINESE_SIMPLIFIED.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_CHINESE_TRADITIONAL.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_CZECH.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_ENGLISH.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_FINNISH.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_FRENCH.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_GERMAN.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_GREEK.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_HUNGARIAN.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_ITALIAN.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_LATVIAN.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_PORTUGUESE.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_ROMANIAN.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_SLOVAK.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_SPANISH.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_SWEDISH.toLowerCase()) == false
                            && tmpInput.getTargetLanguage().toLowerCase().equals(TranslationManager.GPII_TURKISH.toLowerCase()) == false)
                        URLToReturn = "Language code \"" + tmpInput.getTargetLanguage() + "\" is not supported! Supported codes: " 
                                + "\"" + TranslationManager.GPII_CATALAN.toLowerCase() + "\" (CATALAN), "
                                + "\"" + TranslationManager.GPII_CHINESE_SIMPLIFIED.toLowerCase() + "\" (CHINESE SIMPLIFIED), "
                                + "\"" + TranslationManager.GPII_CHINESE_TRADITIONAL.toLowerCase() + "\" (CHINESE TRADITIONAL), "
                                + "\"" + TranslationManager.GPII_CZECH.toLowerCase() + "\" (CZECH), "
                                + "\"" + TranslationManager.GPII_ENGLISH.toLowerCase() + "\" (ENGLISH), "
                                + "\"" + TranslationManager.GPII_FINNISH.toLowerCase() + "\" (FINNISH), "
                                + "\"" + TranslationManager.GPII_FRENCH.toLowerCase() + "\" (FRENCH), "
                                + "\"" + TranslationManager.GPII_GERMAN.toLowerCase() + "\" (GERMAN), "
                                + "\"" + TranslationManager.GPII_GREEK.toLowerCase() + "\" (GREEK), "
                                + "\"" + TranslationManager.GPII_HUNGARIAN.toLowerCase() + "\" (HUNGARIAN), "
                                + "\"" + TranslationManager.GPII_ITALIAN.toLowerCase() + "\" (ITALIAN), "
                                + "\"" + TranslationManager.GPII_LATVIAN.toLowerCase() + "\" (LATVIAN), "
                                + "\"" + TranslationManager.GPII_PORTUGUESE.toLowerCase() + "\" (PORTUGUESE), "
                                + "\"" + TranslationManager.GPII_ROMANIAN.toLowerCase() + "\" (ROMANIAN), "
                                + "\"" + TranslationManager.GPII_SLOVAK.toLowerCase() + "\" (SLOVAK), "
                                + "\"" + TranslationManager.GPII_SPANISH.toLowerCase() + "\" (SPANISH), "
                                + "\"" + TranslationManager.GPII_SWEDISH.toLowerCase() + "\" (SWEDISH) and "
                                + "\"" + TranslationManager.GPII_TURKISH.toLowerCase() + "\" (TURKISH)";

                    tmpOutput.setFinalUrl(URLEncoder.encode(URLToReturn, "UTF-8"));
                    tmpOutput.setTargetLanguageCode(tmpInput.getTargetLanguage());
		}
		catch(Exception e)
		{
			System.out.println("\n * Translatewebpage EXCEPTION: " + e.getMessage() + "\n");
		}
                
                //additional properties - input examples
                tmpOutput.setAdditionalProperty("urlToTranslate_example", "http://www.accessible-project.eu");
                tmpOutput.setAdditionalProperty("targetLanguage_example", "el, es, fr, etc.");

		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/call_Identifyimageswithoutaltattributeinwebpage")
	@Consumes("application/json")
	public Response call_Identifyimageswithoutaltattributeinwebpage(com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput tmpInput)
	{
		com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageOutput.IdentifyimageswithoutaltattributeinwebpageOutput tmpOutput = new com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageOutput.IdentifyimageswithoutaltattributeinwebpageOutput();

		try
		{
                    String URLToReturn = "Connection error!";
                    String generatedWebPageURL = "";
                    try {
                        generatedWebPageURL = identifyImagesAndAddAltTextWhereMissing(URLDecoder.decode(tmpInput.getInputUrl(), "UTF-8"));
                        URLToReturn = "http://" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/" + generatedWebPageURL;
                    } catch (Exception ex) {
                        Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
                    }        

                    tmpOutput.setFinalUrl(URLEncoder.encode(URLToReturn, "UTF-8"));
		}
		catch(Exception e)
		{
			System.out.println("\n * Identifyimageswithoutaltattributeinwebpage EXCEPTION: " + e.getMessage() + "\n");
		}
                
                //additional properties - input examples
                tmpOutput.setAdditionalProperty("url_example", "http://legacy.montevallo.edu/counselingcenter");

		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/call_CallWebAnywhere")
	@Consumes("application/json")
	public Response call_CallWebAnywhere(com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput tmpInput)
	{
		com.certh.iti.cloud4all.restful.CallWebAnywhereOutput.CallWebAnywhereOutput tmpOutput = new com.certh.iti.cloud4all.restful.CallWebAnywhereOutput.CallWebAnywhereOutput();
                String URLToReturn = "";
		try
		{
                    URLToReturn = "http://webanywhere.cs.washington.edu/beta/?starting_url=" + URLDecoder.decode(tmpInput.getInputUrl(), "UTF-8") + "&locale=";

                    if(tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_CATALAN.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_CHINESE_SIMPLIFIED.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_CHINESE_TRADITIONAL.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_CZECH.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_ENGLISH.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_FINNISH.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_FRENCH.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_GERMAN.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_GREEK.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_HUNGARIAN.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_ITALIAN.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_LATVIAN.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_PORTUGUESE.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_ROMANIAN.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_SLOVAK.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_SPANISH.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_SWEDISH.toLowerCase()) == false
                        && tmpInput.getVoiceLanguage().toLowerCase().equals(TranslationManager.GPII_TURKISH.toLowerCase()) == false)
                        URLToReturn = "Language code \"" + tmpInput.getVoiceLanguage() + "\" is not supported! Supported codes: " 
                            + "\"" + TranslationManager.GPII_CATALAN.toLowerCase() + "\" (CATALAN), "
                            + "\"" + TranslationManager.GPII_CHINESE_SIMPLIFIED.toLowerCase() + "\" (CHINESE SIMPLIFIED), "
                            + "\"" + TranslationManager.GPII_CHINESE_TRADITIONAL.toLowerCase() + "\" (CHINESE TRADITIONAL), "
                            + "\"" + TranslationManager.GPII_CZECH.toLowerCase() + "\" (CZECH), "
                            + "\"" + TranslationManager.GPII_ENGLISH.toLowerCase() + "\" (ENGLISH), "
                            + "\"" + TranslationManager.GPII_FINNISH.toLowerCase() + "\" (FINNISH), "
                            + "\"" + TranslationManager.GPII_FRENCH.toLowerCase() + "\" (FRENCH), "
                            + "\"" + TranslationManager.GPII_GERMAN.toLowerCase() + "\" (GERMAN), "
                            + "\"" + TranslationManager.GPII_GREEK.toLowerCase() + "\" (GREEK), "
                            + "\"" + TranslationManager.GPII_HUNGARIAN.toLowerCase() + "\" (HUNGARIAN), "
                            + "\"" + TranslationManager.GPII_ITALIAN.toLowerCase() + "\" (ITALIAN), "
                            + "\"" + TranslationManager.GPII_LATVIAN.toLowerCase() + "\" (LATVIAN), "
                            + "\"" + TranslationManager.GPII_PORTUGUESE.toLowerCase() + "\" (PORTUGUESE), "
                            + "\"" + TranslationManager.GPII_ROMANIAN.toLowerCase() + "\" (ROMANIAN), "
                            + "\"" + TranslationManager.GPII_SLOVAK.toLowerCase() + "\" (SLOVAK), "
                            + "\"" + TranslationManager.GPII_SPANISH.toLowerCase() + "\" (SPANISH), "
                            + "\"" + TranslationManager.GPII_SWEDISH.toLowerCase() + "\" (SWEDISH) and "
                            + "\"" + TranslationManager.GPII_TURKISH.toLowerCase() + "\" (TURKISH)";
                    else
                        URLToReturn = URLToReturn + tmpInput.getVoiceLanguage();
                    
                    tmpOutput.setFinalUrl(URLEncoder.encode(URLToReturn, "UTF-8"));
		}
		catch(Exception e)
		{
			System.out.println("\n * CallWebAnywhere EXCEPTION: " + e.getMessage() + "\n");
		}
                
                //additional properties - input examples
                tmpOutput.setAdditionalProperty("urlToOpen_example", "http://www.accessible-project.eu");
                tmpOutput.setAdditionalProperty("voiceLanguage_example", "el, es, fr, etc.");

		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/call_CaptchaResolver")
	@Consumes("application/json")
	public Response call_CaptchaResolver(com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput tmpInput)
	{
		com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput tmpOutput = null;

		try
		{
                        //https://www.mashape.com/metropolis-api/captcha#!endpoint-solve
			HttpResponse<JsonNode> request = Unirest.get("https://metropolis-api-captcha.p.mashape.com/solve?image=" + tmpInput.getInputUrl() + "&timeout=%3Ctimeout%3E")
				.header("X-Mashape-Authorization", SST_Manager.getInstance().X_MASHAPE_AUTHORIZATION_KEY)
				.asJson();

			tmpOutput = TranslationManager.getInstance().gson.fromJson(request.getBody().toString(), com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput.class);
		}
		catch(Exception e)
		{
			System.out.println("\n * CaptchaResolver EXCEPTION: " + e.getMessage() + "\n");
		}
                
                //additional properties - input examples
                tmpOutput.setAdditionalProperty("captchaImageUrl_example", "http://www.metropolisapi.com/temp/metropolis_api.jpg");

		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
                //replace "apiMessage" with "api-message"
                tmpOutputStr = tmpOutputStr.replaceAll("apiMessage", "api-message");
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/call_TextToSpeech")
	@Consumes("application/json")
	public Response call_TextToSpeech(com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput tmpInput)
	{
		com.certh.iti.cloud4all.restful.TextToSpeechOutput.TextToSpeechOutput tmpOutput = new com.certh.iti.cloud4all.restful.TextToSpeechOutput.TextToSpeechOutput();
                tmpInput.setTextToSpeak(tmpInput.getTextToSpeak().replace(' ', '+'));
                
                Long curMilliseconds = System.currentTimeMillis();
                deleteOldTemporaryFiles();

                String tmpURLToReturn = "";
                String tmpFilepath = "";
                if(SST_Manager.getInstance().USE_TIMESTAMP_IN_OUTPUT_FILENAMES)
                {    
                    tmpURLToReturn = "http://" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/" + "textToSpeech_" + Long.toString(curMilliseconds) + ".mp3";
                    tmpFilepath = SST_Manager.getInstance().HTDOCS_DIR + "/" + "textToSpeech_" + Long.toString(curMilliseconds) + ".mp3";
                }
                else
                {
                    tmpURLToReturn = "http://" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/" + "textToSpeech.mp3";
                    tmpFilepath = SST_Manager.getInstance().HTDOCS_DIR + "/" + "textToSpeech.mp3";
                }

		try
		{
			HttpResponse<InputStream> request = Unirest.get("https://montanaflynn-text-to-speech.p.mashape.com/speak?text=" + tmpInput.getTextToSpeak())
				.header("X-Mashape-Authorization", SST_Manager.getInstance().X_MASHAPE_AUTHORIZATION_KEY)
				.asBinary();
                        
                        InputStream instream = request.getRawBody();

                        OutputStream output = new FileOutputStream(tmpFilepath);
                        byte[] buffer = new byte[1024];

                        for (int length = 0; (length = instream.read(buffer)) > -1;) {
                            output.write(buffer, 0, length);
                        }

                        output.close();
                        instream.close();
                        
			tmpOutput.setSpokenTextMp3URL(tmpURLToReturn);

		}
		catch(Exception e)
		{
			System.out.println("\n * TextToSpeech EXCEPTION: " + e.getMessage() + "\n");
		}
                
                //additional properties - input examples
                tmpOutput.setAdditionalProperty("textToSpeak_example", "Welcome to the text-to-speech service!");

		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/call_FontConverter")
	@Consumes("application/json")
	public Response call_FontConverter(com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput tmpInput)
	{
            com.certh.iti.cloud4all.restful.FontConverterOutput.FontConverterOutput tmpOutput = new com.certh.iti.cloud4all.restful.FontConverterOutput.FontConverterOutput();

		try
		{
                    String URLToReturn = "Connection error!";
                    String fontConvertedWebPageURL = "";
                    
                    String fontFamilyToBeApplied = tmpInput.getTargetFontFamily();
                    if(fontFamilyToBeApplied==null || fontFamilyToBeApplied.trim().length()==0)
                        fontFamilyToBeApplied = "NOT DEFINED";
                    String fontSizeToBeApplied = tmpInput.getTargetFontSize();
                    if(fontSizeToBeApplied==null || fontSizeToBeApplied.trim().length()==0)
                        fontSizeToBeApplied = "NOT DEFINED";
                    String colorToBeApplied = tmpInput.getTargetColor();
                    if(colorToBeApplied==null || colorToBeApplied.trim().length()==0)
                        colorToBeApplied = "NOT DEFINED";
                    String backgroundToBeApplied = tmpInput.getTargetBackground();
                    if(backgroundToBeApplied==null || backgroundToBeApplied.trim().length()==0)
                        backgroundToBeApplied = "NOT DEFINED";
                    
                    try {
                        fontConvertedWebPageURL = changeFont(URLDecoder.decode(tmpInput.getInputUrl(), "UTF-8"), fontFamilyToBeApplied, fontSizeToBeApplied, colorToBeApplied, backgroundToBeApplied);
                        URLToReturn = "http://" + SST_Manager.getInstance().APACHE_SERVER_IP + "/" + SST_Manager.getInstance().DIR_IN_HTDOCS + "/" + fontConvertedWebPageURL;
                    } catch (Exception ex) {
                        Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
                    }        

                    tmpOutput.setFinalUrl(URLEncoder.encode(URLToReturn, "UTF-8"));
		}
		catch(Exception e)
		{
			System.out.println("\n * call_FontConverter EXCEPTION: " + e.getMessage() + "\n");
		}

                //additional properties - input examples
                tmpOutput.setAdditionalProperty("urlToConvertFont_example", "http://www.accessible-project.eu");
                tmpOutput.setAdditionalProperty("targetFontFamily", "Times New Roman, Arial, etc.");
                tmpOutput.setAdditionalProperty("targetFontSize_example", "12, 18, 22, etc.");
                tmpOutput.setAdditionalProperty("targetColor_example", "red, blue, rgb(215,226,240), etc.");
                tmpOutput.setAdditionalProperty("targetBackground_example", "black, yellow, rgb(205,216,210), etc.");
                
		//output
		String tmpOutputStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			tmpOutputStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(tmpOutput);
		} catch (IOException ex) {
			Logger.getLogger(SST_WebService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return Response.ok(tmpOutputStr, MediaType.APPLICATION_JSON).build();
	}
}
