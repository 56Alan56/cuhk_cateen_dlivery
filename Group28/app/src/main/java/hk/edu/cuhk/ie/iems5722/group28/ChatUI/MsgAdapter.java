package hk.edu.cuhk.ie.iems5722.group28.ChatUI;



import static hk.edu.cuhk.ie.iems5722.group28.LoginUI.FragmentLogin.UID;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.group28.R;


public class MsgAdapter extends ArrayAdapter<Msg> {
    public MsgAdapter(@NonNull Context context, int resource, @NonNull List<Msg> objects) {
        super(context, resource, objects);

    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Msg msg = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_item, parent, false);
        }

        // Lookup view for data population
        LinearLayout send_layout = (LinearLayout)convertView.findViewById(R.id.send_msg_layout);
        LinearLayout receive_layout = (LinearLayout)convertView.findViewById(R.id.receive_msg_layout);

        TextView send_text_tv = (TextView) convertView.findViewById(R.id.send_msg_text);
        TextView send_time_tv = (TextView) convertView.findViewById(R.id.send_msg_time);
        TextView send_name_tv = (TextView) convertView.findViewById(R.id.send_msg_name);

        TextView receive_text_tv = (TextView) convertView.findViewById(R.id.receive_msg_text);
        TextView receive_time_tv = (TextView) convertView.findViewById(R.id.receive_msg_time);
        TextView receive_name_tv = (TextView) convertView.findViewById(R.id.receive_msg_name);

        // Populate the data into the template view using the data object
        if(msg.MsgType==Msg.MSG_RECEIVE){
            receive_layout.setVisibility(View.VISIBLE);
            send_layout.setVisibility(View.GONE);
            receive_text_tv.setText(msg.MsgText);
            receive_time_tv.setText(msg.MsgTime);
            receive_name_tv.setText(msg.Sender);
        }else{
            receive_layout.setVisibility(View.GONE);
            send_layout.setVisibility(View.VISIBLE);
            send_text_tv.setText(msg.MsgText);
            send_time_tv.setText(msg.MsgTime);
            send_name_tv.setText(msg.Sender);

        }

        // Return the completed view to render on screen
        return convertView;
    }
}