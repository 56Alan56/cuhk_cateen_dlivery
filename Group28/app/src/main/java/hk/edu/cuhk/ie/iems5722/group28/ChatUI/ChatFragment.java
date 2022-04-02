package hk.edu.cuhk.ie.iems5722.group28.ChatUI;

import static hk.edu.cuhk.ie.iems5722.group28.LoginUI.FragmentLogin.UID;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.httpGet;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.postMsg2Server;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.serverIP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.group28.R;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    String hisUID;
    EditText editText;
    ImageButton sendButton;
    private boolean refreshState = false;
    private ArrayList<Msg> msgArrayList = new ArrayList<Msg>();
    private ListView listview;
    private MsgAdapter msgAdapter;
    private DownloadTask messageDownload;
    private int pageNum = 1;//default page=1 in the URL
    private int API_pageNum;//fetching the server page, if pageNum exceeds, toast
    private String URL;
    private boolean pageOverflowFlag = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        Intent intent = getActivity().getIntent();
        hisUID = intent.getStringExtra("hisID");

        msgAdapter = new MsgAdapter(getContext(),R.layout.msg_item,msgArrayList);
        listview = (ListView) view.findViewById(R.id.chatUI);

        Date currentTime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm");

        editText = view.findViewById(R.id.msgInputBox);
        sendButton = view.findViewById(R.id.msgSendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputMsg = editText.getText().toString();
                //sendButton.setBackgroundColor(getResources().getColor(R.color.design_default_color_secondary));
                if(inputMsg.equals("")){
                    Toast.makeText(getContext(),"Message Cannot be Blank!",Toast.LENGTH_LONG).show();
                }
                else{
                    Msg newMsg = new Msg(editText.getText(),UID,hisUID,ft.format(currentTime));
                    msgArrayList.add(newMsg);
                    msgAdapter.notifyDataSetChanged();
                    Collections.sort(msgArrayList, new Comparator<Msg>() {
                        @Override
                        public int compare(Msg msg, Msg t1) {
                            return (msg.MsgTime.compareTo(t1.MsgTime));
                        }
                    });
                    listview.setSelection(msgArrayList.size());
                    new AsyncTask<String,Integer,String>(){

                        @Override
                        protected String doInBackground(String... strings) {
                            try {
                                postMsg2Server(newMsg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                    editText.setText("");
                }
            }
        });

        //RECEIVE: Similar to fetching Chatroom List
        messageDownload = new DownloadTask();
        messageDownload.execute();
        return view;
    }

    private class DownloadTask extends AsyncTask<String,Integer,String> {
        String jsonString;
        @Override
        protected String doInBackground(String... strings) {
            jsonString = httpGet(("http://"+serverIP + "/get_messages/?id="+UID));
            try {
                JSONObject json = new JSONObject(jsonString);
//                API_pageNum = Integer.parseInt(json.getJSONObject("data").getString("total_pages"));
//                //Make a flag if reaches the last page and Handler it in the postExecute()
//                if (pageNum==API_pageNum){
//                    pageOverflowFlag=true;
//                }
                if (json.getString("status").equals("OK")){
                    JSONArray jsonArray = json.getJSONObject("data").getJSONArray("messages");
                    //API_pageNum = Integer.parseInt(json.getJSONObject("data").getString("current_page"));//use current_page or last page
                    for (int i = 0; i < jsonArray.length(); i++) {
                        msgArrayList.add(new Msg(jsonArray.getJSONObject(i)));
                        Collections.sort(msgArrayList, new Comparator<Msg>() {
                            @Override
                            public int compare(Msg msg, Msg t1) {
                                return (msg.MsgTime.compareTo(t1.MsgTime));
                            }
                        });
                    }
                }else{
                    System.out.println("Message API fetching error "+ json.getString("status"));
                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Refresh Everytime update
            msgAdapter.notifyDataSetChanged();
            listview.setAdapter(msgAdapter);

//            //Init the listview, if not the first page, startfromTop
//            if (pageNum==1){
//                listview.setAdapter(msgAdapter);
//            }
//            else if (refreshState){
//                listview.setSelection(msgArrayList.size());
//                refreshState = false;
//            }
//            else {
//                listview.setSelection(0);
//            }
//
//            //LastPage Handler: Toast the page overflow information and stop update
//            if (pageOverflowFlag){
//                Toast.makeText(getContext().getApplicationContext(),"Pages has been all loaded" ,Toast.LENGTH_LONG).show();
//            }

        }
    }


}