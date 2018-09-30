package ch.compile.blitzremote.model

import ch.compile.blitzremote.actions.ConnectAction
import ch.compile.blitzremote.actions.DownloadAction
import javax.swing.Action

class ConnectionEntryTreeNode(name: String) : AbstractBlitzTreeNode(ConnectionEntry(name), false) {

    override val actions: List<Action>
        get() = super.actions + listOf(ConnectAction(this.userObject as ConnectionEntry))
}