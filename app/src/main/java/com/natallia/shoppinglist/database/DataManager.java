package com.natallia.shoppinglist.database;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Natallia on 22.12.2015.
 */
public class DataManager {

    public static final String TAG = DataManager.class.getName();

    private Activity activity;


    public static Realm realm;

    public DataManager(Activity activity) {
        this.activity = activity;
        realm = Realm.getInstance(activity);
    }

    public static List<Category> getCategories() {

        final RealmResults<Category> results = realm.where(Category.class).findAll();
        return results;
    }

    public static List<ShoppingList> getShoppingLists() {

        final RealmResults<ShoppingList> results = realm.where(ShoppingList.class).findAllSorted("id", Sort.DESCENDING);
        return results;
    }
    public static List<ShoppingListItem> getShoppingListItems(ShoppingList shoppingList) {


        return  shoppingList.getItems();
    }

    public static void setNameShoppingList(ShoppingList shoppingList, String name) {
        realm.beginTransaction();
        shoppingList.setName(name);
        realm.commitTransaction();
    }
    public static void setNameShoppingListItem(Item item, String name) {
        realm.beginTransaction();
        item.setName(name);
        realm.commitTransaction();
    }
    public static void setExpanded(ShoppingList shoppingList, boolean expanded) {
        realm.beginTransaction();
        shoppingList.setExpanded(expanded);
        realm.commitTransaction();
    }

    public static ShoppingList getShoppingListById(int id){

        ShoppingList shoppingList = realm.where(ShoppingList.class).equalTo("id",id).findFirst();
        return shoppingList;
    }

    public static void toggleExpanded(ShoppingList shoppingList) {
        realm.beginTransaction();
        shoppingList.setExpanded(!shoppingList.isExpanded());
        realm.commitTransaction();
    }

    public static void  deleteCategory(Category category) {

        realm.beginTransaction();
        category.removeFromRealm();
        realm.commitTransaction();
    }

    public static void  deleteShoppingListItem(ShoppingListItem shoppingListItem) {

        realm.beginTransaction();
        shoppingListItem.removeFromRealm();
        realm.commitTransaction();
    }

       public static int getNextId(Class<? extends RealmObject> clazz) {
        final Number currentMaxId = realm.where(clazz).max("id");
        if (currentMaxId == null) {
            return 1;
        }
        return currentMaxId.intValue() + 1;
    }

    public static ShoppingList createShoppingList() {
        realm.beginTransaction();
        ShoppingList shoppingList = realm.createObject(ShoppingList.class);
        shoppingList.setId(getNextId(ShoppingList.class));
        shoppingList.setName("Продукты");
       // shoppingList.getItems().add(realm.where(ShoppingListItem.class).findFirst());
        shoppingList.setExpanded(true);

        realm.commitTransaction();
        return shoppingList;
    }

    public static ShoppingListItem createShoppingListItem(ShoppingList shoppingList) { //TODO дописать
        realm.beginTransaction();

        Item item = realm.createObject(Item.class);
        item.setId(getNextId(Item.class));
        //shoppingList.getItems().add(item)
        //item.setCategory("Продукты");
        // shoppingList.getItems().add(realm.where(ShoppingListItem.class).findFirst());
        shoppingList.setExpanded(true);

        ShoppingListItem shoppingListItem = realm.createObject(ShoppingListItem.class);
        shoppingListItem.setId(getNextId(ShoppingListItem.class));
        shoppingListItem.setPosition(1);
        shoppingListItem.setItem(item);
        shoppingListItem.setChecked(false);
        shoppingListItem.setCount(1f);
        shoppingList.getItems().add(shoppingListItem);

        realm.commitTransaction();
        return shoppingListItem;
    }
    public static void InitializeData() {
        //  showStatus("Perform basic Create/Read/Update/Delete (CRUD) operations...");

        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        Category category;
        // Add a category
        if (realm.allObjects(Category.class).size() == 0) {
            int id = 0;//realm.allObjects(Category.class).max("id").intValue();
            ;
            category = realm.createObject(Category.class);
            category.setId(getNextId(Category.class));
            category.setName("Продукты");

            category = realm.createObject(Category.class);
            category.setId(getNextId(Category.class));
            category.setName("Бытовая химия");

           // TODO перенести в стринги


            category = realm.where(Category.class).findFirst();
            Log.d(TAG, category.getName());
            // When the transaction is committed, all changes a synced to disk.
        }

// создаем пару элементов
        if (realm.allObjects(Item.class).size() == 0) {
            int id = 0;//realm.allObjects(Category.class).max("id").intValue();
            ;
            category = realm.where(Category.class).findFirst();
            Item item = realm.createObject(Item.class);
            item.setId(getNextId(Item.class));
            item.setName("Молоко");
            item.setCategory(category);   // TODO перенести в стринги


            item = realm.createObject(Item.class);
            item.setId(getNextId(Item.class));
            item.setName("Капуста");
            item.setCategory(category);

            item = realm.createObject(Item.class);
            item.setId(getNextId(Item.class));
            item.setName("Сметана");
            item.setCategory(category);

        }


        if (realm.allObjects(ShoppingListItem.class).size() == 0) {
            int id = 0;//realm.allObjects(Category.class).max("id").intValue();
            ;
            Item item = realm.where(Item.class).equalTo("name","Молоко").findFirst();
            ShoppingListItem shoppingListItem = realm.createObject(ShoppingListItem.class);
            shoppingListItem.setId(getNextId(ShoppingListItem.class));
            shoppingListItem.setPosition(1);
            shoppingListItem.setItem(item);
            shoppingListItem.setChecked(false);
            shoppingListItem.setCount(1f);

            shoppingListItem = realm.createObject(ShoppingListItem.class);
            shoppingListItem.setId(getNextId(ShoppingListItem.class));
            shoppingListItem.setPosition(2);
            shoppingListItem.setItem(realm.where(Item.class).equalTo("name", "Капуста").findFirst());
            shoppingListItem.setChecked(false);
            shoppingListItem.setCount(1f);

            shoppingListItem = realm.createObject(ShoppingListItem.class);
            shoppingListItem.setId(getNextId(ShoppingListItem.class));
            shoppingListItem.setPosition(3);
            shoppingListItem.setItem(realm.where(Item.class).equalTo("name", "Сметана").findFirst());
            shoppingListItem.setChecked(false);
            shoppingListItem.setCount(1f);

        }
        if (realm.allObjects(ShoppingList.class).size() == 0) {
            int id = 0;//realm.allObjects(Category.class).max("id").intValue();
            ;
            Item item = realm.where(Item.class).equalTo("name","Молоко").findFirst();
            ShoppingList shoppingList = realm.createObject(ShoppingList.class);
            shoppingList.setId(getNextId(ShoppingList.class));
            shoppingList.setName("Голубцы");
            shoppingList.getItems().addAll(realm.where(ShoppingListItem.class).findAll());   // TODO перенести в стринги
            shoppingList.setExpanded(true);

            shoppingList = realm.createObject(ShoppingList.class);
            shoppingList.setId(getNextId(ShoppingList.class));
            shoppingList.setName("Продукты");
            shoppingList.getItems().add(realm.where(ShoppingListItem.class).findFirst());
            shoppingList.setExpanded(true);

        }




        realm.commitTransaction();
       /*
        // Find the first person (no query conditions) and read a field
        person = realm.where(Person.class).findFirst();
        showStatus(person.getName() + ":" + person.getAge());

        // Update person in a transaction
        realm.beginTransaction();
        person.setName("Senior Person");
        person.setAge(99);
        showStatus(person.getName() + " got older: " + person.getAge());
        realm.commitTransaction();

        // Delete all persons
        realm.beginTransaction();
        realm.allObjects(Person.class).clear();
        realm.commitTransaction();

        */
    }

    public static void toggleChecked(ShoppingListItem shoppingListItem) {
        realm.beginTransaction();
        shoppingListItem.setChecked(!shoppingListItem.isChecked());
        realm.commitTransaction();
    }

}
