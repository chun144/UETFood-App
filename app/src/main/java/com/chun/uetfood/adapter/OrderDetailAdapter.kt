package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemOrderFoodBinding
import com.chun.uetfood.model.response.OrderCompositionResponse
import com.chun.uetfood.ui.main.fragment.OrderDetailFragment

class OrderDetailAdapter : RecyclerView.Adapter<OrderDetailAdapter.OrderDetailHolder>{
    private val inter: IOrderDetailAdapter

    constructor(inter: OrderDetailFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailHolder {
        return OrderDetailHolder(
            ItemOrderFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderDetailHolder, position: Int) {
        holder.binding.data = inter.getOrderDetail(position)
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IOrderDetailAdapter{
        fun getCount():Int
        fun getOrderDetail(position:Int): OrderCompositionResponse
    }
    class OrderDetailHolder(val binding:ItemOrderFoodBinding) : RecyclerView.ViewHolder(binding.root)
}