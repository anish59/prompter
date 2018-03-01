package bbt.com.prompter.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import bbt.com.iconiccardview.widgets.CircleImageView;
import bbt.com.prompter.R;
import bbt.com.prompter.model.ContactsModel;

/**
 * Created by anish on 10-01-2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<ContactsModel> originalContactList;
    private List<ContactsModel> contactListItems;
    private ContactClickListener contactClickListener;

    public ContactAdapter(Context context, ContactClickListener contactClickListener) {
        this.context = context;
        this.contactClickListener = contactClickListener;
    }

    public void setAndNotifyItems(List<ContactsModel> contactsList) {
        this.originalContactList = new ArrayList<>();
        this.originalContactList = contactsList;
        this.contactListItems = contactsList;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtContactName.setText(contactListItems.get(position).getName());
        holder.txtContactNumber.setText(contactListItems.get(position).getNumber());
        Glide.with(context).load(contactListItems.get(position).getImgUri())
                .apply(new RequestOptions().placeholder(R.drawable.ic_avatar).error(R.drawable.ic_avatar))
                .thumbnail(0.5f)
                .into(holder.contactImage);

        final int pos = position;
        holder.txtContactName.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactClickListener.onItemClicked(contactListItems.get(pos).getName()
                        , contactListItems.get(pos).getNumber()
                        , contactListItems.get(position).getImgUri());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (contactListItems != null && contactListItems.size() > 0) ? contactListItems.size() : 0;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView contactImage;
        TextView txtContactName;
        TextView txtContactNumber;
        CardView cardHolder;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardHolder = (CardView) itemView.findViewById(R.id.cardHolder);
            txtContactNumber = (TextView) itemView.findViewById(R.id.txtContactNumber);
            txtContactName = (TextView) itemView.findViewById(R.id.txtContactName);
            contactImage = (CircleImageView) itemView.findViewById(R.id.contactImage);
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<ContactsModel> filteredContactsList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = originalContactList.size();
                    filterResults.values = originalContactList;
                } else {
                    constraint = constraint.toString().toLowerCase();

                    for (ContactsModel contactsModel : originalContactList) {
                        if (contactsModel.getName().toLowerCase().contains(constraint)) {
                            filteredContactsList.add(contactsModel);
                        }
                    }

                    filterResults.count = filteredContactsList.size();
                    filterResults.values = filteredContactsList;

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactListItems = new ArrayList<>();
                contactListItems = (List<ContactsModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    public interface ContactClickListener {
        void onItemClicked(String name, String phoneNo,String imgUri);
    }

}
