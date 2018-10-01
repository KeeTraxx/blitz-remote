package ch.compile.blitzremote.components

import ch.compile.blitzremote.actions.ConnectAction
import ch.compile.blitzremote.model.AbstractBlitzTreeNode
import ch.compile.blitzremote.model.ConnectionEntry
import ch.compile.blitzremote.model.ConnectionEntryTreeNode
import ch.compile.blitzremote.model.ConnectionModel
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPopupMenu
import javax.swing.JTree
import javax.swing.SwingUtilities
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreeSelectionModel


object ConnectionSelector : JTree(ConnectionModel) {

    init {
        this.isEditable = true
        this.selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        this.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(p0: MouseEvent) {
                super.mouseClicked(p0)
                val path = this@ConnectionSelector.getPathForLocation(p0.x, p0.y) ?: return

                val entry = path.lastPathComponent

                if (SwingUtilities.isRightMouseButton(p0)) {
                    val contextMenu = JPopupMenu()
                    if (entry is AbstractBlitzTreeNode) {
                        entry.actions.forEach { contextMenu.add(it) }
                    }

                    contextMenu.show(this@ConnectionSelector, p0.x, p0.y)

                    // this@ConnectionSelector.startEditingAtPath(basePath)
                }

                if (p0.button == MouseEvent.BUTTON1 && p0.clickCount > 1 && entry is ConnectionEntryTreeNode) {
                    ConnectAction(entry.userObject as ConnectionEntry).actionPerformed(null)
                }
            }
        })

        this.model.addTreeModelListener(object : TreeModelListener {
            override fun treeNodesInserted(treeModelEvent: TreeModelEvent?) {
                if (treeModelEvent != null) {
                    this@ConnectionSelector.expandPath(treeModelEvent.treePath)
                    this@ConnectionSelector.selectionPath = treeModelEvent.treePath.pathByAddingChild(treeModelEvent.children[0])
                }
            }

            override fun treeStructureChanged(p0: TreeModelEvent?) {
            }

            override fun treeNodesChanged(p0: TreeModelEvent) {
                // Update the Property Editor in an awkward way...
                val oldpath = this@ConnectionSelector.selectionPath
                this@ConnectionSelector.selectionPath = this@ConnectionSelector.getPathForRow(0)
                this@ConnectionSelector.selectionPath = oldpath
            }

            override fun treeNodesRemoved(p0: TreeModelEvent?) {
            }

        })
    }
}
