package popup.popfisher.com.smartpopupwindow;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.PopupWindow;

public class ScreenChangeUpdatePopupActivity extends Activity {
    private Button mAnchorBtn;
    private PopupWindow mPopupWindow = null;
    private int mCurOrientation = -1;

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
                // showAsDropDown里面注册了一个OnScrollChangedListener,我们自己也注册一个OnScrollChangedListener
                // 但是要在它的后面，这样系统回调的时候会先做完它的再做我们自己的，就可以用我们自己正确的值覆盖掉它的
                // initViewListener();
            }
        });
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 转屏时直接调用update方法更新位置，现象如下
        // 1. 如果R.layout.activity_screen_change_update_popup中的ListView可见，则update无效
        // 2. 如果R.layout.activity_screen_change_update_popup中的ListView不可见，则update有效
        mCurOrientation = newConfig.orientation;
        // 如果要解决上面的问题就把下面这句话注释掉，并且打开注释掉的initViewListener
        updatePopupPos();
    }

    private void initViewListener() {
        mAnchorBtn.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mPopupWindow == null || !mPopupWindow.isShowing()) {
                    return;
                }
                updatePopupPos();
            }
        });
    }

    private void updatePopupPos() {
        if (mCurOrientation == ActivityInfo.SCREEN_ORIENTATION_USER || mCurOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mPopupWindow.update(0, 0, -1, -1);
        } else if (mCurOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mPopupWindow.update(0, 800, -1, -1);
        }
    }
}
