package com.example.teodora.employeetaskmanager.Activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.teodora.employeetaskmanager.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = LocationActivity.class.getSimpleName();
    private LatLng latLng;
    private String name;
    private String address;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Choose location");
        setSupportActionBar(toolbar);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        /*
        * The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
        * set a filter returning only results with a precise address.
        */
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(41.608460, 21.738529),
                new LatLng(41.609460, 21.838529)));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName());//get place details here
                latLng = place.getLatLng();
                address = place.getAddress().toString();
                name = place.getName().toString();
                initMap();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);


    }

    public void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title(name));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 12.0f));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent (LocationActivity.this, AssignTaskActivity.class);
        intent.putExtra("address", address);
        setResult(Activity.RESULT_OK,intent);
        finish();

        return super.onOptionsItemSelected(item);

    }
}




