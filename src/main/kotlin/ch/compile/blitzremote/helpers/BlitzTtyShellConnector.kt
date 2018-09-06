package ch.compile.blitzremote.helpers

import ch.compile.blitzremote.model.ConnectionEntry
import com.jcraft.jsch.*
import com.jediterm.ssh.jsch.JSchShellTtyConnector
import org.slf4j.LoggerFactory
import java.util.*

class BlitzTtyShellConnector(private val connectionEntry: ConnectionEntry) : JSchShellTtyConnector(connectionEntry.hostname, connectionEntry.port, connectionEntry.username, connectionEntry.password) {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }
    private val sessionAvailableListeners = ArrayList<((Session) -> Unit)>()
    var session: Session? = null

    fun addSessionAvailableListener(listener: (Session) -> Unit) {
        sessionAvailableListeners.add(listener)
    }

    override fun configureSession(session: Session, config: Properties) {
        super.configureSession(session, config)

        this.session = session

        if (connectionEntry.httpProxy != null) {
            LOG.warn("Setting HTTP PROXY to ${connectionEntry.httpProxy}")
            session.setProxy(ProxyHTTP(connectionEntry.httpProxy))
        }

        if (connectionEntry.portforwarding != null) {
            try {
                LOG.info("Setting up port forwarding - ${connectionEntry.portforwarding}")
                session.setPortForwardingL(connectionEntry.portforwarding)
            } catch (e: JSchException) {
                LOG.warn("Can't bind port! Ignoring port forwarding...", e)
            }
        }
    }

    override fun openChannel(session: Session): ChannelShell {
        sessionAvailableListeners.forEach { it.invoke(session) }
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