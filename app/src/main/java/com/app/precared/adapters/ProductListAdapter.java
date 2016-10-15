package com.app.precared.adapters;

import android.content.Context;
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
import com.app.precared.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prashant on 13/7/16.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.TicketViewHolder> {
    private final static String TAG = ProductListAdapter.class.getSimpleName();
    private Context mContext;
    private List<Product> mProductList;

    public ProductListAdapter(Context context, List<Product> productList) {
        this.mProductList = productList;
        this.mContext = context;
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_custom_product_list, parent, false);
        return new TicketViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
        holder.mRatingBar.setRating(3.5f);
        holder.priceTextView.setPaintFlags(holder.priceTextView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return 5;

    }

    /**
     * class to hold ticket view
     */
    class TicketViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitleTextView, priceTextView, OfferPriseTextView;
        private RatingBar mRatingBar;
        private Button clickBtn;

        public TicketViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.productImg);
            productTitleTextView = (TextView) itemView.findViewById(R.id.productTitle);
            priceTextView = (TextView) itemView.findViewById(R.id.priceText);
            OfferPriseTextView = (TextView) itemView.findViewById(R.id.offerPriceText);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            clickBtn = (Button) itemView.findViewById(R.id.clickBtn);
        }
    }
}
