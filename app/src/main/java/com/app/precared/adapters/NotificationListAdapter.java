package com.app.precared.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.precared.R;
import com.app.precared.activities.SellerActivity;
import com.app.precared.models.NotificationRow;
import com.app.precared.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prashant on 13/7/16.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder> {
    private final static String TAG = NotificationListAdapter.class.getSimpleName();
    private Context mContext;
    private List<NotificationRow> mNotificationList;

    public NotificationListAdapter(Context context, List<NotificationRow> notificationList) {
        this.mNotificationList = notificationList;
        this.mContext = context;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_custom_notification_row, parent, false);
        return new NotificationViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationRow notificationRow = mNotificationList.get(position);
        holder.title.setText(""+notificationRow.title);
        holder.subtitle.setText(""+notificationRow.desc);
        holder.dateTextView.setText(""+notificationRow.dateText);
    }

    @Override
    public int getItemCount() {
        return  mNotificationList.size();
    }

    /**
     * class to hold ticket view
     */
    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subtitle,dateTextView;
        public NotificationViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subTitle);
            dateTextView = (TextView) itemView.findViewById(R.id.dateText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, SellerActivity.class).putExtra("sub_type",""+mNotificationList.get(getAdapterPosition()).subType));
                }
            });
        }
    }
}
