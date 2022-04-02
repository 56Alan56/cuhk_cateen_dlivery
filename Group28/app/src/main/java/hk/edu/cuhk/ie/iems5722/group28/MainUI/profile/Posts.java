package hk.edu.cuhk.ie.iems5722.group28.MainUI.profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Posts {
    private String header, subHeaderLeft,subHeaderMid, subHeaderRight, description1,description2,description3,created_time,supplementary_text;


    public Posts(String header, String subHeaderLeft, String subHeaderMid, String subHeaderRight, String description1, String description2,String description3) {
        this.header = header;
        this.subHeaderLeft = subHeaderLeft;
        this.subHeaderMid = subHeaderMid;
        this.subHeaderRight = subHeaderRight;
        this.description1 = description1;
        this.description2 = description2;
        this.description3 = description3;
    }

    public Posts(JSONObject jsonObject) throws JSONException {
        this.supplementary_text = jsonObject.getString("canteen_name");
        this.header = jsonObject.getString("post_status");
        this.subHeaderLeft = jsonObject.getString("expected_time");
        this.subHeaderMid = jsonObject.getString("destination");
        this.subHeaderRight = jsonObject.getString("fees") + " HKD";
        this.description1 = jsonObject.getString("orderer_id");
        this.description2 = jsonObject.getString("food");
        this.description3 = jsonObject.getString("deliverer_id");
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


    public String getSupplementary_text() {
        return supplementary_text;
    }

    public void setSupplementary_text(String supplementary_text) {
        this.supplementary_text = supplementary_text;
    }

    public String getDescription3() {
        return description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }
}
