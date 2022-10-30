package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemFoodCartBinding
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.ui.main.fragment.CartFragment

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartHolder>{
    private val inter: ICartAdapter

    constructor(inter: CartFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        return CartHolder(
            ItemFoodCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        holder.binding.data = inter.getCart(position)

        holder.binding.btnAddOrderCart.setOnClickListener {
            this.inter.onClickButtonOrderItem(holder.adapterPosition)
        }

        holder.binding.btnRemoveCart.setOnClickListener {
            this.inter.onClickButtonRemoveItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface ICartAdapter{
        fun getCount():Int
        fun getCart(position:Int): FoodResponse
        fun onClickButtonOrderItem(position:Int)
        fun onClickButtonRemoveItem(position:Int)
    }
    class CartHolder(val binding:ItemFoodCartBinding) : RecyclerView.ViewHolder(binding.root)
}