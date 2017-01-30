package com.example.teodora.employeetaskmanager.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.teodora.employeetaskmanager.Models.ContactModel;
import com.example.teodora.employeetaskmanager.R;
import java.util.List;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.MyViewHolder> {

    private List<ContactModel> contactModelList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView contactUserName;

        public MyViewHolder(View view) {
            super(view);
            contactUserName = (TextView) view.findViewById(R.id.txtView);

        }
    }

    public ContactsRecyclerViewAdapter(Context context, List<ContactModel> contactInfosList) {
        this.contactModelList = contactInfosList;
        this.mContext = context;
    }

    @Override
    public ContactsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_model, parent, false);

        return new ContactsRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactsRecyclerViewAdapter.MyViewHolder holder, int position) {
        ContactModel contactModel = contactModelList.get(position);
        holder.contactUserName.setText(contactModel.getName());
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }
}
