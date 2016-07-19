package in.yousee.theadmin;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;
import java.util.Map;

import in.yousee.theadmin.util.LogUtil;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationFragment extends DialogFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView testText;
    private GoogleMap mMap;
    private MapView mapView;


    private OnFragmentInteractionListener mListener;

    public LocationFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();

        GoogleMapOptions mapOptions = new GoogleMapOptions();
        mapOptions.zOrderOnTop(true);
        MapFragment mapFragment = MapFragment.newInstance(mapOptions);

        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        //fragment.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHI‌​ND);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        createPlayServiceInstance();
        //getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_Dialog);
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                LogUtil.print("location changed :"+location.getLatitude() +" "+ location.getLongitude());

                CameraPosition.Builder cameraPosition = new CameraPosition.Builder(mMap.getCameraPosition());
                cameraPosition.target(latLng);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition.build());
                mMap.animateCamera(cameraUpdate);
                checkAndChangeColor(latLng);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        testText = (TextView) view.findViewById(R.id.testText);

        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onStart() {

        mGoogleApiClient.connect();
        super.onStart();
        //mapView.onStart
    }

    @Override
    public void onStop() {

        mGoogleApiClient.disconnect();
        super.onStop();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        GoogleMapOptions mapOptions = new GoogleMapOptions();
        mapOptions.zOrderOnTop(true);

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogUtil.print("no permission");
        }
        else {

            mMap.setMyLocationEnabled(true);
            addPolyAreaOnMap();
        }

        LogUtil.print("adding markers");
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target()      // Sets the center of the map to Mountain View
//                .zoom(5)                   // Sets the zoom
//                //.bearing(90)                // Sets the orientation of the camera to east
//                //.tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                .build();                   // Creates a CameraPosition from the builder
//        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    @Override
    public void onLocationChanged(Location location) {
        LogUtil.print("location changed");

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        checkAndChangeColor(latLng);
        //locationManager.removeUpdates(this);
    }


    private GoogleApiClient mGoogleApiClient;

    public void createPlayServiceInstance() {

        //mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LogUtil.print("onconnected()");
        boolean fineLocation = (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED);
        boolean coarseLocation = (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);

        LogUtil.print("fine location = " + fineLocation);
        LogUtil.print("coarselocation = " + coarseLocation);


        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        else {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            LogUtil.print("got location");
            if (mLastLocation != null) {

                LogUtil.print(String.valueOf(mLastLocation.getLatitude()));
                LogUtil.print(String.valueOf(mLastLocation.getLongitude()));
                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                mMap.animateCamera(cameraUpdate);
                checkAndChangeColor(latLng);


            }
        }
    }

    private void checkAndChangeColor(LatLng latLng)
    {
        boolean present = pointInPolygon(latLng, polygon);

        if(present == true)
        {
            polygon.setStrokeColor(Color.argb(128, 50, 255, 50));
            polygon.setFillColor(Color.argb(128, 0, 255, 0));
        }
        else
        {
            polygon.setStrokeColor(Color.argb(128, 255, 50, 50));
            polygon.setFillColor(Color.argb(128, 255, 0, 0));
        }
        testText.setText(""+present);

    }

    Polygon polygon;
    public void addPolyAreaOnMap()
    {
        // Instantiates a new Polygon object and adds points to define a rectangle
//        PolygonOptions rectOptions = new PolygonOptions()
//                .add(new LatLng(17.422535, 78.501770),
//                        new LatLng(17.424829, 78.502028),
//                        new LatLng(17.424727, 78.503605),
//                        new LatLng(17.426063, 78.504042),
//                        new LatLng(17.423995, 78.505869),
//                        new LatLng(17.422245, 78.504238)
//                )
//                .strokeColor(Color.RED)
//                .fillColor(Color.argb(128, 255, 0, 0))
//                ;
//        PolygonOptions rectOptions = new PolygonOptions()
//                .add(new LatLng(17.319037, 78.527645),
//                        new LatLng(17.319301, 78.527712),
//                        new LatLng(17.319268, 78.527892),
//                        new LatLng(17.319002, 78.527876)
//                        )
//                .strokeColor(Color.RED)
//                .fillColor(Color.argb(128, 255, 0, 0))
//                ;

        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(17.426084, 78.453917),
                        new LatLng(17.426178, 78.454073),
                        new LatLng(17.425977, 78.454150),
                        new LatLng(17.425905, 78.453998)
                )
                .strokeColor(Color.RED)
                .fillColor(Color.argb(128, 255, 0, 0))
                ;


        // Get back the mutable Polygon
        polygon = mMap.addPolygon(rectOptions);




    }


    public boolean pointInPolygon(LatLng point, Polygon polygon) {
        // ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
        int crossings = 0;
        List<LatLng> path = polygon.getPoints();
        path.remove(path.size()-1); //remove the last point that is added automatically by getPoints()

        // for each edge
        for (int i=0; i < path.size(); i++) {
            LatLng a = path.get(i);
            int j = i + 1;
            //to close the last edge, you have to take the first point of your polygon
            if (j >= path.size()) {
                j = 0;
            }
            LatLng b = path.get(j);
            if (rayCrossesSegment(point, a, b)) {
                crossings++;
            }
        }

        // odd number of crossings?
        return (crossings % 2 == 1);
    }

    public boolean rayCrossesSegment(LatLng point, LatLng a,LatLng b) {
        // Ray Casting algorithm checks, for each segment, if the point is 1) to the left of the segment and 2) not above nor below the segment. If these two conditions are met, it returns true
        double px = point.longitude,
                py = point.latitude,
                ax = a.longitude,
                ay = a.latitude,
                bx = b.longitude,
                by = b.latitude;
        if (ay > by) {
            ax = b.longitude;
            ay = b.latitude;
            bx = a.longitude;
            by = a.latitude;
        }
        // alter longitude to cater for 180 degree crossings
        if (px < 0 || ax <0 || bx <0) { px += 360; ax+=360; bx+=360; }
        // if the point has the same latitude as a or b, increase slightly py
        if (py == ay || py == by) py += 0.00000001;


        // if the point is above, below or to the right of the segment, it returns false
        if ((py > by || py < ay) || (px > Math.max(ax, bx))){
            return false;
        }
        // if the point is not above, below or to the right and is to the left, return true
        else if (px < Math.min(ax, bx)){
            return true;
        }
        // if the two above conditions are not met, you have to compare the slope of segment [a,b] (the red one here) and segment [a,p] (the blue one here) to see if your point is to the left of segment [a,b] or not
        else {
            double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Double.POSITIVE_INFINITY;
            double blue = (ax != px) ? ((py - ay) / (px - ax)) : Double.POSITIVE_INFINITY;
            return (blue >= red);
        }

    }


    public boolean isCurrentLocationNearBy(LatLng currentLocation)
    {
        float radius = 10;

        return false;
    }


    private static final int REQUEST_ACCESS_LOCATION = 0;

    public boolean requestLocationPermission()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(this.getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(),ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else
        {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_LOCATION);
        }
        return false;
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogUtil.print("Permission granted");
                onConnected(null);

            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}