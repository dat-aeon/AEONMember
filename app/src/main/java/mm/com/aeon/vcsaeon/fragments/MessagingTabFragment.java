package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
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
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.MessageInfoBean;
import mm.com.aeon.vcsaeon.beans.TempOldMsgInfo;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.adapters.MessageListAdapter;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.common_utils.UiUtils;
import mm.com.aeon.vcsaeon.delegates.AccessPermissionResultDelegate;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.WebSocketClientListener;

import static android.app.Activity.RESULT_OK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CALL_REQUEST_CODE;
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

public class MessagingTabFragment extends BaseFragment implements LanguageChangeListener, AccessPermissionResultDelegate {

    View view;

    public static Button sendBtn;
    private ImageView imgBrowse;
    private ImageView imgCapture;
    private ImageView imgSend;
    private ImageView imgAdd;
    public static EditText textMsg;

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

    UserInformationFormBean userInformationFormBean;

    private String mCurrentPhotoPath;
    private static String imgEncoded;

    final int RESULT_LOAD_IMAGE = 1;
    final int REQUEST_TAKE_PHOTO = 1;

    static String phoneNo;
    static int msgId;

    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.messaging_tab, container, false);
        setHasOptionsMenu(true);

        PreferencesManager.setSocketFlagClose(getActivity(), false);

        // close button listener
        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
        ((MainMenuActivityDrawer) getActivity()).setAccessDelegate(this);

        if (PreferencesManager.getMainNavFlag(getContext())) {
            ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
            toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_main_home);
        } else {
            ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
            toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        }
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setAnimation(UiUtils.animSlideToRight(getActivity()));
        menuBackBtn.setVisibility(View.VISIBLE);

        imgBrowse = view.findViewById(R.id.img_browse);
        imgCapture = view.findViewById(R.id.img_capture);
        imgSend = view.findViewById(R.id.img_send);
        imgAdd = view.findViewById(R.id.img_add);

        textMsg = view.findViewById(R.id.edittext_chatbox);
        sendBtn = view.findViewById(R.id.button_chatbox_send);

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        textPhoneNo = view.findViewById(R.id.admin_phone);
        textPhoneNo.setText(userInformationFormBean.getHotlinePhone());
        btnAdminCall = view.findViewById(R.id.btn_admin_call);
        btnAdminCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    makeCallRequest();
                } else {
                    final String hotlinePhoneNo = userInformationFormBean.getHotlinePhone();
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
        if (curLang.equals(LANG_MM)) {
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        msgReqDialog = new ProgressDialog(getActivity());
        msgReqDialog.setMessage(getString(R.string.progress_loading_message));
        msgReqDialog.setCancelable(false);
        msgReqDialog.show();

        allMessageList = new ArrayList<>();
        messageListForUI = new ArrayList<>();

        phoneNo = userInformationFormBean.getPhoneNo();
        final int senderId = userInformationFormBean.getCustomerId();

        final WebSocketClientListener.SocketListiner listener = new WebSocketClientListener.SocketListiner() {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                mesgSocketClient.send("userName:" + phoneNo + "userId:" + senderId);
                mesgSocketClient.send("cr:" + phoneNo + "or:" + "userWithAgency:");
            }

            @Override
            public void onMessage(final String message) {
                final String messageStr = message;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(messageStr);
                            String type = object.getString("type");

                            //if socket notified message is coming
                            if (type.equals("message")) {
                                JSONObject messageObj = object.getJSONObject("data");
                                String sendFlag = messageObj.getString("op_send_flag");
                                String messageType = messageObj.getString("message_type");

                                // if message send from operator, add message to messaging UI
                                if (sendFlag.equals("1")) {

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
                                        }
                                    }
                                }
                            }

                            // if socket is created room on server, get message List
                            else if (type.equals("room")) {
                                if (messageListForUI.size() == 0) {
                                    mesgSocketClient.send("messageList:");
                                } else {
                                    mesgSocketClient.send("unReadMessageList:");
                                }
                            }

                            // if message list is got from server, display on messaging UI
                            else if (type.equals("messageListData")) {
                                mesgSocketClient.send("unReadMesgCount:");
                                List<Integer> unReadMessageList = new ArrayList<Integer>();
                                JSONArray arr = object.getJSONArray("data");

                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        MessageInfoBean messageInfoBean = new MessageInfoBean();
                                        messageInfoBean.setMessage(arr.getJSONObject(i).getString("messageContent"));
                                        messageInfoBean.setSendTime(CommonUtils.getFormatTimestampString(arr.getJSONObject(i).getString("sendTime")));
                                        messageInfoBean.setSender(phoneNo);
                                        messageInfoBean.setReceiveMesg((arr.getJSONObject(i).getInt("opSendFlag") == 1 ? true : false));
                                        messageInfoBean.setReadFlag(arr.getJSONObject(i).getInt("readFlag") == 1 ? true : false);
                                        messageInfoBean.setPhoto(arr.getJSONObject(i).getInt("messageType") == 1 ? true : false);
                                        messageInfoBean.setMsgId(arr.getJSONObject(i).getInt("messageId"));

                                        if (!messageInfoBean.isReadFlag() && messageInfoBean.isReceiveMesg()) {
                                            unReadMessageList.add(arr.getJSONObject(i).getInt("messageId"));
                                        }
                                        allMessageList.add(messageInfoBean);
                                    }
                                }

                                // if message list is exceed 25 messages, show more message button
                                if (allMessageList.size() >= MESSAGE_DISPLAY_COUNT) {
                                    if (arr.length() >= MESSAGE_DISPLAY_COUNT) {
                                        MessageInfoBean infoBean = new MessageInfoBean();
                                        infoBean.setButton(true);
                                        messageListForUI.add(infoBean);
                                    }
                                    for (int i = MESSAGE_DISPLAY_COUNT - 1; i >= 0; i--) {
                                        messageListForUI.add(allMessageList.get(i));
                                    }
                                } else {
                                    for (int i = allMessageList.size() - 1; i >= 0; i--) {
                                        messageListForUI.add(allMessageList.get(i));
                                    }
                                }

                                if (messageListForUI.size() > 2) {
                                    msgId = messageListForUI.get(1).getMsgId();
                                }

                                mMessageRecycler = view.findViewById(R.id.reyclerview_message_list);
                                mMessageAdapter = new MessageListAdapter(getActivity(), messageListForUI, getParentFragment(), phoneNo);
                                mMessageRecycler.setAdapter(mMessageAdapter);
                                mMessageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mMessageAdapter.notifyDataSetChanged();
                                mMessageRecycler.smoothScrollToPosition(messageListForUI.size());

                                if (unReadMessageList.size() > 0) {
                                    for (int messageId : unReadMessageList) {
                                        mesgSocketClient.send("ChangeReadFlagWithMsgId:" + messageId);
                                    }
                                }

                            } else if (type.equals("mobileOldMessage")) {

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
                closeDialog(msgReqDialog);
            }
        };
        webSocketClientListener = new WebSocketClientListener(getActivity(), listener, BuildConfig.WEB_SOCKET_URL);

        try {
            mesgSocketClient = webSocketClientListener.connectWebsocket();
        } catch (URISyntaxException e) {
            closeDialog(msgReqDialog);
            e.printStackTrace();
        } catch (Exception ex) {
            closeDialog(msgReqDialog);
            ex.printStackTrace();
        }

        mMessageRecycler = view.findViewById(R.id.reyclerview_message_list);
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
                        }

                        textMsg.setText(BLANK);
                        imgEncoded = null;
                        imgSend.setVisibility(View.GONE);

                    } else {
                        showNetworkErrorDialog(getActivity(), getNetErrMsg());
                    }

                } catch (WebsocketNotConnectedException e) {
                    e.printStackTrace();
                } catch (Exception ee) {
                    ee.printStackTrace();
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

        if (mesgSocketClient.isClosed()) {
            try {
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
    }

    @Override
    public void onStart() {
        super.onStart();

        if (imgEncoded == null) {
            //do something.
        } else {
            imgAdd.setVisibility(View.GONE);
            imgBrowse.setVisibility(View.VISIBLE);
            imgSend.setVisibility(View.VISIBLE);
            imgCapture.setVisibility(View.VISIBLE);
            imgSend.setImageBitmap(decodeStringToBitmap(imgEncoded));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mesgSocketClient.isClosed()) {
            mesgSocketClient.close();
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

    //Create Image File and Capture.
    public void openCamera(int senderId) {
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

    public static int getMsgId() {
        return msgId;
    }

    public static int startIndexForListForUI() {
        int cursor = allMessageList.size() - (messageListForUI.size() - 1);
        return cursor;
    }

    public void showCallMessage() {
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
                CALL_REQUEST_CODE);
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        mesgSocketClient.close();
        replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
    }

    @Override
    public void onAccessRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    showSnackBarMessage(getString(R.string.message_permission_deniled));
                } else {
                    openCamera(userInformationFormBean.getCustomerId());
                }
                return;
            }

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    showSnackBarMessage(getString(R.string.message_permission_deniled));

                } else {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
                return;
            }

            //Call Service.
            case CALL_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    showSnackBarMessage(getString(R.string.message_permission_deniled));
                } else {
                    final String hotlinePhoneNo = textPhoneNo.getText().toString();
                    if (hotlinePhoneNo == null || hotlinePhoneNo.equals(BLANK)) {
                        showSnackBarMessage(getString(R.string.message_call_not_available));
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX + hotlinePhoneNo));
                        startActivity(callIntent);
                    }
                }
                return;
            }
        }
    }

    protected void showSnackBarMessage(String message) {
        final Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.pyidaungsu_regular));
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

}
