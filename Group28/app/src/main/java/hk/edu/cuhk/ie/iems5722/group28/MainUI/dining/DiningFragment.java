package hk.edu.cuhk.ie.iems5722.group28.MainUI.dining;

import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.serverIP;
import static hk.edu.cuhk.ie.iems5722.group28.Utilities.HttpBean.httpGet;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.group28.MainUI.MainActivity;
import hk.edu.cuhk.ie.iems5722.group28.MyFirebaseMessagingService;
import hk.edu.cuhk.ie.iems5722.group28.R;

public class DiningFragment extends Fragment {

//    private HomeViewModel homeViewModel;
//    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    List<Canteens> canteensList;
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyFirebaseMessagingService myFCMService = new MyFirebaseMessagingService();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_dining, container, false);
        recyclerView = view.findViewById(R.id.canteen_recyclerview);
        //initData();
        new DownloadTask().execute();

          //For LocalTest
//        CanteensAdapter canteensAdapter = new CanteensAdapter(canteensList);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(canteensAdapter);
        return view;
    }

        //For local Test
//    private void initData() {
//        canteensList = new ArrayList<>();
//        canteensList.add(new Canteens("Android 10","Version:10","API Level 29","This is a test description"));
//        canteensList.add(new Canteens("Android 10","Version:10","API Level 29","This is a test description"));
//        canteensList.add(new Canteens("Android 10","Version:10","API Level 29","This is a test description"));
//        canteensList.add(new Canteens("Android 10","Version:10","API Level 29","This is a test description"));
//        canteensList.add(new Canteens("Android 10","Version:10","API Level 29","This is a test description"));
//        canteensList.add(new Canteens("Android 10","Version:10","API Level 29","This is a test description"));
//        canteensList.add(new Canteens("Android 10","Version:10","API Level 29","This is a test description"));
//
//    }

    private void setRecyclerView() {


    }

    private class DownloadTask extends AsyncTask<String,Integer,String> {
        String jsonString;
        @Override
        protected String doInBackground(String... strings) {
            jsonString = httpGet("http://"+ serverIP + "/get_canteens/"); //server
            try {
                JSONObject json = new JSONObject(jsonString);
                if (json.getString("status").equals("OK")){
                    JSONArray jsonArray = json.getJSONArray("data");
                    canteensList = Canteens.fromJSONArray(jsonArray);
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
            //ArrayAdapter<String> chatroomAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,chatroomNameList);
            CanteensAdapter canteensAdapter = new CanteensAdapter(canteensList);
            recyclerView.setAdapter(canteensAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}