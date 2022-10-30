package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemFoodUserBinding
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.ui.main.fragment.FoodUserFragment

class FoodUserAdapter : RecyclerView.Adapter<FoodUserAdapter.FoodUserHolder>{
    private val inter: IFoodUserAdapter

    constructor(inter: FoodUserFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodUserHolder {
        return FoodUserHolder(
            ItemFoodUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodUserHolder, position: Int) {
        holder.binding.data = inter.getFoodUser(position)

        holder.binding.btnAddOrder.setOnClickListener {
            this.inter.onClickButtonOrderItem(holder.adapterPosition)
        }

        holder.binding.btnAddCart.setOnClickListener {
            this.inter.onClickButtonCartItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IFoodUserAdapter{
        fun getCount():Int
        fun getFoodUser(position:Int): FoodResponse
        fun onClickButtonOrderItem(position:Int)
        fun onClickButtonCartItem(position:Int)
    }
    class FoodUserHolder(val binding:ItemFoodUserBinding) : RecyclerView.ViewHolder(binding.root)
}