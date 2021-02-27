package com.codefestfinal.codefest21;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codefestfinal.codefest21.directionsLib.FetchURL;
import com.codefestfinal.codefest21.directionsLib.GPSHelper;
import com.codefestfinal.codefest21.directionsLib.mapDistanceObj;
import com.codefestfinal.codefest21.directionsLib.mapTimeObj;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class newsMap extends Fragment {

    private static final int LOCATION_PERMISSION = 100;
    public Marker ridermaeker;
    public GoogleMap currentgooglemap;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap currengoogleMap;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(newsMap.super.getContext());
            currengoogleMap = googleMap;
            UpdateCustomerLocation();

            //  LatLng sydney = new LatLng(-34, 151);
            //  googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //  googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        }


    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==LOCATION_PERMISSION){
            if(permissions.length>0){
                UpdateCustomerLocation();
            }

        }
    }

    private void UpdateCustomerLocation() {
        if (ActivityCompat.checkSelfPermission(newsMap.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(newsMap.super.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION);

            return;
        }


        Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
        System.out.println(lastLocation);
        lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
            LatLng customertlocation;
            LatLng dragtlocation;
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    customertlocation = new LatLng(location.getLatitude(),location.getLongitude());

                    Toast.makeText(newsMap.super.getContext(),"location>>"+location.getLatitude()+" "+location.getLongitude(),Toast.LENGTH_SHORT).show();

                    BitmapDescriptor bitcurrent =  getBitmapDesc(getContext(),R.drawable.ic_location);
                    BitmapDescriptor bitmdrag =  getBitmapDesc(getContext(),R.drawable.ic_walk);

                    MarkerOptions markerOptions = new MarkerOptions().icon(bitcurrent).draggable(false).position(customertlocation).title(" Im here");
                    MarkerOptions markerOptions1 = new MarkerOptions().icon(bitmdrag).draggable(true).position(customertlocation).title(" I Want to go");


                    currengoogleMap.addMarker(markerOptions);
                    currengoogleMap.addMarker(markerOptions1);
                    currengoogleMap.moveCamera(CameraUpdateFactory.newLatLng(customertlocation));
                    currengoogleMap.moveCamera(CameraUpdateFactory.zoomTo(19));

                    currengoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {
                            Log.d("TAG","START");
                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {
                            Log.d("TAG","DRAG");
                        }
                        Polyline polyline;
                        @Override
                        public void onMarkerDragEnd(Marker marker) {
                            Log.d("TAG","END");
                            dragtlocation = marker.getPosition();
                            // currengoogleMap.addPolygon(new PolygonOptions().add(customertlocation,dragtlocation));
                          //  ((Home)getActivity()).setLatlang(customertlocation,dragtlocation);

                            new FetchURL() {
                                @Override
                                public void onTaskDone(Object... values) {
                                    if(polyline!=null){
                                        polyline.remove();
                                    }
                                    currengoogleMap.addPolyline((PolylineOptions) values[0]);
                                }

                                @Override
                                public void onDistanceTaskDone(mapDistanceObj distance) {
                                    Toast.makeText(getActivity(), distance.getDistanceText(), Toast.LENGTH_SHORT).show();
                                    double startPrice = 50;
                                    double aditionalPricePerKm = 40;
//                                    ena distance eken 1km arala ithuru tika gannva
                                    double adtionalm = distance.getDistanceValM()-1000;
//                                    A ena Meter gaana Km krnva
                                    double adtionalPrice = ((int)(adtionalm/1000)) * aditionalPricePerKm;
                                    double estimatedPrice = startPrice + adtionalPrice;
                                    Toast.makeText(getActivity(), "Your Estimated Price is: "+estimatedPrice,Toast.LENGTH_SHORT).show();
//                                    ((Home)getActivity()).setestimatevalue(estimatedPrice);
                                }

                                @Override
                                public void onTimeTaskDone(mapTimeObj time) {
//                                    ((Home)getActivity()).seteduration(time.getTimeInText());

                                }


                            }.execute(getUrl(customertlocation,dragtlocation,"driving"),"driving");

                        }
                    });
                }else{
                    Toast.makeText(newsMap.super.getContext(),"location Not Found>>",Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(newsMap.super.getContext(),"location Not Foundddddddddd>>"+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private BitmapDescriptor getBitmapDesc(Context context  , int ic_baseline_add_location_24) {
        Drawable LAYER_1 = ContextCompat.getDrawable(context,ic_baseline_add_location_24);
        LAYER_1.setBounds(0, 0, LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        LAYER_1.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        GPSHelper gpsHelper = new GPSHelper(this);
        gpsHelper.getCurrentLocationListner(getActivity());

    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        Log.d("TAG","URL:"+url);
        return url;
    }


    public void showRiderLocation(double currentrider_lat, double currentrider_lon) {
        if(currengoogleMap!=null){
            if(ridermaeker==null){
                ridermaeker=currengoogleMap.addMarker(new MarkerOptions().position(new LatLng(currentrider_lat,currentrider_lon)).icon(getBitmapDesc(getActivity(),R.drawable.ic_motorbike)).title("Roider"));

            }
            ridermaeker.setPosition(new LatLng(currentrider_lat,currentrider_lon));
        }else{
            Log.d("MAP FRAGMNET","MAP NOT READY SKIPPPPPPPP");

        }
    }

//    public DocumentReference getcurrentjobfromhomeactivity(){
//        return ((Home)getActivity()).getCurrentjobcollection();
//    }
}