package com.example.routecraft.features.main

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.routecraft.R
import com.example.routecraft.data.pojos.Route
import com.example.routecraft.databinding.ItemRouteBinding

class RouteAdapter(private val listener: Listener) : ListAdapter<Route, RouteAdapter.RouteViewHolder>(DiffCallback()) {

    interface Listener {
        fun routeClicked(route: Route)
        fun openRenameRouteDialog(route: Route)
        fun openDeleteRouteDialog(route: Route)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val binding = ItemRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RouteViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val currentRoute = getItem(position)
        holder.bind(currentRoute)
    }

    inner class RouteViewHolder(private val binding: ItemRouteBinding, private val listener: Listener) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        listener.routeClicked(getItem(position))
                    }
                }
                routeMenuIb.setOnClickListener{
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val popupMenu = PopupMenu(it.context, it)
                        popupMenu.gravity = Gravity.END
                        popupMenu.inflate(R.menu.navigation_view_route_menu)
                        popupMenu.setOnMenuItemClickListener {item ->
                            when (item.itemId) {
                                R.id.action_popup_rename -> {
                                    listener.openRenameRouteDialog(getItem(position))
                                    true
                                }
                                R.id.action_popup_delete -> {
                                    listener.openDeleteRouteDialog(getItem(position))
                                    true
                                }
                                else -> false
                            }
                        }
                        popupMenu.show()
                    }
                }
            }
        }

        fun bind(route: Route){
            Log.d("DEBUG_TAG", "bind");
            binding.routeNameTv.text = route.name

            if (route.selected) {
                binding.apply {
                    container.setBackgroundResource(R.drawable.navigation_view_route_selected_bg)
                    routeNameTv.setTextColor(Color.parseColor("#167ef9"))
                    routeNameTv.typeface = Typeface.DEFAULT_BOLD
                    routeMenuIb.setImageResource(R.drawable.ic_route_menu_blue)
                    routeIcIv.setImageResource(R.drawable.ic_route_blue)
                }
            } else {
                binding.apply {
                    container.setBackgroundResource(R.drawable.navigation_view_route_bg)
                    routeNameTv.setTextColor(Color.parseColor("#515767"))
                    routeNameTv.typeface = Typeface.DEFAULT
                    routeMenuIb.setImageResource(R.drawable.ic_route_menu_black)
                    routeIcIv.setImageResource(R.drawable.ic_route_black)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Route>(){

        override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
            val same: Boolean = oldItem.id == newItem.id
//            Log.d("DEBUG_TAG", "- areItemsTheSame: $same");
//            Log.d("DEBUG_TAG", "old item name: "+ oldItem.name);
//            Log.d("DEBUG_TAG", "new item name: " + newItem.name);
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
            val same: Boolean = oldItem == newItem;
            Log.d("DEBUG_TAG", "* areContentsTheSame: $same")
            Log.d("DEBUG_TAG", "old item name: "+ oldItem.name);
            Log.d("DEBUG_TAG", "old item selected: "+ oldItem.selected)
            Log.d("DEBUG_TAG", "old item creation: "+ oldItem.creationDate)
            Log.d("DEBUG_TAG", "new item name: " + newItem.name)
            Log.d("DEBUG_TAG", "new item selected: " + newItem.selected)
            Log.d("DEBUG_TAG", "new item creation: " + newItem.creationDate)
            return oldItem == newItem
        }
    }
}