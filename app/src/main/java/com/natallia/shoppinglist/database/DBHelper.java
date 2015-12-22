package com.natallia.shoppinglist.database;

    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    import io.realm.RealmObject;

/**
     * Created by USER on 27.11.2015.
     */
    public class DBHelper {// extends SQLiteOpenHelper {

        private static final String TAG = DBHelper.class.getSimpleName();
        private static final String DATABASE_NAME = "database.db";
        private static final int DATABASE_VERSION = 1;
/*
        private static class List_Item extends RealmObject {
          //  private String TABLE_NAME = "List_Item";
            private String id;
            private String name;
        }


        private SQLiteDatabase mDatabase;
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d(TAG,"super");
            this.mDatabase = getWritableDatabase();
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "onCreate");
            db.execSQL("CREATE TABLE '"+ List_Item.TABLE_NAME +"' ("+
                    List_Item.COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    List_Item.COL_NAME+" TEXT);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "onUpdate");
        }
        public void saveList(String name, int age){
            mDatabase.execSQL("INSERT INTO '" + List_Item.TABLE_NAME +
                    "' ('" + List_Item.COL_NAME+ "') VALUES ('" + name + "', '" + age + "')");
        }
        public void updateDoctors(String name, int age){
            String query = String.format("select %s, %s from %s where %s = ?",
                    List_Item.COL_ID,
                    List_Item.COL_NAME,
                    List_Item.TABLE_NAME,
                    List_Item.COL_NAME);
            Cursor cursor = mDatabase.rawQuery(query, new String[] {name});
            if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor
                        .getColumnIndex(List_Item.COL_ID));
                String queryUpdate = String.format("UPDATE %s SET %s = ? where %s = ?",
                        List_Item.TABLE_NAME,
                        List_Item.COL_ID);
                mDatabase.execSQL(queryUpdate,new Object[] {age, id});
                Log.i(TAG, "ROW " + id + " HAS age" + age);
                cursor.close();
            }
            else {
                saveList(name, age);
            }
        }
        public void getDoctors(){
            Cursor cursor;
            cursor = mDatabase.query(List_Item.TABLE_NAME,
                    null,null,null,null,null,null);
            int i = 0;
            Log.d(TAG,"count "+ cursor.getCount());
            if (cursor.moveToFirst()){
                while (cursor.moveToNext()){
                    Log.d(TAG,"============|" + i +" | ===========");
                    Log.d(TAG,"id "+cursor.getInt(cursor.getColumnIndex(List_Item.COL_ID)));
                    Log.d(TAG,"name "+cursor.getString(1));
                    Log.d(TAG,"age "+cursor.getInt(2));
                    i++;
                }
            }
            else {
                Log.d(TAG,"error with moveToFirst");
            }
        }*/
    }

