package com.release.orderandroiddemo;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {

    private Context mContext;
    private List<OrderModel> orderModelList;

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView orderNumberTV, orderDueDateTV, buyerNameTV,buyerPhoneTV,orderTotalTV,orderGPSTV,customerAddressTV;

        public OrderViewHolder(View view) {
            super(view);
            orderNumberTV = (TextView) view.findViewById(R.id.orderNumberTV);
            orderDueDateTV = (TextView) view.findViewById(R.id.orderDueDateTV);
            buyerNameTV = (TextView) view.findViewById(R.id.buyerNameTV);
            buyerPhoneTV = (TextView) view.findViewById(R.id.buyerPhoneTV);
            orderTotalTV = (TextView) view.findViewById(R.id.orderTotalTV);
            orderGPSTV = (TextView) view.findViewById(R.id.gpsTV);
            customerAddressTV = (TextView) view.findViewById(R.id.customerAddressTV);

        }
    }


    public OrderListAdapter(Context mContext, List<OrderModel> orderModelList) {
        this.mContext = mContext;
        this.orderModelList = orderModelList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_row, parent, false);

        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderViewHolder holder, int position) {
        OrderModel orderModel = orderModelList.get(position);
        holder.orderNumberTV.setText(orderModel.getOrderNumber());
        holder.orderDueDateTV.setText(orderModel.getOrderDueDate());
        holder.buyerNameTV.setText(orderModel.getBuyerName());
        holder.buyerPhoneTV.setText(orderModel.getBuyerPhone());
        holder.orderTotalTV.setText("INR "+orderModel.getOrderTotal());
        holder.customerAddressTV.setText(orderModel.getAddress());
        holder.orderGPSTV.setText(orderModel.getCity() +"-"+orderModel.getState() +"-"+orderModel.getCountry());


    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
//    private void showPopupMenu(View view) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//    }

    /**
     * Click listener for popup menu items
     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.action_add_favourite:
//                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }
}
