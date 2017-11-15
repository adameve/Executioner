package anulom.executioner5.com3.anulom.fragment;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import anulom.executioner5.com3.anulom.Pay;
import anulom.executioner5.com3.anulom.ACVReport;
import anulom.executioner5.com3.anulom.CommentInfo;
import anulom.executioner5.com3.anulom.Details;
import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.IFetchCurrentLocation;
import anulom.executioner5.com3.anulom.MapsActivity;
import anulom.executioner5.com3.anulom.R;
import anulom.executioner5.com3.anulom.StatusInfo;

import anulom.executioner5.com3.anulom.appointmentoption;
import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.newstatusinfo;
import anulom.executioner5.com3.anulom.reschedule;
import anulom.executioner5.com3.anulom.services.call;
import anulom.executioner5.com3.anulom.services.fetchactualtime;

import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.ACTUAL_TIME;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.CALL;

public class CompletedDetails extends Fragment implements IFetchCurrentLocation {

    public static CompletedDetails thiscompleteDetails = null;
    ListView tasklist;
    String content1 = "Completed", Date, timevalue1 = "", ID1, curdate1, A, o_ = "", t_ = "", w1_ = "", w2_ = "";
    DBOperation db;
    Button time;
    int count = 0;
    TextView comment;
    private SharedPreferences usermail;
    private String username2 = "", password2 = "";
    ArrayList<HashMap<String, String>> getAlldataList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> getCommentDatalist = null;
    ArrayList<String> reportkey = new ArrayList<>();
    ArrayList<String> documentidpay = new ArrayList<>();
    ArrayList<String> documentidcomment = new ArrayList<>();
    ArrayList<HashMap<String, String>> paymentlist = null;
    double currentLat;
    double currentLong;
    ArrayAdapter<String> adpt;
    private Calendar mcurrenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.viewdetails, container, false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        curdate1 = df2.format(c.getTime());
        tasklist = (ListView) rootView.findViewById(R.id.tasklist);
        reFreshReload();
        return rootView;
    }

    private void createAdpt() {
        // TODO Auto-generated method stub

        adpt = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.taskdetails, reportkey) {

            public View getView(final int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View v = inflater.inflate(R.layout.taskdetails, null);
                TextView R_key = (TextView) v.findViewById(R.id.tvreportKey);
                TextView Oname = (TextView) v.findViewById(R.id.tvowner1);
                TextView Tname = (TextView) v.findViewById(R.id.tvtenant1);
                TextView status = (TextView) v.findViewById(R.id.tvstatus1);
                final TextView date = (TextView) v.findViewById(R.id.tvdate1);
                comment = (TextView) v.findViewById(R.id.tvcomment1);
                TextView addpay = (TextView) v.findViewById(R.id.tvAddpay1);
                TextView acvr = (TextView) v.findViewById(R.id.tvACVR1);
                TextView map = (TextView) v.findViewById(R.id.tvMap1);
                ImageView sync_status = (ImageView) v.findViewById(R.id.imgSyncStatus);
                ImageView comment_sync_status = (ImageView) v.findViewById(R.id.imgCommmentSyncStatus);
                ImageView acvr_sync_status = (ImageView) v.findViewById(R.id.imgACVRSyncStatus);

                TextView appoint = (TextView) v.findViewById(R.id.appointment);
                LinearLayout contactperson = (LinearLayout) v.findViewById(R.id.contactperson);

                TextView contac1 = (TextView) v.findViewById(R.id.contact1);
                TextView biometricstatus = (TextView) v.findViewById(R.id.bio1);
                TextView biometric = (TextView) v.findViewById(R.id.bio2);
                biometricstatus.setVisibility(View.GONE);
                biometric.setVisibility(View.GONE);
                final Button but3 = (Button) v.findViewById(R.id.buttonnotify1);

                but3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (count == 1) {
                            but3.setBackgroundResource(R.drawable.notification_2);
                            count = 0;
                        } else {
                            but3.setBackgroundResource(R.drawable.notification_1);
                            count = count + 1;
                        }

                    }
                });
                time = (Button) v.findViewById(R.id.owner2);
                usermail = PreferenceManager.getDefaultSharedPreferences(getActivity());
                username2 = usermail.getString("username", "");
                password2 = usermail.getString("password", "");

                LinearLayout owner = (LinearLayout) v.findViewById(R.id.layout1);
                LinearLayout tenant = (LinearLayout) v.findViewById(R.id.layout2);
                ImageView img1 = (ImageView) v.findViewById(R.id.buttonimg);
                ImageView img2 = (ImageView) v.findViewById(R.id.buttonimg1);
                ImageView img3 = (ImageView) v.findViewById(R.id.buttonimg2);
                if (getAlldataList.get(position).get("contact_person").equals("No details")) {


                } else if (getAlldataList.get(position).get("contact_person").equals("Details")) {


                    if (getAlldataList.get(position).get("apptype").equals("1")) {

                        if (getAlldataList.get(position).get("ocontact").equals(getAlldataList.get(position).get("contact_no"))) {

                            Oname.setTextColor(Color.parseColor("#006B3C"));
                            Oname.setAllCaps(true);
                            img1.setVisibility(View.VISIBLE);

                            owner.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    calendar = Calendar.getInstance();
                                    simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                                    Date = simpleDateFormat.format(calendar.getTime());

                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + getAlldataList.get(position).get("ocontact")));
                                    startActivity(intent);
                                    SQLiteDatabase sqldb1 = db.getWritableDatabase();
                                    ContentValues values1 = new ContentValues();
                                    values1.put(DBManager.TableInfo.KEYID, ID1);
                                    values1.put(DBManager.TableInfo.DocumentId, getAlldataList.get(position).get("docid"));
                                    values1.put(DBManager.TableInfo.AppointmentId, getAlldataList.get(position).get("appid"));
                                    values1.put(DBManager.TableInfo.actual_time, timevalue1);
                                    values1.put(DBManager.TableInfo.call_time, Date);
                                    values1.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                                    String conditionw = DBManager.TableInfo.AppointmentId + " =?";
                                    Cursor cursorw = sqldb1.query(CALL, null, conditionw, new String[]{DBManager.TableInfo.actual_time}, null, null, null);
                                    sqldb1.insert(CALL, null, values1);
                                    cursorw.close();
                                    sqldb1.close();
                                    Intent intent1 = new Intent(getActivity().getApplicationContext(), call.class);
                                    getActivity().getApplicationContext().startService(intent1);

                                }
                            });

                        } else if (getAlldataList.get(position).get("tcontact").equals(getAlldataList.get(position).get("contact_no"))) {

                            Tname.setTextColor(Color.parseColor("#006B3C"));
                            Tname.setAllCaps(true);
                            img2.setVisibility(View.VISIBLE);
                            tenant.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    calendar = Calendar.getInstance();
                                    simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                                    Date = simpleDateFormat.format(calendar.getTime());
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + getAlldataList.get(position).get("tcontact")));
                                    startActivity(intent);

                                    SQLiteDatabase sqldb1 = db.getWritableDatabase();
                                    ContentValues values1 = new ContentValues();
                                    values1.put(DBManager.TableInfo.KEYID, ID1);
                                    values1.put(DBManager.TableInfo.DocumentId, getAlldataList.get(position).get("docid"));
                                    values1.put(DBManager.TableInfo.AppointmentId, getAlldataList.get(position).get("appid"));
                                    values1.put(DBManager.TableInfo.actual_time, timevalue1);
                                    values1.put(DBManager.TableInfo.call_time, Date);
                                    values1.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                                    String conditionw = DBManager.TableInfo.AppointmentId + " =?";
                                    Cursor cursorw = sqldb1.query(CALL, null, conditionw, new String[]{DBManager.TableInfo.actual_time}, null, null, null);
                                    sqldb1.insert(CALL, null, values1);
                                    cursorw.close();
                                    sqldb1.close();
                                    Intent intent1 = new Intent(getActivity().getApplicationContext(), call.class);
                                    getActivity().getApplicationContext().startService(intent1);
                                }
                            });


                        } else {
                            contactperson.setVisibility(View.VISIBLE);
                            contac1.setText(getAlldataList.get(position).get("contact_name"));
                            contac1.setTextColor(Color.parseColor("#006B3C"));
                            contac1.setAllCaps(true);
                            img3.setVisibility(View.VISIBLE);
                            contactperson.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    calendar = Calendar.getInstance();
                                    simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                                    Date = simpleDateFormat.format(calendar.getTime());

                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + getAlldataList.get(position).get("contact_no")));
                                    startActivity(intent);

                                    SQLiteDatabase sqldb1 = db.getWritableDatabase();
                                    ContentValues values1 = new ContentValues();
                                    values1.put(DBManager.TableInfo.KEYID, ID1);
                                    values1.put(DBManager.TableInfo.DocumentId, getAlldataList.get(position).get("docid"));
                                    values1.put(DBManager.TableInfo.AppointmentId, getAlldataList.get(position).get("appid"));
                                    values1.put(DBManager.TableInfo.actual_time, timevalue1);
                                    values1.put(DBManager.TableInfo.call_time, Date);
                                    values1.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                                    String conditionw = DBManager.TableInfo.AppointmentId + " =?";
                                    Cursor cursorw = sqldb1.query(CALL, null, conditionw, new String[]{DBManager.TableInfo.actual_time}, null, null, null);
                                    sqldb1.insert(CALL, null, values1);
                                    //System.out.println("time:"+timevalue1+getAlldataList.get(position).get("appid")+String.valueOf(currentTime).substring(10,20));
                                    cursorw.close();
                                    sqldb1.close();
                                    Intent intent1 = new Intent(getActivity().getApplicationContext(), call.class);
                                    getActivity().getApplicationContext().startService(intent1);
                                }
                            });
                        }

                    } else if (getAlldataList.get(position).get("apptype").equals("2")) {

                        if (getAlldataList.get(position).get("ocontact").equals(getAlldataList.get(position).get("contact_no"))) {

                            //OwnerContact.setVisibility(View.VISIBLE);
                            Oname.setTextColor(Color.parseColor("#006B3C"));
                            Oname.setAllCaps(true);
                            img1.setVisibility(View.VISIBLE);
                            owner.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    calendar = Calendar.getInstance();
                                    simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                                    Date = simpleDateFormat.format(calendar.getTime());

                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + getAlldataList.get(position).get("contact_no")));
                                    startActivity(intent);

                                    SQLiteDatabase sqldb1 = db.getWritableDatabase();
                                    ContentValues values1 = new ContentValues();
                                    values1.put(DBManager.TableInfo.KEYID, ID1);
                                    values1.put(DBManager.TableInfo.DocumentId, getAlldataList.get(position).get("docid"));
                                    values1.put(DBManager.TableInfo.AppointmentId, getAlldataList.get(position).get("appid"));
                                    values1.put(DBManager.TableInfo.actual_time, timevalue1);
                                    values1.put(DBManager.TableInfo.call_time, Date);
                                    values1.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                                    String conditionw = DBManager.TableInfo.AppointmentId + " =?";
                                    Cursor cursorw = sqldb1.query(CALL, null, conditionw, new String[]{DBManager.TableInfo.actual_time}, null, null, null);
                                    sqldb1.insert(CALL, null, values1);
                                    //System.out.println("time:"+timevalue1+getAlldataList.get(position).get("appid")+String.valueOf(currentTime).substring(10,20));
                                    cursorw.close();
                                    sqldb1.close();
                                    Intent intent1 = new Intent(getActivity().getApplicationContext(), call.class);
                                    getActivity().getApplicationContext().startService(intent1);

                                }
                            });

                        } else if (getAlldataList.get(position).get("tcontact").equals(getAlldataList.get(position).get("contact_no"))) {

                            //TenantContact.setVisibility(View.VISIBLE);
                            Tname.setTextColor(Color.parseColor("#006B3C"));
                            Tname.setAllCaps(true);
                            img2.setVisibility(View.VISIBLE);
                            tenant.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    calendar = Calendar.getInstance();
                                    simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                                    Date = simpleDateFormat.format(calendar.getTime());

                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + getAlldataList.get(position).get("contact_no")));
                                    startActivity(intent);

                                    SQLiteDatabase sqldb1 = db.getWritableDatabase();
                                    ContentValues values1 = new ContentValues();
                                    values1.put(DBManager.TableInfo.KEYID, ID1);
                                    values1.put(DBManager.TableInfo.DocumentId, getAlldataList.get(position).get("docid"));
                                    values1.put(DBManager.TableInfo.AppointmentId, getAlldataList.get(position).get("appid"));
                                    values1.put(DBManager.TableInfo.actual_time, timevalue1);
                                    values1.put(DBManager.TableInfo.call_time, Date);
                                    values1.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                                    String conditionw = DBManager.TableInfo.AppointmentId + " =?";
                                    Cursor cursorw = sqldb1.query(CALL, null, conditionw, new String[]{DBManager.TableInfo.actual_time}, null, null, null);
                                    sqldb1.insert(CALL, null, values1);
                                    // System.out.println("time:"+timevalue1+getAlldataList.get(position).get("appid")+String.valueOf(currentTime).substring(10,20));
                                    cursorw.close();
                                    sqldb1.close();
                                    Intent intent1 = new Intent(getActivity().getApplicationContext(), call.class);
                                    getActivity().getApplicationContext().startService(intent1);
                                }
                            });


                        } else {
                            contactperson.setVisibility(View.VISIBLE);
                            contac1.setText(getAlldataList.get(position).get("contact_name"));
                            contac1.setTextColor(Color.parseColor("#006B3C"));
                            contac1.setAllCaps(true);
                            img3.setVisibility(View.VISIBLE);
                            contactperson.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    calendar = Calendar.getInstance();
                                    simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                                    Date = simpleDateFormat.format(calendar.getTime());

                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + getAlldataList.get(position).get("contact_no")));
                                    startActivity(intent);

                                    SQLiteDatabase sqldb1 = db.getWritableDatabase();
                                    ContentValues values1 = new ContentValues();
                                    values1.put(DBManager.TableInfo.KEYID, ID1);
                                    values1.put(DBManager.TableInfo.DocumentId, getAlldataList.get(position).get("docid"));
                                    values1.put(DBManager.TableInfo.AppointmentId, getAlldataList.get(position).get("appid"));
                                    values1.put(DBManager.TableInfo.actual_time, timevalue1);
                                    values1.put(DBManager.TableInfo.call_time, Date);
                                    values1.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                                    String conditionw = DBManager.TableInfo.AppointmentId + " =?";
                                    Cursor cursorw = sqldb1.query(CALL, null, conditionw, new String[]{DBManager.TableInfo.actual_time}, null, null, null);
                                    sqldb1.insert(CALL, null, values1);
                                    //System.out.println("time:"+timevalue1+getAlldataList.get(position).get("appid")+String.valueOf(currentTime).substring(10,20));
                                    cursorw.close();
                                    sqldb1.close();
                                    Intent intent1 = new Intent(getActivity().getApplicationContext(), call.class);
                                    getActivity().getApplicationContext().startService(intent1);
                                }
                            });
                        }

                    }


                }


                TextView schedule = (TextView) v.findViewById(R.id.tvdate2);
                Oname.setText(getAlldataList.get(position).get("oname"));
                Tname.setText(getAlldataList.get(position).get("tname"));
//                status.setText(getAlldataList.get(position).get("tstatus"));

                String date1 = getAlldataList.get(position).get("sdate");
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                Date date11 = null;
                try {
                    date11 = format1.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                System.out.println(format2.format(date11));

                String datest = getAlldataList.get(position).get("stime1");
                SimpleDateFormat formatst = new SimpleDateFormat("HH:mm");
                SimpleDateFormat formatst2 = new SimpleDateFormat("hh:mm");
                Date stdate11 = null;
                try {
                    stdate11 = formatst.parse(datest);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                System.out.println(formatst2.format(stdate11));

                String dateend = getAlldataList.get(position).get("stime2");
                SimpleDateFormat formatend = new SimpleDateFormat("HH:mm");
                SimpleDateFormat formatend2 = new SimpleDateFormat("hh:mm a");
                Date enddate11 = null;
                try {
                    enddate11 = formatend.parse(dateend);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                System.out.println(formatend2.format(enddate11));

                date.setText(format2.format(date11) + " " + "\n" + formatst2.format(stdate11) + "-" + formatend2.format(enddate11));

                String strStatus = getAlldataList.get(position).get("syncstatus");
                String strAcvrReportStatus = getAlldataList.get(position).get("acvrreportstatus");


                boolean commentStatus = CheckCommentStatus(position);
                if (commentStatus) {
                    comment.setBackgroundResource(R.drawable.commentsgreen);
                } else {
                    comment.setBackgroundResource(R.drawable.commentsred);
                }

//                System.out.println("apptypefromcompleted:"+getAlldataList.get(position).get("apptype"));
                //System.out.println("apptypefromolder:"+getAlldataList.get(position).get("apptype"));
                if (getAlldataList.get(position).get("apptype").equals("1")) {

                    A = "(S)";
                    if (getAlldataList.get(position).get("biocom1").equals("1")) {
                        o_ = "O";
                    } else {
                        o_ = "";
                    }
                    if (getAlldataList.get(position).get("biocomp").equals("1")) {
                        t_ = "T";
                    } else {
                        t_ = "";
                    }
                    if (getAlldataList.get(position).get("regfromcomp").equals("1")) {
                        w1_ = "W1";
                    } else {
                        w1_ = "";
                    }
                    if (getAlldataList.get(position).get("witness").equals("1")) {
                        w2_ = "W2";
                    } else {
                        w2_ = "";
                    }

                } else if (getAlldataList.get(position).get("apptype").equals("2")) {
                    A = "(M)";


                } else if (getAlldataList.get(position).get("apptype").equals("null")) {
                    A = "";
                }
                boolean reportkeystatus = checkreportkeystatus(position);
                if (reportkeystatus) {
                    R_key.setText(getAlldataList.get(position).get("rkey") + A);
                    R_key.setTextColor(Color.parseColor("#ff0000"));
                } else {
                    R_key.setText(getAlldataList.get(position).get("rkey") + A);
                    R_key.setTextColor(Color.parseColor("#006B3C"));
                }


                if (strAcvrReportStatus.equals("ASYNC")) {
                    acvr.setBackgroundResource(R.drawable.reportred);
                } else {
                    acvr.setBackgroundResource(R.drawable.reportgreen);
                }

                if (strStatus.equals("ASYNC")) {
                    status.setBackgroundResource(R.drawable.statusred);
                } else {
                    status.setBackgroundResource(R.drawable.statusgrren);
                }


                addpay.setBackgroundResource(R.drawable.rupeesgreen);


                R_key.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent i = new Intent(getActivity().getApplicationContext(), Details.class);
                        i.putExtra("ReportToken", getAlldataList.get(position).get("Rtoken"));
                        i.putExtra("ReportKey", getAlldataList.get(position).get("rkey"));
                        i.putExtra("Uname", getAlldataList.get(position).get("name"));
                        i.putExtra("Umail", getAlldataList.get(position).get("mail"));
                        i.putExtra("Ucontact", getAlldataList.get(position).get("contact"));
                        i.putExtra("PropertyAddress", getAlldataList.get(position).get("padd"));
                        i.putExtra("OwnerName", getAlldataList.get(position).get("oname"));
                        i.putExtra("OwnerContact", getAlldataList.get(position).get("ocontact"));
                        i.putExtra("OwnerEmail", getAlldataList.get(position).get("omail"));
                        i.putExtra("OwnerAddress", getAlldataList.get(position).get("oadd"));
                        i.putExtra("TenantName", getAlldataList.get(position).get("tname"));
                        i.putExtra("TenantContact", getAlldataList.get(position).get("tcontact"));
                        i.putExtra("TenantEmail", getAlldataList.get(position).get("tmail"));
                        i.putExtra("TenantAddress", getAlldataList.get(position).get("tadd"));
                        i.putExtra("AppointmentAddress", getAlldataList.get(position).get("appadress"));
                        i.putExtra("StartDate", getAlldataList.get(position).get("sdate"));
                        i.putExtra("StartTime1", getAlldataList.get(position).get("stime1"));
                        i.putExtra("StartTime2", getAlldataList.get(position).get("stime2"));
                        i.putExtra("apptype", getAlldataList.get(position).get("apptype"));
                        i.putExtra("docid", getAlldataList.get(position).get("docid"));
                        i.putExtra("landmark", getAlldataList.get(position).get("landmark"));
                        i.putExtra("contactname", getAlldataList.get(position).get("contact_name"));
                        i.putExtra("contactemail", getAlldataList.get(position).get("contact_email"));
                        i.putExtra("contactno", getAlldataList.get(position).get("contact_no"));
                        i.putExtra("appoint", getAlldataList.get(position).get("appid"));

                        startActivity(i);
                    }
                });

                comment.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getActivity().getApplicationContext(), CommentInfo.class);
                        intent.putExtra("DocumentId", getAlldataList.get(position).get("docid"));
                        intent.putExtra("Position", position);
                        intent.putExtra("ReportKey", getAlldataList.get(position).get("rkey"));
                        startActivityForResult(intent, 12345);
                    }

                });

                status.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        System.out.println("typenew"+getAlldataList.get(position).get("apptype"));
                        if (getAlldataList.get(position).get("apptype").equals("2")) {
//

                            Intent i1 = new Intent(getActivity().getApplicationContext(), newstatusinfo.class);
//                            System.out.println("document_idnew" + getAlldataList.get(position).get("docid"));
                            i1.putExtra("document_id", getAlldataList.get(position).get("docid"));
                            i1.putExtra("appointment_id", getAlldataList.get(position).get("appid"));
                            i1.putExtra("rkey", getAlldataList.get(position).get("rkey"));
                            i1.putExtra("post_status", getAlldataList.get(position).get("post_status"));
                            startActivityForResult(i1, 12345);
                        } else {
                            Intent intent = new Intent(getActivity().getApplicationContext(), StatusInfo.class);
                            intent.putExtra("ReportKey", getAlldataList.get(position).get("rkey"));
                            intent.putExtra("DocumentId", getAlldataList.get(position).get("docid"));
                            intent.putExtra("BiometricComp", getAlldataList.get(position).get("biocomp"));
                            intent.putExtra("BiometricComp1", getAlldataList.get(position).get("biocom1"));
                            intent.putExtra("Reg_From_Comp", getAlldataList.get(position).get("regfromcomp"));
                            intent.putExtra("Witness", getAlldataList.get(position).get("witness"));
                            intent.putExtra("appointment_id", getAlldataList.get(position).get("appid"));
                            intent.putExtra("post_status", getAlldataList.get(position).get("post_status"));
                            startActivityForResult(intent, 12345);

                        }

                    }

                });
                acvr.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getActivity().getApplicationContext(), ACVReport.class);
                        intent.putExtra("ReportKey", getAlldataList.get(position).get("rkey"));
                        intent.putExtra("DocumentId", getAlldataList.get(position).get("docid"));
                        intent.putExtra("AppointmentAddress", getAlldataList.get(position).get("appadress"));
                        intent.putExtra("AppointmentId", getAlldataList.get(position).get("appid"));
                        intent.putExtra("Distance", getAlldataList.get(position).get("distance"));
                        intent.putExtra("Amount", getAlldataList.get(position).get("amount"));
                        intent.putExtra("TransportType", getAlldataList.get(position).get("trasporttype"));
                        intent.putExtra("ApointmentFor", getAlldataList.get(position).get("apointmentfor"));
                        intent.putExtra("Executionerid", getAlldataList.get(position).get("exid"));


                        startActivityForResult(intent, 12345);
                    }
                });


                map.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isLocationTurnedOn()) {
                            alertForLocationSettingDisabled();
                        } else {
                            if (getAlldataList.get(position).get("strlati").equals("") && getAlldataList.get(position).get("strlongi").equals("")) {
                                Toast.makeText(getActivity(), "Customer Location Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                                intent.putExtra("CustomerLatitude", getAlldataList.get(position).get("strlati"));
                                intent.putExtra("CustomerLatlong", getAlldataList.get(position).get("strlongi"));
                                startActivity(intent);
                            }
                        }
                    }


                });


                addpay.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(getActivity().getApplicationContext(), Pay.class);
                        intent1.putExtra("DocumentId", getAlldataList.get(position).get("docid"));
                        intent1.putExtra("ReportKey", getAlldataList.get(position).get("rkey"));
//                        intent1.putExtra("Amount",)
                        startActivityForResult(intent1, 12345);
                    }
                });

                schedule.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(getActivity().getApplicationContext(), reschedule.class);
                        intent1.putExtra("DocumentId", getAlldataList.get(position).get("docid"));
                        intent1.putExtra("AppointmentId", getAlldataList.get(position).get("appid"));
//                        System.out.println("appointmentid:"+getAlldataList.get(position).get("appid"));
                        startActivityForResult(intent1, 12345);
                    }
                });
                appoint.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity().getApplicationContext(), appointmentoption.class);
                        i.putExtra("DocumentId", getAlldataList.get(position).get("docid"));
                        i.putExtra("apptype", getAlldataList.get(position).get("apptype"));
                        i.putExtra("content", content1);
                        i.putExtra("ReportKey", getAlldataList.get(position).get("rkey"));
                        i.putExtra("AppointmentId", getAlldataList.get(position).get("appid"));
//                        System.out.println("doc:"+getAlldataList.get(position).get("docid")+"type:"+getAlldataList.get(position).get("apptype"));

                        startActivity(i);
                    }
                });

                time.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final LayoutInflater inflater = LayoutInflater.from(getContext());
                        View popupView = inflater.inflate(R.layout.popup_layout, null);

                        final PopupWindow popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


                        TextView tv = (TextView) popupView.findViewById(R.id.textView7);
                        TextView tv1 = (TextView) popupView.findViewById(R.id.textView8);

                        final TextView e2 = (TextView) popupView.findViewById(R.id.editTime1);
                        final TextView e3 = (TextView) popupView.findViewById(R.id.editTime2);
                        Button b1 = (Button) popupView.findViewById(R.id.buttontime1);
                        Button b2 = (Button) popupView.findViewById(R.id.buttontime2);
                        Button tv2 = (Button) popupView.findViewById(R.id.text8);
                        Button tv3 = (Button) popupView.findViewById(R.id.text7);
                        b1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mcurrenttime = Calendar.getInstance();

                                TimePickerDialog mTimePicker;
                                mTimePicker = new TimePickerDialog(CompletedDetails.this.getContext(), new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                        mcurrenttime.set(Calendar.HOUR_OF_DAY, selectedHour);
                                        mcurrenttime.set(Calendar.MINUTE, selectedMinute);
                                        timePicker.setIs24HourView(true);
                                        e2.setText(selectedHour + ":" + selectedMinute);
                                        timevalue1 = e2.getText().toString();
                                        //System.out.println("time:" + timevalue1);
                                    }
                                }, mcurrenttime.get(Calendar.HOUR_OF_DAY), mcurrenttime.get(Calendar.MINUTE), false);//Yes 24 hour time


                                mTimePicker.show();
                            }
                        });

                        tv2.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(timevalue1.length()>0) {
                                    SQLiteDatabase sqldb1 = db.getWritableDatabase();
                                    ContentValues values1 = new ContentValues();
                                    values1.put(DBManager.TableInfo.KEYID, ID1);
                                    values1.put(DBManager.TableInfo.AppointmentId,getAlldataList.get(position).get("appid"));
                                    values1.put(DBManager.TableInfo.DocumentId,getAlldataList.get(position).get("docid"));
                                    values1.put(DBManager.TableInfo.actual_time,timevalue1);
                                    values1.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                                    String conditionw = DBManager.TableInfo.AppointmentId + " =?";
                                    Cursor cursorw = sqldb1.query(ACTUAL_TIME, null, conditionw, new String[]{DBManager.TableInfo.actual_time}, null, null, null);
                                    sqldb1.insert(ACTUAL_TIME, null, values1);
                                    System.out.println("time:"+timevalue1+getAlldataList.get(position).get("docid"));
                                    cursorw.close();
                                    sqldb1.close();
                                    reFreshReload();

                                    Intent intent = new Intent(getActivity().getApplicationContext(), fetchactualtime.class);
                                    getActivity().getApplicationContext().startService(intent);
                                }
                                popupWindow.dismiss();
                            }

                        });

                        tv3.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                            }
                        });

                        popupWindow.setFocusable(true);

                        // If you need the PopupWindow to dismiss when when touched outside
                        popupWindow.setBackgroundDrawable(new ColorDrawable());

                        int location[] = new int[2];

                        // Get the View's(the one that was clicked in the Fragment) location
                        v.getLocationOnScreen(location);

                        // Using location, the PopupWindow will be displayed right under anchorView
                        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
                                location[0], location[1] + v.getHeight());

                    }
//
//

                });

                return v;
            }
        };

        tasklist.setAdapter(adpt);

    }

    private boolean checkreportkeystatus(int position) {
        boolean commentFlag = true;
        // db = new DBOperation(getActivity());

        String doc_id = getAlldataList.get(position).get("docid");

        paymentlist = db.getPaymentReport1(db);
        for (int i = 0; i < paymentlist.size(); i++) {
//            System.out.println(getAlldataList.get(position).get("rkey")+" "+doc_id+" "+paymentlist.get(i).get("docid"));
            if (doc_id.equals(paymentlist.get(i).get("docid"))) {

//                System.out.println("green color id"+paymentlist.get(i).get("docid"));
                commentFlag = false;

            }


        }


        return commentFlag;
    }

    private boolean CheckCommentStatus(int position) {
        boolean commentFlag = true;

        String doc_id = getAlldataList.get(position).get("rkey");
        if (documentidcomment.contains(doc_id)) {

            ArrayList<Integer> positionlistcomments = new ArrayList<>();

            for (int i = 0; i < documentidcomment.size(); i++) {
                String temp1 = documentidcomment.get(i);

                if (doc_id != null && temp1.equalsIgnoreCase(doc_id)) {
                    positionlistcomments.add(i);
                }

            }

            for (int j = 0; j < positionlistcomments.size(); j++) {

                String strComment = getCommentDatalist.get(positionlistcomments.get(j)).get("CommentStatus");
                if (strComment.equals(GenericMethods.AsyncStatus)) {
                    commentFlag = false;
                    break;
                }
            }

        }
        return commentFlag;
    }

    private void alertForLocationSettingDisabled() {
        try {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.create();
            alert.setTitle("Alert")
                    .setCancelable(false)
                    .setMessage("Please Enable Location Setting ")
                    .setPositiveButton("Go To Setting",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    NavigateToLocationSetting();
                                }
                            });
            alert.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void NavigateToLocationSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private boolean isLocationTurnedOn() {
        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled || isNetworkEnabled) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void viewDetails() {
        // TODO Auto-generated method stub
        db = new DBOperation(getActivity());
        reportkey.clear();
        documentidcomment.clear();
        documentidpay.clear();

        getCommentDatalist = db.getCommentlist(db);

        getAlldataList.clear();

        if (GenericMethods.value.equals("true")) {
//            System.out.println("from true if");
            getAlldataList = db.getAllList1(db);
            // System.out.println("from true if");
            for (int i = 0; i < db.getAllList1(db).size(); i++) {


                reportkey.add(db.getAllList1(db).get(i).get("rkey"));
                //System.out.println("rkey"+getAlldataList.get(i).get("rkey"));

//                }
            }
        } else if (GenericMethods.value.equals("false")) {
            getAlldataList = db.getAllList(db);

//            System.out.println("from false if");
            for (int i = 0; i < getAlldataList.size(); i++) {

                reportkey.add(getAlldataList.get(i).get("rkey"));

            }
        }
        for (int i = 0; i < getCommentDatalist.size(); i++) {
            documentidcomment.add(getCommentDatalist.get(i).get("Did"));
        }

    }


    @Override
    public void onSuccess(Location loc) {
        currentLat = loc.getLatitude();
        currentLong = loc.getLongitude();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void saveOldLocation(Location loc) {

    }


    public void onResume() {
        super.onResume();
        thiscompleteDetails = this;
    }

    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        thiscompleteDetails = null;
    }

    public void reFreshReload() {
        viewDetails();
        createAdpt();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        reFreshReload();
    }
}
