package com.natallia.shoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.MaterialMenuDrawable;
import com.natallia.shoppinglist.UI.OnShoppingListEdit;
import com.natallia.shoppinglist.UI.SendSMS;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.fragments.SendSMSFragment;
import com.natallia.shoppinglist.fragments.ShoppingListsEditFragment;
import com.natallia.shoppinglist.fragments.ShoppingListsFragment;

public class MainActivity extends AppCompatActivity implements ActivityListener,OnShoppingListEdit,SendSMS{

    public static final String TAG = MainActivity.class.getName();

    private MaterialMenuDrawable mMenu;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DataManager dataManager;
    private FragmentTransaction tr;
    private boolean direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        dataManager = new DataManager(this);
        DataManager.InitializeData();

        if (savedInstanceState == null) {
            tr = getSupportFragmentManager().beginTransaction();
            ShoppingListsFragment fragment = ShoppingListsFragment.getInstance(getIntent());
            fragment.onShoppingListEdit = this;
            tr.replace(R.id.container, fragment);
            tr.commit();
        }
 }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
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
        getSupportActionBar().setCustomView(R.layout.drawer_l);

        mDrawerLayout = ((DrawerLayout)findViewById(R.id.drawer_layout));
        mDrawerList = (NavigationView)mDrawerLayout.getChildAt(1);//(ListView) findViewById(R.id.left_drawer);
        mDrawerList.setBackgroundResource(R.color.itemListBackground);
        mDrawerList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        showDialogAbout();
                        break;
                }
                return false;
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
               toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

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
    }

    private void showDialogAbout() {
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.about_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
        builder.setTitle(R.string.drawer_item_about);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void changeFragment(Fragment fragment) {
        tr = getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_in, R.anim.slide_right_out);
        tr.replace(R.id.container, fragment, "EDIT");
        tr.addToBackStack(null);
        tr.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    // открываем фрагмент редактирования shopping list
    public void openEditShoppingListFragment(int shoppingListId){
        Intent intent = getIntent();
        intent.putExtra("ShoppingListId",shoppingListId);
        ShoppingListsEditFragment fragment  = ShoppingListsEditFragment.getInstance(intent);
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
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void sendSMS(String smsText) {
        openSendSMSFragment(smsText);
    }

    //открываем фрагмент отправки смс
    public void openSendSMSFragment(String smsText){
        Intent intent = getIntent();
        intent.putExtra("SMSText",smsText);
        SendSMSFragment fragment  = SendSMSFragment.getInstance(intent);
        changeFragment(fragment);
    }
}
