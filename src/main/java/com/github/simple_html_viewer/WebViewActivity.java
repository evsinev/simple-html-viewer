package com.github.simple_html_viewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Field;

/**
 *
 */
public class WebViewActivity extends Activity {

    private final static LogUtil LOG = new LogUtil(WebViewActivity.class);

    @Override
    protected void onCreate(Bundle aSavedBundle) {
        super.onCreate(aSavedBundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.web_layout);
        theWebView = (WebView) findViewById(R.id.web);

        theWebView.setWillNotCacheDrawing(false);

        WebSettings webSettings = theWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(false);
        webSettings.setSupportZoom(false);
        webSettings.setDefaultFontSize(20);

        setTextZoom(theWebView, theCurrentTextZoom);

    }


    private static void setTextZoom(WebView aView, int aTextZoom) {
        // android 2.x allows changing a text zoom by predefined values: 50, 75, 100, 150, 200
        // this hack provides an ability to use custom values

        WebSettings settings = aView.getSettings();

        // using LARGEST or LARGER enums just to emulate the textSize changing
        WebSettings.TextSize size = settings.getTextSize() == WebSettings.TextSize.LARGEST ? WebSettings.TextSize.LARGER : WebSettings.TextSize.LARGEST;

        try {
            // changes enum value
            Field valueField = size.getClass().getDeclaredField("value");
            valueField.setAccessible(true);
            valueField.setInt(size, aTextZoom);

            int value = valueField.getInt(size);
            LOG.debug("new text size: %d", value);

            settings.setTextSize(size);

        } catch (Exception e) {
            LOG.error("Can't change text size", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        theScrollY = 0;

        Intent intent = getIntent();
        if(intent!=null) {
            Uri uri = intent.getData();
            if(uri!=null) {
                LOG.debug("Loading %s...", uri.toString());
                theWebView.loadUrl(uri.toString());
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        LOG.debug("code: %d, event: %s", keyCode, event.toString());
        int scrollOffset = theWebView.getHeight() - 50;

        if(event.getAction()==KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                case KeyEvent.KEYCODE_NUMPAD_0:
                    theScrollY += scrollOffset;
                    theWebView.scrollTo(theScrollX, theScrollY);
                    return true;

                case KeyEvent.KEYCODE_VOLUME_DOWN:
                case KeyEvent.KEYCODE_NUM_LOCK:
                    theScrollY -= scrollOffset;
                    theWebView.scrollTo(theScrollX, theScrollY);
                    return true;

                case KeyEvent.KEYCODE_MENU:
                    LOG.debug("Menu key");
                    return true;

                case KeyEvent.KEYCODE_NUMPAD_7:
                    theEpdHelper.requestEpdMode(theWebView, EpdHelper.EPD_FULL);
                    theWebView.invalidate();
                    return true;

                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    theScrollX+=10;
                    theWebView.scrollTo(theScrollX, theScrollY);
                    return true;

                case KeyEvent.KEYCODE_DPAD_LEFT:
                    theScrollX-=10;
                    theWebView.scrollTo(theScrollX, theScrollY);
                    return true;

                case KeyEvent.KEYCODE_DPAD_UP:
                    theCurrentTextZoom +=5;
                    setTextZoom(theWebView, theCurrentTextZoom);
                    return true;

                case KeyEvent.KEYCODE_DPAD_DOWN:
                    theCurrentTextZoom -=5;
                    setTextZoom(theWebView, theCurrentTextZoom);
                    return true;

            }
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                case KeyEvent.KEYCODE_DPAD_UP:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    private final EpdHelper theEpdHelper = new EpdHelper();
    private WebView theWebView;
    int theCurrentTextZoom = 120;
    int theScrollY = 0;
    int theScrollX = 0;
}
