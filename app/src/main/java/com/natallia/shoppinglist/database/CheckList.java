package com.natallia.shoppinglist.database;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 20.12.15.
 */
public class CheckList extends RealmObject {

    private int id;
   // private ShopList shopList;
    private int position;
    private RealmList<Item> items;
    private boolean checked;

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

    public RealmList<Item> getItems() {
        return items;
    }

    public void setItems(RealmList<Item> items) {
        this.items = items;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

   /* public ShopList getShopList() {
        return shopList;
    }

    public void setShopList(ShopList shopList) {
        this.shopList = shopList;
    }
*/
}
