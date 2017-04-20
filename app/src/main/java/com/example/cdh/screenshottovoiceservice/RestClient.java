package com.example.cdh.screenshottovoiceservice;
import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Corey on 4/20/2017.
 * http://stackoverflow.com/questions/6047194/how-to-call-a-restful-web-service-from-android
 */

public class RestClient {
        public static final String POST_IMAGE_URL = "describephoto/";
        public static final String POST_PREFERENCES_URL = "loadpreferences/";

        public RestClient()
        {}

        public String postImageToRestApi(RequestParams rp)
        {
                final String[] finalResponse = {"post call was unsuccessful."};
                HttpUtils.post(POST_IMAGE_URL, rp, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                                // If the response is JSONObject instead of expected JSONArray
                                Log.d("JSONResponse", "---------------- this is response : " + response);
                                try {
                                        JSONObject serverResp = new JSONObject(response.toString());
                                        finalResponse[0] = serverResp.toString();
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

                /*        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                // If the response is JSONObject instead of expected JSONArray
                                Log.d("Response", "---------------- this is response : " + responseString);
                                try {
                                        // JSONObject serverResp = new JSONObject(response.toString());
                                        finalResponse[0] = responseString;
                                } catch (Exception e) {
                                        // TODO Auto-generated catch block


                                }
                        } */

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
                HttpUtils.get(POST_IMAGE_URL, rp, new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                                // If the response is JSONObject instead of expected JSONArray
                                Log.d("Response", "---------------- this is response : " + response);
                                try {
                                        JSONObject serverResp = new JSONObject(response.toString());
                                        finalResponse[0] = serverResp.toString();
                                } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                        }

                });

                return finalResponse[0];
        }


}
