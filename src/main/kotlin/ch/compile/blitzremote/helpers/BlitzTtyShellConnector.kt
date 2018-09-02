package ch.compile.blitzremote.helpers

import ch.compile.blitzremote.actions.ConnectAction
import ch.compile.blitzremote.model.ConnectionEntry
import com.jcraft.jsch.*
import com.jediterm.ssh.jsch.JSchShellTtyConnector

class BlitzTtyShellConnector(private val connectionEntry: ConnectionEntry) : JSchShellTtyConnector(connectionEntry.hostname, connectionEntry.port, connectionEntry.username, connectionEntry.password), SessionAvailableListener {

    private val sessionAvailableListeners = ArrayList<((Session) -> Unit)>()
    var session: Session? = null

    init {
        this.addSessionAvailableListener(listener = this::onSessionAvailable)
    }

    fun addSessionAvailableListener(listener: (Session) -> Unit) {
        sessionAvailableListeners.add(listener)
    }

    override fun onSessionAvailable(session: Session) {
        this.session = session
        if (connectionEntry.portforwarding != null) {
            try {
                ConnectAction.LOG.info("Setting up port forwarding - ${connectionEntry.portforwarding}")
                session.setPortForwardingL(connectionEntry.portforwarding)
            } catch (e: JSchException) {
                ConnectAction.LOG.warn("Can't bind port! Ignoring port forward...", e)
            }
        }
    }

    override fun openChannel(session: Session): ChannelShell {
        sessionAvailableListeners.forEach { it.invoke(session) }
        if (connectionEntry.httpProxy != null) {
            session.setProxy(ProxyHTTP(connectionEntry.httpProxy))
        }
        return super.openChannel(session)
    }

    override fun configureJSch(jsch: JSch) {
        super.configureJSch(jsch)
        jsch.setKnownHosts(System.getProperty("user.home") + "/.ssh/known_hosts")
        try {
            jsch.addIdentity(System.getProperty("user.home") + "/.ssh/id_rsa")
        } catch (e: JSchException) {
            System.err.println("Error adding identity: " + System.getProperty("user.home") + "/.ssh/id_rsa")
        }
        if (connectionEntry.identity != null) {
            try {
                jsch.addIdentity(connectionEntry.identity)
            } catch (e: JSchException) {
                System.err.println("Error adding identity: " + connectionEntry.identity)
            }
        }
    }
}