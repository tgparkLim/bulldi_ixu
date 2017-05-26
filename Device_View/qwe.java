package openstack.bulldi.safe3x.Device_View;

/**
 * Created by park on 2017-03-12.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import openstack.bulldi.safe3x.R;

/**
 * GPS 샘플
 */
public class qwe extends Activity {

    // GPSTracker class
    public Gpsinfo gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GPS 정보를 보여주기 위한 이벤트 클래스 등록

        gps = new Gpsinfo(qwe.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {

           final double latitude = gps.getLatitude();
           final double longitude = gps.getLongitude();

            Intent intent = new Intent(getApplicationContext(), SensorTagIRTemperatureProfile.class);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            //startActivity(intent);

        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

    }
}