package com.natallia.shoppinglist.database;

import io.realm.RealmObject;

/**
 * Связь между элементом списка покупок и самим списком (сущность)
 * */
public class ShoppingListItem extends RealmObject {

    private int id;
    private int position;
    private Item item;
    private boolean checked;
    private float amount;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}
