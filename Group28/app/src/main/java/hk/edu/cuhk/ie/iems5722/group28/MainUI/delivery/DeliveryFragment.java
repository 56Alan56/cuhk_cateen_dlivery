package hk.edu.cuhk.ie.iems5722.group28.MainUI.delivery;

import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.httpGet;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.serverIP;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.group28.MainUI.dining.Canteens;
import hk.edu.cuhk.ie.iems5722.group28.MainUI.dining.CanteensAdapter;
import hk.edu.cuhk.ie.iems5722.group28.R;

public class DeliveryFragment extends Fragment {
    RecyclerView recyclerView;
    List<hk.edu.cuhk.ie.iems5722.group28.MainUI.delivery.Posts> postsList;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delivery, container, false);

        recyclerView = view.findViewById(R.id.delivery_recyclerview);
        //initData();
        new DownloadTask().execute();
        return view;
    }

//    private void initData() {
//        postsList = new ArrayList<>();
//        postsList.add(new Posts("Post1","3:15","ShanHeng College","5 HKD","Chicken Fired Rice(with extra rice)"));
//        postsList.add(new Posts("Post1","3:15","5 HKD","ShanHeng College","Chicken Fired Rice(with extra rice)"));
//        postsList.add(new Posts("Post1","3:15","5 HKD","ShanHeng College","Chicken Fired Rice(with extra rice)"));
//        postsList.add(new Posts("Post1","3:15","5 HKD","ShanHeng College","Chicken Fired Rice(with extra rice)"));
//        postsList.add(new Posts("Post1","3:15","5 HKD","ShanHeng College","Chicken Fired Rice(with extra rice)"));
//        postsList.add(new Posts("Post1","3:15","5 HKD","ShanHeng College","Chicken Fired Rice(with extra rice)"));
//
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class DownloadTask extends AsyncTask<String,Integer,String> {
        String jsonString;
        @Override
        protected String doInBackground(String... strings) {
            jsonString = httpGet("http://"+ serverIP + "/get_posts/"); //server
            try {
                JSONObject json = new JSONObject(jsonString);
                if (json.getString("status").equals("OK")){
                    JSONArray jsonArray = json.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        postsList.add(new Posts(jsonArray.getJSONObject(i)));
//                        Collections.sort(postsList, new Comparator<Posts>() {
//                            @Override
//                            public int compare(Posts posts, Posts t1) {
//                                return (posts.getCreated_time().compareTo(t1.getCreated_time()));
//                            }
//                        });
//                    }
                    postsList = Posts.fromJSONArray(jsonArray);
                    System.out.println("Status OK");
                }
                else{
                    System.out.println("ChatroomAPI Fetching Error" + json.getString("status"));
                }
//                //Former Testing on ArrayAdapter
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    chatroomNameList.add(jsonArray.getJSONObject(i).getString("name"));
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PostsAdapter postsAdapter = new PostsAdapter(postsList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(postsAdapter);

        }
    }

}