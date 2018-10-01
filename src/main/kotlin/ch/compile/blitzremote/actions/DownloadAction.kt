package ch.compile.blitzremote.actions

import ch.compile.blitzremote.BlitzRemote
import ch.compile.blitzremote.components.BlitzTerminal
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.SftpProgressMonitor
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.event.ActionEvent
import java.io.File
import java.nio.file.Paths
import java.util.*
import javax.swing.AbstractAction
import javax.swing.JOptionPane
import javax.swing.JProgressBar
import javax.swing.SwingWorker
import kotlin.collections.ArrayList

class DownloadAction(private val blitzTerminal: BlitzTerminal) : AbstractAction("Download from remote...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        val file = JOptionPane.showInputDialog(BlitzRemote.instance, "Please provide a basePath (relative to home directory)", "Download from remote...", JOptionPane.QUESTION_MESSAGE, null, null, "") as String
        blitzTerminal.add(DownloadBar(file), BorderLayout.SOUTH)
        blitzTerminal.revalidate()
    }

    inner class DownloadBar(val file: String) : JProgressBar() {
        var downloadDir = Paths.get(System.getProperty("user.home"), "Downloads", "blitz-remote", blitzTerminal.connectionEntry.name)!!
        private val channelSftp: ChannelSftp = blitzTerminal.session!!.openChannel("sftp") as ChannelSftp

        inner class BackgroundDownloader : SwingWorker<Unit, BackgroundDownloader.GenericSftpMonitor>() {
            override fun doInBackground() {
                progress = 0
                val list = fileList(this@DownloadBar.file)

                list.forEachIndexed { index, file ->
                    val local = File("$downloadDir/$file")
                    local.parentFile.mkdirs()
                    val monitor = GenericSftpMonitor()
                    channelSftp.get(file, local.path, monitor)
                    progress = 100 * index / list.size
                }

                progress = 100
            }

            override fun done() {
                this@DownloadBar.progressString = "Finished download to $downloadDir"
                Desktop.getDesktop().open(downloadDir.toFile())
            }

            override fun process(p0: MutableList<GenericSftpMonitor>) {
                val monitor = p0.first()
                val perc = 100 * monitor.pos / monitor.max.toDouble()
                this@DownloadBar.progressString = String.format("Transferring %s (%.2f%%)", monitor.src.substringAfterLast('/'), perc)
                this@DownloadBar.repaint()
                this@DownloadBar.revalidate()
            }

            private fun fileList(file: String): List<String> {
                val list = ArrayList<String>()
                val attrs = channelSftp.stat(file)
                if (attrs.isDir) {
                    val fileList = channelSftp.ls(file) as Vector<*>
                    channelSftp.cd(file)
                    fileList.filter { it is ChannelSftp.LsEntry && !it.filename.startsWith('.') }.forEach {
                        if (it is ChannelSftp.LsEntry && !it.filename.startsWith('.')) {
                            list.addAll(fileList(it.filename))
                        }
                    }
                    channelSftp.cd("..")
                } else {
                    list.add("${channelSftp.pwd()}/$file")
                }
                return list
            }

            inner class GenericSftpMonitor : SftpProgressMonitor {
                var op: Int = 0
                var src: String = ""
                var dest: String = ""
                var max: Long = 0
                var pos: Long = 0

                override fun count(count: Long): Boolean {
                    pos += count
                    this@BackgroundDownloader.publish(this)
                    return true
                }

                override fun end() {
                    this@BackgroundDownloader.publish(this)
                }

                override fun init(op: Int, src: String, dest: String, max: Long) {
                    this.op = op
                    this.src = src
                    this.dest = dest
                    this.max = max
                    this@BackgroundDownloader.publish(this)
                }
            }
        }

        init {
            channelSftp.connect()
            this.isStringPainted = true
            this.progressString = "Getting file list..."
            val backgroundDownloader = BackgroundDownloader()
            backgroundDownloader.addPropertyChangeListener {
                when (it.propertyName) {
                    "progress" -> {
                        this.value = it.newValue as Int
                    }
                }
            }
            backgroundDownloader.execute()
        }
    }


}
