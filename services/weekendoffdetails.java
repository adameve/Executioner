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
import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.WEEKEND;

/**
 * Created by Admin on 7/12/2016.
 */
public class weekendoffdetails extends Service {
    private SharedPreferences sharedPreferences;
    DBOperation db;

    public String umail = Login.umailid;
    GenericMethods mResponse;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF8");
    String username3,status="1";
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

            ArrayList<HashMap<String, String>> workleavelist = db.getweekendoffdetails(db);
            System.out.println("workleavelist size:" + workleavelist.size());
            for (int i = 0; i < workleavelist.size(); i++) {
                String exec_email = workleavelist.get(i).get("exec_email");
                String from_date = workleavelist.get(i).get("from_date");
                String to_date = workleavelist.get(i).get("to_date");
                String status_off = workleavelist.get(i).get("status_off");
                String reason_off = workleavelist.get(i).get("reason_off");

                postData(exec_email,from_date,to_date,status_off,reason_off,token);
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

        public void postData(String exec_email,String from_date ,String to_date, String status_off, String reason_off,  String token) {

            DBOperation db = new DBOperation(getApplicationContext());


            String URL = "http://52.33.203.208:3000/api/v2/biometric_data/leave_tracker";

            try {

                JSONObject app = new JSONObject();
                app.put("exec_email", exec_email);
                app.put("from_date",from_date);
                app.put("to_date", to_date);
                app.put("status", status_off);
                app.put("reason",reason_off);


                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);
//
                JSONObject main = new JSONObject();
                main.put("user", app);
                main.put("auth_token", tokenjson);
                String json = "";
                json = main.toString();
                System.out.println(main);

                String strResponsePost = mResponse.doPostRequest(URL, json);
//                System.out.println("Status"+strResponsePost);
                if (!strResponsePost.equals("")) {
                    JSONObject jResult = new JSONObject(strResponsePost);
                    String strStatus = jResult.getString("status");
//                    System.out.println("Status"+strStatus);
                    if (strStatus.equals(status)) {
//                        System.out.println("success");
                        SQLiteDatabase sqldb = db.getWritableDatabase();

                        int count = sqldb.delete(WEEKEND, DBManager.TableInfo.from_date + "=?", new String[]{from_date});
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