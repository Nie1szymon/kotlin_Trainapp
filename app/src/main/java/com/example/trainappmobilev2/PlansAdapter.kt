package com.example.trainappmobilev2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.Plan

class PlansAdapter(
    private val onActionClickListener: (Plan) -> Unit,
    private val actionButtonText: String
) : RecyclerView.Adapter<PlansAdapter.PlanViewHolder>() {

    private var plans: List<Plan> = listOf()

    fun setPlans(plans: List<Plan>) {
        this.plans = plans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view, onActionClickListener, actionButtonText)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    override fun getItemCount(): Int = plans.size

    class PlanViewHolder(
        itemView: View,
        private val onActionClickListener: (Plan) -> Unit,
        private val actionButtonText: String
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val descTextView: TextView = itemView.findViewById(R.id.descTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val ownerTextView: TextView = itemView.findViewById(R.id.ownerTextView)
        private val actionButton: Button = itemView.findViewById(R.id.actionButton)

        fun bind(plan: Plan) {
            nameTextView.text = plan.name
            descTextView.text = plan.desc
            priceTextView.text = plan.price
            ownerTextView.text = plan.owner
            actionButton.text = actionButtonText

            actionButton.setOnClickListener {
                onActionClickListener(plan)
            }
        }
    }
}