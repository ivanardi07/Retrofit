package id.putraprima.retrofit.api.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private static final String APP_KEY ="key_app";
    private static final String VERSION_KEY ="version_app";

    private SharedPreferences sharedPreferences;

    public Session(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getApp(){
        return sharedPreferences.getString(APP_KEY, null);
    }

    public void setApp(String app){
        sharedPreferences.edit().putString(APP_KEY, app).apply();
    }

    public String getVersion(){
        return sharedPreferences.getString(VERSION_KEY, null);
    }

    public void setVersion(String version){
        sharedPreferences.edit().putString(VERSION_KEY, version).apply();
    }
}
