package com.example.himanshijain.callback;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Himanshi Jain on 2/28/2016.
 */
public class ContactsAdapter  extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    ArrayList<Contact> data;
    Context context;
    TextView name,number;
    ImageView delete,call;
    ContactsListHelper contactsListHelper;

    ContactsAdapter(Context context,ArrayList<Contact> data){
        this.context=context;
        this.data=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        contactsListHelper=new ContactsListHelper(context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final int j=i;
           name=(TextView)viewHolder.v.findViewById(R.id.contact_name);
           number=(TextView)viewHolder.v.findViewById(R.id.contact_number);
        name.setText(data.get(i).name);
        number.setText(data.get(i).phone_no);
        delete=(ImageView)viewHolder.v.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsListHelper.deleteContact(data.get(j).id);
                data.remove(j);
                notifyDataSetChanged();
            }
        });
        call=(ImageView)viewHolder.v.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" + data.get(j).phone_no));
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {

        View v;
        public ViewHolder(View itemView) {
            super(itemView);
            v=itemView;
        }
    }
}
