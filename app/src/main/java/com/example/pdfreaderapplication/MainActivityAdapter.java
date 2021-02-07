package com.example.pdfreaderapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pdfreaderapplication.entity.HistoryMarkEntity;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivityAdapter extends ArrayAdapter<File> implements Filterable {
    Context context;
    ViewHolder viewHolder=new ViewHolder();
    ArrayList<File> al_pdf;
    ArrayList<File> itemsModelListFiltered=null;

    public MainActivityAdapter(Context context, ArrayList<File> itemsModelListFiltered) {
        super(context, R.layout.custom_main_layout, itemsModelListFiltered);
        this.context=context;
        this.itemsModelListFiltered=itemsModelListFiltered;
        this.al_pdf=new ArrayList<File>();
        this.al_pdf.addAll(itemsModelListFiltered);
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if(itemsModelListFiltered!=null){
            return  itemsModelListFiltered.size();
        }
        return 0;
    }

    @Nullable
    @Override
    public File getItem(int position) {
        return itemsModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (itemsModelListFiltered.size() > 0) {
            return itemsModelListFiltered.size();
        } else {
            return 1;
        }
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.custom_main_layout, parent, false);
        }
        Database db = new Database(context);
        viewHolder.tv_filename =(TextView)view.findViewById(R.id.tv_name);
       //viewHolder.tv_filename.setText(al_pdf.get(position).getName());
        if(viewHolder.tv_filename!=null && position<itemsModelListFiltered.size()) {
            viewHolder.tv_filename.setText(itemsModelListFiltered.get(position).getName());
        }
        viewHolder.frontDate = (TextView) view.findViewById(R.id.frontDate);
        String ct = DateFormat.getDateInstance().format(new Date());
        viewHolder.frontDate.setText(ct);
        viewHolder.image_dotted=(ImageView)view.findViewById(R.id.frontMorevertImage);
        viewHolder.image_dotted.setTag(new Integer(position));
        viewHolder.image_dotted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,v);
                popup.getMenuInflater().inflate(R.menu.morevert_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.View:
                                Intent intent = new Intent(getContext(), Dashboard.class);
                                intent.putExtra("position", position);
                                context.startActivity(intent);
                                return true;
                            case R.id.Delete:
                                int deleteID = position;
                                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                dialog.setTitle("Deletion Confirmation");
                                dialog.setMessage("Are you sure you want to delete " + " " + al_pdf.get(position));
                                dialog .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                               boolean frontData = db.DeleteHistoryData(deleteID);
                                                if (frontData == true) {
                                                    Toast.makeText(context, "Front deleted", Toast.LENGTH_SHORT).show();
                                                    //deletefrontItem(deleteID);
                                                    Intent intentPos=new Intent(context,MainActivity.class);
                                                    intentPos.putExtra("INTENTPOS",deleteID);
                                                    context.startActivity(intentPos);
                                                } else {
                                                    Toast.makeText(context, "front not deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog a=dialog.create();
                                a.show();
                                Button positive=a.getButton(DialogInterface.BUTTON_POSITIVE);
                                positive.setTextColor(Color.BLACK);
                                Button negative=a.getButton(DialogInterface.BUTTON_NEGATIVE);
                                negative.setTextColor(Color.BLACK);
                                return true;
                            case R.id.morevertShare:
                                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+al_pdf.get(position)));
                               //shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here");
                                //Command for share application.
                                //String app_url = "https://play.google.com/store/apps/details?id=com.example.pdfreaderapplication";
                                //shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                                context.startActivity(Intent.createChooser(shareIntent, "Share the file"));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
        return view;
    }
    public class ViewHolder {
        TextView tv_filename,frontDate;
        ImageView image_dotted;
    }
    public void deletefrontItem(int id) {
        al_pdf.remove(id);
        notifyDataSetChanged();
    }
    //@NonNull
  //  @Override
  /*  public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = al_pdf.size();
                    filterResults.values = al_pdf;
                }
                else{
                    List<File> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();
                    for(File itemModel:al_pdf){
                        if(itemModel.getName().contains(searchStr)){
                            resultsModel.add(itemModel);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsModelListFiltered = (ArrayList<File>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }*/
    public void filterData(String charText){
        charText=charText.toLowerCase(Locale.getDefault());
        itemsModelListFiltered.clear();
        if(charText.length()==0){
            itemsModelListFiltered.addAll(al_pdf);
        }else {
            for (File file:al_pdf){
                if(file.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    itemsModelListFiltered.add(file);
                }
            }
        }
        notifyDataSetChanged();
    }
}