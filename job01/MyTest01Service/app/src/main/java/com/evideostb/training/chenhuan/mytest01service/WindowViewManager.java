package com.evideostb.training.chenhuan.mytest01service;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ChenHuan on 2018/1/31.
 */

public class WindowViewManager {
    /**
     * 小悬浮窗View的实例
     */
    private static FloatWindowView smallWindow;

    /**
     * 小悬浮窗View的参数
     */
    private static WindowManager.LayoutParams smallWindowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    private static Handler handle;

    private static String TAG = "WindowViewManager";

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     * @param context
     * 必须为应用程序的Context.
     */
    public static void createSmallWindow(Context context) {
        if (handle == null){
            handle = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (smallWindow != null){
                        smallWindow.findViewById(R.id.ll_container).setVisibility(View.VISIBLE);
                    }
                }
            };
        }
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (smallWindow == null) {
            smallWindow = new FloatWindowView(context);
            if (smallWindowParams == null) {
                smallWindowParams = new WindowManager.LayoutParams();
                smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = FloatWindowView.viewWidth;
                smallWindowParams.height = FloatWindowView.viewHeight;
                smallWindowParams.x = 0;
                smallWindowParams.y = 0;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     * @param context
     *  必须为应用程序的Context.
     */
    public static void removeSmallWindow(Context context) {
        if (smallWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(smallWindow);
            smallWindow = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据。
     * 可传入应用程序上下文。
     */
    public static void updateData(String data) {
        if (smallWindow != null) {
            LinearLayout ll_container = (LinearLayout) smallWindow.findViewById(R.id.ll_container);
            ll_container.setVisibility(View.GONE);
            TextView tv_data = (TextView) smallWindow.findViewById(R.id.tv_data);
            tv_data.setText(data);
            Log.d(TAG,"data:" + data);
            handle.sendMessageDelayed(Message.obtain(),100);
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return smallWindow != null ;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     * @param context
     * 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

}
