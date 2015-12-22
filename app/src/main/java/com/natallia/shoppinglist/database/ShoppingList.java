package com.natallia.shoppinglist.database;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 20.12.15.
 */
public class ShoppingList extends RealmObject {
    private int id;
    private String name;
    private RealmList<ShoppingListItem> items;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<ShoppingListItem> getItems() {
        return items;
    }

    public void setItems(RealmList<ShoppingListItem> items) {
        this.items = items;
    }
}
