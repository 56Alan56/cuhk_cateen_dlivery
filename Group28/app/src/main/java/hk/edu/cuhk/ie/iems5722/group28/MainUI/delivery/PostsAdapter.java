package hk.edu.cuhk.ie.iems5722.group28.MainUI.delivery;

import static hk.edu.cuhk.ie.iems5722.group28.LoginUI.FragmentLogin.UID;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.httpGet;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.postStartStatus2Server;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.serverIP;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.group28.ChatUI.ChatActivity;
import hk.edu.cuhk.ie.iems5722.group28.MainUI.MainActivity;
import hk.edu.cuhk.ie.iems5722.group28.R;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsVH> {

    List<Posts> postsList;

    public PostsAdapter(List<Posts> postsList) {
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public PostsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        return new PostsVH(view);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_post;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsVH holder, int position) {
        Posts posts = postsList.get(position);
        holder.headerTv.setText(posts.getHeader());
        holder.subHeaderLeftTv.setText(posts.getSubHeaderLeft());
        holder.subHeaderMidTv.setText(posts.getSubHeaderMid());
        holder.subHeaderRightTv.setText(posts.getSubHeaderRight());
        holder.descriptionTv.setText(posts.getDescription1());
        holder.description2Tv.setText(posts.getDescription2());

        holder.btnDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext().getApplicationContext(), ChatActivity.class);
                intent.putExtra("hisID",posts.getDescription1().replaceAll("Orderer: ",""));
                view.getContext().startActivity(intent);
                new PushTask().execute(posts.getDescription2());
                Toast.makeText(view.getContext(), "Transaction Starts!", Toast.LENGTH_SHORT).show();
                //Send the post to server database and change the transaction status
            }
        });

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class PostsVH extends RecyclerView.ViewHolder {

        TextView headerTv, subHeaderLeftTv, subHeaderRightTv, subHeaderMidTv, descriptionTv,description2Tv;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        Button btnDeliver;


        public PostsVH(@NonNull View itemView) {
            super(itemView);
            headerTv = itemView.findViewById(R.id.Header);
            subHeaderLeftTv = itemView.findViewById(R.id.subheader_value_left);
            subHeaderMidTv = itemView.findViewById(R.id.subheader_value_mid);
            subHeaderRightTv = itemView.findViewById(R.id.subheader_value_right);
            descriptionTv = itemView.findViewById(R.id.description);
            description2Tv = itemView.findViewById(R.id.description2);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            btnDeliver = itemView.findViewById(R.id.button_deliver);

//            linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Posts posts = postsList.get(getAdapterPosition());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });


        }
    }


    private class PushTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                postStartStatus2Server(UID,strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }






}
