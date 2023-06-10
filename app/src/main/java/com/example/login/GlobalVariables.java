package com.example.login;

import android.widget.RadioButton;

public class GlobalVariables {

    private static GlobalVariables instance;
    private boolean vld;
    private int MessageArrSize;

    public int getMessageArrSize() {
        return MessageArrSize;
    }

    public void setMessageArrSize(int messageArrSize) {
        MessageArrSize = messageArrSize;
    }

    public boolean isVld() {
        return vld;
    }
public boolean getVld(){
        return vld;
}
    public void setVld(boolean vld) {
        this.vld = vld;
    }

    private String globalVariable1;
    private String counsellorname;

    public String getLoggedUser() {
        return LoggedUser;
    }

    public void setLoggedUser(String loggedUser) {
        LoggedUser = loggedUser;
    }

    private String LoggedUser;

    public String getCounsellorname() {
        return counsellorname;
    }

    public void setCounsellorname(String counsellorname) {
        this.counsellorname = counsellorname;
    }

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    private int globalVariable2;
    // Add more global variables as needed

    private GlobalVariables() {
        // Private constructor to prevent direct instantiation
    }

    public static synchronized GlobalVariables getInstance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    public boolean isCounsillor(RadioButton radU,RadioButton radC){
        boolean r = true;
        if(radU.isChecked()){
            r=false;
        } else if (radC.isChecked()) {
            r=true;

        }
        return r;
    }

    public String getGlobalVariable1() {
        return globalVariable1;
    }

    public void setGlobalVariable1(String value) {
        globalVariable1 = value;
    }

    public int getGlobalVariable2() {
        return globalVariable2;
    }

    public void setGlobalVariable2(int value) {
        globalVariable2 = value;
    }
    // Add getter and setter methods for additional global variables
}
