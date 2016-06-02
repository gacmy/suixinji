/*
 * Copyright 2012 - 2014 Benjamin Weiss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gacmy.suixinji.myview.toast;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;



final class GacManager extends Handler {
  private static final class Messages {
    private Messages() { /* no-op */ }

    public static final int DISPLAY_CROUTON = 0xc2007;
    public static final int ADD_CROUTON_TO_VIEW = 0xc20074dd;
    public static final int REMOVE_CROUTON = 0xc2007de1;
  }

  public static  int TIME_DURATION=1500;
  private static GacManager INSTANCE;

  private final Queue<GacToast> queue;

  private GacManager() {
    queue = new LinkedBlockingQueue<GacToast>();
  }


  static synchronized GacManager getInstance() {
    if (null == INSTANCE) {
      INSTANCE = new GacManager();
    }

    return INSTANCE;
  }


  void add(GacToast crouton) {
    queue.add(crouton);
    Log.e("gac","add queue");
    //displayCrouton();
    displaytost();
  }

  private void displaytost(){
    if (queue.isEmpty()) {
        return;
    }
    GacToast toast = queue.peek();
    if (null == toast.getActivity()) {
      queue.poll();
    }
    Log.e("gac", "ADD_CROUTON_TO_VIEW");
    if(!toast.isShowing()){
      sendMessage(toast, Messages.ADD_CROUTON_TO_VIEW);
    }else{
      Log.e("gac", "DISPLAY_CROUTON");
      sendMessageDelayed(toast, Messages.DISPLAY_CROUTON, calculateCroutonDuration(toast));
    }

  }

  private long calculateCroutonDuration(GacToast crouton) {
    long croutonDuration = TIME_DURATION;
    croutonDuration += crouton.getInAnimation().getDuration();
    croutonDuration += crouton.getOutAnimation().getDuration();
    return croutonDuration;
  }



  private void sendMessage(GacToast toast, final int messageId) {
     final Message message = obtainMessage(messageId);
     message.obj = toast;
     sendMessage(message);
  }

  public void handleMessage(Message message) {
    final GacToast toast = (GacToast) message.obj;
    if (null == toast) {
      return;
    }
    switch (message.what) {

      case Messages.ADD_CROUTON_TO_VIEW: {
        addToastToView(toast);
        break;

      }
      case Messages.REMOVE_CROUTON:
        removeCrouton(toast);
        break;
      case Messages.DISPLAY_CROUTON:
        displaytost();
        break;

      }

  }

  private void removeAllMessagesForCrouton(GacToast crouton) {
    removeMessages(Messages.ADD_CROUTON_TO_VIEW, crouton);
    removeMessages(Messages.DISPLAY_CROUTON, crouton);
    removeMessages(Messages.REMOVE_CROUTON, crouton);

  }
  protected void removeCrouton(GacToast crouton) {
    // If the crouton hasn't been displayed yet a `Crouton.hide()` will fail to hide
    // it since the DISPLAY message might still be in the queue. Remove all messages
    // for this crouton.
    removeAllMessagesForCrouton(crouton);

    View croutonView = crouton.getView();
    ViewGroup croutonParentView = (ViewGroup) croutonView.getParent();

    if (null != croutonParentView) {
      croutonView.startAnimation(crouton.getOutAnimation());

      // Remove the Crouton from the queue.
      GacToast removed = queue.poll();

      // Remove the crouton from the view's parent.
      croutonParentView.removeView(croutonView);
      if (null != removed) {
        removed.detachActivity();
        removed.detachViewGroup();

      }

      // Send a message to display the next crouton but delay it by the out
      // animation duration to make sure it finishes
      //延迟当前toast退出的动作时间继续发送显示时间
       sendMessageDelayed(crouton, Messages.DISPLAY_CROUTON, crouton.getOutAnimation().getDuration());
    }
  }

  private void addToastToView(final GacToast toast){
    if (toast.isShowing()) {
      return;
    }
    final View view = toast.getView();
    if (null == view.getParent()) {
      ViewGroup.LayoutParams params = view.getLayoutParams();
      if (null == params) {
        params =
                new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      }
      // display Crouton in ViewGroup if it has been supplied

        Activity activity = toast.getActivity();
        if (null == activity || activity.isFinishing()) {
          return;
        }
        handleTranslucentActionBar((ViewGroup.MarginLayoutParams) params, activity);
        handleActionBarOverlay((ViewGroup.MarginLayoutParams) params, activity);

        activity.addContentView(view, params);

    }

    view.requestLayout(); // This is needed so the animation can use the measured with/height
    ViewTreeObserver observer = view.getViewTreeObserver();
    if (null != observer) {
      observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        @TargetApi(16)
        public void onGlobalLayout() {
          if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
          view.startAnimation(toast.getInAnimation());
          announceForAccessibilityCompat(toast.getActivity(), toast.getText());
          //延迟显示的时间发送删除消息
          sendMessageDelayed(toast, Messages.REMOVE_CROUTON,
                  TIME_DURATION + toast.getInAnimation().getDuration());
        }
      });
    }
  }
  private void sendMessageDelayed(GacToast crouton, final int messageId, final long delay) {
    Message message = obtainMessage(messageId);
    message.obj = crouton;
    sendMessageDelayed(message, delay);
  }

  public static void announceForAccessibilityCompat(Context context, CharSequence text) {
    if (Build.VERSION.SDK_INT >= 4) {
      AccessibilityManager accessibilityManager = null;
      if (null != context) {
        accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
      }
      if (null == accessibilityManager || !accessibilityManager.isEnabled()) {
        return;
      }

      // Prior to SDK 16, announcements could only be made through FOCUSED
      // events. Jelly Bean (SDK 16) added support for speaking text verbatim
      // using the ANNOUNCEMENT event type.
      final int eventType;
      if (Build.VERSION.SDK_INT < 16) {
        eventType = AccessibilityEvent.TYPE_VIEW_FOCUSED;
      } else {
        eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT;
      }

      // Construct an accessibility event with the minimum recommended
      // attributes. An event without a class name or package may be dropped.
      final AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
      event.getText().add(text);
      event.setClassName(GacManager.class.getName());
      event.setPackageName(context.getPackageName());

      // Sends the event directly through the accessibility manager. If your
      // application only targets SDK 14+, you should just call
      // getParent().requestSendAccessibilityEvent(this, event);
      accessibilityManager.sendAccessibilityEvent(event);
    }
  }

  private void handleTranslucentActionBar(ViewGroup.MarginLayoutParams params, Activity activity) {
    // Translucent status is only available as of Android 4.4 Kit Kat.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      final int flags = activity.getWindow().getAttributes().flags;
      final int translucentStatusFlag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
      if ((flags & translucentStatusFlag) == translucentStatusFlag) {
        setActionBarMargin(params, activity);
      }
    }
  }

  @TargetApi(11)
  private void handleActionBarOverlay(ViewGroup.MarginLayoutParams params, Activity activity) {
    // ActionBar overlay is only available as of Android 3.0 Honeycomb.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      final boolean flags = activity.getWindow().hasFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
      if (flags) {
        setActionBarMargin(params, activity);
      }
    }
  }

  private void setActionBarMargin(ViewGroup.MarginLayoutParams params, Activity activity) {
    final int actionBarContainerId = Resources.getSystem().getIdentifier("action_bar_container", "id", "android");
    final View actionBarContainer = activity.findViewById(actionBarContainerId);
    // The action bar is present: the app is using a Holo theme.
    if (null != actionBarContainer) {
      params.topMargin = actionBarContainer.getBottom();
    }
  }

}
