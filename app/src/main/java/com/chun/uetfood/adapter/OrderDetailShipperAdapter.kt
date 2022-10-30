package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemOrderFoodBinding
import com.chun.uetfood.model.response.OrderCompositionResponse
import com.chun.uetfood.ui.main.fragment.OrderDetailShipperFragment

class OrderDetailShipperAdapter : RecyclerView.Adapter<OrderDetailShipperAdapter.OrderDetailShipperHolder>{
    private val inter: IOrderDetailShipperAdapter

    constructor(inter: OrderDetailShipperFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailShipperHolder {
        return OrderDetailShipperHolder(
            ItemOrderFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderDetailShipperHolder, position: Int) {
        holder.binding.data = inter.getOrderDetailShipper(position)
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IOrderDetailShipperAdapter{
        fun getCount():Int
        fun getOrderDetailShipper(position:Int): OrderCompositionResponse
    }
    class OrderDetailShipperHolder(val binding:ItemOrderFoodBinding) : RecyclerView.ViewHolder(binding.root)
}