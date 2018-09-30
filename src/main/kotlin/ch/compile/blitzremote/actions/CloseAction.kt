package ch.compile.blitzremote.actions

import ch.compile.blitzremote.components.BlitzTerminal
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

open class CloseAction(private val terminal: BlitzTerminal) : AbstractAction("Close...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        LOG.info("Closing terminal ${terminal.connectionEntry.name} (${terminal.connectionEntry.hostname})...")
        terminal.terminal.close()
    }

}
