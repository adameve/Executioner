package anulom.executioner5.com3.anulom.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.fragment.CompletedDetails;
import anulom.executioner5.com3.anulom.fragment.NewDetails;
import anulom.executioner5.com3.anulom.fragment.OlderDetails;
import anulom.executioner5.com3.anulom.fragment.TodayDetails;

/**
 * Created by Admin on 7/1/2016.
 */
public class StatusReportService extends Service {
    GenericMethods mResponse;
    private SharedPreferences sharedPreferences;
    private String password2="";
    @Nullable

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mResponse=new GenericMethods();
        if (GenericMethods.isConnected(getApplicationContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new SyncStatusInfo()
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {
                new SyncStatusInfo().execute();
            }
        }
        return START_STICKY;



    }

    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }


    private class SyncStatusInfo extends AsyncTask<String, Void, String> {
        DBOperation db = new DBOperation(getApplicationContext());

        @Override
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub
            String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            password2 = sharedPreferences.getString("password", "");
            ArrayList<HashMap<String,String>> statusreportlist=db.getSyncStatusReport(db);
            for(int i=0;i<statusreportlist.size();i++)
            {
                String docid = statusreportlist.get(i).get("DocId");
                String item = statusreportlist.get(i).get("OwnBio");
                String item1 = statusreportlist.get(i).get("TenantBio");
                String item2 = statusreportlist.get(i).get("Wit1");
                String item3 = statusreportlist.get(i).get("Wit2");

//                System.out.println("Biometric values " +item +" " +item1 +" " +item2 +" "+ item3);


                postData(docid, item, item1, item2, item3, token,db,password2);

            }

//            System.out.println("**********Post Completed************");
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
            stopSelf();
            if(TodayDetails.thisToday!=null){
                TodayDetails.thisToday.reFreshReload();
            }
            if(OlderDetails.thisOlderDetails!=null){
                OlderDetails.thisOlderDetails.reFreshReload();
            }
            if(NewDetails.thisnewDetails!=null){
                NewDetails.thisnewDetails.reFreshReload();
            }
            if(CompletedDetails.thiscompleteDetails!=null){
                CompletedDetails.thiscompleteDetails.reFreshReload();
            }

//            startActivity(new Intent(getApplicationContext(), NextActivity.class));
        }

    }

    public void postData(String did, String oc, String tc, String w1c, String w2c, String token, DBOperation db,String password) {
        String URL = "http://52.33.203.208:3000/api/v1/exec_status/appointment_details.json";
        try {
            // Execute HTTP Post Request
            JSONObject app = new JSONObject();
            app.put("docid", did);
            app.put("biocomp1", oc);
            app.put("biocomp", tc);
            app.put("reg_from_comp", w1c);
            app.put("witness", w2c);
            app.put("password", password);
//            System.out.println("appp JSON:" + app);
            JSONObject tokenjson = new JSONObject();
            tokenjson.put("token", token);
//            System.out.println("Token JSON:" + tokenjson);
            JSONObject main = new JSONObject();
            main.put("appointment", app);
            main.put("auth_token", tokenjson);
//            System.out.println("main JSON:" + main);
            String json = "";
            json = main.toString();
            String  strResponsePost=mResponse.doPostRequest(URL,json);
            if(!strResponsePost.equals("")){
                JSONObject jResult=new JSONObject(strResponsePost);
                String strStatus=jResult.getString("status");
                if(strStatus.equals("1")){
                    db.UpdateAppointmentStatus(db,did);
//                    Toast.makeText(StatusReportService.this, "Sync data successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ClientProtocolException e) {
            stopSelf();
            // TODO Auto-generated catch block
        } catch (IOException e) {
            stopSelf();
            // TODO Auto-generated catch block
        } catch (JSONException e) {
            stopSelf();
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
