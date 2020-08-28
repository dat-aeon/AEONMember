package mm.com.aeon.vcsaeon.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.ArraySet;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.EventsNewsInfoResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.UiUtils;
import mm.com.aeon.vcsaeon.views.customviews.MyMapView;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.NEWS_URL;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isLatLongValid;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.animSlideToLeft;

public class EventsNewsListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_HISTORY_ROW = 1;
    private Context context;
    private List<EventsNewsInfoResBean> eventsNewsInfoResBeanList;
    private RecyclerView eventsNewsInfoRecyclerView;
    private Bundle savedInstanceState;
    private GoogleMap mMap;
    private int imgViewHeight = 0;

    private Set<Integer> visitedItems = new ArraySet<>();

    public EventsNewsListAdapter(Context context, List<EventsNewsInfoResBean> eventsNewsInfoResBeanList, RecyclerView eventsNewsInfoRecyclerView, Bundle savedInstanceState) {
        this.context = context;
        this.eventsNewsInfoResBeanList = eventsNewsInfoResBeanList;
        this.eventsNewsInfoRecyclerView = eventsNewsInfoRecyclerView;
        this.savedInstanceState = savedInstanceState;
        imgViewHeight = (int) context.getResources().getDimension(R.dimen.image_detail_height);
    }

    @Override
    public int getItemCount() {
        return eventsNewsInfoResBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_HISTORY_ROW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //return null;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.events_news_template, viewGroup, false);
        return new EventsNewsInfoDataRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        EventsNewsInfoResBean eventsNewsInfoResBean = eventsNewsInfoResBeanList.get(i);
        ((EventsNewsInfoDataRowHolder) viewHolder).bind(eventsNewsInfoResBean, i);
    }

    private class EventsNewsInfoDataRowHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        TextView textPublishedDate;
        TextView textContent;
        TextView textReadMore;
        ImageView newsImage;

        EventsNewsInfoDataRowHolder(final View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.news_title);
            textPublishedDate = itemView.findViewById(R.id.news_published_date);
            textContent = itemView.findViewById(R.id.news_content);
            textReadMore = itemView.findViewById(R.id.news_read_more);
            newsImage = itemView.findViewById(R.id.news_img);
        }

        public String getReadMoreText(String language) {
            return CommonUtils.getLocaleString(new Locale(language), R.string.read_more, context);
        }

        void bind(final EventsNewsInfoResBean eventsNewsInfoResBean, int index) {

            if (visitedItems.contains(eventsNewsInfoResBean.getNewsInfoId())) {
                itemView.clearAnimation();
            } else {
                if (index > 5) index = index / 5;
                itemView.setAnimation(UiUtils.rvAnimSlideToLeft(context, (++index * 100) + 100)); //Anim.
                visitedItems.add(eventsNewsInfoResBean.getNewsInfoId());
            }

            final SharedPreferences mPreferences = PreferencesManager.getApplicationPreference(context);
            final String curLang = PreferencesManager.getStringEntryFromPreferences(mPreferences, "lang");

            if (curLang.equals(LANG_MM)) {
                textTitle.setText(eventsNewsInfoResBean.getTitleMyn());
                textContent.setText(eventsNewsInfoResBean.getContentMyn());
            } else {
                textTitle.setText(eventsNewsInfoResBean.getTitleEng());
                textContent.setText(eventsNewsInfoResBean.getContentEng());
            }

            textReadMore.setText(getReadMoreText(curLang));
            if (eventsNewsInfoResBean.getDisplayDate() != null) {
                textPublishedDate.setText(CommonUtils.getStringFromDateDisplay(eventsNewsInfoResBean.getDisplayDate()));
            }

            //Coupon Img.
            final String imagePath = NEWS_URL + eventsNewsInfoResBean.getImagePath();
            if (imagePath == null || imagePath == BLANK) {
                //Picasso.get().load(R.drawable.noimage).into(newsImage);
            } else {
                Picasso.get().load(imagePath).into(newsImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.get().load(imagePath).into(newsImage);
                    }

                    @Override
                    public void onError(Exception e) {
                        //Picasso.get().load(R.drawable.noimage).into(newsImage);
                    }
                });
            }

            //click item for detail.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.events_news_detail_tab);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    ImageView detailBack = dialog.findViewById(R.id.news_detail_back);
                    detailBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    final MyMapView mMapView;

                    TextView textDetailTitle = dialog.findViewById(R.id.news_detail_title);
                    textDetailTitle.setAnimation(UiUtils.animSlideToRight(context));

                    TextView textDetailPublishedDate = dialog.findViewById(R.id.news_detail_published_date);
                    textDetailPublishedDate.setAnimation(UiUtils.animSlideToRight(context));

                    TextView textDetailContent = dialog.findViewById(R.id.news_detail_content);
                    textDetailContent.setAnimation(UiUtils.animSlideToRight(context));

                    TextView textNewsUrlLink = dialog.findViewById(R.id.news_link);
                    textNewsUrlLink.setAnimation(UiUtils.animSlideToRight(context));

                    if (eventsNewsInfoResBean.getNewsUrl() != null) {
                        textNewsUrlLink.setText(eventsNewsInfoResBean.getNewsUrl());
                    } else {
                        textNewsUrlLink.setVisibility(View.GONE);
                    }

                    final ImageView newsDetailImage = dialog.findViewById(R.id.news_detail_image);
                    newsDetailImage.setAnimation(UiUtils.animSlideToRight(context));

                    final SharedPreferences mPreferences = PreferencesManager.getApplicationPreference(context);
                    final String curLang = PreferencesManager.getStringEntryFromPreferences(mPreferences, "lang");

                    if (eventsNewsInfoResBean.getDisplayDate() != null) {
                        textDetailPublishedDate.setText(CommonUtils.getStringFromDateDisplay(eventsNewsInfoResBean.getDisplayDate()));
                    }

                    if (curLang.equals(LANG_MM)) {
                        textDetailTitle.setText(eventsNewsInfoResBean.getTitleMyn());
                        textDetailContent.setText(eventsNewsInfoResBean.getContentMyn());
                    } else {
                        textDetailTitle.setText(eventsNewsInfoResBean.getTitleEng());
                        textDetailContent.setText(eventsNewsInfoResBean.getContentEng());
                    }

                    mMapView = dialog.findViewById(R.id.mapView);
                    mMapView.setAnimation(UiUtils.animSlideToRight(context));
                    mMapView.onCreate(savedInstanceState);
                    mMapView.onResume(); // needed to get the map to display immediately

                    try {
                        MapsInitializer.initialize(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //location(x,y)
                    if (isLatLongValid(eventsNewsInfoResBean.getLatitude(), eventsNewsInfoResBean.getLongitude())) {

                        final double latitude = Double.valueOf(eventsNewsInfoResBean.getLatitude());
                        final double longitude = Double.valueOf(eventsNewsInfoResBean.getLongitude());

                        mMapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {

                                mMap = googleMap;

                                // Add a marker in Sydney and move the camera
                                LatLng position = new LatLng(latitude, longitude);
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

                    //Coupon Img.
                    final String imagePath = NEWS_URL + eventsNewsInfoResBean.getImagePath();
                    if (imagePath == null || imagePath == BLANK) {
                        newsDetailImage.setMinimumHeight(imgViewHeight);
                    } else {
                        Picasso.get().load(imagePath).into(newsDetailImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.get().load(imagePath).into(newsDetailImage);
                            }

                            @Override
                            public void onError(Exception e) {
                                newsDetailImage.setMinimumHeight(imgViewHeight);
                            }
                        });
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
