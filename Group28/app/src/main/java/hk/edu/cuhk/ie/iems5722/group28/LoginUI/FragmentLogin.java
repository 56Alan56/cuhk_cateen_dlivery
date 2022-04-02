package hk.edu.cuhk.ie.iems5722.group28.LoginUI;

import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.httpGet;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.serverIP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import hk.edu.cuhk.ie.iems5722.group28.MainUI.MainActivity;
import hk.edu.cuhk.ie.iems5722.group28.R;

public class FragmentLogin extends Fragment {
    public static String UID = "";
    public static String PASSWORD = "";

    Button btnLogin,btnRegister, btnDebug1,btnDebug2;
    TextView etUserID,etPassword;
    ProgressBar pbLogin;
    CallbackFragment callbackFragment;
    String userID,password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox cbRemember;
    boolean remember = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Init UI widgets
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        etUserID = view.findViewById(R.id.et_uid);
        etPassword = view.findViewById(R.id.et_password);
        btnRegister = view.findViewById(R.id.btn_register);
        btnLogin = view.findViewById(R.id.btn_login);
        btnDebug1 = view.findViewById(R.id.btn_debugger1);
        btnDebug2 = view.findViewById(R.id.btn_debugger2);
        pbLogin = view.findViewById(R.id.pb_login);
        cbRemember = view.findViewById(R.id.login_remember);

        sharedPreferences = getActivity().getSharedPreferences("account",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        readSharedPreferences();

        //Set Listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = etUserID.getText().toString();
                password = etPassword.getText().toString();
                if(doValidateForm()){
                    pbLogin.setVisibility(View.VISIBLE);
                    new Thread(new LoginThread()).start();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callbackFragment!=null){
                    callbackFragment.jump2RegisterFragment();
                }
            }
        });
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cbRemember.isChecked()){
                    remember=true;
                }else{
                    remember=false;
                }
            }
        });
        btnDebug1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MainActivity.class));
                UID = "1155162635";
            }
        });
        btnDebug2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MainActivity.class));
                UID = "1155160918";
            }
        });


        return view;
    }

    public void setCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }

    //Remember Login status
    private void saveSharedPreferences(){
        if(remember){
            editor.putString("uid", userID);
            editor.putString("pwd", password);
        }else{
            editor.clear();
        }
        editor.commit();
    }

    private void readSharedPreferences(){
        String uid = sharedPreferences.getString("uid", "");
        String pwd = sharedPreferences.getString("pwd", "");
        etUserID.setText(uid);
        etPassword.setText(pwd);
        if(!(uid.equals("") &&pwd.equals(""))){
            remember=true;
            cbRemember.setChecked(true);
        }
    }

    //Validation
    private boolean doValidateForm(){
        if(userID.length()!=10){
            Toast.makeText(getContext(), "Please input a validate CUHK Link ID!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(password.length()<1){
            Toast.makeText(getContext(), "password can't be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private Handler LoginHandler=new Handler(){
        public void handleMessage(Message msg){
            pbLogin.setVisibility(View.GONE);

            if(msg.what==103){
                String info="";
                String flag=(String)msg.obj;
                if(flag.equals("1")){
                    info="Successfully Login！";
                    Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
                    UID = userID;
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }else if(flag.equals("0")){
                    info="The account does not exist！";
                }else if(flag.equals("3")){
                    info="The password is not correct！";
                }else{
                    info="Server issue, please try later！";
                }
                Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
            }
        }
    };
    class LoginThread implements Runnable{
        public void run(){
            String flag = httpGet("http://"+ serverIP +"/login/?id="+userID+"&pwd="+password);
            if(flag.equals("1")){
                //记住用户登录信息
                saveSharedPreferences();
            }
            LoginHandler.obtainMessage(103, flag).sendToTarget();
        }
    }
}
