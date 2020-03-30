package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.OutletInfoListBaseResBean;
import mm.com.aeon.vcsaeon.beans.OutletListInfoResBean;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_MULTIPLE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_M_LOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_NON_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;

public class OutletLocationTabFragment extends BaseFragment implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    View view;
    View serviceUnavailable;
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    private List<OutletListInfoResBean> outletListInfoResBeanList;
    private OutletInfoListBaseResBean outletInfoListBaseResBean;
    double outletLimitMeter;

    public static LatLng latLng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.outlet_location, container, false);
        serviceUnavailable = view.findViewById(R.id.loc_service_unavailable);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.outlet_loc_map);
        mapFragment.getMapAsync(this);

        getOutletListData();

        return view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        if(outletInfoListBaseResBean != null){
            setMarkersOnGoogleMap(outletInfoListBaseResBean.getOutletInfoList());
        } else {

            Service service = APIClient.getUserService();
            Call<BaseResponse<OutletInfoListBaseResBean>> req = service.getOutletInfoList();

            getActivity().setTheme(R.style.MessageDialogTheme);
            final ProgressDialog getOutletDialog = new ProgressDialog(getActivity());
            getOutletDialog.setMessage(getString(R.string.progress_loading_outlet));
            getOutletDialog.setCancelable(false);
            getOutletDialog.show();

            req.enqueue(new Callback<BaseResponse<OutletInfoListBaseResBean>>() {
                @Override
                public void onResponse(Call<BaseResponse<OutletInfoListBaseResBean>> call, Response<BaseResponse<OutletInfoListBaseResBean>> response) {
                    if(response.isSuccessful()){
                        BaseResponse baseResponse = response.body();
                        if(baseResponse.getStatus().equals(SUCCESS)){
                            closeDialog(getOutletDialog);
                            outletInfoListBaseResBean = (OutletInfoListBaseResBean) baseResponse.getData();
                            outletListInfoResBeanList = outletInfoListBaseResBean.getOutletInfoList();
                            outletLimitMeter = outletInfoListBaseResBean.getOutletLimitMetre();

                            if(outletLimitMeter == 0.0){
                                serviceUnavailable.setVisibility(View.VISIBLE);
                            }

                            if(outletListInfoResBeanList != null){
                                setMarkersOnGoogleMap(outletListInfoResBeanList);
                            } else {
                                serviceUnavailable.setVisibility(View.VISIBLE);
                            }

                        } else {
                            closeDialog(getOutletDialog);
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }
                    } else {
                        closeDialog(getOutletDialog);
                        serviceUnavailable.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<OutletInfoListBaseResBean>> call, Throwable t) {
                    closeDialog(getOutletDialog);
                    serviceUnavailable.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        //Default Zoom
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(16.8005901,96.148891)).zoom(12).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private String getMapUri(LatLng curPosition, LatLng destPosition){
        double curLat = curPosition.latitude;
        double curLng = curPosition.longitude;
        double destLat = destPosition.latitude;
        double destLng = destPosition.longitude;
        String url = "http://maps.google.com/maps?saddr="+curLat+","+curLng+"&daddr="+destLat+","+destLng;
        return url;
    }

    private Bitmap getAeonMarker(){
        int height = 50;
        int width = 50;
        BitmapDrawable bitmapDrawable=(BitmapDrawable)getResources().getDrawable(R.drawable.aeon_logo_white);
        Bitmap b=bitmapDrawable.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }

    private void getOutletListData(){

        Service service = APIClient.getUserService();
        Call<BaseResponse<OutletInfoListBaseResBean>> req = service.getOutletInfoList();

        getActivity().setTheme(R.style.MessageDialogTheme);
        final ProgressDialog getOutletDialog = new ProgressDialog(getActivity());
        getOutletDialog.setMessage(getString(R.string.progress_loading_outlet));
        getOutletDialog.setCancelable(false);
        getOutletDialog.show();

        req.enqueue(new Callback<BaseResponse<OutletInfoListBaseResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<OutletInfoListBaseResBean>> call, Response<BaseResponse<OutletInfoListBaseResBean>> response) {
                if(response.isSuccessful()){
                    BaseResponse baseResponse = response.body();
                    if(baseResponse.getStatus().equals(SUCCESS)){
                        closeDialog(getOutletDialog);
                        outletInfoListBaseResBean = (OutletInfoListBaseResBean) baseResponse.getData();
                        outletListInfoResBeanList = outletInfoListBaseResBean.getOutletInfoList();
                        Log.e("outlet list", outletListInfoResBeanList.size()+"");
                        outletLimitMeter = outletInfoListBaseResBean.getOutletLimitMetre();
                        Log.e("outlet list", outletLimitMeter+"");
                        if(outletLimitMeter == 0.0){
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }
                    } else {
                        closeDialog(getOutletDialog);
                        serviceUnavailable.setVisibility(View.VISIBLE);
                    }
                } else {
                    closeDialog(getOutletDialog);
                    serviceUnavailable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<OutletInfoListBaseResBean>> call, Throwable t) {
                closeDialog(getOutletDialog);
                serviceUnavailable.setVisibility(View.VISIBLE);
            }
        });
    }


    //set outlet markers on google map.
    private void setMarkersOnGoogleMap(List<OutletListInfoResBean> outletInfoList){

        if(outletInfoList != null){

            for (OutletListInfoResBean outletInfo : outletInfoList) {
                LatLng position = new LatLng(outletInfo.getLatitude(),outletInfo.getLongitude());
                MarkerOptions markerOpts = new MarkerOptions();
                markerOpts.position(position);
                markerOpts.title(outletInfo.getOutletName());
                markerOpts.snippet(outletInfo.getOutletAddress());

                switch (outletInfo.getRoleId()){
                    case ROLE_MOBILE : markerOpts.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));break;
                    case ROLE_NON_MOBILE : markerOpts.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));break;
                    case ROLE_M_LOAN : markerOpts.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));break;
                    case ROLE_MULTIPLE : markerOpts.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));break;
                    default: continue;
                }

                if(outletInfo.isAeon()){
                    markerOpts.icon(BitmapDescriptorFactory.fromBitmap(getAeonMarker()));
                }

                mMap.addMarker(markerOpts);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            }

            //Open Google Map and draw route.
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(getMapUri(latLng, marker.getPosition())));
                    if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                        startActivity(intent);
                    } else {
                        //Toast.makeText(getActivity(), getString(R.string.message_gmap_not_support), Toast.LENGTH_SHORT).show();
                        displayMessage(getActivity(), getString(R.string.message_gmap_not_support));
                    }
                    return false;
                }
            });

        } else {
            serviceUnavailable.setVisibility(View.VISIBLE);
        }

        //move map camera
        mMap.getUiSettings().setMyLocationButtonEnabled(false); //hide location button.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
}
