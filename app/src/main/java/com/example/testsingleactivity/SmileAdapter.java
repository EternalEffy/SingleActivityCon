package com.example.testsingleactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

public class SmileAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> smiles;
    private EmojiHelper emojiHelper = new EmojiHelper();

    public SmileAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);

        this.context = context;
        this.smiles = objects;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View getView(int position, View convertView, ViewGroup parent) {
        String smile = smiles.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.user, null);

        TextView userView = (TextView) view.findViewById(R.id.item);

        userView.setText(formattedSmile(smile));
        return view;
    }

    private SpannableString formattedSmile(String smile){
        String fullSmile = smile + " " + new String(Character.toChars(emojiHelper.getEmoji(smile)));
        SpannableString formattedSmile = new SpannableString(fullSmile);
        return formattedSmile;
    }
}
