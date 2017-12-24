package com.example.visak.assignment2;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by visak on 2017-10-03.
 */

public class CustomGridAdapter extends BaseAdapter{

    private Context context;
    LayoutInflater layoutInflater;
    public ArrayList<String> solutionsList;
    public HashMap<String,Integer> colorList = new HashMap<String, Integer>();
    public HashMap<String,Integer> getColorList(){
        colorList.put("0",Color.GRAY);
        colorList.put("1",Color.BLUE);
        colorList.put("2",Color.YELLOW);
        colorList.put("3",Color.MAGENTA);
        colorList.put("4",Color.GREEN);
        colorList.put("5",Color.GREEN);
        colorList.put("6",Color.GREEN);
        colorList.put("7",Color.RED);
        colorList.put("8",Color.RED);
        colorList.put("F",Color.MAGENTA);
        colorList.put("W",Color.RED);
        colorList.put("*",Color.RED);
        colorList.put(" ",Color.WHITE);
        return colorList;
    }


    public CustomGridAdapter(Context context, ArrayList<String> solutionsList){
        this.context = context;
        this.solutionsList = solutionsList;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return solutionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.grid, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.value);
        textView.setTextColor(getColorList().get(solutionsList.get(position)));
//        if (solutionsList.get(position).equals("0")){
//            Log.d("Issue",""+position);
//            textView.setBackgroundColor(Color.GRAY);
//        }
        if (solutionsList.get(position).equals(" ")){
            textView.setBackgroundResource(R.drawable.tileimage);
        }
        else if(solutionsList.get(position).equals("*")){
            textView.setBackgroundResource(R.drawable.bomb);
        }
        else if(solutionsList.get(position).equals("F")){
            textView.setBackgroundResource(R.drawable.flag);
        }
        else if(solutionsList.get(position).equals("W")){
            textView.setBackgroundResource(R.drawable.crossedbomb);
        }
        else {
            textView.setBackgroundResource(R.drawable.open);
        }
        if(!((solutionsList.get(position).equals("0"))||(solutionsList.get(position).equals("*"))||solutionsList.get(position).equals("F")||solutionsList.get(position).equals("W"))){
            textView.setText(solutionsList.get(position));
        }
        return convertView;
    }
}
