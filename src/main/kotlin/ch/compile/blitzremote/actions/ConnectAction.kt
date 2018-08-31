package ch.compile.blitzremote.actions

import ch.compile.blitzremote.components.BlitzTerminal
import ch.compile.blitzremote.components.TabbedSSHPanel
import ch.compile.blitzremote.helpers.BlitzTtyShellConnector
import ch.compile.blitzremote.helpers.SessionAvailableListener
import ch.compile.blitzremote.model.ConnectionEntry
import ch.compile.blitzremote.settings.BlitzRemoteSettingsProvider
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import com.jediterm.ssh.jsch.JSchShellTtyConnector
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import kotlin.concurrent.thread

class ConnectAction(private val connectionEntry: ConnectionEntry) : AbstractAction("Connect...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        LOG.info("Connecting to ${connectionEntry.hostname}...")
        val terminal = BlitzTerminal(connectionEntry)
        val ttyConnector = BlitzTtyShellConnector(connectionEntry)

        terminal.ttyConnector = ttyConnector

        TabbedSSHPanel.add(terminal)

        terminal.start()

        thread(start = true) {
            val connector = terminal.ttyConnector as JSchShellTtyConnector
            while (!connector.isConnected) Thread.sleep(100)
            while (connector.isConnected) Thread.sleep(100)

            TabbedSSHPanel.remove(terminal)
        }
    }

}
