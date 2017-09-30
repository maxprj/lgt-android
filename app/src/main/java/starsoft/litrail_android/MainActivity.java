package starsoft.litrail_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_map:
                    Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
                    MainActivity.this.startActivity(myIntent);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Tvarkaraščių užklausa");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Button departureStationButton = (Button) findViewById(R.id.buttonDeparture);
        final Button arrivalStationButton = (Button) findViewById(R.id.buttonDestination);
        final Button searchButton = (Button) findViewById(R.id.buttonSearch);
        final CalendarView departureDatePicker = (CalendarView) findViewById(R.id.calendarView);

        final Calendar departureDate = Calendar.getInstance();

        // rodomi navigacijos elementų pavadinimai
        BottomNavigationViewExpander.disableShiftMode(navigation);

        //demo DB stočių duomenys
        List<String> stations = new ArrayList<>();
        stations.add("Vilnius");
        stations.add("Kaunas");
        stations.add("Šiauliai");
        stations.add("Klaipėda");
        stations.add("Panevėžys");
        stations.add("Radviliškis");
        final ArrayAdapter<String> stationsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stations);

        // paspaustas išvykimo stočių mygtukas, rodantis sąrašą vietų
        departureStationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                departureStationButton.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.text), PorterDuff.Mode.SRC_ATOP);
                popUpLocationPicker(getResources().getString(R.string.select_departure_station), stationsAdapter, departureStationButton);
            }
        });
        // paspaustas atvykimo stočių mygtukas, rodantis sąrašą vietų
        arrivalStationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                arrivalStationButton.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.text), PorterDuff.Mode.SRC_ATOP);
                popUpLocationPicker(getResources().getString(R.string.select_arrival_station), stationsAdapter, arrivalStationButton);
            }
        });

        // paspaustas paieškos mygtukas, pateikiantis paieškos užklausą kontroleriui
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isInputValid(departureStationButton, arrivalStationButton)) {
                    Toast.makeText(getApplicationContext(), "Klaida. Nenurodytas pilnas maršrutas.", Toast.LENGTH_SHORT).show();
                }
                else if (departureStationButton.getText().equals(arrivalStationButton.getText())) {
                    Toast.makeText(getApplicationContext(), "Tokiam maršrutui traukinių nereikia!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
            }
        });

        // Pakeista data datos pasirinkimo widget'e
        departureDatePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                departureDate.set(year, month, dayOfMonth);
                Toast.makeText(getApplicationContext(),
                        "Data: " + year + " " + month + " " + dayOfMonth, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Inicializuojamas viršutinis meniu langas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    // paspaustas mygtukas viršutiniame meniu lange
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;
            case R.id.action_report:
                return true;
            case R.id.about_us:
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void popUpLocationPicker(String title, final ArrayAdapter<String> adapter, final Button button) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // pakeičia mygtuko tekstą į pasirinktą
                button.setText(adapter.getItem(item));
            }
        });
        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }

    private boolean isInputValid(Button button1, Button button2) {
        boolean status = true;

        if (button1.getText().equals(getResources().getString(R.string.undefined_place))) {
            status = false;
            button1.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.error), PorterDuff.Mode.SRC_ATOP);
        }
        if (button2.getText().equals(getResources().getString(R.string.undefined_place))) {
            status = false;
            button2.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.error), PorterDuff.Mode.SRC_ATOP);
        }

        return status;
    }
}
