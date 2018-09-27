package ch.compile.blitzremote.actions

import ch.compile.blitzremote.BlitzRemote
import ch.compile.blitzremote.model.ConnectionEntry
import com.jcraft.jsch.*
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import java.io.File
import java.util.*
import javax.swing.AbstractAction
import javax.swing.JOptionPane
import kotlin.collections.ArrayList

class DownloadAction(private val connectionEntry: ConnectionEntry) : AbstractAction("Download from remote...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    var channelSftp: ChannelSftp = ChannelSftp()

    var downloadDir = File(System.getProperty("user.home") + "/Downloads/blitz-remote")

    override fun actionPerformed(p0: ActionEvent?) {
        val file = JOptionPane.showInputDialog(BlitzRemote.instance, "Please provide a path", "Download from remote...", JOptionPane.QUESTION_MESSAGE, null, null, "./.profile")
        LOG.debug("User selected $file")
        val jsch = JSch()
        jsch.setKnownHosts(System.getProperty("user.home") + "/.ssh/known_hosts")

        if (connectionEntry.identity != null) {
            try {
                val f = connectionEntry.identity
                val kp = KeyPair.load(jsch, f)
                if (kp.isEncrypted) {
                    val passphrase = JOptionPane.showInputDialog(BlitzRemote.instance, "Please enter passphrase for $f", "Passphrase needed", JOptionPane.QUESTION_MESSAGE, null, null, "")
                    if (passphrase is String) {
                        jsch.addIdentity(f, passphrase)
                    }
                } else {
                    jsch.addIdentity(f)
                }
            } catch (e: JSchException) {
                System.err.println("Error adding identity: " + connectionEntry.identity)
            }
        } else {
            try {
                val f = System.getProperty("user.home") + "/.ssh/id_rsa"
                val kp = KeyPair.load(jsch, f)
                if (kp.isEncrypted) {
                    val passphrase = JOptionPane.showInputDialog(BlitzRemote.instance, "Please enter passphrase for $f", "Passphrase needed", JOptionPane.QUESTION_MESSAGE, null, null, "")
                    if (passphrase is String) {
                        jsch.addIdentity(f, passphrase)
                    }
                } else {
                    jsch.addIdentity(f)
                }
            } catch (e: JSchException) {
                System.err.println("Error adding identity: " + System.getProperty("user.home") + "/.ssh/id_rsa")
            }
        }

        val session = jsch.getSession(connectionEntry.username, connectionEntry.hostname, connectionEntry.port)
        session.connect()

        channelSftp = session.openChannel("sftp") as ChannelSftp
        channelSftp.connect()
        val list = fileList(file as String)
        list.forEach {
            download(it)
        }
        LOG.debug("DONE DOWNLOADING!")
    }

    private fun fileList(file: String): List<String> {
        val list = ArrayList<String>()
        val attrs = channelSftp.stat(file)
        if (attrs.isDir) {
            val fileList = channelSftp.ls(file) as Vector<ChannelSftp.LsEntry>
            channelSftp.cd(file)
            fileList.filter { !it.filename.startsWith('.') }.forEach {
                if (!it.filename.startsWith('.')) {
                    list.addAll(fileList(it.filename))
                }
            }
            channelSftp.cd("..")
        } else {
            list.add("${channelSftp.pwd()}/$file")
        }
        return list
    }

    private fun download(file: String) {
        val local = File("${downloadDir.path}/${connectionEntry.name}/$file")
        LOG.debug("Would download to $local")
        local.parentFile.mkdirs()
        channelSftp.get(file, local.path, DownloadMonitor())
        /* channelSftp.lcd(downloadDir.path)
        channelSftp.get(file)*/
    }

    class DownloadMonitor : SftpProgressMonitor {
        var op: Int = 0
        var src: String? = null
        var dest: String? = null
        var max: Long = 0
        var pos: Long = 0

        override fun count(count: Long): Boolean {
            pos += count
            val progress = String.format("%5.2f", pos / this.max.toDouble() * 100)
            LOG.debug("$src ($progress)")
            return true
        }

        override fun end() {
            when (op) {
                SftpProgressMonitor.GET -> LOG.debug("Finished download: $src")
                SftpProgressMonitor.PUT -> LOG.debug("Finished upload: $src")
            }
        }

        override fun init(op: Int, src: String, dest: String, max: Long) {
            this.op = op
            this.src = src
            this.dest = dest
            this.max = max
            when (op) {
                SftpProgressMonitor.GET -> LOG.debug("Starting download: $src => $dest ($max)")
                SftpProgressMonitor.PUT -> LOG.debug("Starting upload: $src => $dest ($max)")
            }
        }

    }
}
