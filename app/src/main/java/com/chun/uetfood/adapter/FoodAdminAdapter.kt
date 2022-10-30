package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemFoodAdminBinding
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.ui.main.fragment.FoodAdminFragment

class FoodAdminAdapter : RecyclerView.Adapter<FoodAdminAdapter.FoodAdminHolder>{
    private val inter: IFoodAdminAdapter

    constructor(inter: FoodAdminFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdminHolder {
        return FoodAdminHolder(
            ItemFoodAdminBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodAdminHolder, position: Int) {
        holder.binding.data = inter.getFoodAdmin(position)

        holder.binding.btnFix.setOnClickListener {
            this.inter.onClickButtonItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IFoodAdminAdapter{
        fun getCount():Int
        fun getFoodAdmin(position:Int): FoodResponse
        fun onClickButtonItem(position:Int)
    }
    class FoodAdminHolder(val binding:ItemFoodAdminBinding) : RecyclerView.ViewHolder(binding.root)
}