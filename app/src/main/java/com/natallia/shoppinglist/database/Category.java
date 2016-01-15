package com.natallia.shoppinglist.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Категория товара (сущность)
 */
public class Category extends RealmObject{
    @PrimaryKey
    private  int id;
    private String name;

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

