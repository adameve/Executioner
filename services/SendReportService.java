package anulom.executioner5.com3.anulom.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.Login;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.fragment.CompletedDetails;
import anulom.executioner5.com3.anulom.fragment.NewDetails;
import anulom.executioner5.com3.anulom.fragment.OlderDetails;
import anulom.executioner5.com3.anulom.fragment.TodayDetails;

/**
 * Created by Admin on 7/12/2016.
 */
public class SendReportService extends Service {



    public String umail = Login.umailid;
    GenericMethods mResponse;
    private SharedPreferences sharedPreferences;
    private String password2="";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//         Toast.makeText(this, "called from report", Toast.LENGTH_LONG).show();
        mResponse=new GenericMethods();

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

        HashMap<String, String> user;
        DBOperation db = new DBOperation(getApplicationContext());
        HashMap<String, String> auth_token;
        ArrayList<HashMap<String, String>> list;

        String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";

        @Override
        protected Double doInBackground(String... params) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            password2 = sharedPreferences.getString("password", "");
            ArrayList<HashMap<String,String>> acvrreportlist=db.getAcvrReport(db);
            for(int i=0;i<acvrreportlist.size();i++) {
                String docid = acvrreportlist.get(i).get("DocId");
                String distance = acvrreportlist.get(i).get("Distance");
                String amount = acvrreportlist.get(i).get("Amount");
                String itemtransport = acvrreportlist.get(i).get("TransportType");
                String app_id = acvrreportlist.get(i).get("Appointmentid");

                postData(docid, distance, amount, itemtransport, umail, token, app_id,db,password2);
            }
//            System.out.println("**********Post Completed************");
            return null;
        }

        protected void onPostExecute(Double result) {

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

        protected void onProgressUpdate(Integer... progress) {

        }

        public void postData(String did, String distance, String amount, String transport, String executioner, String token, String app_id,DBOperation db,String password) {
            String URL = GenericMethods.MAIN_URL+"/api/v2/biometric_data/update_acvr";
            try {
                // Execute HTTP Post Request

                JSONObject app = new JSONObject();

                app.put("Docid", did);
                app.put("app_id", app_id);
                app.put("Distance", distance);
                app.put("Amount", amount);
                app.put("Transport", transport);
                app.put("Executioner", executioner);
                app.put("password", password);

                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);

//                System.out.println("Token JSON:" + tokenjson);

                JSONObject main = new JSONObject();
                main.put("user", app);
                main.put("auth_token", tokenjson);
//                System.out.println("main JSON:" + main);

                String json = "";
                json = main.toString();

                 String  strResponsePost=mResponse.doPostRequest(URL,json);
//

                if(!strResponsePost.equals("")){
                    JSONObject jResult=new JSONObject(strResponsePost);
                    String strStatus=jResult.getString("status");
                    if(strStatus.equals("1")){
                        db.UpdateacvrStatus(db,app_id);
//                    Toast.makeText(StatusReportService.this, "Sync data successfully.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Network Problem.", Toast.LENGTH_SHORT).show();
                }

//                System.out.println("Response from post call:" + response);

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
