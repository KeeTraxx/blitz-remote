package ch.compile.blitzremote.actions

import ch.compile.blitzremote.components.BlitzTerminal
import ch.compile.blitzremote.components.TabbedSSHPanel
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class QuitAction() : AbstractAction("Quit") {
    override fun actionPerformed(p0: ActionEvent?) {
        System.exit(0)
    }
}
