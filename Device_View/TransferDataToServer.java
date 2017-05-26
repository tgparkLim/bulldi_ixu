//package openstack.bulldi.safe3x.Device_View;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//
///**
// * Created by park on 2017-03-19.
// */
//
//public class TransferDataToServer {
//
//    public void Data_temp_co_smoke_TranferToServer() {
//
//        //temperature
//        double temperature_show = SensorTagIRTemperatureProfile.myList.get(SensorTagIRTemperatureProfile.myList.size() - 1);
//        final String tempNow = String.format("%.1f", temperature_show);
//
//        //co
//        double co_show = SensorTagCOProfile.myList_co.get(SensorTagCOProfile.myList_co.size() - 1);
//        final String coNow = String.format("%.0f ", co_show);
//
//        //smoke
//        double smoke_show = SensorTagSmokeProfile.myList_smoke.get(SensorTagSmokeProfile.myList_smoke.size() -1);
//        final String smokeNow = String.format("%.0f ", smoke_show);
//
//        //transfer Temperature
//        //get to current location
//        final Gpsinfo gps;
//
//        gps = new Gpsinfo(context);
//
//
//        final double latitude = gps.getLatitude();
//        final double longitude = gps.getLongitude();
//
//        final String slatitude = Double.toString(latitude);
//        final String slongitude = Double.toString(longitude);
//
//        Calendar calendar = Calendar.getInstance();
//        final String transtime = calendar.getTime().toString();
//
//        //transfer temperature to serverComputer phpmyadmin
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                Constants.gimpo_temp,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("temp", tempNow);
//                params.put("co", coNow);
//                params.put("smoke", smokeNow);
//                params.put("transtime", transtime);
//                params.put("latitude", slatitude);
//                params.put("longitude", slongitude);
//                return params;
//            }
//        };
//        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
//    }
//
//
//
//
//}
