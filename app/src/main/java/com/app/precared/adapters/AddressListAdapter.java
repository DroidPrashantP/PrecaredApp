package com.app.precared.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.precared.R;
import com.app.precared.activities.SellerActivity;
import com.app.precared.models.Address;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;
import com.app.precared.utils.Utils;

import java.util.List;

/**
 * Created by prashant on 13/7/16.
 */
public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.NotificationViewHolder> {
    private final static String TAG = AddressListAdapter.class.getSimpleName();
    private Context mContext;
    private List<Address> mAddressList;
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private int addressId ;

    public AddressListAdapter(Context context, List<Address> notificationList) {
        this.mAddressList = notificationList;
        this.mContext = context;
        mPrecaredSharePreferences = new PrecaredSharePreferences(context);
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_custom_row, parent, false);
        return new NotificationViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        final Address address = mAddressList.get(position);
        holder.name.setText(""+mPrecaredSharePreferences.getName());
        String completeAddress="";
        if (StringUtils.isNotEmpty(address.line1)){
            completeAddress = address.line1;
        }if (StringUtils.isNotEmpty(address.line2)){
            completeAddress = completeAddress+"\n"+address.line2;
        }
        if (StringUtils.isNotEmpty(address.city)){
            completeAddress = completeAddress+"\n"+address.city;
        }
        if (StringUtils.isNotEmpty(address.state)){
            completeAddress = completeAddress+"\n"+address.state;
        }
        holder.address.setText(completeAddress+"-"+address.pincode);
        holder.number.setText(""+address.mobile_no);
        holder.checkBox.setChecked(address.defaultSelection);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    addressId = address.id;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public String getAddressID() {
        return ""+addressId;
    }

    /**
     * class to hold ticket view
     */
    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView name, address, number;
        private CheckBox checkBox;
        public NotificationViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            number = (TextView) itemView.findViewById(R.id.mobileNumber);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

//            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//            Display display = wm.getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            int width = size.x * 85 / 100;
//            CardView.LayoutParams layoutParams = new CardView.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(Utils.dpToPx(8), Utils.dpToPx(10), Utils.dpToPx(8), Utils.dpToPx(10));
//            itemView.setLayoutParams(layoutParams);

        }
    }
}
