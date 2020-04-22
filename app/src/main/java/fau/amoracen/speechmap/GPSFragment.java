package fau.amoracen.speechmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class GPSFragment extends Fragment {
    private TextView locationTextView;
    private static final int REQUEST_CODE_PERMISSION = 2;
    // GPSTracker class
    private GPSTracker gps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gps_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), mPermission) != PackageManager.PERMISSION_GRANTED) {
                //ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationTextView = view.findViewById(R.id.locationTextView);
        Button locationButton = view.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create class object
                gps = new GPSTracker(getContext(), getActivity());
                // check if GPS enabled
                if (gps.checkPermission()) {
                    if (gps.canGetLocation()) {
                        //Wait 1 seconds for TextToSpeech
                        new CountDownTimer(1500, 1000) {
                            public void onTick(long millisecondsUntilDone) {
                                //countdown every second
                            }

                            public void onFinish() {
                                //Counter is finished(after 1 seconds)
                                Log.i("Done ", "CountDown Finished");
                                double latitude = gps.getLatitude();
                                double longitude = gps.getLongitude();
                                String locationResult = "Location is: \nLatitude: " + latitude + "\nLongitude: " + longitude;
                                locationTextView.setText(locationResult);
                            }
                        }.start();

                    } else {
                        String error = "Can't get location";
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                        locationTextView.setText(error);
                    }
                } else {
                    gps.requestPermission();
                }
            }
        });
    }
}
