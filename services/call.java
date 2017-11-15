package anulom.executioner5.com3.anulom.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.Login;
import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.fragment.CompletedDetails;
import anulom.executioner5.com3.anulom.fragment.NewDetails;
import anulom.executioner5.com3.anulom.fragment.OlderDetails;
import anulom.executioner5.com3.anulom.fragment.TodayDetails;

import static anulom.executioner5.com3.anulom.GenericMethods.document;
import static anulom.executioner5.com3.anulom.R.layout.details;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.ACTUAL_TIME;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.APPOINTMENTBOOKING;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.CALL;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.POSTDOC;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.POST_TASK;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.REASSIGN;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.UPDATEPARTY;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.actual_time;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.address;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.app_date;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.attendees;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.attendeescontact;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.attendeesemail;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.division_id;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.free;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.free_reason;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.landmark;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.new_owner;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.reason;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.region_id;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.request_no;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.timenew;

public class call extends Service {
    private SharedPreferences sharedPreferences;
    DBOperation db;
    public SharedPreferences calltime1;

    public String umail = Login.umailid;
    GenericMethods mResponse;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF8");
    String username3, status = "1",exec_email,docid,appid,calltime,actual_time,gen_distance="";
    int count = 0;
    private String username2 = "";
    JSONArray appointment = new JSONArray();
    String value="",taskid="",comment="";
    // ArrayList<HashMap<String, String>> party = new ArrayList<HashMap<String, String>>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {

        mResponse = new GenericMethods();

        if (GenericMethods.isConnected(getApplicationContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new MyAsyncTask()
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {
                new MyAsyncTask().execute();


            }
        }
        return START_STICKY;


    }

    public void onCreate() {


        // TODO Auto-generated method stub
        super.onCreate();


    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {


        @Override
        protected Double doInBackground(String... params) {

            DBOperation db = new DBOperation(getApplicationContext());
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username2 = sharedPreferences.getString("username", "");
            username3 = username2;
            ArrayList<HashMap<String, String>>getcalllist = db.calldetails(db);


            try {

                for (int i = 0; i < getcalllist.size(); i++) {


                    exec_email = getcalllist.get(i).get("exec_email");
                    docid=getcalllist.get(i).get("docid");
                    appid = getcalllist.get(i).get("appid");
                    gen_distance=getcalllist.get(i).get("gen_distance");
                    actual_time=getcalllist.get(i).get("actual_time");
                    calltime=getcalllist.get(i).get("call_time");


                }

                postData(exec_email,docid,appid,calltime,gen_distance,actual_time);
            } catch (Exception e) {

                // TODO Auto-generated catch block
                //     e.printStackTrace();
                // }

            }
            return null;

        }

        protected void onPostExecute(Double result) {
            stopSelf();

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
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        public void postData(String exec_email,String docid,String appid,String calltime,String gen_distance,String actual_time)
        {

            DBOperation db = new DBOperation(getApplicationContext());
            String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";
            String URL ="http://52.33.203.208:3000/api/v2/biometric_data/app_update";


            try {


                JSONObject app = new JSONObject();
                app.put("Executioner", exec_email);
                app.put("Docid",docid);
                app.put("app_id", appid);
                app.put("call_time",calltime);
                app.put("gen_distance","");
                app.put("actual_time","");



                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);
                JSONObject main = new JSONObject();
                main.put("user", app);
                main.put("auth_token", tokenjson);
                String json = "";
                json = main.toString();
                System.out.println("jsonfinal:" + json);
                String strResponsePost = mResponse.doPostRequest(URL, json);

                if (!strResponsePost.equals("")) {
                    JSONObject jResult = new JSONObject(strResponsePost);
                    String strStatus = jResult.getString("status");

                    if (strStatus.equals(status)) {
//
                        SQLiteDatabase sqldb = db.getWritableDatabase();

                        int count = sqldb.delete(CALL, DBManager.TableInfo.call_time + "=?", new String[]{calltime});
                        System.out.println(" DB ROW DELETED SUCCESS:" + count);
//
                    }

                }
                System.out.println("**********Post Completed************");

            } catch (JSONException e) {

                // TODO Auto-generated catch block
                //     e.printStackTrace();
                // }

            } catch (ClientProtocolException e) {

                // TODO Auto-generated catch block
            } catch (IOException e) {

            }
        }


    }


}
