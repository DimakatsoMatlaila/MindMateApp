package com.example.login;

public class GlobalVariables {
    private static GlobalVariables instance;

    private String globalVariable1;
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
