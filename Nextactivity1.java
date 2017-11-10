package anulom.executioner5.com3.anulom;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.fragment.adhocdetails;
import anulom.executioner5.com3.anulom.services.GetAllDataList;
import anulom.executioner5.com3.anulom.services.GetCommentService;
import anulom.executioner5.com3.anulom.services.Posttask;
import anulom.executioner5.com3.anulom.services.SendCommentService;
import anulom.executioner5.com3.anulom.services.SendmultiPartyReport;
import anulom.executioner5.com3.anulom.services.SendPaymentService;
import anulom.executioner5.com3.anulom.services.SendReportService;
import anulom.executioner5.com3.anulom.services.StatusReportService;
import anulom.executioner5.com3.anulom.services.call;
import anulom.executioner5.com3.anulom.services.fetchactualtime;
import anulom.executioner5.com3.anulom.services.not_applicablepost;
import anulom.executioner5.com3.anulom.services.postappointmentbooking;
import anulom.executioner5.com3.anulom.services.reassignpost;
import anulom.executioner5.com3.anulom.services.rescheduledetails;

import static anulom.executioner5.com3.anulom.GenericMethods.TABLE_LAST_UPDATE;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.MULTIWORK;

/**
 * Created by anulom on 21/7/17.
 */

public class Nextactivity1 extends AppCompatActivity {


    String umail = Login.umailid;
    EditText etusermailid = Login.etusermailid;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DBOperation database;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date, ID;
    TextView text1;
    Button but1, but2, but3, but4;
    private ProgressDialog pDialog;
    Menu menu;

    private SharedPreferences usermail;
    private SharedPreferences Date1;
    private String username2 = "", password2 = "";
    public static NextActivity thisnext = null;
    DBOperation db;

    Timer singleTask = new Timer();
    int startdelay = 5000;
    // Tab titles


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next1);
        database = new DBOperation(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("  Anulom");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        setupViewPager(viewPager);
        text1 = (TextView) findViewById(R.id.text1);
        //tabLayout = (TabLayout) findViewById(R.id.tabs1);
        but1 = (Button) findViewById(R.id.but1);
        but2 = (Button) findViewById(R.id.but2);
        but3 = (Button) findViewById(R.id.but3);
        but4 = (Button) findViewById(R.id.but4);
//        tabLayout.setupWithViewPager(viewPager);

//System.out.println("Date:"+cursor.getString(cursor.getColumnIndex(GenericMethods.LAST_UPDATED)));



        usermail = PreferenceManager.getDefaultSharedPreferences(this);
        username2 = usermail.getString("username", "");
        password2 = usermail.getString("password", "");

        Date1=PreferenceManager.getDefaultSharedPreferences(this);
        String currentdate=Date1.getString("Date","");
        text1.setText("  " +currentdate);


    }

    public void appointment(View v) {


        Intent i = new Intent(Nextactivity1.this, NextActivity.class);
        startActivity(i);

    }

    public void adhoc(View v) {
        db = new DBOperation(Nextactivity1.this);
        //System.out.println("getadhoc:"+db.getadhocdetails(db));
        if (db.getadhocdetails(db).size() == 0) {


            Intent i = new Intent(Nextactivity1.this, adhocremainder.class);
            startActivity(i);

        }
    }

    public void witness(View v) {
        db = new DBOperation(Nextactivity1.this);
        if (db.getWitnessdetails(db).size() == 0) {

            Intent i = new Intent(Nextactivity1.this, adhocremainder.class);
            startActivity(i);

        } else {

            Intent i = new Intent(Nextactivity1.this, NextActivity2.class);
            startActivity(i);
        }
    }

    public void complaint(View v) {
        db = new DBOperation(Nextactivity1.this);
        if (db.getComplaintdetails(db).size() == 0) {

            Intent i = new Intent(Nextactivity1.this, adhocremainder.class);
            startActivity(i);

        } else {

            Intent i = new Intent(Nextactivity1.this, NextActivity3.class);
            startActivity(i);
        }
    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new adhocdetails(), "Adhoc");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        menu.findItem(R.id.login_id).setTitle(username2);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.action_refresh:


                if (GenericMethods.isConnected(getApplicationContext())) {
                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
                    Date = simpleDateFormat.format(calendar.getTime());
                    SharedPreferences.Editor editor = Date1.edit();
                    editor.putString("Date", Date);
                    editor.commit();
                    text1.setText("  " +Date);

                    SQLiteDatabase sqldb = database.getWritableDatabase();
                    int count = sqldb.delete(MULTIWORK, null, null);
                    System.out.println("CHECK PARTY DB ROW DELETED SUCCESS:" + count);

                    db = new DBOperation(Nextactivity1.this);


                    ArrayList<HashMap<String, String>> listofcommment = db.getcomment(db);
                    System.out.println("list from next:" + listofcommment.size());
                    ArrayList<HashMap<String, String>> acvrreportlist = db.getAcvrReport(db);
                    ArrayList<HashMap<String, String>> statusreportlist = db.getSyncStatusReport(db);
                    boolean flag = false;
                    if (listofcommment.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, SendCommentService.class);
                        startService(intent);
                        flag = true;


                    }
                    if (acvrreportlist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, SendReportService.class);
                        startService(intent);
                        flag = true;
                    }
                    if (statusreportlist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, StatusReportService.class);
                        startService(intent);
                        flag = true;

                    }
                    ArrayList<HashMap<String, String>> paymentlist = db.getPaymentReport(db);
//                    System.out.println("paymentlist size:"+paymentlist.size());
                    if (paymentlist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, SendPaymentService.class);
                        startService(intent);
                        flag = true;
                    }
//
                    ArrayList<HashMap<String, String>> partieslist = db.getPartypost(db);
//                    System.out.println("partieslistsize:" + partieslist.size());
                    if (partieslist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, SendmultiPartyReport.class);
                        startService(intent);
                        flag = true;
                    }
                    ArrayList<HashMap<String, String>> appointmentlist = db.postappointment(db);

                    if (appointmentlist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, postappointmentbooking.class);
                        startService(intent);
                        flag=true;
                    }

                    ArrayList<HashMap<String, String>> calllist = db.calldetails(db);

                    if (calllist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, call.class);
                        startService(intent);
                        flag=true;
                    }

                    ArrayList<HashMap<String, String>> reschedulelist = db.getrescheduledetails(db);

                    if (reschedulelist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, rescheduledetails.class);
                        startService(intent);
                        flag=true;
                    }

                    ArrayList<HashMap<String, String>> fetchactualtimelist = db.getactualtime(db);

                    if (fetchactualtimelist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, fetchactualtime.class);
                        startService(intent);
                        flag=true;
                    }
                    ArrayList<HashMap<String, String>> posttasklist = db.getposttaskdetails(db);

                    if (posttasklist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, Posttask.class);
                        startService(intent);
                        flag=true;
                    }
                    ArrayList<HashMap<String, String>> notassignlist = db.getnot_applicable(db);

                    if (notassignlist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, not_applicablepost.class);
                        startService(intent);
                        flag=true;
                    }
                    ArrayList<HashMap<String, String>> reassignlist = db.getreassign(db);

                    if (reassignlist.size() > 0) {
                        Intent intent = new Intent(Nextactivity1.this, reassignpost.class);
                        startService(intent);
                        flag=true;
                    }

                    if (flag) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Nextactivity1.this).create();
                        alertDialog.setMessage("Need To Sync All Data");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    } else {

                        pDialog = new ProgressDialog(Nextactivity1.this);
                        pDialog.setMessage("Fetching Data...");

                        pDialog.setCancelable(false);
                        pDialog.show();

                        Intent service = new Intent(getApplicationContext(), GetAllDataList.class);
                        startService(service);
                        Intent inte = new Intent(getApplicationContext(), GetCommentService.class);
                        startService(inte);
                    }

                    singleTask.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("Refresh over");
                            GenericMethods.ctr2 = 0;
                            GenericMethods.ctr3 = 0;



                        }


                    }, startdelay);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_LONG).show();
                }


                return true;

            case R.id.abt:
                Intent i3 = new Intent(getApplicationContext(), about.class);
                startActivity(i3);
                return true;


            case R.id.weekend:
                Intent i2 = new Intent(Nextactivity1.this, weekendoff.class);
                startActivity(i2);


                this.finish();
                return true;

            case R.id.log_out:

                usermail = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = usermail.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();

                GenericMethods.ctr2 = 0;
                GenericMethods.ctr3 = 0;
                Intent i = new Intent(getApplicationContext(), Login.class);

                startActivity(i);


        }
        return super.onOptionsItemSelected(item);
    }

    public void closeProgressBar() {
        pDialog.cancel();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        finish();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (singleTask != null) {

            singleTask.cancel();
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        thisnext = null;
    }
}


