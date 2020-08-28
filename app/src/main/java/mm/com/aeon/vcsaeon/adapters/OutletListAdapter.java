package mm.com.aeon.vcsaeon.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.OutletListInfoResBean;
import mm.com.aeon.vcsaeon.fragments.OutletLocationTabFragment;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_MULTIPLE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_M_LOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_NON_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_P_LOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.OUTLET_URL;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;

public class OutletListAdapter extends RecyclerView.Adapter {

    private static final int RECORD_REQUEST_CODE = 101;

    private List<OutletListInfoResBean> outletListInfoResBeanList;
    private Context context;

    public OutletListAdapter(List<OutletListInfoResBean> outletListInfoResBeanList, Context context) {
        this.context = context;
        this.outletListInfoResBeanList = outletListInfoResBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outlets_template, parent, false);
        return new OutletInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OutletListInfoResBean outletListInfoResBean = outletListInfoResBeanList.get(position);
        ((OutletInfoViewHolder) holder).bind(outletListInfoResBean);
    }

    @Override
    public int getItemCount() {
        return outletListInfoResBeanList.size();
    }

    private class OutletInfoViewHolder extends RecyclerView.ViewHolder {

        TextView outletName;
        TextView outletAddress;
        TextView outletDistance;
        ImageView imgNav;

        public OutletInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            outletName = itemView.findViewById(R.id.outlet_name);
            outletAddress = itemView.findViewById(R.id.outlet_address);
            outletDistance = itemView.findViewById(R.id.outlet_distance);
            imgNav = itemView.findViewById(R.id.img_navigation);
        }

        public void bind(final OutletListInfoResBean outletListInfoResBean) {

            DecimalFormat df = new DecimalFormat("###");
            outletDistance.setText(df.format(outletListInfoResBean.getDistanceInMeter()) + " m");

            outletName.setText(outletListInfoResBean.getOutletName());
            outletAddress.setText(outletListInfoResBean.getOutletAddress());

            int roleId = outletListInfoResBean.getRoleId();
            switch (roleId) {
                case ROLE_MOBILE:
                    Glide.with(itemView).load(R.drawable.ic_location_mobile).into(imgNav);
                    break;
                case ROLE_NON_MOBILE:
                    Glide.with(itemView).load(R.drawable.ic_location_non_mobile).into(imgNav);
                    break;
                case ROLE_M_LOAN:
                    Glide.with(itemView).load(R.drawable.ic_location_motorcycle).into(imgNav);
                    break;
                case ROLE_MULTIPLE:
                    Glide.with(itemView).load(R.drawable.ic_location_multiple_loan).into(imgNav);
                    break;
            }

            if (outletListInfoResBean.isAeon()) {
                Glide.with(itemView).load(R.drawable.aeon_msg_logo).circleCrop().into(imgNav);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.outlet_info_detail);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    ImageView imgBack = dialog.findViewById(R.id.btn_outlet_back);
                    imgBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    RelativeLayout callLayout = dialog.findViewById(R.id.laout_phone);
                    if (outletListInfoResBean.getPhoneNo() == null || outletListInfoResBean.getPhoneNo().isEmpty()) {
                        callLayout.setVisibility(View.GONE);
                    } else {
                        TextView textPhoneNo = dialog.findViewById(R.id.text_telephone_no);
                        textPhoneNo.setText(outletListInfoResBean.getPhoneNo());
                        callLayout.setVisibility(View.VISIBLE);
                    }

                    TextView textOutletName = dialog.findViewById(R.id.outletName);
                    textOutletName.setText(outletListInfoResBean.getOutletName());

                    TextView textAddress = dialog.findViewById(R.id.outletAddress);
                    textAddress.setText(outletListInfoResBean.getOutletAddress());

                    final ImageView outletImage = dialog.findViewById(R.id.outlet_img);
                    String imgFileName = outletListInfoResBean.getImagePath();
                    final String imagePath = OUTLET_URL + imgFileName;

                    if (imgFileName == null || imgFileName.equals(BLANK)) {
                        //do something.
                    } else {
                        Picasso.get().load(imagePath).into(outletImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.get().load(imagePath).into(outletImage);
                            }

                            @Override
                            public void onError(Exception e) {
                                //do something.
                            }
                        });
                    }

                    ImageView btnNavigate = dialog.findViewById(R.id.btn_map_nav);
                    btnNavigate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int permission = ContextCompat.checkSelfPermission(context,
                                    Manifest.permission.ACCESS_FINE_LOCATION);

                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                makeLocationRequest();
                            } else {
                                //Destination Position
                                LatLng destPosition = new LatLng(
                                        outletListInfoResBean.getLatitude(),
                                        outletListInfoResBean.getLongitude()
                                );

                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse(getMapUri(OutletLocationTabFragment.latLng, destPosition)));
                                if (intent.resolveActivity(context.getPackageManager()) != null) {
                                    context.startActivity(intent);
                                } else {
                                    displayMessage(context, context.getString(R.string.message_gmap_not_support));
                                }

                            }

                        }
                    });

                    ImageView btnCall = dialog.findViewById(R.id.btn_outlet_call);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int permission = ContextCompat.checkSelfPermission(context,
                                    Manifest.permission.CALL_PHONE);

                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                makeCallRequest();
                            } else {
                                String outletPhone = outletListInfoResBean.getPhoneNo();
                                if (outletPhone != null) {
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse(PHONE_URI_PREFIX + outletPhone));
                                    context.startActivity(callIntent);
                                } else {
                                    displayMessage(context, context.getString(R.string.message_call_not_available));
                                }
                            }
                        }
                    });

                    dialog.show();

                }
            });
        }
    }

    private String getMapUri(LatLng curPosition, LatLng destPosition) {
        double curLat = curPosition.latitude;
        double curLng = curPosition.longitude;
        double destLat = destPosition.latitude;
        double destLng = destPosition.longitude;
        String url = "http://maps.google.com/maps?saddr=" + curLat + "," + curLng + "&daddr=" + destLat + "," + destLng;
        Log.d("TAG", url);
        return url;
    }

    protected void makeCallRequest() {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.CALL_PHONE},
                RECORD_REQUEST_CODE);
    }

    protected void makeLocationRequest() {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RECORD_REQUEST_CODE);
    }
}
