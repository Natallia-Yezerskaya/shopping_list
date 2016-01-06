package com.natallia.shoppinglist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.MaterialMenuDrawable;
import com.natallia.shoppinglist.UI.OnShoppingListEdit;
import com.natallia.shoppinglist.UI.SendSMS;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.fragments.CategoryListFragment;
import com.natallia.shoppinglist.fragments.SendSMSFragment;
import com.natallia.shoppinglist.fragments.ShoppingListsEditFragment;
import com.natallia.shoppinglist.fragments.ShoppingListsFragment;

import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements ActivityListener,OnShoppingListEdit,SendSMS{

    public static final String TAG = MainActivity.class.getName();

    private TextView mTitleView;
    private MaterialMenuDrawable mMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private DataManager dataManager;
    private FragmentTransaction tr;

    private boolean          direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initToolbar();

        dataManager = new DataManager(this);
        DataManager.InitializeData();
        if (savedInstanceState == null) {

            tr = getSupportFragmentManager().beginTransaction();

            ShoppingListsFragment fragment = ShoppingListsFragment.getInstance("djkgfhsg", getIntent());
            fragment.onShoppingListEdit = this;
            tr.replace(R.id.container, fragment, "First");
            tr.commit();
        }
    }


    private void showStatus(String txt) {
        Log.i(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);

        //rootLayout.addView(tv);
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
        tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.container, fragment, "Edit");
        tr.addToBackStack(null);
        tr.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    public void openEditShoppingListFragment(int shoppingListId){
        Intent intent = getIntent();
        intent.putExtra("ShoppingListId",shoppingListId);
        ShoppingListsEditFragment fragment  = ShoppingListsEditFragment.getInstance("djkgfhsg", intent);
        fragment.sendSMS = this;
        changeFragment(fragment);
    }


    @Override
    public void onShoppingListEdit(int shoppingListId) {
        openEditShoppingListFragment(shoppingListId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
       //outState.putInt("ShoppingListId");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void sendSMS(String smsText) {
        openSendSMSFragment(smsText);
    }

    public void openSendSMSFragment(String smsText){
        Intent intent = getIntent();
        intent.putExtra("SMSText",smsText);
        SendSMSFragment fragment  = SendSMSFragment.getInstance("djkgfhsg", intent);
        changeFragment(fragment);
    }
}
