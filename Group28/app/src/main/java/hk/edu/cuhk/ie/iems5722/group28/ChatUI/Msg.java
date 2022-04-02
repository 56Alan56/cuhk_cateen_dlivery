package hk.edu.cuhk.ie.iems5722.group28.ChatUI;

import static hk.edu.cuhk.ie.iems5722.group28.LoginUI.FragmentLogin.UID;

import android.text.Editable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Msg {
    public static final int MSG_RECEIVE = 0;
    public static final int MSG_SEND = 1;
    public String MsgText;
    public String MsgTime;
    public String Sender;
    public int MsgType;
    public String Receiver;

    //Normal Constructor, unused
    public Msg(String Text, String Time, int Type) {
        this.MsgText = Text;
        this.MsgTime = Time;
        this.MsgType = Type;
    }


    //Sender Constructor
    public Msg(Editable text, String sender,String receiver,String time) {
        this.MsgText = String.valueOf(text);
        this.MsgTime = time;
        this.Sender = sender;
        this.Receiver = receiver;
        this.MsgType = MSG_SEND;
    }



    //Receiver Constructor
    public Msg(JSONObject jsonObject) throws JSONException, ParseException {
        this.MsgText = jsonObject.getString("message");
        //Time Convert
        String message_time = jsonObject.getString("message_time");
        SimpleDateFormat old_sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        Date date = old_sdf.parse(message_time);
        SimpleDateFormat new_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.MsgTime = new_sdf.format(date);

        this.Sender = jsonObject.getString("sender_id");
        this.Receiver = jsonObject.getString("receiver_id");
        if (jsonObject.getString("sender_id").equals(UID)) {
            this.MsgType = MSG_SEND;
        } else {
            this.MsgType = MSG_RECEIVE;
        }


    }

}
