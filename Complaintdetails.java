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

public class Complaintdetails extends Fragment {

    public static TodayDetails thisToday = null;

    ArrayList<HashMap<String, String>> getAlldataList = null;

    String curdate1, value1 = "Complaint";
    ListView tasklist1;
    DBOperation db;

    ArrayList<String> reportkey = new ArrayList<>();


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
                TextView comment1 = (TextView) v.findViewById(R.id.tvtenant);
                TextView status1 = (TextView) v.findViewById(R.id.tvtenantvalue);
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

        getAlldataList = db.getComplaintdetails(db);
//        System.out.println("size from complaint:"+getAlldataList.size());
        for (int i = 0; i < getAlldataList.size(); i++) {
            reportkey.add(getAlldataList.get(i).get("id1"));
            //System.out.println("id"+getAlldataList.get(i).get("id1"));

        }

    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //thisToday = this;
    }

    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        thisToday = null;
    }

    public void reFreshReload() {
        viewDetails();
        createAdpt();
    }

}