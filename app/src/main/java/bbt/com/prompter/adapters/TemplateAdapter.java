package bbt.com.prompter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bbt.com.iconiccardview.IconicCardView;
import bbt.com.prompter.R;
import bbt.com.prompter.model.ContactTable;

/**
 * Created by anish on 09-01-2018.
 */

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.MyViewHolder> {

    private Context context;
    private List<ContactTable> templateList;
    private OnEditClickedListener onEditClickedListener;

    public TemplateAdapter(Context context, OnEditClickedListener onEditClickedListener) {
        this.context = context;
        this.onEditClickedListener = onEditClickedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.msg_template, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_contact_view, null, false);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
        TextView txtNumber = (TextView) view.findViewById(R.id.txtNumber);
        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        holder.iconicitem.addInsideView(view);

        txtName.setText(templateList.get(position).getName());
        txtNumber.setText(templateList.get(position).getNumber());
        txtMsg.setText(templateList.get(position).getTemplate());
        if (templateList.get(position).getImgUri() != null) {
            holder.iconicitem.setIconicImage(templateList.get(position).getImgUri());
        }

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClickedListener.onEdit(templateList.get(position).getNumber());
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClickedListener.onDelete(templateList.get(position).getContactId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (templateList != null && templateList.size() > 0) ? templateList.size() : 0;
    }

    public void setItems(List<ContactTable> templateList) {
        this.templateList = new ArrayList<>();
        this.templateList = templateList;
        notifyDataSetChanged();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        IconicCardView iconicitem;

        public MyViewHolder(View itemView) {
            super(itemView);
            iconicitem = (IconicCardView) itemView.findViewById(R.id.iconic_item);
        }
    }

    public interface OnEditClickedListener {
        void onEdit(String phoneNo);

        void onDelete(long contactTableId);
    }
}
