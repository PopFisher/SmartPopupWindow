# SmartPopupWindow #
提供关于PopupWindow使用过程中遇到的一些特殊问题的解决方案

1. PopupWindow不响应点击外部消失和返回键消失的解决方法<br>博文地址：[http://www.cnblogs.com/popfisher/p/5608717.html](http://www.cnblogs.com/popfisher/p/5608717.html "PopupWindow不响应点击外部消失和返回键消失的解决方法")

2. PopupWindow精确计算要显示位置原理和方法<br>博文地址：[http://www.cnblogs.com/popfisher/p/5608436.html](http://www.cnblogs.com/popfisher/p/5608436.html "PopupWindow精确计算要显示位置原理和方法")

3. 不得不吐槽的Android PopupWindow的几个痛点（实现带箭头的上下文菜单遇到的坑）<br>博文地址：[http://www.cnblogs.com/popfisher/p/5944054.html](http://www.cnblogs.com/popfisher/p/5944054.html "不得不吐槽的Android PopupWindow的几个痛点")

4. Android7.0 PopupWindow的兼容问题，修复了问题1和3中描述的问题，但是由引入了新的问题吗，具体详情<br> 博文地址：[http://www.cnblogs.com/popfisher/p/6434757.html](http://www.cnblogs.com/popfisher/p/6434757.html "博文地址") <br> Github地址：[https://github.com/PopFisher/SmartPopupWindow/blob/master/7.0PopupWindow.md](https://github.com/PopFisher/SmartPopupWindow/blob/master/7.0PopupWindow.md "7.0PopupWindow介绍Github地址")

5. 屏幕旋转时调用PopupWindow update方法更新位置失效的问题及解决方案。<br>博文地址：[http://www.cnblogs.com/popfisher/p/8029616.html](http://www.cnblogs.com/popfisher/p/8029616.html "博客园地址")<br>Github地址：[https://github.com/PopFisher/SmartPopupWindow/blob/master/ScreenRotateUpdatePopup.md](https://github.com/PopFisher/SmartPopupWindow/blob/master/ScreenRotateUpdatePopup.md "ScreenRotateUpdatePopup.md")


## 效果如下图所示：##

### 问题1截图 

![不会响应外部点击消失事件的情况](/docpic/1.png "主界面入口")

### 问题2截图

![动态计算弹出框的位置](/docpic/2.png "在anchorView下面弹出PopupWindow")    ![动态计算弹出框的位置](/docpic/3.png "在anchorView上面弹出PopupWindow")

### 问题3截图

![第一排左边按钮菜单](/docpic/topleft.png	"符合预期")   ![第一排右边按钮菜单](/docpic/topright.png "出乎意料了,不符合预期")   ![第二排中间按钮菜单](/docpic/bottomcenter.png "符合预期")  

### 问题4相关截图

![7.0主Activity](/docpic/7.0.png	"7.0 PopupWindow兼容性测试")

**1. PopupWindow高宽都设置为match\_parent:从屏幕左上角弹出**

![](/docpic/7.0_1.png	"PopupWindow高宽都设置为match_parent")

**2. 宽度wrap\_content-高度match\_parent:从屏幕左上角弹出**

![](/docpic/7.0_2.png	"宽度wrap_content-高度match_parent")

**3. 宽度match\_parent-高度wrap\_content:从anchorView下方弹出**

![](/docpic/7.0_3.png	"宽度match_parent-高度wrap_content")

**4. 宽度wrap_content-高度大于anchorView到屏幕底部的距离:从anchorView上方弹出，与anchorView左对齐**

![](/docpic/7.0_4.png	"宽度wrap_content-高度大于anchorView到屏幕底部的距离")

**5. showAtLocation传入Gravity.Bottom:从anchorView下方弹出**

![](/docpic/7.0_5.png	"宽度wrap_content-高度大于anchorView到屏幕底部的距离")

**6. 调用update方法更新第5点中弹出PopupWindow，发现PopupWindow的Gravity发生了改变**

详细可以参考 [http://www.jianshu.com/p/0df10893bf5b](http://www.jianshu.com/p/0df10893bf5b "PopupWindow 在 Android N(7.0) 的兼容性问题")

![](/docpic/7.0_6.png	"调用update方法更新弹出Popup")