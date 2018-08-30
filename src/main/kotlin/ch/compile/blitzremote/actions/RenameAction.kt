package ch.compile.blitzremote.actions

import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JTree
import javax.swing.tree.TreePath

class RenameAction(val tree: JTree, val path:TreePath) : AbstractAction("Rename") {
    override fun actionPerformed(p0: ActionEvent?) {
        tree.startEditingAtPath(path)
    }

}
