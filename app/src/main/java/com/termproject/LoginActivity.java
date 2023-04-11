package com.termproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private WeightsDatabase mWeightsDatabase;
    private final AuthenticatedUserManager mAuthManager = AuthenticatedUserManager.getInstance();
    private Toolbar mToolbar;
    private EditText mUsernameInput;
    private EditText mPasswordInput;
    private Button mLoginBtn;
    private Button mCreateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // singleton
        mWeightsDatabase = WeightsDatabase.getInstance(getApplicationContext());

        // initialize toolbar
        mToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        // initialize buttons
        mLoginBtn = findViewById(R.id.loginBtn);
        mLoginBtn.setOnClickListener(v -> handleLoginButton());
        mCreateBtn = findViewById(R.id.createBtn);
        mCreateBtn.setOnClickListener(v -> handleCreateButton());

        // initialize text inputs
        mUsernameInput = findViewById(R.id.usernameInput);
        mPasswordInput = findViewById(R.id.passwordInput);
    }

    /**
     * OnClick Listener for the Login button
     **/
    private void handleLoginButton(){
        // acquire the input username and password from inputs
        String usernameInput = String.valueOf(mUsernameInput.getText());
        String passwordInput = String.valueOf(mPasswordInput.getText());

        // validate the login with the database
        if (mWeightsDatabase.validateLogin(usernameInput, passwordInput)){
            // activate them as the authenticated user
            AuthenticatedUser newUser = new AuthenticatedUser(usernameInput);
            mAuthManager.setUser(newUser);

            // route them to the home screen and pass username
            Intent intent = new Intent(this, HomeActivity.class);
            finish();
            LoginActivity.this.startActivity(intent);
        }
        else{
            // incorrect login
            String text = getString(R.string.incorrect_login);
            Toast toast = Toast.makeText(getApplicationContext(),
                    text , Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * OnClick Listener for the Create button
     **/
    private void handleCreateButton(){
        // acquire the input username and password from inputs
        String usernameInput = String.valueOf(mUsernameInput.getText());
        String passwordInput = String.valueOf(mPasswordInput.getText());

        // try creating a new account
        if (mWeightsDatabase.createAccount(usernameInput, passwordInput)){
            // activate them as the authenticated user
            AuthenticatedUser newUser = new AuthenticatedUser(usernameInput);
            mAuthManager.setUser(newUser);

            // route them to the home screen and pass username
            Intent intent = new Intent(this, HomeActivity.class);
            finish();
            LoginActivity.this.startActivity(intent);
        }
        else{
            // account creation failed
            String text = getString(R.string.incorrect_create);
            Toast toast = Toast.makeText(getApplicationContext(),
                    text , Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}