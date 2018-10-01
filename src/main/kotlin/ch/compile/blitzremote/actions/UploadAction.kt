package ch.compile.blitzremote.actions

import ch.compile.blitzremote.components.BlitzTerminal
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.SftpProgressMonitor
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.swing.AbstractAction
import javax.swing.JFileChooser
import javax.swing.JProgressBar
import javax.swing.SwingWorker

class UploadAction(private val blitzTerminal: BlitzTerminal) : AbstractAction("Upload to remote...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        val fileChooser = JFileChooser()
        fileChooser.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
        fileChooser.isMultiSelectionEnabled = false

        when (fileChooser.showOpenDialog(blitzTerminal)) {
            JFileChooser.APPROVE_OPTION -> {
                LOG.info("User selected: " + fileChooser.selectedFile)
                blitzTerminal.add(UploadBar(Paths.get(fileChooser.selectedFile.absolutePath)), BorderLayout.SOUTH)
                blitzTerminal.revalidate()
            }

            JFileChooser.CANCEL_OPTION -> {
                LOG.info("User cancelled file selection")
            }
        }

        /* blitzTerminal.add(UploadBar(file), BorderLayout.SOUTH)
        blitzTerminal.revalidate() */
    }

    inner class UploadBar(val basePath: Path) : JProgressBar() {
        private val channelSftp: ChannelSftp = blitzTerminal.session!!.openChannel("sftp") as ChannelSftp

        inner class BackgroundUploader : SwingWorker<Unit, BackgroundUploader.GenericSftpMonitor>() {
            override fun doInBackground() {
                val uploadDir = "${channelSftp.home}/blitz-uploads"
                try {
                    channelSftp.mkdir(uploadDir)
                } catch (e: Exception) {
                    LOG.debug("blitz-remote dir already exists...")
                }
                channelSftp.cd(uploadDir)

                progress = 0

                val fileList = Files.walk(basePath).collect(Collectors.toList())

                fileList.forEachIndexed { index, path ->
                    progress = index / fileList.size
                    val uploadPath = basePath.parent.relativize(path)
                    LOG.debug("would upload $uploadPath")

                    if (path.toFile().isDirectory) {
                        LOG.debug("Creating directory $uploadPath")
                        try {
                            channelSftp.mkdir(uploadPath.toString())
                        } catch (e: Exception) {
                            LOG.debug("$uploadDir/$uploadPath dir already exists...")
                        }

                        channelSftp.cd(uploadPath.toString())
                    } else {
                        LOG.debug("Uploading file $path to $uploadPath")
                        channelSftp.put(path.toString(), uploadPath.toString(), GenericSftpMonitor(), ChannelSftp.OVERWRITE)
                    }
                    channelSftp.cd(uploadDir)
                }

                progress = 100
            }

            override fun done() {
            }

            override fun process(p0: MutableList<GenericSftpMonitor>) {
                val monitor = p0.first()
                val perc = 100 * monitor.pos / monitor.max.toDouble()
                this@UploadBar.progressString = String.format("Transferring %s (%.2f%%)", monitor.src.substringAfterLast('/'), perc)
                this@UploadBar.repaint()
                this@UploadBar.revalidate()
            }

            inner class GenericSftpMonitor : SftpProgressMonitor {
                var op: Int = 0
                var src: String = ""
                var dest: String = ""
                var max: Long = 0
                var pos: Long = 0

                override fun count(count: Long): Boolean {
                    pos += count
                    this@BackgroundUploader.publish(this)
                    return true
                }

                override fun end() {
                    this@BackgroundUploader.publish(this)
                }

                override fun init(op: Int, src: String, dest: String, max: Long) {
                    when (op) {
                        SftpProgressMonitor.PUT -> LOG.debug("Starting upload of $src to $dest")
                        SftpProgressMonitor.GET -> LOG.debug("Starting download of $src to $dest")
                    }
                    this.op = op
                    this.src = src
                    this.dest = dest
                    this.max = max
                    this@BackgroundUploader.publish(this)
                }
            }
        }

        init {
            channelSftp.connect()
            this.isStringPainted = true
            this.progressString = "Getting file list..."
            val backgroundDownloader = BackgroundUploader()
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
