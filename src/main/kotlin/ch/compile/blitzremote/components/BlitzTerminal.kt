package ch.compile.blitzremote.components

import ch.compile.blitzremote.helpers.BlitzTtyShellConnector
import ch.compile.blitzremote.interfaces.ConnectionListener
import ch.compile.blitzremote.model.ConnectionEntry
import ch.compile.blitzremote.settings.BlitzRemoteSettingsProvider
import com.jcraft.jsch.Session
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.TerminalWidget
import com.jediterm.terminal.ui.TerminalWidgetListener
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.concurrent.thread

class BlitzTerminal(val connectionEntry: ConnectionEntry) : JPanel() {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    val terminal = JediTermWidget(BlitzRemoteSettingsProvider())

    val sshConnector = BlitzTtyShellConnector(connectionEntry)

    var session:Session? = null

    private var watchConnection = true
    private var isConnected = false

    private val connectionListeners = ArrayList<ConnectionListener>()

    init {
        layout = BorderLayout()
        this.add(terminal, BorderLayout.CENTER)
        terminal.addListener {
            LOG.debug("Close has been called on ${connectionEntry.name}")
            watchConnection = false
            TabbedSSHPanel.remove(this@BlitzTerminal)
        }
        terminal.ttyConnector = sshConnector

        this.sshConnector.addSessionAvailableListener { session -> this.session = session }

        terminal.start()

        this.addConnectionListener(object : ConnectionListener {
            override fun onConnectionStateChanged(isConnected: Boolean) {
                if (!isConnected) {
                    TabbedSSHPanel.remove(this@BlitzTerminal)
                }
            }
        })

        thread(start = true) {
            while (watchConnection) {
                Thread.sleep(100)
                if (isConnected != sshConnector.isConnected) {
                    isConnected = sshConnector.isConnected
                    LOG.info("Connection state changed ${connectionEntry.hostname}: connected = $isConnected")
                    connectionListeners.forEach { it.onConnectionStateChanged(isConnected) }
                }
            }
        }
    }

    fun addConnectionListener(connectionListener: ConnectionListener) {
        connectionListeners.add(connectionListener)
    }
}