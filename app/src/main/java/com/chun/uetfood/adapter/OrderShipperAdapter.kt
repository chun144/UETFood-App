package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemOrderShipperBinding
import com.chun.uetfood.model.response.OrderResponse
import com.chun.uetfood.ui.main.fragment.OrderShipperFragment

class OrderShipperAdapter : RecyclerView.Adapter<OrderShipperAdapter.OrderShipperHolder>{
    private val inter: IOrderShipperAdapter

    constructor(inter: OrderShipperFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderShipperHolder {
        return OrderShipperHolder(
            ItemOrderShipperBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderShipperHolder, position: Int) {
        holder.binding.data = inter.getOrderShipper(position)

        holder.binding.orderShipperItem.setOnClickListener {
            this.inter.onClickItem(holder.adapterPosition)
        }

        holder.binding.btnShip.setOnClickListener {
            this.inter.onClickButtonItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IOrderShipperAdapter{
        fun getCount():Int
        fun getOrderShipper(position:Int): OrderResponse
        fun onClickItem(position:Int)
        fun onClickButtonItem(position:Int)
    }
    class OrderShipperHolder(val binding:ItemOrderShipperBinding) : RecyclerView.ViewHolder(binding.root)
}