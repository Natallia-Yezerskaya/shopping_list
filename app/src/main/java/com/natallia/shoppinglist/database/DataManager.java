package com.natallia.shoppinglist.database;

import android.app.Activity;
import android.util.Log;

import com.natallia.shoppinglist.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Класс для работы с бд (в данном случае realm)
 */
public class DataManager {

    public static final String TAG = DataManager.class.getName();
    public Activity activity;
    public static Realm realm;

    public DataManager(Activity activity) {
        this.activity = activity;
        realm = Realm.getInstance(activity);
    }

    public static List<Category> getCategories() {
        final RealmResults<Category> results = realm.where(Category.class).findAll();
        return results;
    }

    // получаем список всех ShoppingList, отсортированный по позиции
    public static List<ShoppingList> getShoppingLists() {
        final RealmResults<ShoppingList> results = realm.where(ShoppingList.class).findAllSorted("position", Sort.DESCENDING);
        return results;
    }

    // сортируем список всех ShoppingList, вычеркнутые списки внизу
    public static void sortShoppingLists() {
        realm.beginTransaction();
        RealmResults<ShoppingList> results = realm.where(ShoppingList.class).findAllSorted("isChecked", Sort.DESCENDING, "id", Sort.ASCENDING);
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setPosition(i);
        }
        realm.commitTransaction();
    }

    // устанавливаем название списка
    public static void setNameShoppingList(ShoppingList shoppingList, String name) {
        realm.beginTransaction();
        shoppingList.setName(name);
        realm.commitTransaction();
    }

    // устанавливаем name элемента списка (item)
    public static void setNameShoppingListItem(Item item, String name) {
        realm.beginTransaction();
        item.setName(name);
        realm.commitTransaction();
    }

    // устанавливаем необходимое количество элемента списка (item)
    public static void setAmountShoppingListItem(ShoppingListItem shoppingListItem, Float amount) {
        realm.beginTransaction();
        shoppingListItem.setAmount(amount);
        realm.commitTransaction();
    }

    // получаем список по id
    public static ShoppingList getShoppingListById(int id){
        ShoppingList shoppingList = realm.where(ShoppingList.class).equalTo("id",id).findFirst();
        return shoppingList;
    }

    // меняем признак expanded у Shopping list
    public static void toggleExpanded(ShoppingList shoppingList) {
        realm.beginTransaction();
        shoppingList.setExpanded(!shoppingList.isExpanded());
        realm.commitTransaction();
    }
    // меняем признак favorite (избранный) у Shopping list
    public static void toggleFavorite(ShoppingList shoppingList) {
        realm.beginTransaction();
        shoppingList.setFavorite(!shoppingList.isFavorite());
        realm.commitTransaction();
    }

    public static void  deleteCategory(Category category) {
        realm.beginTransaction();
        category.removeFromRealm();
        realm.commitTransaction();
    }

    // удаляем элемент из списка
    public static void  deleteShoppingListItem(ShoppingListItem shoppingListItem) {
        realm.beginTransaction();
        shoppingListItem.removeFromRealm();
        realm.commitTransaction();
    }

    // удаляем список из базы
    public static void  deleteShoppingList(ShoppingList shoppingList) {
        realm.beginTransaction();
        shoppingList.removeFromRealm();
        realm.commitTransaction();
    }

    public static boolean  shoppingListIsChecked(ShoppingList shoppingList) {
       Long count = shoppingList.getItems().where().equalTo("checked",false).count();
        if (count == 0){
            return true;
        }
        else {
            return false;}
    }

    public static void  setShoppingListIsChecked(ShoppingList shoppingList, boolean isChecked) {
        realm.beginTransaction();
        shoppingList.setIsChecked(isChecked);
        realm.commitTransaction();
    }
    public static int  shoppingListGetChecked(ShoppingList shoppingList) {
        Long count = shoppingList.getItems().where().equalTo("checked",true).count();
        return count.intValue();
    }

    public static List<ShoppingList>  getFavoriteShoppingLists(int idShoppingList) {
        List <ShoppingList> favoritesShoppingList = realm.where(ShoppingList.class).equalTo("favorite",true).notEqualTo("id",idShoppingList).findAll();
        for (int i = 0; i <favoritesShoppingList.size() ; i++) {

        }
        return favoritesShoppingList;
    }
    public static int getNextId(Class<? extends RealmObject> clazz) {
        final Number currentMaxId = realm.where(clazz).max("id");
        if (currentMaxId == null) {
            return 1;
        }
        return currentMaxId.intValue() + 1;
    }

    public static int getNextPositionOfShoppingListItem(ShoppingList shoppingList) {
        final Number currentMaxPosition = shoppingList.getItems().where().max("position");
        if (currentMaxPosition == null) {
            return 1;
        }
        return currentMaxPosition.intValue() + 1;
    }
    public static int getNextPositionOfShoppingList() {
        final Number currentMaxPosition = realm.where(ShoppingList.class).findAll().where().max("position");
        if (currentMaxPosition == null) {
            return 1;
        }
        return currentMaxPosition.intValue() + 1;
    }

    // первоначальное заполнение базы
    public static ShoppingList createShoppingList() {
        realm.beginTransaction();
        ShoppingList shoppingList = realm.createObject(ShoppingList.class);
        shoppingList.setId(getNextId(ShoppingList.class));
        shoppingList.setPosition(getNextPositionOfShoppingList());
        shoppingList.setName("Новый список");
        shoppingList.setExpanded(true);
        realm.commitTransaction();
        return shoppingList;
    }

    public static ShoppingListItem createShoppingListItem(ShoppingList shoppingList) {
        realm.beginTransaction();
        Item item = realm.createObject(Item.class);
        item.setId(getNextId(Item.class));
        shoppingList.setExpanded(true);

        ShoppingListItem shoppingListItem = realm.createObject(ShoppingListItem.class);
        shoppingListItem.setId(getNextId(ShoppingListItem.class));

        shoppingListItem.setPosition(getNextPositionOfShoppingListItem(shoppingList));
        shoppingListItem.setItem(item);
        shoppingListItem.setChecked(false);
        shoppingListItem.setAmount(1f);
        shoppingList.getItems().add(shoppingListItem);

        realm.commitTransaction();
        return shoppingListItem;
    }

    public static void addShoppingListItem(ShoppingList shoppingList,ShoppingListItem shoppingListItem) {
        realm.beginTransaction();

        ShoppingListItem newShoppingListItem = realm.createObject(ShoppingListItem.class);

        newShoppingListItem.setAmount(shoppingListItem.getAmount());
        newShoppingListItem.setItem(shoppingListItem.getItem());
        newShoppingListItem.setChecked(false);
        newShoppingListItem.setId(getNextId(ShoppingListItem.class));
        newShoppingListItem.setPosition(getNextPositionOfShoppingListItem(shoppingList));
        shoppingList.getItems().add(newShoppingListItem);

        realm.commitTransaction();

    }
    public static void InitializeData() {
        realm.beginTransaction();
        Category category;
        // Add a category
        if (realm.allObjects(Category.class).size() == 0) {
            int id = 0;
            category = realm.createObject(Category.class);
            category.setId(getNextId(Category.class));
            category.setName("Продукты");

            category = realm.createObject(Category.class);
            category.setId(getNextId(Category.class));
            category.setName("Бытовая химия");

            category = realm.where(Category.class).findFirst();
            Log.d(TAG, category.getName());
        }

// создаем пару элементов
        if (realm.allObjects(Item.class).size() == 0) {
            int id = 0;//realm.allObjects(Category.class).max("id").intValue();
            ;
            category = realm.where(Category.class).findFirst();
            Item item = realm.createObject(Item.class);
            item.setId(getNextId(Item.class));
            item.setName("Молоко");
            item.setCategory(category);

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
            shoppingListItem.setAmount(1f);

            shoppingListItem = realm.createObject(ShoppingListItem.class);
            shoppingListItem.setId(getNextId(ShoppingListItem.class));
            shoppingListItem.setPosition(2);
            shoppingListItem.setItem(realm.where(Item.class).equalTo("name", "Капуста").findFirst());
            shoppingListItem.setChecked(false);
            shoppingListItem.setAmount(1f);

            shoppingListItem = realm.createObject(ShoppingListItem.class);
            shoppingListItem.setId(getNextId(ShoppingListItem.class));
            shoppingListItem.setPosition(3);
            shoppingListItem.setItem(realm.where(Item.class).equalTo("name", "Сметана").findFirst());
            shoppingListItem.setChecked(false);
            shoppingListItem.setAmount(1f);

        }
        if (realm.allObjects(ShoppingList.class).size() == 0) {
            int id = 0;//realm.allObjects(Category.class).max("id").intValue();
            ;
            //Item item = realm.where(Item.class).equalTo("name","Молоко").findFirst();
            ShoppingList shoppingList = realm.createObject(ShoppingList.class);
            shoppingList.setId(getNextId(ShoppingList.class));
            shoppingList.setPosition(getNextPositionOfShoppingList());
            Log.d("shopping position", String.valueOf(shoppingList.getPosition()));
            shoppingList.setName("Голубцы");
            shoppingList.getItems().addAll(realm.where(ShoppingListItem.class).findAll());   // TODO перенести в стринги
            shoppingList.setExpanded(true);

            Item item = realm.where(Item.class).equalTo("name","Молоко").findFirst();
            ShoppingListItem shoppingListItem = realm.createObject(ShoppingListItem.class);
            shoppingListItem.setId(getNextId(ShoppingListItem.class));
            shoppingListItem.setPosition(1);
            shoppingListItem.setItem(item);
            shoppingListItem.setChecked(false);
            shoppingListItem.setAmount(1f);


            shoppingList = realm.createObject(ShoppingList.class);
            shoppingList.setId(getNextId(ShoppingList.class));
            shoppingList.setPosition(getNextPositionOfShoppingList());
            Log.d("shopping position", String.valueOf(shoppingList.getPosition()));
            shoppingList.setName("Продукты");
            shoppingList.getItems().add(shoppingListItem);
            shoppingList.setExpanded(true);
        }
        realm.commitTransaction();

    }

    public static void toggleChecked(ShoppingListItem shoppingListItem) {
        realm.beginTransaction();
        shoppingListItem.setChecked(!shoppingListItem.isChecked());
        realm.commitTransaction();
    }

    // меняем позиции элементов в списке
    public static void swapShoppingListItems(List<ShoppingListItem> shoppingListItems, int fromPosition, int toPosition) {
        if (fromPosition > toPosition) {
            int x = toPosition;
            toPosition = fromPosition;
            fromPosition = x;
        }
        realm.beginTransaction();
            ShoppingListItem from = shoppingListItems.get(fromPosition);
            ShoppingListItem to = shoppingListItems.get(toPosition);
            int temp = from.getPosition();
            from.setPosition(to.getPosition());
            to.setPosition(temp);
        realm.commitTransaction();
    }
}
