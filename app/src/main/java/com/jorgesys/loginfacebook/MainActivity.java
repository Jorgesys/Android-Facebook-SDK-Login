package com.jorgesys.loginfacebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Arrays;

/*
jorgesys12@gmail.com
https://stackoverflow.com/users/250260/jorgesys

Login for android using Android SDK.
https://developers.facebook.com/docs/facebook-login/android
app settings
https://developers.facebook.com/apps/
 */

public class MainActivity extends AppCompatActivity {


    private final static String TAG = "MainActivity";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*---------------Facebook retrieveLoginStatus -------------------*/
        //10. Comprobar el estado del inicio de sesión
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Log.i( TAG, "onCreate() isLoggedIn: " + isLoggedIn);
        //11. Activar el inicio de sesión rápido
        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"
                Log.i( TAG, "retrieveLoginStatus() onCompleted() getApplicationId: " + accessToken.getApplicationId());
                Log.i( TAG, "retrieveLoginStatus() onCompleted() getUserId: " + accessToken.getUserId());
                Log.i( TAG, "retrieveLoginStatus() onCompleted() getToken: " + accessToken.getToken());
            }
            @Override
            public void onFailure() {
                Log.e( TAG, "onFailure() No access token could be retrieved for the user.");
            }
            @Override
            public void onError(Exception exception) {
                Log.e( TAG, "onError() exception " + exception.getMessage());
            }
        });
        /*--------------------------------------------------------------------*/

        //9. Registrar una devolución de llamada
        callbackManager = CallbackManager.Factory.create();

      /*---------------Authentication using LoginButton -------------------*/
        /*LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i( TAG, "LoginButton onSuccess() loginResult: " + loginResult.hashCode());
                Log.i( TAG, "LoginButton onSuccess() getUserId: " + loginResult.getAccessToken().getUserId());
                Log.i( TAG, "LoginButton onSuccess() getApplicationId: " + loginResult.getAccessToken().getApplicationId());
                Log.i( TAG, "LoginButton onSuccess() getToken: " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
               Log.e(TAG, "LoginButton onCancel() ");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "LoginButton onError() " + exception.getMessage());
            }
        });*/
        /*---------------------------------------------------------------*/

        /*---------------Call automatic authentication in onCreate()-------------------------*/
        LoginManager manager = LoginManager.getInstance();
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile.fetchProfileForCurrentAccessToken();
                Log.i(TAG, "manager.registerCallback onSuccess() getUserId: " + loginResult.getAccessToken().getUserId());
                Log.i(TAG, "manager.registerCallback onSuccess() getApplicationId: " + loginResult.getAccessToken().getApplicationId());
                Log.i(TAG, "manager.registerCallback onSuccess() getToken: " + loginResult.getAccessToken().getToken());
            }
            @Override
            public void onError(FacebookException exception) {
                AccessToken.setCurrentAccessToken(null);
                //currentUserChanged();
                Log.e(TAG, "manager.registerCallback onError()! "  + exception.getMessage());
            }
            @Override
            public void onCancel() {
                AccessToken.setCurrentAccessToken(null);
                Log.e(TAG, "manager.registerCallback onCancel()! ");
            }
        });

        manager.logIn(this, Arrays.asList("public_profile"/*, "user_friends" : invalid scope*/) );

        Button buttonLogOut = (Button) findViewById(R.id.btnLogOut);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log OUT
                LoginManager.getInstance().logOut();
            }
        });
        /*---------------------------------------*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult() requestCode " + requestCode + " resultCode " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

}


