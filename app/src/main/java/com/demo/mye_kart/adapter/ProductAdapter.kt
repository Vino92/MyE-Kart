package com.demo.mye_kart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.mye_kart.R
import com.demo.mye_kart.models.ModelProducts

class ProductAdapter(
    list: ArrayList<ModelProducts.ModelProductsItem>,
    applicationContext: Context
) : RecyclerView.Adapter<ProductAdapter.AdapterViewHolder>() {

    private var itemList: ArrayList<ModelProducts.ModelProductsItem>
    private var ctx: Context

    init {
        this.itemList = list
        this.ctx = applicationContext
    }

    class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tx_title)
        val tvCategory: AppCompatTextView = itemView.findViewById(R.id.tx_category)
        val tvPrice: AppCompatTextView = itemView.findViewById(R.id.tx_price)
        val tvCount: AppCompatTextView = itemView.findViewById(R.id.tx_count)
        val imageView: AppCompatImageView = itemView.findViewById(R.id.image)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_row_product, parent, false)
        return AdapterViewHolder(view)
    }

    override fun getItemCount(): Int {

        return itemList.size
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        try {
            val model = itemList[position]
            holder.tvTitle.text = model.title
            holder.tvCategory.text = model.category
            holder.tvCount.text = buildString {
                append(model.rating.count.toString())
                append(" ratings")
            }
            holder.tvPrice.text = buildString {
                append("\u20B9 ")
                append(model.price.toString())
            }
            if (model.image.isNotEmpty()) {
                Glide.with(ctx).load(model.image)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.imageView)
            } else {
                Glide.with(ctx).load(R.drawable.logo).into(holder.imageView)
            }
            holder.ratingBar.rating = model.rating.rate.toFloat()

        } catch (e: IndexOutOfBoundsException) {
            println(e.printStackTrace())
        }

    }
}