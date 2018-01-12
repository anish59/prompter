package bbt.com.prompter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import bbt.com.iconiccardview.IconicCardView;
import bbt.com.prompter.R;

/**
 * Created by anish on 09-01-2018.
 */

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.MyViewHolder> {

    private Context context;

    public TemplateAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.msg_template, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.contact_view_item, null, false);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
        TextView txtNumber = (TextView) view.findViewById(R.id.txtNumber);
        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        holder.iconicitem.addInsideView(view);
        txtName.setText("Clara");
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        IconicCardView iconicitem;

        public MyViewHolder(View itemView) {
            super(itemView);
            iconicitem = (IconicCardView) itemView.findViewById(R.id.iconic_item);
        }
    }
}
