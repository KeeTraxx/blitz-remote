package ch.compile.blitzremote.actions

import ch.compile.blitzremote.model.ConnectionEntryTreeNode
import ch.compile.blitzremote.model.ConnectionFolderTreeNode
import ch.compile.blitzremote.model.ConnectionModel
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class CreateConnectionEntryAction(private val connectionFolderTreeNode: ConnectionFolderTreeNode) : AbstractAction("New connection...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        LOG.info("New ConnectionEntry created (with parent = ${connectionFolderTreeNode.userObject})")
        ConnectionModel.insertNodeInto(ConnectionEntryTreeNode("New connection"), connectionFolderTreeNode, connectionFolderTreeNode.childCount)
    }
}