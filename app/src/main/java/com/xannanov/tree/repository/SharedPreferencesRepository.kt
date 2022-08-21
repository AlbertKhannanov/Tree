package com.xannanov.tree.repository

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.xannanov.tree.models.Node
import com.xannanov.tree.models.Tree
import com.xannanov.tree.ui.PREF_KEY

class SharedPreferencesRepository(
    private val sp: SharedPreferences
) {
    private val gson: Gson = Gson()

    fun getTreeState(): Tree =
        (gson.fromJson(sp.getString(PREF_KEY, ""), Tree::class.java) ?: Tree(Node())).apply {
            root.children.forEach {
                setParent(it, root)
            }
        }

    fun saveState(tree: Tree) {
        sp.edit().apply {
            putString(PREF_KEY, Gson().toJson(tree))
            apply()
        }
    }

    private fun setParent(child: Node, parent: Node) {
        child.parent = parent
        child.children.forEach {
            setParent(it, child)
        }
    }
}