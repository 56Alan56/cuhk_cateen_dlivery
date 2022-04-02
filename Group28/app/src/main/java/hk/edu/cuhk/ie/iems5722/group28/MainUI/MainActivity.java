package hk.edu.cuhk.ie.iems5722.group28.MainUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import hk.edu.cuhk.ie.iems5722.group28.ChatUI.ChatActivity;
import hk.edu.cuhk.ie.iems5722.group28.LoginUI.LoginActivity;
import hk.edu.cuhk.ie.iems5722.group28.MyFirebaseMessagingService;
import hk.edu.cuhk.ie.iems5722.group28.PostActivity;
import hk.edu.cuhk.ie.iems5722.group28.R;
import hk.edu.cuhk.ie.iems5722.group28.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dining, R.id.navigation_delivery,R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);



        //mActionBar.setTitle("TEST");


    }

    /**
     * 复写：添加菜单布局
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    /**
     * 复写：设置菜单监听
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //actionbar navigation up 按钮
            case android.R.id.home:
                finish();
                break;

            case R.id.post_add:
                //Toast.makeText(this, "Post an Activity!", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, PostActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

}