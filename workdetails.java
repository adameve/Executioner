package anulom.executioner5.com3.anulom.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import anulom.executioner5.com3.anulom.R;
import anulom.executioner5.com3.anulom.database.DBManager;
import anulom.executioner5.com3.anulom.database.DBOperation;
import anulom.executioner5.com3.anulom.services.weekendoffdetails;

import static anulom.executioner5.com3.anulom.database.DBManager.TableInfo.WEEKEND;

/**
 * Created by anulom on 2/8/17.
 */

public class workdetails extends Fragment {
    String curdate1, opt, datevalue1, datevalue2, timevalue1, timevalue2;
    EditText edt_on_date, edt2, edt3, edt4;
    Button btn_date, btn2, btn3, btn4, updatework;
    private SharedPreferences usermail;
    private String username2 = "", password2 = "";
    private Calendar myCalendar;
    RadioButton rb1, rb2;
    RadioGroup rg1;
    DBOperation db;
    String ID1;
    DatePickerDialog.OnDateSetListener date1, date2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.viewwork, container, false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        curdate1 = df2.format(c.getTime());
        db = new DBOperation(getActivity());
        btn_date = (Button) rootView.findViewById(R.id.btn_date);
        edt_on_date = (EditText) rootView.findViewById(R.id.edt_on_date);
        btn2 = (Button) rootView.findViewById(R.id.btn_date1);
        btn3 = (Button) rootView.findViewById(R.id.buttontime1);
        btn4 = (Button) rootView.findViewById(R.id.buttontime2);
        edt2 = (EditText) rootView.findViewById(R.id.edt_on_date1);
        updatework = (Button) rootView.findViewById(R.id.btnSubmit);
        edt3 = (EditText) rootView.findViewById(R.id.editTime1);
        edt4 = (EditText) rootView.findViewById(R.id.editTime2);
        rb1 = (RadioButton) rootView.findViewById(R.id.work1);
        rb2 = (RadioButton) rootView.findViewById(R.id.work2);
        rg1 = (RadioGroup) rootView.findViewById(R.id.radioGroupwork);
        usermail = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username2 = usermail.getString("username", "");
        password2 = usermail.getString("password", "");

        myCalendar = Calendar.getInstance();

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date1 = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "YYYY-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edt2.setText(sdf.format(myCalendar.getTime()));
                        datevalue1 = edt2.getText().toString();
                        //System.out.println("from date:"+ datevalue1);

                    }

                };

                new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date2 = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "YYYY-MM-dd "; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edt_on_date.setText(sdf.format(myCalendar.getTime()));
                        datevalue2 = edt_on_date.getText().toString();
                        //System.out.println("to date:"+ datevalue2);

                    }

                };

                new DatePickerDialog(getActivity(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt3.setText(selectedHour + ":" + selectedMinute);
                        timevalue1 = edt3.getText().toString();
                        //System.out.println("from time:"+ timevalue1);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt4.setText(selectedHour + ":" + selectedMinute);
                        timevalue2 = edt4.getText().toString();
                        //System.out.println("from time:"+ timevalue2);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.work1:
                        opt = "WeekEnd";
                        break;
                    case R.id.work2:
                        opt = "Holiday";
                        break;
                }

                //System.out.println("radio:"+opt);
            }


        });

//        finalfromdate=datevalue1+" "+timevalue1;
//        finaltodate=datevalue1+" "+timevalue2;

        updatework.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                SQLiteDatabase base3 = db.getWritableDatabase();
                base3.delete(WEEKEND, null, null);
                //System.out.println("work DB Deleted");

                SQLiteDatabase db4 = db.getWritableDatabase();
                ContentValues values4 = new ContentValues();
                values4.put(DBManager.TableInfo.KEYID, ID1);
                values4.put(DBManager.TableInfo.from_date, datevalue1 + " " + timevalue1);
                values4.put(DBManager.TableInfo.to_date, datevalue2 + " " + timevalue2);
                values4.put(DBManager.TableInfo.statusoff, 0);
                values4.put(DBManager.TableInfo.reasonoff, opt);
                values4.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                String condition4 = DBManager.TableInfo.reasonoff + "=?";
                Cursor cursor4 = db4.query(WEEKEND, null, condition4, new String[]{DBManager.TableInfo.to_date}, null, null, null);
                long status4 = db4.insert(WEEKEND, null, values4);
//         System.out.println("work insert : " + status4);
//        System.out.println("Success" + username2);
                cursor4.close();
                db4.close();

                getActivity().startService(new Intent(getActivity(), weekendoffdetails.class));
                //getActivity().finish();

            }
        });


        return rootView;

    }


}
