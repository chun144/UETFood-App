package com.chun.uetfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chun.uetfood.databinding.ItemFeedbackBinding
import com.chun.uetfood.databinding.ItemQuestionBinding
import com.chun.uetfood.model.response.FeedbackResponse
import com.chun.uetfood.model.response.QuestionResponse
import com.chun.uetfood.ui.main.fragment.CommunicateFragment
import com.chun.uetfood.ui.main.fragment.FeedbackFragment

class FeedbackAdapter : RecyclerView.Adapter<FeedbackAdapter.FeedbackHolder>{
    private val inter: IFeedbackAdapter

    constructor(inter: FeedbackFragment){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackHolder {
        return FeedbackHolder(
            ItemFeedbackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: FeedbackHolder, position: Int) {
        holder.binding.data = inter.getFeedback(position)
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IFeedbackAdapter{
        fun getCount():Int
        fun getFeedback(position:Int): FeedbackResponse
    }
    class FeedbackHolder(val binding:ItemFeedbackBinding) : RecyclerView.ViewHolder(binding.root)
}