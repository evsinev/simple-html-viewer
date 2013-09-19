package com.github.simple_html_viewer;

import android.view.View;

import java.lang.reflect.Method;

/**
 *
 */
public class EpdHelper {

    public static final int EPD_A2 = 2;
    public static final int EPD_AUTO = 0;
    public static final int EPD_FULL = 1;
    public static final int EPD_NULL = -1;
    public static final int EPD_OED_PART = 10;
    public static final int EPD_PART = 3;

    private final static LogUtil LOG = new LogUtil(EpdHelper.class);

    public EpdHelper() {
        // public boolean requestEpdMode(int i)
        Method requestEpdModeMethod;
        try {
            requestEpdModeMethod = View.class.getMethod("requestEpdMode", int.class);
            LOG.debug("request epd method = %s", requestEpdModeMethod.toString());
        } catch (NoSuchMethodException e) {
            LOG.error("Method requestEpdMode(int) not found", e);
            e.printStackTrace();
            requestEpdModeMethod = null;
        }
        theRequestEpdModeMethod = requestEpdModeMethod;
    }

    public boolean isSupported() {
        return theRequestEpdModeMethod!=null;
    }

    public boolean requestEpdMode(View aView, int aMode) {
        try {
            return (Boolean)theRequestEpdModeMethod.invoke(aView, aMode);
        } catch (Exception e) {
            LOG.error("Can't invoke method: "+theRequestEpdModeMethod, e);
            return false;
        }
    }

    private final Method theRequestEpdModeMethod;

}
