package com.natallia.shoppinglist.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Item  extends RealmObject{

    @PrimaryKey
    private int id;
    private String name;
    private Category category;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
