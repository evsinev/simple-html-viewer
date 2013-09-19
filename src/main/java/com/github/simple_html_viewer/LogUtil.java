package com.github.simple_html_viewer;

import android.util.Log;


/**
 *
 */
public class LogUtil {

    public LogUtil(Class aType) {
        theName = "htmlviewer."+aType.getSimpleName();
    }

    public void debug(String aFormat, Object ... args) {
        Log.d(theName, String.format(". "+aFormat, args));
    }

    public void error(String aText, Exception e) {
        Log.e(theName, String.format(aText, e));
    }

    private final String theName;
}
