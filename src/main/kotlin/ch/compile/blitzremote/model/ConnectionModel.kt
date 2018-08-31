package ch.compile.blitzremote.model

import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.MutableTreeNode
import javax.swing.tree.TreePath

object ConnectionModel : DefaultTreeModel(ConnectionFolderTreeNode("Connections"), true) {
    init {

    }

    override fun valueForPathChanged(treePath: TreePath, obj: Any?) {
        val userObject = (treePath.lastPathComponent as AbstractBlitzTreeNode).userObject

        if (userObject is BlitzModel) {
            userObject.name = obj.toString()
            this.nodeChanged(treePath.lastPathComponent as MutableTreeNode)
        }
    }
}