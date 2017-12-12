# 屏幕旋转时调用PopupWindow update方法更新位置失效的问题及解决方案

&emsp;&emsp; 接到一个博友的反馈，在屏幕旋转时调用PopupWindow的update方法失效。使用场景如下：在一个Activity中监听屏幕旋转事件，在Activity主布局文件中有个按钮点击弹出一个PopupWindow，另外在主布局文件中有个ListView。测试结果发现：如果ListView设置为可见（visibile）的话，屏幕旋转时调用的update方法无效，如果ListView设置为不可见（gone）或者直接删除的话，屏幕旋转时调用的update方法就生效。下面先展示两种情况的效果图对比。

## ListView不可见的情况（update生效，效果符合预期）
### 横屏效果图如下
![](/docpic/update1.png	"ListView不可见，横屏效果")
### 竖屏效果图如下
![](/docpic/update2.png	"ListView不可见，竖屏效果")

## ListView可见的情况（update不生效，效果不符合预期）
### 横屏效果图如下
![](/docpic/update3.png	"ListView可见，横屏效果")
### 竖屏效果图如下
![](/docpic/update4.png	"ListView可见，竖屏效果")

## 看了上面的效果图，再来看看简单的布局实现和Activity代码实现

## Activity主布局文件如下

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    	xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:paddingBottom="@dimen/activity_vertical_margin"
	    android:paddingLeft="@dimen/activity_horizontal_margin"
	    android:paddingRight="@dimen/activity_horizontal_margin"
	    android:paddingTop="@dimen/activity_vertical_margin"
	    tools:context="popup.popfisher.com.smartpopupwindow.PopupWindowMainActivity">
    <!-- 这个ListView的显示隐藏直接影响到PopupWindow在屏幕旋转的时候update方法是否生效 -->
    <ListView
        android:id="@+id/listview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:cacheColorHint="@android:color/transparent"
        android:visibility="visible" />

    <TextView
        android:id="@+id/textView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="监听屏幕旋转并调用PopupWindow的update方法，发现如果ListView可见的时候，update方法不生效，ListView不可见的时候update生效" />

    <Button
        android:id="@+id/anchor_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignLeft="@+id/textView1"
        android:layout_below="@+id/textView1"
        android:layout_marginLeft="44dp"
        android:layout_marginTop="40dp"
        android:text="点击弹出PopupWindow" />

    <LinearLayout
        android:id="@+id/btnListLayout"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:background="@android:color/transparent"
        android:orientation="horizontal"></LinearLayout>

	</RelativeLayout>

## Activity代码如下（onConfigurationChanged中根据屏幕方向调用update方法）
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

&emsp;&emsp; 效果图也看了，代码也看了，感觉代码本身没什么毛病，引起这个问题的导火索却是一个ListView，怎么办？当然一开始肯定要不停的尝试新的写法，看看是不是布局文件本身有什么问题。如果怎么尝试都解决不了的时候，这个时候可能已经踩到系统的坑了，可是怎么确定？去看看源码，然后调试一下看看。首先源码要确定是哪个版本的，发现这个问题的Android版本是6.0（其实这个是个普遍的问题，应该不是特有的，看后面的源码分析），那就找个api = 23的（平时空闲的时候再Android studio上把各种版本的api源码全部下载下来吧，方便直接调试和查看）。

## 准备好源码和调试环境之后，准备先看下源码（从哪儿开始看？）
&emsp;&emsp; 我们之前发现的现象是update方法失效，准确的说是update的前两个参数 x,y 坐标失效，高度和宽度是可以的。那我们就看开update方法的前面两个参数怎么使用的。

	public void update(int x, int y, int width, int height, boolean force) {
		if (width >= 0) {
			mLastWidth = width;
			setWidth(width);
		}
	
		if (height >= 0) {
			mLastHeight = height;
			setHeight(height);
		}
	
		if (!isShowing() || mContentView == null) {
			return;
		}
		// 这里拿到了 mDecorView 的布局参数 WindowManager.LayoutParams p
		final WindowManager.LayoutParams p =
				(WindowManager.LayoutParams) mDecorView.getLayoutParams();
	
		boolean update = force;
	
		final int finalWidth = mWidthMode < 0 ? mWidthMode : mLastWidth;
		if (width != -1 && p.width != finalWidth) {
			p.width = mLastWidth = finalWidth;
			update = true;
		}
	
		final int finalHeight = mHeightMode < 0 ? mHeightMode : mLastHeight;
		if (height != -1 && p.height != finalHeight) {
			p.height = mLastHeight = finalHeight;
			update = true;
		}
		// 这里把x,y分别赋值给 WindowManager.LayoutParams p
		if (p.x != x) {
			p.x = x;
			update = true;
		}
	
		if (p.y != y) {
			p.y = y;
			update = true;
		}
	
		final int newAnim = computeAnimationResource();
		if (newAnim != p.windowAnimations) {
			p.windowAnimations = newAnim;
			update = true;
		}
	
		final int newFlags = computeFlags(p.flags);
		if (newFlags != p.flags) {
			p.flags = newFlags;
			update = true;
		}
	
		if (update) {
			setLayoutDirectionFromAnchor();
			// 这里把 WindowManager.LayoutParams p 设置给了 mDecorView
			mWindowManager.updateViewLayout(mDecorView, p);
		}
	}

&emsp;&emsp;里面的几个注释是本人加的，仔细看这个方法好像没什么毛病。但是这个时候还是要坚信代码里面存在真理，它不会骗人。这里其实可以靠猜，是不是可能存在调用了多次update，本来设置好的又被其他地方调用update给覆盖了。但是猜是靠经验的，一般不好猜，还是笨方法吧，在update方法开头打个断点，看看代码怎么执行的。

## 万能的Debug，找准位置打好断点，开始调试
&emsp;&emsp;先把弹窗弹出来，然后打上断点，绑定调试的进程，转屏之后断点就过来了，如下所示

![](/docpic/update5.png	"Debug")

&emsp;&emsp;然后单步调试（AS的F8）完看看各个地方是不是正常的流程。这里会发现整个 update 方法都正常，那我们走完它吧（AS的F9快捷键）,奇怪的时候发现update又一次调用进来了，这一次参数有点不一样，看调用堆栈是从一个 onScrollChanged 方法调用过来的，而且参数x,y已经变了，高度宽度还是-1没变（到这里问题已经找到了，就是update被其他地方调用把我们设置的值覆盖了，不过都到这里了，肯定想知道为什么吧，继续看吧）。

![](/docpic/update6.png	"Debug")

&emsp;&emsp;从上面的调用堆栈，找到了 onScrollChanged 方法，我们查找一下看看，果然不出所料，这个方法改变了 x,y 参数，具体修改的地方是 findDropDownPosition 方法中，想知道怎么改的细节，可以继续断点调试。

![](/docpic/update7.png	"onScrollChanged")

![](/docpic/update8.png	"onScrollChanged")

&emsp;&emsp;继续寻找调用源头，mOnScrollChangedListener 的 onScrollChanged 谁调用？

![](/docpic/update9.png	"registerForScrollChanged")

![](/docpic/update10.png	"showAsDropDown中调用registerForScrollChanged方法")

## 源码分析找到原因了，有什么解决方案呢？
&emsp;&emsp;最后通过源码看到，在调用 showAsDropDown 方法的时候，会调用 registerForScrollChanged 方法，此方法会拿到anchorView 的 ViewTreeObserver 并添加一个全局的滚动监听事件。至于为什么有ListView的时候会触发到这个滚动事件，这个具体也不知道，不过从这里可以推测，可能不仅是ListView会出现这种情况，理论上还有很多其他的写法会导致转屏的时候触发到那个滚动事件，转屏这个操作太重了，什么的欧可能发生。所以个人推测这是一个普遍存在的问题，只是这种使用场景比较少。所以个人有如下建议：

1. 可以想办法把它注册的那个 OnScrollChangedLister 反注册掉
2. 转屏的时候延迟一下，目的是等它的 OnScrollChangedLister 回调走完，我们再走一次把正确的值覆盖掉，但是延迟时间不好控制。还可以自己也给那个anchorView的ViewTreeObserver添加一个OnScrollChangedLister，准确的监听到这个回调之后重新调用update方法设置正确的值，不过这个要和屏幕旋转回调做好配合。
3. 绕过这个坑，用其他的方式实现

## 第二种方法比较常用，代码如下

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
	                initViewListener();
	            }
	        });
	    }
	
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        mCurOrientation = newConfig.orientation;
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