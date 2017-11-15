package anulom.executioner5.com3.anulom.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.fragment.CompletedDetails;
import anulom.executioner5.com3.anulom.fragment.NewDetails;
import anulom.executioner5.com3.anulom.fragment.OlderDetails;
import anulom.executioner5.com3.anulom.fragment.TodayDetails;

/**
 * Created by Admin on 7/4/2016.
 */
public class SendCommentService extends Service {

    GenericMethods mResponse;
    String URL = "http://52.33.203.208/cmt/api/v1/comment_data/add_comment";
    private SharedPreferences sharedPreferences;
    private String password2="",username="";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (GenericMethods.isConnected(getApplicationContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new SendComment()
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {
                new SendComment().execute();
            }
        }
        return START_STICKY;


    }

    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }


    private class SendComment extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            String strResponsePost = "",cmd_type="";
            String ownerTypeRole = "";
            // TODO Auto-generated method stub


            try {

                DBOperation db = new DBOperation(getBaseContext());


                ArrayList<HashMap<String, String>> listofcommment = db.getcomment(db);
                System.out.println("commentsize from send:"+listofcommment.size());
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                password2 = sharedPreferences.getString("password", "");
                username = sharedPreferences.getString("username", "");
                mResponse = new GenericMethods();
                for (int i = 0; i < listofcommment.size(); i++) {

                    String strOwner = listofcommment.get(i).get("Commentowner");
                    String strCustomerComment = listofcommment.get(i).get("CustomerComment");
                    String strCommentUser = listofcommment.get(i).get("CommentUser");
                    String strDocId = listofcommment.get(i).get("DocId");
                    String strCommentID = listofcommment.get(i).get("ComentID");
                    System.out.println("Strdocid:"+strDocId);
//                    String strCommentdate = listofcommment.get(i).get("CommentDate");
                    String strReminderDate = listofcommment.get(i).get("ReminderDate");
                    if(strReminderDate.equals(""))
                    {
                        cmd_type="Internal";
                    }
                    else {
                        cmd_type="Reminder";
                    }



                    if(strOwner.equals(""))
                    {
                        ownerTypeRole = "";
                    }
                    else
                    {
                        ArrayList<HashMap<String, String>> userlist = db.getOwnerType(db, strOwner);
                        //System.out.println("size:"+userlist.size());
//                        for(int j=0;j<userlist.size();j++){
//
//                            System.out.println("role:i:"+j+db.getOwnerType(db,strOwner).get(j).get("role2"));
//                        }
                        ownerTypeRole = userlist.get(0).get("role2");

                    }

                    ArrayList<HashMap<String, String>> userlist1=db.getExecutionerType(db,username);
                   // System.out.println("size1:"+userlist1.size());
//
// for(int j=0;j<userlist1.size();j++){
//
//
//                        System.out.println("role2:i:"+j+db.getExecutionerType(db,username).get(j).get("role2"));
//
//                    }
                    String executionerTypeRole = userlist1.get(0).get(DBManager.TableInfo.Role);

                    ArrayList<HashMap<String, String>> userlist2=db.getDocumentUser(db,strDocId);
                   // System.out.println("size2:"+userlist2.size());
//                    for(int j=0;j<userlist2.size();j++){
//
//                        System.out.println("role3:i:"+j+db.getDocumentUser(db,strDocId).get(j).get("Omail"));
//                    }
                    String document_user = userlist2.get(0).get(DBManager.TableInfo.OwnerEmail);

                    String strJS = createServiceJson(strDocId,strCustomerComment,document_user,cmd_type,
                    strCommentUser,strReminderDate,executionerTypeRole,strOwner,ownerTypeRole);
                    strResponsePost = mResponse.doPostRequest(URL, strJS);

                    if(!strResponsePost.equals("")){
                        JSONObject jResult= new JSONObject();
                        String strStatus= "";
                        try {

                            jResult = new JSONObject(strResponsePost);
                            strStatus = jResult.getString("status");
                            Log.d("Status cmt",strStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(strStatus.equals("1")){
                            db.UpdateCommentStatus(db,strCommentID);
                           //Toast.makeText(SendCommentService.this, "all is well", Toast.LENGTH_SHORT).show();
                            System.out.println("all is well");
                        }
                    }

                }

            } catch (IOException e) {
                stopSelf();
                e.printStackTrace();
            }


            System.out.println("**********Post Completed************");
            return strResponsePost;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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

    private String createServiceJson(String strDocId, String strCustomerComment, String document_user, String cmd_type,
                                     String strCommentUser,String strCommentdate,
                                     String executionerTypeRole,String strOwner,String ownerTypeRole) {
        DBOperation db = new DBOperation(getBaseContext());
        try {

            JSONObject serviceJsn = new JSONObject();
            JSONObject obj = new JSONObject();
            try {
                obj.put("docid", strDocId);
                obj.put("comment", strCustomerComment);
                obj.put("document_user", document_user);
                obj.put("cmt_type", cmd_type);
                obj.put("executioner", strCommentUser);
                obj.put("reminder", strCommentdate);
                obj.put("executioner_type", executionerTypeRole);
                obj.put("owner", strOwner);
                obj.put("owner_type", ownerTypeRole);
                obj.put("platform_id","3");

            }
            catch (JSONException e) {


                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            serviceJsn.put("user", obj);
            JSONObject jsonauto_token = new JSONObject();
            jsonauto_token.put("token", "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7");
            serviceJsn.put("auth_token", jsonauto_token);
           // String post="";
            return serviceJsn.toString();
            //String strResponsePost = mResponse.doPostRequest(URL, post);


//            if (!strResponsePost.equals("")) {
//                JSONObject jResult = new JSONObject(strResponsePost);
//                String strStatus = jResult.getString("status");
//                if (strStatus.equals("1")) {
//                    System.out.println("from comment:"+strStatus);
//                }
//
//            }


        }

        catch (Exception e) {

        }

        return null;
    }



}


