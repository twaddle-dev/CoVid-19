package com.twaddle.covid19.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twaddle.covid19.R;
import com.twaddle.covid19.model.Elder;

import java.util.ArrayList;
import java.util.List;

public class EldersListAdapter extends RecyclerView.Adapter<EldersListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Elder> eldersList = new ArrayList<>();
    private static final String TAG = "EldersListAdapter";
    public EldersListAdapter(ArrayList<Elder> eldersList , Context context) {
        this.eldersList = eldersList;
        this.context = context;
    }

    @NonNull
    @Override
    public EldersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemElder = LayoutInflater.from(context).inflate( R.layout.list_item_elder , parent , false );
        return new ViewHolder( listItemElder );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Elder elder = eldersList.get( position );
        holder.tv_elderItem.setText( elder.getItem() );
        holder.tv_elderAddress.setText( elder.getAddress() );
        holder.tv_elderName.setText( elder.getName() );
        holder.btn_deny.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eldersList.remove( position );
                notifyItemRemoved( position );
                notifyItemRangeChanged( position , eldersList.size() );
                notifyDataSetChanged();
                Log.i( TAG, "onClick: deny" );
            }
        } );
        holder.btn_accept.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eldersList.remove( position );
                notifyItemRemoved( position );
                notifyItemRangeChanged( position , eldersList.size() );
                notifyDataSetChanged();
                Log.i( TAG, "onClick: accept" );

            }
        } );
    }

    @Override
    public int getItemCount() {
        return eldersList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_elderName;
        public TextView tv_elderAddress;
        public TextView tv_elderItem;
        public Button btn_accept;
        public Button btn_deny;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            tv_elderItem = itemView.findViewById( R.id.elder_item );
            tv_elderName = itemView.findViewById( R.id.elder_name );
            tv_elderAddress = itemView.findViewById( R.id.elder_address );
            btn_accept = itemView.findViewById( R.id.btn_accept_help );
            btn_deny = itemView.findViewById( R.id.btn_deny_help );
        }
    }
}
