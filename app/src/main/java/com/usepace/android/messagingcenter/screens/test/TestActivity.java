package com.usepace.android.messagingcenter.screens.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sendbird.android.SendBird;
import com.usepace.android.messagingcenter.clients.connection_client.MessageCenter;
import com.usepace.android.messagingcenter.exceptions.MessageCenterException;
import com.usepace.android.messagingcenter.interfaces.ConnectionInterface;
import com.usepace.android.messagingcenter.interfaces.OnCallButtonClickedResult;
import com.usepace.android.messagingcenter.interfaces.OpenChatViewInterface;
import com.usepace.android.messagingcenter.interfaces.SdkCallbacks;
import com.usepace.android.messagingcenter.model.ConnectionRequest;
import com.usepace.android.messagingcenter.model.Theme;
import com.usepace.android.messagingcenter.utils.DeviceUtils;

import java.util.Map;

public class TestActivity extends AppCompatActivity{

    //private String chat_id = "sendbird1_group_channel_2456028_f4a5055d72e15074e5832cd3d60d5fa662980e84";
    private String chat_id = "sendbird_group_channel_4291064_038de356412d5789e2cd2232f4f55f595e35728d";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout fragContainer = new LinearLayout(this);
        fragContainer.setOrientation(LinearLayout.HORIZONTAL);

        Button btn = new Button(this);
        btn.setText("ClickMe");

        fragContainer.addView(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TestActivity","hello");



                MessageCenter.connect(TestActivity.this, prepareCustomerRequest(), new ConnectionInterface() {
                    @Override
                    public void onMessageCenterConnected() {
                        MessageCenter.openChatView(TestActivity.this, null, chat_id, new Theme("Sample_app", "Sample App", "#122333 🤓", "Hello and welcome 👀", true), new OpenChatViewInterface() {
                            @Override
                            public void onViewWillStart() {

                            }

                            @Override
                            public void onError(MessageCenterException messageCenterException) {

                            }
                        }, new SdkCallbacks() {
                            @Override
                            public void onCallButtonClicked(final OnCallButtonClickedResult onCallButtonClickedResult) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        onCallButtonClickedResult.onSuccess("041222222");
                                    }
                                }, 5000);
                            }
                            @Override
                            public void onEvent(String app_name, String event_key, Map<String, Object> data) {

                                SendBird.reconnect();
                            }
                        });
                    }

                    @Override
                    public void onMessageCenterConnectionError(int code, MessageCenterException e) {

                    }
                });

            }
        });

        setContentView(fragContainer);


    }




    private ConnectionRequest prepareCustomerRequest() {
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setAppId("FE3AD311-7F0F-4E7E-9E22-25FF141A37C0");
        connectionRequest.setClient(MessageCenter.CLIENT_SENDBIRD);
        connectionRequest.setUserId("customer_swyft_85");
        connectionRequest.setAccessToken("c0af86a2df17e4e8aebc7d05a0875d0c9d596eee");
        connectionRequest.setFcmToken("testo");
        return connectionRequest;
    }



    private ConnectionRequest prepareCustomerRequest1() {
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setAppId("08CF9E26-0EA6-43C7-920E-2238DA08D2E1");
        connectionRequest.setClient(MessageCenter.CLIENT_SENDBIRD);
        connectionRequest.setUserId("rider_sony");
        connectionRequest.setAccessToken("56baea33529cf4b380a56ae52b052e4be9c903f7");
        connectionRequest.setFcmToken("testo");
        return connectionRequest;
    }




    private ConnectionRequest prepareDriverRequest() {
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setAppId("FE3AD311-7F0F-4E7E-9E22-25FF141A37C0");
        connectionRequest.setClient(MessageCenter.CLIENT_SENDBIRD);
        connectionRequest.setUserId("customer_hs_184890");
        connectionRequest.setAccessToken("8b21b79c6a07d74e95cf6c91837ec2a64e9cbc54");
        connectionRequest.setFcmToken("testo");
        return connectionRequest;
    }
}
