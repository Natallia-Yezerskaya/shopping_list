package com.natallia.shoppinglist.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 20.12.15.
 */
public class ShoppingList extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private RealmList<ShoppingListItem> items;
    private boolean expanded;



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

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
