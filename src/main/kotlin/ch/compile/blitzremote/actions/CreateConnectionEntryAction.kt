package ch.compile.blitzremote.actions

import ch.compile.blitzremote.model.ConnectionEntryTreeNode
import ch.compile.blitzremote.model.ConnectionFolderTreeNode
import ch.compile.blitzremote.model.ConnectionModel
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class CreateConnectionEntryAction(private val connectionFolderTreeNode: ConnectionFolderTreeNode) : AbstractAction("New connection...") {
    override fun actionPerformed(p0: ActionEvent?) {
        ConnectionModel.insertNodeInto(ConnectionEntryTreeNode("New connection"), connectionFolderTreeNode, connectionFolderTreeNode.childCount)
    }
}