package com.usepace.android.messagingcenter.screens.sendbird;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.sendbird.android.SendBird;
import com.usepace.android.messagingcenter.R;
import com.usepace.android.messagingcenter.clients.connection_client.MessageCenter;
import com.usepace.android.messagingcenter.model.Theme;
import com.usepace.android.messagingcenter.utils.PreferenceUtils;


public class SendBirdChatActivity extends AppCompatActivity{


    private onBackPressedListener mOnBackPressedListener;
    private Toolbar toolbar;
    private TextView toolbarSubtitle;
    private Theme theme;
    private Menu menu;
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);
        init();
    }

    private void init() {
        theme = getIntent().hasExtra("THEME") ? (Theme) getIntent().getExtras().getParcelable("THEME") : null;
        SendBird.setAutoBackgroundDetection(true);
        PreferenceUtils.init(this);
        initToolBar();
        String channelUrl = getIntent().getStringExtra("CHANNEL_URL");

        if(channelUrl != null) {
            MessageCenter.clearNotificationInboxMessages(channelUrl);
            Fragment fragment = SendBirdChatFragment.newInstance(channelUrl);
            Bundle bundle = new Bundle();
            bundle.putString("CHANNEL_URL", channelUrl);
            if (getIntent() != null && getIntent().getExtras() != null) {
                bundle.putAll(getIntent().getExtras());
            }
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();
            manager.beginTransaction().replace(R.id.container_group_channel, fragment).commit();
        }
        else {
            finish();
        }
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.my_toolbar);
        toolbarSubtitle = findViewById(R.id.toolbar_subtitle);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(theme != null && theme.getToolbarTitle() != null ? theme.getToolbarTitle() : getString(R.string.message_center_toolbar_title));
        }
        toolbarSubtitle.setText(theme != null && theme.getToolbarSubtitle() != null ? theme.getToolbarSubtitle() : "");
        PACKAGE_NAME = getIntent().getStringExtra("PACKAGE_NAME");
    }

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    public void freeze() {
        if (menu != null && menu.findItem(R.id.menu_action_call) != null && theme != null && theme.isCallEnabled()) {
            menu.findItem(R.id.menu_action_call).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_calldisabled));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.hasExtra("close") && intent.getBooleanExtra("close", false)) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceUtils.init(this);
    }

    @Override
    public void onBackPressed() {
        setResult(MessageCenter.OPEN_CHAT_VIEW_RESPONSE_CODE);
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        setResult(MessageCenter.OPEN_CHAT_VIEW_RESPONSE_CODE);
    }

    @Override
    protected void onDestroy() {
        setResult(MessageCenter.OPEN_CHAT_VIEW_RESPONSE_CODE);
        super.onDestroy();
        SendBird.disconnect(new SendBird.DisconnectHandler() {
            @Override
            public void onDisconnected() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (theme != null && theme.isCallEnabled()) {
            getMenuInflater().inflate(R.menu.menu_with_call, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id == R.id.menu_action_call) { //Handle Call

        }
        return super.onOptionsItemSelected(item);
    }

    public interface onBackPressedListener {
        boolean onBack();
    }
}