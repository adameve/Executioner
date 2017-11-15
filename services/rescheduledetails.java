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
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.RESCHEDULE;

/**
 * Created by Admin on 7/12/2016.
 */
public class rescheduledetails extends Service {
    private SharedPreferences sharedPreferences;
    DBOperation db;

    public String umail = Login.umailid;
    GenericMethods mResponse;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF8");
    String username3,status="0";
    private String username2 = "";

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
        DBOperation db = new DBOperation(getApplicationContext());

        String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";

        @Override
        protected Double doInBackground(String... params) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username2 = sharedPreferences.getString("username", "");
            username3 = username2;

            ArrayList<HashMap<String, String>> reschedulelist = db.getrescheduledetails(db);
            System.out.println("reschedulelist size:" + reschedulelist.size());
            for (int i = 0; i < reschedulelist.size(); i++) {
                String exec_email = reschedulelist.get(i).get("exec_email");
                String docid = reschedulelist.get(i).get("docid");
                String appointmentid = reschedulelist.get(i).get("appointment_id");
                String rescheduledate = reschedulelist.get(i).get("rescheduledate");
                String rescheduletime = reschedulelist.get(i).get("rescheduletime");
                String reason = reschedulelist.get(i).get("reason1");

                postData(exec_email,docid,appointmentid,rescheduledate,rescheduletime,reason,token);
//                System.out.println(docid);
//                System.out.println(appointmentid);
//                System.out.println(rescheduledate);
//                System.out.println(rescheduletime);
//                System.out.println(reason);


            }


          System.out.println("**********Post Completed************");



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

        public void postData(String exec_email,String docid ,String appointmentid, String rescheduledate, String rescheduletime, String reason, String token) {

            DBOperation db = new DBOperation(getApplicationContext());


            String URL = "http://52.33.203.208:3000/api/v2/biometric_data/reschedule_appointment";

            try {

                JSONObject app = new JSONObject();
                app.put("exec_email", exec_email);
                app.put("docid",docid);
                app.put("appointment_id", appointmentid);
                app.put("date", rescheduledate);
                app.put("time",rescheduletime);
                app.put("res_reason", reason);
                app.put("slot_id","");
                app.put("app_flag",1);

                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);

                JSONObject main = new JSONObject();
                main.put("appointment", app);
                main.put("auth_token", tokenjson);
                String json = "";
                json = main.toString();
                System.out.println(main);

                String strResponsePost = mResponse.doPostRequest(URL, json);
                //System.out.println("Status"+strResponsePost);
                if (!strResponsePost.equals("")) {
                    JSONObject jResult = new JSONObject(strResponsePost);
                    String strStatus = jResult.getString("status");
                    System.out.println("Status"+strStatus);
                    if (strStatus.equals(status)) {
//                        System.out.println("success");
                        SQLiteDatabase sqldb = db.getWritableDatabase();

                        int count = sqldb.delete(RESCHEDULE, DBManager.TableInfo.DocumentId + "=?", new String[]{docid});
                        System.out.println(" DB ROW DELETED SUCCESS:" + count);


                    }

                }
                System.out.println("**********Post Completed************");


            } catch (JSONException e) {

                // TODO Auto-generated catch block
                //     e.printStackTrace();
                // }

            }catch (ClientProtocolException e) {

                // TODO Auto-generated catch block
            } catch (IOException e) {

            }

        }


    }
}