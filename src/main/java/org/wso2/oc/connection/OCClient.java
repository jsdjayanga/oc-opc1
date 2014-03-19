package org.wso2.oc.connection;


import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.wso2.carbon.server.admin.common.IServerAdmin;
import org.wso2.carbon.server.admin.service.ServerAdmin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by jayanga on 3/17/14.
 */
public class OCClient {

    IServerAdmin serverAdmin;
    DefaultHttpClient httpClient;
    boolean registered;

    public static double id;

    public OCClient(IServerAdmin serverAdmin){
        this.serverAdmin = serverAdmin;
        httpClient = null;
        registered = false;
    }

    public boolean connect(){

        if (registered == true){
            return true;
        }

        //Define a postRequest request
        HttpPost postRequest = new HttpPost("http://192.168.184.1:9763/operation_center/api/register");

        //Set the API media type in http content-type header
        postRequest.addHeader("content-type", "application/json");

        String data = "";
        try {

            id = Math.random();

            data = "{\"registration\": [{ \"servername\":\"" + serverAdmin.getServerData().getServerName()+ "-" + id
                    + "\" , \"status\":\"" + ((ServerAdmin)serverAdmin).getServerStatus()
                    + "\" , \"seconds\":\"" + serverAdmin.getServerData().getServerUpTime().getSeconds()
                    + "\" },{ \"firstName\":\"Anna\" , \"lastName\":\"Smith\" },{ \"firstName\":\"Peter\" , \"lastName\":\"Jones\" }]}";
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set the request post body
        StringEntity userEntity = null;
        try {
            userEntity = new StringEntity(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        postRequest.setEntity(userEntity);

        //Send the request; It will immediately return the response in HttpResponse object if any
        HttpResponse response = null;
        try {
            if (httpClient == null){
                httpClient = new DefaultHttpClient();
            }

            response = httpClient.execute(postRequest);
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        //verify the valid error code first
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 201)
        {
            throw new RuntimeException("Failed with HTTP error code : " + statusCode);
        }

        try {

            registered = true;

            System.out.println(response.getEntity().getContent().toString());

            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(responseString);


            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void sendUpdate(String data){

        if (connect() == true){
            //Define a postRequest request
            HttpPost postRequest = new HttpPost("http://192.168.184.1:9763/operation_center/api/update");

            //Set the API media type in http content-type header
            postRequest.addHeader("content-type", "application/json");

            //Set the request post body
            StringEntity userEntity = null;
            try {
                userEntity = new StringEntity(data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            postRequest.setEntity(userEntity);

            //Send the request; It will immediately return the response in HttpResponse object if any
            HttpResponse response = null;
            try {
                response = httpClient.execute(postRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //verify the valid error code first
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200)
            {
                throw new RuntimeException("Failed with HTTP error code : " + statusCode);
            }

            try {
                System.out.println("+++++++" + response.getEntity().getContentType() + "|" + response.getEntity().getContentEncoding() +
                        "|" + response.getEntity().getContentLength() + "|" +
                        response.getEntity().getContent().toString());

                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(responseString);

                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
