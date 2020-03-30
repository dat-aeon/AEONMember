package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mm.com.aeon.vcsaeon.BuildConfig;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.adapters.ShoppingMessageListAdapter;
import mm.com.aeon.vcsaeon.beans.BrandResponse;
import mm.com.aeon.vcsaeon.beans.BuyCallUpdate;
import mm.com.aeon.vcsaeon.beans.BuyMessagesBean;
import mm.com.aeon.vcsaeon.beans.BuyMoreMessageUIBean;
import mm.com.aeon.vcsaeon.beans.BuyMsgReqInfo;
import mm.com.aeon.vcsaeon.beans.BuyReceiveMsgUIBean;
import mm.com.aeon.vcsaeon.beans.BuyReqBean;
import mm.com.aeon.vcsaeon.beans.BuyReqInfo;
import mm.com.aeon.vcsaeon.beans.BuyResInfo;
import mm.com.aeon.vcsaeon.beans.BuySendMsgUIBean;
import mm.com.aeon.vcsaeon.beans.CategoryResponse;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.delegates.PurchaseEventDelegate;
import mm.com.aeon.vcsaeon.networking.WebSocketClientListener;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.getChangeTimestampToString3;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.getCurrentTimeStamp;

public class NavPurchaseMessagingTabFragment extends BaseFragment implements PurchaseEventDelegate, LocationListener , LanguageChangeListener {

    View view;

    private static final int RECORD_BUY_REQUEST_CODE = 320;

    private static List<String> categories;
    private static List<String> brands;

    private static List<Integer> categoriesId;
    private static List<Integer> brandsId;

    private static String selectedCategory;
    private static String selectedBrand;

    RecyclerView rvBuyMessage;

    private static Dialog dialog;

    // popup view
    private TextView labelCategory;
    private TextView labelBrand;
    private TextView labelAddText;
    private TextView labelLocation;
    private Button btnSendMsg;

    private static Spinner spinnerCategory;
    private static Spinner spinnerBrand;

    private Button btnShopping;

    public static List<BuyMessagesBean> messageListForUI;
    //public static List<BuyMessagesBean> unreadMessageListForUI;

    public WebSocketClientListener webSocketClientListener;
    public static WebSocketClient webSocketClient;

    TextInputEditText textLocation;

    public static int buyMessageCount = 0;

    static List<BuyResInfo> buyResInfoList;
    static List<Integer> startingIndexList;

    public static ShoppingMessageListAdapter adapter;

    private LocationManager mLocationManager;
    private static Location mCurLocation = null;

    //Buy Send Data.
    int categoryId;
    int brandId;

    public static int userId;
    CountDownTimer timer;
    int count;

    public static final String LANG_MM = "my";
    public static final String LANG_EN = "en";
    public String curLang;
    private static final int DISPLAY_MESSAGE_THICK_COUNT = 10;
    private static final int DISPLAY_LIMIT = DISPLAY_MESSAGE_THICK_COUNT + 1;

    boolean isBuyFormValid = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.purchase_messaging_tab_layout, container, false);
        setHasOptionsMenu(true);
        Log.e("onCreate", "Agent Channel Fragment");

        // set listener
        ((MainMenuActivityDrawer)getActivity()).setLanguageListener(this);
        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);

        String userInfo = PreferencesManager.getCurrentUserInfo(getActivity());
        UserInformationFormBean userInformationFormBean = new Gson().fromJson(userInfo, UserInformationFormBean.class);
        userId = userInformationFormBean.getCustomerId();

        curLang = PreferencesManager.getCurrentLanguage(getContext());
        btnShopping = view.findViewById(R.id.btn_shopping);
        btnShopping.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.agent_channel_btn, getActivity()));

        //Conversation List.
        messageListForUI = new ArrayList();
        //unreadMessageListForUI = new ArrayList();

        startingIndexList = new ArrayList<>();

        //Get Old Messages.
        rvBuyMessage = view.findViewById(R.id.rv_buy_messages_list);

        PreferencesManager.clearBandAndCategory(getActivity());

        // initialize the count
        count = 10;
        final WebSocketClientListener.SocketListiner listener = new WebSocketClientListener.SocketListiner() {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.e("TAG", "shopping-chat --------------- onOpen()." +
                        "\nHttp Status Code : " + serverHandshake.getHttpStatus() + "" +
                        "\nHttp Status Message : " + serverHandshake.getHttpStatusMessage());

                BuyReqBean<BuyMsgReqInfo> historyReqBean = new BuyReqBean();
                historyReqBean.setApi("get-message-history");
                BuyMsgReqInfo buyMsgReqInfo = new BuyMsgReqInfo();
                buyMsgReqInfo.setCustomerId(userId);
                historyReqBean.setParam(buyMsgReqInfo);
                final String get_message_history = new Gson().toJson(historyReqBean);
                Log.e("TAG", "get_message_history : " + get_message_history);
                webSocketClient.send(get_message_history);

                //get brands and categories.
                BuyReqBean brandsReqBean = new BuyReqBean();
                brandsReqBean.setApi("get-device-brands");
                final String get_brand_api = new Gson().toJson(brandsReqBean);
                webSocketClient.send(get_brand_api);
                Log.e("TAG", "JSON : " + get_brand_api);

                BuyReqBean categoriesReqBean = new BuyReqBean();
                categoriesReqBean.setApi("get-device-categories");
                final String get_category_api = new Gson().toJson(categoriesReqBean);
                webSocketClient.send(get_category_api);
                Log.e("TAG", "JSON : " + get_category_api);

            }

            @Override
            public void onMessage(final String message) {

                //
                //
                //
                // Log.e("TAG", "shopping-chat --------------- onMessage(). " + message);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Log.e("TAG", "Running on UI Thread .......");
                            JSONObject object = new JSONObject(message);
                            String type = object.getString("type");
                            Log.e("Message Type", type);

                            if (type.equals("get-categories")) {

                                final String categoriesJson = object.getString("data");
                                Log.e("TAG", "Categories : " + categoriesJson);
                                List<CategoryResponse> categoryResponses = new Gson().fromJson(categoriesJson, new TypeToken<List<CategoryResponse>>() {
                                }.getType());
                                PreferencesManager.saveCategories(getActivity(), categoryResponses);

                            } else if (type.equals("get-brands")) {

                                final String brandsJson = object.getString("data");
                                Log.e("TAG", "Brands : " + brandsJson);
                                List<BrandResponse> brandResponses = new Gson().fromJson(brandsJson, new TypeToken<List<BrandResponse>>() {
                                }.getType());
                                PreferencesManager.saveBrands(getActivity(), brandResponses);

                            } else if (type.equals("post-buy-ok")) {

                                final String okJson = object.getString("data");
                                Log.e("TAG", "Post Buy OK. DATA : " + okJson);

                            } else if (type.equals("post-buy-not-ok")) {

                                final String notOkJson = object.getString("data");
                                Log.e("TAG", "Post Buy Not OK. DATA : " + notOkJson);

                            } else if (type.equals("get-unread-messages")) {

                                final String receiveMessageJson = object.getString("data");
                                Log.e("TAG", "un-Read Message : " + receiveMessageJson);

                                TypeToken<List<BuyResInfo>> token = new TypeToken<List<BuyResInfo>>() {
                                };
                                List<BuyResInfo> buyResInfoList = new Gson().fromJson(receiveMessageJson, token.getType());

                                //BuyMessagesBean tempBuyMessagesBean = new BuyMessagesBean();

                                // read-flag change object
                                BuyReqBean<BuyResInfo> brandsReqBean;
                                for (BuyResInfo buyResInfo : buyResInfoList) {
                                    BuyReceiveMsgUIBean buyReceiveMsgUIBean = new BuyReceiveMsgUIBean();
                                    buyReceiveMsgUIBean.setMessageId(buyResInfo.getMessageId());
                                    buyReceiveMsgUIBean.setAgentId(buyResInfo.getAgentId());
                                    buyReceiveMsgUIBean.setAgentName(buyResInfo.getAgentName());
                                    buyReceiveMsgUIBean.setBrandId(buyResInfo.getBrandId());
                                    buyReceiveMsgUIBean.setBrandName(buyResInfo.getBrandName());
                                    buyReceiveMsgUIBean.setCategoryId(buyResInfo.getCategoryId());
                                    buyReceiveMsgUIBean.setCategoryName(buyResInfo.getCategoryName());
                                    buyReceiveMsgUIBean.setMessageContent(buyResInfo.getContentMessage());
                                    buyReceiveMsgUIBean.setPhoneNo(buyResInfo.getPhoneNo());
                                    buyReceiveMsgUIBean.setPrice(buyResInfo.getPrice());
                                    buyReceiveMsgUIBean.setUrlLink(buyResInfo.getUrlLink());
                                    buyReceiveMsgUIBean.setSendTime(getChangeTimestampToString3(buyResInfo.getSendTime()));
                                    BuyMessagesBean tempBuyMessagesBean = new BuyMessagesBean();
                                    tempBuyMessagesBean.setMessageType(3);
                                    tempBuyMessagesBean.setBuyReceiveMsgUIBean(buyReceiveMsgUIBean);
                                    messageListForUI.add(tempBuyMessagesBean);

                                    brandsReqBean = new BuyReqBean();
                                    brandsReqBean.setApi("read-messages");
                                    brandsReqBean.setParam(buyResInfo);
                                    final String change_read_messages = new Gson().toJson(brandsReqBean);
                                    Log.e("TAG", "read-messages : " + change_read_messages);
                                    webSocketClient.send(change_read_messages);
                                }

                                if (buyResInfoList.size() > 0) {
                                    adapter.notifyDataSetChanged();
                                    rvBuyMessage.smoothScrollToPosition(messageListForUI.size());
                                }
                                int newMsgCount = buyResInfoList.size();
                                if (newMsgCount > 1) {
                                    Toast.makeText(getActivity(), "You have received " + newMsgCount + " new messages.", Toast.LENGTH_LONG).show();
                                } else if (newMsgCount > 0) {
                                    Toast.makeText(getActivity(), "You have received " + newMsgCount + " new message.", Toast.LENGTH_LONG).show();
                                }


                            } else if (type.equals("get-msg-history")) {

                                final String msgHistoryJson = object.getString("data");
                                Log.e("Messages", msgHistoryJson);

                                TypeToken<List<BuyResInfo>> token = new TypeToken<List<BuyResInfo>>() {
                                };
                                buyResInfoList = new Gson().fromJson(msgHistoryJson, token.getType());

                                if (buyResInfoList != null) {

                                    int ListSize = buyResInfoList.size();
                                    Log.e("Size", "" + ListSize);

                                    int startingIndex = ListSize - DISPLAY_LIMIT;
                                    Log.e("Starting index", "" + startingIndex);

                                    if (ListSize > DISPLAY_LIMIT) {
                                        addInitMessageUI(true, startingIndex);
                                    } else {
                                        addInitMessageUI(false, startingIndex);
                                    }
                                }

                                adapter = new ShoppingMessageListAdapter(messageListForUI, NavPurchaseMessagingTabFragment.this);
                                rvBuyMessage.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                Log.e("message list size", messageListForUI.size() + "");
                                rvBuyMessage.setAdapter(adapter);
                                rvBuyMessage.smoothScrollToPosition(messageListForUI.size());

                                count = 10;
                                timer = new CountDownTimer(10000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        count -= 1;
                                    }

                                    @Override
                                    public void onFinish() {
                                        BuyReqBean<BuyMsgReqInfo> historyReqBean = new BuyReqBean();
                                        historyReqBean.setApi("get-unread-messages");
                                        BuyMsgReqInfo buyMsgReqInfo = new BuyMsgReqInfo();
                                        buyMsgReqInfo.setCustomerId(userId);
                                        historyReqBean.setParam(buyMsgReqInfo);
                                        final String get_unread_messages = new Gson().toJson(historyReqBean);
                                        //Log.e("TAG", "get-unread-messages : " + get_unread_messages);

                                        try {
                                            webSocketClient.send(get_unread_messages);
                                            timer.start();
                                        } catch (WebsocketNotConnectedException e) {
                                            try {
                                                webSocketClient = webSocketClientListener.connectWebsocket();
                                            } catch (URISyntaxException urie) {
                                                urie.printStackTrace();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                timer.start();
                            } else if (type.equals("update-call-count")) {

                                final String categoriesJson = object.getString("data");
                                Log.e("Result for Update Call", categoriesJson);

                            }

                        } catch (WebsocketNotConnectedException e) {
                            try {
                                webSocketClient = webSocketClientListener.connectWebsocket();
                            } catch (URISyntaxException urie) {
                                urie.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onClose(int i, String message, boolean b) {
                Log.e("Socket Closed", message);
                timer.cancel();
            }

        };

        webSocketClientListener = new WebSocketClientListener(getActivity(), listener, BuildConfig.PL_CHAT_URL);

        try {
            webSocketClient = webSocketClientListener.connectWebsocket();
        } catch (URISyntaxException urie) {
            urie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.shopping_dialog_layout);

                    if (categories == null) {
                        categories = new ArrayList<>();
                    } else if (!categories.isEmpty()) {
                        categories = new ArrayList<>();
                    }

                    if (categoriesId == null) {
                        categoriesId = new ArrayList<>();
                    } else if (!categoriesId.isEmpty()) {
                        categoriesId = new ArrayList<>();
                    }
                    labelCategory = dialog.findViewById(R.id.lbl_category);
                    labelBrand = dialog.findViewById(R.id.lbl_brand);
                    labelAddText = dialog.findViewById(R.id.lbl_additional_text);
                    labelLocation = dialog.findViewById(R.id.lbl_location);

                    final TextView errAddtext = dialog.findViewById(R.id.err_add_text);
                    final TextView errLocation = dialog.findViewById(R.id.err_location);

                    curLang = PreferencesManager.getCurrentLanguage(getContext());
                    labelCategory.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.agent_channel_category, getContext()));
                    labelBrand.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.agent_channel_brand, getContext()));
                    labelAddText.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.agent_channel_addText, getContext()));
                    labelLocation.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.agent_channel_location, getContext()));

                    List<CategoryResponse> categoryResponses = PreferencesManager.getCategories(getActivity());

                    for (CategoryResponse categoryResponse : categoryResponses) {
                        categories.add(categoryResponse.getProduct_name());
                        categoriesId.add(categoryResponse.getProduct_type_id());
                    }

                    spinnerCategory = dialog.findViewById(R.id.txt_category);
                    ArrayAdapter<String> catAdapter = new ArrayAdapter(dialog.getContext(), R.layout.nrc_spinner_item_4, categories);
                    spinnerCategory.setAdapter(catAdapter);
                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            categoryId = categoriesId.get(position);
                            selectedCategory = categories.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    if (brands == null) {
                        brands = new ArrayList<>();
                    } else if (!brands.isEmpty()) {
                        brands = new ArrayList<>();
                    }

                    if (brandsId == null) {
                        brandsId = new ArrayList<>();
                    } else if (!brandsId.isEmpty()) {
                        brandsId = new ArrayList<>();
                    }

                    List<BrandResponse> brandResponses = PreferencesManager.getBrands(getActivity());

                    for (BrandResponse brandResponse : brandResponses) {
                        brands.add(brandResponse.getBrand_name());
                        brandsId.add(brandResponse.getBrand_id());
                    }

                    spinnerBrand = dialog.findViewById(R.id.txt_brand);
                    ArrayAdapter<String> brandAdapter = new ArrayAdapter(dialog.getContext(), R.layout.nrc_spinner_item_4, brands);
                    spinnerBrand.setAdapter(brandAdapter);
                    spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            brandId = brandsId.get(position);
                            selectedBrand = brands.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    //set location.
                    textLocation = dialog.findViewById(R.id.txt_location);
                    getUpdatedCurLocation();

                    Button btnBuy = dialog.findViewById(R.id.btn_shopping);
                    btnBuy.setText(CommonUtils.getLocaleString(new Locale(curLang),R.string.btn_send, getContext()));

                    btnBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            isBuyFormValid = true;

                            TextInputEditText addTextInput = dialog.findViewById(R.id.txt_additional_text);
                            final String additionalText = addTextInput.getText().toString();

                            final String location = textLocation.getText().toString();

                            if (brandId == 0) {
                                isBuyFormValid = false;
                            }

                            if (categoryId == 0) {
                                isBuyFormValid = false;
                            }

                            if (CommonUtils.isEmptyOrNull(additionalText)) {
                                isBuyFormValid = false;
                                errAddtext.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.at_add_text_err, getContext()));
                                errAddtext.setVisibility(View.VISIBLE);

                            } else {
                                errAddtext.setVisibility(View.GONE);
                            }

                            if (CommonUtils.isEmptyOrNull(location)) {
                                isBuyFormValid = false;
                                errLocation.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.at_location_err, getContext()));
                                errLocation.setVisibility(View.VISIBLE);

                            } else {
                                errLocation.setVisibility(View.GONE);
                            }

                            if (isBuyFormValid) {
                                try {
                                    if (webSocketClient.isClosed()) {
                                        webSocketClientListener.connectWebsocket();
                                        Log.e("TAG", "Reconnect web-socket listener ....");
                                    }

                                    BuyReqInfo mBuySendInfo = new BuyReqInfo();
                                    mBuySendInfo.setCategoryId(categoryId);
                                    mBuySendInfo.setBrandId(brandId);
                                    mBuySendInfo.setLocation(location);
                                    mBuySendInfo.setAdditionalText(additionalText);
                                    mBuySendInfo.setReadFlag(0);
                                    mBuySendInfo.setSendFlag(0);

                                    mBuySendInfo.setSendTime(getCurrentTimeStamp());
                                    mBuySendInfo.setUserId(userId);

                                    //Send mBuySendInfo.
                                    BuyReqBean<BuyReqInfo> buyReqBean = new BuyReqBean<>();
                                    buyReqBean.setApi("post-buy-info");
                                    buyReqBean.setParam(mBuySendInfo);

                                    final String post_buy_info = new Gson().toJson(buyReqBean);
                                    webSocketClient.send(post_buy_info);
                                    Log.e("TAG", "JSON : " + post_buy_info);

                                    BuySendMsgUIBean buySendMsgUIBean = new BuySendMsgUIBean();
                                    buySendMsgUIBean.setBrandId(brandId);
                                    buySendMsgUIBean.setBrandName(selectedBrand);
                                    buySendMsgUIBean.setCategoryId(categoryId);
                                    buySendMsgUIBean.setCategoryName(selectedCategory);
                                    buySendMsgUIBean.setLocation(location);
                                    buySendMsgUIBean.setMessageBody(additionalText);
                                    buySendMsgUIBean.setSenderId(userId);
                                    buySendMsgUIBean.setSendTime(getChangeTimestampToString3(mBuySendInfo.getSendTime()));

                                    BuyMessagesBean tempBuyMessagesBean = new BuyMessagesBean();
                                    tempBuyMessagesBean.setMessageType(2);
                                    tempBuyMessagesBean.setBuySendMsgUIBean(buySendMsgUIBean);
                                    messageListForUI.add(tempBuyMessagesBean);
                                    adapter.notifyDataSetChanged();
                                    rvBuyMessage.smoothScrollToPosition(messageListForUI.size());

                                    dialog.dismiss();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public void replaceFragment(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment, "TAG");
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorite:
                //this.languageFlag = item;
                if (item.getTitle().equals(LANG_MM)) {
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    btnShopping.setText(CommonUtils.getLocaleString(new Locale(LANG_MM), R.string.agent_channel_btn, getActivity()));
                    curLang = LANG_MM;
                    PreferencesManager.setCurrentLanguage(getContext(), curLang);

                } else if (item.getTitle().equals(LANG_EN)) {
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    btnShopping.setText(CommonUtils.getLocaleString(new Locale(LANG_EN), R.string.agent_channel_btn, getActivity()));
                    curLang = LANG_EN;
                    PreferencesManager.setCurrentLanguage(getContext(), curLang);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTouchPhoneCall(int agentId, String phoneNo, int messageId) {

        int permission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeCallRequest();
        } else {

            if (phoneNo == null || phoneNo.equals(BLANK)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.message_call_not_available), Toast.LENGTH_SHORT).show();
            } else {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(PHONE_URI_PREFIX + phoneNo));

                BuyReqBean<BuyCallUpdate> buyReqBean = new BuyReqBean<>();
                BuyCallUpdate buyCallUpdate = new BuyCallUpdate();
                buyCallUpdate.setAgentId(agentId);
                buyCallUpdate.setMessageId(messageId);
                buyReqBean.setApi("update-call-count");
                buyReqBean.setParam(buyCallUpdate);

                final String update_call_count = new Gson().toJson(buyReqBean);
                webSocketClient.send(update_call_count);
                Log.e("TAG", "JSON : " + update_call_count);

                startActivity(callIntent);
            }
        }
    }


    @Override
    public void onTouchReadMore(int currentIndex, int endingIndex) {

        int previousIndex = startingIndexList.get(startingIndexList.size() - 1);

        if (previousIndex > DISPLAY_LIMIT) {
            addInitMessageUI(true, currentIndex);
        } else {
            addInitMessageUI(false, currentIndex);
        }

        adapter = new ShoppingMessageListAdapter(messageListForUI, NavPurchaseMessagingTabFragment.this);
        rvBuyMessage.setAdapter(adapter);
        rvBuyMessage.scrollToPosition(DISPLAY_LIMIT - 1);
    }

    @Override
    public void onClickUrlLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    protected void makeCallRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CALL_PHONE},
                RECORD_BUY_REQUEST_CODE);
    }

    void addMessagesToUI(BuyResInfo newMessage) {

        int opSendFlag = newMessage.getOpSendFlag();

        BuyMessagesBean tempBuyMessagesBean = new BuyMessagesBean();
        BuyReqBean<BuyMsgReqInfo> brandsReqBean = new BuyReqBean();
        brandsReqBean.setApi("read-messages");
        BuyMsgReqInfo buyMsgReqInfo = new BuyMsgReqInfo();

        switch (opSendFlag) {
            case 0:
                BuySendMsgUIBean buySendMsgUIBean = new BuySendMsgUIBean();
                buySendMsgUIBean.setBrandId(newMessage.getBrandId());
                buySendMsgUIBean.setBrandName(newMessage.getBrandName());
                buySendMsgUIBean.setCategoryId(newMessage.getCategoryId());
                buySendMsgUIBean.setCategoryName(newMessage.getCategoryName());
                buySendMsgUIBean.setLocation(newMessage.getLocation());
                buySendMsgUIBean.setMessageBody(newMessage.getContentMessage());
                buySendMsgUIBean.setSenderId(newMessage.getSenderId());
                buySendMsgUIBean.setSendTime(getChangeTimestampToString3(newMessage.getSendTime()));
                tempBuyMessagesBean.setMessageType(2);
                tempBuyMessagesBean.setBuySendMsgUIBean(buySendMsgUIBean);
                messageListForUI.add(tempBuyMessagesBean);
                break;

            case 1:

                //set message
                BuyReceiveMsgUIBean buyReceiveMsgUIBean = new BuyReceiveMsgUIBean();
                buyReceiveMsgUIBean.setMessageId(newMessage.getMessageId());
                buyReceiveMsgUIBean.setAgentId(newMessage.getAgentId());
                buyReceiveMsgUIBean.setAgentName(newMessage.getAgentName());
                buyReceiveMsgUIBean.setBrandId(newMessage.getBrandId());
                buyReceiveMsgUIBean.setBrandName(newMessage.getBrandName());
                buyReceiveMsgUIBean.setCategoryId(newMessage.getCategoryId());
                buyReceiveMsgUIBean.setCategoryName(newMessage.getCategoryName());
                buyReceiveMsgUIBean.setMessageContent(newMessage.getContentMessage());
                buyReceiveMsgUIBean.setPhoneNo(newMessage.getPhoneNo());
                buyReceiveMsgUIBean.setPrice(newMessage.getPrice());
                buyReceiveMsgUIBean.setUrlLink(newMessage.getUrlLink());
                buyReceiveMsgUIBean.setSendTime(getChangeTimestampToString3(newMessage.getSendTime()));
                tempBuyMessagesBean.setMessageType(3);
                tempBuyMessagesBean.setBuyReceiveMsgUIBean(buyReceiveMsgUIBean);
                messageListForUI.add(tempBuyMessagesBean);

                //update read_flag as read.
                buyMsgReqInfo.setMessageId(newMessage.getMessageId());
                brandsReqBean.setParam(buyMsgReqInfo);
                final String read_messages = new Gson().toJson(brandsReqBean);
                webSocketClient.send(read_messages);
                break;
        }
    }

    void addInitMessageUI(Boolean anyMoreMessage, int startingIndex) {

        if (messageListForUI.size() != 0) {
            if (messageListForUI.get(0).getMessageType() == 4) {
                messageListForUI.remove(0);
            }
        }

        startingIndexList.add(startingIndex);

        if (anyMoreMessage) {

            List<BuyMessagesBean> displayedMessageUI = messageListForUI;
            messageListForUI = new ArrayList<>();

            int endingIndex = startingIndex + DISPLAY_LIMIT - 1;
            int currentIndex = startingIndex - DISPLAY_LIMIT;

            Log.e("start end ", endingIndex +","+ currentIndex);
            BuyMoreMessageUIBean buyMoreMessageUIBean = new BuyMoreMessageUIBean();
            buyMoreMessageUIBean.setEndingIndex(endingIndex);
            buyMoreMessageUIBean.setCurrentIndex(currentIndex);
            BuyMessagesBean buyMessagesBean = new BuyMessagesBean();
            buyMessagesBean.setMessageType(4);
            buyMessagesBean.setBuyMoreMessageUIBean(buyMoreMessageUIBean);
            messageListForUI.add(buyMessagesBean);

            for (int i = startingIndex; i <= endingIndex; i++) {
                BuyResInfo buyResInfo = buyResInfoList.get(i);
                addMessagesToUI(buyResInfo);
            }

            for (BuyMessagesBean displayedMessagesBean : displayedMessageUI) {
                messageListForUI.add(displayedMessagesBean);
            }

        } else {

            List<BuyMessagesBean> displayedMessageUI = messageListForUI;
            messageListForUI = new ArrayList<>();

            int displayedCount = displayedMessageUI.size();
            int totalCount = buyResInfoList.size();
            int remainCount = totalCount - displayedCount;

            for (int i = 0; i < remainCount; i++) {
                BuyResInfo buyResInfo = buyResInfoList.get(i);
                addMessagesToUI(buyResInfo);
            }

            for (BuyMessagesBean displayedMessagesBean : displayedMessageUI) {
                messageListForUI.add(displayedMessagesBean);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurLocation = location;
        Log.e("TAG", "Lat : " + mCurLocation.getLatitude() + " | Long : " + mCurLocation.getLongitude());
        getAddressFromLocation(mCurLocation);
        mLocationManager.removeUpdates(this);
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

    private void getUpdatedCurLocation() {
        try {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getAddressFromLocation(Location location) {

        if (location != null) {

            //Current Location.
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    textLocation.setText(getAddressString(address));
                } else {
                    textLocation.setText(BLANK);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Could not get address..!");
            }

        } else {
            textLocation.setText(BLANK);
            Log.e("TAG", "Location is null.");
        }
    }

    String getAddressString(Address address) {

        String fullAddress = BLANK;

        if (address != null) {

            String road = address.getThoroughfare();
            String township = address.getSubLocality();
            String city = address.getLocality();
            String countryName = address.getCountryName();

            Log.e("TAG", "getAddressString().");

            if (road != null) {
                fullAddress = fullAddress + road;
            }

            if (township != null) {
                if (fullAddress.equals(BLANK)) {
                    fullAddress = fullAddress + township;
                } else {
                    fullAddress = fullAddress + ", " + township;
                }
            }

            if (city != null) {
                if (fullAddress.equals(BLANK)) {
                    fullAddress = fullAddress + city;
                } else {
                    fullAddress = fullAddress + ", " + city;
                }
            }

            if (countryName != null) {
                if (fullAddress.equals(BLANK)) {
                    fullAddress = fullAddress + countryName + ".";
                } else {
                    fullAddress = fullAddress + ", " + countryName + ".";
                }
            }
        }

        return fullAddress;
    }

    @Override
    public void changeLanguageTitle(String lang) {
        btnShopping.setText(CommonUtils.getLocaleString(new Locale(lang), R.string.agent_channel_btn, getActivity()));
        PreferencesManager.setCurrentLanguage(getContext(), lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
    }
}
