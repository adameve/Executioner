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
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.APPOINTMENTBOOKING;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.POSTDOC;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.POST_TASK;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.TABLE_TASK;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.UPDATEPARTY;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.address;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.app_date;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.attendees;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.attendeescontact;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.attendeesemail;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.division_id;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.free;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.free_reason;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.landmark;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.region_id;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.request_no;

public class Posttask extends Service {
    private SharedPreferences sharedPreferences;
    DBOperation db;

    public String umail = Login.umailid;
    GenericMethods mResponse;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF8");
    String username3, status = "1",exec_email,taskid,isdone,reason,stat;
    int count = 0;
    private String username2 = "";
    JSONArray appointment = new JSONArray();
String value="";
    // ArrayList<HashMap<String, String>> party = new ArrayList<HashMap<String, String>>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        value=intent.getStringExtra("Value");
        stat=intent.getStringExtra("status");
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
            ArrayList<HashMap<String, String>>posttaskdetails = db.getposttaskdetails(db);


            try {

for (int i = 0; i < posttaskdetails.size(); i++) {

    exec_email = posttaskdetails.get(i).get("exec_email");
    taskid = posttaskdetails.get(i).get("task_id");
    reason = posttaskdetails.get(i).get("reason");
    isdone = posttaskdetails.get(i).get("is_done");

                }

                postData(taskid,exec_email,isdone,reason);
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

        public void postData(String taskid, String exec_email,String isdone,String reason)
        {

            DBOperation db = new DBOperation(getApplicationContext());
            String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";
            String URL ="http://52.33.203.208/cmt/api/v1/comment_data/task_done";


            try {


                JSONObject app = new JSONObject();
                app.put("task_id", taskid);
                app.put("exec_email", exec_email);
                app.put("is_done",isdone);
                app.put("not_done_reason", reason);


                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);
                JSONObject main = new JSONObject();
                main.put("comment", app);
                main.put("auth_token", tokenjson);
                String json = "";
                json = main.toString();
                System.out.println("jsonfinal:" + json);
                String strResponsePost = mResponse.doPostRequest(URL, json);

                if (!strResponsePost.equals("")) {
                    JSONObject jResult = new JSONObject(strResponsePost);
                    String strStatus = jResult.getString("status");

                    if (strStatus.equals(status)) {

                        SQLiteDatabase sqldb = db.getWritableDatabase();

                        int count = sqldb.delete(POST_TASK, DBManager.TableInfo.TASK_ID + "=?", new String[]{taskid});
                        System.out.println(" DB ROW DELETED SUCCESS:" + count);
                        if (stat.equals("Completed")) {
                            int count1 = sqldb.delete(TABLE_TASK, DBManager.TableInfo.TASK_ID + "=?", new String[]{taskid});
                            System.out.println(" DB ROW DELETED SUCCESS:" + count1);

                        }
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
