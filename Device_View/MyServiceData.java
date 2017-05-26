//package openstack.bulldi.safe3x.Device_View;
//
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Handler;
//import android.os.IBinder;
//import android.widget.Toast;
//
//import com.google.android.gms.common.api.GoogleApiClient;
//
//import openstack.bulldi.safe3x.MainActivity;
//
//public class MyServiceData extends Service {
//
//    ServiceThread serviceThread;
//
//    private GoogleApiClient mGoogleApiClient;
//    private PendingIntent mPendingIntent;
//    private LocationFenceActivity.LocationFenceReceiver mLocationFenceReceiver;
//
//    public int count = 0;
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "background Prosess started", Toast.LENGTH_LONG).show();
//
//        for(int i = 1; i > 0; i++) {
//            count++;
//        }
//
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        Toast.makeText(this, "background Prosess stopped", Toast.LENGTH_LONG).show();
//        super.onDestroy();
//    }
//
//}
