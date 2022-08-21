package com.xannanov.tree.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xannanov.tree.databinding.NodeItemBinding
import com.xannanov.tree.models.Node

class ChildrenAdapter(
    private val onClick: (node: Node) -> Unit,
    private val onDelete: (node: Node) -> Unit
) : ListAdapter<Node, ChildrenViewHolder>(
    object : DiffUtil.ItemCallback<Node>() {
        override fun areItemsTheSame(oldItem: Node, newItem: Node): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Node, newItem: Node): Boolean =
            oldItem.name == newItem.name && oldItem.children == newItem.children && oldItem.fragment == newItem.fragment && oldItem.parent == newItem.parent
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildrenViewHolder =
        ChildrenViewHolder(
            NodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick,
            onDelete
        )

    override fun onBindViewHolder(holder: ChildrenViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun submitList(list: MutableList<Node>?) {
        super.submitList(if (list != null) ArrayList(list) else list)
    }
}

class ChildrenViewHolder(
    private var binding: NodeItemBinding,
    private val onClick: (node: Node) -> Unit,
    private val onDelete: (node: Node) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(node: Node) {
        binding.tvChildFragmentName.text = node.name

        itemView.setOnClickListener {
            onClick(node)
        }
        binding.ivDelete.setOnClickListener {
            onDelete(node)
        }
    }
}