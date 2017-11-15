package anulom.executioner5.com3.anulom.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import anulom.executioner5.com3.anulom.R;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.taskcomment;

public class adhocdetails extends Fragment {
    ArrayList<HashMap<String, String>> getAlldataList = null;
    String curdate1, value1 = "Adhoc";
    ListView tasklist1;
    DBOperation db;
    ArrayList<String> reportkey = new ArrayList<>();
    ArrayList<String> documentidpay = new ArrayList<>();
    ArrayList<String> documentidcomment = new ArrayList<>();
    ArrayAdapter<String> adpt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.viewwitdetails, container, false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        curdate1 = df2.format(c.getTime());
        tasklist1 = (ListView) rootView.findViewById(R.id.tasklist1);
        reFreshReload();
        return rootView;

    }

    private void createAdpt() {


        adpt = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.taskadwitnessdetails1, reportkey) {

            public View getView(final int position, final View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                final LayoutInflater inflater = LayoutInflater.from(getContext());
                View v = inflater.inflate(R.layout.taskwitadhoc, null);

                TextView R_key = (TextView) v.findViewById(R.id.tvreportKey1);
                TextView createdat = (TextView) v.findViewById(R.id.tvdate2);
                TextView assignby = (TextView) v.findViewById(R.id.tvtenant2);
                TextView status1 = (TextView) v.findViewById(R.id.tvtenantvalue);
                final TextView date = (TextView) v.findViewById(R.id.tvdate2);


                TextView comment1 = (TextView) v.findViewById(R.id.tvtenant);


                assignby.setText(getAlldataList.get(position).get("assign_by"));
                comment1.setText(getAlldataList.get(position).get("comment"));
                createdat.setText(getAlldataList.get(position).get("created_at").substring(0, 10) + "\n" +
                        getAlldataList.get(position).get("created_at").substring(11, 16));
                R_key.setText(getAlldataList.get(position).get("document_id"));

                status1.setText(getAlldataList.get(position).get("status"));
                status1.setTextColor(Color.parseColor("#006B3C"));


                status1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity().getApplicationContext(), taskcomment.class);
                        i.putExtra("Value", value1);
                        i.putExtra("task_id", getAlldataList.get(position).get("task_id"));
                        i.putExtra("comment", getAlldataList.get(position).get("comment"));
                        startActivity(i);

                    }
                });


                return v;
            }

        };

        tasklist1.setAdapter(adpt);


    }


    private void viewDetails() {
        db = new DBOperation(getActivity());
        reportkey.clear();
        documentidcomment.clear();
        documentidpay.clear();
        getAlldataList = db.getadhocdetails(db);

        if (getAlldataList.size() == 0) {

        }
        for (int i = 0; i < getAlldataList.size(); i++) {
            reportkey.add(getAlldataList.get(i).get("id1"));

        }
    }


    public void reFreshReload() {
        viewDetails();
        createAdpt();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        reFreshReload();
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        reFreshReload();
//    }
//    public void notification(int hour, int min, int id1, String rkey,String id,String owner,String tenant,String w1,String w2,String news,String from,String content){
//        not_id =String.valueOf(id1);
//        AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//
//        Intent notificationIntent = new Intent(getActivity().getApplicationContext(), MyReceiver.class);
//
//        notificationIntent.putExtra("rkey",rkey);
//        notificationIntent.putExtra("id",id);
//        notificationIntent.putExtra("owner",owner);
//        notificationIntent.putExtra("tenant",tenant);
//        notificationIntent.putExtra("w1",w1);
//        notificationIntent.putExtra("w2",w2);
//        notificationIntent.putExtra("news",news);
//        notificationIntent.putExtra("not_id",not_id);
//        notificationIntent.putExtra("from",from);
//        notificationIntent.putExtra("content",content);
//
//        notificationIntent.addCategory("android.intent.category.DEFAULT");
//
//        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity().getApplicationContext(),id1, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY,hour);
//        cal.set(Calendar.MINUTE,min);
//
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
//        // alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),7200000, broadcast);
//
//
//    }

}