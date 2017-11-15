package anulom.executioner5.com3.anulom.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.Login;
import anulom.executioner5.com3.anulom.database.DBOperation;

/**
 * Created by Admin on 11/17/2016.
 */

public class GetCommentService extends Service {


    public String umail = Login.umailid;
    GenericMethods mResponse;
    private SharedPreferences sharedPreferences;
    private String Url = "",commentdate="";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {

//        Toast.makeText(this, "called from report", Toast.LENGTH_LONG).show();
        mResponse = new GenericMethods();
        Url = "http://52.33.203.208/cmt/api/v1/comment_data/fetch_comment";
        new GetCommentTask().execute();
        return START_STICKY;


    }

    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    private class GetCommentTask extends AsyncTask<Void, Void, Void> {
        String strResponsePost = "";
        private JSONArray ja = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DBOperation db = new DBOperation(GetCommentService.this);

                List<String> reportleylist = db.getReportkey();
                Log.d("tag", "REPORTKEYLIST" + reportleylist);

                String strJS = createServiceJson(reportleylist);

                strResponsePost = mResponse.doPostRequest(Url, strJS);
                JSONObject jsonObj = new JSONObject(strResponsePost);
                // Getting JSON Array node
                ja = jsonObj.getJSONArray(GenericMethods.TAG_NAME1);

                if (ja.length() > 0) {
                    db.deleteSyncComment(db);
                }
                for (int j = 0; j < ja.length(); j++) {
                    JSONObject c = ja.getJSONObject(j);
                    String env_id = c.getString(GenericMethods.TAG_ENV);
                    String cust_comments = c.getString(GenericMethods.TAG_COMMENTS);
                    String user = c.getString(GenericMethods.TAG_CUSER);
                    String owner = c.getString(GenericMethods.TAG_COWNER);
                    String cid = c.getString(GenericMethods.TAG_CID);
                    String isdone = c.getString(GenericMethods.TAG_C_IS_DONE);
                    String datecomment = c.getString(GenericMethods.TAG_CDATE);
                    String reminder_dt = c.getString("reminder_dt");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");

                    java.util.Date cdate;
                    try {
                        cdate = sdf.parse(datecomment);

                        commentdate = output.format(cdate);

//                        System.out.println("formatted DATE:'''''''''''''''" + commentdate);

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String SyncStatus = "SYNC";
//                    if (!db.checkCommentID(db, cid)) {
                        db.InsertRecord3(db, env_id, cust_comments, user, owner, cid, isdone, commentdate, SyncStatus,reminder_dt);
//                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private String createServiceJson(List<String> reportleylist) {
        try {
            JSONObject serviceJsn = new JSONObject();
            JSONObject obj = new JSONObject();
            try {

                    obj.put("key",reportleylist);
                Log.d("TAG","Main json:"+reportleylist);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            serviceJsn.put("document", obj);
            JSONObject jsonauto_token = new JSONObject();
            jsonauto_token.put("token", "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7");
            serviceJsn.put("auth_token", jsonauto_token);
            return serviceJsn.toString();

        } catch (Exception e) {
        }

        return null;
    }

}
