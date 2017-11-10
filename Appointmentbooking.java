package anulom.executioner5.com3.anulom;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.services.SendCommentService;
import anulom.executioner5.com3.anulom.services.getappointmentslot;
import anulom.executioner5.com3.anulom.services.postappointmentbooking;

import static anulom.executioner5.com3.anulom.R.id.textView;
import static anulom.executioner5.com3.anulom.R.id.view;
import static anulom.executioner5.com3.anulom.R.layout.addattendees;
import static anulom.executioner5.com3.anulom.R.layout.check;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.APPOINTMENTBOOKING;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.POSTDOC;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.date;

/**
 * Created by anulom on 18/8/17.
 */

public class Appointmentbooking extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = "Anulom-Executioner";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyClWGASK76mkrDB3Z2aYQB195kxv7KS84A";
    Toolbar toolbar;
    int v, a, a1, indi;
    String apart, indiv;
    int check_ctr = 0;
    Button confirm, btn_date;
    TextView t1, t2, t3, tv2, reason;
    EditText landmark1, slottime, free_reason1, line1, line2, name1, email2, contact2;
    int startdelay = 5000;
    Date date = null, date2 = null;
    int j, checkk = 0, checkk1 = 0;
    private SharedPreferences usermail;
    String TAG = "", current_Date;
    int mposition;

    private String username2 = "", password2 = "";
    String email, email1, appointment_id, contact, contact1, add, land, ID1, contactmail, contactphone, value, value1, contactmail1, contactphone1;
    DBOperation db;
    CheckBox check1, check2, check3;
    Timer singleTask = new Timer();
    String document, apptype, con1, mwit, option1, mtype, manulom, mregion, mpoa, division_id, option, free_reason, slotid, reportkey, mperson, mperson1;
    int count = 0, img, img1;
    String mspin = "", datevalue = "", datevalue1 = "", starttime, endtime, finaltime, timevalue = "", slot = "", mowner = "", mtenant = "", name;
    DatePickerDialog.OnDateSetListener date1;
    List<String> categories1 = new ArrayList<String>();
    List<String> Owner = new ArrayList<String>();
    List<String> Tenant = new ArrayList<String>();
    List<String> Witness = new ArrayList<String>();
    List<String> AnulomWitness = new ArrayList<String>();
    List<String> partytype = new ArrayList<String>();
    List<String> city = new ArrayList<String>();
    List<String> region = new ArrayList<String>();
    List<String> region1 = new ArrayList<>();
    List<String> region2 = new ArrayList<String>();
    List<String> region3 = new ArrayList<>();
    List<String> region4 = new ArrayList<String>();
    List<String> region5 = new ArrayList<>();
    List<String> division1 = new ArrayList<String>();
    List<String> division2 = new ArrayList<String>();
    List<String> division3 = new ArrayList<String>();
    List<String> division4 = new ArrayList<String>();
    List<String> division5 = new ArrayList<String>();
    List<String> division7 = new ArrayList<String>();
    List<String> division9 = new ArrayList<String>();
    List<String> division11 = new ArrayList<String>();
    List<String> division12 = new ArrayList<>();
    List<String> division13 = new ArrayList<String>();
    List<String> contactperson1 = new ArrayList<String>();

    List<String> doclist = new ArrayList<String>();
    List<String> contactmaillist = new ArrayList<String>();
    List<String> namelist = new ArrayList<String>();
    List<String> emaillist = new ArrayList<String>();
    List<String> contactphonelist = new ArrayList<String>();
    List<String> contactlist = new ArrayList<String>();
    DatePickerDialog.OnDateSetListener StartTime;

    protected void onCreate(Bundle savedInstanceState) {
        db = new DBOperation(getApplicationContext());
        super.onCreate(savedInstanceState);
        final LinearLayout rootView = new LinearLayout(this);

        document = getIntent().getStringExtra("DocumentId");
        apptype = getIntent().getStringExtra("apptype");
        con1 = getIntent().getStringExtra("content");
        option = getIntent().getStringExtra("option");
        free_reason = getIntent().getStringExtra("free_reason");
        reportkey = getIntent().getStringExtra("ReportKey");
        appointment_id = getIntent().getStringExtra("AppointmentId");
        current_Date = GenericMethods.getCurrentDate();

        usermail = PreferenceManager.getDefaultSharedPreferences(this);
        username2 = usermail.getString("username", "");
        password2 = usermail.getString("password", "");
        TextView citytext = new TextView(this);
        citytext.setText("  Select the City:");
        citytext.setTextColor(Color.parseColor("#000080"));
        citytext.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        final TextView regiontext = new TextView(this);
        regiontext.setText("  Select the Region:");
        regiontext.setTextColor(Color.parseColor("#000080"));
        regiontext.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));

        TextView regiontext1 = new TextView(this);
        regiontext1.setText("  Select Date:");
        regiontext1.setTextColor(Color.parseColor("#000080"));
        regiontext1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));


        final TextView name = new TextView(Appointmentbooking.this);
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        name.setTextColor(Color.parseColor("#000080"));
        name.setText("  Name:");
        name.setVisibility(View.GONE);

        final TextView email = new TextView(Appointmentbooking.this);
        email.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        email.setTextColor(Color.parseColor("#000080"));
        email.setText("  Email:");
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        email.setVisibility(View.GONE);

        final TextView datetest = new TextView(Appointmentbooking.this);
        datetest.setTextColor(Color.parseColor("#000080"));
        datetest.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        datetest.setVisibility(View.INVISIBLE);

        final TextView contact = new TextView(Appointmentbooking.this);
        contact.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        contact.setTextColor(Color.parseColor("#000080"));
        contact.setText("  Contact No:");
        contact.setInputType(InputType.TYPE_CLASS_PHONE);
        contact.setVisibility(View.GONE);


        name1 = new EditText(this);
        name1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramsname = (LinearLayout.LayoutParams) name1.getLayoutParams();
        paramsname.setMargins(40, 20, 50, 10);
        name1.setLayoutParams(paramsname);
        name1.setVisibility(View.GONE);
        name1.setBackgroundResource(R.drawable.edittextborder);

        email2 = new EditText(this);
        email2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramsemail = (LinearLayout.LayoutParams) email2.getLayoutParams();
        paramsemail.setMargins(40, 20, 50, 10);
        email2.setLayoutParams(paramsemail);
        email2.setVisibility(View.GONE);
        email2.setBackgroundResource(R.drawable.edittextborder);

        contact2 = new EditText(this);
        contact2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramscontact = (LinearLayout.LayoutParams) contact2.getLayoutParams();
        paramscontact.setMargins(40, 20, 50, 10);
        contact2.setLayoutParams(paramscontact);
        contact2.setVisibility(View.GONE);
        contact2.setBackgroundResource(R.drawable.edittextborder);

        final TextView regiontext2 = new TextView(this);
        regiontext2.setText("  Select Time Slot:");
        regiontext2.setTextColor(Color.parseColor("#000080"));
        regiontext2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        regiontext2.setVisibility(View.INVISIBLE);

        TextView attendees = new TextView(this);
        attendees.setText("  Select Attendees:");
        attendees.setTextColor(Color.parseColor("#000080"));
        attendees.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        TextView tv = new TextView(this);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        tv.setTextColor(Color.parseColor("#545454"));
        tv.setText("  Owner:");
        TextView tv1 = new TextView(this);
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        tv1.setTextColor(Color.parseColor("#545454"));
        tv1.setText("  Tenant:");

        final CheckBox add1 = new CheckBox(this);
        add1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        add1.setTextColor(Color.parseColor("#000080"));
        add1.setText("Add Attendees");
        add1.setId(a1);


        final Button addattendees = new Button(this);
        addattendees.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        addattendees.setGravity(Gravity.CENTER);
        addattendees.setText("ADD ATTENDEES");

        addattendees.setBackgroundColor(Color.parseColor("#ffa500"));
        addattendees.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams paramsaddattendess = (LinearLayout.LayoutParams) addattendees.getLayoutParams();
        paramsaddattendess.setMargins(40, 20, 50, 10);
        addattendees.setLayoutParams(paramsaddattendess);
        addattendees.setVisibility(View.GONE);

        reason = new TextView(this);
        reason.setText("  Type the reason for free appointment:");
        reason.setTextColor(Color.parseColor("#545454"));
        reason.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        //reason.setVisibility(View.GONE);
        TextView tv3 = new TextView(this);
        tv3.setText("  Contact Person:");
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        tv3.setTextColor(Color.parseColor("#545454"));

        TextView address = new TextView(this);
        address.setText("  Address:");
        address.setTextColor(Color.parseColor("#545454"));
        address.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));

        TextView landmark = new TextView(this);
        landmark.setText("  Landmark:");
        landmark.setTextColor(Color.parseColor("#545454"));
        landmark.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));

        TextView buildingtype = new TextView(this);
        buildingtype.setText("  Select The Building Type:");
        buildingtype.setTextColor(Color.parseColor("#000080"));
        buildingtype.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));

        add1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (add1.isChecked()) {

                    name.setVisibility(View.VISIBLE);
                    name1.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    email2.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    contact2.setVisibility(View.VISIBLE);
                    addattendees.setVisibility(View.VISIBLE);


                } else if (!add1.isChecked()) {


                    name1.setText("");
                    email2.setText("");
                    contact2.setText("");

                    name.setVisibility(View.GONE);
                    name1.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    email2.setVisibility(View.GONE);
                    contact.setVisibility(View.GONE);
                    contact2.setVisibility(View.GONE);
                    addattendees.setVisibility(View.GONE);


                }
            }
        });

        Button update1 = new Button(this);
        update1.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        update1.setBackgroundResource(R.drawable.calendar);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) update1.getLayoutParams();
        params2.setMargins(40, 10, 250, 0);
        update1.setLayoutParams(params2);


        Button confirm = new Button(this);
        confirm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        confirm.setGravity(Gravity.CENTER);
        confirm.setText("CONFIRM APPOINTMENT");
        confirm.setBackgroundColor(Color.parseColor("#ffa500"));
        confirm.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) confirm.getLayoutParams();
        params.setMargins(40, 20, 50, 10);
        confirm.setLayoutParams(params);

        line1 = new EditText(this);
        line1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramsline = (LinearLayout.LayoutParams) line1.getLayoutParams();
        paramsline.setMargins(40, 20, 50, 10);
        line1.setHint("Building Name/Door No");
        line1.setLayoutParams(paramsline);

        line2 = new EditText(this);
        line2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramsline2 = (LinearLayout.LayoutParams) line2.getLayoutParams();
        paramsline2.setMargins(40, 20, 50, 10);
        line2.setHint("Street Name/Locality");
        line2.setLayoutParams(paramsline2);

        slottime = new EditText(this);
        slottime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) slottime.getLayoutParams();
        params1.setMargins(40, 20, 320, 10);
        slottime.setLayoutParams(params1);

        free_reason1 = new EditText(this);
        free_reason1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) free_reason1.getLayoutParams();
        para.setMargins(40, 20, 50, 10);
        free_reason1.setLayoutParams(para);
        //free_reason1.setVisibility(View.GONE);


        final AutoCompleteTextView address1 = new AutoCompleteTextView(this);
        address1.setId(v);
        address1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) address1.getLayoutParams();
        params3.setMargins(40, 20, 50, 10);
        address1.setHint("City/State/Pincode");
        address1.setLayoutParams(params3);
        address1.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_item));
        address1.setOnItemClickListener(this);

        landmark1 = new EditText(this);
        landmark1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) landmark1.getLayoutParams();
        params4.setMargins(40, 20, 50, 10);
        landmark1.setHint("Please Enter Your Landmark");
        landmark1.setLayoutParams(params4);


        final CheckBox check1 = new CheckBox(this);
        check1.setTextSize(18);
        check1.setTextColor(Color.parseColor("#545454"));
        check1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) check1.getLayoutParams();
        params5.setMargins(40, 20, 50, 10);
        check1.setLayoutParams(params5);

        final CheckBox check2 = new CheckBox(this);
        check2.setTextSize(18);
        check2.setTextColor(Color.parseColor("#545454"));
        check2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params6 = (LinearLayout.LayoutParams) check2.getLayoutParams();
        params6.setMargins(40, 20, 50, 10);
        check2.setLayoutParams(params6);

        if (option.equals("0")) {

            //System.out.println("option" + option);
            free_reason1.setVisibility(View.GONE);
            reason.setVisibility(View.GONE);

        }

        RadioGroup rg = new RadioGroup(getApplicationContext());
        rg.setOrientation(RadioGroup.HORIZONTAL);


        final RadioButton apartment = new RadioButton(getApplicationContext());
        apartment.setText("Apartment");
        apartment.setTextColor(Color.BLACK);
        apartment.setTextSize(18);
        rg.addView(apartment);

        // Create another Radio Button for RadioGroup
        final RadioButton Individual_house = new RadioButton(getApplicationContext());
        Individual_house.setText("Individual House");
        Individual_house.setTextColor(Color.BLACK);
        Individual_house.setTextSize(18);
        rg.addView(Individual_house);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (apartment.isChecked()) {
                    option1 = "Apartment";
                } else if (Individual_house.isChecked()) {
                    option1 = "Individual House";
                }

                System.out.println(option1);
            }
        });

        Spinner sp1 = new Spinner(this);
        final Spinner sp2 = new Spinner(this);
        final Spinner sp3 = new Spinner(this);
        final Spinner sp4 = new Spinner(this);
        final Spinner sp5 = new Spinner(this);
        final Spinner sp6 = new Spinner(this);
        sp3.setGravity(Gravity.CENTER);
        sp3.setBackgroundResource(R.drawable.ic_down);
        sp3.setVisibility(View.INVISIBLE);
        categories1.add("-SELECT-");
        sp5.setGravity(Gravity.CENTER);
        sp5.setBackgroundResource(R.drawable.ic_down);

        sp6.setGravity(Gravity.CENTER);
        sp6.setBackgroundResource(R.drawable.ic_down);
        sp6.setVisibility(View.GONE);


        sp1.setGravity(Gravity.CENTER);
        sp1.setBackgroundResource(R.drawable.ic_down);

doclist.add(document);
        city.add("Pune");
        city.add("Mumbai");
        city.add("Mumbai Suburban");
        city.add("Thane");
        city.add("Raigad");
        city.add("Navi Mumbai");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, city);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(dataAdapter);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mspin = parent.getItemAtPosition(position).toString();


                if (mspin.equals("Pune")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, region);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);
                } else if (mspin.equals("Mumbai")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, region1);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);


                } else if (mspin.equals("Mumbai Suburban")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, region2);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);

                } else if (mspin.equals("Thane")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, region3);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);


                } else if (mspin.equals("Raigad")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, region4);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);

                } else if (mspin.equals("Navi Mumbai")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, region5);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp2.setGravity(Gravity.CENTER);
        sp2.setBackgroundResource(R.drawable.ic_down);

        region.add("Akurdi");
        region.add("Alandi Devachi");
        region.add("Alandi Khed");
        region.add("Alandi Road");
        region.add("Amanora Township");
        region.add("Ambegaon BK");
        region.add("Anandnagar");
        region.add("Aundh");
        region.add("Aundh Road");
        region.add("Balaji Nagar");
        region.add("Balewadi");
        region.add("Baner");
        region.add("Baner Road");
        region.add("Bavdhan");
        region.add("Bhandarkar Road");
        region.add("Bhavani Peth");
        region.add("Bhawani Peth Road");
        region.add("Bhosari");
        region.add("Bhosarigaon");
        region.add("Bibvewadi");
        region.add("Blueridge");
        region.add("Bopodi");
        region.add("Budhwar Peth");
        region.add("Bund Garden Road");
        region.add("Camp");
        region.add("Chakan");
        region.add("Chikhali");
        region.add("Chinchwad");
        region.add("Chinchwad East");
        region.add("Chinchwadgaon");
        region.add("Dapodi");
        region.add("Dattawadi");
        region.add("Deccan Gymkhana");
        region.add("Dehu Road");
        region.add("Dhankawadi");
        region.add("Dhanori");
        region.add("Dhayari");
        region.add("Dhole Patil Road");
        region.add("Dighi Camp");
        region.add("Erandwana");
        region.add("Fatima Nagar");
        region.add("Fergusson College Road");
        region.add("Ganesh Peth");
        region.add("Ganeshkhind");
        region.add("Ghorpade Peth");
        region.add("Gokhale Nagar");
        region.add("Gultekdi");
        region.add("Guruwar Peth");
        region.add("Hadapsar");
        region.add("Hadapsar Indl Estate");
        region.add("Handevadi");
        region.add("Hingane Khurd");
        region.add("Hinjewadi");
        region.add("Indrayani Darshan Dehu Road");
        region.add("Jangali Maharaj Road");
        region.add("Kalewadi");
        region.add("Kalyani Nagar");
        region.add("Karve Nagar");
        region.add("Karve Road");
        region.add("Kasarwadi");
        region.add("Kasba Peth");
        region.add("Katraj");
        region.add("Khadakwasla");
        region.add("Khadki");
        region.add("Kharadi");
        region.add("Khed");
        region.add("Kondhwa");
        region.add("Kondhwa Budruk");
        region.add("Kondhwa Khurd");
        region.add("Koregaon Park");
        region.add("Kothrud");
        region.add("Law College Road");
        region.add("Laxmi Road");
        region.add("Lohegaon");
        region.add("Loni Kalbhor");
        region.add("Lulla Nagar");
        region.add("Magarpatta");
        region.add("Mahatma Gandhi Road");
        region.add("Mangalwar Peth");
        region.add("Manik Baug");
        region.add("Market Yard");
        region.add("Megapolis");
        region.add("Model Colony");
        region.add("Mukund Nagar");
        region.add("Mundhawa");
        region.add("Nagar Road");
        region.add("Nana Peth");
        region.add("Narayan Peth");
        region.add("Narayangaon");
        region.add("Navi Peth");
        region.add("Navsahyadri");
        region.add("Nigdi");
        region.add("Padmavati");
        region.add("Parvati");
        region.add("Pashan");
        region.add("Paud Road");
        region.add("Phursungi");
        region.add("Pimple Gurav");
        region.add("Pimple Nilakh");
        region.add("Pimple Saudagar");
        region.add("Pimpri");
        region.add("Pimpri Chinchwad");
        region.add("Pimpri Colony");
        region.add("Pirangut");
        region.add("Prabhat Road");
        region.add("Pune Railway Station");
        region.add("Range Hill");
        region.add("Rasta Peth");
        region.add("Raviwar Peth");
        region.add("S.P. College");
        region.add("Sadashiv Peth");
        region.add("Sahakar Nagar");
        region.add("Salunke Vihar");
        region.add("Sangavi");
        region.add("Sasane Nagar");
        region.add("Sasoon Road");
        region.add("Satara Road");
        region.add("Senapati Bapat Marg");
        region.add("Shaniwar Peth");
        region.add("Shivaji Housing Society");
        region.add("Shivaji Nagar");
        region.add("Shukrawar Peth");
        region.add("Sinhagad Road");
        region.add("Somwar Peth");
        region.add("Sus");
        region.add("Swargate");
        region.add("Talegaon Dabhade");
        region.add("Thergaon");
        region.add("Tilak Road");
        region.add("Undri");
        region.add("Uruli");
        region.add("Vadgaon Budruk");
        region.add("Vadgaon Sheri");
        region.add("Viman Nagar");
        region.add("Vishrantwadi");
        region.add("Wadgaon Budruk");
        region.add("Wagholi");
        region.add("Wakad");
        region.add("Wakadewadi");
        region.add("Wanawadi");
        region.add("Warje");
        region.add("Warje Malwadi");
        region.add("Yerawada");
        region1.add("Dharavi");
        region1.add("Fort");
        region1.add("Girgaon");
        region1.add("Lower-Parel");
        region1.add("Mahim");
        region1.add("Malabar");
        region1.add("Mandavi");
        region1.add("Matunga");
        region1.add("Mazgaon");
        region1.add("Naigaon");
        region1.add("Parel");
        region1.add("Parela-shiwadi");
        region1.add("Princes-Dock");
        region1.add("Saltpan");
        region1.add("Shiwadi");
        region1.add("Sion");
        region1.add("Tardev");
        region1.add("Varali");
        region1.add("Bhuleshwar");
        region1.add("Byculla");
        region1.add("Culaba R");
        region1.add("Dadar");
        region1.add("Dadara-naigaon");
        region2.add("Pavai");
        region2.add("Kolekalyan");
        region2.add("Eksar");
        region2.add("Pi");
        region2.add("Kondivita");
        region2.add("Erangal");
        region2.add("Poisar");
        region2.add("Kurla");
        region2.add("Ghatkopar");
        region2.add("Sai");
        region2.add("Madh");
        region2.add("Gorai");
        region2.add("Saki");
        region2.add("Majas");
        region2.add("Goregaon");
        region2.add("Shimpavali");
        region2.add("Marol");
        region2.add("Gundgaon");
        region2.add("Tirandaj");
        region2.add("Mogara");
        region2.add("Hariyali");
        region2.add("Tulasi");
        region2.add("Mulgaon");
        region2.add("Kandivali");
        region2.add("Tungaona");
        region2.add("Oshivara");
        region2.add("Kanheri");
        region2.add("Turbhe");
        region2.add("Parajapur");
        region2.add("Kanjur");
        region2.add("Vadhavali");
        region2.add("Parighakhar");
        region2.add("Kirol");
        region2.add("Vadhavan");
        region2.add("Sahar");
        region2.add("Klarebad");
        region2.add("Valnai");
        region2.add("Santakrujh");
        region2.add("Kopari");
        region2.add("Vikroli");
        region2.add("Varsova");
        region2.add("Kurar");
        region2.add("Vileparle");
        region2.add("Magathane");
        region2.add("Vyaravali");
        region2.add("Mahul");
        region2.add("Akse");
        region2.add("Malad");
        region2.add("Akurli");
        region2.add("Malavani");
        region2.add("Ambivali");
        region2.add("Anik");
        region2.add("Manbudruk");
        region2.add("Andheri");
        region2.add("Are");
        region2.add("Mandale");
        region2.add("Bandivali");
        region2.add("Asalpha");
        region2.add("Mandapeshwar");
        region2.add("Bandra");
        region2.add("Badhavan");
        region2.add("Balewadi");
        region2.add("Mankhurd");
        region2.add("Bapanale");
        region2.add("Bhandup");
        region2.add("Manori");
        region2.add("Bramhanvada");
        region2.add("Borala");
        region2.add("Maravali");
        region2.add("Borivali");
        region2.add("Chandivali");
        region2.add("Maroshi");
        region2.add("Chakala");
        region2.add("Charkop");
        region2.add("Marve");
        region2.add("Chuing");
        region2.add("Chembur");
        region2.add("Mohili");
        region2.add("Danda");
        region2.add("Chinchavali");
        region2.add("Mulund");
        region2.add("Gundavali");
        region2.add("Dahisar");
        region2.add("Nahur");
        region2.add("Ismaliya");
        region2.add("Daravali");
        region2.add("Nanole");
        region2.add("Pahadi");
        region2.add("Juhu");
        region2.add("Devanar");
        region2.add("Pasapoli");
        region2.add("Khari");
        region2.add("Dindoshi");
        region3.add("Belapur");
        region3.add("Bhaindar");
        region3.add("Bhivandi");
        region3.add("Chauk");
        region3.add("Dahisar");
        region3.add("Diva");
        region3.add("Dive");
        region3.add("Dongari");
        region3.add("Eiroli");
        region3.add("Kalyan");
        region3.add("Nerul");
        region3.add("Palghar");
        region3.add("Sanapada");
        region3.add("Thane");
        region3.add("Turbhe");
        region3.add("Ulhasnagar");
        region3.add("Vashi");
        region3.add("Badalapur");
        region3.add("Dahanu");
        region3.add("Javhar");
        region3.add("Mokhada");
        region3.add("Murabad");
        region3.add("Shahapur");
        region3.add("Talasari");
        region3.add("Vada");
        region3.add("Vasai");
        region3.add("Vikramagad");
        region3.add("Ambarnath");
        region1.add("Mumbai C.S.T");
        region1.add("Masjid");
        region1.add("Sandhurst Road");
        region1.add("Dockyard Road");
        region1.add("Chinchpokli");
        region1.add("Reay Road ");
        region1.add("Currey Road");
        region1.add("Cotton Green");
        region1.add("Lower Parel");
        region1.add("Sewree");
        region1.add("Vadala");
        region1.add("King Circle");
        region1.add("Chunabhatti");
        region1.add("Churchgate");
        region1.add("Marin Lines");
        region1.add("Charni Road ");
        region1.add("Grant Road");
        region1.add("Mumbai Central ");
        region1.add("Mahalaxmi");
        region1.add("Elphinstone Road");
        region1.add("Worli");
        region1.add("Culaba");
        region1.add("Malabar Hill");
        region4.add("Kharghar");
        region4.add("Mansarovar");
        region4.add("Khandeshwar");
        region4.add("Panvel");
        region4.add("New Panvel");
        region4.add("Komthe");
        region4.add("Taloja");
        region4.add("Kalamboli");
        region5.add("Sanpada");
        region5.add("Juinagar");
        region5.add("Seawoods");
        region5.add("Kopar-Khairane");
        region5.add("Ghansoli");
        region5.add("Rabale");
        region5.add("Airoli");
        region2.add("Khar");
        region2.add("Santacruz");
        region2.add("Vile Parle");
        region2.add("Jogeshwari");
        region2.add("Vidhyavihar");
        region2.add("Vikhroli");
        region2.add("Kanjurmarg");
        region2.add("Tilaknagar");
        region2.add("Govandi");
        region2.add("Maro");
        region2.add("Sakinaka");
        region2.add("Pawai");
        region3.add("Dhahisar");
        region3.add("Mira Road");
        region3.add("Bhayander");
        region3.add("Nallasopara");
        region3.add("Virar");
        region3.add("Vaitarna");
        region3.add("Saphale");
        region3.add("Boisar");
        region3.add("Dahanu Road");
        region3.add("Kalwa");
        region3.add("Mumbra");
        region3.add("Dombivali");
        region3.add("Thakurli");
        region3.add("Badlapur");
        region3.add("Shahad");
        region3.add("Titwala");
        region3.add("Ghodbunder Road");
        region3.add("Nilaje");
        region3.add("Shilphata");


        division7.add("Akurdi");
        division7.add("Alandi Devachi");
        division7.add("Alandi Khed");
        division7.add("Alandi Road");
        division5.add("Amanora Township");
        division5.add("Ambegaon BK");
        division5.add("Anandnagar");
        division7.add("Aundh");
        division7.add("Aundh Road");
        division5.add("Balaji Nagar");
        division7.add("Balewadi");
        division7.add("Baner");
        division7.add("Baner Road");
        division5.add("Bavdhan");
        division5.add("Bhandarkar Road");
        division5.add("Bhavani Peth");
        division5.add("Bhawani Peth Road");
        division7.add("Bhosari");
        division7.add("Bhosarigaon");
        division7.add("Bibvewadi");
        division5.add("Blueridge");
        division7.add("Bopodi");
        division7.add("Budhwar Peth");
        division5.add("Bund Garden Road");
        division5.add("Camp");
        division5.add("Chakan");
        division7.add("Chikhali");
        division7.add("Chinchwad");
        division7.add("Chinchwad East");
        division7.add("Chinchwadgaon");
        division7.add("Dapodi");
        division7.add("Dattawadi");
        division5.add("Deccan Gymkhana");
        division5.add("Dehu Road");
        division7.add("Dhankawadi");
        division5.add("Dhanori");
        division7.add("Dhayari");
        division5.add("Dhole Patil Road");
        division5.add("Dighi Camp");
        division5.add("Erandwana");
        division5.add("Fatima Nagar");
        division7.add("Fergusson College Road");
        division5.add("Ganesh Peth");
        division5.add("Ganeshkhind");
        division5.add("Ghorpade Peth");
        division5.add("Gokhale Nagar");
        division5.add("Gultekdi");
        division5.add("Guruwar Peth");
        division5.add("Hadapsar");
        division5.add("Hadapsar Indl Estate");
        division5.add("Handevadi");
        division5.add("Hingane Khurd");
        division7.add("Hinjewadi");
        division7.add("Indrayani Darshan Dehu Road");
        division5.add("Jangali Maharaj Road");
        division7.add("Kalewadi");
        division5.add("Kalyani Nagar");
        division5.add("Karve Nagar");
        division5.add("Karve Road");
        division7.add("Kasarwadi");
        division5.add("Kasba Peth");
        division5.add("Katraj");
        division5.add("Khadakwasla");
        division7.add("Khadki");
        division7.add("Kharadi");
        division7.add("Khed");
        division5.add("Kondhwa");
        division5.add("Kondhwa Budruk");
        division5.add("Kondhwa Khurd");
        division5.add("Koregaon Park");
        division5.add("Kothrud");
        division5.add("Law College Road");
        division5.add("Laxmi Road");
        division5.add("Lohegaon");
        division5.add("Loni Kalbhor");
        division5.add("Lulla Nagar");
        division5.add("Magarpatta");
        division5.add("Mahatma Gandhi Road");
        division5.add("Mangalwar Peth");
        division5.add("Manik Baug");
        division5.add("Market Yard");
        division7.add("Megapolis");
        division5.add("Model Colony");
        division5.add("Mukund Nagar");
        division5.add("Mundhawa");
        division5.add("Nagar Road");
        division5.add("Nana Peth");
        division5.add("Narayan Peth");
        division5.add("Narayangaon");
        division5.add("Navi Peth");
        division5.add("Navsahyadri");
        division5.add("Nigdi");
        division7.add("Padmavati");
        division5.add("Parvati");
        division5.add("Pashan");
        division7.add("Paud Road");
        division5.add("Phursungi");
        division5.add("Pimple Gurav");
        division7.add("Pimple Nilakh");
        division7.add("Pimple Saudagar");
        division7.add("Pimpri");
        division7.add("Pimpri Chinchwad");
        division7.add("Pimpri Colony");
        division7.add("Pirangut");
        division5.add("Prabhat Road");
        division5.add("Pune Railway Station");
        division5.add("Range Hill");
        division7.add("Rasta Peth");
        division5.add("Raviwar Peth");
        division5.add("S.P. College");
        division5.add("Sadashiv Peth");
        division5.add("Sahakar Nagar");
        division5.add("Salunke Vihar");
        division5.add("Sangavi");
        division7.add("Sasane Nagar");
        division5.add("Sasoon Road");
        division5.add("Satara Road");
        division5.add("Senapati Bapat Marg");
        division5.add("Shaniwar Peth");
        division5.add("Shivaji Housing Society");
        division5.add("Shivaji Nagar");
        division5.add("Shukrawar Peth");
        division5.add("Sinhagad Road");
        division5.add("Somwar Peth");
        division5.add("Sus");
        division5.add("Swargate");
        division7.add("Talegaon Dabhade");
        division5.add("Thergaon");
        division7.add("Tilak Road");
        division7.add("Undri");
        division5.add("Uruli");
        division5.add("Vadgaon Budruk");
        division5.add("Vadgaon Sheri");
        division5.add("Viman Nagar");
        division5.add("Vishrantwadi");
        division5.add("Wadgaon Budruk");
        division5.add("Wagholi");
        division5.add("Wakad");
        division5.add("Wakadewadi");
        division7.add("Wanawadi");
        division5.add("Warje");
        division5.add("Warje Malwadi");
        division5.add("Yerawada");
        division5.add("Dharavi");
        division5.add("Fort");
        division3.add("Girgaon");
        division3.add("Lower-Parel");
        division3.add("Mahim");
        division3.add("Malabar");
        division3.add("Mandavi");
        division3.add("Matunga");
        division3.add("Mazgaon");
        division3.add("Naigaon");
        division3.add("Parel");
        division3.add("Parela-shiwadi");
        division3.add("Princes-Dock");
        division3.add("Saltpan");
        division3.add("Shiwadi");
        division3.add("Sion");
        division3.add("Tardev");
        division3.add("Varali");
        division3.add("Bhuleshwar");
        division3.add("Byculla");
        division3.add("Culaba R");
        division3.add("Dadar");
        division3.add("Dadara-naigaon");
        division3.add("Pavai");
        division3.add("Kolekalyan");
        division4.add("Eksar");
        division4.add("Pi");
        division4.add("Kondivita");
        division4.add("Erangal");
        division4.add("Poisar");
        division4.add("Kurla");
        division4.add("Ghatkopar");
        division3.add("Sai");
        division3.add("Madh");
        division3.add("Gorai");
        division4.add("Saki");
        division4.add("Majas");
        division3.add("Goregaon");
        division4.add("Shimpavali");
        division4.add("Marol");
        division4.add("Gundgaon");
        division3.add("Tirandaj");
        division2.add("Mogara");
        division2.add("Hariyali");
        division4.add("Tulasi");
        division2.add("Mulgaon");
        division2.add("Kandivali");
        division2.add("Tungaona");
        division4.add("Oshivara");
        division4.add("Kanheri");
        division4.add("Turbhe");
        division4.add("Parajapur");
        division2.add("Kanjur");
        division2.add("Vadhavali");
        division3.add("Parighakhar");
        division3.add("Kirol");
        division2.add("Vadhavan");
        division2.add("Sahar");
        division2.add("Klarebad");
        division4.add("Valnai");
        division2.add("Santakrujh");
        division2.add("Kopari");
        division2.add("Vikroli");
        division2.add("Varsova");
        division3.add("Kurar");
        division4.add("Vileparle");
        division4.add("Magathane");
        division4.add("Vyaravali");
        division4.add("Mahul");
        division2.add("Akse");
        division4.add("Malad");
        division4.add("Akurli");
        division4.add("Malavani");
        division4.add("Ambivali");
        division4.add("Anik");
        division4.add("Manbudruk");
        division4.add("Andheri");
        division2.add("Are");
        division4.add("Mandale");
        division2.add("Bandivali");
        division2.add("Asalpha");
        division4.add("Mandapeshwar");
        division4.add("Bandra");
        division2.add("Badhavan");
        division4.add("Balewadi");
        division2.add("Mankhurd");
        division3.add("Bapanale");
        division4.add("Bhandup");
        division3.add("Manori");
        division2.add("Bramhanvada");
        division2.add("Borala");
        division2.add("Maravali");
        division2.add("Borivali");
        division4.add("Chandivali");
        division4.add("Maroshi");
        division4.add("Chakala");
        division4.add("Charkop");
        division4.add("Marve");
        division4.add("Chuing");
        division4.add("Chembur");
        division3.add("Mohili");
        division2.add("Danda");
        division4.add("Chinchavali");
        division4.add("Mulund");
        division3.add("Gundavali");
        division4.add("Dahisar");
        division4.add("Nahur");
        division3.add("Ismaliya");
        division3.add("Daravali");
        division4.add("Nanole");
        division4.add("Pahadi");
        division4.add("Juhu");
        division4.add("Devanar");
        division3.add("Pasapoli");
        division4.add("Khari");
        division4.add("Dindoshi");
        division4.add("Belapur");
        division9.add("Bhaindar");
        division9.add("Bhivandi");
        division9.add("Chauk");
        division9.add("Dahisar");
        division9.add("Diva");
        division9.add("Dive");
        division9.add("Dongari");
        division9.add("Eiroli");
        division9.add("Kalyan");
        division9.add("Nerul");
        division9.add("Palghar");
        division9.add("Sanapada");
        division9.add("Thane");
        division9.add("Turbhe");
        division9.add("Ulhasnagar");
        division9.add("Vashi");
        division9.add("Badalapur");
        division9.add("Dahanu");
        division9.add("Javhar");
        division9.add("Mokhada");
        division9.add("Murabad");
        division9.add("Shahapur");
        division9.add("Talasari");
        division9.add("Vada");
        division9.add("Vasai");
        division9.add("Vikramagad");
        division9.add("Ambarnath");
        division9.add("Mumbai C.S.T");
        division3.add("Masjid");
        division3.add("Sandhurst Road");
        division3.add("Dockyard Road");
        division13.add("Chinchpokli");
        division13.add("Reay Road ");
        division13.add("Currey Road");
        division13.add("Cotton Green");
        division13.add("Lower Parel");
        division3.add("Sewree");
        division13.add("Vadala");
        division2.add("King Circle");
        division13.add("Chunabhatti");
        division13.add("Churchgate");
        division3.add("Marin Lines");
        division3.add("Charni Road ");
        division3.add("Grant Road");
        division3.add("Mumbai Central ");
        division3.add("Mahalaxmi");
        division3.add("Elphinstone Road");
        division3.add("Worli");
        division3.add("Culaba");
        division3.add("Malabar Hill");
        division13.add("Kharghar");
        division11.add("Mansarovar");
        division11.add("Khandeshwar");
        division11.add("Panvel");
        division11.add("New Panvel");
        division11.add("Komthe");
        division11.add("Taloja");
        division11.add("Kalamboli");
        division11.add("Sanpada");
        division12.add("Juinagar");
        division11.add("Seawoods");
        division11.add("Kopar-Khairane");
        division12.add("Ghansoli");
        division12.add("Rabale");
        division12.add("Airoli");
        division12.add("Khar");
        division3.add("Santacruz");
        division3.add("Vile Parle");
        division3.add("Jogeshwari");
        division4.add("Vidhyavihar");
        division13.add("Vikhroli");
        division13.add("Kanjurmarg");
        division13.add("Tilaknagar");
        division13.add("Govandi");
        division13.add("Maro");
        division4.add("Sakinaka");
        division4.add("Pawai");
        division1.add("Dhahisar");
        division4.add("Mira Road");
        division4.add("Bhayander");
        division4.add("Nallasopara");
        division4.add("Virar");
        division4.add("Vaitarna");
        division4.add("Saphale");
        division4.add("Boisar");
        division4.add("Dahanu Road");
        division4.add("Kalwa");
        division9.add("Mumbra");
        division9.add("Dombivali");
        division9.add("Thakurli");
        division9.add("Badlapur");
        division9.add("Shahad");
        division9.add("Titwala");
        division9.add("Ghodbunder Road");
        division12.add("Nilaje");
        division9.add("Shilphata");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, region);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(dataAdapter2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mregion = parent.getItemAtPosition(position).toString();
                //System.out.println("region:" + mregion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ScrollView m_Scroll = new ScrollView(this);
        m_Scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        Toolbar toolbar = new Toolbar(this);
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        toolbar.setBackgroundColor(Color.parseColor("#ffa500"));
        toolbar.setTitle("Appointment Booking");
        toolbar.setTitleTextColor(Color.WHITE);
        rootView.addView(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.addView(reason);
        rootView.addView(free_reason1);
        rootView.addView(citytext);
        rootView.addView(sp1);
        rootView.addView(regiontext);
        rootView.addView(sp2);
        rootView.addView(regiontext1);
        rootView.addView(update1);
        rootView.addView(slottime);
        rootView.addView(regiontext2);
        rootView.addView(sp3);
        rootView.addView(attendees);
        rootView.addView(tv);
        rootView.addView(check1);
        rootView.addView(tv1);
        rootView.addView(check2);


        m_Scroll.addView(rootView);

        setContentView(m_Scroll);
        update1.setOnClickListener(new View.OnClickListener() {

            Calendar myCalendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                StartTime = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                        slottime.setText(sdf.format(myCalendar.getTime()));
                        datevalue = slottime.getText().toString();

                        if (datevalue != datevalue1) {
                            if (division1.contains(mregion)) {
                                division_id = "1";
                            } else if (division2.contains(mregion)) {
                                division_id = "2";
                            } else if (division3.contains(mregion)) {
                                division_id = "3";
                            } else if (division4.contains(mregion)) {
                                division_id = "4";
                            } else if (division5.contains(mregion)) {
                                division_id = "5";
                            } else if (division7.contains(mregion)) {
                                division_id = "7";
                            } else if (division9.contains(mregion)) {
                                division_id = "9";
                            } else if (division11.contains(mregion)) {
                                division_id = "11";
                            } else if (division12.contains(mregion)) {
                                division_id = "12";
                            } else if (division13.contains(mregion)) {
                                division_id = "13";
                            }

                            //System.out.println("division_id:" + datevalue + division_id);
                            Intent i2 = new Intent(Appointmentbooking.this, getappointmentslot.class);
                            i2.putExtra("app_date", datevalue);
                            i2.putExtra("division_id", division_id);
                            startService(i2);

                            singleTask.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    DBOperation db = new DBOperation(Appointmentbooking.this);
                                    if (GenericMethods.app_value.equals("true")) {

                                        // categories1.clear();
                                        //System.out.println("from value if" + db.getAppointmentslot(db).size());
                                        for (int i = 0; i < db.getAppointmentslot(db).size(); i++) {
                                            System.out.println("from for" + datevalue + db.getAppointmentslot(db).get(i).get("start_time").substring(0, 8));

                                            if (db.getAppointmentslot(db).get(i).get("Available").equals("true") && db.getAppointmentslot(db).get(i).get("Block").equals("0")) {

                                                System.out.println("from if1" + db.getAppointmentslot(db).get(i).get("start_time") + db.getAppointmentslot(db).get(i).get("Block"));
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, categories1);
                                                        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        sp3.setAdapter(dataAdapter3);
                                                        sp3.setVisibility(View.VISIBLE);
                                                        regiontext2.setVisibility(View.VISIBLE);
                                                    }
                                                });


                                                starttime = db.getAppointmentslot(db).get(i).get("start_time").substring(0, 8);
                                                endtime = db.getAppointmentslot(db).get(i).get("End_time").substring(0, 8);

                                                if (db.getAppointmentslot(db).get(i).get("Discount").equals("true")) {
                                                    System.out.println("1");
                                                    finaltime = starttime + "-" + endtime + "(Discount%)";
                                                } else if (db.getAppointmentslot(db).get(i).get("Discount").equals("false")) {
                                                    System.out.println("2");
                                                    finaltime = starttime + "-" + endtime;
                                                }

                                                categories1.add(finaltime);


                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, categories1);
                                                        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        sp3.setAdapter(dataAdapter3);
                                                        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

                                                        {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                slot = parent.getSelectedItem().toString();
                                                                System.out.println("mspin:" + slot);
                                                                DBOperation db = new DBOperation(Appointmentbooking.this);
                                                                for (int i = 0; i < db.getAppointmentslot(db).size(); i++) {

                                                                    if (slot.substring(0, 8).equals(db.getAppointmentslot(db).get(i).get("start_time").substring(0, 8)) && (slot.substring(9).equals(db.getAppointmentslot(db).get(i).get("End_time").substring(0, 8)))) {


                                                                        slotid = db.getAppointmentslot(db).get(i).get("slot_id");
                                                                        System.out.println("mspin1:" + slotid);

                                                                    }


                                                                }


                                                            }


                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parent) {

                                                            }
                                                        });

                                                    }
                                                });


                                            }

                                        }
                                    }


                                }


                            }, 1000);


                        }

                    }

                };
                new DatePickerDialog(Appointmentbooking.this, StartTime, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        addattendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namelist.add(name1.getText().toString());
                emaillist.add(email2.getText().toString());
                contactlist.add(contact2.getText().toString());
                contactperson1.add(name1.getText().toString());
                contactmaillist.add(email2.getText().toString());
                contactphonelist.add(contact2.getText().toString());

                Snackbar snackbar = Snackbar.make(rootView, name1.getText().toString() + " " + "Added Successfully!!", Snackbar.LENGTH_LONG);
                snackbar.show();

                if (add1.isChecked()) {
                    add1.setChecked(false);
                }

                name.setVisibility(View.GONE);
                name1.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                email2.setVisibility(View.GONE);
                contact.setVisibility(View.GONE);
                contact2.setVisibility(View.GONE);
                addattendees.setVisibility(View.GONE);

            }
        });


        if (apptype.equals("1")) {

            namelist.clear();
            emaillist.clear();
            contactlist.clear();

            //System.out.println("from apptype2" + apptype);

            if (con1.equals("Today")) {

                for (int i = 0; i < db.getAllTodayList(db).size(); i++) {
                    if (db.getAllTodayList(db).get(i).get("docid").equals(document)) {


                        check1.setText(db.getAllTodayList(db).get(i).get("oname"));
                        value = check1.getText().toString();
                        //contactperson1.add(db.getAllTodayList(db).get(i).get("oname"));
                        contactmail = db.getAllTodayList(db).get(i).get("omail");
                        contactphone = db.getAllTodayList(db).get(i).get("ocontact");

                        check2.setText(db.getAllTodayList(db).get(i).get("tname"));
                        value1 = check2.getText().toString();
                        // contactperson1.add(db.getAllTodayList(db).get(i).get("tname"));
                        contactmail1 = db.getAllTodayList(db).get(i).get("tmail");
                        contactphone1 = db.getAllTodayList(db).get(i).get("tcontact");

                    }


                }


            }

            if (con1.equals("Older")) {

                for (int i = 0; i < db.getAllOlderist(db).size(); i++) {
                    if (db.getAllOlderist(db).get(i).get("docid").equals(document)) {

                        check1.setText(db.getAllOlderist(db).get(i).get("oname"));
                        value = check1.getText().toString();
                        System.out.println("1" + value);
                        //contactperson1.add(db.getAllOlderist(db).get(i).get("oname"));
                        contactmail = db.getAllOlderist(db).get(i).get("omail");
                        contactphone = db.getAllOlderist(db).get(i).get("ocontact");


                        check2.setText(db.getAllOlderist(db).get(i).get("tname"));
                        value1 = check2.getText().toString();
                        System.out.println("2" + value1);
                        //contactperson1.add(db.getAllOlderist(db).get(i).get("tname"));
                        contactmail1 = db.getAllOlderist(db).get(i).get("tmail");
                        contactphone1 = db.getAllOlderist(db).get(i).get("tcontact");
                    }
                }

            }
            if (con1.equals("New")) {

                for (int i = 0; i < db.getAllNewList(db).size(); i++) {
                    if (db.getAllNewList(db).get(i).get("docid").equals(document)) {

                        check1.setText(db.getAllNewList(db).get(i).get("oname"));
                        value = check1.getText().toString();
                        // contactperson1.add(db.getAllNewList(db).get(i).get("oname"));
                        contactmail = db.getAllNewList(db).get(i).get("omail");
                        contactphone = db.getAllNewList(db).get(i).get("ocontact");


                        check2.setText(db.getAllNewList(db).get(i).get("tname"));
                        value1 = check2.getText().toString();
                        //contactperson1.add(db.getAllNewList(db).get(i).get("tname"));
                        contactmail1 = db.getAllNewList(db).get(i).get("tmail");
                        contactphone1 = db.getAllNewList(db).get(i).get("tcontact");
                    }
                }

            }
            if (con1.equals("Completed")) {

                for (int i = 0; i < db.getAllList(db).size(); i++) {
                    if (db.getAllList(db).get(i).get("docid").equals(document))


                    {

                        check1.setText(db.getAllList(db).get(i).get("oname"));
                        value = check1.getText().toString();
                        //contactperson1.add(db.getAllList(db).get(i).get("oname"));
                        contactmail = db.getAllList(db).get(i).get("omail");
                        contactphone = db.getAllList(db).get(i).get("ocontact");


                        check2.setText(db.getAllList(db).get(i).get("tname"));
                        value1 = check2.getText().toString();
                        //contactperson1.add(db.getAllList(db).get(i).get("tname"));
                        contactmail1 = db.getAllList(db).get(i).get("tmail");
                        contactphone1 = db.getAllList(db).get(i).get("tcontact");
                    }
                }
            }
            check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (check1.isChecked()) {
                        // System.out.println("from check1");
                        namelist.add(value);
                        emaillist.add(contactmail);
                        contactlist.add(contactphone);
                        contactperson1.add(value);
                        contactmaillist.add(contactmail);
                        contactphonelist.add(contactphone);

                    } else if (!check1.isChecked()) {

                        if (namelist.contains(value)) {
                            namelist.remove(value);
                            emaillist.remove(contactmail);
                            contactlist.remove(contactphone);
                            contactperson1.remove(value);
                            contactmaillist.remove(contactmail);
                            contactphonelist.remove(contactphone);
                        }
                    }
                    //System.out.println("mailsize:"+contactmaillist.size()+" "+"contactsize:"+contactphonelist.size()+"positin:"+mposition);

                }
            });
            check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (check2.isChecked()) {
                        //System.out.println("from check2");
                        namelist.add(value1);
                        emaillist.add(contactmail1);
                        contactlist.add(contactphone1);
                        contactperson1.add(value1);
                        contactmaillist.add(contactmail1);
                        contactphonelist.add(contactphone1);


                    } else if (!check2.isChecked()) {

                        if (namelist.contains(value1)) {
                            namelist.remove(value1);
                            emaillist.remove(contactmail1);
                            contactlist.remove(contactphone1);
                            contactperson1.remove(value1);
                            contactmaillist.remove(contactmail1);
                            contactphonelist.remove(contactphone1);
                        }
                    }
                    // System.out.println("mailsize:"+contactmaillist.size()+" "+"contactsize:"+contactphonelist.size()+"positin:"+mposition);

                }
            });


        } else if (apptype.equals("2")) {
            int o_count = 0, t_count = 0, w_count = 0, aw_count = 0, poa_count = 0;


            for (int i = 0; i < db.getPartiesReport(db).size(); i++) {


                if (document.equals(db.getPartiesReport(db).get(i).get("document_id"))) {

                    TextView tv2 = new TextView(this);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen.font_size_medium));
                    tv2.setTextColor(Color.parseColor("#545454"));

                    CheckBox check3 = new CheckBox(this);
                    check3.setId(i);
                    check3.setTextSize(18);
                    check3.setTextColor(Color.parseColor("#000000"));
                    check3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    LinearLayout.LayoutParams params7 = (LinearLayout.LayoutParams) check3.getLayoutParams();
                    params7.setMargins(40, 0, 50, 0);

                    check3.setLayoutParams(params7);
                    //System.out.println("id1:" + check3.getId() + check3.isChecked());


                    if (db.getPartiesReport(db).get(i).get("party_type").equals("1")) {


                        if (o_count == 0) {
                            tv2.setText("  Owner:");
                            o_count = o_count + 1;
                            rootView.addView(tv2);
                        }

                        check3.setText(db.getPartiesReport(db).get(i).get("name"));
                        //contactperson1.add(db.getPartiesReport(db).get(i).get("name"));
                        rootView.addView(check3);

                    } else if (db.getPartiesReport(db).get(i).get("party_type").equals("2")) {


                        if (t_count == 0) {
                            tv2.setText("  Tenant:");
                            t_count = t_count + 1;
                            rootView.addView(tv2);
                        }
                        check3.setText(db.getPartiesReport(db).get(i).get("name"));
                        //contactperson1.add(db.getPartiesReport(db).get(i).get("name"));
                        rootView.addView(check3);

                    } else if (db.getPartiesReport(db).get(i).get("party_type").equals("4")) {

                        if (w_count == 0) {
                            tv2.setText("  Witness:");
                            w_count = w_count + 1;
                            rootView.addView(tv2);
                        }
                        check3.setText(db.getPartiesReport(db).get(i).get("name"));
                        //contactperson1.add(db.getPartiesReport(db).get(i).get("name"));
                        rootView.addView(check3);

                    } else if (db.getPartiesReport(db).get(i).get("party_type").equals("5")) {

                        if (aw_count == 0) {
                            tv2.setText("  Anulom Witness:");
                            aw_count = aw_count + 1;
                            rootView.addView(tv2);
                        }
                        check3.setText(db.getPartiesReport(db).get(i).get("name"));
                        //contactperson1.add(db.getPartiesReport(db).get(i).get("name"));
                        rootView.addView(check3);

                    } else if (db.getPartiesReport(db).get(i).get("party_type").equals("3")) {

                        if (poa_count == 0) {
                            tv2.setText("  POA:");
                            poa_count = poa_count + 1;
                            rootView.addView(tv2);
                        }
                        check3.setText(db.getPartiesReport(db).get(i).get("name"));
                        //contactperson1.add(db.getPartiesReport(db).get(i).get("name"));
                        rootView.addView(check3);

                    }


                }


                check1.setVisibility(View.GONE);
                check2.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                tv1.setVisibility(View.GONE);

            }


        }
        contactperson1.add("-SELECT-");

        ArrayAdapter<String> dataAdapter8 = new ArrayAdapter<String>(Appointmentbooking.this, R.layout.witnesslay, contactperson1);
        dataAdapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp5.setAdapter(dataAdapter8);
        sp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contactmaillist.add("select");
                contactphonelist.add("select");
                mperson1 = parent.getItemAtPosition(position).toString();
                mposition = position;
                //System.out.println("ID:"+parent.getItemAtPosition(position)+mposition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rootView.addView(add1);
        rootView.addView(name);
        rootView.addView(name1);
        rootView.addView(email);
        rootView.addView(email2);
        rootView.addView(contact);
        rootView.addView(contact2);
        rootView.addView(addattendees);
        rootView.addView(tv3);
        rootView.addView(sp5);
        rootView.addView(buildingtype);
        rootView.addView(rg);
        rootView.addView(address);
        rootView.addView(line1);
        rootView.addView(line2);
        rootView.addView(address1);
        rootView.addView(landmark);
        rootView.addView(landmark1);
        rootView.addView(confirm);

        add = address1.getText().toString();
        land = landmark1.getText().toString();

        for (j = 0; j < db.getPartiesReport(db).size(); j++) {
            if (document.equals(db.getPartiesReport(db).get(j).get("document_id"))) {
                checkk = checkk + 1;
                final CheckBox chec = (CheckBox) findViewById(j);


                chec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //System.out.println("id:" + chec.getId() + chec.isChecked());
                        if (chec.isChecked()) {

                            namelist.add(db.getPartiesReport(db).get(chec.getId()).get("name"));
                            emaillist.add(db.getPartiesReport(db).get(chec.getId()).get("email"));
                            contactlist.add(db.getPartiesReport(db).get(chec.getId()).get("contact_no"));
                            contactperson1.add(db.getPartiesReport(db).get(chec.getId()).get("name"));
                            contactmaillist.add(db.getPartiesReport(db).get(chec.getId()).get("email"));
                            contactphonelist.add(db.getPartiesReport(db).get(chec.getId()).get("contact_no"));

                            //System.out.println("1st insert");
                            //System.out.println("mailsize:"+contactmaillist.size()+" "+"contactsize:"+contactphonelist.size()+"positin:"+mposition);


                        } else if (!chec.isChecked()) {

                            if (namelist.contains(db.getPartiesReport(db).get(chec.getId()).get("name"))) {
                                namelist.remove(db.getPartiesReport(db).get(chec.getId()).get("name"));
                                emaillist.remove(db.getPartiesReport(db).get(chec.getId()).get("email"));
                                contactlist.remove(db.getPartiesReport(db).get(chec.getId()).get("contact_no"));
                                contactperson1.remove(db.getPartiesReport(db).get(chec.getId()).get("name"));
                                contactmaillist.remove(db.getPartiesReport(db).get(chec.getId()).get("email"));
                                contactphonelist.remove(db.getPartiesReport(db).get(chec.getId()).get("contact_no"));

                                //System.out.println(" deleted");

                            }
                        }

                    }


                });

            }
        }


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GenericMethods.isConnected(getApplicationContext())) {
                    Snackbar snackbar2 = Snackbar.make(rootView, "Refreshing...", Snackbar.LENGTH_LONG);
                    snackbar2.show();
                    if ((option.equals("1") && free_reason1.getText().toString().length() > 0) && line1.getText().toString().length() > 0 && line2.getText().toString().length() > 0 && address1.getText().toString().length() > 0 || (option.equals("0") && line1.getText().toString().length() > 0 && line2.getText().toString().length() > 0 && address1.getText().toString().length() > 0)) {


                        for (j = 0; j < doclist.size(); j++) {
                            System.out.println("namelistsize:" + namelist.size());
                            System.out.println("doclistsize:" + doclist.size());


                            SQLiteDatabase complaint = db.getWritableDatabase();
                            ContentValues valuesw = new ContentValues();

                            valuesw.put(DBManager.TableInfo.KEYID, ID1);
                            valuesw.put(DBManager.TableInfo.request_no, reportkey);
                            valuesw.put(DBManager.TableInfo.timenew, document);
                            valuesw.put(DBManager.TableInfo.slotid, slotid);
                            valuesw.put(DBManager.TableInfo.app_date, datevalue);
                            valuesw.put(DBManager.TableInfo.division_id, division_id);
                            valuesw.put(DBManager.TableInfo.region_id, mregion);
                            valuesw.put(DBManager.TableInfo.free, option);
                            valuesw.put(DBManager.TableInfo.free_reason, free_reason1.getText().toString());
                            valuesw.put(DBManager.TableInfo.attendees, namelist.get(j));
                            valuesw.put(DBManager.TableInfo.attendeesemail, emaillist.get(j));
                            valuesw.put(DBManager.TableInfo.attendeescontact, contactlist.get(j));
                            valuesw.put(DBManager.TableInfo.address, option1 + "," + line1.getText().toString() + "," + line2.getText().toString() + "," + address1.getText().toString());
                            valuesw.put(DBManager.TableInfo.landmark, landmark1.getText().toString());
                            valuesw.put(DBManager.TableInfo.contactperson, mperson1);
                            valuesw.put(DBManager.TableInfo.contactpersonemail, contactmaillist.get(mposition));
                            valuesw.put(DBManager.TableInfo.contactpersoncont, contactphonelist.get(mposition));
                            valuesw.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                            valuesw.put(DBManager.TableInfo.city, mspin);
                            valuesw.put(DBManager.TableInfo.AppointmentId, appointment_id);
                            String conditionw = DBManager.TableInfo.request_no + " =?";

                            Cursor cursorw = complaint.query(APPOINTMENTBOOKING, null, conditionw, new String[]{DBManager.TableInfo.slotid1}, null, null, null);
                            long statusw = complaint.insert(APPOINTMENTBOOKING, null, valuesw);
                            System.out.println("landmark:" + landmark1.getText().toString()+statusw);


                            cursorw.close();
                            complaint.close();


                        }

                        String commentID = randomInteger(0, 999999);
                        if (option.equals("0")) {
                            db.InsertRecord3(db, reportkey, "New Appointment Booked" + free_reason1.getText().toString(), username2, username2, commentID, "0", current_Date, GenericMethods.AsyncStatus, datevalue);
                        } else if (option.equals("1")) {
                            db.InsertRecord3(db, reportkey, "New Free Appointment Booked,Reason:" + free_reason1.getText().toString(), username2, username2, commentID, "0", current_Date, GenericMethods.AsyncStatus, datevalue);
                        }
                        Intent intent = new Intent(Appointmentbooking.this, SendCommentService.class);
                        startService(intent);

                        Intent i = new Intent(Appointmentbooking.this, postappointmentbooking.class);
                        startService(i);

                        namelist.clear();
                        emaillist.clear();
                        contactlist.clear();
                        singleTask.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println("Refresh over");
                                GenericMethods.ctr2 = 0;
                                GenericMethods.ctr3 = 0;

                                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }


                        }, startdelay);
                    } else {

                        Snackbar snackbar3 = Snackbar.make(rootView, "Enter All the required Fields!!", Snackbar.LENGTH_LONG);
                        snackbar3.show();
                    }

                } else {
                    Snackbar snackbar4 = Snackbar.make(rootView, "Please Check the Internet Connection!!", Snackbar.LENGTH_LONG);
                    snackbar4.show();
                }
            }
        });


    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            //openconnection-instance that represents a connection to the remote object referred to by the URL
            conn = (HttpURLConnection) url.openConnection();
            //Inputstream to convert bytes into characters
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            //Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
//                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
//                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public String randomInteger(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum + "";
    }

}






