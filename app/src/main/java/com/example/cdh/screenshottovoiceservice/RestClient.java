package com.example.cdh.screenshottovoiceservice;
import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by Corey on 4/20/2017.
 * http://stackoverflow.com/questions/6047194/how-to-call-a-restful-web-service-from-android
 */

public class RestClient {
        public static final String POST_IMAGE_URL = "describephoto/";
        public static final String POST_PREFERENCES_URL = "loadpreferences/";
        public static final String GET_TEST_URL ="testjson/";

        public RestClient()
        {}

        public String postImageToRestApi(RequestParams rp)
        {
                Log.d("RESTAPI", "Sending Image");
                Log.d("RP", rp.toString());

                final String[] finalResponse = {"post call was unsuccessful."};
                HttpUtils.post(POST_IMAGE_URL, rp, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                                // If the response is JSONObject instead of expected JSONArray
                                Log.d("JSONResponse", "---------------- this is response : " + response);
                                try {
                                        JSONObject serverResp = new JSONObject(response.toString());
                                        int isOutdoor = serverResp.getInt("Outdoor");

                                        Log.d("Is Outdoor", String.valueOf(isOutdoor));
                                        if (isOutdoor == 1)
                                        {
                                                finalResponse[0] = "The photograph was taken outside.";
                                        }
                                        else if (isOutdoor == 0)
                                        {
                                                finalResponse[0] = "The photograph was taken inside.";
                                        }

                                } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                // If the response is JSONObject instead of expected JSONArray
                                Log.d("StringResponse", "---------------- this is response : " + responseString);
                                try {
                                        // JSONObject serverResp = new JSONObject(response.toString());
                                        finalResponse[0] = responseString;
                                } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Log.d("Failed: ", ""+statusCode);
                                Log.d("Error : ", "" + throwable);
                        }

                });

                return finalResponse[0];
        }


        public String getTestFromRestApi(RequestParams rp)
        {
                final String[] finalResponse = {""};
                HttpUtils.get(GET_TEST_URL, rp, new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                                // If the response is JSONObject instead of expected JSONArray
                                Log.d("Response", "---------------- this is response : " + response);
                                try {
                                        JSONObject serverResp = new JSONObject(response.toString());
                                        JSONArray peopleResp = serverResp.getJSONArray("People");
                                        finalResponse[0] = parseServiceResponse(serverResp);
                                        Log.d("Array Response", "---------------- Length : " + peopleResp.length());
                                        Log.d("Response", "---------------- String : " +parseServiceResponse(serverResp));
                                } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                        }

                });
                Log.d("Response", "---------------- triggered");
                return finalResponse[0];
        }

        private String parseServiceResponse(JSONObject serverResp) throws JSONException
        {

                String responseString = "";
                int isOutdoor = serverResp.getInt("Outdoor");
                int recommendation = serverResp.getInt("Recommendation");
                JSONArray peopleResp = serverResp.getJSONArray("People");
                if(peopleResp.length() == 0)
                {
                        responseString += "There are no people in this photo.";
                }
                else if(peopleResp.length() == 1)
                {
                        responseString += "There is one person in this photo.";
                }
                else
                {
                        responseString += "There are " +Integer.toString(peopleResp.length()) +" people in this photo.";
                }

                if(isOutdoor == 1)
                {
                        responseString += " The photo was taken outdoors.";
                }
                else
                {
                        responseString += " The photo was taken indoors.";
                }

                for(int index = 0; index < peopleResp.length(); index++)
                {
                        JSONObject person = peopleResp.getJSONObject(index).getJSONObject("Person");
                        String temp = " Person " +Integer.toString(index + 1) + " is ";
                        if(person.getInt("Gender") == 1)
                        {
                                temp += " male ";
                        }
                        else
                        {
                                temp += " female ";
                        }

                        if(person.getInt("LongFace") == 1)
                        {
                                temp += " with a Long Face ";
                        }
                        else
                        {
                                temp += " with a Round Face,";
                        }

                        if(person.getInt("LongHair") == 1)
                        {
                                temp += "and long hair. ";
                        }
                        else
                        {
                                temp += "and short hair. ";
                        }

                        if(person.getInt("Smiling") == 1)
                        {
                                if(person.getInt("Gender") == 1)
                                {
                                        temp += "  He is smiling";
                                }

                                else
                                {
                                        temp += "  She is smiling";
                                }
                        }
                        else
                        {
                                if(person.getInt("Gender") == 1)
                                {
                                        temp += "  He is not smiling";
                                }

                                else
                                {
                                        temp += "  She is not smiling";
                                }
                        }

                        responseString += temp;
                }


                responseString += "  Based on this photo you have a " +Integer.toString(recommendation) +" percent change of liking this person.";
                return responseString +".";
        }

}
