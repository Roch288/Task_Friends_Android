package com.friends.task_friends_android;

import java.util.ArrayList;

public class Categories {

    private static ArrayList<Categories> categoriesArrayList = new ArrayList<>();
    private String catId;
    private String catName;

    public Categories(String catId, String catName) {
        this.catId = catId;
        this.catName = catName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public static void initCategories() {
        categoriesArrayList.clear();
        categoriesArrayList.add(0,new Categories("-1", "Select Category"));
        Categories school = new Categories("0", "School");
        categoriesArrayList.add(school);

        Categories work = new Categories("1", "Work");
        categoriesArrayList.add(work);

        Categories shopping = new Categories("2", "Shopping");
        categoriesArrayList.add(shopping);
    }

    public int getImage() {
        switch (getCatId()) {
            case "-1":
                return R.drawable.ic_select;
            case "0":
                return R.drawable.school;
            case "1":
                return R.drawable.work;
            case "2":
                return R.drawable.shopping;
        }
        return R.drawable.school;
    }


    public static ArrayList<Categories> getCategoriesArrayList() {
        return categoriesArrayList;
    }
}
