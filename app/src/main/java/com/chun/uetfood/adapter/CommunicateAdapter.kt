package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemQuestionBinding
import com.chun.uetfood.model.response.QuestionResponse
import com.chun.uetfood.ui.main.fragment.CommunicateFragment

class CommunicateAdapter : RecyclerView.Adapter<CommunicateAdapter.CommunicateHolder>{
    private val inter: ICommunicateAdapter

    constructor(inter: CommunicateFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunicateHolder {
        return CommunicateHolder(
            ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: CommunicateHolder, position: Int) {
        holder.binding.data = inter.getQuestion(position)

        holder.binding.itemQuestion.setOnClickListener {
            this.inter.onClickItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface ICommunicateAdapter{
        fun getCount():Int
        fun getQuestion(position:Int): QuestionResponse
        fun onClickItem(position:Int)
    }
    class CommunicateHolder(val binding:ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root)
}