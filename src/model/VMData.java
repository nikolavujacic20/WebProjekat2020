package model;

import java.util.ArrayList;

public class VMData {
    private String name;
    private String category;
    private ArrayList<String> checkedDiscs;
    private String org;

    public VMData() {
        super();
    }

    public VMData(String name, String category, ArrayList<String> checkedDiscs, String org) {
        super();
        this.name = name;
        this.category = category;
        this.checkedDiscs = checkedDiscs;
        this.org = org;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getCheckedDiscs() {
        return checkedDiscs;
    }

    public void setCheckedDiscs(ArrayList<String> checkedDiscs) {
        this.checkedDiscs = checkedDiscs;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }


}
