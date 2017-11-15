package anulom.executioner5.com3.anulom.services;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import anulom.executioner5.com3.anulom.GenericMethods;
import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;

public class LocationTrackingService extends Service implements
		ConnectionCallbacks, OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {
	final long MIN_TIME_BW_UPDATES = 1 * 60 * 1000; // 1 minute
	LocationRequest mLocationRequest = null;
	GoogleApiClient mGoogleApiClient;
	boolean canGetLocation = false;
	private String UserName;
	private double latitude;
	private double longitude;
	private long locationTime;
	private int mins;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// removeGPSUpdates();
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.removeLocationUpdates(
					mGoogleApiClient, this);

			mGoogleApiClient.disconnect();
		}
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	private void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
		if (mGoogleApiClient != null) {
			createLocationRequest();
			mGoogleApiClient.connect();

		}

	}
	private void createLocationRequest() {

		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(MIN_TIME_BW_UPDATES);
		mLocationRequest.setFastestInterval(MIN_TIME_BW_UPDATES);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		buildGoogleApiClient();
		return super.onStartCommand(intent, flags, startId);

	}
	@Override
	public void onLocationChanged(Location location) {

		try {
			SharedPreferences setting = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			locationTime = location.getTime();
			String nowDate = ft.format(locationTime);
			Log.d("Location Changed",
					location.getLatitude() + " " + location.getLongitude());

			Editor editor = setting.edit();
			UserName = setting.getString("username", "");
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			String lastDateinMiliSeconds = setting.getString("lastDate", "");
			if (lastDateinMiliSeconds.equals("")) {
				mins = 1;
			} else if (!lastDateinMiliSeconds.equals("")) {
				String lastDate = convertDate(lastDateinMiliSeconds, "yyyy-MM-dd hh:mm a");
				Date Date1 = ft.parse(nowDate);
				Date Date2 = ft.parse(lastDate);
				long mills = Date1.getTime() - Date2.getTime();
//				mins = (int) (mills / (1000));
				mins = (int) (mills / (1000 * 60));

			}


			if ((mins >= 1)) {

				if (!UserName.equals("")) {

//					myToast("Inserted location " + latitude + " And "
//							+ longitude + "");
					Log.d("Inserted location", "Inserted location");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(DBManager.TableInfo.latitudeInput, "" + latitude);
					map.put(DBManager.TableInfo.longitudeInput, "" + longitude);
					map.put(DBManager.TableInfo.DATE, nowDate);
					map.put(DBManager.TableInfo.UserName, UserName);
					DBOperation db = new DBOperation(this);
					db.insertLocationDetails(db, map);
					locationTime = location.getTime();


					SharedPreferences shared1 = PreferenceManager.getDefaultSharedPreferences(this);
					SharedPreferences.Editor editor1 = shared1.edit();
					editor1.putString("Currentlatitude", String.valueOf(latitude));
					editor1.putString("CurrentLongitude", String.valueOf(longitude));
					editor1.commit();
					editor.putString("lastDate", "" + locationTime);
					editor.commit();

					if (SendLocationService.isRunning == false) {

						if (GenericMethods.isConnected(getApplicationContext())) {

							Intent timerIntent = new Intent(
									getApplicationContext(),
									SendLocationService.class);
							startService(timerIntent);

						}
					}

				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String convertDate(String dateInMilliseconds,
			String dateFormat) {
		return DateFormat
				.format(dateFormat, Long.parseLong(dateInMilliseconds))
				.toString();
	}

	@Override
	public void onConnected(Bundle arg0) {

		if (mLocationRequest != null) {
			startLocationUpdates();
		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	private void startLocationUpdates() {

		try
		{

		if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
		{
			LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
			Log.d("LocationTrackingService",
				"Location update started ..............: ");
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
