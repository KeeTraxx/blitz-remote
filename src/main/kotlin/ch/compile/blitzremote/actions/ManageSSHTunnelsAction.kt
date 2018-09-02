package ch.compile.blitzremote.actions

import ch.compile.blitzremote.components.BlitzTerminal
import ch.compile.blitzremote.components.SshTunnelEditor
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ManageSSHTunnelsAction(val blitzTerminal: BlitzTerminal) : AbstractAction("Manage SSH tunnels...") {
    override fun actionPerformed(e: ActionEvent?) {
        SshTunnelEditor(blitzTerminal).isVisible = true
    }

}
