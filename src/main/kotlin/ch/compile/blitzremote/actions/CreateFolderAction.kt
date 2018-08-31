package ch.compile.blitzremote.actions

import ch.compile.blitzremote.model.ConnectionFolderTreeNode
import ch.compile.blitzremote.model.ConnectionModel
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class CreateFolderAction(private val connectionFolderTreeNode: ConnectionFolderTreeNode) : AbstractAction("New folder...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        CreateConnectionEntryAction.LOG.info("New CreateFolderAction created (with parent = ${connectionFolderTreeNode.userObject})")
        ConnectionModel.insertNodeInto(ConnectionFolderTreeNode("New Folder"), connectionFolderTreeNode, connectionFolderTreeNode.childCount)
    }
}