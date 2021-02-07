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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdfreaderapplication.entity.BookMarkEntity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookmarkDeleteAdapter extends ArrayAdapter<BookMarkEntity> {
    Context context;
    ArrayList<BookMarkEntity> all_book_pdf;
    public BookmarkDeleteAdapter(Context context, ArrayList<BookMarkEntity> all_book_pdf) {
        super(context, R.layout.bookmark_delete_layout, all_book_pdf);
        this.context=context;
        this.all_book_pdf=all_book_pdf;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (all_book_pdf.size() > 0) {
            return all_book_pdf.size();
        } else {
            return 1;
        }
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.bookmark_delete_layout, parent, false);
        }
        DatabaseBookmark db=new DatabaseBookmark(context);
        viewHolder.tv_historyname =(TextView)view.findViewById(R.id.b_bookmark_name);
        viewHolder.tv_historyname.setText(all_book_pdf.get(position).getBookMarkFileName());
        viewHolder.bookmarkDate = (TextView) view.findViewById(R.id.b_bookmarkDate);
        String ct = DateFormat.getDateInstance().format(new Date());
        viewHolder.bookmarkDate.setText(ct);
        viewHolder.image_historyview=(ImageView)view.findViewById(R.id.b_bookmarkdeleteImage);
        viewHolder.image_historyview.setTag(new Integer(position));
        viewHolder.image_historyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletedId=position;
                Log.e("deleted ID",String.valueOf(deletedId));
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Deletion Confirmation " );
                alert.setMessage("Are you sure want to remove bookmark from" + " " + all_book_pdf.get(position));
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean data=db.DeleteHBookmarkDataNew(all_book_pdf.get(position).getBookMarkFileName());
                        if(data==true)
                        {
                            Toast.makeText(context, "successfull deletion", Toast.LENGTH_SHORT).show();
                            deleteItem(deletedId);
                        }
                        else {
                            Toast.makeText(context, "Not deleted", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                alert .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog a=alert.create();
                a.show();
                Button positive=a.getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setTextColor(Color.BLACK);
                Button negative=a.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(Color.BLACK);
            }
        });
        return view;
    }
    public class ViewHolder {
        TextView tv_historyname,bookmarkDate;
        ImageView image_historyview;
    }
    public void deleteItem(int id) {
        all_book_pdf.remove(id);
        notifyDataSetChanged();
    }
}
