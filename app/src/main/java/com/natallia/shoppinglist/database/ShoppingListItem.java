package com.natallia.shoppinglist.database;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 20.12.15.
 */
public class ShoppingListItem extends RealmObject {

    private int id;
   // private ShoppingList shopList;
    private int position;
    private Item item;
    private boolean checked;
    private float count;

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

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }
/* public ShoppingList getShopList() {
        return shopList;
    }

    public void setShopList(ShoppingList shopList) {
        this.shopList = shopList;
    }
*/
}
