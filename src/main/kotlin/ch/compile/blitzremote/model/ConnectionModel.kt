package ch.compile.blitzremote.model

import javax.swing.tree.DefaultTreeModel

object ConnectionModel : DefaultTreeModel(ConnectionFolderTreeNode("Connections"), true) {
    init {

    }
}