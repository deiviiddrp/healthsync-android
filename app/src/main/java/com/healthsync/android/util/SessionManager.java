package com.healthsync.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SessionManager {

    private static final String PREF_NAME   = "hs_session_secure";
    private static final String KEY_ACCESS  = "access_token";
    private static final String KEY_REFRESH = "refresh_token";
    private static final String KEY_EMAIL   = "user_email";
    private static final String KEY_FCM     = "fcm_token";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        SharedPreferences p;
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            p = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            p = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        this.prefs = p;
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

    public void saveFcmToken(String token) {
        prefs.edit().putString(KEY_FCM, token).apply();
    }

    public String getAccessToken()  { return prefs.getString(KEY_ACCESS, null); }
    public String getRefreshToken() { return prefs.getString(KEY_REFRESH, null); }
    public String getEmail()        { return prefs.getString(KEY_EMAIL, null); }
    public String getFcmToken()     { return prefs.getString(KEY_FCM, null); }

    public boolean isLoggedIn() { return getAccessToken() != null; }

    public void clearSession() { prefs.edit().clear().apply(); }
}