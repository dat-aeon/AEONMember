package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mm.com.aeon.vcsaeon.BuildConfig;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.adapters.MessageListAdapter;
import mm.com.aeon.vcsaeon.beans.AutoReplyMessageBean;
import mm.com.aeon.vcsaeon.beans.MessageInfoBean;
import mm.com.aeon.vcsaeon.beans.TempOldMsgInfo;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.common_utils.UiUtils;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.WebSocketClientListener;

import static android.app.Activity.RESULT_OK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.AUTO_MESSAGE_REPLY;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CAMERA_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MSG_TYPE_GET_STARTED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MSG_TYPE_LANGUAGE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STORAGE_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_SEND_IMG_MSG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_SEND_TEXT_MSG;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.decodeStringToBitmap;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.hideKeyboard;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.animSlideToLeft;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.animSlideToUp;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class NavFreeChatFragment extends BaseFragment implements LanguageChangeListener {

    View view;

    public static Button sendBtn;
    private ImageView imgBrowse;
    private ImageView imgCapture;
    private ImageView imgSend;
    private ImageView imgAdd;
    public static EditText textMsg;

    private RelativeLayout layoutMyanmar;
    private RelativeLayout layoutMyanmar2;
    private TextView lblMMInterestRate;
    private TextView lblMMPhoneNo;
    private TextView lblMMOfficeAddress;

    private LinearLayout layoutChatInput;

    private RelativeLayout layoutEnglish;
    private RelativeLayout layoutEnglish2;
    private TextView lblEnInterestRate;
    private TextView lblEnPhoneNo;
    private TextView lblEnOfficeAddress;

    //Messenger-Bot Menu Layout.
    private ScrollView layoutMessengerBotMenu;

    private RelativeLayout layoutHome;
    private RelativeLayout layoutGetStarted;
    private ImageView imgBotMenu;
    private CoordinatorLayout rootView;

    RelativeLayout layoutLoading;
    ImageView imgLoading;

    private ImageView btnAdminCall;
    private TextView textPhoneNo;

    public static MessageListAdapter mMessageAdapter;
    public static RecyclerView mMessageRecycler;
    public static ProgressDialog msgReqDialog;
    public static List<MessageInfoBean> allMessageList;
    public static List<MessageInfoBean> messageListForUI;
    public List<TempOldMsgInfo> tempOldMsgList;
    public WebSocketClientListener webSocketClientListener;

    public static WebSocketClient messageSocketClient;

    private String mCurrentPhotoPath;
    private static String imgEncoded;

    final int RESULT_LOAD_IMAGE = 1;
    final int REQUEST_TAKE_PHOTO = 2;

    static String phoneNo;
    String senderId;

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.free_chat_fragment, container, false);
        setHasOptionsMenu(true);

        PreferencesManager.setSocketFlagClose(getActivity(), false);

        // add listener
        ((MainActivity) getActivity()).setLanguageListener(this);
        Toolbar toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar_main_home);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        TextView menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarLevelInfo.setText(R.string.menu_level_one);
        menuBackBtn.setAnimation(UiUtils.animSlideToRight(getActivity()));
        menuBackBtn.setVisibility(View.VISIBLE);

        bindViews();

        msgReqDialog = new ProgressDialog(getActivity());
        msgReqDialog.setMessage(getString(R.string.progress_loading));
        msgReqDialog.setCancelable(false);
        msgReqDialog.show();

        allMessageList = new ArrayList<>();
        messageListForUI = new ArrayList<>();

        String freeChatPhone = PreferencesManager.getInstallPhoneNo(getContext()).trim();
        phoneNo = freeChatPhone;

        //setFreMessageRoom();
        senderId = PreferencesManager.getCurrentFreeChatId(getContext());

        setUpListeners();
        setUpInitView();

        return view;
    }


    //Create Image File and Capture.
    public void openCamera(String senderId) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri temUri;
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss_SSS").format(new Date());
        String imageFileName = senderId + "_" + timeStamp;
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoCaptured = createTempFile(imageFileName, storageDir);

        try {

            if (Build.VERSION.SDK_INT >= 24) {
                temUri = FileProvider.getUriForFile(getActivity(),
                        getActivity().getPackageName()
                                + ".my.package.name.provider", new File(photoCaptured.getAbsolutePath()));
            } else {
                temUri = Uri.fromFile(photoCaptured);
            }

            mCurrentPhotoPath = photoCaptured.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, temUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);

        } catch (Exception e) {
            e.printStackTrace();
            displayMessage(getActivity(), "Image cannot be created.");
        }
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
                if (item.getTitle().equals(LANG_MM)) {
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeLabel(LANG_MM);

                } else if (item.getTitle().equals(LANG_EN)) {
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeLabel(LANG_EN);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        //setup message-socket-listener.
        if (messageSocketClient.isClosed()) {
            try {
                messageSocketClient = webSocketClientListener.connectWebsocket();
            } catch (URISyntaxException e) {
                closeDialog(msgReqDialog);
                e.printStackTrace();
            } catch (OutOfMemoryError ex) {
                closeDialog(msgReqDialog);
                ex.printStackTrace();
            } catch (Exception ee) {
                closeDialog(msgReqDialog);
                ee.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (imgEncoded == null) {
            setUpInitView();
        } else {
            displaySendImg();
            displayMediaInputs();
            displaySendBtn();
            hideBotMenuLayout();
            hideBotMenu();
            hideAdd();
            imgSend.setImageBitmap(decodeStringToBitmap(imgEncoded));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //clear socket connection.
        if (!messageSocketClient.isClosed()) {
            messageSocketClient.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RESULT_LOAD_IMAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery();
        } else if (requestCode == CAMERA_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takeImageFromCameraCapture();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //selected photo.
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            //Load Image Dialog.
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //selected file.
            File mFile = new File(picturePath);
            if (mFile.exists()) {
                //Convert JPG image to Base64 encoded string.
                imgEncoded = CommonUtils.encodeFileToString(mFile);
            }

            //captured photo.
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                File mFile = new File(mCurrentPhotoPath);
                if (mFile.exists()) {
                    //Convert JPG image to Base64 encoded string.
                    imgEncoded = CommonUtils.encodeFileToString(mFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Change languages.
    public void changeLabel(String language) {
        sendBtn.setText(CommonUtils.getLocaleString(new Locale(language), R.string.btn_send, getActivity()));
        textMsg.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.hint_msg, getActivity()));
        //change auto-reply message language.
        if (messageListForUI != null && mMessageAdapter != null
                && mMessageRecycler != null && messageListForUI.size() > 0) {
            messageListForUI.get(0).setMessage(setAutoReplyMessage(language));
            mMessageAdapter.notifyDataSetChanged();
        }
        PreferencesManager.setCurrentLanguage(getActivity(), language);
    }

    //Check phone storage permission allowed or not.
    void requestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        }
    }

    //check camera permission.
    void requestDeviceCameraAccessPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
    }

    private File createTempFile(String imageFileName, File storageDir) {
        try {
            return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (Exception e) {
            e.printStackTrace();
            displayMessage(getActivity(), "Image File could not be created.2\n" + e.getMessage());
            return null;
        }
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        messageSocketClient.close();
        replaceFragment(new NewMainPageFragment(), R.id.content_new_main_drawer);
    }

    //retrieve message according to language flag.
    private String setAutoReplyMessage(String langCode) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(getActivity());
        String initMessageReply = PreferencesManager.getStringEntryFromPreferences(preferences, AUTO_MESSAGE_REPLY);
        if (!initMessageReply.equals(BLANK)) {
            AutoReplyMessageBean messageReply = new Gson().fromJson(initMessageReply, AutoReplyMessageBean.class);
            if (langCode.equals(LANG_MM)) {
                return messageReply.getMessageMya();
            } else {
                return messageReply.getMessageEng();
            }
        }
        return BLANK;
    }

    void pickImageFromGallery() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        } else {
            requestReadExternalStoragePermission();
        }
    }

    void takeImageFromCameraCapture() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera(senderId);
        } else {
            requestDeviceCameraAccessPermission();
        }
    }

    //bind views from xml to fragment class.
    void bindViews() {

        imgBrowse = view.findViewById(R.id.img_browse);
        imgCapture = view.findViewById(R.id.img_capture);
        imgSend = view.findViewById(R.id.img_send);
        imgAdd = view.findViewById(R.id.img_add);
        textMsg = view.findViewById(R.id.edittext_chatbox);
        sendBtn = view.findViewById(R.id.button_chatbox_send);
        textPhoneNo = view.findViewById(R.id.admin_phone);
        textPhoneNo.setText(PreferencesManager.getHotlinePhone(getContext()));
        btnAdminCall = view.findViewById(R.id.btn_admin_call);

        //setup _recycler_view.
        mMessageRecycler = view.findViewById(R.id.recyclerview_message_list);

        layoutMessengerBotMenu = view.findViewById(R.id.layoutBotMenu);

        layoutHome = view.findViewById(R.id.layoutHome);
        layoutGetStarted = view.findViewById(R.id.layoutGetStarted);
        imgBotMenu = view.findViewById(R.id.imgBotMenu);
        rootView = view.findViewById(R.id.freeChatViewGroup);

        layoutLoading = view.findViewById(R.id.layout_loading);
        imgLoading = view.findViewById(R.id.img_loading);

        layoutMyanmar = view.findViewById(R.id.layoutMyanmar);
        layoutMyanmar2 = view.findViewById(R.id.layoutMyanmar2);
        lblMMInterestRate = view.findViewById(R.id.labelMyanmar2InterestRate);
        lblMMPhoneNo = view.findViewById(R.id.labelMyanmar2PhoneNo);
        lblMMOfficeAddress = view.findViewById(R.id.labelMyanmar2Address);

        layoutEnglish = view.findViewById(R.id.layoutEnglish);
        layoutEnglish2 = view.findViewById(R.id.layoutEnglish2);
        lblEnInterestRate = view.findViewById(R.id.labelEnglishInterestRate);
        lblEnPhoneNo = view.findViewById(R.id.labelEnglishPhoneNo);
        lblEnOfficeAddress = view.findViewById(R.id.labelEnglishAddress);

        layoutChatInput = view.findViewById(R.id.layout_chatbox);
    }


    void setUpListeners() {

        final String curLang = PreferencesManager.getCurrentLanguage(getActivity());
        changeLabel(curLang);

        final WebSocketClientListener.SocketListiner listener = new WebSocketClientListener.SocketListiner() {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                //Log.e("free-chat", "on @open.");
                messageSocketClient.send("userName:" + phoneNo + "userId:" + senderId);
                messageSocketClient.send("cr:" + phoneNo + "or:" + "userWithAgency:");
            }

            @Override
            public void onMessage(final String message) {

                //Log.e("free-chat", "on @message");

                final String messageStr = message;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject object = new JSONObject(messageStr);
                            String type = object.getString("type");

                            //if socket notified message is coming
                            if (type.equals("message")) {

                                //Log.e("free-chat", "on message.");

                                JSONObject messageObj = object.getJSONObject("data");
                                String sendFlag = messageObj.getString("op_send_flag");
                                String messageType = messageObj.getString("message_type");

                                // if message send from operator, add message to FreeChat UI
                                if (sendFlag.equals("1")) {

                                    //Log.e("free-chat", "on message sent from operator.");

                                    MessageInfoBean message = new MessageInfoBean();
                                    message.setSender("A");
                                    message.setMessage(messageObj.getString("text"));
                                    message.setReceiveMesg(true);
                                    message.setSendTime(CommonUtils.getCurTimeString2());

                                    messageListForUI.add(message);
                                    mMessageAdapter.notifyDataSetChanged();
                                    mMessageRecycler.smoothScrollToPosition(messageListForUI.size());

                                    //clear socket connection.
                                    if ((messageSocketClient != null)) {
                                        if ((!messageSocketClient.isClosed())) {
                                            messageSocketClient.send("ChangeReadFlagWithMsgId:" + messageObj.getString("message_id"));
                                        }
                                    }

                                }
                            }

                            // if socket is created room on server, get message List
                            else if (type.equals("room")) {
                                //Log.e("free-chat", "room created.");
                            }

                            // if message list is got from server, display on FreeChat UI
                            else if (type.equals("messageListData")) {
                                //Log.e("free-chat", "on message-list data.");
                            }

                            //load old message.
                            else if (type.equals("mobileOldMessage")) {

                                //Log.e("free-chat", "on mobile-old-message.");

                                //setup auto-reply message at top of message list.
                                MessageInfoBean _messageInfoBean = new MessageInfoBean();
                                _messageInfoBean.setMessage(setAutoReplyMessage(curLang));
                                _messageInfoBean.setIntro(true);
                                messageListForUI.add(_messageInfoBean);

                                Gson gson = new Gson();
                                Type token = new TypeToken<List<TempOldMsgInfo>>() {
                                }.getType();
                                tempOldMsgList = gson.fromJson(object.getString("data"), token);
                                List<MessageInfoBean> tempMessageListForUI = new ArrayList<>();

                                //setup api old message to temp. message list.
                                if (tempOldMsgList != null && tempOldMsgList.size() > 0) {
                                    for (int in = tempOldMsgList.size() - 1; in >= 0; in--) {
                                        MessageInfoBean messageInfoBean = new MessageInfoBean();
                                        messageInfoBean.setMessage(tempOldMsgList.get(in).getText());
                                        //messageInfoBean.setSendTime(CommonUtils.getFormatTimestampStringFreeOld(tempOldMsgList.get(in).getTime()));
                                        messageInfoBean.setSendTime(tempOldMsgList.get(in).getTime());
                                        messageInfoBean.setSender(phoneNo);
                                        messageInfoBean.setReceiveMesg((tempOldMsgList.get(in).getOp_send_flag() == 1 ? true : false));
                                        messageInfoBean.setReadFlag(false); //edit
                                        messageInfoBean.setPhoto(tempOldMsgList.get(in).getMessage_type() == 1 ? true : false);
                                        messageInfoBean.setMsgId(tempOldMsgList.get(in).getMessage_id());
                                        tempMessageListForUI.add(messageInfoBean);
                                    }
                                }

                                //setup old messages to existed message list.
                                if (tempMessageListForUI.size() > 0) {
                                    for (MessageInfoBean messageInfoBean : tempMessageListForUI) {
                                        messageListForUI.add(messageInfoBean);
                                    }
                                }

                                //update rv_view with message list.
                                mMessageAdapter = new MessageListAdapter(getActivity(), messageListForUI, getParentFragment(), phoneNo);
                                mMessageRecycler.setAdapter(mMessageAdapter);
                                mMessageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

                                mMessageAdapter.notifyDataSetChanged();
                                mMessageRecycler.smoothScrollToPosition(messageListForUI.size());
                            }

                            closeDialog(msgReqDialog);
                        } catch (JSONException e) {

                            e.printStackTrace();
                            closeDialog(msgReqDialog);
                        } catch (WebsocketNotConnectedException socketEx) {

                            socketEx.printStackTrace();
                            closeDialog(msgReqDialog);
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                //Log.e("free-chat", "on socket connection @close.");
                closeDialog(msgReqDialog);
            }
        };

        webSocketClientListener = new WebSocketClientListener(getActivity(), listener, BuildConfig.FREE_CHAT_URL);

        try {
            messageSocketClient = webSocketClientListener.connectWebsocket();
        } catch (URISyntaxException e) {
            closeDialog(msgReqDialog);
            e.printStackTrace();
        } catch (Exception ex) {
            closeDialog(msgReqDialog);
            ex.printStackTrace();
        }

        mMessageRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mMessageRecycler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMessageRecycler.scrollToPosition(messageListForUI.size());
                        }
                    }, 100);
                }
            }
        });

        mMessageRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //hide keyboard on tab or scroll the _recycler_view.
        mMessageRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(getActivity());
                return false;
            }
        });

        //add media files to message.
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMediaInputs();
                displayBotMenu();
                hideBotMenuLayout();
                hideSendImg();
                hideAdd();
                hideSendBtn();
                minimizeInputBox();
            }
        });

        //capture image.
        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputBox();
                minimizeInputBox();
                takeImageFromCameraCapture();
            }
        });

        //pick image from app-gallery.
        imgBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputBox();
                minimizeInputBox();
                pickImageFromGallery();
            }
        });

        //button send message.
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (CommonUtils.isNetworkAvailable(getActivity())) {

                        MessageInfoBean message = new MessageInfoBean();

                        if (imgEncoded == null) {
                            messageSocketClient.send("msg:" + textMsg.getText().toString() + "op_send_flag:" + 0 + "message_type:" + TYPE_SEND_TEXT_MSG);
                            messageSocketClient.send("ChangeFinishFlagByMobile:" + phoneNo);
                            message.setMessage(textMsg.getText().toString());
                            message.setPhoto(false);
                            clearInputBox();
                        } else {
                            messageSocketClient.send("msg:" + imgEncoded + "op_send_flag:" + 0 + "message_type:" + TYPE_SEND_IMG_MSG);
                            messageSocketClient.send("ChangeFinishFlagByMobile:" + phoneNo);
                            message.setMessage(imgEncoded);
                            message.setPhoto(true);
                            clearImageEncoded();
                        }

                        message.setSender(phoneNo);
                        message.setSendTime(CommonUtils.getCurTimeString2());
                        messageListForUI.add(message);
                        mMessageAdapter.notifyDataSetChanged();
                        mMessageRecycler.smoothScrollToPosition(messageListForUI.size());

                    } else {
                        showNetworkErrorDialog(getActivity(), getNetErrMsg());
                    }

                } catch (WebsocketNotConnectedException e) {
                    e.printStackTrace();
                    try {
                        messageSocketClient = webSocketClientListener.connectWebsocket();
                    } catch (URISyntaxException e1) {
                        closeDialog(msgReqDialog);
                        e1.printStackTrace();
                    } catch (Exception ex) {
                        closeDialog(msgReqDialog);
                        ex.printStackTrace();
                    }

                } catch (Exception ee) {
                    ee.printStackTrace();
                    try {
                        messageSocketClient = webSocketClientListener.connectWebsocket();
                    } catch (URISyntaxException e) {
                        closeDialog(msgReqDialog);
                        e.printStackTrace();
                    } catch (Exception ex) {
                        closeDialog(msgReqDialog);
                        ex.printStackTrace();
                    }
                }
            }
        });

        imgBotMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMessengerBotMenuLayoutDisplayed()) {
                    displayMediaInputs();
                    displayBotMenu();
                    hideSendImg();
                    hideAdd();
                    hideSendBtn();
                    hideBotMenuLayout();
                } else {
                    hideKeyboard(getActivity());
                    minimizeInputBox();
                    displayBotMenuLayout();
                    displayMediaInputs();
                    displayBotMenu();
                    hideSendImg();
                    hideAdd();
                    hideSendBtn();
                }
                hideMyanmar();
                hideEnglish();
                hideMyanmarLayout2();
                hideEnglishLayout2();
            }
        });

        layoutGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBotMessage("Get Started", MSG_TYPE_GET_STARTED);
            }
        });

        textMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /*if (!textMsg.getText().toString().trim().isEmpty()) {
                    hideBotMenu();
                    displaySendBtn();
                }*/

                displayAdd();
                displayBotMenu();
                hideSendBtn();
                hideBotMenuLayout();
                hideSendImg();
                hideMediaInputs();

                expandInputBox();
                clearImageEncoded();
                return false;
            }
        });

        textMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    setUpViewOnInputClear();
                } else {
                    displayAdd();
                    displaySendBtn();
                    hideBotMenuLayout();
                    hideSendImg();
                    hideMediaInputs();
                    hideBotMenu();
                }
            }
        });

        layoutMyanmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMyanmarLayout2();
                hideMyanmar();
                hideEnglish();
                hideEnglishLayout2();
            }
        });

        layoutEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEnglishLayout2();
                hideMyanmar();
                hideEnglish();
                hideMyanmarLayout2();
            }
        });

        layoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideHome();
                displayGetStarted();
                hideMyanmar();
                hideMyanmarLayout2();
                hideEnglish();
                hideEnglishLayout2();
            }
        });

        lblMMInterestRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBotMessage("အတိုးနှုန်းများ", MSG_TYPE_LANGUAGE);
                receiveBotMessage("AEON အတိုးနှုန်းမှာ (၁.၄) ဖြစ်ပါသည်။");
            }
        });

        lblMMPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBotMessage("ဖုန်းနံပါတ်များ", MSG_TYPE_LANGUAGE);
                receiveBotMessage("၀၉ ၆၉၇၁၂၁၁၁");
                //receiveBotMessage("၀၁ ၅၁၂၃၄၅");
            }
        });

        lblMMOfficeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBotMessage("ရုံးလိပ်စာများ", MSG_TYPE_LANGUAGE);
                receiveBotMessage("အခန်းအမှတ်(၂၁၀)၊ (၂၁၁)၊ (၂၁၂)၊ အဆောင်(ဃ)ဒုတိယထပ်၊ ပုလဲကွန်ဒို၊ ကမ္ဘာအေးဘုရားလမ်း၊ ဗဟန်းမြို့နယ်၊ ရန်ကုန်မြို့ ။");
                //receiveBotMessage("အမှတ်(၇၈၇)၊ ရန်ရှင်းလမ်း၊ ရန်ကင်းမြို့နယ်၊ ရန်ကုန်မြို။");
            }
        });

        lblEnInterestRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBotMessage("Interest Rate", MSG_TYPE_LANGUAGE);
                //receiveBotMessage("AEON interest rate is (1.4).");
            }
        });

        lblEnPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBotMessage("Phone No", MSG_TYPE_LANGUAGE);
                receiveBotMessage("09 9697121116");
                //receiveBotMessage("01 512345");
            }
        });

        lblEnOfficeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBotMessage("Offices' Address", MSG_TYPE_LANGUAGE);
                receiveBotMessage("Units 210, 211, 212, 2nd Floor, Building(D), Pearl Condo, Kabaraye Pagoda, Bahan Township, Yangon.");
                //receiveBotMessage("No.(787), Yanshin Road, Yankin Township, Yangon.");
            }
        });

    }

    void hideMessengerBotMenuLayout() {
        layoutMessengerBotMenu.setVisibility(View.GONE);
    }

    Boolean isMessengerBotMenuLayoutDisplayed() {
        return (layoutMessengerBotMenu.getVisibility() == View.VISIBLE);
    }

    void displaySendBtn() {
        sendBtn.setVisibility(View.VISIBLE);
    }

    void hideSendBtn() {
        sendBtn.setVisibility(View.GONE);
    }

    void clearInputBox() {
        textMsg.setText(BLANK);
    }

    void minimizeInputBox() {
        textMsg.setMaxLines(1);
    }

    void expandInputBox() {
        textMsg.setMaxLines(4);
    }

    void setImageEncoded(String encodedString) {
        imgEncoded = encodedString;
    }

    void clearImageEncoded() {
        if (imgEncoded == null) {
            displayMessage(getActivity(), "Image already cleared.");
        } else {
            imgEncoded = null;
            displayMessage(getActivity(), "Image cleared.");
        }
        hideSendImg();
    }

    /*Boolean isImageEncodedExisted() {
        if (imgEncoded == null) {
            return false;
        }
        return true;
    }*/

    void setUpInitView() {
        displayMediaInputs();
        displayBotMenu();
        hideSendBtn();
        hideBotMenuLayout();
        hideAdd();
        hideSendImg();

        clearInputBox();
        clearImageEncoded();

        //blurLayoutChatInput();
    }

    void setUpViewOnInputClear() {
        displayMediaInputs();
        displayBotMenu();
        hideSendBtn();
        hideBotMenuLayout();
        hideAdd();
        hideSendImg();
        clearImageEncoded();
    }

    void displayMediaInputs() {
        displayCamera();
        displayGallery();
    }

    void hideMediaInputs() {
        hideCamera();
        hideGallery();
    }

    /**
     * ____________msgr helpers.
     */
    void sendBotMessage(String msg, int botType) {
        //This is just testing.
        try {

            if (CommonUtils.isNetworkAvailable(getActivity())) {

                MessageInfoBean message = new MessageInfoBean();

                messageSocketClient.send("msg:" + msg + "op_send_flag:" + 0 + "message_type:" + TYPE_SEND_TEXT_MSG);
                messageSocketClient.send("ChangeFinishFlagByMobile:" + phoneNo);
                message.setMessage(msg);
                message.setPhoto(false);

                message.setSender(phoneNo);
                message.setSendTime(CommonUtils.getCurTimeString2());
                messageListForUI.add(message);
                mMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.smoothScrollToPosition(messageListForUI.size());

                if (botType == MSG_TYPE_GET_STARTED) {
                    displayHome();
                    displayMyanmar();
                    displayEnglish();
                    hideGetStarted();
                }

            } else {
                showNetworkErrorDialog(getActivity(), getNetErrMsg());
            }

        } catch (WebsocketNotConnectedException e) {
            e.printStackTrace();
            try {
                messageSocketClient = webSocketClientListener.connectWebsocket();
            } catch (URISyntaxException e1) {
                closeDialog(msgReqDialog);
                e1.printStackTrace();
            } catch (Exception ex) {
                closeDialog(msgReqDialog);
                ex.printStackTrace();
            }

        } catch (Exception ee) {
            ee.printStackTrace();
            try {
                messageSocketClient = webSocketClientListener.connectWebsocket();
            } catch (URISyntaxException e) {
                closeDialog(msgReqDialog);
                e.printStackTrace();
            } catch (Exception ex) {
                closeDialog(msgReqDialog);
                ex.printStackTrace();
            }
        }
    }

    void receiveBotMessage(final String msg) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MessageInfoBean message = new MessageInfoBean();
                message.setMessage(msg);
                message.setPhoto(false);
                message.setReceiveMesg(true);
                message.setSendTime(CommonUtils.getCurTimeString2());
                messageListForUI.add(message);
                mMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.smoothScrollToPosition(messageListForUI.size());
            }
        }, 800);
    }


    /**
     * _____________ui helpers.
     */
    void displayCamera() {
        imgCapture.setVisibility(View.VISIBLE);
    }

    void hideCamera() {
        imgCapture.setVisibility(View.GONE);
    }

    void displayGallery() {
        imgBrowse.setVisibility(View.VISIBLE);
    }

    void hideGallery() {
        imgBrowse.setVisibility(View.GONE);
    }

    void displayAdd() {
        imgAdd.setVisibility(View.VISIBLE);
    }

    void hideAdd() {
        imgAdd.setVisibility(View.GONE);
    }

    void displayBotMenu() {
        imgBotMenu.setVisibility(View.VISIBLE);
    }

    void hideBotMenu() {
        imgBotMenu.setVisibility(View.GONE);
    }

    void displayBotMenuLayout() {
        //layoutMessengerBotMenu.setAnimation(animSlideToUp(getActivity()));
        layoutMessengerBotMenu.setVisibility(View.VISIBLE);
        hideHome();
        displayGetStarted();
    }

    void hideBotMenuLayout() {
        layoutMessengerBotMenu.setAnimation(animSlideToLeft(getActivity()));
        layoutMessengerBotMenu.setVisibility(View.GONE);
    }

    void displayHome() {
        layoutHome.setAnimation(animSlideToLeft(getContext()));
        layoutHome.setVisibility(View.VISIBLE);
    }

    void hideHome() {
        layoutHome.setVisibility(View.GONE);
    }

    void displayGetStarted() {
        layoutGetStarted.setAnimation(animSlideToLeft(getContext()));
        layoutGetStarted.setVisibility(View.VISIBLE);
    }

    void hideGetStarted() {
        layoutGetStarted.setVisibility(View.GONE);
    }

    void displaySendImg() {
        imgSend.setVisibility(View.VISIBLE);
    }

    void hideSendImg() {
        imgSend.setVisibility(View.GONE);
    }

    void displayMyanmar() {
        layoutMyanmar.setAnimation(animSlideToLeft(getContext()));
        layoutMyanmar.setVisibility(View.VISIBLE);
    }

    void hideMyanmar() {
        //layoutMyanmar.setAnimation(animSlideToLeft(getContext()));
        layoutMyanmar.setVisibility(View.GONE);
    }

    void displayEnglish() {
        layoutEnglish.setAnimation(animSlideToLeft(getContext()));
        layoutEnglish.setVisibility(View.VISIBLE);
    }

    void hideEnglish() {
        //layoutEnglish.setAnimation(animSlideToLeft(getContext()));
        layoutEnglish.setVisibility(View.GONE);
    }

    void displayMyanmarLayout2() {
        layoutMyanmar2.setAnimation(animSlideToLeft(getContext()));
        layoutMyanmar2.setVisibility(View.VISIBLE);
    }

    void hideMyanmarLayout2() {
        //layoutMyanmar2.setAnimation(animSlideToLeft(getContext()));
        layoutMyanmar2.setVisibility(View.GONE);
    }

    void displayEnglishLayout2() {
        layoutEnglish2.setAnimation(animSlideToLeft(getContext()));
        layoutEnglish2.setVisibility(View.VISIBLE);
    }

    void hideEnglishLayout2() {
        //layoutEnglish2.setAnimation(animSlideToLeft(getContext()));
        layoutEnglish2.setVisibility(View.GONE);
    }

}

