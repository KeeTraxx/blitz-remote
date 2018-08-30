package ch.compile.blitzremote.actions

import ch.compile.blitzremote.components.BlitzTerminal
import ch.compile.blitzremote.components.TabbedSSHPanel
import ch.compile.blitzremote.helpers.BlitzTtyShellConnector
import ch.compile.blitzremote.helpers.SessionAvailableListener
import ch.compile.blitzremote.model.ConnectionEntry
import ch.compile.blitzremote.settings.BlitzRemoteSettingsProvider
import com.jcraft.jsch.Session
import com.jediterm.ssh.jsch.JSchShellTtyConnector
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import kotlin.concurrent.thread

class ConnectAction(val connectionEntry: ConnectionEntry) : AbstractAction("Connect..."), SessionAvailableListener {
    override fun onSessionAvailable(session: Session) {
        System.out.println("Connected to " + session.host)
        if (connectionEntry.portforwarding != null) {
            session.setPortForwardingL(connectionEntry.portforwarding)
        }
    }

    override fun actionPerformed(p0: ActionEvent?) {
        System.out.println("Would connect to " + connectionEntry.hostname)
        val terminal = BlitzTerminal(BlitzRemoteSettingsProvider())
        val ttyConnector = BlitzTtyShellConnector(connectionEntry)

        ttyConnector.addSessionAvailableListener(this)

        terminal.ttyConnector = ttyConnector

        TabbedSSHPanel.add(connectionEntry.toString(), terminal)
        terminal.start()

        thread(start = true) {
            val connector = terminal.ttyConnector as JSchShellTtyConnector
            while (!connector.isConnected) {
                Thread.sleep(100)
            }

            while (connector.isConnected) {

                Thread.sleep(100)
            }

            TabbedSSHPanel.remove(terminal)
        }
    }

}
