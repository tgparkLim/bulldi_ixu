//package openstack.bulldi.safe3x.Device_View;
//
//import android.Manifest;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.awareness.Awareness;
//import com.google.android.gms.awareness.fence.AwarenessFence;
//import com.google.android.gms.awareness.fence.FenceState;
//import com.google.android.gms.awareness.fence.FenceUpdateRequest;
//import com.google.android.gms.awareness.fence.LocationFence;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.ResultCallbacks;
//import com.google.android.gms.common.api.Status;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.messaging.FirebaseMessaging;
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
//import static android.app.ProgressDialog.show;
//import static android.support.v7.appcompat.R.id.text;
//
//public class LocationFenceActivity extends AppCompatActivity {
//
//    private static final String IN_LOCATION_FENCE_KEY = "IN_LOCATION_FENCE_KEY";
//    private static final String EXITING_LOCATION_FENCE_KEY = "EXITING_LOCATION_FENCE_KEY";
//    private static final String ENTERING_LOCATION_FENCE_KEY = "ENTERING_LOCATION_FENCE_KEY";
//
//    public static final int STATUS_IN = 0;
//    public static final int STATUS_OUT = 1;
//    public static final int STATUS_ENTERING = 2;
//    public static final int STATUS_EXITING = 3;
//
//    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;
//
//    private static GoogleApiClient mGoogleApiClient;
//    private static PendingIntent mPendingIntent;
//    private LocationFenceReceiver mLocationFenceReceiver;
//
//    static String latitude;
//    static String longitude;
//
//    static double newLatitude;
//    static double newLongitude;
//
//    static double newQLatitude;
//    static double newQLongitude;
//    static String result;
//    static InputStream isr;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.activity_main);
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Awareness.API)
//                .build();
//        mGoogleApiClient.connect();
//
//        mLocationFenceReceiver = new LocationFenceReceiver();
//        Intent intent = new Intent(LocationFenceReceiver.FENCE_RECEIVER_ACTION);
//        mPendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//
//        new getData().execute("");
//    }
//
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        registerFences();
//        registerReceiver(mLocationFenceReceiver, new IntentFilter(LocationFenceReceiver.FENCE_RECEIVER_ACTION));
//
////        FirebaseMessaging.getInstance().subscribeToTopic("test");
////        FirebaseInstanceId.getInstance().getToken();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterFences();
//        unregisterReceiver(mLocationFenceReceiver);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    registerFences();
//                } else {
////                    Snackbar.make(mLayoutLocationFence,
////                            getString(R.string.error_loading_places),
////                            Snackbar.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//
//    public void registerFences() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
//        } else {
//            AwarenessFence inLocationFence = LocationFence.in(newLatitude, newLongitude, 200, 1);
//            //AwarenessFence inLocationFence = LocationFence.in(50.830951, -0.146978, 200, 1);
//            AwarenessFence exitingLocationFence = LocationFence.exiting(50.830951, -0.146978, 200);
//            AwarenessFence enteringLocationFence = LocationFence.entering(50.830951, -0.146978, 200);
//
//            Awareness.FenceApi.updateFences(
//                    mGoogleApiClient,
//                    new FenceUpdateRequest.Builder()
//                            .addFence(IN_LOCATION_FENCE_KEY, inLocationFence, mPendingIntent)
//                            .addFence(EXITING_LOCATION_FENCE_KEY, exitingLocationFence, mPendingIntent)
//                            .addFence(ENTERING_LOCATION_FENCE_KEY, enteringLocationFence, mPendingIntent)
//                            .build())
//                    .setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(@NonNull Status status) {
//                            if (status.isSuccess()) {
////                                Snackbar.make(mLayoutLocationFence,
////                                        "Fence Registered",
////                                        Snackbar.LENGTH_LONG).show();
//                            } else {
////                                Snackbar.make(mLayoutLocationFence,
////                                        "Fence Not Registered",
////                                        Snackbar.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//        }
//    }
//
//    private void unregisterFences() {
//        Awareness.FenceApi.updateFences(
//                mGoogleApiClient,
//                new FenceUpdateRequest.Builder()
//                        .removeFence(IN_LOCATION_FENCE_KEY)
//                        .removeFence(EXITING_LOCATION_FENCE_KEY)
//                        .removeFence(ENTERING_LOCATION_FENCE_KEY)
//                        .build()).setResultCallback(new ResultCallbacks<Status>() {
//            @Override
//            public void onSuccess(@NonNull Status status) {
////                Snackbar.make(mLayoutLocationFence,
////                        "Fence Removed",
////                        Snackbar.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFailure(@NonNull Status status) {
////                Snackbar.make(mLayoutLocationFence,
////                        "Fence Not Removed",
////                        Snackbar.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    public static class LocationFenceReceiver extends BroadcastReceiver {
//
//        public static final String FENCE_RECEIVER_ACTION =
//                //"com.hitherejoe.aware.ui.fence.LocationFenceReceiver.FENCE_RECEIVER_ACTION";
//        "openstack.bulldi.safe3x.Device_View.LocationFenceReceiver.FENCE_RECEIVER_ACTION";
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            FenceState fenceState = FenceState.extract(intent);
//
//            if (TextUtils.equals(fenceState.getFenceKey(), IN_LOCATION_FENCE_KEY)) {
//                switch (fenceState.getCurrentState()) {
//                    case FenceState.TRUE:
//                        //setHeadphoneState(STATUS_IN);
//                        break;
//                    case FenceState.FALSE:
//                        //setHeadphoneState(STATUS_OUT);
//                        //Toast.makeText(getApplicationContext(), "123", Toast.LENGTH_LONG).show();
//
//                        new AlertDialog.Builder(context)
//                                .setTitle("1")
//                                .setMessage("2")
//                                .setNeutralButton("3", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dlg, int sumthin) {
//
//                                    }
//                                })
//                                .show();
//
//                        break;
//                    case FenceState.UNKNOWN:
////                        Snackbar.make(mLayoutLocationFence,
////                                "Oops, your headphone status is unknown!",
////                                Snackbar.LENGTH_LONG).show();
//                        break;
//                }
//            } else if (TextUtils.equals(fenceState.getFenceKey(), EXITING_LOCATION_FENCE_KEY)) {
//                switch (fenceState.getCurrentState()) {
//                    case FenceState.TRUE:
//                        //setHeadphoneState(STATUS_EXITING);
//                        break;
//                    case FenceState.FALSE:
//
//                        break;
//                    case FenceState.UNKNOWN:
////                        Snackbar.make(mLayoutLocationFence,
////                                "Oops, your headphone status is unknown!",
////                                Snackbar.LENGTH_LONG).show();
//                        break;
//                }
//            } else if (TextUtils.equals(fenceState.getFenceKey(), ENTERING_LOCATION_FENCE_KEY)) {
//                switch (fenceState.getCurrentState()) {
//                    case FenceState.TRUE:
//                        //setHeadphoneState(STATUS_ENTERING);
//                        break;
//                    case FenceState.FALSE:
//
//                        break;
//                    case FenceState.UNKNOWN:
////                        Snackbar.make(mLayoutLocationFence,
////                                "Oops, your headphone status is unknown!",
////                                Snackbar.LENGTH_LONG).show();
//                        break;
//                }
//            }
//        }
//    }
//
//    public static class getData extends AsyncTask<String, Void, String> {
//        String name;
//
//        @Override
//        protected String doInBackground(String... params) {
//            result = "";
//            isr = null;
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost("http://13.124.11.28/temp/GetData.php"); //YOUR PHP SCRIPT ADDRESS
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
//
//            try {
//
//                JSONArray jArray = new JSONArray(result);
//
//                for (int i = 0; i < jArray.length(); i++) {
//                    JSONObject json = jArray.getJSONObject(i);
//
//
//                    latitude = json.getString("latitude");
//                    longitude = json.getString("longitude");
//                    //longitude = longitude +"\n"+  json.getString("longitude");
//
//                    newLatitude = Double.parseDouble(latitude);
//                    newLongitude = Double.parseDouble(longitude);
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
//
//            newQLatitude = 37.529648;
//            newQLongitude = 126.722083;
//
//            //tv.setText(""+ newLatitude + "," + newLongitude);
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
//}
