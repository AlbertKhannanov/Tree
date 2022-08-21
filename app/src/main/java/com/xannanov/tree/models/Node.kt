package com.xannanov.tree.models

import com.xannanov.tree.ui.NodeFragment
import java.util.*

class Node {

    // Для алгоритма обхода в ширину
    @Transient
    var visited = false
    @Transient
    var parent: Node? = null
    @Transient
    val fragment = NodeFragment(node = this)
    val children: List<Node> = ArrayList()
    var isCurrent: Boolean = false
    /**
     * Так как все символы, генерируемые UUID.randomUUID() есть в таблице ASCII =>
     * каждый символ занимает 1 байт => можно взять последние 20 символов из хэша, таким образом
     * мы возьмем 20 последних байт
     **/
    val name = myHashCode().run {
        slice(length - 20 until length)
    }

    fun addChild(node: Node) {
        node.parent = this
        (children as ArrayList).add(node)
    }

    fun remove() {
        (parent?.children as? ArrayList)?.apply {
            remove(this@Node)
            replaceParent(parent)
            addAll(this@Node.children)
        }
    }

    private fun replaceParent(newParent: Node?) =
        children.forEach {
            it.parent = newParent
        }

    private fun myHashCode(): String = UUID.randomUUID().toString()
}