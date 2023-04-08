package com.termproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ProfileActivity extends AppCompatActivity {

    private AuthenticatedUserManager mAuthManager = AuthenticatedUserManager.getInstance();
    private WeightsDatabase mWeightsDatabase;
    private Toolbar mToolbar;
    private TextView mGoalWeightDisplay;
    private Button mEditGoalButton;
    private ToggleButton mEnableSmsToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // singleton
        mWeightsDatabase = WeightsDatabase.getInstance(getApplicationContext());

        // initialize toolbar
        mToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        // initialize text view
        mGoalWeightDisplay = findViewById(R.id.goalWeight);
        float currentGoal = mWeightsDatabase.getGoal(mAuthManager.getUser().getUsername());
        if (currentGoal != 0.0){
            mGoalWeightDisplay.setText(String.valueOf(currentGoal));
        }
        else{
            mGoalWeightDisplay.setText(R.string.notset);
        }

        // initialize button
        mEditGoalButton = findViewById(R.id.editGoalBtn);
        mEditGoalButton.setOnClickListener(v -> handleEditGoalButton());

        // initialize toggle button
        mEnableSmsToggleButton = findViewById(R.id.enableSmsToggleButton);
        mEnableSmsToggleButton.setOnClickListener(v -> handleToggleSmsButton());
    }

    /**
     * OnClick Listener for the Edit Goal button
     *
     **/
    private void handleEditGoalButton(){
        Intent intent = new Intent(this, SetGoalActivity.class);
        finish();
        ProfileActivity.this.startActivity(intent);
    }

    /**
     * OnClick Listener for the Toggle SMS button
     *
     **/
    private void handleToggleSmsButton(){
        // FIXME: implement this
    }
}