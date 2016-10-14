package ch.hes_so.listapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<AndroidVersion> androidList = new ArrayList<AndroidVersion>();
    private ArrayAdapter simpleAdapter;
    private Boolean isNew = true;
    private int posClick = 0;
    public ArrayList<String> itemslistArray = new ArrayList<String>();

    private String[] mListsTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private void initList(ArrayList<AndroidVersion> androidList)
    {
        AndroidVersion version1 = new AndroidVersion();
        version1.setVersionName("Cupcake");
        version1.setVersionNumber("1.5");
        androidList.add(version1);

        AndroidVersion version2 = new AndroidVersion();
        version2.setVersionName("Donut");
        version2.setVersionNumber("1.6");
        androidList.add(version2);

        AndroidVersion version3 = new AndroidVersion();
        version3.setVersionName("Eclair");
        version3.setVersionNumber("2.0.x");
        androidList.add(version3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        /*
            TOOLBAR
         */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.simpleAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, itemslistArray);

        this.itemslistArray.add("Item 1");
        this.itemslistArray.add("Item 2");
        this.itemslistArray.add("Item 3");

        /*
            Liste simple
         */

        ListView simpleListView = (ListView) findViewById(R.id.simplelistView);
        simpleListView.setAdapter(simpleAdapter);
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
//                Context context = getApplicationContext();
//                Toast toast = Toast.makeText(context, (String)adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT);
//                toast.show();
                posClick = position;
                manageItem((String)adapterView.getItemAtPosition(posClick));
            }
        });

        /*
            Bouton flottant
         */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageItem("");
            }
        });

        /*
            DRAWER
         */

        mListsTitles = getResources().getStringArray(R.array.list_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,              /* host Activity */
                mDrawerLayout,     /* DrawerLayout object */
                R.string.app_name, /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        );

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mListsTitles));

        /*
            Liste Custom
         */

        AndroidAdapter adapter = new AndroidAdapter(this, R.layout.list_layout, androidList);
        final ListView list = (ListView) findViewById(R.id.customlistView);
        initList(androidList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id)
            {
                AndroidVersion selectedItem = (AndroidVersion) adapterView.getItemAtPosition(position);
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Element selectionne : " + selectedItem.getVersionName(), Toast.LENGTH_SHORT);
                toast.show();
//                manageItem(selectedItem.getVersionName());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            manageItem("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();

    }

    public void manageItem(String item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_new_item_dialog, null);

        // Set other dialog properties
        final EditText newItemTextField = (EditText)dialogView.findViewById(R.id.newItemEditText);

        if (item != "") {
            newItemTextField.setText(item);
            builder.setTitle("Edit item").setView(dialogView);
            isNew = false;
        }
        else {
            builder.setTitle("Add new item").setView(dialogView);
            isNew = true;
        }

        // Add the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Log.i("Test: ", isNew.toString());

                if (isNew) {
                    itemslistArray.add(String.valueOf(newItemTextField.getText()));
                } else {
                    itemslistArray.set(posClick, newItemTextField.getText().toString());
                }

                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // On click...
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
