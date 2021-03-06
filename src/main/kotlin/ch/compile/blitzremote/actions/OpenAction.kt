package ch.compile.blitzremote.actions

import ch.compile.blitzremote.BlitzRemote
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class OpenAction : AbstractAction("Open...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        LOG.debug("Opening FileChooser...")
        val folder = JFileChooser(BlitzRemote.instance?.FILE)
        folder.fileFilter = FileNameExtensionFilter("JSON files", "json")
        folder.fileSelectionMode = JFileChooser.FILES_ONLY
        when (folder.showDialog(BlitzRemote.instance, "Open")) {
            JFileChooser.CANCEL_OPTION -> {
                LOG.debug("USER CANCELLED")
            }

            JFileChooser.APPROVE_OPTION -> {
                LOG.debug("User selected FILE" + folder.selectedFile)
                LoadAction(folder.selectedFile).actionPerformed(null)
            }
        }
    }
}
