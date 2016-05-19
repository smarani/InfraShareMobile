package stephmarani.infragram;

import android.content.Intent;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private String latitude = "";
    private String longitude = "";
    private String filePathOriginal = "";
    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        byteArray = getIntent().getByteArrayExtra("OriginalImage");
        filePathOriginal = getIntent().getExtras().getString("FilePath");


        Button buttonContinue = (Button) findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                intent.putExtra("OriginalImage", byteArray);
                intent.putExtra("FilePath", filePathOriginal);
                startActivity(intent);

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        getOriginalCoord();
        Log.d("Infragram", "latitude is:" + latitude);
        LatLng sydney = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        mMap.setOnMarkerDragListener(this);
    }

    private void getOriginalCoord() {
        try {
            ExifInterface ef = new ExifInterface(filePathOriginal);

            latitude = ef.getAttribute(ef.TAG_GPS_LATITUDE);
            longitude = ef.getAttribute(ef.TAG_GPS_LONGITUDE);

            if (latitude == null | longitude == null) {
                GPS newGPS = GPS.sharedInstance(this);
                if (newGPS.canGetLocation()) {
                    Location l = newGPS.getLastKnownLocation();
                    latitude = Double.toString(l.getLatitude());
                    longitude = Double.toString(l.getLongitude());
                    Log.d("Infragram", "got a location:" + l.getLatitude());
                }
            }

        } catch (Exception e) {
            latitude = "0.0";
            longitude = "0.0";
        }

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position=marker.getPosition();
        latitude = Double.toString(position.latitude);
        longitude = Double.toString(position.longitude);

        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                position.latitude,
                position.longitude));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position=marker.getPosition();
        latitude = Double.toString(position.latitude);
        longitude = Double.toString(position.longitude);

        Log.d(getClass().getSimpleName(),
                String.format("Dragging to %f:%f", position.latitude,
                        position.longitude));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position=marker.getPosition();
        latitude = Double.toString(position.latitude);
        longitude = Double.toString(position.longitude);

        Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
                position.latitude,
                position.longitude));
    }

}
