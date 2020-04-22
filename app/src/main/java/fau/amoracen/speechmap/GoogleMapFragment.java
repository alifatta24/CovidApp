package fau.amoracen.speechmap;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final int REQ_CODE = 100;
    private TextToSpeech textToSpeech;
    private GPSTracker gps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        ImageView speakImageView = view.findViewById(R.id.speakImageView);
        speakImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getContext(), "Sorry your device is not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                assert result != null;
                if (result.get(0).toLowerCase().contains("location")) {
                    speak(result.get(0));
                    Toast.makeText(getContext(), result.get(0), Toast.LENGTH_LONG).show();
                }
                //Get the location using the gps tracker service
                getLocation();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng FAU = new LatLng(26.3700, -80.1013);
        //Configure Map
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        // Add a marker in FAU and move the camera
        mMap.addMarker(new MarkerOptions().position(FAU).title("Florida Atlantic University"));
        moveCamera(FAU, 15);
    }

    /**
     * Move the camera to a new location
     *
     * @param location a location object
     * @param zoom     an integer representing the map's zoom
     */
    private void moveCamera(LatLng location, int zoom) {
        CameraPosition.Builder camBuilder = CameraPosition.builder();
        camBuilder.bearing(0);
        camBuilder.tilt(30);
        camBuilder.target(location);
        camBuilder.zoom(zoom);
        CameraPosition cp = camBuilder.build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

    /**
     * Get the Location
     */
    private void getLocation() {
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
                        updateMap(latitude, longitude);
                    }
                }.start();

            } else {
                String error = "Can't get location";
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
                speak(error);
            }
        } else {
            gps.requestPermission();
        }
    }

    /**
     * Speak the message
     *
     * @param msg an string
     */
    private void speak(String msg) {
        if (textToSpeech != null) {
            textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    /**
     * Update the map to a new location
     *
     * @param latitude
     * @param longitude
     */
    private void updateMap(double latitude, double longitude) {
        LatLng newLocation = new LatLng(latitude, longitude);
        //Find information about the location
        Geocoder geocoder = new Geocoder(Objects.requireNonNull(getActivity()).getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocation(latitude, longitude, 1);
            if (listAddress != null && listAddress.size() > 0) {
                String currentAddress = listAddress.get(0).getAddressLine(0);
                speak("Address: " + currentAddress);
                //Add marker to Map
                mMap.addMarker(new MarkerOptions().position(newLocation)
                        .title("Longitude: " + longitude + " Latitude: " + latitude)
                        .snippet("Address: " + currentAddress));
            } else {
                speak("Did not find an address");
                //Add marker to Map
                mMap.addMarker(new MarkerOptions().position(newLocation)
                        .title("Longitude: " + longitude + " Latitude: " + latitude));
            }
            moveCamera(newLocation, 18);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
