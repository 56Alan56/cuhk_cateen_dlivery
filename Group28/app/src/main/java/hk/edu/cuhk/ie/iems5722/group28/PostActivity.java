package hk.edu.cuhk.ie.iems5722.group28;

import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.httpGet;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.postPosts2Server;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.serverIP;
import static hk.edu.cuhk.ie.iems5722.group28.LoginUI.FragmentLogin.UID;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import hk.edu.cuhk.ie.iems5722.group28.LoginUI.FragmentLogin;
import hk.edu.cuhk.ie.iems5722.group28.MainUI.MainActivity;

public class PostActivity extends AppCompatActivity {

    TextView canteenHeader;
    Button btnSelectTime,btnPost;
    EditText etFee,etDest,etFood;
    String canteenName,time,fees,dest,food;
    ProgressBar pbPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();

        canteenName = intent.getStringExtra("CanteenName");
        canteenHeader = findViewById(R.id.canteen_name);
        canteenHeader.setText(canteenName);

        pbPost = findViewById(R.id.pb_post);

        btnPost = findViewById(R.id.button_post);
        btnSelectTime = findViewById(R.id.button_select_time);
        etFee = findViewById(R.id.et_fees);
        etDest = findViewById(R.id.et_destination);
        etFood=  findViewById(R.id.et_food);




        //Set Timing
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(PostActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String hourDisplay,minDisplay;
                                if (minute<10){
                                    minDisplay = "0" + String.valueOf(minute);
                                }else{
                                    minDisplay = String.valueOf(minute);
                                }
                                time = String.format("%d:%s", hourOfDay, minDisplay);
                                TextView tvSelectTimeShow = findViewById(R.id.tv_selected_time);
                                tvSelectTimeShow.setText(time);
                            }
                        }, 0, 0, true);
                timePickerDialog.show();

            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fees = etFee.getText().toString();
                food = etFood.getText().toString();
                dest = etDest.getText().toString();
                //new PushPostTask().execute();
                pbPost.setVisibility(View.VISIBLE);
                new Thread(new PostThread()).start();

            }
        });

    }

//    private class PushPostTask extends AsyncTask<String,Integer,String> {
//        String res;
//        @Override
//        protected String doInBackground(String... strings) {
//            //String url = "http://"+ serverIP + "/push_posts/?time="+time+"&dest="+dest+"&fees="+fees+"&food="+food+"&canteen="+canteenName+"&orderer=1155162635"
//            try {
//                res = postPosts2Server(time,fees,dest,food,canteenName); //server
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

    private Handler PostHandler=new Handler(){
        public void handleMessage(Message msg){
            pbPost.setVisibility(View.GONE);

            if(msg.what==104){
                String info="";
                String flag=(String)msg.obj;
                if(flag.equals("1")){
                    info="Successfully Post！";
                    Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }else if(flag.equals("2")){
                    info="Please check your input Your Parameters ！";
                }else{
                    info="Server/Network issue, please try later！";
                }
                Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
            }
        }
    };
    class PostThread implements Runnable{
        public void run(){
            String flag = "0"; //server
            try {
                flag = postPosts2Server(time,fees,dest,food,canteenName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //String flag = httpGet("http://"+ serverIP +"/login/?id="+userID+"&pwd="+password);
            PostHandler.obtainMessage(104, flag).sendToTarget();

        }
    }




}