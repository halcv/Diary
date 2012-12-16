package org.h_naka.diary;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.SharedPreferences;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemSelectedListener;
//import android.util.Log;
import java.util.Calendar;

public class DiaryMainActivity
    extends Activity
    implements OnClickListener, OnItemSelectedListener {

    private boolean m_isRead = true;
    private String m_strYear;
    private String m_strMonth;
    private String m_strDay;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        initWidget();
        setDate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }

    private void initWidget() {
        final String [] yearArray = getResources().getStringArray(R.array.year);
        final ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,R.layout.item,yearArray);
        final Spinner yearSpinner = (Spinner)findViewById(R.id.year);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(this);
        
        final String [] monthArray = getResources().getStringArray(R.array.month);
        final ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,R.layout.item,monthArray);
        final Spinner monthSpinner = (Spinner)findViewById(R.id.month);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(this);
        
        final String [] dayArray = getResources().getStringArray(R.array.day);
        final ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this,R.layout.item,dayArray);
        final Spinner daySpinner = (Spinner)findViewById(R.id.day);
        daySpinner.setAdapter(dayAdapter);
        daySpinner.setOnItemSelectedListener(this);
        
        final Button btnReadWrite = (Button)findViewById(R.id.read_writeButton);
        btnReadWrite.setOnClickListener(this);

        final Button btnSave = (Button)findViewById(R.id.saveButton);
        btnSave.setOnClickListener(this);
        
        final EditText eText = (EditText)findViewById(R.id.edit);
        eText.setVisibility(View.GONE);

        final ScrollView scroll = (ScrollView)findViewById(R.id.scroll);
        scroll.setVisibility(View.VISIBLE);

    }

    private void setDate() {
        Calendar now = Calendar.getInstance();
        final Spinner yearSpinner = (Spinner)findViewById(R.id.year);
        yearSpinner.setSelection(now.get(Calendar.YEAR) - 2012);
        final Spinner monthSpinner = (Spinner)findViewById(R.id.month);
        monthSpinner.setSelection(now.get(Calendar.MONTH));
        final Spinner daySpinner = (Spinner)findViewById(R.id.day);
        daySpinner.setSelection(now.get(Calendar.DATE) - 1);
    }
    
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.read_writeButton:
            if (m_isRead) {
                m_isRead = false;
                enableEditText();
            } else {
                m_isRead = true;
                enableTextView();
            }
            changeButtonText();
            break;
        case R.id.saveButton:
            saveText();
            break;
        }
    }

    public void onItemSelected(AdapterView<?> parent,View v,int pos,long id) {
        saveText();
        final Spinner spinner = (Spinner)parent;
        if (spinner == findViewById(R.id.year)) {
            m_strYear = spinner.getSelectedItem().toString();
        } else if (spinner == findViewById(R.id.month)) {
            m_strMonth = spinner.getSelectedItem().toString();
        } else if (spinner == findViewById(R.id.day)) {
            m_strDay = spinner.getSelectedItem().toString();
        }

        if (m_strYear != null && m_strMonth != null && m_strDay != null) {
            final TextView vText = (TextView)findViewById(R.id.text);
            final EditText eText = (EditText)findViewById(R.id.edit);
            String text = getText();
            vText.setText(text);
            eText.setText(text);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void enableEditText() {
        final TextView vText = (TextView)findViewById(R.id.text);
        final EditText eText = (EditText)findViewById(R.id.edit);
        final ScrollView Scroll = (ScrollView)findViewById(R.id.scroll);
        String text = vText.getText().toString();
        eText.setText(text);
        Scroll.setVisibility(View.GONE);
        eText.setVisibility(View.VISIBLE);
        eText.setEnabled(true);
        eText.setFocusable(true);
        eText.setFocusableInTouchMode(true);
    }

    private void enableTextView() {
        final TextView vText = (TextView)findViewById(R.id.text);
        final EditText eText = (EditText)findViewById(R.id.edit);
        final ScrollView Scroll = (ScrollView)findViewById(R.id.scroll);
        String text = eText.getText().toString();
        vText.setText(text);
        eText.setVisibility(View.GONE);
        Scroll.setVisibility(View.VISIBLE);
        eText.setEnabled(false);
        eText.setFocusable(false);
        eText.setFocusableInTouchMode(false);
    }

    private void changeButtonText() {
        int text;
        if (m_isRead) {
            text = R.string.write;
        } else {
            text = R.string.read;
        }

        final Button btnReadWrite = (Button)findViewById(R.id.read_writeButton);
        btnReadWrite.setText(text);
    }

    private String getText() {
        String index = m_strYear + m_strMonth + m_strDay;
        SharedPreferences sp = getSharedPreferences("Diary",MODE_PRIVATE);
        return (sp.getString(index,""));
    }

    private void saveText() {
        final EditText eText = (EditText)findViewById(R.id.edit);
        String text = eText.getText().toString();
        if (m_strYear != null && m_strMonth != null && m_strDay != null &&
            text.length() != 0) {
            String index = m_strYear + m_strMonth + m_strDay;
            SharedPreferences sp = getSharedPreferences("Diary",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(index,text);
            editor.commit();
        }
    }
}
