package mm.com.aeon.vcsaeon.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.PromotionsInfoResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.views.customviews.MyMapView;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.PROMOTIONS_URL;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isLatLongValid;

public class AnnouncementListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_HISTORY_ROW = 1;
    private Context context;
    private List<PromotionsInfoResBean> promotionsInfoResBeanList;
    private Bundle savedInstanceState;
    private int imgViewHeight=0;

    MyMapView mMapView;
    private GoogleMap mMap;

    public AnnouncementListAdapter(Context context, List<PromotionsInfoResBean> promotionsInfoResBeanList, Bundle savedInstanceState) {
        this.context = context;
        this.promotionsInfoResBeanList = promotionsInfoResBeanList;
        this.savedInstanceState=savedInstanceState;
        imgViewHeight = (int) context.getResources().getDimension(R.dimen.image_detail_height);
    }

    @Override
    public int getItemCount() {
        return promotionsInfoResBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_HISTORY_ROW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //return null;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.promotions_template, viewGroup, false);
        return new PromotionsInfoDataRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        PromotionsInfoResBean promotionsInfoResBean = promotionsInfoResBeanList.get(i);
        ((PromotionsInfoDataRowHolder) viewHolder).bind(promotionsInfoResBean);
    }

    public String getReadMoreText(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.read_more, context);
    }

    private class PromotionsInfoDataRowHolder extends RecyclerView.ViewHolder{

        TextView textTitle;
        TextView textPublishedDate;
        TextView textContent;
        TextView textReadMore;
        ImageView promotionsImage;

        PromotionsInfoDataRowHolder(final View itemView){
            super(itemView);
            textTitle = itemView.findViewById(R.id.promotions_title);
            textPublishedDate = itemView.findViewById(R.id.promotions_published_date);
            textContent = itemView.findViewById(R.id.promotions_content);
            textReadMore = itemView.findViewById(R.id.promotions_read_more);
            promotionsImage = itemView.findViewById(R.id.promotions_img);

        }

        void bind(final PromotionsInfoResBean promotionsInfoResBean){

            final SharedPreferences mPreferences= PreferencesManager.getApplicationPreference(context);
            final String curLang = PreferencesManager.getStringEntryFromPreferences(mPreferences, "lang");

            String mContent = promotionsInfoResBean.getContentEng();

            if(curLang.equals(LANG_MM)){
                mContent = promotionsInfoResBean.getContentMyn();
                textTitle.setText(promotionsInfoResBean.getTitleMyn());
                textReadMore.setText(getReadMoreText(curLang));
            } else {
                textTitle.setText(promotionsInfoResBean.getTitleEng());
                textReadMore.setText(getReadMoreText(curLang));
            }

            textPublishedDate.setText(CommonUtils.getStringFromDateDisplay(promotionsInfoResBean.getDisplayDate()));
            textContent.setText(mContent);

            //Coupon Img.
            final String imagePath = PROMOTIONS_URL+promotionsInfoResBean.getImagePath();
            if(imagePath==null || imagePath==BLANK) {
                //do something.
            } else {
                Picasso.get().load(imagePath).into(promotionsImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.get().load(imagePath).into(promotionsImage);
                    }
                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            //click item for detail.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.promotions_detail_tab);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    ImageView btnBack = dialog.findViewById(R.id.promotions_detail_back);
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    TextView textDetailTitle = dialog.findViewById(R.id.promotions_detail_title);
                    TextView textDetailPublishedDate = dialog.findViewById(R.id.promotions_detail_published_date);
                    TextView textDetailContent = dialog.findViewById(R.id.promotions_detail_content);
                    TextView textAnnouncementLink = dialog.findViewById(R.id.announcements_link);
                    final ImageView promotionsDetailImage = dialog.findViewById(R.id.promotions_detail_image);

                    if(promotionsInfoResBean.getAnnouncementUrl() != null){
                        //textAnnouncementLink.setText(Html.fromHtml(stringToHtmlUrl(promotionsInfoResBean.getAnnouncementUrl())));
                        textAnnouncementLink.setText(promotionsInfoResBean.getAnnouncementUrl());
                    } else {
                        textAnnouncementLink.setVisibility(View.GONE);
                    }

                    final SharedPreferences mPreferences= PreferencesManager.getApplicationPreference(context);
                    final String curLang = PreferencesManager.getStringEntryFromPreferences(mPreferences, "lang");

                    textDetailPublishedDate.setText(CommonUtils.getStringFromDateDisplay(promotionsInfoResBean.getDisplayDate()));

                    if(curLang.equals(LANG_MM)){
                        textDetailTitle.setText(promotionsInfoResBean.getTitleMyn());
                        textDetailContent.setText(promotionsInfoResBean.getContentMyn());
                    } else {
                        textDetailTitle.setText(promotionsInfoResBean.getTitleEng());
                        textDetailContent.setText(promotionsInfoResBean.getContentEng());
                    }


                    //Coupon Img.
                    final String imagePath = PROMOTIONS_URL+promotionsInfoResBean.getImagePath();
                    if(imagePath==null || imagePath==BLANK) {
                        promotionsDetailImage.setMinimumHeight(imgViewHeight);
                    } else {
                        Picasso.get().load(imagePath).into(promotionsDetailImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                try{
                                    Picasso.get().load(imagePath).into(promotionsDetailImage);
                                } catch (Exception e){
                                    promotionsDetailImage.setMinimumHeight(imgViewHeight);
                                }
                            }
                            @Override
                            public void onError(Exception e) {
                                promotionsDetailImage.setMinimumHeight(imgViewHeight);
                            }
                        });
                    }

                    mMapView = dialog.findViewById(R.id.promotionsMapView);
                    mMapView.onCreate(savedInstanceState);
                    mMapView.onResume(); // needed to get the map to display immediately

                    try {
                        MapsInitializer.initialize(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(isLatLongValid(promotionsInfoResBean.getLatitude(),promotionsInfoResBean.getLongitude())){

                        //location(x,y)
                        final double latitude = Double.valueOf(promotionsInfoResBean.getLatitude());
                        final double longitude = Double.valueOf(promotionsInfoResBean.getLongitude());

                        mMapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                mMap = googleMap;

                                // Add a marker in Sydney and move the camera
                                LatLng position = new LatLng(latitude,longitude);
                                mMap.addMarker(new MarkerOptions().position(position));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

                                // For zooming automatically to the location of the marker
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(15).build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        });

                    } else {
                        mMapView.setVisibility(View.GONE);
                    }

                    //removing background dim
                    Window w = dialog.getWindow();
                    w.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                    dialog.show();
                }
            });
        }
    }
}
