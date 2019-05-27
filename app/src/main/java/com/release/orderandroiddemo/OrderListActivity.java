package com.release.orderandroiddemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.release.orderandroiddemo.Constants.ANDROID_DEMO_SUBHALAXMI_UID_KEY;

public class OrderListActivity extends AppCompatActivity {
    private List<OrderModel> orderModelList = new ArrayList<>();
    private RecyclerView orderRecyclerView;
    private FloatingActionButton addOrderButton;
    private static final int PERMISSIONS_REQUEST = 100;
    DatabaseReference databaseReference;
    AlertDialog dialog;

    private OrderListAdapter orderListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_order_list));
        if(Utils.readStringFromSharedPreferences(OrderListActivity.this,Constants.PREF_UID_KEY)!= null) {
            Constants.ANDROID_DEMO_SUBHALAXMI_UID_KEY = Utils.readStringFromSharedPreferences(OrderListActivity.this,Constants.PREF_UID_KEY);

        }



        orderRecyclerView = (RecyclerView) findViewById(R.id.orderRecyclerView);



        addOrderButton = (FloatingActionButton) findViewById(R.id.addOrderButton);
        addOrderButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderListActivity.this,AddOrderActivity.class));
            }
        });


        orderRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), orderRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                OrderModel orderModel = orderModelList.get(position);
                Intent intent = new Intent(OrderListActivity.this,AddOrderActivity.class);
                intent.putExtra("Order", orderModel);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareOrderData();

    }

    private void prepareOrderData() {
        showLoader();

        databaseReference.child("orders").child(ANDROID_DEMO_SUBHALAXMI_UID_KEY).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderModel orderModel = snapshot.getValue(OrderModel.class);

                    orderModelList.add(orderModel);
                }
                orderListAdapter = new OrderListAdapter(OrderListActivity.this,orderModelList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                orderRecyclerView.setLayoutManager(mLayoutManager);

                orderRecyclerView.setAdapter(orderListAdapter);
                hideLoader();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });




    }

    public void showLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog ==null){
                    dialog = Utils.showProgress(OrderListActivity.this);
                }
                dialog.show();
            }
        });

    }

    public void hideLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog!=null) {
                    dialog.hide();
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(OrderListActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Logout Alert!");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.removeFromSharedPreferences(OrderListActivity.this,Constants.PREF_UID_KEY);
                        FirebaseAuth.getInstance().signOut();

                        startActivity(new Intent(OrderListActivity.this,LoginActivity.class));
                        finish();
                    }
                });
                //second parameter used for onclicklistener
                builder.setNegativeButton("No", null);
                builder.show();
                //Do Whatever you want to do here.
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
