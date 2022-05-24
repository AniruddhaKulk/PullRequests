package com.ani.kulk.pullrequests.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ani.kulk.pullrequests.R
import com.ani.kulk.pullrequests.databinding.ItemClosedPullRequestsBinding
import com.ani.kulk.pullrequests.model.PullRequest
import com.ani.kulk.pullrequests.utils.DateDisplayUtils
import com.bumptech.glide.Glide

class ClosedPullRequestAdapter(private val context: Context, private val closedPullRequestList: MutableList<PullRequest>) :
    RecyclerView.Adapter<ClosedPullRequestAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemClosedPullRequestsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClosedPullRequestsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = closedPullRequestList[position]
        holder.binding.itemUserNameTextView.text = item.user.login
        holder.binding.itemPullRequestTitleTextView.text = item.title
        holder.binding.itemPullRequestClosedOnTextView.text = context.getString(R.string.closed_on, DateDisplayUtils.getDayMonthYearFormattedDate(item.closed_at))
        holder.binding.itemPullRequestNumberTextView.text = context.getString(R.string.pr_number_created_on, item.number.toString(), DateDisplayUtils.getDayMonthYearFormattedDate(item.created_at))
        Glide.with(holder.binding.itemUserImageView.context).load(item.user.avatar_url)
            .into(holder.binding.itemUserImageView)
    }

    override fun getItemCount(): Int = closedPullRequestList.size

    fun setItems(items: List<PullRequest>) {
        closedPullRequestList.clear()
        closedPullRequestList.addAll(items)
        notifyDataSetChanged()
    }
}