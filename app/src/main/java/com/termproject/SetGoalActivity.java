package com.termproject;

import static java.lang.Float.parseFloat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetGoalActivity extends AppCompatActivity {

    private String mCurrentUsername;
    private WeightsDatabase mWeightsDatabase;
    private Toolbar mToolbar;
    private EditText mNewGoalInput;
    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);

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

        // initialize edit text
        mNewGoalInput = findViewById(R.id.newGoalDecimal);
        float currentGoal = mWeightsDatabase.getGoal(mCurrentUsername);
        if (currentGoal != 0.0){
            mNewGoalInput.setText(String.valueOf(currentGoal));
        }

        // initialize button
        mSubmitButton = findViewById(R.id.submitGoalBtn);
        mSubmitButton.setOnClickListener(v -> handleSubmitButton());
    }

    /**
     * OnClick Listener for the Submit button
     *
     **/
    private void handleSubmitButton(){
        // acquire value from input
        String inputValue = String.valueOf(mNewGoalInput.getText());
        if (inputValue.equals("")){
            String text = getString(R.string.empty_input);
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            // acquire the value from the input text
            float newGoal = parseFloat(inputValue);

            // set the new goal in the database
            mWeightsDatabase.setGoal(newGoal, mCurrentUsername);

            // redirect to the profile screen
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("username", mCurrentUsername);
            finish();
            SetGoalActivity.this.startActivity(intent);
        }
    }
}