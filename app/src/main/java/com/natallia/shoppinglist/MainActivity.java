package com.natallia.shoppinglist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.MaterialMenuDrawable;
import com.natallia.shoppinglist.database.Category;
import com.natallia.shoppinglist.fragments.ListFragment;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements ActivityListener{

    public static final String TAG = MainActivity.class.getName();

    private TextView mTitleView;
    private MaterialMenuDrawable mMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    public static Realm realm;

    private boolean          direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initToolbar();

       realm = Realm.getInstance(this);
        //basicCRUD(realm);


        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

        ListFragment fragment  = ListFragment.getInstance("djkgfhsg", getIntent());
        tr.replace(R.id.container, fragment, "First");
        tr.commit();

    }


    private void showStatus(String txt) {
        Log.i(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);
        //rootLayout.addView(tv);
    }

    public static void basicCRUD(Realm realm) {
      //  showStatus("Perform basic Create/Read/Update/Delete (CRUD) operations...");

        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();

        // Add a category
        if (realm.allObjects(Category.class).size() == 0) {
            int id = realm.allObjects(Category.class).max("id").intValue();
            ;
            Category category = realm.createObject(Category.class);
            category.setId(++id);
            category.setName("Продукты"); // TODO перенести в стринги


            category = realm.createObject(Category.class);
            //category.setId(2);
            category.setName("Бытовая химия");

            realm.commitTransaction();



        category = realm.where(Category.class).findFirst();
        Log.d(TAG, category.getName());
        // When the transaction is committed, all changes a synced to disk.
        }


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

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mMenu = new MaterialMenuDrawable(this, Color.BLACK,MaterialMenuDrawable.Stroke.REGULAR,300);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenu.animateIconState(MaterialMenuDrawable.IconState.ARROW);

            }
        });
        toolbar.setNavigationIcon(mMenu);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setCustomView(R.layout.qqqw);

       //materialMenuView = (MaterialMenuView) parent.findViewById(R.id.material_menu_button);
        //materialMenuView.setOnClickListener(this);
        //materialIcon = actionBarIcon;

        mDrawerLayout = ((DrawerLayout)findViewById(R.id.drawer_layout));
        mDrawerLayout.setScrimColor(Color.parseColor("#66000000"));
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,
         //       R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
               toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Этот код вызывается, когда боковое меню переходит в полностью закрытое состояние. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
              //  getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Этот код вызывается, когда боковое меню полностью открывается. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
               // getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);




    mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mMenu.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        direction ? 2 - slideOffset : slideOffset
                );
            }

            @Override
            public void onDrawerOpened(android.view.View drawerView) {
                direction = true;
            }

            @Override
            public void onDrawerClosed(android.view.View drawerView) {
                direction = false;
            }
        });

       /* mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        }, 1500);
*/

       /* new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withBadge("99").withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
                        new SectionDrawerItem().withName(R.string.drawer_item_settings),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_github).withBadge("12+").withIdentifier(1)
                )
                .build();
*/


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // realm.close();
    }

    @Override
    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    @Override
    public void changeFragment(Fragment fragment) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
