package com.example.pdfreaderapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pdfreaderapplication.entity.HistoryMarkEntity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryDeleteAdapter extends ArrayAdapter<HistoryMarkEntity> {
    Context context;
    ArrayList<HistoryMarkEntity> all_pdf;
    ViewHolder holder=new ViewHolder();
    public HistoryDeleteAdapter(Context context, ArrayList<HistoryMarkEntity> all_pdf) {
        super(context, R.layout.history_delete_layout, all_pdf);
        this.context=context;
        this.all_pdf=all_pdf;
    }

    @Override
    public int getCount() {
        return all_pdf.size();
    }

    @Nullable
    @Override
    public HistoryMarkEntity getItem(int position) {
        return all_pdf.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (all_pdf.size() > 0) {
            return all_pdf.size();
        } else {
            return 1;
        }
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder=new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.history_delete_layout, parent, false);
        }
        Database db=new Database(context);
        holder.textView =(TextView)view.findViewById(R.id.h_history_name);
        holder.textView.setText(all_pdf.get(position).getHistoryMarkFileName());
        holder.historyDate = (TextView) view.findViewById(R.id.h_historyDate);
        String ct = DateFormat.getDateInstance().format(new Date());
        holder.historyDate.setText(ct);
        holder.deleteImageView=(ImageView)view.findViewById(R.id.h_historydeleteImage);
        holder.checkBox=(CheckBox)view.findViewById(R.id.h_historycheckbox);
        holder.deleteImageView.setTag(new Integer(position));
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletehisData=position;
                Log.e("deleted ID",String.valueOf(deletehisData));
                AlertDialog.Builder alertDi = new AlertDialog.Builder(v.getContext());
                alertDi.setTitle("Deletion Confirmation " );
                alertDi.setMessage("Are you sure want to remove history from" + " " + all_pdf.get(position));
                alertDi.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean data=db.DeleteHistoryDataNew(all_pdf.get(position).getHistoryMarkFileName());
                        if(data==true)
                        {
                            Toast.makeText(context, "successfull history deletion", Toast.LENGTH_SHORT).show();
                            deleteHistoryItem(deletehisData);
                        }

                        else {
                            Toast.makeText(context, "Not deleted of history", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                alertDi.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog a=alertDi.create();
                a.show();
                Button positive=a.getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setTextColor(Color.BLACK);
                Button negative=a.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(Color.BLACK);
            }
        });
        return  view;
    }

    public class ViewHolder {
        TextView textView,historyDate;
        ImageView deleteImageView;
        CheckBox checkBox;
    }

    public void deleteHistoryItem(int id) {
        all_pdf.remove(id);
        notifyDataSetChanged();
    }
}
