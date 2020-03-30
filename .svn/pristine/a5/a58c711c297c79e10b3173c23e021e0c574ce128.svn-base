package mm.com.aeon.vcsaeon.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.OutletInfoListBaseResBean;
import mm.com.aeon.vcsaeon.beans.OutletListInfoResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.adapters.OutletListAdapter;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;

public class OutletListTabFragment extends BaseFragment implements LocationListener {

    View view;
    View serviceUnavailable;

    private RecyclerView outletInfoRecyclerView;
    private OutletListAdapter outletListAdapter;

    List<OutletListInfoResBean> outletListInfoResBeanList;
    private OutletInfoListBaseResBean outletInfoListBaseResBean;
    private double outletLimitMeter;

    private LocationManager mLocationManager;

    private static Location mCurLocation = null;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog getOutletDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.outlet_list, container, false);
        serviceUnavailable = view.findViewById(R.id.outlet_unavailable);
        outletListInfoResBeanList = new ArrayList<>();

        getUpdatedCurLocation();

        Service service = APIClient.getUserService();
        Call<BaseResponse<OutletInfoListBaseResBean>> req = service.getOutletInfoList();

        getActivity().setTheme(R.style.MessageDialogTheme);
        getOutletDialog = new ProgressDialog(getActivity());
        getOutletDialog.setMessage("Loading Outlets ...");
        getOutletDialog.setCancelable(false);
        getOutletDialog.show();

        req.enqueue(new Callback<BaseResponse<OutletInfoListBaseResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<OutletInfoListBaseResBean>> call, Response<BaseResponse<OutletInfoListBaseResBean>> response) {

                if (response.isSuccessful()) {

                    BaseResponse baseResponse = response.body();

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        closeDialog(getOutletDialog);

                        outletInfoListBaseResBean = (OutletInfoListBaseResBean) baseResponse.getData();
                        outletListInfoResBeanList = outletInfoListBaseResBean.getOutletInfoList();
                        outletLimitMeter = outletInfoListBaseResBean.getOutletLimitMetre();

                        if (outletLimitMeter == 0.0) {
                            //service unavailable
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }

                        if (outletListInfoResBeanList == null) {
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }

                        //pull refresh
                        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
                        swipeRefreshLayout.setColorSchemeColors(getActivity().getColor(R.color.colorPrimary),
                                getActivity().getColor(R.color.orange), getActivity().getColor(R.color.newsheader));

                        swipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                                getUpdatedCurLocation();
                            }
                        });

                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                getUpdatedCurLocation();
                            }
                        });


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

        return view;
    }


    @Override
    public void onLocationChanged(Location location) {
        mCurLocation = location;
        loadDataToRecyclerView();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public List<OutletListInfoResBean> calculateAndSortDistance(List<OutletListInfoResBean> outletInfoList, double outletLimitMetre, LatLng curPosition) {
        List<OutletListInfoResBean> outletInfoTempList = new ArrayList<>();
        //add distance
        for (OutletListInfoResBean outletInfo : outletInfoList) {
            LatLng latLng = new LatLng(outletInfo.getLatitude(), outletInfo.getLongitude());
            double tempDistance = posDistanceInMeter(curPosition, latLng);
            if(outletLimitMetre >= tempDistance){
                outletInfo.setDistanceInMeter(tempDistance);
                outletInfoTempList.add(outletInfo);
            }
        }
        //sort by distances
        Collections.sort(outletInfoTempList,new SortByDistance());
        return outletInfoTempList;

    }

    private double posDistanceInMeter(LatLng curPosition, LatLng destPosition) {
        final double R = 6372.8; //In kilometers
        double lat1 = curPosition.latitude;
        double lon1 = curPosition.longitude;
        double lat2 = destPosition.latitude;
        double lon2 = destPosition.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double distance = R * c * 1000;
        return distance;
    }

    class SortByDistance implements Comparator<OutletListInfoResBean>{
        @Override
        public int compare(OutletListInfoResBean o1, OutletListInfoResBean o2) {
            return (int) (o1.getDistanceInMeter() - o2.getDistanceInMeter());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getUpdatedCurLocation();
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    private void loadDataToRecyclerView(){
        if (mCurLocation != null) {
            if(outletListInfoResBeanList!=null){
                List<OutletListInfoResBean> outletInfoTempList =
                        calculateAndSortDistance(outletListInfoResBeanList,outletLimitMeter,
                                new LatLng(mCurLocation.getLatitude(),mCurLocation.getLongitude()));
                outletInfoRecyclerView = view.findViewById(R.id.recycler_view_outlets);
                outletListAdapter = new OutletListAdapter(outletInfoTempList, getActivity());
                outletInfoRecyclerView.setAdapter(outletListAdapter);
                outletInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mLocationManager.removeUpdates(this);
            } else {
                serviceUnavailable.setVisibility(View.VISIBLE);
            }
            if(swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private void getUpdatedCurLocation(){
        try {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
            closeDialog(getOutletDialog);
        }
    }

}