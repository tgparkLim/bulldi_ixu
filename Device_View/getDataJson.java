//package openstack.bulldi.safe3x.Device_View;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
////import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.TextView;
//
//import com.google.android.gms.awareness.fence.LocationFence;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import openstack.bulldi.safe3x.R;
//
//public class getDataJson extends AppCompatActivity {
//    String sData;
//    TextView tv;
//
//    String Data = "";
//
//    String id = "";
//    String latitude = "";
//    String longitude = "";
//
//    String result;
//    InputStream isr;
//    Context con;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.activity_main);
//        //tv=(TextView)findViewById(R.id.test);
//
//        Thread transJson = new Thread() {
//          @Override
//            public void run() {
//              while(!isInterrupted()) {
//                  try {
//                      Thread.sleep(10000);
//
//                      runOnUiThread(new Runnable() {
//                          @Override
//                          public void run() {
//                              new getData().execute("");
//                          }
//                      });
//                  } catch (InterruptedException e) {
//                      e.printStackTrace();
//                  }
//              }
//          }
//        };
//        transJson.start();
//        //new getData().execute("");
//
//        String idTest = id;
//
//    }
//
//
//    final public class getData extends AsyncTask<String, Void, String> {
//        String name;
//
//        @Override
//        protected String doInBackground(String... params) {
//            result = "";
//            isr = null;
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost("http://13.124.11.28/toAndroid/getdata.php"); //YOUR PHP SCRIPT ADDRESS
//                HttpResponse response = httpclient.execute(httppost);
//                HttpEntity entity = response.getEntity();
//                isr = entity.getContent();
//            } catch (Exception e) {
//                Log.e("log_tag", "Error in http connection " + e.toString());
//
//            }
//
//            //convert response to string
//            try {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//                isr.close();
//
//                result = sb.toString();
//            } catch (Exception e) {
//                Log.e("log_tag", "Error  converting result " + e.toString());
//            }
//
//            try {
//
//                JSONArray jArray = new JSONArray(result);
//
//                for (int i = 0; i < jArray.length(); i++) {
//                    JSONObject json = jArray.getJSONObject(i);
//
//
//
//                    id = id + "\n" + json.getString("id");
//                    Intent idData_intent = new Intent(getApplicationContext(), LocationFenceActivity.class);
//                    idData_intent.putExtra("1id", id);
//                    startActivity(idData_intent);
//
//                    latitude = latitude + "\n" + json.getString("latitude");
//                    Intent latitude_intent = new Intent(getApplicationContext(), LocationFenceActivity.class);
//                    latitude_intent.putExtra("2latitude", latitude);
//                    startActivity(latitude_intent);
//
//                    longitude = longitude + "\n" + json.getString("longitude");
//                    Intent longitude_intent = new Intent(getApplicationContext(), LocationFenceActivity.class);
//                    longitude_intent.putExtra("3longitude", longitude);
//                    startActivity(longitude_intent);
//
//
//                }
//
//            } catch (Exception e) {
//                // TODO: handle exception
//                Log.e("log_tag", "Error Parsing Data " + e.toString());
//            }
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            //tv.setText(""+latitude);
//
//
//
//
//        }
//
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected void onProgressUpdate(Void... values) {}
//    }
//
//
//
//}
