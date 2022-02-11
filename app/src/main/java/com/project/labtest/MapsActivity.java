package com.project.labtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.mMenuImg)
    ImageView mMenuImg;

    @BindView(R.id.nav_view)
    NavigationView mNavView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private GoogleMap mMap;
    double lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        mNavView.setNavigationItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lat = bundle.getDouble("Lat");
            lng = bundle.getDouble("Lng");
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMenuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            recreate();
        } else if (id == R.id.nav_show) {
            Intent intent = new Intent(MapsActivity.this, ProductsActivty.class);
            startActivity(intent);
        } else if (id == R.id.nav_add) {
            Intent intent = new Intent(MapsActivity.this, AddProductActivity.class);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }
}