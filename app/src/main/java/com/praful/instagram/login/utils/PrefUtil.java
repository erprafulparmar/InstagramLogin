package com.praful.instagram.login.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtil
{

    public static final String PRF_LOGINCHECK = "pref_logincheck";
    public static final String PRF_ACCESS_TOKEN_CHECK = "access_token";


    public static void putstringPref(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getstringPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }


    public static void putint_pref(String key, int value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, 0);
    }

    public static boolean putbooleanPref(String key, boolean value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
        return value;
    }

    public static boolean getbooleanPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }
}



split = strWord.split("#");
        for (String s : split) {
            Log.e("split", "===>" + s);
            stringList.add(s);
        }

        for (int k = 0; k < stringList.size(); k++) {
            StringBuilder stringBuilder=new StringBuilder();
            String sValue = stringList.get(k);
            if(k==0 || k==3)
            {
                int spaceCenter = (sValue.length() - 14) / 2;
                spaceCenter= Math.abs(spaceCenter);
                Log.e("space", "===>" + spaceCenter);

                for(int box=0;box<spaceCenter;box++)
                {
                    Log.e("spaceCenter1", "===>" + spaceCenter);
                    stringBuilder.append(" ");
                }
            }
            else  if(k==1 || k==2)
            {
                int spaceCenter = (sValue.length() - 16) / 2;
                spaceCenter= Math.abs(spaceCenter);
                Log.e("space", "===>" + spaceCenter);
                for(int box=0;box<spaceCenter;box++)
                {
                    stringBuilder.append(" ");
                    Log.e("spaceCenter1", "===>" + spaceCenter);
                }
            }

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("key", String.valueOf(k));
            hashMap.put("value", stringBuilder.append(sValue).toString());
            finalWords.add(hashMap);
        }


        for (HashMap<String, String> list : finalWords) {
            Log.e("key", "===>" + list.get("key"));
            Log.e("value", "===>" + list.get("value"));
            char[] values = list.get("value").toCharArray();
            for(char cc:values)
            {
                Log.e("cc", "===>" + cc);

                charsList.add(String.valueOf(cc));
            }
        }
        Log.e("charsList", "===>" + charsList.size());

