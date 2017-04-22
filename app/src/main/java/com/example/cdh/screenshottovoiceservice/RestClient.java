package com.example.cdh.screenshottovoiceservice;
import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
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
        public static final String GET_TEST_URL ="time/";

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
                final String[] finalResponse = {"post call was unsuccessful."};
                HttpUtils.get(GET_TEST_URL, rp, new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                                // If the response is JSONObject instead of expected JSONArray
                                Log.d("Response", "---------------- this is response : " + response);
                                try {
                                        JSONObject serverResp = new JSONObject(response.toString());
                                        //finalResponse[0] = serverResp.toString();
                                        finalResponse[0] = "Post call was successful.";
                                } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                        }

                });

                return finalResponse[0];
        }


}
