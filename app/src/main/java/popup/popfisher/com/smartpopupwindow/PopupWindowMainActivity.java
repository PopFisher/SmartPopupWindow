package popup.popfisher.com.smartpopupwindow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PopupWindowMainActivity extends Activity implements View.OnClickListener {

    private View mButton1;
    private View mButton2;
    private View mButton3;
    private View mButton4;
    private View mButton5;

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window_main);
        initView();
        bindViewListeners();
    }

    private View getPopupWindowContentView() {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_layout;   // 布局ID
        View contentView = LayoutInflater.from(this).inflate(layoutId, null);
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Click " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        };
        contentView.findViewById(R.id.menu_item1).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_item2).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_item3).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_item4).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_item5).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }

    private void testPopupWindowType1() {
        View contentView = getPopupWindowContentView();
        // 创建PopupWindow时候指定高宽时showAsDropDown能够自适应(能够根据剩余空间自动选中向上向下弹出)
        // 如果设置为wrap_content,showAsDropDown会认为下面空间一直很充足（我以认为这个Google的bug）
        // 备注如果PopupWindow里面有ListView,ScrollView时，一定要动态设置PopupWindow的大小
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        // popupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        // popupWindow.showAsDropDown(mButton1);  // 默认在mButton1的左下角显示
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = mButton1.getWidth() / 2 - contentView.getMeasuredWidth() / 2;
        mPopupWindow.showAsDropDown(mButton1, xOffset, 0);    // 在mButton1的中间显示
    }

    private void testPopupWindowType2() {
        View contentView = getPopupWindowContentView();
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        // popupWindow.showAsDropDown(mButton2);  // 默认在mButton2的左下角显示
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = mButton2.getWidth() / 2 - contentView.getMeasuredWidth() / 2;
        mPopupWindow.showAsDropDown(mButton2, xOffset, 0);    // 在mButton2的中间显示
    }

    private void testPopupWindowType3() {
        Intent intent = new Intent(this, CustomPosPopupActivity.class);
        startActivity(intent);
    }

    private void testPopupWindowType4() {
        Intent intent = new Intent(this, TopBottomArrowPopupActivity.class);
        startActivity(intent);
    }

    private void testPopupWindowType5() {
        Intent intent = new Intent(this, Android7_0PopupActivity.class);
        startActivity(intent);
    }

    private void initView() {
        mButton1 = findViewById(R.id.popup_test_button1);
        mButton2 = findViewById(R.id.popup_test_button2);
        mButton3 = findViewById(R.id.popup_test_button3);
        mButton4 = findViewById(R.id.popup_test_button4);
        mButton5 = findViewById(R.id.popup_test_button5);
    }

    private void bindViewListeners() {
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        System.out.println(Build.MODEL);
        switch (id) {
            case R.id.popup_test_button1:
                testPopupWindowType1();
                break;
            case R.id.popup_test_button2:
                testPopupWindowType2();
                break;
            case R.id.popup_test_button3:
                testPopupWindowType3();
                break;
            case R.id.popup_test_button4:
                testPopupWindowType4();
                break;
            case R.id.popup_test_button5:
                testPopupWindowType5();
                break;
            default:
                break;
        }
    }
}
