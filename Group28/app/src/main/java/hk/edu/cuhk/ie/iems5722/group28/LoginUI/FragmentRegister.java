package hk.edu.cuhk.ie.iems5722.group28.LoginUI;

import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.httpGet;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.serverIP;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import hk.edu.cuhk.ie.iems5722.group28.R;

public class FragmentRegister extends Fragment {


    CallbackFragment callbackFragment;
    Button btnRegister,btnSendEmail;
    TextView etUserName,etPassword,etUID,etVeriCode;
    String userID,userName,password,userEmail,veriCode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar pbRegister;

//    @Override
//    public void onAttach(@NonNull Context context) {
//        sharedPreferences = context.getSharedPreferences("usersFile",Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        super.onAttach(context);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        etUID = view.findViewById(R.id.et_uid);
        etUserName = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        etVeriCode = view.findViewById(R.id.et_email_verification_code);
        btnRegister = view.findViewById(R.id.btn_register);
        pbRegister = view.findViewById(R.id.pb_register);
        btnSendEmail = view.findViewById(R.id.btn_send_email);
        //sharedPreferences = getContext().getSharedPreferences("account",Context.MODE_PRIVATE);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btnRegister.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.design_default_color_primary)));
                btnRegister.setEnabled(true);
                userID = etUID.getText().toString();
                if(userID.length()!=10){
                    Toast.makeText(getContext(), "Please input a validate ID", Toast.LENGTH_SHORT).show();
                }else{
                    pbRegister.setVisibility(View.VISIBLE);
                    new Thread(new VerificationThread()).start();
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = etUserName.getText().toString();
                password = etPassword.getText().toString();
                userID = etUID.getText().toString();
                veriCode = etVeriCode.getText().toString();
                if(doValidateForm()){
                    pbRegister.setVisibility(View.VISIBLE);
                    //开启线程进行用户登录处理
                    new Thread(new RegisterThread()).start();
                }
            }
        });


        return view;
    }

    private boolean doValidateForm(){
        if(userName.length()<1){
            Toast.makeText(getContext(), "Username can't be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(password.length()<1){
            Toast.makeText(getContext(), "password can't be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void setCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }

    private Handler RegisterHandler=new Handler(){
        public void handleMessage(@NonNull Message msg){
            pbRegister.setVisibility(View.GONE);
            if(msg.what==100){
                String info="";
                String flag=(String)msg.obj;
                if(flag.equals("1")){
                    info="Successfully Registered！";
                    Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
                    //Intent intent=new Intent(LoginActivity.this,OtherActivity.class);
                    //startActivity(intent);
                    if(callbackFragment!=null) {
                        callbackFragment.jump2LoginFragment();
                    }

                    return;
                }else if(flag.equals("0")){
                    info="The account already exists！";
                }else if(flag.equals("3")) {
                    info = "The VeriCode is not correct!";
                }
                else{
                    info="Server issue, please try later！";
                }
                System.out.println("The flag is " + flag);
                Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
            }
        }
    };
    class RegisterThread implements Runnable{
        public void run(){
            String flag = httpGet("http://"+ serverIP +"/register/?name=" +userName+"&pwd="+password+"&id="+userID+"&veri="+veriCode);
            RegisterHandler.obtainMessage(100, flag).sendToTarget();
            System.out.println("The response flag is:" + flag);
        }
    }



    private Handler VerificationHandler=new Handler(){
        public void handleMessage(Message msg){
            pbRegister.setVisibility(View.GONE);

            if(msg.what==101){
                String info="";
                String flag=(String)msg.obj;
                if(flag.equals("sent")){
                    info="Please check your CUHK Link email for the verification Code";
                }else{
                    info="Server/Network issue, please resend！";
                }
                Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
            }
        }
    };
    class VerificationThread implements Runnable{
        public void run(){
            String flag = httpGet("http://"+ serverIP +"/verification/?id="+userID);
            VerificationHandler.obtainMessage(101, flag).sendToTarget();
        }
    }
}
