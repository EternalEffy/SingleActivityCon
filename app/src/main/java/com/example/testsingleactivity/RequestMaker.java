package com.example.testsingleactivity;

public class RequestMaker {
    private String request;

    public String makeRequestLogin(String login, String password){
        return ("{\"request\":\""+Requests.get+"\"," +
                "\"userData\":"+"[\""+login+"\",\""+password+"\",\""+"\"]}");
    }

    public String makeRequestReg(String login,String password,String fullName,String birthday,String eMail){
        return "{\"request\":\""+Requests.get+"\"," +
                "\"userData\":"+"[\""+login+"\",\""+password+"\",\""+fullName+"\",\""+birthday+"\",\""+eMail+"\"]}";
    }
}
