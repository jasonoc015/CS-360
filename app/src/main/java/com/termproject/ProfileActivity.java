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

    private String mCurrentUsername;
    private WeightsDatabase mWeightsDatabase;
    private Toolbar mToolbar;
    private TextView mGoalWeightDisplay;
    private Button mEditGoalButton;
    private ToggleButton mEnableSmsToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCurrentUsername = extras.getString("username");
        }
        else {
            // check saved instance state
            try{
                mCurrentUsername = savedInstanceState.getString("username");
            }
            catch (RuntimeException e){
                throw new RuntimeException();
            }
        }

        // singleton
        mWeightsDatabase = WeightsDatabase.getInstance(getApplicationContext());

        // initialize toolbar
        mToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        // initialize text view
        mGoalWeightDisplay = findViewById(R.id.goalWeight);
        float currentGoal = mWeightsDatabase.getGoal(mCurrentUsername);
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
        intent.putExtra("username", mCurrentUsername);
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

    /**
     * Handle the App bar being clicked.
     * @param item The menu item that was selected.
     *
     * @return bool
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.profileIcon) {
            // FIXME: route to profile activity
            return true;
        }
        else{
            // FIXME: the thing that was clicked was unrecognized
            return super.onOptionsItemSelected(item);
        }
    }

    // note that we do not need the profile icon in app bar here because we are already in
    // the profile section
}