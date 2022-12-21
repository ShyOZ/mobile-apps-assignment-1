package com.mobileapps.assignments.presentation.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobileapps.assignments.R;
import com.mobileapps.assignments.data.Score;
import com.paz.prefy_lib.Prefy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapsFragment extends Fragment {
    private GoogleMap googleMap;
    private List<Marker> markers = new ArrayList<>();

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        markers = Prefy.getInstance().getArrayList(view.getContext().getString(R.string.scores_key),
                        new ArrayList<Score>()).stream()
                .map(score -> new MarkerOptions()
                        .position(score.getLocation())
                        .title(score.getPlayerName() + " - " + score.getScore()))
                .map(googleMap::addMarker).collect(Collectors.toList());

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void focusOnLocation(Score score) {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(score.getLocation(), 15));
            });
        }
    }
}