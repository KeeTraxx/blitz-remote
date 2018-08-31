package ch.compile.blitzremote.actions

import ch.compile.blitzremote.components.BlitzTerminal
import ch.compile.blitzremote.components.TabbedSSHPanel
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

open class CloseAction(private val terminal:BlitzTerminal) : AbstractAction("Close...") {
    override fun actionPerformed(p0: ActionEvent?) {
        terminal.close()
        TabbedSSHPanel.remove(terminal)
    }

}
