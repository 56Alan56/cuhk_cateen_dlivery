package hk.edu.cuhk.ie.iems5722.group28.LoginUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import hk.edu.cuhk.ie.iems5722.group28.MyFirebaseMessagingService;
import hk.edu.cuhk.ie.iems5722.group28.R;

public class LoginActivity extends AppCompatActivity implements CallbackFragment{


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isGooglePlayServicesAvailable(this);
        addFragment();
    }

    private boolean isGooglePlayServicesAvailable(LoginActivity loginActivity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(loginActivity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(loginActivity, status, 9000).show();
            }
            return false;
        }
        return true;
    }


    public void addFragment(){
        FragmentLogin fragment = new FragmentLogin();
        fragment.setCallbackFragment(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();
    }

    public void replaceLoginFragment(){
        FragmentRegister fragment = new FragmentRegister();
        fragment.setCallbackFragment(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();

    }

    public void replaceRegisterFragment(){
        FragmentLogin fragment = new FragmentLogin();
        fragment.setCallbackFragment(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();

    }




    @Override
    public void jump2LoginFragment() {
        replaceRegisterFragment();
    }

    @Override
    public void jump2RegisterFragment() {
        replaceLoginFragment();
    }


}