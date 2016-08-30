package com.app.precared.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.precared.R;
import com.app.precared.activities.SellerActivity;
import com.app.precared.models.NotificationRow;
import com.app.precared.models.State;

import java.util.List;

/**
 * Created by prashant on 13/7/16.
 */
public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.StateHolder> {
    private final static String TAG = StateListAdapter.class.getSimpleName();
    private Context mContext;
    private List<State> mStateList;

    public StateListAdapter(Context context, List<State> notificationList) {
        this.mStateList = notificationList;
        this.mContext = context;
    }

    @Override
    public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_custom_row, parent, false);
        return new StateHolder(rootView);
    }

    @Override
    public void onBindViewHolder(StateHolder holder, int position) {
        State state = mStateList.get(position);
        holder.count.setText(""+state.count);
        holder.stateName.setText(""+state.name);
    }

    @Override
    public int getItemCount() {
        return  mStateList.size();
    }

    /**
     * class to hold ticket view
     */
    class StateHolder extends RecyclerView.ViewHolder {
        private TextView count, stateName;
        public StateHolder(View itemView) {
            super(itemView);
            count = (TextView) itemView.findViewById(R.id.count);
            stateName = (TextView) itemView.findViewById(R.id.stateName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mContext instanceof SellerActivity){
                        ((SellerActivity)mContext).executeSellerRequest(mStateList.get(getAdapterPosition()).stateName);
                    }
                }
            });
        }
    }
}
