package ch.compile.blitzremote.components

import ch.compile.blitzremote.helpers.BlitzTtyShellConnector
import ch.compile.blitzremote.interfaces.ConnectionListener
import ch.compile.blitzremote.model.ConnectionEntry
import ch.compile.blitzremote.settings.BlitzRemoteSettingsProvider
import com.jediterm.terminal.ui.JediTermWidget
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread

class BlitzTerminal(val connectionEntry: ConnectionEntry) : JediTermWidget(BlitzRemoteSettingsProvider()) {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    private val jSchShellTtyConnector = BlitzTtyShellConnector(connectionEntry)

    private var watchConnection = true
    private var isConnected = false

    private val connectionListeners = ArrayList<ConnectionListener>()

    init {
        this.ttyConnector = jSchShellTtyConnector
        this.start()

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
                if (isConnected != jSchShellTtyConnector.isConnected) {
                    isConnected = jSchShellTtyConnector.isConnected
                    LOG.info("Connection state changed ${connectionEntry.hostname}: connected = $isConnected")
                    connectionListeners.forEach { it.onConnectionStateChanged(isConnected) }
                }
            }
        }
    }

    fun addConnectionListener(connectionListener: ConnectionListener) {
        connectionListeners.add(connectionListener)
    }

    override fun close() {
        LOG.debug("Close has been called on ${connectionEntry.name}")
        watchConnection = false
        super.close()
        TabbedSSHPanel.remove(this)
    }

}