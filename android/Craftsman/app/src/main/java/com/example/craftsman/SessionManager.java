package com.example.craftsman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    //call class SharedPreferences
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;


    private  static final String PREF_NAME ="LOGIN";
    private  static final String LOGIN ="IS_LOGIN";
    public  static final String NAME ="NAME";
    public  static final String EMAIL ="EMAIL";
    public  static final String ID ="ID";
    public  static final String TYPE ="TYPE";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();

    }
    public void createSession(String name, String email,String id, String type){
        //add name and email to session
        editor.putBoolean(LOGIN,true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(ID, id);
        editor.putString(TYPE, type);
//        editor.apply();
        editor.commit();

    }
    // Get Login State
    public boolean islogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }



    public void checkLogin(){
        if(!this.islogin())
        {
            // user is not logged in redirect him to Login Activity
            Intent i =  new Intent(context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            context.startActivity(i);
            ((MainActivity)context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        //HashMap<String,String> user =  new HashMap<>();
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(NAME,sharedPreferences.getString(NAME,null));
        // user email
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        // user id
        user.put(ID,sharedPreferences.getString(ID,null));
        // user type
        user.put(TYPE,sharedPreferences.getString(TYPE,null));
        return user;
    }

    public void logout() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
        Intent i =  new Intent(context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);

    }

    public void logoutAdmin() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
        Intent i =  new Intent(context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);

    }

}
