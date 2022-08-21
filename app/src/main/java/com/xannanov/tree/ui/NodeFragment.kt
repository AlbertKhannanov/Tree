package com.xannanov.tree.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.xannanov.tree.R
import com.xannanov.tree.databinding.FragmentNodeBinding
import com.xannanov.tree.models.Node

class NodeFragment(
    private val node: Node
) : Fragment() {

    private var _binding: FragmentNodeBinding? = null
    private val binding get() = _binding!!
    private val navigation by lazy(LazyThreadSafetyMode.NONE) {
        (requireActivity() as MainActivity).navigation
    }

    private val adapter: ChildrenAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ChildrenAdapter(onClick = {
            navigation.navigateToNode(it)
        }, onDelete = {
            it.remove()
            adapter.submitList(node.children as ArrayList)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onResume() {
        super.onResume()

        adapter.submitList(node.children as ArrayList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        with(binding) {
            rvChildren.adapter = adapter
            tvFragmentName.text = getString(R.string.fragment_title, node.name)

            btnAddNode.setOnClickListener {
                node.addChild(Node())
                adapter.submitList(node.children as ArrayList)
            }

            btnRemoveCurrentNode.setOnClickListener {
                if (!navigation.removeCurrentNode()) {
                    Toast.makeText(requireContext(), "Невозможно удалить корневой элемент", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}