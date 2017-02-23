package popup.popfisher.com.smartpopupwindow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 此Activity专门演示PopupWindow在android7.0系统的是兼容情况
 */
public class Android7_0PopupActivity extends Activity implements View.OnClickListener {

    private View mButton1;
    private View mButton2;
    private View mButton3;
    private View mButton4;
    private View mButton5;
    private View mButton6;
    private PopupWindow mCurPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_7_0_popup_window);
        mButton1 = findViewById(R.id.buttion1);
        mButton2 = findViewById(R.id.buttion2);
        mButton3 = findViewById(R.id.buttion3);
        mButton4 = findViewById(R.id.buttion4);
        mButton5 = findViewById(R.id.buttion5);
        mButton6 = findViewById(R.id.buttion6);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttion1:
                mCurPopupWindow = showTipPopupWindow1(mButton1);
                break;
            case R.id.buttion2:
                mCurPopupWindow = showTipPopupWindow2(mButton2);
                break;
            case R.id.buttion3:
                mCurPopupWindow = showTipPopupWindow3(mButton3);
                break;
            case R.id.buttion4:
                mCurPopupWindow = showTipPopupWindow4(mButton4);
                break;
            case R.id.buttion5:
                mCurPopupWindow = showTipPopupWindow5(mButton5);
                break;
            case R.id.buttion6:
                updatePopupWindow5(mButton6);
                break;
        }
    }

    private View createPopupContentView(Context context) {
        final View contentView = LayoutInflater.from(context).inflate(R.layout.popup_empty_content_layout, null);
        contentView.setOnClickListener(mClickContentCancelListener);
        return contentView;
    }

    public PopupWindow showTipPopupWindow1(final View anchorView) {
        final View contentView = createPopupContentView(anchorView.getContext());
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // anchorView 上方下放的空间都不够时，Window从屏幕左上角开始显示
        popupWindow.showAsDropDown(anchorView);
        return popupWindow;
    }

    public PopupWindow showTipPopupWindow2(final View anchorView) {
        final View contentView = createPopupContentView(anchorView.getContext());
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // anchorView 下面的空间足够，Window显示时会显示在 anchorView 下面
        // anchorView 下面的空间不够，Window显示时会显示在 anchorView 上面
        // anchorView 上面的空间也不够时，Window从屏幕左上角开始显示
        popupWindow.showAsDropDown(anchorView);
        return popupWindow;
    }

    public PopupWindow showTipPopupWindow3(final View anchorView) {
        final View contentView = createPopupContentView(anchorView.getContext());
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // anchorView 下面的空间足够，Window显示时会显示在 anchorView 下面
        popupWindow.showAsDropDown(anchorView);
        return popupWindow;
    }

    public PopupWindow showTipPopupWindow4(final View anchorView) {
        final View contentView = createPopupContentView(anchorView.getContext());
        final int pos[] = new int[2];
        anchorView.getLocationOnScreen(pos);
        int windowHeight = ScreenUtils.getScreenHeight(getApplicationContext()) - pos[1];
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, windowHeight, true);
        // anchorView 下面的空间不够，Window显示时会显示在 anchorView 上面
        popupWindow.showAsDropDown(anchorView);
        return popupWindow;
    }

    public PopupWindow showTipPopupWindow5(final View anchorView) {
        final View contentView = createPopupContentView(anchorView.getContext());
        final int pos[] = new int[2];
        anchorView.getLocationOnScreen(pos);
        int windowHeight = ScreenUtils.getScreenHeight(getApplicationContext()) - pos[1];
        // focus参数传入false，好验证update方法对Gravity的影响
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, windowHeight, false);
        // 使用 showAtLocation 方法：anchorView 下面即使空间不够也还是会显示在 anchorView 下面
        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);
        return popupWindow;
    }

    public void updatePopupWindow5(final View anchorView) {
        // 在android7.0里面，update方法里面会覆盖PopupWindow的Gravity属性
        if (mCurPopupWindow != null) {
            mCurPopupWindow.update();
        }
    }

    private View.OnClickListener mClickContentCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCurPopupWindow != null) {
                mCurPopupWindow.dismiss();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (mCurPopupWindow != null && mCurPopupWindow.isShowing()) {
            mCurPopupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
