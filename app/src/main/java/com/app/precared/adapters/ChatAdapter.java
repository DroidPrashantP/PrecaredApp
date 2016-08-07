package com.app.precared.adapters;

import android.content.Context;
import android.provider.SyncStateContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.precared.R;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.MyChats;
import com.app.precared.utils.PrecaredSharePreferences;

import java.util.List;

/**
 * Created by prashant on 13/7/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.TicketViewHolder> {
    private final static String TAG = ChatAdapter.class.getSimpleName();
    private Context mContext;
    private List<MyChats> mTicketsList;
    private PrecaredSharePreferences mPrecaredSharePreferences;

    public ChatAdapter(Context context, List<MyChats> ticketsList) {
        this.mTicketsList = ticketsList;
        this.mContext = context;
        mPrecaredSharePreferences = new PrecaredSharePreferences(mContext);
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_item, parent, false);
        return new TicketViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
        Log.e("pos", ""+position);
        Log.e("size", ""+mTicketsList.size());
        MyChats mychats = mTicketsList.get((mTicketsList.size()-1)- position);
        holder.body.setText(mychats.message);
        Log.e("UserID", ""+mPrecaredSharePreferences.getUserId());
        if (mPrecaredSharePreferences.getUserId() == mychats.sender_id) {
            holder.name.setText("By " + mychats.recevier_name);
            Log.e("sender", ""+mychats.recevier_name);
        }else {
            holder.name.setText("By " + mychats.sender_name);
            Log.e("receiver", ""+mychats.sender_name);
        }

        setChatLayout(holder, mPrecaredSharePreferences.getUserId().equalsIgnoreCase(mychats.sender_id), mychats);
    }

//    /***
//     * set attachment layout
//     */
//    private void setAttachmentLayout(final TicketViewHolder holder, mychats mychats) {
//        if (holder.attachmentLayout != null) {
//            (holder.attachmentLayout).removeAllViews();
//        }
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        for (int i = 0; i < mychats.attachmentList.size(); i++) {
//            final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_attachments, holder.attachmentLayout, false);
//            if (Constants.mychatsKeys.USER.equalsIgnoreCase(mychats.repliedBy)) {
//                params.setMargins(0, 0, 15, 0);
//            } else {
//                params.setMargins(15, 0, 0, 0);
//
//            }
//            view.setLayoutParams(params);
//            TextView attachName = (TextView) view.findViewById(R.id.attachNameTextView);
//            TextView attachSize = (TextView) view.findViewById(R.id.attachSizeTextView);
//            attachName.setText(mychats.attachmentList.get(i).contentFileName);
//            int fileSize = Integer.parseInt(mychats.attachmentList.get(i).contentFileSize);
//            attachSize.setText(Utils.getFileSize(fileSize));
//            final String url = mychats.attachmentList.get(i).attachmentUrl;
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Utils.openUrlToBrowser(url, mContext);
//                }
//            });
//            view.setTag(holder);
//            if (holder.attachmentLayout.getChildAt(i) == null)
//                holder.attachmentLayout.addView(view);
//            else
//                (holder.attachmentLayout).removeView(view);
//        }
//    }

    /**
     * set chat layout left or right
     */
    private void setChatLayout(TicketViewHolder holder, boolean isMe, MyChats mychats) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        if (isMe) {
            bodyParams.setMargins(0, 0, 5, 0);
            params.gravity = Gravity.END;
            holder.bodyLayout.setLayoutParams(params);
            holder.tableRow.setLayoutParams(params);
            holder.bodyLayout.setBackgroundResource(R.drawable.ic_me_bubble);
            holder.body.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
            holder.viewLeft.setVisibility(View.VISIBLE);
            holder.viewRight.setVisibility(View.GONE);

        } else {
            bodyParams.setMargins(5, 0, 0, 0);
            params.gravity = Gravity.START;
            holder.bodyLayout.setLayoutParams(params);
            holder.tableRow.setLayoutParams(params);
            holder.bodyLayout.setBackgroundResource(R.drawable.ic_support_bubble);
            holder.body.setTextColor(mContext.getResources().getColor(R.color.text_white));
            holder.viewRight.setVisibility(View.VISIBLE);
            holder.viewLeft.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mTicketsList.size();
    }

    /**
     * class to hold ticket view
     */
    class TicketViewHolder extends RecyclerView.ViewHolder {
        private TextView name, body, createdAt;
        private TableRow tableRow;
        private View viewLeft, viewRight;
        private LinearLayout bodyLayout, attachmentLayout;

        public TicketViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            body = (TextView) itemView.findViewById(R.id.body);
            createdAt = (TextView) itemView.findViewById(R.id.createdAtTextView);
            tableRow = (TableRow) itemView.findViewById(R.id.tableRow);
            viewLeft = itemView.findViewById(R.id.viewLeft);
            viewRight = itemView.findViewById(R.id.viewRight);
            bodyLayout = (LinearLayout) itemView.findViewById(R.id.bodyLayout);
            attachmentLayout = (LinearLayout) itemView.findViewById(R.id.attachmentLayout);
        }
    }
}
