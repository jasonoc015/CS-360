package com.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private AuthenticatedUserManager mAuthManager = AuthenticatedUserManager.getInstance();
    private Toolbar mToolbar;
    private RecyclerView mWeightsList;
    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // initialize toolbar
        mToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        // initialize recycler view
        mWeightsList = findViewById(R.id.weights_list);
        mWeightsList.setLayoutManager((new LinearLayoutManager(getApplicationContext())));
        // send weights to recycler view
        EntryAdapter adapter = new EntryAdapter(
                WeightsDatabase.getInstance(getApplicationContext()).getEntries(
                        mAuthManager.getUser().getUsername()));
        mWeightsList.setAdapter(adapter);

        // initialize fab
        mFAB = findViewById(R.id.floatingActionButton);
        mFAB.setOnClickListener(v -> handleFloatingActionButton());

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        // save username to saved instance state bundle
        savedInstanceState.putString("username", mAuthManager.getUser().getUsername());
    }

    /**
     * OnClick Listener for the Floating Action Button
     **/
    private void handleFloatingActionButton(){
        // launch the add weight activity
        Intent intent = new Intent(this, AddWeightActivity.class);
        intent.putExtra("username", mAuthManager.getUser().getUsername());
        HomeActivity.this.startActivity(intent);
    }

    /**
     * Handle the App bar being clicked.
     * @param item - the menu item that was selected.
     *
     * @return bool
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.profileIcon) {
            // launch the add weight activity
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("username", mAuthManager.getUser().getUsername());
            HomeActivity.this.startActivity(intent);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    private class EntryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Entry mEntry;
        private TextView mTextView;

        public EntryHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_weight, parent, false));
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.weightTextView);
        }

        public void bind(Entry entry, int position) {
            mEntry = entry;

            // check current day for "Today" label
            String todayTimeStamp = new SimpleDateFormat("yyyy-MM-dd").format(
                    Calendar.getInstance().getTime());
            if (entry.getDate().equals(todayTimeStamp)){
                mTextView.setText(getString(R.string.weight_list_item, getString(R.string.today),
                        String.valueOf(entry.getWeight())));
            }
            else{
                mTextView.setText(getString(R.string.weight_list_item, entry.getDate(),
                        String.valueOf(entry.getWeight())));
            }
        }

        @Override
        public void onClick(View view){
            // Start QuestionActivity, indicating what subject was clicked
            Intent intent = new Intent(HomeActivity.this, AddWeightActivity.class);
            intent.putExtra("date", mEntry.getDate());
            intent.putExtra("weight", mEntry.getWeight());
            startActivity(intent);
        }
    }

    private class EntryAdapter extends RecyclerView.Adapter<EntryHolder> {

        private List<Entry> mEntryList;

        public EntryAdapter(List<Entry> entries) {
            mEntryList = entries;
        }

        @Override
        public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new EntryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EntryHolder holder, int position){
            holder.bind(mEntryList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mEntryList.size();
        }
    }
}