package com.app.precared.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.precared.R;
import com.app.precared.activities.AddSellerProduct;
import com.app.precared.activities.ChatActivity;
import com.app.precared.activities.SellerActivity;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.MyChats;
import com.app.precared.models.Seller;
import com.app.precared.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prashant on 13/7/16.
 */
public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {
    private final static String TAG = SellerAdapter.class.getSimpleName();
    private Context mContext;
    private List<Seller> mSellerList;

    public SellerAdapter(Context context, List<Seller> sellerList) {
        this.mSellerList = sellerList;
        this.mContext = context;
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
        if (StringUtils.isNotEmpty(seller.seller_price)) {
            holder.priceRL.setVisibility(View.VISIBLE);
            holder.productAmount.setText("Rs "+seller.seller_price);
        }else {
            holder.productAmount.setText("");
            holder.priceRL.setVisibility(View.GONE);
        }
        if (StringUtils.isNotEmpty(seller.image_url)) {
              Picasso.with(mContext).load(seller.image_url).placeholder(R.drawable.place_product).into(holder.productImage);
        }else {
            holder.productImage.setImageResource(R.drawable.place_product);
        }

        if (StringUtils.isNotEmpty(seller.selleName)) {
            holder.sellerName.setText(seller.selleName);
        }else {
            holder.sellerName.setText("");
        }

        if (StringUtils.isNotEmpty(seller.seller_price) && StringUtils.isNotEmpty(seller.selling_price)){
            holder.priceCompImage.setVisibility(View.VISIBLE);
        }else {
            holder.priceCompImage.setVisibility(View.GONE);
        }

        holder.priceCompImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isNotEmpty(seller.seller_price)){
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
            chatImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ChatActivity.class));
                }
            });
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

        listedFor.setText(""+seller.selling_price);
        myPrice.setText(""+seller.seller_price);
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
