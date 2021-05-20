package com.example.testsingleactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    private Button add,addUser,update,updateUser,remove,language,back,smile,logIn,signUp,reg,backToMain;
    private LinearLayout pageOne,pageTwo,pageLogin,pageReg;
    private EditText name,exp,messageCount,login,password,fullName,birthday,eMail,regLogin,regPass,repPass;
    private TextView message,welcome;
    private ImageView logo;
    private String emoji;
    private final EmojiHelper emojiHelper = new EmojiHelper();
    private final DBHelper dbHelper = new DBHelper(this);
    private final ContentValues contentValues = new ContentValues();
    private SQLiteDatabase usersDb;
    private Cursor cursor;
    private final ArrayList<User> users = new ArrayList<>();
    private UsersAdapter adapter;
    private ArrayAdapter<String> smileAdapter;
    private ListView userList;
    private SharedPreferences preferences;
    public int l,p=2,idUser=0,selected,count = 4;
    private final Languages lang = new Languages();
    private boolean flag = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        initRes();
        loadProgramData();
        loadDb();
        loadPage(l,p);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    public void setButtons(int n){
        switch (n){
            case 0:
                add.setOnClickListener(v -> {
                    p=1;
                    loadPage(l,p);
                    addUser.setVisibility(View.VISIBLE);
                    updateUser.setVisibility(View.INVISIBLE);
                });
                update.setOnClickListener(v -> {
                        p=1;
                        loadPage(l,p);
                        setUpdatePage();
                        addUser.setVisibility(View.INVISIBLE);
                        updateUser.setVisibility(View.VISIBLE);
                });
                remove.setOnClickListener(v -> {
                        usersDb.delete(DBHelper.TABLE_USERS, DBHelper.KEY_ID + " = " + idUser, null);
                        idUser = 0;
                        if(l % 2 == 0)
                            Toast.makeText(MainActivity.this, lang.messages[0], Toast.LENGTH_SHORT).show();
                        else Toast.makeText(MainActivity.this, lang.messages[1], Toast.LENGTH_SHORT).show();
                        initRes();
                        users.clear();
                        loadDb();
                        adapter.notifyDataSetChanged();
                        loadPage(l,p);
                });
                userList.setOnItemClickListener((parent, view, position, id) -> {
                    cursor.moveToPosition(position);
                    selected = position;
                    idUser= cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID));
                    checkValidId(remove,idUser);
                    checkValidId(update,idUser);
                });
                language.setOnClickListener(v -> {
                    l++;
                    idUser = 0;
                    loadPage(l,p);
                });
                break;
            case 1:
                addUser.setOnClickListener(v -> {
                    if(name.getText().length()>0 & !(emoji ==null) & exp.getText().length()>0 & messageCount.getText().length()>0) {
                        if (checkExpCount(exp, messageCount)) {
                            checker(String.valueOf(name.getText()),0);
                            if (!flag) {
                                makeContent();
                                usersDb.insert(DBHelper.TABLE_USERS, null, contentValues);
                                idUser = 0;
                                if (l % 2 == 0)
                                    Toast.makeText(MainActivity.this, lang.messages[4], Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(MainActivity.this, lang.messages[5], Toast.LENGTH_SHORT).show();
                                name.setText("");
                                exp.setText("");
                                messageCount.setText("");
                                emoji = null;
                                initRes();
                                users.clear();
                                loadDb();
                                adapter.notifyDataSetChanged();
                                p = 0;
                                loadPage(l, p);
                            } else if (l % 2 == 0) {
                                flag = false;
                                Toast.makeText(MainActivity.this, lang.messages[8], Toast.LENGTH_SHORT).show();
                            }
                            else {
                                flag = false;
                                Toast.makeText(MainActivity.this, lang.messages[9], Toast.LENGTH_SHORT).show();
                            }
                        }else
                            if(l % 2 ==0)
                                Toast.makeText(MainActivity.this,lang.messages[12], Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MainActivity.this,lang.messages[13], Toast.LENGTH_SHORT).show();
                    }
                    else
                    if (l % 2 == 0)
                        Toast.makeText(MainActivity.this, lang.messages[6],Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this,lang.messages[7],Toast.LENGTH_SHORT).show();
                });
                updateUser.setOnClickListener(v -> {
                    if(name.getText().length()>0 & !(emoji ==null) & exp.getText().length()>0 & messageCount.getText().length()>0) {
                        if (checkExpCount(exp, messageCount)) {
                            checker(String.valueOf(name.getText()),1);
                            if (!flag) {
                                makeContent();
                                if(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)).equals(String.valueOf(name.getText()))
                                        & cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SMILE)).equals(String.valueOf(emojiHelper.getEmoji(emoji)))
                                        & cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EXP)).equals(String.valueOf(exp.getText()))
                                        & cursor.getString(cursor.getColumnIndex(DBHelper.KEY_COUNT)).equals(String.valueOf(messageCount.getText()))) {
                                }else {
                                    if (l % 2 == 0)
                                        Toast.makeText(MainActivity.this, lang.messages[14], Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(MainActivity.this, lang.messages[15], Toast.LENGTH_SHORT).show();
                                }
                                usersDb.update(DBHelper.TABLE_USERS,contentValues,DBHelper.KEY_ID + " = " + idUser,null);
                                idUser = 0;
                                name.setText("");
                                exp.setText("");
                                messageCount.setText("");
                                emoji = null;
                                initRes();
                                users.clear();
                                loadDb();
                                adapter.notifyDataSetChanged();
                                p = 0;
                                loadPage(l, p);
                            } else if (l % 2 == 0) {
                                flag = false;
                                Toast.makeText(MainActivity.this, lang.messages[8], Toast.LENGTH_SHORT).show();
                            }
                            else{
                                flag = false;
                                Toast.makeText(MainActivity.this, lang.messages[9], Toast.LENGTH_SHORT).show();
                            }
                        }else
                        if(l % 2 ==0)
                            Toast.makeText(MainActivity.this,lang.messages[12], Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this,lang.messages[13], Toast.LENGTH_SHORT).show();
                    }
                    else
                        if (l % 2 == 0)
                            Toast.makeText(MainActivity.this, lang.messages[6],Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this,lang.messages[7],Toast.LENGTH_SHORT).show();
                });
                back.setOnClickListener(v -> {
                    p=0;
                    name.setText("");
                    exp.setText("");
                    messageCount.setText("");
                    idUser = 0;
                    loadPage(l,p);
                });
                smile.setOnClickListener(v -> {
                    String title = l % 2 ==0?lang.messages[10]:lang.messages[11];
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(title)
                            .setSingleChoiceItems(smileAdapter, 0, (dialog, which) -> {
                                switch (l % 2){
                                    case 0:
                                        smile.setText(emojiHelper.emotionsRus[which]+" "+new String(Character.toChars(emojiHelper.getEmoji(emojiHelper.emotionsRus[which]))));
                                        emoji = emojiHelper.emotionsRus[which];
                                        break;
                                    case 1:
                                        smile.setText(emojiHelper.emotions[which]+" "+new String(Character.toChars(emojiHelper.getEmoji(emojiHelper.emotions[which]))));
                                        emoji = emojiHelper.emotions[which];
                                        break;
                                }
                                dialog.dismiss();
                            }).create().show();
                });
                break;
            case 2:
                language.setOnClickListener(v -> {
                    l++;
                    loadPage(l,p);
                });
                logIn.setOnClickListener(v -> {
                    if (checkLogPass(login,password)){
                        if (login.getText().toString().equals(preferences.getString("login", "")) & password.getText().toString().equals(preferences.getString("password", ""))) {
                            p = 0;
                            loadPage(l, p);
                        } else {
                            if (count >= 0) {
                                switch (l % 2) {
                                    case 0:
                                        Toast.makeText(MainActivity.this, lang.messages[18] + count + lang.messages[20], Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(MainActivity.this, lang.messages[21] + count + lang.messages[23], Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                count--;
                            } else {
                                switch (l % 2) {
                                    case 0:
                                        Toast.makeText(MainActivity.this, lang.messages[19], Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(MainActivity.this, lang.messages[22], Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                logIn.setEnabled(false);
                                logIn.setAlpha((float) 0.5);
                            }
                        }
                    }else {
                        switch (l % 2){
                            case 0:
                                Toast.makeText(MainActivity.this, lang.messages[6], Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(MainActivity.this,lang.messages[7], Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                signUp.setOnClickListener(v -> {
                    login.setText("");
                    password.setText("");
                    p=3;
                    loadPage(l,p);
                });
                break;
            case 3:
                backToMain.setOnClickListener(v -> {
                    p=2;
                    loadPage(l,p);
                    fullName.setText("");
                    birthday.setText("");
                    eMail.setText("");
                    regLogin.setText("");
                    regPass.setText("");
                    repPass.setText("");
                });
                reg.setOnClickListener(v -> {
                    if(checkReg(regPass,repPass,regLogin,eMail,fullName,birthday) & isValidEmail(String.valueOf(eMail.getText())) & isValidDate(String.valueOf(birthday.getText()))){
                        switch (l % 2){
                            case 0:
                                Toast.makeText(this,lang.messages[16],Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(this,lang.messages[17],Toast.LENGTH_SHORT).show();
                                break;
                        }
                        p=2;
                        loadPage(l,p);
                    }
                });
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK & p==1) {
            name.setText("");
            exp.setText("");
            messageCount.setText("");
            p=0;
            loadPage(l,p);

            return true; }
        else
            super.onKeyDown(keyCode,event);
        return super.onKeyDown(keyCode, event);
    }

    private void initRes(){
        preferences = getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        usersDb = dbHelper.getWritableDatabase();
        cursor = usersDb.query(DBHelper.TABLE_USERS,null,null,null,null,null,null);
    }

    private void loadProgramData(){
        if(preferences.contains("lang")) l=preferences.getInt("lang",0);
    }

    private void saveProgramData(){
        preferences.edit().putInt("lang", l).apply();
        preferences.edit().putString("login","admin").apply();
        preferences.edit().putString("password","admin").apply();
    }

    private void loadDb(){
        if(cursor.moveToFirst()){
            do {
                User user = new User(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)),cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_SMILE)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EXP)),cursor.getString(cursor.getColumnIndex(DBHelper.KEY_COUNT)));
                users.add(user);
            }while (cursor.moveToNext());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadPage(int l, int p){
        switch (p){
            case 0:
                pageLogin.setVisibility(View.INVISIBLE);
                pageTwo.setVisibility(View.INVISIBLE);
                pageOne.setVisibility(View.VISIBLE);
                pageReg.setVisibility(View.INVISIBLE);
                userList = setViewList(R.id.userList,users);
                switch (l%2){
                    case 0:
                        language = setViewBtn(R.id.lang,lang.lang[p][0][3]);
                        add = setViewBtn(R.id.add,lang.lang[p][0][1]);
                        update = setViewBtn(R.id.update,lang.lang[p][0][5]);
                        remove = setViewBtn(R.id.remove,lang.lang[p][0][2]);
                        break;
                    case 1:
                        language = setViewBtn(R.id.lang,lang.lang[p][1][3]);
                        add = setViewBtn(R.id.add,lang.lang[p][1][1]);
                        update = setViewBtn(R.id.update,lang.lang[p][1][5]);
                        remove = setViewBtn(R.id.remove,lang.lang[p][1][2]);
                        break;
                }
                checkList();
                checkValidId(remove,idUser);
                checkValidId(update,idUser);
                setButtons(p);
                break;
            case 1:
                pageReg.setVisibility(View.INVISIBLE);
                pageLogin.setVisibility(View.INVISIBLE);
                pageTwo.setVisibility(View.VISIBLE);
                pageOne.setVisibility(View.INVISIBLE);
                switch (l%2){
                    case 0:
                        name = setViewEdit(R.id.userName,lang.lang[p][0][0]);
                        smile = setViewBtn(R.id.smile,lang.lang[p][0][1]);
                        setViewSmile(emojiHelper.emotionsRus);
                        exp = setViewEdit(R.id.exp,lang.lang[p][0][2]);
                        exp.setFilters(new InputFilter[]{new InputFilterMinMax("1","5000")});
                        messageCount = setViewEdit(R.id.messageCount,lang.lang[p][0][3]);
                        messageCount.setFilters(new InputFilter[]{new InputFilterMinMax("1","50000")});
                        addUser = setViewBtn(R.id.add_form,lang.lang[p][0][5]);
                        updateUser = setViewBtn(R.id.update_form,lang.lang[0][0][6]);
                        back = setViewBtn(R.id.back,lang.lang[p][0][4]);
                        break;
                    case 1:
                        name = setViewEdit(R.id.userName,lang.lang[p][1][0]);
                        smile = setViewBtn(R.id.smile,lang.lang[p][1][1]);
                        setViewSmile(emojiHelper.emotions);
                        exp = setViewEdit(R.id.exp,lang.lang[p][1][2]);
                        exp.setFilters(new InputFilter[]{new InputFilterMinMax("1","5000")});
                        messageCount = setViewEdit(R.id.messageCount,lang.lang[p][1][3]);
                        messageCount.setFilters(new InputFilter[]{new InputFilterMinMax("1","50000")});
                        addUser = setViewBtn(R.id.add_form,lang.lang[p][1][5]);
                        updateUser = setViewBtn(R.id.update_form,lang.lang[0][1][6]);
                        back = setViewBtn(R.id.back,lang.lang[p][1][4]);
                        break;
                }
                setButtons(p);
                break;
            case 2:
                pageReg = setViewPage(R.id.pageReg,View.INVISIBLE);
                pageOne = setViewPage(R.id.pageOne,View.INVISIBLE);
                pageTwo = setViewPage(R.id.pageTwo,View.INVISIBLE);
                pageLogin = setViewPage(R.id.pageLogin,View.VISIBLE);
                logo = setViewImage(R.id.logo);
                switch (l % 2){
                    case 0:
                        language = setViewBtn(R.id.langStart,lang.lang[0][0][3]);
                        welcome = setViewText(R.id.welcome,lang.messages[16]);
                        logIn = setViewBtn(R.id.enter,lang.lang[0][0][7]);
                        signUp = setViewBtn(R.id.reg,lang.lang[0][0][8]);
                        login = setViewEdit(R.id.login,lang.lang[1][0][7]);
                        password = setViewEdit(R.id.password,lang.lang[1][0][8]);
                        break;
                    case 1:
                        language = setViewBtn(R.id.langStart,lang.lang[0][1][3]);
                        welcome = setViewText(R.id.welcome,lang.messages[17]);
                        logIn = setViewBtn(R.id.enter,lang.lang[0][1][7]);
                        signUp = setViewBtn(R.id.reg,lang.lang[0][1][8]);
                        login = setViewEdit(R.id.login,lang.lang[1][1][7]);
                        password = setViewEdit(R.id.password,lang.lang[1][1][8]);
                        break;
                }
                setButtons(p);
                break;
            case 3:
                pageReg.setVisibility(View.VISIBLE);
                pageOne.setVisibility(View.INVISIBLE);
                pageTwo.setVisibility(View.INVISIBLE);
                pageLogin.setVisibility(View.INVISIBLE);
                switch (l % 2){
                    case 0:
                        reg = setViewBtn(R.id.registration,lang.lang[0][0][9]);
                        backToMain = setViewBtn(R.id.backToMain,lang.lang[1][0][4]);
                        fullName = setViewEdit(R.id.FIO,lang.registration[0]);
                        birthday = setViewEdit(R.id.birthday,lang.registration[2]);
                        eMail = setViewEdit(R.id.email,lang.registration[4]);
                        regLogin = setViewEdit(R.id.loginReg,lang.lang[1][0][7]);
                        regPass = setViewEdit(R.id.passReg,lang.lang[1][0][8]);
                        repPass = setViewEdit(R.id.repeatPass,lang.registration[6]);
                        break;
                    case 1:
                        reg = setViewBtn(R.id.registration,lang.lang[0][1][8]);
                        backToMain = setViewBtn(R.id.backToMain,lang.lang[1][1][4]);
                        fullName = setViewEdit(R.id.FIO,lang.registration[1]);
                        birthday = setViewEdit(R.id.birthday,lang.registration[3]);
                        eMail = setViewEdit(R.id.email,lang.registration[5]);
                        regLogin = setViewEdit(R.id.loginReg,lang.lang[1][1][7]);
                        regPass = setViewEdit(R.id.passReg,lang.lang[1][1][8]);
                        repPass = setViewEdit(R.id.repeatPass,lang.registration[7]);
                        break;
                }
                birthday.addTextChangedListener(new DateInputMask());
                setButtons(p);
                break;
        }
    }

    private ImageView setViewImage(int logo) {
        ImageView image = findViewById(logo);
        return image;
    }

    private LinearLayout setViewPage(int id, int visibility){
        LinearLayout linearLayout = findViewById(id);
        linearLayout.setVisibility(visibility);
        return linearLayout;
    }

    public Button setViewBtn(int id,String text){
        Button btn = findViewById(id);
        btn.setText(text);
        return btn;
    }
    public TextView setViewText(int id, String text){
        TextView textView = findViewById(id);
        textView.setText(text);
        return textView;
    }

    public EditText setViewEdit(int id, String text){
        EditText editText = findViewById(id);
        editText.setHint(text);
        return editText;
    }

    private void setViewSmile(String[] list){
       smileAdapter = new SmileAdapter(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));
    }


    private ListView setViewList(int id, ArrayList<User> list){
        ListView listView = findViewById(id);
        adapter = new UsersAdapter(this, android.R.layout.simple_list_item_1,list);
        adapter.setLang(l);
        listView.setAdapter(adapter);
        return listView;
    }

    @SuppressLint("SetTextI18n")
    private void setUpdatePage(){
        name.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)));
        emoji = emojiHelper.getEmoji(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SMILE))),l).toLowerCase();
        smile.setText(emoji+" "+new String(Character.toChars(emojiHelper.getEmoji(emoji))));
        exp.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EXP)));
        messageCount.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_COUNT)));
    }

    private void makeContent(){
        contentValues.put(DBHelper.KEY_NAME, name.getText().toString());
        contentValues.put(DBHelper.KEY_SMILE, checkSmile(emoji,l));
        contentValues.put(DBHelper.KEY_EXP, exp.getText().toString());
        contentValues.put(DBHelper.KEY_COUNT, messageCount.getText().toString());
    }

    private void anim(){
        logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.hide_anim));
        welcome.startAnimation(AnimationUtils.loadAnimation(this,R.anim.hide_anim));
        language.startAnimation(AnimationUtils.loadAnimation(this,R.anim.hide_anim));
        login.startAnimation(AnimationUtils.loadAnimation(this,R.anim.hide_anim));
        password.startAnimation(AnimationUtils.loadAnimation(this,R.anim.hide_anim));
        logIn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.hide_anim));
        signUp.startAnimation(AnimationUtils.loadAnimation(this,R.anim.hide_anim));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isValidDate(String data) {
        boolean result = true;
        try {
            Date date=new SimpleDateFormat("dd/MM/yyyy").parse(data);
            Date current = new Date();
            Date start = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1940");

            if(date.getTime()<start.getTime() | date.getTime()>current.getTime()) {
                result = false;
            }
        } catch (ParseException e) {
            result = false;
        }return result;
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        if(!Patterns.EMAIL_ADDRESS.matcher(target).matches()){
            switch (l % 2){
                case 0:
                    Toast.makeText(this,lang.messages[32],Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(this,lang.messages[33],Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checker(String name, int ua) {
        switch (ua) {
            case 0:
                for (int i = 0; i < users.size(); i++) {
                    if (name.equalsIgnoreCase(users.get(i).nickName)) {
                        flag = true;
                        return;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < users.size(); i++) {
                    if (name.equalsIgnoreCase(users.get(i).nickName)) {
                        if (selected != i) {
                            flag = true;
                            return;
                        }
                    }
                }
                break;
        }
    }

    private boolean checkReg(EditText regPass, EditText repPass, EditText regLogin, EditText eMail,EditText fullName,EditText birthday){
        boolean correct = true;

        if(fullName.getText().length()== 0 || birthday.getText().length() == 0 || eMail.getText().length() == 0 || regLogin.getText().length() == 0){
            switch (l % 2){
                case 0:
                    Toast.makeText(this,lang.messages[6],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
                case 1:
                    Toast.makeText(this,lang.messages[7],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
            }
            return correct;
        }
        if(regPass.getText().length()<10){
            switch (l % 2){
                case 0:
                    Toast.makeText(this,lang.messages[24],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
                case 1:
                    Toast.makeText(this,lang.messages[25],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
            }return correct;
        }
        if(String.valueOf(regPass.getText()).equalsIgnoreCase(String.valueOf(regLogin.getText()))){
            switch (l % 2){
                case 0:
                    Toast.makeText(this,lang.messages[26],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
                case 1:
                    Toast.makeText(this,lang.messages[27],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
            }return correct;
        }
        if(String.valueOf(regPass.getText()).equalsIgnoreCase(String.valueOf(eMail.getText()))){
            switch (l % 2){
                case 0:
                    Toast.makeText(this,lang.messages[28],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
                case 1:
                    Toast.makeText(this,lang.messages[29],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
            }return correct;
        }
        if(!(String.valueOf(regPass.getText()).equalsIgnoreCase(String.valueOf(repPass.getText())))){
            switch (l % 2){
                case 0:
                    Toast.makeText(this,lang.messages[30],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
                case 1:
                    Toast.makeText(this,lang.messages[31],Toast.LENGTH_SHORT).show();
                    correct = false;
                    break;
            }return correct;
        }
        return correct;
    }

    private void checkList(){
        if(users.size()==0){
            remove.setEnabled(false);
            remove.setAlpha((float) 0.5);
            switch (l % 2){
                case 0:
                    message = setViewText(R.id.message,lang.lang[0][0][4]);
                    break;
                case 1:
                    message = setViewText(R.id.message,lang.lang[0][1][4]);
                    break;
            }
        }else {
            remove.setEnabled(true);
            remove.setAlpha((float) 1.0);
            switch (l % 2){
                case 0:
                    message = setViewText(R.id.message,lang.lang[0][0][0]);
                    break;
                case 1:
                    message = setViewText(R.id.message,lang.lang[0][1][0]);
                    break;
            }
        }
    }

    private void checkStart(){
        if(preferences.getBoolean("first start",true)){
            anim();
            preferences.edit().putBoolean("first start",false).apply();
        }
    }

    boolean checkLogPass(EditText login, EditText password){
        if(login.getText().length()>0 & password.getText().length()>0){
            return true;
        }
        return false;
    }

    int checkSmile(String smile, int l) {
        for (int i = 0; i < emojiHelper.emotions.length; i++) {
            switch (l % 2) {
                case 0:
                    if (smile.equalsIgnoreCase(emojiHelper.emotionsRus[i])) {
                        return emojiHelper.getEmoji(smile);
                    }
                    break;
                case 1:
                    if (smile.equalsIgnoreCase(emojiHelper.emotions[i])) {
                        return emojiHelper.getEmoji(smile);
                    }
                    break;
            }
        }return emojiHelper.getEmoji(emojiHelper.emotions[0]);
    }

    void checkValidId(Button btn, int idUser){
        if(idUser==0) {
            btn.setEnabled(false);
            btn.setAlpha((float) 0.5);
        }else{
            btn.setEnabled(true);
            btn.setAlpha((float) 1.0);
        }
    }

    boolean checkExpCount(EditText exp, EditText messageCount){
        int expInt = Integer.parseInt(String.valueOf(exp.getText()));
        int messInt = Integer.parseInt(String.valueOf(messageCount.getText()));
        int MIN = 1;
        int eMAX = 5000;
        int mMAX = 50000;
        return expInt >= MIN & expInt <= eMAX & messInt>= MIN & messInt<= mMAX;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveProgramData();
        checkStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void recreate() {
        super.recreate();
    }
}