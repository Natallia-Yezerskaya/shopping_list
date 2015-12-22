package com.natallia.shoppinglist.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 20.12.15.
 */
public class Category extends RealmObject{
    @PrimaryKey
    private  int id;
    private String name;

    // TODO добавить цвет категории


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}

