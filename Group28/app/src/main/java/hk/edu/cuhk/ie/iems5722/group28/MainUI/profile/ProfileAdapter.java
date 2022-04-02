package hk.edu.cuhk.ie.iems5722.group28.MainUI.profile;

import static hk.edu.cuhk.ie.iems5722.group28.LoginUI.FragmentLogin.UID;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.httpGet;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.postFinishStatus2Server;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.serverIP;

import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.group28.ChatUI.ChatActivity;
import hk.edu.cuhk.ie.iems5722.group28.MainUI.delivery.PostsAdapter;
import hk.edu.cuhk.ie.iems5722.group28.R;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.PostsVH> {

    List<Posts> postsList;

    public ProfileAdapter(List<Posts> postsList) {
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
        return R.layout.item_profile;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsVH holder, int position) {
        Posts posts = postsList.get(position);
        holder.headerTv.setText(posts.getHeader());
        holder.subHeaderLeftTv.setText(posts.getSubHeaderLeft());
        holder.subHeaderMidTv.setText(posts.getSubHeaderMid());
        holder.subHeaderRightTv.setText(posts.getSubHeaderRight());
        holder.descriptionTv.setText("Orderer: " + posts.getDescription1());
        holder.description2Tv.setText(posts.getSupplementary_text() + " : " + posts.getDescription2());
        holder.description3Tv.setText("Deliverer: " + posts.getDescription3());

        if (posts.getHeader().equals("Pending")||posts.getHeader().equals("Finished")){
            holder.btnDetails.setVisibility(View.GONE);
            holder.btnFinished.setVisibility(View.GONE);
        }

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            String hisUID;
            @Override
            public void onClick(View view) {
                if (posts.getDescription1().equals(UID)){
                    hisUID = posts.getDescription3();
                }else{
                    hisUID = posts.getDescription1();
                }

                Intent intent = new Intent(view.getContext().getApplicationContext(), ChatActivity.class);
                intent.putExtra("hisID",hisUID);
                view.getContext().startActivity(intent);
            }
        });

        holder.btnFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new PushTask().execute(posts.getDescription2());
                new AsyncTask<String,Integer,String>(){

                        @Override
                        protected String doInBackground(String... strings) {
                            try {
                                postFinishStatus2Server(posts.getDescription3()+"To"+posts.getDescription1(),strings[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute(posts.getDescription2());
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class PostsVH extends RecyclerView.ViewHolder {

        TextView headerTv, subHeaderLeftTv, subHeaderRightTv, subHeaderMidTv, descriptionTv,description2Tv,description3Tv;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        Button btnDetails,btnFinished;


        public PostsVH(@NonNull View itemView) {
            super(itemView);
            headerTv = itemView.findViewById(R.id.Header);
            subHeaderLeftTv = itemView.findViewById(R.id.subheader_value_left);
            subHeaderMidTv = itemView.findViewById(R.id.subheader_value_mid);
            subHeaderRightTv = itemView.findViewById(R.id.subheader_value_right);
            descriptionTv = itemView.findViewById(R.id.description);
            description2Tv = itemView.findViewById(R.id.description2);
            description3Tv = itemView.findViewById(R.id.description3);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            btnDetails = itemView.findViewById(R.id.button_details);
            btnFinished = itemView.findViewById(R.id.button_finished);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Posts posts = postsList.get(getAdapterPosition());
                    notifyItemChanged(getAdapterPosition());
                }
            });


        }
    }




}
