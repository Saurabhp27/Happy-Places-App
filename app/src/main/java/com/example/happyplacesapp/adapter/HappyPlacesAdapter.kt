package com.example.happyplacesapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplacesapp.AddHappyPlacesActivity
import com.example.happyplacesapp.MainActivity
import com.example.happyplacesapp.R
import com.example.happyplacesapp.database.DatabaseHandler
import com.example.happyplacesapp.model.HappyPlaceModel
import kotlinx.android.synthetic.main.item_happy_place.view.*

open class HappyPlacesAdapter (
    private val context: Context,
    private var list: ArrayList<HappyPlaceModel>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var onClickListener: OnClickListener? = null

        /**
         * Inflates the item views which is designed in xml layout file
         *
         * create a new
         * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            return MyViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_happy_place,
                    parent,
                    false
                )
            )
        }

        /**
         * Binds each item in the ArrayList to a view
         *
         * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
         * an item.
         *
         * This new ViewHolder should be constructed with a new View that can represent the items
         * of the given type. You can either create a new View manually or inflate it from an XML
         * layout file.
         */
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val model = list[position]

            if (holder is MyViewHolder) {
                holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
                holder.itemView.tvTitle.text = model.title
                holder.itemView.tvDescription.text = model.description

                holder.itemView.setOnClickListener {

                    if (onClickListener != null) {
                        onClickListener!!.onClick(position, model)
                    }
                }
            }
        }

        /**
         * Gets the number of items in the list
         */
        override fun getItemCount(): Int {
            return list.size
        }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlacesActivity::class.java)
        intent.putExtra(MainActivity.Extra_Place_Details, list[position])
        activity.startActivityForResult(
            intent,
            requestCode
        ) // Activity is started with requestCode

        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }

        /**
         * A function to bind the onclickListener.
         */
        fun setOnClickListener(onClickListener: OnClickListener) {
            this.onClickListener = onClickListener
        }

    fun removeAt(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])

        if (isDeleted > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    interface OnClickListener {
            fun onClick(position: Int, model: HappyPlaceModel)
        }

        /**
         * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
         */
        private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}