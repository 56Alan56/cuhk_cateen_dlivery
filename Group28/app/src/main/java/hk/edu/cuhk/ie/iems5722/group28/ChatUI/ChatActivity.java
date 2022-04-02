package hk.edu.cuhk.ie.iems5722.group28.ChatUI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import hk.edu.cuhk.ie.iems5722.group28.R;

public class ChatActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);


        addFragment();
    }

    private void addFragment() {
        Fragment chatFragment = new ChatFragment();
        //Fragment blankFragment = new BlankFragment();
        Fragment mapFragment =  new MapsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.upFragmentContainer,mapFragment);
        fragmentTransaction.add(R.id.downFragmentContainer,chatFragment);
        fragmentTransaction.commit();
    }


}