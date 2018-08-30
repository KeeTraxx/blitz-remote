package ch.compile.blitzremote.model

import ch.compile.blitzremote.actions.CreateConnectionEntryAction
import ch.compile.blitzremote.actions.CreateFolderAction
import javax.swing.Action

class ConnectionFolderTreeNode(name:String) : AbstractBlitzTreeNode(ConnectionFolder(name), true) {
    override val actions: List<Action>
        get() = super.actions + listOf(CreateFolderAction(this), CreateConnectionEntryAction(this))

}