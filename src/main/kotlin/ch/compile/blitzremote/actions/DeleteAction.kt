package ch.compile.blitzremote.actions

import ch.compile.blitzremote.model.AbstractBlitzTreeNode
import ch.compile.blitzremote.model.ConnectionModel
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

open class DeleteAction(private val treeNode: AbstractBlitzTreeNode) : AbstractAction("Delete") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        ConnectionModel.removeNodeFromParent(treeNode)
    }

}
