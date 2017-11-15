package anulom.executioner5.com3.anulom.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.NextActivity;
import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.fragment.CompletedDetails;
import anulom.executioner5.com3.anulom.fragment.NewDetails;
import anulom.executioner5.com3.anulom.fragment.OlderDetails;
import anulom.executioner5.com3.anulom.fragment.TodayDetails;



import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.ATTENDEES;


import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.PARTIES1;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.PAYMENT;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.TABLE_TASK;
import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Admin on 8/22/2016.
 */
public class GetAllDataList extends Service {
    String startnewdate, startnewtime1, startnewtime2, commentdate;
    ArrayList<String> ownername = new ArrayList<>();
    ArrayList<String> ownercontact = new ArrayList<>();
    ArrayList<String> documentid = new ArrayList<>();
    ArrayList<String> appointmentaddress = new ArrayList<>();
    ArrayList<String> executionerid = new ArrayList<>();
    ArrayList<String> appointmentid = new ArrayList<>();
    ArrayList<String> commentid = new ArrayList<>();
    ArrayList<String> Addpayid = new ArrayList<>();
    ArrayList<String> userlist = new ArrayList<>();
    ArrayList<HashMap<String, String>> getCommentDatalist = null;
    ArrayList<HashMap<String, String>> getPaymentDataList = null;
    ArrayList<HashMap<String, String>> getAlldataList = null;
    ArrayList<HashMap<String, String>> getuserdataList = null;
    JSONArray ja = null;
    JSONArray ja3 = null;
    JSONArray ja4 = null;
    JSONArray ja5 = null;
    JSONArray ja9 = null;
    JSONArray ja10 = null;
    JSONArray ja11 = null;
    GenericMethods response, response2;
    String APPOINTMENTURL = "", WITNESSURL = "";
    String ID1;

    DBOperation db;

    private SharedPreferences usermail;
    private String username2 = "", password2 = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        db = new DBOperation(getApplicationContext());


        response = new GenericMethods();
        response2 = new GenericMethods();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            usermail = PreferenceManager.getDefaultSharedPreferences(this);
            username2 = usermail.getString("username", "");
            password2 = usermail.getString("password", "");
            APPOINTMENTURL = "http://52.33.203.208:3000/api/v2/get_exec_data?get_auth_token=DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7&exec_email=" + username2 + "&password=" + password2 + "&version=16.4.4";
            WITNESSURL="http://52.33.203.208/cmt/api/v1/comment_data/get_task?get_auth_token=DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7&user="+username2;

            arraylistclear();
            db = new DBOperation(getApplicationContext());
            new GetContacts().execute();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void arraylistclear() {
        ownername.clear();
        ownercontact.clear();
        documentid.clear();
        appointmentaddress.clear();
        executionerid.clear();
        appointmentid.clear();
        commentid.clear();
        Addpayid.clear();
        userlist.clear();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            String jsonStr = "";
            String jsonStr2 = "";



            try {
                System.out.println("Request");
                jsonStr = response.doGetRequest(APPOINTMENTURL);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null || !jsonStr.trim().isEmpty()) {
                try {
                    JSONObject jsonObject = null;

                    jsonObject = new JSONObject(jsonStr);

                    String jStrResult = jsonObject.getString("status");

                    if (jStrResult.equals("OK")) {
                        db.delLocationDetails(db);
                        db.delAppointment(db);
                        db.deluser(db);
                        db.delDocument(db);
                        db.delComments(db);
                        db.delUserRole(db);
                        SQLiteDatabase base1 = db.getWritableDatabase();
                        base1.delete(ATTENDEES, null, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            getAlldataList = db.getAllData(db);
            System.out.println("Get size " + getAlldataList.size());
            for (int i = 0; i < getAlldataList.size(); i++) {
                documentid.add(getAlldataList.get(i).get("Did"));
                appointmentid.add(getAlldataList.get(i).get("Aid"));
                ownername.add(getAlldataList.get(i).get("Oname"));
                executionerid.add(getAlldataList.get(i).get("Exid"));
            }
            getCommentDatalist = db.getCommentlist(db);
            for (int i = 0; i < getCommentDatalist.size(); i++) {
                commentid.add(getCommentDatalist.get(i).get("Cid"));
            }

            getuserdataList = db.getdataList(db);
            for (int i = 0; i < getuserdataList.size(); i++) {
                userlist.add(getuserdataList.get(i).get("UserId"));
            }

            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    ja = jsonObj.getJSONArray(GenericMethods.TAG_NAME);
//                    ja1 = jsonObj.getJSONArray(GenericMethods.TAG_NAME1);
                    // ja2 = jsonObj.getJSONArray(GenericMethods.TAG_NAME2);
                    ja3 = jsonObj.getJSONArray(GenericMethods.TAG_NAME3);
                    ja4 = jsonObj.getJSONArray(GenericMethods.TAG_NAME4);
                   // System.out.println("Array list "+ja.length());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject c = ja.getJSONObject(i);
                        String rtoken1 = c.getString(GenericMethods.TAG_RTOKEN);
                        String rkey1 = c.getString(GenericMethods.TAG_REPORT_KEY);
                        String uname1 = c.getString(GenericMethods.TAG_UNAME);
                        String email1 = c.getString(GenericMethods.TAG_UEMAIL);
                        String contact_number1 = c.getString(GenericMethods.TAG_UCONTACT);
                        String paddress1 = c.getString(GenericMethods.TAG_PADDRESS);
                        String oname1 = c.getString(GenericMethods.TAG_ONAME);
                        String ocontact1 = c.getString(GenericMethods.TAG_OCONTACT);
                        String oemail1 = c.getString(GenericMethods.TAG_OEMAIL);
                        String oaddress1 = c.getString(GenericMethods.TAG_OADDRESS);
                        String tname1 = c.getString(GenericMethods.TAG_TNAME);
                        String tcontact1 = c.getString(GenericMethods.TAG_TCONTACT);
                        String temail1 = c.getString(GenericMethods.TAG_TEMAIL);
                        String taddress1 = c.getString(GenericMethods.TAG_TADDRESS);
                        String status1 = c.getString(GenericMethods.TAG_STATUS);
                        String appdate = c.getString(GenericMethods.TAG_APPDATE);
                        String biocomp = c.getString(GenericMethods.TAG_BIO_COMP);
                        String appdate1 = c.getString(GenericMethods.TAG_APPDATE1);
                        String biocomp1 = c.getString(GenericMethods.TAG_BIO_COMP1);
                        String regfromcomp = c.getString(GenericMethods.TAG_REG_FROM_COMP);
                        String witness = c.getString(GenericMethods.TAG_WITNESS);
                        String shipadd = c.getString(GenericMethods.TAG_SHIP_ADDRESS);
                        String shipdiffadd = c.getString(GenericMethods.TAG_SHIP_DIFF_ADDRESS);
                        String strlatitude, strlongitude;
                        if (c.has(GenericMethods.TAG_CUSTOMER_LAT)) {
                            strlatitude = c.getString(GenericMethods.TAG_CUSTOMER_LAT);
                        } else strlatitude = "0";
                        if (c.has(GenericMethods.TAG_CUSTOMER_LONG)) {
                            strlongitude = c.getString(GenericMethods.TAG_CUSTOMER_LONG);
                        } else strlongitude = "0";
                        String docid = c.getString(GenericMethods.TAG_DOCID);

                        String appid = c.getString(GenericMethods.TAG_APPID);
                        System.out.println("Initial docid and appid:"+" "+appdate+" "+appdate1);
                        String start1 = c.getString(GenericMethods.TAG_START1);
                        String start2 = c.getString(GenericMethods.TAG_START2);
                        String appaddress = c.getString(GenericMethods.TAG_APPADDRESS);
                        String exeid = c.getString(GenericMethods.TAG_EXECUTIONER_ID);
                        String appfor = c.getString(GenericMethods.TAG_APP_FOR);
                        String apptype = c.getString(GenericMethods.TAG_APPTYPE);
                        String post_status = c.getString(GenericMethods.TAG_POST_STATUS);

                        String landmark = c.getString(GenericMethods.TAG_LANDMARK);
                        String contactperson = c.getString(GenericMethods.TAG_CONTACTPERSON);

                       // System.out.println("contact:" + contactperson + "landmark:" + landmark);
                        String distance1 = c.getString(GenericMethods.TAG_CUSTOMER_DISTANCE);
                        String amount1 = c.getString(GenericMethods.TAG_CUSTOMER_AMOUNT);
                        String transporttype1 = c.getString(GenericMethods.TAG_CUSTOMER_TRANS_TYPE);

                        JSONObject jsonatten = ja.getJSONObject(i);

                        JSONArray jsonattendees = jsonatten.getJSONArray(GenericMethods.TAG_NAME13);

                        if (jsonattendees != null) {


                            for (int j = 0; j < jsonattendees.length(); j++) {
                                JSONObject atten = jsonattendees.getJSONObject(j);

                                if (atten != null) {
                                    String name = atten.getString(GenericMethods.TAG_NAMEATTEND);
                                    String email = atten.getString(GenericMethods.TAG_EMAILATTEND);
                                    String contact = atten.getString(GenericMethods.TAG_CONTACTATTEND);
                                    //System.out.println("docid:" + i + " " + appid + " " + "name" + name + "email" + email + "contact" + contact);


                                    SQLiteDatabase db13 = db.getWritableDatabase();
                                    ContentValues values3 = new ContentValues();
                                    values3.put(DBManager.TableInfo.KEYID, ID1);
                                    values3.put(DBManager.TableInfo.nameattend, name);
                                    values3.put(DBManager.TableInfo.emailattend, email);
                                    values3.put(DBManager.TableInfo.contactattend, contact);
                                    values3.put(DBManager.TableInfo.appointattend, appid);
                                    values3.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                                    String condition3 = DBManager.TableInfo.nameattend + " =?";
                                    Cursor cursor3 = db13.query(ATTENDEES, null, condition3, new String[]{DBManager.TableInfo.emailattend}, null, null, null);
                                    long status3 = db13.insert(ATTENDEES, null, values3);
                                    //Log.d(TAG, "attendees insert : " + status3);
                                   // System.out.println("Success" + username2 + appid);
                                    cursor3.close();
                                    db13.close();


                                }

                            }

                        }


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat output1 = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat output2 = new SimpleDateFormat("HH:mm");

                        java.util.Date date, time1, time2;
                        try {
                            date = sdf.parse(start1);
                            time1 = sdf.parse(start1);
                            time2 = sdf.parse(start2);
                            startnewdate = output.format(date);
                            startnewtime1 = output1.format(time1);
                            startnewtime2 = output2.format(time2);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (documentid.contains(docid)) {
                            db.Update(db, docid, rtoken1, rkey1, uname1, email1, contact_number1, paddress1, oname1,
                                    ocontact1, oemail1, oaddress1, tname1, tcontact1, temail1, taddress1, status1,
                                    appdate, biocomp, appdate1, biocomp1, regfromcomp, witness, shipadd, shipdiffadd, strlatitude, strlongitude, apptype, landmark, contactperson, post_status);
                            System.out.println("Updated Document'''''''''''''''''''" + appid + " "+docid);
                        } else {
                            if (!db.DocumentID(db, docid)) {
                                System.out.println("docidalllist" +documentid +docid);
                                db.InsertRecord(db, rtoken1, rkey1, uname1, email1, contact_number1, paddress1, oname1,
                                        ocontact1, oemail1, oaddress1, tname1, tcontact1, temail1, taddress1, status1,
                                        username2, docid, appdate, biocomp, appdate1, biocomp1, regfromcomp, witness, shipadd,
                                        shipdiffadd, strlatitude, strlongitude, "SYNC", apptype, landmark, contactperson, post_status);
                                System.out.println("docid:" + docid);
                            }
                        }

                        if (appointmentid.contains(appid)) {
//
                            db.Updateacvr(db, appid, docid, startnewdate, startnewtime1, startnewtime2, appaddress, exeid,
                                    appfor, distance1, amount1, transporttype1);
//                            distance1,amount1,transporttype1
                            System.out.println("Updated Appointment'''''''''''''''''''" + appid+" "+ docid);

                        } else {
                            System.out.println("Appidalllist" +appointmentid +appid +" "+exeid);
                            db.InsertRecord2(db, docid, appid, startnewdate, startnewtime1, startnewtime2, appaddress,
                                    exeid, appfor, distance1, amount1, transporttype1, "SYNC");
                        }
                    }


//                    SQLiteDatabase ret = db.getReadableDatabase();
//                    String query = "select * from  " + ATTENDEES + " where " + DBManager.TableInfo.KEY_LOGIN_USER + " = '" + GenericMethods.email + "'";
//                    Cursor cu = ret.rawQuery(query, null);
//                    while (cu.moveToNext()) {
//
//                        System.out.println("name attendees:" + cu.getString(cu.getColumnIndex(DBManager.TableInfo.nameattend)));
//                        System.out.println("email attendees:" + cu.getString(cu.getColumnIndex(DBManager.TableInfo.emailattend)));
//                        System.out.println("contact attendees:" + cu.getString(cu.getColumnIndex(DBManager.TableInfo.contactattend)));
//                        System.out.println("login user:" + cu.getString(cu.getColumnIndex(DBManager.TableInfo.KEY_LOGIN_USER)));
//
//                    }
//                    cu.close();
//                    ret.close();


                    for (int l = 0; l < ja3.length(); l++) {
                        JSONObject c = ja3.getJSONObject(l);
                        String userid = c.getString(GenericMethods.TAG_USERID);
                        String username = c.getString(GenericMethods.TAG_USERNAME);
                        String email = c.getString(GenericMethods.TAG_EMAIL);
                        String role = c.getString(GenericMethods.TAG_ROLE);
                        String platformid = c.getString(GenericMethods.TAG_PLATFORMID);
                        String roleid = c.getString(GenericMethods.TAG_ROLEID);
                        String idu = c.getString(GenericMethods.TAG_IDu);

                        if (userlist.contains(userid)) {
                            db.Updateuser(db, userid, username, email, role, platformid, roleid, idu);

//                            System.out.println("Updated doc_________________" + userid);
                        } else {
                            if (!db.checkUserId(db, userid)) {
                                db.InsertRecord5(db, userid, username, email, role, platformid, roleid, idu);
                            }
                        }
                    }

                    SQLiteDatabase base = db.getWritableDatabase();
                    base.delete(PAYMENT, null, null);

                    for (int n = 0; n < ja4.length(); n++) {
                        JSONObject c = ja4.getJSONObject(n);
                        String DocumentId = c.getString(GenericMethods.TAG_DOCUMENTID);
                        String Amt = c.getString(GenericMethods.TAG_PAYAMOUNT);
                        String ReportKey = c.getString(GenericMethods.TAG_REPORTKEY);
//

                        SQLiteDatabase db3 = db.getWritableDatabase();
                        ContentValues values3 = new ContentValues();
                        values3.put(DBManager.TableInfo.KEYID, ID1);
                        values3.put(DBManager.TableInfo.doc1, DocumentId);
                        values3.put(DBManager.TableInfo.rep1, ReportKey);
                        values3.put(DBManager.TableInfo.payamount, Amt);
                        values3.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                        String condition3 = DBManager.TableInfo.doc1 + " =?";
                        Cursor cursor3 = db3.query(PAYMENT, null, condition3, new String[]{DBManager.TableInfo.rep1}, null, null, null);
                        long status3 = db3.insert(PAYMENT, null, values3);
                        Log.d(TAG, "Payment insert : " + status3);
//                                System.out.println("Success" + username2);
                        cursor3.close();
                        db3.close();


                    }


//new API
                    SQLiteDatabase base3 = db.getWritableDatabase();
                    base3.delete(PARTIES1, null, null);
                    System.out.println("Parties DB Deleted");

                    ja5 = jsonObj.getJSONArray(GenericMethods.TAG_NAME5);
                    for (int n = 0; n < ja5.length(); n++) {
                        JSONObject c = ja5.getJSONObject(n);
                        String Document1 = c.getString(GenericMethods.TAG_DOCUMENT);
                        String Attendance = c.getString(GenericMethods.TAG_ATTENDANCE);
                        String name = c.getString(GenericMethods.TAG_NAMENEW);
                        String email = c.getString(GenericMethods.TAG_EMAILNEW);
                        String partytype = c.getString(GenericMethods.TAG_PARTYTYPE);
                        String poa = c.getString(GenericMethods.TAG_POA);
                        String contact = c.getString(GenericMethods.TAG_CONTACTNUMBER);
                        String biometric = c.getString(GenericMethods.TAG_BIOMETRIC);
//
//                                System.out.println("biometric_party:" + c.getString(GenericMethods.TAG_BIOMETRIC));
//                                System.out.println("contact:" + c.getString(GenericMethods.TAG_CONTACTNUMBER));

                        SQLiteDatabase db4 = db.getWritableDatabase();
                        ContentValues values4 = new ContentValues();
                        values4.put(DBManager.TableInfo.KEYID, ID1);
                        values4.put(DBManager.TableInfo.DOCUMENT, Document1);
                        values4.put(DBManager.TableInfo.ATTENDANCE, Attendance);
                        values4.put(DBManager.TableInfo.NAMENEW, name);
                        values4.put(DBManager.TableInfo.EMAILNEW, email);
                        values4.put(DBManager.TableInfo.PARTYTYPE, partytype);
                        values4.put(DBManager.TableInfo.POA, poa);
                        values4.put(DBManager.TableInfo.CONTACT, contact);
                        values4.put(DBManager.TableInfo.BIOMETRIC, biometric);
                        values4.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                        String condition4 = DBManager.TableInfo.DOCUMENT + " =?";
                        Cursor cursor4 = db4.query(PARTIES1, null, condition4, new String[]{DBManager.TableInfo.ATTENDANCE}, null, null, null);
                        long status4 = db4.insert(PARTIES1, null, values4);
                        Log.d(TAG, "Parties insert : " + status4);
                        System.out.println("Success" + username2);
                        cursor4.close();
                        db4.close();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    jsonStr2 = response2.doGetRequest(WITNESSURL);

                } catch (IOException e) {
                    e.printStackTrace();
                }

////////adhoc & witness
                Log.d("Response2: ", "> " + jsonStr2);

                SQLiteDatabase base1 = db.getWritableDatabase();
                base1.delete(TABLE_TASK, null, null);

                if (jsonStr2 != null) {

                    try {

                        JSONObject jsonObj1 = new JSONObject(jsonStr2);
                        ja9 = jsonObj1.getJSONArray(GenericMethods.TAG_NAME9);
                        for (int n = 0; n < ja9.length(); n++) {
                            JSONObject c = ja9.getJSONObject(n);
                            String witnessid = c.getString(GenericMethods.TAG_ID);
                            String comment = c.getString(GenericMethods.TAG_COMMENT);
                            String isdone = c.getString(GenericMethods.TAG_IS_DONE);
                            String doc = c.getString(GenericMethods.TAG_DOC);
                            String create = c.getString(GenericMethods.TAG_CREATE);
                            String remainder = c.getString(GenericMethods.TAG_REMAINDER);
                            String assign = c.getString(GenericMethods.TAG_ASSIGN);
                            String taskid = c.getString(GenericMethods.TAG_TASK_ID);



//////db for adhoc
                            SQLiteDatabase adhoc = db.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(DBManager.TableInfo.KEYID, ID1);
                            values.put(DBManager.TableInfo.WITID, witnessid);
                            values.put(DBManager.TableInfo.COMMENT, comment);
                            values.put(DBManager.TableInfo.IS_DONE, isdone);
                            values.put(DBManager.TableInfo.DOC, doc);
                            values.put(DBManager.TableInfo.CREATE, create);
                            values.put(DBManager.TableInfo.ADHOC,"Adhoc");
                            values.put(DBManager.TableInfo.status1,"Not Completed");
                            values.put(DBManager.TableInfo.REMAINDER, remainder);
                            values.put(DBManager.TableInfo.ASSIGN, assign);
                            values.put(DBManager.TableInfo.TASK_ID, taskid);
                            values.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                            String condition = DBManager.TableInfo.DOC + " =?";
                            Cursor cursor = adhoc.query(TABLE_TASK, null, condition, new String[]{DBManager.TableInfo.TASK_ID}, null, null, null);
                            long status = adhoc.insert(TABLE_TASK, null, values);
                            Log.d(TAG, "Adhoc insert : " + status);
                            System.out.println("Success" + username2);
                            cursor.close();
                            adhoc.close();

                        }
//
//                        //witness

                        ja10 = jsonObj1.getJSONArray(GenericMethods.TAG_NAME10);
                        for (int n = 0; n < ja10.length(); n++) {
                            JSONObject c = ja10.getJSONObject(n);
                            String witnessid = c.getString(GenericMethods.TAG_ID);
                            String comment = c.getString(GenericMethods.TAG_COMMENT);
                            String isdone = c.getString(GenericMethods.TAG_IS_DONE);
                            String doc = c.getString(GenericMethods.TAG_DOC);
                            String create = c.getString(GenericMethods.TAG_CREATE);
                            String remainder = c.getString(GenericMethods.TAG_REMAINDER);
                            String assign = c.getString(GenericMethods.TAG_ASSIGN);
                            String taskid = c.getString(GenericMethods.TAG_TASK_ID);

                            SQLiteDatabase witn = db.getWritableDatabase();
                            ContentValues valuesw = new ContentValues();
                            valuesw.put(DBManager.TableInfo.KEYID, ID1);
                            valuesw.put(DBManager.TableInfo.WITID, witnessid);
                            valuesw.put(DBManager.TableInfo.COMMENT, comment);
                            valuesw.put(DBManager.TableInfo.IS_DONE, isdone);
                            valuesw.put(DBManager.TableInfo.DOC, doc);
                            valuesw.put(DBManager.TableInfo.CREATE, create);
                            valuesw.put(DBManager.TableInfo.WITNESS,"Witness");
                            valuesw.put(DBManager.TableInfo.status1,"Not Completed");
                            valuesw.put(DBManager.TableInfo.REMAINDER, remainder);
                            valuesw.put(DBManager.TableInfo.ASSIGN, assign);
                            valuesw.put(DBManager.TableInfo.TASK_ID, taskid);
                            valuesw.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                            String conditionw = DBManager.TableInfo.DOC + " =?";
                            Cursor cursorw = witn.query(TABLE_TASK, null, conditionw, new String[]{DBManager.TableInfo.TASK_ID}, null, null, null);
                            long statusw = witn.insert(TABLE_TASK, null, valuesw);
                            Log.d(TAG, "Witness insert : " + statusw);
                            System.out.println("Success" + username2);
                            cursorw.close();
                            witn.close();
                        }
////complaint




                        ja11 = jsonObj1.getJSONArray(GenericMethods.TAG_NAME11);
                        for (int n = 0; n < ja11.length(); n++) {
                            JSONObject c = ja11.getJSONObject(n);
                            String witnessid = c.getString(GenericMethods.TAG_ID);
                            String comment = c.getString(GenericMethods.TAG_COMMENT);
                            String isdone = c.getString(GenericMethods.TAG_IS_DONE);
                            String doc = c.getString(GenericMethods.TAG_DOC);
                            String create = c.getString(GenericMethods.TAG_CREATE);
                            String remainder = c.getString(GenericMethods.TAG_REMAINDER);
                            String assign = c.getString(GenericMethods.TAG_ASSIGN);
                            String taskid = c.getString(GenericMethods.TAG_TASK_ID);

                            SQLiteDatabase complaint = db.getWritableDatabase();
                            ContentValues valuesw = new ContentValues();
                            valuesw.put(DBManager.TableInfo.KEYID, ID1);
                            valuesw.put(DBManager.TableInfo.WITID, witnessid);
                            valuesw.put(DBManager.TableInfo.COMMENT, comment);
                            valuesw.put(DBManager.TableInfo.IS_DONE, isdone);
                            valuesw.put(DBManager.TableInfo.DOC, doc);
                            valuesw.put(DBManager.TableInfo.CREATE, create);
                            valuesw.put(DBManager.TableInfo.COMPLAINT,"Complaint");
                            valuesw.put(DBManager.TableInfo.status1,"Not Completed");
                            valuesw.put(DBManager.TableInfo.REMAINDER, remainder);
                            valuesw.put(DBManager.TableInfo.ASSIGN, assign);
                            valuesw.put(DBManager.TableInfo.TASK_ID, taskid);
                            valuesw.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                            String conditionw = DBManager.TableInfo.DOC + " =?";
                            Cursor cursorw = complaint.query(TABLE_TASK, null, conditionw, new String[]{DBManager.TableInfo.TASK_ID}, null, null, null);
                            long statusw = complaint.insert(TABLE_TASK, null, valuesw);
                            Log.d(TAG, "Complaint insert : " + statusw);
                            //System.out.println("Success" + username2);
                            cursorw.close();
                            complaint.close();
//
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (TodayDetails.thisToday != null) {
                TodayDetails.thisToday.reFreshReload();
            }
            if (OlderDetails.thisOlderDetails != null) {
                OlderDetails.thisOlderDetails.reFreshReload();
            }
            if (NewDetails.thisnewDetails != null) {
                NewDetails.thisnewDetails.reFreshReload();
            }
            if (CompletedDetails.thiscompleteDetails != null) {
                CompletedDetails.thiscompleteDetails.reFreshReload();
            }
            cancelProgressBar();

        }
    }

    private void deleteAllTable() {
        DBOperation db = new DBOperation(getBaseContext());

    }

    private void cancelProgressBar() {
        try {
            if (NextActivity.thisnext != null) {
                NextActivity.thisnext.closeProgressBar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
