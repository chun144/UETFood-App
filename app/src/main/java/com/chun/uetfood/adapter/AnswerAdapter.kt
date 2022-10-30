package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemAnswerBinding
import com.chun.uetfood.model.response.AnswerResponse
import com.chun.uetfood.ui.main.fragment.QuestionFragment

class AnswerAdapter : RecyclerView.Adapter<AnswerAdapter.AnswerHolder>{
    private val inter: IAnswerAdapter

    constructor(inter: QuestionFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
        return AnswerHolder(
            ItemAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: AnswerHolder, position: Int) {
        holder.binding.data = inter.getAnswer(position)

    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IAnswerAdapter{
        fun getCount():Int
        fun getAnswer(position:Int): AnswerResponse
    }
    class AnswerHolder(val binding:ItemAnswerBinding) : RecyclerView.ViewHolder(binding.root)
}