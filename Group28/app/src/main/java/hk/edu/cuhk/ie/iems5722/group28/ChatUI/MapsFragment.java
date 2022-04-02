package hk.edu.cuhk.ie.iems5722.group28.ChatUI;

import static android.content.Context.LOCATION_SERVICE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hk.edu.cuhk.ie.iems5722.group28.R;

public class MapsFragment extends Fragment implements LocationListener,OnMapReadyCallback {
    private GoogleMap mMap;
    Marker myMarker, hisMarker;
    private DatabaseReference myRef, hisRef;
    private LocationManager manager;
    private final int MIN_TIME = 1000;
    private final int MIN_DISTANCE = 1;


//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//        /**
//         * Manipulates the map once available.
//         * This callback is triggered when the map is ready to be used.
//         * This is where we can add markers or lines, add listeners or move the camera.
//         * In this case, we just add a marker near Sydney, Australia.
//         * If Google Play services is not installed on the device, the user will be prompted to
//         * install it inside the SupportMapFragment. This method will only be triggered once the
//         * user has installed Google Play services and returned to the app.
//         */
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//
//        }
//    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://chatapp-1155162635-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("Order") ;
        hisRef = database.getReference("Deliver") ;
        manager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        getLocationUpdates();
        readChanges();
        return inflater.inflate(R.layout.fragment_maps, container, false);

    }

    private void getLocationUpdates() {
        if(manager!=null){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,this);
                }else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME,MIN_DISTANCE,this);
                }else{
                    Toast.makeText(getContext().getApplicationContext(), "No Location Provider", Toast.LENGTH_SHORT).show();
                }
            }else{
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
            }
        }
    }

    private void readChanges() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    MyLocation myLocation  = snapshot.getValue(MyLocation.class);
                    if (myLocation!=null){
                        myMarker.setPosition(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()));
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        hisRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    MyLocation myLocation  = snapshot.getValue(MyLocation.class);
                    if (myLocation!=null){
                        hisMarker.setPosition(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()));
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location!=null){
            saveLocation(location);
        }else{
            Toast.makeText(getContext().getApplicationContext(), "no location", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLocation(Location location) {
        myRef.setValue(location);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng myInitLoc = new LatLng(22.41966381477976, 114.20885962952606);
        LatLng hisInitLoc = new LatLng(22.421488696187197, 114.20617742066102);
        myMarker = mMap.addMarker(new MarkerOptions().position(myInitLoc).title("My Location"));
        hisMarker = mMap.addMarker(new MarkerOptions().position(hisInitLoc).title("His Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myInitLoc));
        mMap.setMinZoomPreference(15);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

    }
}