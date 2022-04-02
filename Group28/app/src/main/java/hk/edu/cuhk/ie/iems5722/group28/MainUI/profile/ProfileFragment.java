package hk.edu.cuhk.ie.iems5722.group28.MainUI.profile;

import static hk.edu.cuhk.ie.iems5722.group28.LoginUI.FragmentLogin.UID;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.httpGet;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.serverIP;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hk.edu.cuhk.ie.iems5722.group28.MainUI.delivery.DeliveryFragment;
import hk.edu.cuhk.ie.iems5722.group28.MainUI.delivery.PostsAdapter;
import hk.edu.cuhk.ie.iems5722.group28.R;
import hk.edu.cuhk.ie.iems5722.group28.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    RecyclerView recyclerView;
    List<Posts> postsList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = view.findViewById(R.id.profile_recyclerview);
        new DownloadTask().execute();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class DownloadTask extends AsyncTask<String,Integer,String> {
        String jsonString;
        @Override
        protected String doInBackground(String... strings) {
            jsonString = httpGet("http://"+ serverIP + "/get_profiles/?id="+UID); //server
            try {
                JSONObject json = new JSONObject(jsonString);
                if (json.getString("status").equals("OK")){
                    JSONArray jsonArray = json.getJSONArray("data");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    String header = jsonArray.getJSONObject(i).getString("canteen_name");
//                    String subHeaderLeft = jsonArray.getJSONObject(i).getString("canteen_name");
//                    String subHeaderMid = jsonArray.getJSONObject(i).getString("canteen_name");
//                    String subHeaderRight = jsonArray.getJSONObject(i).getString("canteen_name");
//                    String description1 = jsonArray.getJSONObject(i).getString("canteen_name");
//                    String description2 = jsonArray.getJSONObject(i).getString("canteen_name");
//                    postsList.add(new Posts(header,));
//                }

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
            ProfileAdapter profileAdapter = new ProfileAdapter(postsList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(profileAdapter);

        }
    }

}



