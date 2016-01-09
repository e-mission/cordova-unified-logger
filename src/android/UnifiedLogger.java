package edu.berkeley.eecs.emission.cordova.unifiedlogger;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Intent;
import java.io.PrintWriter;
import java.io.StringWriter;
import edu.berkeley.eecs.emission.cordova.unifiedlogger.Log;

public class UnifiedLogger extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        if (action.equals("log")) {
            try {
                String level = data.getString(0);
                String message = data.getString(1);
                Log.log(cordova.getActivity(), level, "js", message);
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                callbackContext.error(exceptionAsString);
            }
            return true;
        } else if (action.equals("clear")) {
            try {
                Log.clear(cordova.getActivity());
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                callbackContext.error(exceptionAsString);
            }
            return true;
        } else {
            return false;
        }
    }
}
