package com.example.testsingleactivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User>{

    private Context context;
    private List<User> users;
    private int l;
    private Languages lang = new Languages();

    public UsersAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);

        this.context = context;
        this.users = objects;
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View getView(int position, View convertView, ViewGroup parent) {
        User userFromList = users.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.user, null);

        TextView userView = (TextView) view.findViewById(R.id.item);

        userView.setText(formattedUser(userFromList, l));
        return view;
    }

    private SpannableString formattedUser(User user, int l){
        String formatted;
        SpannableString formattedUser = null;
        switch (l % 2){
            case 0:
                int idxStartExp = lang.lang[1][0][0].length()+2+user.getNickName().length()+
                        lang.lang[1][0][6].length()+2+new String(Character.toChars(user.getSmile())).length()+
                        lang.lang[1][0][2].substring(0,4).length()+4;
                int idxStartCount = idxStartExp+user.getExp().length()+lang.lang[1][0][3].substring(7,16).length()+3;

                formatted = lang.lang[1][0][0] +": "+ user.getNickName()+
                        "\n"+lang.lang[1][0][6]+": "+ new String(Character.toChars(user.getSmile()))+
                        "\n"+lang.lang[1][0][2].substring(0,4)+": "+user.getExp()+
                        "\n"+lang.lang[1][0][3].substring(7,16)+": "+user.getMessageCount();
                formattedUser = new SpannableString(formatted);
                formattedUser.setSpan(new StyleSpan(Typeface.BOLD),lang.lang[1][0][0].length(),lang.lang[1][0][0].length()+2 + user.getNickName().length(), 0);
                formattedUser.setSpan(new StyleSpan(Typeface.BOLD),idxStartExp,idxStartExp+user.getExp().length(),0);
                formattedUser.setSpan(new StyleSpan(Typeface.BOLD),idxStartCount,idxStartCount+user.getMessageCount().length(),0);
                break;
            case 1:
                int ixStartExp = lang.lang[1][1][0].length()+2+user.getNickName().length()+
                        lang.lang[1][1][6].length()+2+new String(Character.toChars(user.getSmile())).length()+
                        lang.lang[1][1][2].substring(0,4).length()+3;
                int ixStartCount = ixStartExp+user.getExp().length()+lang.lang[1][1][3].substring(8,13).length()+3;

                formatted = lang.lang[1][1][0] +": "+ user.getNickName()+
                        "\n"+lang.lang[1][1][6]+": "+ new String(Character.toChars(user.getSmile()))+
                        "\n"+lang.lang[1][1][2].substring(0,3)+": "+user.getExp()+
                        "\n"+lang.lang[1][1][3].substring(8,13)+": "+user.getMessageCount();
                formattedUser = new SpannableString(formatted);
                formattedUser.setSpan(new StyleSpan(Typeface.BOLD),lang.lang[1][1][0].length(),lang.lang[1][1][0].length()+2 + user.getNickName().length(), 0);
                formattedUser.setSpan(new StyleSpan(Typeface.BOLD),ixStartExp,ixStartExp+user.getExp().length(),0);
                formattedUser.setSpan(new StyleSpan(Typeface.BOLD),ixStartCount,ixStartCount+user.getMessageCount().length(),0);
                break;
        }
        return formattedUser;
    }

    public void setLang(int l) {
        this.l = l;
    }
}


