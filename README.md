# SmartPopupWindow #
提供关于PopupWindow使用过程中遇到的一些特殊问题的解决方案

1. PopupWindow不响应点击外部消失和返回键消失的解决方法，博文地址：[http://www.cnblogs.com/popfisher/p/5608717.html](http://www.cnblogs.com/popfisher/p/5608717.html "PopupWindow不响应点击外部消失和返回键消失的解决方法")
2. PopupWindow精确计算要显示位置原理和方法，博文地址：[http://www.cnblogs.com/popfisher/p/5608436.html](http://www.cnblogs.com/popfisher/p/5608436.html "PopupWindow精确计算要显示位置原理和方法")
3. 不得不吐槽的Android PopupWindow的几个痛点（实现带箭头的上下文菜单遇到的坑），博文地址：[http://www.cnblogs.com/popfisher/p/5944054.html](http://www.cnblogs.com/popfisher/p/5944054.html "不得不吐槽的Android PopupWindow的几个痛点")


## 效果如下图所示：##

### 问题1截图 

![不会响应外部点击消失事件的情况](/docpic/1.png "主界面入口")

### 问题2截图

![动态计算弹出框的位置](/docpic/2.png "在anchorView下面弹出PopupWindow")    ![动态计算弹出框的位置](/docpic/3.png "在anchorView上面弹出PopupWindow")

### 问题3截图

![第一排左边按钮菜单](/docpic/topleft.png	"符合预期")   ![第一排右边按钮菜单](/docpic/topright.png "出乎意料了,不符合预期")   ![第二排中间按钮菜单](/docpic/bottomcenter.png "符合预期")  
