package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemFoodOrderBinding
import com.chun.uetfood.model.order.OrderComposition
import com.chun.uetfood.ui.main.fragment.FoodOrderFragment

class FoodOrderAdapter : RecyclerView.Adapter<FoodOrderAdapter.FoodOrderHolder>{
    private val inter: IFoodOrderAdapter

    constructor(inter: FoodOrderFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodOrderHolder {
        return FoodOrderHolder(
            ItemFoodOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodOrderHolder, position: Int) {
        holder.binding.data = inter.getFoodOrder(position)

        holder.binding.ibUp.setOnClickListener {
            this.inter.onClickButtonUpItem(holder.adapterPosition)
        }

        holder.binding.ibDown.setOnClickListener {
            this.inter.onClickButtonDownItem(holder.adapterPosition)
        }

        holder.binding.btnRemoveFood.setOnClickListener {
            this.inter.onClickButtonRemoveItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IFoodOrderAdapter{
        fun getCount():Int
        fun getFoodOrder(position:Int): OrderComposition
        fun onClickButtonUpItem(position:Int)
        fun onClickButtonDownItem(position:Int)
        fun onClickButtonRemoveItem(position:Int)
    }
    class FoodOrderHolder(val binding:ItemFoodOrderBinding) : RecyclerView.ViewHolder(binding.root)
}