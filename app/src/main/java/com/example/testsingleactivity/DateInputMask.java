package com.example.testsingleactivity;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateInputMask implements TextWatcher {

    public static final int MAX_FORMAT_LENGTH = 8;
    public static final int MIN_FORMAT_LENGTH = 3;

    private String updatedText;
    private boolean editing;


    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        if (text.toString().equals(updatedText) || editing) return;

        String digitsOnly = text.toString().replaceAll("\\D", "");
        int digitLen = digitsOnly.length();

        if (digitLen < MIN_FORMAT_LENGTH || digitLen > MAX_FORMAT_LENGTH) {
            updatedText = digitsOnly;
            return;
        }

        if (digitLen <= 4) {
            String month = digitsOnly.substring(2);
            int m = Integer.parseInt(month);
            if(m>=13) {
                month = "12";
            }
            String day = digitsOnly.substring(0, 2);
            int d = Integer.parseInt(day);
            if(d<=0){
                day = "01";
            }else if(d>=32){
                day = "31";
            }
            updatedText = String.format(Locale.UK, "%s/%s", day, month);
        }
        else {
            String month = digitsOnly.substring(2, 4);
            int m = Integer.parseInt(month);
            if(m>=13) {
                month = "12";
            }
            String day = digitsOnly.substring(0, 2);
            int d = Integer.parseInt(day);
            if(d<=0){
                day = "01";
            }else if(d>=32){
                day = "31";
            }
            String year = digitsOnly.substring(4);
            int y = Integer.parseInt(year);
            if(y<=0){
                year = "1970";
            }else if(y>=2022){
                year = "2020";
            }

            updatedText = String.format(Locale.UK, "%s/%s/%s", day, month, year);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (editing) return;

        editing = true;

        editable.clear();
        editable.insert(0, updatedText);

        editing = false;
    }
}