package ch.compile.blitzremote.actions

import ch.compile.blitzremote.BlitzRemote
import ch.compile.blitzremote.FILE
import ch.compile.blitzremote.model.ConnectionFolder
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
        val folder = JFileChooser(FILE.parentFile)
        folder.fileFilter = FileNameExtensionFilter("JSON files", "json")
        folder.fileSelectionMode = JFileChooser.FILES_ONLY
        when (folder.showDialog(BlitzRemote.instance, "Save")) {
            JFileChooser.CANCEL_OPTION -> {
                LOG.debug("USER CANCELLED")
            }

            JFileChooser.APPROVE_OPTION -> {
                LOG.debug("User selected FILE" + folder.selectedFile)
                FILE = folder.selectedFile
                SaveAction().actionPerformed(null)
            }
        }
    }

}