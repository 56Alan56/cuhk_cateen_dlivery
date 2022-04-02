package hk.edu.cuhk.ie.iems5722.group28.MainUI.delivery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class Posts {
    private String header, subHeaderLeft,subHeaderMid, subHeaderRight, description1,description2,created_time;

    public Posts(String header, String subHeaderLeft, String subHeaderMid, String subHeaderRight, String description1, String description2) {
        this.header = header;
        this.subHeaderLeft = subHeaderLeft;
        this.subHeaderMid = subHeaderMid;
        this.subHeaderRight = subHeaderRight;
        this.description1 = description1;
        this.description2 = description2;
    }

    public Posts(JSONObject jsonObject) throws JSONException {
        this.header = jsonObject.getString("canteen_name");
        this.subHeaderLeft = jsonObject.getString("expected_time");
        this.subHeaderMid = jsonObject.getString("destination");
        this.subHeaderRight = jsonObject.getString("fees") + " HKD";
        this.description1 = "Orderer: " + jsonObject.getString("orderer_id");
        this.description2 = jsonObject.getString("food");
        this.created_time = jsonObject.getString("created_time");
    }

    public static List<Posts> fromJSONArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Posts> posts = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            posts.add(new Posts(jsonArray.getJSONObject(i)));
        }
        return posts;
    }



    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSubHeaderLeft() {
        return subHeaderLeft;
    }

    public void setSubHeaderLeft(String subHeaderLeft) {
        this.subHeaderLeft = subHeaderLeft;
    }

    public String getSubHeaderMid() {
        return subHeaderMid;
    }

    public void setSubHeaderMid(String subHeaderMid) {
        this.subHeaderMid = subHeaderMid;
    }

    public String getSubHeaderRight() {
        return subHeaderRight;
    }

    public void setSubHeaderRight(String subHeaderRight) {
        this.subHeaderRight = subHeaderRight;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }
}
