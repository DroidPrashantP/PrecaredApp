package com.app.precared.adapters;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.precared.R;
import com.app.precared.activities.ChatActivity;
import com.app.precared.activities.SellerActivity;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Seller;
import com.app.precared.utils.StateProgressBar;
import com.app.precared.utils.NetworkManager;
import com.app.precared.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prashant on 13/7/16.
 */
public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {
    private final static String TAG = SellerAdapter.class.getSimpleName();
    private Context mContext;
    private List<Seller> mSellerList;
    String[] descriptionData = {"New", "Accept", "Pickup", "Received", "Ready", "Live", "Booked","Sold", "Paid"};
    ArrayList descriptionDataFromServer = new ArrayList();

    public SellerAdapter(Context context, List<Seller> sellerList) {
        this.mSellerList = sellerList;
        this.mContext = context;
        descriptionDataFromServer.add(0,"New");
        descriptionDataFromServer.add(1,"Accept");
        descriptionDataFromServer.add(2,"Pickup");
        descriptionDataFromServer.add(3,"Received");
        descriptionDataFromServer.add(4,"Ready");
        descriptionDataFromServer.add(5,"On hold");
        descriptionDataFromServer.add(6,"Booked");
        descriptionDataFromServer.add(7,"Sold");
        descriptionDataFromServer.add(8,"Paid");


    }

    @Override
    public SellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_custom_seller_row, parent, false);
        return new SellerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final SellerViewHolder holder, int position) {
        final Seller seller = mSellerList.get(position);
        holder.productName.setText(seller.name);
        holder.productID.setText(""+seller.id);
        holder.productStatus.setText(""+seller.display_state);
        holder.productViewCount.setText(""+seller.view_count);
        if (StringUtils.isNotEmpty(seller.myPrice)) {
            holder.priceRL.setVisibility(View.VISIBLE);
            holder.productAmount.setText("Rs "+seller.myPrice);
        }else {
            holder.productAmount.setText("");
            holder.priceRL.setVisibility(View.GONE);
        }
        if (StringUtils.isNotEmpty(seller.image_url)) {
              Picasso.with(mContext).load(seller.image_url).placeholder(R.drawable.place_product).into(holder.productImage);
        }else {
            holder.productImage.setImageResource(R.drawable.place_product);
        }

        holder.sellerName.setText(StringUtils.isNotEmpty(seller.selleName) ? seller.selleName : "");

        if (StringUtils.isNotEmpty(seller.myPrice) && StringUtils.isNotEmpty(seller.precaredPrice)){
            holder.priceCompImage.setVisibility(View.VISIBLE);
        }else {
            holder.priceCompImage.setVisibility(View.GONE);
        }

        holder.priceCompImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isNotEmpty(seller.myPrice)){
                    showSuccessCustomAlert(seller);
                }
            }
        });

        holder.goLiveBtn.setVisibility(seller.canPublish?View.VISIBLE:View.GONE);
        holder.recallBtn.setVisibility(seller.canHold?View.VISIBLE:View.GONE);

        holder.goLiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SellerActivity)mContext).updateSellerProduct("publish", seller.id);
            }
        });

        holder.recallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SellerActivity)mContext).updateSellerProduct("hold", seller.id);
            }
        });

        holder.chatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ChatActivity.class).putExtra(Constants.BundleKeys.ProductId,""+seller.id));
            }
        });


        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkManager.isConnectedToInternet(mContext) && !TextUtils.isEmpty(seller.productUrl)) {
                    try {
                        Log.d("openUrlToBrowser", seller.productUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(seller.productUrl));
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException exception) {
                        Log.e("Redirecting to browser:", exception.toString());
                    }
                }
            }
        });
        holder.mStateProgressBar.setStateDescriptionData(descriptionData);

        holder.statusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.showStatusbar){
                    holder.showStatusbar = false;
                    holder.statesHorizontalView.setVisibility(View.GONE);
                }else {
                    holder.showStatusbar = true;
                    holder.statesHorizontalView.setVisibility(View.VISIBLE);
                }
            }
        });
        if (seller.display_state.equalsIgnoreCase("live")){
            holder.mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.SIX);
        }else if (seller.display_state.equalsIgnoreCase("Rejected") || seller.display_state.equalsIgnoreCase("Reject")){
            holder.mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        }else {
            setStateProgressBar(holder.mStateProgressBar, descriptionDataFromServer.indexOf(seller.display_state));
        }
    }

    private void setStateProgressBar(StateProgressBar mStateProgressBar, int i) {

        Log.e("Posiiton", ""+i);
        switch (i){
            case 0:
                mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                break;
            case 1:
                mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                break;
            case 2:
                mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                break;
            case 3:
                mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                break;
            case 4:
                mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FICE);
                break;
            case 5:
                mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.SIX);
                break;
            case 6:
                mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.SEVEN);
                break;
            case 7:
                mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.EIGHT);
                break;
            case 8:
                mStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.NINE);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mSellerList.size();
    }

    /**
     * class to hold ticket view
     */
    class SellerViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout priceRL;
        private ImageView productImage, chatImage, priceCompImage;
        private TextView productName, productID, productAmount, productStatus, productViewCount, sellerName;
        private Button recallBtn, goLiveBtn;
        private StateProgressBar mStateProgressBar;
        private HorizontalScrollView statesHorizontalView;
        private RelativeLayout statusLayout;
        private boolean showStatusbar;

        public SellerViewHolder(View itemView) {
            super(itemView);
            priceRL = (RelativeLayout) itemView.findViewById(R.id.priceRL);
            sellerName = (TextView) itemView.findViewById(R.id.sellerNameText);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productID = (TextView) itemView.findViewById(R.id.productId);
            productAmount = (TextView) itemView.findViewById(R.id.productAmount);
            productStatus = (TextView) itemView.findViewById(R.id.productStatus);
            productViewCount = (TextView) itemView.findViewById(R.id.productView);
            chatImage = (ImageView) itemView.findViewById(R.id.chatImg);
            priceCompImage = (ImageView) itemView.findViewById(R.id.expandPrice);
            recallBtn = (Button) itemView.findViewById(R.id.recallBtn);
            goLiveBtn = (Button) itemView.findViewById(R.id.goLiveBtn);
            mStateProgressBar = (StateProgressBar) itemView.findViewById(R.id.state_progress_bar);
            statesHorizontalView = (HorizontalScrollView) itemView.findViewById(R.id.statesHorizontalView);
            statusLayout = (RelativeLayout) itemView.findViewById(R.id.statusLayout);

        }
    }

    /**
     * Create custom alert dialogue
     */
    public void showSuccessCustomAlert(Seller seller) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.price_custom_layout);
        final TextView listedFor = (TextView) dialog.findViewById(R.id.sellerText);
        final TextView myPrice = (TextView) dialog.findViewById(R.id.sellingText);
        final TextView serviceTax = (TextView) dialog.findViewById(R.id.serviceTaxText);
        final TextView refurbishCost = (TextView) dialog.findViewById(R.id.refurbishCostText);
        final Button okayBtn = (Button) dialog.findViewById(R.id.btnSubmit);

        listedFor.setText(""+seller.precaredPrice);
        myPrice.setText(""+seller.myPrice);
        serviceTax.setText(""+seller.serviceTax);
        refurbishCost.setText(""+seller.refurbishCash);
        dialog.setCancelable(false);
        dialog.show();

        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
