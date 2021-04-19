package com.kushalsharma.adastra.adapter



import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kushalsharma.adastra.R
import com.kushalsharma.adastra.modals.OnBoardingItem


class onBoardingAdapter(private val onBoardingItemList: ArrayList<OnBoardingItem> ) : RecyclerView.Adapter<onBoardingAdapter.ItemViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItems(onBoardingItemList[position])
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: ${ onBoardingItemList.size} " )
        return onBoardingItemList.size
    }


    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindItems(onBoardingItem: OnBoardingItem){
            val itemImage = itemView.findViewById<ImageView>(R.id.item_image)
            val itemDescription = itemView.findViewById<TextView>(R.id.item_description)
            itemImage.setImageResource(onBoardingItem.image)
            itemDescription.text = onBoardingItem.description

        }
    }
}