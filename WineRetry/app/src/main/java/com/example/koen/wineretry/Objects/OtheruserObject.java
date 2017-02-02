package com.example.koen.wineretry.Objects;

/* Created by Koen Zijlstra
* University of Amsterdam
* Student number: 10741615
* Coarse: Programmeerproject
*
* Object that contains the username and ID of the other user. The name is used for displaying the
* names of all people that current user has a chat with, and the id is used for opening the
* correct chat in ChatActivity
*/


public class OtheruserObject {

    public String usernameother;
    public String userIDother;

    public OtheruserObject(){
    }

    public OtheruserObject(String usernameother, String userIDother){
        this.usernameother = usernameother;
        this.userIDother = userIDother;
    }

    public String getUsernameother(){
        return usernameother;
    }
    public String getUserIDother(){
        return userIDother;
    }

    // setters

}





