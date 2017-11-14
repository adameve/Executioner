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

import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.Login;
import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;

import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.APPOINTMENTSLOT;

/**
 * Created by Admin on 11/17/2016.
 */

public class getappointmentslot extends Service {


    public String umail = Login.umailid;
    GenericMethods responseslot;

    private String APPOINTMENTSLOTURL = "";
    String ID1;
    private SharedPreferences usermail;
    private String username2 = "", password2 = "", app_date = "", divisionid = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {

//        Toast.makeText(this, "called from report", Toast.LENGTH_LONG).show();
        responseslot = new GenericMethods();

        app_date = intent.getStringExtra("app_date");
        divisionid = intent.getStringExtra("division_id");
        APPOINTMENTSLOTURL = "http://52.33.203.208/plus/api/v1/get_time_slot?get_auth_token=DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7&app_date=" + app_date + "&division_id=" + divisionid;
        System.out.println("valuesfrmservice:" + APPOINTMENTSLOTURL);
        new GetCommentTask().execute();

        return START_STICKY;


    }

    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    private class GetCommentTask extends AsyncTask<Void, Void, Void> {
        String jsonStr3 = "";
        private JSONArray ja12 = null;

        @Override
        protected Void doInBackground(Void... params) {
            DBOperation db = new DBOperation(getappointmentslot.this);
            usermail = PreferenceManager.getDefaultSharedPreferences(getappointmentslot.this);
            username2 = usermail.getString("username", "");
            password2 = usermail.getString("password", "");


            try {
                jsonStr3 = responseslot.doGetRequest(APPOINTMENTSLOTURL);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Response from slot: ", "> " + jsonStr3);

            SQLiteDatabase base = db.getWritableDatabase();
            base.delete(APPOINTMENTSLOT, null, null);
            System.out.println("DB Deleted");

            if (jsonStr3 != null)

                try {


                    JSONObject jsonObj1 = new JSONObject(jsonStr3);
                    ja12 = jsonObj1.getJSONArray(GenericMethods.TAG_NAME12);

                    for (int n = 0; n < ja12.length(); n++) {
                        JSONObject c = ja12.getJSONObject(n);
                        String slotid = c.getString(GenericMethods.TAG_SLOTID);
                        String starttime = c.getString(GenericMethods.TAG_STARTTIME);
                        String endtime = c.getString(GenericMethods.TAG_ENDTIME);
                        String available = c.getString(GenericMethods.TAG_AVAILABLE);
                        String block = c.getString(GenericMethods.TAG_BLOCK);
                        String discount = c.getString(GenericMethods.TAG_DISCOUNT);

                        System.out.println("slotid:" + c.getString(GenericMethods.TAG_SLOTID));
                        System.out.println("starttime:" + c.getString(GenericMethods.TAG_STARTTIME));
                        System.out.println("endtime:" + c.getString(GenericMethods.TAG_ENDTIME));
                        System.out.println("available:" + c.getString(GenericMethods.TAG_AVAILABLE));
                        System.out.println("block:" + c.getString(GenericMethods.TAG_BLOCK));
                        System.out.println("discount:" + c.getString(GenericMethods.TAG_DISCOUNT));

                        SQLiteDatabase db3 = db.getWritableDatabase();
                        ContentValues values3 = new ContentValues();
                        values3.put(DBManager.TableInfo.KEYID, ID1);
                        values3.put(DBManager.TableInfo.slotid1, slotid);
                        values3.put(DBManager.TableInfo.starttime1, starttime);
                        values3.put(DBManager.TableInfo.endtime1, endtime);
                        values3.put(DBManager.TableInfo.available1, available);
                        values3.put(DBManager.TableInfo.block1, block);
                        values3.put(DBManager.TableInfo.discount1, discount);
                        values3.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                        String condition3 = DBManager.TableInfo.slotid1 + " =?";
                        Cursor cursor3 = db3.query(APPOINTMENTSLOT, null, condition3, new String[]{DBManager.TableInfo.available1}, null, null, null);
                        long status3 = db3.insert(APPOINTMENTSLOT, null, values3);
                        System.out.println("slot insert : " + status3);
//
                        cursor3.close();
                        db3.close();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            GenericMethods.app_value = "true";


            return null;
        }
    }


}
