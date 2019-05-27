package com.release.orderandroiddemo;

import android.Manifest;
import android.Manifest.permission;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.release.orderandroiddemo.Constants.ANDROID_DEMO_SUBHALAXMI_UID_KEY;

public class AddOrderActivity extends AppCompatActivity implements LocationListener {
    private MaterialEditText dueDateEditText;
    private MaterialEditText buyerNameEditText;
    private MaterialEditText buyerPhoneEditText;
    private MaterialEditText totalEditText;
    private MaterialEditText addressEditText;
    private Button addOrderButton;
    private Button deleteOrderButton;
    double latitude;
    double longitude;
    final Calendar calendar = Calendar.getInstance();
    DatabaseReference databaseReference;

    OrderModel orderModel;
    LocationManager locationManager;
    String provider;
    AlertDialog dialog;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // {Some Code}
                    gps = new GpsTracker(AddOrderActivity.this);

                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();


                    } else {

                    }

                }
            }
        }
    }
    GpsTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (Utils.readStringFromSharedPreferences(AddOrderActivity.this, Constants.PREF_UID_KEY) != null) {
            Constants.ANDROID_DEMO_SUBHALAXMI_UID_KEY = Utils.readStringFromSharedPreferences(AddOrderActivity.this, Constants.PREF_UID_KEY);

        }



        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }else{

        }
        getLocation();
        gps = new GpsTracker(AddOrderActivity.this);

        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


        } else {

        }


        if (getIntent().getSerializableExtra("Order") != null) {
            orderModel = (OrderModel) (getIntent().getSerializableExtra("Order"));
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_edit_order));
        } else {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_add_order));
        }


        //getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_add_order));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        dueDateEditText = findViewById(R.id.dueDateEditText);
        buyerNameEditText = findViewById(R.id.buyerNameEditText);
        buyerPhoneEditText = findViewById(R.id.buyerPhoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        totalEditText = findViewById(R.id.totalEditText);
        addOrderButton = findViewById(R.id.placeOrderButton);
        deleteOrderButton = findViewById(R.id.deleteOrderButton);



        if (orderModel != null) {
            dueDateEditText.setText(orderModel.getOrderDueDate());
            buyerNameEditText.setText(orderModel.getBuyerName());
            buyerPhoneEditText.setText(orderModel.getBuyerPhone());
            totalEditText.setText(orderModel.getOrderTotal());
            dueDateEditText.setText(orderModel.getOrderDueDate());
            addressEditText.setText(orderModel.getAddress());
            addOrderButton.setText(getResources().getString(R.string.edit_order_btn_text));
            deleteOrderButton.setVisibility(View.VISIBLE);

        } else {
            updateLabel();
            addOrderButton.setText(getResources().getString(R.string.add_order_btn_text));
            deleteOrderButton.setVisibility(View.GONE);


        }

        addOrderButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneStr = buyerPhoneEditText.getText().toString();
                String nameStr = buyerNameEditText.getText().toString();
                String totalStr = totalEditText.getText().toString();
                String addressStr = addressEditText.getText().toString();

                if (nameStr == null || nameStr.matches("")) {
                    Toast.makeText(AddOrderActivity.this, getResources().getString(R.string.invalid_name), Toast.LENGTH_LONG).show();
                    return;
                }
                if (phoneStr == null || phoneStr.matches("")) {
                    Toast.makeText(AddOrderActivity.this, getResources().getString(R.string.invalid_phone_number), Toast.LENGTH_LONG).show();
                    return;
                }
                if (totalStr == null || totalStr.matches("")) {
                    Toast.makeText(AddOrderActivity.this, getResources().getString(R.string.invalid_order), Toast.LENGTH_LONG).show();
                    return;
                }
                if (addressStr == null || addressStr.matches("")) {
                    Toast.makeText(AddOrderActivity.this, getResources().getString(R.string.invalid_address), Toast.LENGTH_LONG).show();
                    return;
                }
                if (Utils.isValidMobile(buyerPhoneEditText.getText().toString().trim()) == false) {
                    Toast.makeText(AddOrderActivity.this, getResources().getString(R.string.invalid_phone_number), Toast.LENGTH_LONG).show();
                    return;
                }
                showLoader();
                addOrderButton.setEnabled(false);


                final OrderModel tempOrderModel = new OrderModel();
                    tempOrderModel.setOrderTotal(totalStr);
                    tempOrderModel.setOrderDueDate(dueDateEditText.getText().toString().trim());
                    tempOrderModel.setBuyerPhone(phoneStr);
                    tempOrderModel.setBuyerName(nameStr);
                    tempOrderModel.setLatitude(latitude);
                    tempOrderModel.setLongitude(longitude);
                    tempOrderModel.setAddress(addressStr);
                    Geocoder geocoder = new Geocoder(AddOrderActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String cityName = addresses.get(0).getLocality();
                    String stateName = addresses.get(0).getAdminArea();
                    String countryName = addresses.get(0).getCountryName();
                    tempOrderModel.setCity(cityName);
                    tempOrderModel.setState(stateName);
                    tempOrderModel.setCountry(countryName);
                    if (orderModel != null) {
                        tempOrderModel.setOrderNumber(orderModel.getOrderNumber());
                        databaseReference.child("orders").child(ANDROID_DEMO_SUBHALAXMI_UID_KEY).child(tempOrderModel.getOrderNumber()).setValue(tempOrderModel, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                hideLoader();
                                if (databaseError != null) {
                                    addOrderButton.setEnabled(true);
                                    Toast.makeText(AddOrderActivity.this, "Couldn't be able to update the order. try Again!!!", Toast.LENGTH_LONG).show();

                                } else {
                                    //orderModel.setOrderNumber(databaseReference.getKey());
                                    Toast.makeText(AddOrderActivity.this, "Order is successfully Placed", Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            }
                        });
                    } else {
                        databaseReference.child("orders").child(ANDROID_DEMO_SUBHALAXMI_UID_KEY).push().setValue(tempOrderModel, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                databaseReference.getKey();
                                if (databaseError != null) {
                                    hideLoader();
                                    addOrderButton.setEnabled(true);
                                    Toast.makeText(AddOrderActivity.this, "Couldn't be able to place the order. try Again!!!", Toast.LENGTH_LONG).show();

                                } else {
                                    tempOrderModel.setOrderNumber(databaseReference.getKey());
                                    databaseReference.setValue(tempOrderModel);
                                    hideLoader();
                                    Toast.makeText(AddOrderActivity.this, "Order is successfully Placed", Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            }
                        });
                    }



            }
        });

        deleteOrderButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(AddOrderActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Cancel Order!");
                builder.setMessage("Are you sure you want to cancel the order?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("orders").child(ANDROID_DEMO_SUBHALAXMI_UID_KEY).child(orderModel.getOrderNumber()).removeValue();
                        Toast.makeText(AddOrderActivity.this, "Order is Successfully removed", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                //second parameter used for onclicklistener
                builder.setNegativeButton("No", null);
                builder.show();

            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dueDateEditText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddOrderActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);

        dueDateEditText.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* getMenuInflater().inflate(R.menu.activity_main, menu); */
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {



    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    public void showLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = Utils.showProgress(AddOrderActivity.this);
                }
                dialog.show();
            }
        });

    }

    public void hideLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.hide();
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 5, this);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GpsTracker.isFromSetting == true) {
            finish();
            startActivity(getIntent());
            GpsTracker.isFromSetting = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (GpsTracker.isFromSetting == true) {
            finish();
            startActivity(getIntent());
            GpsTracker.isFromSetting = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}
