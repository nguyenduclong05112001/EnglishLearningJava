package com.example.englishlearn.sharedpreferences;

import android.content.Context;

import com.example.englishlearn.models.InfoPoinOfDay;
import com.google.gson.Gson;

public class DataLocalManage {
    private static final String IS_LOGIN_IN_SYSTEM = "IS_LOGIN_IN_SYSTEM";
    private static final String IS_CHECKED_REMEMBER = "IS_CHECKED_REMEMBER";
    private static final String USER_LOGINED = "USER_LOGINED";
    private static final String PASS_LOGINED = "PASS_LOGINED";
    private static final String USER_CHECK = "USER_CHECK";
    private static final String POINT_OF_DAY = "POINT_OF_DAY";
    private static final String IS_FIRST_COMPLETE_OF_DAY = "IS_FIRST_COMPLETE_OF_DAY";
    private static final String FIRST_INTO_APP = "FIRST_INTO_APP";

    private static DataLocalManage mDataLocalManage;
    private static SharedPreferencesSystem mSharedPreferencesSystem;

    public static void init(Context context) {
        mDataLocalManage = new DataLocalManage();
        mDataLocalManage.mSharedPreferencesSystem = new SharedPreferencesSystem(context);
    }

    public static DataLocalManage getmDataLocalManage() {
        if (mDataLocalManage == null) {
            mDataLocalManage = new DataLocalManage();
        }
        return mDataLocalManage;
    }

    public static void setIsLogin(boolean value) {
        DataLocalManage.mSharedPreferencesSystem.pubIsLogin(IS_LOGIN_IN_SYSTEM, value);
    }

    public static boolean getIsLogin() {
        return DataLocalManage.mSharedPreferencesSystem.getIsLogin(IS_LOGIN_IN_SYSTEM);
    }

    public static void setCheckedRe(boolean value) {
        DataLocalManage.mSharedPreferencesSystem.pubIsCheckedRemember(IS_CHECKED_REMEMBER, value);
    }

    public static boolean getCheckedRe() {
        return DataLocalManage.mSharedPreferencesSystem.getIsCheckedRemember(IS_CHECKED_REMEMBER);
    }

    public static void setUsernameLogined(String value) {
        DataLocalManage.mSharedPreferencesSystem.pubUsername(USER_LOGINED, value);
    }

    public static String getUsernameLogined() {
        return DataLocalManage.mSharedPreferencesSystem.getUsername(USER_LOGINED);
    }

    public static void setPasswordLogined(String value) {
        DataLocalManage.mSharedPreferencesSystem.pubPassword(PASS_LOGINED, value);
    }

    public static String getPasswordLogined() {
        return DataLocalManage.mSharedPreferencesSystem.getPassword(PASS_LOGINED);
    }

    public static void setUsertoCheck(String value) {
        DataLocalManage.mSharedPreferencesSystem.pubUsernametoCheck(USER_CHECK, value);
    }

    public static String getUsertoCheck() {
        return DataLocalManage.mSharedPreferencesSystem.getUsernametoCheck(USER_CHECK);
    }

    public static void setPointofday(InfoPoinOfDay value) {
        Gson gson = new Gson();
        String object = gson.toJson(value);
        DataLocalManage.mSharedPreferencesSystem.pubPointoday(POINT_OF_DAY, object);
    }

    public static InfoPoinOfDay getPointofday() {
        String values = DataLocalManage.mSharedPreferencesSystem.getPointoday(POINT_OF_DAY);
        Gson gson = new Gson();
        if(values.isEmpty()){
            return null;
        }
        InfoPoinOfDay infoPoinOfDay = gson.fromJson(values, InfoPoinOfDay.class);
        return infoPoinOfDay;
    }

    public static void setFirstCompleteLearn(boolean value) {
        DataLocalManage.mSharedPreferencesSystem.pubIsFirstCompleteOfDay(IS_FIRST_COMPLETE_OF_DAY, value);
    }

    public static boolean getFirstCompleteLearn() {
        return DataLocalManage.mSharedPreferencesSystem.getIsFirstCompleteOfDay(IS_FIRST_COMPLETE_OF_DAY);
    }

    public static void setFirstInApp(boolean value) {
        DataLocalManage.mSharedPreferencesSystem.pubFirstInApp(FIRST_INTO_APP, value);
    }

    public static boolean getFirstInApp() {
        return DataLocalManage.mSharedPreferencesSystem.getFirstInApp(FIRST_INTO_APP);
    }
}
