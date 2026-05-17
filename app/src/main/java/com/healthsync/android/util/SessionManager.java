package com.healthsync.android.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME   = "hs_session";
    private static final String KEY_ACCESS  = "access_token";
    private static final String KEY_REFRESH = "refresh_token";
    private static final String KEY_EMAIL   = "user_email";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveTokens(String accessToken, String refreshToken) {
        prefs.edit()
                .putString(KEY_ACCESS, accessToken)
                .putString(KEY_REFRESH, refreshToken)
                .apply();
    }

    public void saveEmail(String email) {
        prefs.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getAccessToken()  { return prefs.getString(KEY_ACCESS, null); }
    public String getRefreshToken() { return prefs.getString(KEY_REFRESH, null); }
    public String getEmail()        { return prefs.getString(KEY_EMAIL, null); }

    public boolean isLoggedIn() { return getAccessToken() != null; }

    public void clearSession() { prefs.edit().clear().apply(); }
}