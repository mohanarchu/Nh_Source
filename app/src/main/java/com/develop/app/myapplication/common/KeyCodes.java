package com.develop.app.myapplication.common;

public class KeyCodes {
    private static final KeyCodes keyCodes = new KeyCodes();

    public static KeyCodes getInstance() {
        return keyCodes;
    }


    public String USERNAME="userName";
    public String USERID="userId";
    public String DEVICEID="deviceId";
    public String REFRESHTOKEN="refreshToken";
    public String LOADINGURL="loadingurl";
    public String COUNTURL="counturl";
    public String SUBMODULENAME="subModuleName";

}
