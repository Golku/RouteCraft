package com.example.routecraft.features.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.routecraft.R;
import com.example.routecraft.data.pojos.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.CustomViewHolder>{

    private List<Route> routeList = new ArrayList<>();
    private Listener listener;

    public RouteAdapter(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void routeClicked(Route route);
        void openRenameRouteDialog(Route route);
        void openDeleteRouteDialog(Route route);
    }

    public void setRouteList(List<Route> routeList){
        this.routeList = routeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.routeName.setText(routeList.get(position).getRouteName());

//        if(routeList.get(position).isSelected()){
//            holder.container.setBackgroundColor(Color.parseColor("#e0e0e0"));
//            holder.routeName.setTextColor(Color.parseColor("#167ef9"));
//            holder.routeName.setTypeface(Typeface.DEFAULT_BOLD);
//            holder.routeMenuIb.setBackgroundColor(Color.parseColor("#e0e0e0"));
//        }else{
//            holder.container.setBackgroundColor(Color.WHITE);
//            holder.routeName.setTextColor(Color.parseColor("#515767"));
//            holder.routeName.setTypeface(Typeface.DEFAULT);
//            holder.routeMenuIb.setBackgroundColor(Color.WHITE);
//        }

    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        private ConstraintLayout root;
        private ConstraintLayout container;
        private TextView routeName;
        private ImageButton routeMenuIb;

        CustomViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            root.setOnClickListener(this);
            container = itemView.findViewById(R.id.container);
            routeMenuIb = itemView.findViewById(R.id.route_menu_ib);
            routeMenuIb.setOnClickListener(this);
            routeName = itemView.findViewById(R.id.route_name_tv);
        }

        public void onClick(View v) {

            if(v == root){
                listener.routeClicked(routeList.get(getAdapterPosition()));
            }else if(v == routeMenuIb) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setGravity(Gravity.END);
                popupMenu.inflate(R.menu.navigation_view_route_menu);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_popup_rename:
                    listener.openRenameRouteDialog(routeList.get(getAdapterPosition()));
                    return true;
                case R.id.action_popup_delete:
                    listener.openDeleteRouteDialog(routeList.get(getAdapterPosition()));
                    return true;
                default:
                    return false;
            }
        }
    }
}
