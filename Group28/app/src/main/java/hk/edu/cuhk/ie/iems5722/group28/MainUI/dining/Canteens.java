package hk.edu.cuhk.ie.iems5722.group28.MainUI.dining;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Canteens {
    private String header, subHeaderLeft, subHeaderRight, description,openingStatus;
    private boolean isExpandable;

    public String getOpeningStatus() {
        return openingStatus;
    }

    public void setOpeningStatus(String openingStatus) {
        this.openingStatus = openingStatus;
    }

    public Canteens(JSONObject jsonObject) throws JSONException {
        this.header = jsonObject.getString("canteen_name");
        this.subHeaderLeft = jsonObject.getString("open_hours");
        this.subHeaderRight = jsonObject.getString("location");
        this.description = jsonObject.getString("special");
        this.openingStatus = jsonObject.getString("opening_status");
    }

    public static List<Canteens> fromJSONArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Canteens> canteens = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            canteens.add(new Canteens(jsonArray.getJSONObject(i)));
        }
        return canteens;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public Canteens(String header, String subHeaderLeft, String subHeaderRight, String description,String OpeningStatus) {
        this.header = header;
        this.subHeaderLeft = subHeaderLeft;
        this.subHeaderRight = subHeaderRight;
        this.description = description;
        this.openingStatus = openingStatus;
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

    public String getSubHeaderRight() {
        return subHeaderRight;
    }

    public void setSubHeaderRight(String subHeaderRight) {
        this.subHeaderRight = subHeaderRight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Posts{" +
                "header='" + header + '\'' +
                ", subHeaderLeft='" + subHeaderLeft + '\'' +
                ", subHeaderRight='" + subHeaderRight + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
