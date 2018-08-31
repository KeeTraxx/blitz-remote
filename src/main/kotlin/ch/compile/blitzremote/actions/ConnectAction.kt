package ch.compile.blitzremote.actions

import ch.compile.blitzremote.components.BlitzTerminal
import ch.compile.blitzremote.components.TabbedSSHPanel
import ch.compile.blitzremote.model.ConnectionEntry
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ConnectAction(private val connectionEntry: ConnectionEntry) : AbstractAction("Connect...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        LOG.info("Connecting to ${connectionEntry.hostname}...")
        val terminal = BlitzTerminal(connectionEntry)

        TabbedSSHPanel.add(terminal)
    }

}
