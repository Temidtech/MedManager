package com.swiftsynq.medmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.swiftsynq.medmanager.Model.Medication;
import com.swiftsynq.medmanager.Utils.ReminderUtilities;
import com.swiftsynq.medmanager.data.MedmanagerDbHelper;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

/**
 * Created by popoolaadebimpe on 31/03/2018.
 */

public class MedicationActivity extends AppCompatActivity  {
    @BindView(R.id.edtStartDate)
    Button edtStartDate;
    @BindView(R.id.edtEndDate)
    Button edtEndDate;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.edtDrugName)
    EditText edtDrugName;
    @BindView(R.id.edtDescription)
    EditText edtDescription;
    String interval="";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        Init();

    }
    private void Init()
    {
        ButterKnife.bind(this); // bind butterknife after
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final MedmanagerDbHelper mDbHelper = new MedmanagerDbHelper(getBaseContext());
        final AwesomeValidation mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(this, R.id.edtDrugName, "[a-zA-Z0-9\\s]+", R.string.err_drugname);
        mAwesomeValidation.addValidation(this, R.id.edtDescription, "[a-zA-Z0-9\\s]+", R.string.err_description);
        final ElegantNumberButton button = (ElegantNumberButton) findViewById(R.id.btnInterval);
        final Medication medication=new Medication();

        button.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                interval=button.getNumber().toString();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAwesomeValidation.validate())
                {
                    medication.setInterval(interval);
                    medication.setStartdate(edtStartDate.getText().toString());
                    medication.setEnddate(edtEndDate.getText().toString());
                    medication.setDecsription(edtDescription.getText().toString());

                    medication.setDrugName(edtDrugName.getText().toString());

                    if(MedManagerTbOperations.insert(medication,mDbHelper)>0)
                    {
                        Toast.makeText(getBaseContext(),"Successfully added!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(MedicationActivity.this,HomeActivity.class);
                        startActivity(intent);
                        ReminderUtilities.scheduleMedicationReminder(getBaseContext(),medication);
                    }
                    else {
                        Toast.makeText(getBaseContext(),"Ops`! An error occured!",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                }

            }
        });

        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dt = new Date();
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(dt.getYear(), dt.getMonth(), dt.getDay())
                        .setDoneText("Select")
                        .setCancelText("Cancel")
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                //edtStartDate.setText(getStringDateTime(getString(R.string.calendar_date_picker_result_values, year, monthOfYear, dayOfMonth)));
                                edtStartDate.setText(getString(R.string.calendar_date_picker_result_values, getStringMonthTime(monthOfYear),dayOfMonth,year));

                            }
                        })
                        .setThemeLight();
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });
        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dt = new Date();
                CalendarDatePickerDialogFragment cdpend = new CalendarDatePickerDialogFragment()
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(dt.getYear(), dt.getMonth(), dt.getDay())
                        .setDoneText("Select")
                        .setCancelText("Cancel").setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                edtEndDate.setText(getString(R.string.calendar_date_picker_result_values, getStringMonthTime(monthOfYear),dayOfMonth,year));
                            }
                        })

                        .setThemeLight();
                cdpend.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

    }
    private String getStringMonthTime(int month) {
        String monthString;
        monthString = new DateFormatSymbols().getMonths()[month];
        return monthString;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
