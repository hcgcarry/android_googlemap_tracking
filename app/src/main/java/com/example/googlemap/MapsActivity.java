package com.example.googlemap;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.util.Log;

        import androidx.fragment.app.FragmentActivity;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.CameraPosition;
        import com.google.android.gms.maps.model.CircleOptions;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.Polyline;
        import com.google.android.gms.maps.model.PolylineOptions;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private String TAG="MapsActivityClass";

    private GoogleMap mMap;
    LatLng previousLatLng;
    LatLng currentLatLng;
    private Polyline polyline1;
    private List<LatLng> polylinePoints = new ArrayList<>();
    private Marker mCurrLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //firebaseInsert firebase= new firebaseInsert();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker  and move the camera
        polyline1 = mMap.addPolyline(new PolylineOptions().addAll(polylinePoints));
        fetchLocationUpdates();
    }

    private void fetchLocationUpdates() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Location").child("1");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i(TAG, "New location updated:" + dataSnapshot.getKey());
                updateMap(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateMap(DataSnapshot dataSnapshot) {
        double longitude= 22.990825,latitude = 120.219413;

        /*
        Iterable<DataSnapshot> data = dataSnapshot.getChildren();
        for(DataSnapshot d: data){
            if(d.getKey().equals("latitude")){
                Log.d(TAG,"getlatitude");
                latitude =  Double.toString(d.getValue()) ;
            }else if(d.getKey().equals("longitude")){
                Log.d(TAG,"getlongitude");
                longitude = (Double) d.getValue();
            }
        }

         */
        Location location = dataSnapshot.getValue(Location.class);
        latitude = Double.parseDouble(location.latitude);
        longitude = Double.parseDouble( location.longitude);



        currentLatLng = new LatLng(latitude, longitude);
        Log.d(TAG,Double.toString(latitude));
        Log.d(TAG,Double.toString(longitude));

        if(previousLatLng ==null || previousLatLng != currentLatLng){
            // add marker line
            if(mMap!=null) {
                /*
                previousLatLng  = currentLatLng;
                polylinePoints.add(currentLatLng);
                polyline1.setPoints(polylinePoints);
                Log.w("tag", "Key:" + currentLatLng);
                if(mCurrLocationMarker!=null){
                    mCurrLocationMarker.setPosition(currentLatLng);
                }else{
                    mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentLatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_foreground))
                            .title("Delivery"));
                }
                */
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20));
                //addCircle(currentLatLng);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
                mMap.addMarker(new MarkerOptions().position(currentLatLng).title("CurrentPosition"));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(20));     // 放大地圖到 16 倍大
            }

        }
    }
    private void addCircle(LatLng lat){
        LatLng ny = lat;
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(ny);
        circleOptions.radius(1);
        circleOptions.fillColor(Color.BLUE);
        circleOptions.strokeColor(Color.RED);
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }
}

