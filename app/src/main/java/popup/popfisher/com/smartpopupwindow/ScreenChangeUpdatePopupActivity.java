package popup.popfisher.com.smartpopupwindow;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class ScreenChangeUpdatePopupActivity extends Activity {
    private Button mAnchorBtn;
    private PopupWindow mPopupWindow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_change_update_popup);
        mAnchorBtn = (Button) findViewById(R.id.anchor_button);
        mAnchorBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_content_layout, null);
                mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setFocusable(true);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable());
                mPopupWindow.showAsDropDown(mAnchorBtn, 0, 0);
            }
        });
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 转屏时调用update方法更新位置，现象如下
        // 1. 如果R.layout.activity_screen_change_update_popup中的ListView可见，则update无效
        // 2. 如果R.layout.activity_screen_change_update_popup中的ListView不可见，则update有效
        final int typeScreen = newConfig.orientation;
        if (typeScreen == ActivityInfo.SCREEN_ORIENTATION_USER || typeScreen == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mPopupWindow.update(0, 0, -1, -1);
        } else if (typeScreen == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mPopupWindow.update(0, 800, -1, -1);
        }
    }
}
