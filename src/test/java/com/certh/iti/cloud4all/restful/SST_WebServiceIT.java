package com.certh.iti.cloud4all.restful;

import com.certh.iti.cloud4all.translation.TranslationManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import junit.framework.TestCase;

public class SST_WebServiceIT extends TestCase
{
    public void test_CombinedServices()
    {
        SynthesizerInput tmpInput = new SynthesizerInput();
        
        //input for each service
        ArrayList<ServiceInputTemplate> tmpInputForAllServices = new ArrayList<ServiceInputTemplate>();
        
        //Translatewebpage
        ServiceInputTemplate tmpInputForTraslatewebpage = new ServiceInputTemplate<com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput>();
        tmpInputForTraslatewebpage.setServiceName("Translatewebpage");
        com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput tmpInputObjForTraslatewebpage = new com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput();
        tmpInputObjForTraslatewebpage.setUrlToTranslate("http://www.accessible-project.eu");
        tmpInputObjForTraslatewebpage.setTargetLanguage("el");
        tmpInputForTraslatewebpage.setServiceInput(tmpInputObjForTraslatewebpage);
        tmpInputForAllServices.add(tmpInputForTraslatewebpage);
        
        //FontConverter
        ServiceInputTemplate tmpInputForFontConverter = new ServiceInputTemplate<com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput>();
        tmpInputForFontConverter.setServiceName("FontConverter");
        com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput tmpInputObjForFontConverter = new com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput();
        tmpInputObjForFontConverter.setUrlToConvertFont("OUTPUT_OF_ANOTHER_SERVICE");
        tmpInputObjForFontConverter.setTargetFontFamily("Arial");
        tmpInputObjForFontConverter.setTargetFontSize("18");
        tmpInputObjForFontConverter.setTargetColor("blue");
        tmpInputObjForFontConverter.setTargetBackground("yellow");
        tmpInputForFontConverter.setServiceInput(tmpInputObjForFontConverter);
        tmpInputForAllServices.add(tmpInputForFontConverter);
        
        tmpInput.setInput(tmpInputForAllServices);
        
        //mapped variables
        mappedVariable tmpMappedVariable = new mappedVariable();
        tmpMappedVariable.setFromServiceName("Translatewebpage");
        tmpMappedVariable.setFromVariableName("urlOfTranslatedPage");
        tmpMappedVariable.setToServiceName("FontConverter");
        tmpMappedVariable.setToVariableName("urlToConvertFont");
        ArrayList<mappedVariable> tempMappedVariables = new ArrayList<mappedVariable>();
        tempMappedVariables.add(tmpMappedVariable);
        tmpInput.setMappedVariables(tempMappedVariables);        
        
        String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);
        
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/SST/callCombinedServices");        
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);
        
        // check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }        
        String tmpOutputJson = response.getEntity(String.class);        
        
        System.out.println("Testing 'callCombinedServices' web-service...");
        System.out.println("'callCombinedServices' Input:");
        System.out.println(tmpInputJson);
        System.out.println("'callCombinedServices' Output:");
        System.out.println(tmpOutputJson);
        
        //SynthesizerOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, SynthesizerOutput.class);
        
        assertEquals(1, 1);
        //assertEquals(tmpOutput.size(), 7);
    }
    
    /*public void test_IPtoLatLng() 
    {
        com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput tmpInput = new com.certh.iti.cloud4all.restful.IPtoLatLngInput.IPtoLatLngInput();
        tmpInput.setIp("160.40.50.130");
        String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);
        
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/SST/call_IPtoLatLng");        
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);
        
        // check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }        
        String tmpOutputJson = response.getEntity(String.class);        
        
        System.out.println("Testing 'call_IPtoLatLng' web-service...");
        System.out.println("'call_IPtoLatLng' Input:");
        System.out.println(tmpInputJson);
        System.out.println("'call_IPtoLatLng' Output:");
        System.out.println(tmpOutputJson);
        
        com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.IPtoLatLngOutput.IPtoLatLngOutput.class);
        
        assertEquals(tmpOutput.getCountry(), "Greece");
    }*/
    
    /*public void test_Weather()
    {
        com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput tmpInput = new com.certh.iti.cloud4all.restful.WeatherInput.WeatherInput();
        tmpInput.setLocation("Thessaloniki");
        String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);
        
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/SST/call_Weather");        
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);
        
        // check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }        
        String tmpOutputJson = response.getEntity(String.class);        
        
        System.out.println("Testing 'call_Weather' web-service...");
        System.out.println("'call_Weather' Input:");
        System.out.println(tmpInputJson);
        System.out.println("'call_Weather' Output:");
        System.out.println(tmpOutputJson);
        
        Type listType = new TypeToken<List<com.certh.iti.cloud4all.restful.WeatherOutput.Day>>() {}.getType();
        List<com.certh.iti.cloud4all.restful.WeatherOutput.Day> tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, listType);
        
        assertEquals(tmpOutput.size(), 7);
    }*/

    public void test_TelizeGeolocation()
    {
            com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput tmpInput = new com.certh.iti.cloud4all.restful.TelizeGeolocationInput.TelizeGeolocationInput();
            tmpInput.setIpAddress("160.40.50.130");
            String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);

            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8080/SST/call_TelizeGeolocation");
            ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);

            // check response status code
            if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            String tmpOutputJson = response.getEntity(String.class);

            System.out.println("Testing 'call_TelizeGeolocation' web-service...");
            System.out.println("'call_TelizeGeolocation' Input:");
            System.out.println(tmpInputJson);
            System.out.println("'call_TelizeGeolocation' Output:");
            System.out.println(tmpOutputJson);

            com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.TelizeGeolocationOutput.TelizeGeolocationOutput.class);

            assertEquals(tmpOutput.getCountry(), "Greece");
    }

    public void test_StaticImageMap()
    {
	com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput tmpInput = new com.certh.iti.cloud4all.restful.StaticImageMapInput.StaticImageMapInput();
	tmpInput.setLatitude("40.6403");
        tmpInput.setLongitude("22.9439");
	String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);

	Client client = Client.create();
	WebResource webResource = client.resource("http://localhost:8080/SST/call_StaticImageMap");
	ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);

	// check response status code
	if (response.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	}
	String tmpOutputJson = response.getEntity(String.class);

	System.out.println("Testing 'call_StaticImageMap' web-service...");
	System.out.println("'call_StaticImageMap' Input:");
	System.out.println(tmpInputJson);
	System.out.println("'call_StaticImageMap' Output:");
	System.out.println(tmpOutputJson);

	com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.StaticImageMapOutput.StaticImageMapOutput.class);

        assertTrue(tmpOutput.getImageUrl().length() > 0);
    }
    
    public void test_Identifyimageswithoutaltattributeinwebpage()
    {
	com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput tmpInput = new com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageInput.IdentifyimageswithoutaltattributeinwebpageInput();
	tmpInput.setUrl("http://legacy.montevallo.edu/counselingcenter");
        //tmpInput.setUrl("http://www.paul-simon.info/PHP/gallery_show.php?gallery=10");
        //tmpInput.setUrl("http://www.dreamstime.com/royalty-free-stock-images-beautiful-women-friends-playing-video-games-image20007619");
	String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);

	Client client = Client.create();
	WebResource webResource = client.resource("http://localhost:8080/SST/call_Identifyimageswithoutaltattributeinwebpage");
	ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);

	// check response status code
	if (response.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	}
	String tmpOutputJson = response.getEntity(String.class);

	System.out.println("Testing 'call_IdentifyimageswithoutaltattributeinwebpageInput' web-service...");
	System.out.println("'call_IdentifyimageswithoutaltattributeinwebpageInput' Input:");
	System.out.println(tmpInputJson);
	System.out.println("'call_IdentifyimageswithoutaltattributeinwebpageInput' Output:");
	System.out.println(tmpOutputJson);

	com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageOutput.IdentifyimageswithoutaltattributeinwebpageOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.IdentifyimageswithoutaltattributeinwebpageOutput.IdentifyimageswithoutaltattributeinwebpageOutput.class);

        assertTrue(tmpOutput.getUrlOfGeneratedPage().length() > 0);
    }

    public void test_CallWebAnywhere()
    {
	com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput tmpInput = new com.certh.iti.cloud4all.restful.CallWebAnywhereInput.CallWebAnywhereInput();
	tmpInput.setUrlToOpen("http://www.accessible-project.eu/");
        tmpInput.setVoiceLanguage(TranslationManager.WEBANYWHERE_GREEK);
	String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);

	Client client = Client.create();
	WebResource webResource = client.resource("http://localhost:8080/SST/call_CallWebAnywhere");
	ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);

	// check response status code
	if (response.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	}
	String tmpOutputJson = response.getEntity(String.class);

	System.out.println("Testing 'call_CallWebAnywhere' web-service...");
	System.out.println("'call_CallWebAnywhere' Input:");
	System.out.println(tmpInputJson);
	System.out.println("'call_CallWebAnywhere' Output:");
	System.out.println(tmpOutputJson);

	com.certh.iti.cloud4all.restful.CallWebAnywhereOutput.CallWebAnywhereOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.CallWebAnywhereOutput.CallWebAnywhereOutput.class);

	assertEquals(tmpOutput.getUrlToBeCalled(), "http%3A%2F%2Fwebanywhere.cs.washington.edu%2Fbeta%2F%3Fstarting_url%3Dhttp%3A%2F%2Fwww.accessible-project.eu%2F%26locale%3Del");
    }

    /*public void test_CaptchaResolver()
    {
	com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput tmpInput = new com.certh.iti.cloud4all.restful.CaptchaResolverInput.CaptchaResolverInput();
	tmpInput.setCaptchaImageUrl("http://www.metropolisapi.com/temp/metropolis_api.jpg");
	String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);

	Client client = Client.create();
	WebResource webResource = client.resource("http://localhost:8080/SST/call_CaptchaResolver");
	ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);

	// check response status code
	if (response.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	}
	String tmpOutputJson = response.getEntity(String.class);

	System.out.println("Testing 'call_CaptchaResolver' web-service...");
	System.out.println("'call_CaptchaResolver' Input:");
	System.out.println(tmpInputJson);
	System.out.println("'call_CaptchaResolver' Output:");
	System.out.println(tmpOutputJson);

	com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.CaptchaResolverOutput.CaptchaResolverOutput.class);

	assertEquals(tmpOutput.getCaptcha(), "metropolis api");
    }*/
    
    public void test_TextToSpeech()
    {
	com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput tmpInput = new com.certh.iti.cloud4all.restful.TextToSpeechInput.TextToSpeechInput();
	tmpInput.setTextToSpeak("This is a test!");
	String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);

	Client client = Client.create();
	WebResource webResource = client.resource("http://localhost:8080/SST/call_TextToSpeech");
	ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);

	// check response status code
	if (response.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	}
	String tmpOutputJson = response.getEntity(String.class);

	System.out.println("Testing 'call_TextToSpeech' web-service...");
	System.out.println("'call_TextToSpeech' Input:");
	System.out.println(tmpInputJson);
	System.out.println("'call_TextToSpeech' Output:");
	System.out.println(tmpOutputJson);

	com.certh.iti.cloud4all.restful.TextToSpeechOutput.TextToSpeechOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.TextToSpeechOutput.TextToSpeechOutput.class);

	assertTrue(tmpOutput.getSpokenTextMp3URL().length() > 0);
    }
    
    public void test_URLScreenshotGenerator()
    {
	com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput tmpInput = new com.certh.iti.cloud4all.restful.URLScreenshotGeneratorInput.URLScreenshotGeneratorInput();
	tmpInput.setUrl("http://www.accessible-project.eu");
	String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);

	Client client = Client.create();
	WebResource webResource = client.resource("http://localhost:8080/SST/call_URLScreenshotGenerator");
	ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);

	// check response status code
	if (response.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	}
	String tmpOutputJson = response.getEntity(String.class);

	System.out.println("Testing 'call_URLScreenshotGenerator' web-service...");
	System.out.println("'call_URLScreenshotGenerator' Input:");
	System.out.println(tmpInputJson);
	System.out.println("'call_URLScreenshotGenerator' Output:");
	System.out.println(tmpOutputJson);

	com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.URLScreenshotGeneratorOutput.URLScreenshotGeneratorOutput.class);

	assertTrue(tmpOutput.getScreenshot().length() > 0);
    }

    public void test_Translatewebpage()
    {
        com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput tmpInput = new com.certh.iti.cloud4all.restful.TranslatewebpageInput.TranslatewebpageInput();
        tmpInput.setUrlToTranslate("http://cloud4all.info/");
        tmpInput.setTargetLanguage(TranslationManager.GPII_FRENCH);
        String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);

        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/SST/call_Translatewebpage");
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);

        // check response status code
        if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        String tmpOutputJson = response.getEntity(String.class);

        System.out.println("Testing 'call_Translatewebpage' web-service...");
        System.out.println("'call_Translatewebpage' Input:");
        System.out.println(tmpInputJson);
        System.out.println("'call_Translatewebpage' Output:");
        System.out.println(tmpOutputJson);

        com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.TranslatewebpageOutput.TranslatewebpageOutput.class);

        //assertEquals(tmpOutput.getXXXX(), "XXXX");
    }

    public void test_FontConverter()
    {
        com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput tmpInput = new com.certh.iti.cloud4all.restful.FontConverterInput.FontConverterInput();
        //tmpInput.setUrlToConvertFont("http://www.accessible-project.eu");
        tmpInput.setUrlToConvertFont("http://cloud4all.info/");
        //tmpInput.setUrlToConvertFont("http://legacy.montevallo.edu/counselingcenter");
        //tmpInput.setUrlToConvertFont("http://www.iti.gr/iti/index.html");
        //tmpInput.setUrlToConvertFont("http://www.in.gr");
        //tmpInput.setUrlToConvertFont("http://edition.cnn.com/");
        tmpInput.setTargetFontFamily("Times New Roman");
        tmpInput.setTargetFontSize("16");
        tmpInput.setTargetColor("blue");
        tmpInput.setTargetBackground("rgb(215,226,240)");
        String tmpInputJson = TranslationManager.getInstance().gson.toJson(tmpInput);

        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/SST/call_FontConverter");
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tmpInputJson);

        // check response status code
        if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        String tmpOutputJson = response.getEntity(String.class);

        System.out.println("Testing 'call_FontConverter' web-service...");
        System.out.println("'call_FontConverter' Input:");
        System.out.println(tmpInputJson);
        System.out.println("'call_FontConverter' Output:");
        System.out.println(tmpOutputJson);

        com.certh.iti.cloud4all.restful.FontConverterOutput.FontConverterOutput tmpOutput = TranslationManager.getInstance().gson.fromJson(tmpOutputJson, com.certh.iti.cloud4all.restful.FontConverterOutput.FontConverterOutput.class);

        //assertEquals(tmpOutput.getXXXX(), "XXXX");
    }



	



	



	







	



	



	



	


	



	



	

}
