package ch.compile.blitzremote.actions

import ch.compile.blitzremote.BlitzRemote
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class SaveAsAction : AbstractAction("Save As...") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        LOG.debug("Opening FileChooser")
        val fileChooser = JFileChooser(BlitzRemote.instance?.FILE?.parentFile)
        fileChooser.fileFilter = FileNameExtensionFilter("JSON files", "json")
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
        when (fileChooser.showDialog(BlitzRemote.instance, "Save")) {
            JFileChooser.CANCEL_OPTION -> {
                LOG.debug("USER CANCELLED")
            }

            JFileChooser.APPROVE_OPTION -> {
                LOG.debug("User selected FILE" + fileChooser.selectedFile)
                BlitzRemote.instance?.FILE = fileChooser.selectedFile
                SaveAction().actionPerformed(null)
            }
        }
    }

}
