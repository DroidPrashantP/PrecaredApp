package com.app.precared.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.precared.R;
import com.app.precared.activities.AddSellerProduct;
import com.app.precared.activities.ChatActivity;
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
    public void onBindViewHolder(SellerViewHolder holder, int position) {
        Seller seller = mSellerList.get(position);
        holder.productName.setText(seller.name);
        holder.productID.setText(""+seller.id);

        holder.productStatus.setText(""+seller.display_state);
        holder.productViewCount.setText(""+seller.view_count);
        if (StringUtils.isNotEmpty(seller.seller_price)) {
            holder.productAmount.setText("Rs "+seller.seller_price);
        }else {
            holder.productAmount.setText("");
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

    }

    @Override
    public int getItemCount() {
        return mSellerList.size();
    }

    /**
     * class to hold ticket view
     */
    class SellerViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage, chatImage;
        private TextView productName, productID, productAmount, productStatus, productViewCount, sellerName;

        public SellerViewHolder(View itemView) {
            super(itemView);
            sellerName = (TextView) itemView.findViewById(R.id.sellerNameText);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productID = (TextView) itemView.findViewById(R.id.productId);
            productAmount = (TextView) itemView.findViewById(R.id.productAmount);
            productStatus = (TextView) itemView.findViewById(R.id.productStatus);
            productViewCount = (TextView) itemView.findViewById(R.id.productView);
            chatImage = (ImageView) itemView.findViewById(R.id.chatImg);
            chatImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ChatActivity.class));
                }
            });
        }
    }
}
