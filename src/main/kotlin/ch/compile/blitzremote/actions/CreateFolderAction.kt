package ch.compile.blitzremote.actions

import ch.compile.blitzremote.model.ConnectionFolder
import ch.compile.blitzremote.model.ConnectionFolderTreeNode
import ch.compile.blitzremote.model.ConnectionModel
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class CreateFolderAction(private val connectionFolderTreeNode: ConnectionFolderTreeNode) : AbstractAction("New folder...") {
    override fun actionPerformed(p0: ActionEvent?) {
        ConnectionModel.insertNodeInto(ConnectionFolderTreeNode("New Folder"), connectionFolderTreeNode, connectionFolderTreeNode.childCount)
    }
}