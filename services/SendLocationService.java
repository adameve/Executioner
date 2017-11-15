package anulom.executioner5.com3.anulom.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;

public class  SendLocationService extends Service {

	private final static String NAMESPACE = "http://ws.fieldomobify.com";

	public static boolean isRunning = false;
	String UserName;


	Geocoder geocoder;
	GenericMethods mResponse;
	private String strUserId="",password2="";
	private SharedPreferences sharedPreferences;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		try {
			geocoder = new Geocoder(this);
			mResponse=new GenericMethods();

			isRunning = true;
			//helper = ((MyApplication) getApplication()).getSQLDatabase();
			SharedPreferences setting = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			UserName = setting.getString("username", "");



			if (GenericMethods.isConnected(getApplicationContext())) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					new SendLocation()
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

				} else {
					new SendLocation().execute();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			isRunning = false;
			stopSelf();
		}

		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private class SendLocation extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			String strResponsePost="";

			try {

				DBOperation db = new DBOperation(getBaseContext());
				ArrayList<HashMap<String, String>> listUsername = db.getLocationDetailsUsername(db);


				String strJS=createServiceJson(listUsername);
				strResponsePost=mResponse.doPostRequest(GenericMethods.URL,strJS);

			} catch (Exception e) {
//				// TODO: handle exception
				e.printStackTrace();
				}
//
				return strResponsePost;

		}


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isRunning = false;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			if(result!=null || !result.trim().isEmpty()) {
				try {
					JSONObject jsonObject=new JSONObject(result);
					String jStrResult=jsonObject.getString("status");

				  if (jStrResult.equals("1")) {
					  DBOperation db = new DBOperation(getBaseContext());
					  db.delLocationDetails(db);
				   }
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			isRunning = false;
			stopSelf();
//			Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
		}
	}

	private String createServiceJson(ArrayList<HashMap<String, String>> listData) {
		try {
			JSONObject serviceJsn= new JSONObject();
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			password2 = sharedPreferences.getString("password", "");
			JSONObject obj = null;
			DBOperation db = new DBOperation(getBaseContext());
			strUserId=db.getUserId(db,UserName);
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < listData.size(); i++) {
				obj = new JSONObject();
				try {
					obj.put("id",strUserId);
					obj.put("latitude", listData.get(i).get(DBManager.TableInfo.latitudeInput));
					obj.put("longitude", listData.get(i).get(DBManager.TableInfo.longitudeInput));
					obj.put("cur_dt", listData.get(i).get(DBManager.TableInfo.DATE));
					obj.put("password", password2);;

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jsonArray.put(obj);
			}

			serviceJsn.put("user_location", jsonArray);

			JSONObject jsonauto_token= new JSONObject();
			jsonauto_token.put("token","DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7");
			serviceJsn.put("auth_token",jsonauto_token);

			return serviceJsn.toString();

		}catch (Exception e){}

		return null;
	}

}
