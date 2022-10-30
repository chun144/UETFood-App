package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemOrderBinding
import com.chun.uetfood.model.response.OrderResponse
import com.chun.uetfood.ui.main.fragment.OrderFragment

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderHolder>{
    private val inter: IOrderAdapter

    constructor(inter: OrderFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.binding.data = inter.getOrder(position)

        holder.binding.orderItem.setOnClickListener {
            this.inter.onClickItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IOrderAdapter{
        fun getCount():Int
        fun getOrder(position:Int): OrderResponse
        fun onClickItem(position:Int)
    }
    class OrderHolder(val binding:ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)
}