package com.xannanov.tree.navigation

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.xannanov.tree.models.Node
import com.xannanov.tree.models.Tree
import java.util.*
import kotlin.collections.ArrayList

class Navigation(
    private val fragmentManager: FragmentManager,
    private val containerId: Int,
    private val tree: Tree = Tree(Node())
) {

    private var currentNode = tree.root

    init {
        currentNode = searchActiveNode(currentNode)

        val deepLink = findDeepLink(currentNode)
        deepLink.forEachIndexed { index, node ->
            fragmentManager.beginTransaction().apply {
                addToBackStack(node.name)
                if (index != 0) hide(deepLink[index - 1].fragment)
                add(containerId, node.fragment)
                commit()
            }
        }
    }

    fun navigateToNode(node: Node) =
        fragmentManager.beginTransaction().apply {
            setMaxLifecycle(currentNode.fragment, Lifecycle.State.STARTED)
            hide(currentNode.fragment)
            add(containerId, node.fragment)
            changeCurrentNode(node)
            addToBackStack(node.name)
            commit()
        }

    fun removeCurrentNode(): Boolean {
        if (fragmentManager.backStackEntryCount > 1) {
            currentNode.remove()
            changeCurrentNode(currentNode.parent!!)
            fragmentManager.popBackStack()
            return true
        }
        return false
    }

    fun onBackPressed() {
        changeCurrentNode(currentNode.parent!!)
        fragmentManager.popBackStack()
    }

    private fun changeCurrentNode(newNode: Node) {
        currentNode.isCurrent = false
        currentNode = newNode
        currentNode.isCurrent = true
    }

    private fun searchActiveNode(node: Node): Node {
        val queue: Queue<Node> = LinkedList()

        queue.add(node)

        node.visited = true
        while (queue.size > 0) {
            val v = queue.remove()
            v.children.forEach {
                if (!it.visited) {
                    queue.add(it)
                    it.visited = true
                    if (it.isCurrent) return it
                }
            }
        }

        return tree.root
    }

    private fun findDeepLink(activeNode: Node): List<Node> =
        activeNode.parent?.let {
            val deepLink = ArrayList<Node>()
            var tempNode = activeNode
            deepLink.add(tempNode)
            while (tempNode.parent != null) {
                tempNode = tempNode.parent!!
                deepLink.add(tempNode)
            }
            deepLink.reversed()
        } ?: run {
            listOf(activeNode)
        }
}
