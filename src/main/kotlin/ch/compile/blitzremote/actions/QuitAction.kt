package ch.compile.blitzremote.actions

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class QuitAction : AbstractAction("Quit") {
    override fun actionPerformed(p0: ActionEvent?) {
        System.exit(0)
    }
}
