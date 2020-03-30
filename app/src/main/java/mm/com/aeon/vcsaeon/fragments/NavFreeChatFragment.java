package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import mm.com.aeon.vcsaeon.BuildConfig;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.adapters.MessageListAdapter;
import mm.com.aeon.vcsaeon.beans.MessageInfoBean;
import mm.com.aeon.vcsaeon.beans.TempOldMsgInfo;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.WebSocketClientListener;

import static android.app.Activity.RESULT_OK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.AUTO_MESSAGE_REPLY;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CAMERA_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MESSAGE_DISPLAY_COUNT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STORAGE_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.decodeStringToBitmap;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class NavFreeChatFragment extends BaseFragment implements LanguageChangeListener {

    private static final int RECORD_REQUEST_CODE = 320;

    View view;

    public static Button sendBtn;
    private ImageView imgBrowse;
    private ImageView imgCapture;
    private ImageView imgSend;
    private ImageView imgAdd;
    public static EditText textMsg;

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

    public static WebSocketClient mesgSocketClient;

    private String mCurrentPhotoPath;
    private static String imgEncoded;

    final int RESULT_LOAD_IMAGE = 1;
    final int REQUEST_TAKE_PHOTO = 2;

    static String phoneNo;
    static int msgId;
    String senderId;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.free_chat_fragment, container, false);
        setHasOptionsMenu(true);
        Log.e("Free chat fragment", "onCreateView()");

        layoutLoading = view.findViewById(R.id.layout_loading);
        imgLoading = view.findViewById(R.id.img_loading);

        PreferencesManager.setSocketFlagClose(getActivity(), false);

        // add listener
        ((MainActivity) getActivity()).setLanguageListener(this);
        Toolbar toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar_main_home);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        TextView menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarLevelInfo.setText(R.string.menu_level_one);
        menuBackBtn.setVisibility(View.VISIBLE);

        imgBrowse = view.findViewById(R.id.img_browse);
        imgCapture = view.findViewById(R.id.img_capture);
        imgSend = view.findViewById(R.id.img_send);
        imgAdd = view.findViewById(R.id.img_add);

        textMsg = view.findViewById(R.id.edittext_chatbox);
        sendBtn = view.findViewById(R.id.button_chatbox_send);

        textPhoneNo = view.findViewById(R.id.admin_phone);
        textPhoneNo.setText(PreferencesManager.getHotlinePhone(getContext()));
        btnAdminCall = view.findViewById(R.id.btn_admin_call);
        btnAdminCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permission = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    makeCallRequest();
                } else {
                    final String hotlinePhoneNo = PreferencesManager.getHotlinePhone(getContext());
                    if (hotlinePhoneNo == null || hotlinePhoneNo.equals(BLANK)) {
                        showCallMessage();
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX + hotlinePhoneNo));
                        startActivity(callIntent);
                    }
                }
            }
        });

        final String curLang = PreferencesManager.getCurrentLanguage(getActivity());
        changeLabel(curLang);

        msgReqDialog = new ProgressDialog(getActivity());
        msgReqDialog.setMessage(getString(R.string.progress_loading));
        msgReqDialog.setCancelable(false);
        msgReqDialog.show();

        allMessageList = new ArrayList<>();
        messageListForUI = new ArrayList<>();

        final MainActivity mainActivity = (MainActivity) getActivity();

        String freeChatPhone = PreferencesManager.getInstallPhoneNo(getContext()).trim();
        phoneNo = freeChatPhone;

        //setFreMessageRoom();
        senderId = PreferencesManager.getCurrentFreeChatId(getContext());
        Log.e("Sender Id", String.valueOf(senderId));

        final WebSocketClientListener.SocketListiner listener = new WebSocketClientListener.SocketListiner() {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.e("FreeChatTabFragment", "onOpen()." + phoneNo +" : "+ senderId);
                mesgSocketClient.send("userName:" + phoneNo + "userId:" + senderId);
                mesgSocketClient.send("cr:" + phoneNo + "or:" + "userWithAgency:");
            }

            @Override
            public void onMessage(final String message) {
                Log.e("FreeChatTabFragment", "onMessage()");
                final String messageStr = message;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject object = new JSONObject(messageStr);
                            String type = object.getString("type");

                            //if socket notified message is coming
                            if (type.equals("message")) {
                                Log.e("FreeChatTabFragment", "type-message");

                                JSONObject messageObj = object.getJSONObject("data");
                                String sendFlag = messageObj.getString("op_send_flag");
                                String messageType = messageObj.getString("message_type");

                                // if message send from operator, add message to FreeChat UI
                                if (sendFlag.equals("1")) {

                                    //mainActivity.unReadMessageCount.setText("0");

                                    MessageInfoBean message = new MessageInfoBean();
                                    message.setSender("A");
                                    message.setMessage(messageObj.getString("text"));
                                    message.setReceiveMesg(true);
                                    message.setSendTime(CommonUtils.getCurTimeString());

                                    messageListForUI.add(message);
                                    mMessageAdapter.notifyDataSetChanged();
                                    mMessageRecycler.smoothScrollToPosition(messageListForUI.size());

                                    if ((mesgSocketClient != null)) {

                                        if ((!mesgSocketClient.isClosed())) {
                                            mesgSocketClient.send("ChangeReadFlagWithMsgId:" + messageObj.getString("message_id"));
                                           /* String messageCountStr = mainMenuActivity.unReadMessageCount.getText().toString();
                                            if (messageCountStr != null && !messageCountStr.equals(BLANK)) {
                                                int messageCount = Integer.parseInt(messageCountStr);
                                                if (messageCount == 0) {
                                                    mainMenuActivity.unReadMessageCount.setVisibility(View.GONE);
                                                } else {
                                                    mainMenuActivity.unReadMessageCount.setText(String.valueOf(--messageCount));
                                                    if (messageCount == 0) {
                                                        mainMenuActivity.unReadMessageCount.setVisibility(View.GONE);
                                                    }
                                                }
                                            }*/
                                        }

                                    }
                                }
                            }

                            // if socket is created room on server, get message List
                            else if (type.equals("room")) {
                                Log.e("FreeChatTabFragment", "type-room");

                                SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(getActivity());
                                String initMessageReply = PreferencesManager.getStringEntryFromPreferences(preferences,AUTO_MESSAGE_REPLY);

                                MessageInfoBean messageInfoBean = new MessageInfoBean();
                                messageInfoBean.setMessage(initMessageReply);
                                messageInfoBean.setIntro(true);

                                messageListForUI.add(messageInfoBean);
                                mMessageRecycler = view.findViewById(R.id.recyclerview_message_list);
                                mMessageAdapter = new MessageListAdapter(getActivity(), messageListForUI, getParentFragment(), phoneNo);
                                mMessageRecycler.setAdapter(mMessageAdapter);
                                mMessageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mMessageAdapter.notifyDataSetChanged();
                                mMessageRecycler.smoothScrollToPosition(messageListForUI.size());

                            }

                            // if message list is got from server, display on FreeChat UI
                            else if (type.equals("messageListData")) {

                                Log.e("FreeChatTabFragment", "type-messageListData");

                                MessageInfoBean messageInfoBean = new MessageInfoBean();
                                messageInfoBean.setMessage(getString(R.string.free_chat_introduction));
                                messageInfoBean.setIntro(true);

                                allMessageList.add(messageInfoBean);
                                mMessageRecycler = view.findViewById(R.id.reyclerview_message_list);
                                mMessageAdapter = new MessageListAdapter(getActivity(), messageListForUI, getParentFragment(), phoneNo);
                                mMessageRecycler.setAdapter(mMessageAdapter);
                                mMessageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mMessageAdapter.notifyDataSetChanged();
                                mMessageRecycler.smoothScrollToPosition(messageListForUI.size());


                            }
                            else if (type.equals("mobileOldMessage")) {

                                Log.e("FreeChatTabFragment", "type-mobileOldMessage");

                                Gson gson = new Gson();
                                Type token = new TypeToken<List<TempOldMsgInfo>>() {
                                }.getType();
                                tempOldMsgList = gson.fromJson(object.getString("data"), token);
                                List<MessageInfoBean> tempMessageListForUI = new ArrayList<>();

                                //remove read-more button.
                                messageListForUI.remove(0);

                                //set red-more button if more message exist.
                                if (tempOldMsgList.size() >= MESSAGE_DISPLAY_COUNT) {
                                    MessageInfoBean infoBean = new MessageInfoBean();
                                    infoBean.setButton(true);
                                    tempMessageListForUI.add(infoBean);
                                }

                                if (tempOldMsgList != null && tempOldMsgList.size() > 0) {
                                    for (int in = tempOldMsgList.size() - 1; in >= 0; in--) {
                                        MessageInfoBean messageInfoBean = new MessageInfoBean();
                                        messageInfoBean.setMessage(tempOldMsgList.get(in).getText());
                                        messageInfoBean.setSendTime(CommonUtils.getFormatTimestampStringOld(tempOldMsgList.get(in).getTime())); //edit
                                        messageInfoBean.setSender(phoneNo);
                                        messageInfoBean.setReceiveMesg((tempOldMsgList.get(in).getOp_send_flag() == 1 ? true : false));
                                        messageInfoBean.setReadFlag(false); //edit
                                        messageInfoBean.setPhoto(tempOldMsgList.get(in).getMessage_type() == 1 ? true : false);
                                        messageInfoBean.setMsgId(tempOldMsgList.get(in).getMessage_id());
                                        tempMessageListForUI.add(messageInfoBean);
                                    }
                                }

                                //add existed displaying message.
                                for (MessageInfoBean messageInfoBean : messageListForUI) {
                                    tempMessageListForUI.add(messageInfoBean);
                                }

                                //clear old-displayed message.
                                messageListForUI.clear();


                                for (MessageInfoBean messageInfoBean : tempMessageListForUI) {
                                    messageListForUI.add(messageInfoBean);
                                }

                                if (messageListForUI.size() > 2) {
                                    msgId = messageListForUI.get(1).getMsgId();
                                }

                                mMessageAdapter.notifyDataSetChanged();
                                mMessageRecycler.smoothScrollToPosition(tempOldMsgList.size());

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
                Log.e("FreeChatTabFragment", "onClose()");
                closeDialog(msgReqDialog);
            }
        };

        webSocketClientListener = new WebSocketClientListener(getActivity(), listener, BuildConfig.FREE_CHAT_URL);

        try {
            Log.e("FreeChatTabFragment", "connectWebsocket()");
            mesgSocketClient = webSocketClientListener.connectWebsocket();
        } catch (URISyntaxException e) {
            closeDialog(msgReqDialog);
            e.printStackTrace();
        } catch (Exception ex) {
            closeDialog(msgReqDialog);
            ex.printStackTrace();
        }

        mMessageRecycler = view.findViewById(R.id.recyclerview_message_list);
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

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAdd.setVisibility(View.GONE);
                imgBrowse.setVisibility(View.VISIBLE);
                imgCapture.setVisibility(View.VISIBLE);
                textMsg.setText(BLANK);
            }
        });

        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //camera for capture image.
                textMsg.setText(BLANK);
                if (isCameraAllowed()) {
                    openCamera(senderId);
                }
            }
        });

        imgBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMsg.setText(BLANK);
                //browse image.
                if (isStorageAllowed()) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
            }
        });

        textMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (imgAdd.getVisibility() == View.GONE) {
                    //Toast.makeText(getActivity(),"Image Info Cleared.",Toast.LENGTH_SHORT).show();
                    displayMessage(getActivity(), "Image Info Cleared.");
                }
                imgAdd.setVisibility(View.VISIBLE);
                imgBrowse.setVisibility(View.GONE);
                imgCapture.setVisibility(View.GONE);
                imgEncoded = null;
                imgSend.setVisibility(View.GONE);
                return false;
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CommonUtils.isNetworkAvailable(getActivity())) {

                        MessageInfoBean message = new MessageInfoBean();
                        final String textSendMessage = textMsg.getText().toString();

                        if (textSendMessage.isEmpty() && imgEncoded == null) {

                            //Toast.makeText(getActivity(),"Text or Image is required to send!", Toast.LENGTH_SHORT).show();
                            displayMessage(getActivity(), "Text or Image is required to send!");

                        } else if (!textSendMessage.isEmpty()) {

                            mesgSocketClient.send("msg:" + textMsg.getText().toString() + "op_send_flag:" + 0 + "message_type:" + 0);
                            mesgSocketClient.send("ChangeFinishFlagByMobile:" + phoneNo);

                            message.setMessage(textMsg.getText().toString());
                            message.setSender(phoneNo);
                            message.setSendTime(CommonUtils.getCurTimeString());
                            messageListForUI.add(message);
                            mMessageAdapter.notifyDataSetChanged();
                            mMessageRecycler.smoothScrollToPosition(messageListForUI.size());
                            Log.e("FreeChatTabFragment", "A Text Message is sent.");

                        } else {

                            mesgSocketClient.send("msg:" + imgEncoded + "op_send_flag:" + 0 + "message_type:" + 1);
                            mesgSocketClient.send("ChangeFinishFlagByMobile:" + phoneNo);

                            message.setMessage(imgEncoded);
                            message.setPhoto(true);
                            message.setSender(phoneNo);
                            message.setSendTime(CommonUtils.getCurTimeString());
                            messageListForUI.add(message);
                            mMessageAdapter.notifyDataSetChanged();
                            mMessageRecycler.smoothScrollToPosition(messageListForUI.size());
                            Log.e("FreeChatTabFragment", "An Image is sent.");

                        }

                        textMsg.setText(BLANK);
                        imgEncoded = null;
                        imgSend.setVisibility(View.GONE);

                    } else {
                        showNetworkErrorDialog(getActivity(), getNetErrMsg());
                    }

                } catch (WebsocketNotConnectedException e) {
                    e.printStackTrace();
                    try {
                        Log.e("FreeChatTabFragment", "re-connectWebsocket()");
                        mesgSocketClient = webSocketClientListener.connectWebsocket();
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
                        Log.e("FreeChatTabFragment", "re-connectWebsocket()");
                        mesgSocketClient = webSocketClientListener.connectWebsocket();
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
            //Toast.makeText(getActivity(),"Image cannot be created.",Toast.LENGTH_SHORT).show();
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
                //this.languageFlag = item;
                Log.e("update flag", item.getTitle().toString());
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
        Log.e("FreeChatTabFragment", "onResume()");
        if (mesgSocketClient.isClosed()) {
            try {
                Log.e("FreeChatTabFragment", "connectWebsocket()-onResume()");
                mesgSocketClient = webSocketClientListener.connectWebsocket();
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
        Log.e("FreeChatTabFragment", "onPause()");

        /*if(!mesgSocketClient.isClosed()){
            mesgSocketClient.close();
            Log.e("FreeChatTabFragment","mesgSocketClient.close()-onPause()");
        }*/

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("FreeChatTabFragment", "onStart()");

        if (imgEncoded == null) {
            Log.e("FreeChatTabFragment", "No Image Information.");
        } else {
            imgAdd.setVisibility(View.GONE);
            imgBrowse.setVisibility(View.VISIBLE);
            imgSend.setVisibility(View.VISIBLE);
            imgCapture.setVisibility(View.VISIBLE);
            imgSend.setImageBitmap(decodeStringToBitmap(imgEncoded));
        }

        /*if(!mesgSocketClient.isClosed()){
            mesgSocketClient.close();
            Log.e("FreeChatTabFragment","mesgSocketClient.close()-onStart()");
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FreeChatTabFragment", "onDestroy()");
        if (!mesgSocketClient.isClosed()) {
            mesgSocketClient.close();
            Log.e("FreeChatTabFragment", "mesgSocketClient.close()-onDestroy()");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("FreeChatTabFragment", "onActivityResult()");

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
            Log.e("onActivityResult", RESULT_LOAD_IMAGE+"");

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
            Log.e("onActivityResult", REQUEST_TAKE_PHOTO+"");

        }
    }

    //Change languages.
    public void changeLabel(String language) {
        sendBtn.setText(CommonUtils.getLocaleString(new Locale(language), R.string.btn_send, getActivity()));
        textMsg.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.hint_msg, getActivity()));
    }

    //Check phone storage permission allowed or not.
    public boolean isStorageAllowed() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    //Check camera permission allowed or not.
    public boolean isCameraAllowed() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static int getMsgId() {
        return msgId;
    }

    public static int startIndexForListForUI() {
        int cursor = allMessageList.size() - (messageListForUI.size() - 1);
        return cursor;
    }

    public void showCallMessage() {
        //Toast.makeText(getActivity(),"Call not available!", Toast.LENGTH_SHORT).show();
        displayMessage(getActivity(), "Call not available!");
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
            //Toast.makeText(getActivity(),"Image File could not be created.2\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
            displayMessage(getActivity(), "Image File could not be created.2\n" + e.getMessage());
            return null;
        }
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    private void makeCallRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CALL_PHONE},
                RECORD_REQUEST_CODE);
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        mesgSocketClient.close();
        replaceFragment(new NewMainPageFragment(), R.id.content_new_main_drawer);
    }
}
