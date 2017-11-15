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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.Login;
import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.fragment.CompletedDetails;
import anulom.executioner5.com3.anulom.fragment.NewDetails;
import anulom.executioner5.com3.anulom.fragment.OlderDetails;
import anulom.executioner5.com3.anulom.fragment.TodayDetails;

import static anulom.executioner5.com3.anulom.GenericMethods.email;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.MULTIWORK;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.UPDATEPAYMENT1;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.date;
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.radiotype;

/**
 * Created by Admin on 7/12/2016.
 */
public class AppointmentstatusPost extends Service {
    private SharedPreferences sharedPreferences;
    DBOperation db;
    String rkey, amount, document, ID1, pay, finalamount, commentvalue, val;
    String option = "";
    public String umail = Login.umailid;
    GenericMethods mResponse;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF8");
    String username3,status1="1";
    private String username2 = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//         Toast.makeText(this, "called from report", Toast.LENGTH_LONG).show();
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
        DBOperation db = new DBOperation(getApplicationContext());

        String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";

        @Override
        protected Double doInBackground(String... params) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username2 = sharedPreferences.getString("username", "");
            username3 = username2;

            ArrayList<HashMap<String, String>> multipartychecklist = db.getmultipartycheck(db);
            System.out.println("appstatuslist size:" + multipartychecklist.size());
            for (int i = 0; i < multipartychecklist.size(); i++) {
                String exec_email = multipartychecklist.get(i).get("exec_email");
                String docid = multipartychecklist.get(i).get("docid");
                String appointmentid  = multipartychecklist.get(i).get("appointment_id");
                String status = multipartychecklist.get(i).get("status");
                String reason = multipartychecklist.get(i).get("reason");

                postData(exec_email,docid,appointmentid,status,reason,token);
//                System.out.println(docid);
//                System.out.println(date);
//                System.out.println(item1);
//                System.out.println(item2);
//                System.out.println(item3);


            }


//            System.out.println("**********Post Completed************");



            return null;
        }

        protected void onPostExecute(Double result) {

//            Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
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
//            startActivity(new Intent(getApplicationContext(), NextActivity.class));
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        public void postData(String exec_email,String docid ,String appointmentid, String status,String reason,String token ) {

            DBOperation db = new DBOperation(getApplicationContext());


            String URL = "http://52.33.203.208:3000/api/v2/biometric_data/appointment_status";
            try {
                JSONObject app = new JSONObject();
                app.put("exec_email", exec_email);
                app.put("docid",docid);
                app.put("appointment_id", appointmentid);
                app.put("status", status);
                app.put("reason",reason);

//                System.out.println("appp JSON:" + app);
                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);
//                System.out.println("Token JSON:" + tokenjson);
                JSONObject main = new JSONObject();
                main.put("appointment", app);
                main.put("auth_token", tokenjson);
           // System.out.println("main JSON:" + main);
                String json = "";
                json = main.toString();
             System.out.println("jsonStatus:"+main);

                String strResponsePost = mResponse.doPostRequest(URL, json);


                if (!strResponsePost.equals("")) {
                    JSONObject jResult = new JSONObject(strResponsePost);
                    String strStatus = jResult.getString("status");
                    if (strStatus.equals(status1)) {

                       System.out.println(status1);

                    }

                }

            } catch (ClientProtocolException e) {

                // TODO Auto-generated catch block
            } catch (IOException e) {

                // TODO Auto-generated catch block
            } catch (JSONException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }
}