package ch.compile.blitzremote.actions

import ch.compile.blitzremote.BlitzRemote
import ch.compile.blitzremote.helpers.BlitzObjectMapper
import ch.compile.blitzremote.model.ConnectionFolderTreeNode
import ch.compile.blitzremote.model.ConnectionModel
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import java.io.FileOutputStream
import javax.swing.AbstractAction
import javax.swing.JOptionPane

class SaveAction : AbstractAction("Save") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        LOG.debug("Saving to ${BlitzRemote.instance?.FILE}")
        try {
            val fos = FileOutputStream(BlitzRemote.instance?.FILE)

            LOG.debug(BlitzObjectMapper.writeValueAsString((ConnectionModel.root as ConnectionFolderTreeNode).userObject))

            BlitzObjectMapper.writeValue(fos, (ConnectionModel.root as ConnectionFolderTreeNode).userObject)
            fos.close()
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(BlitzRemote.instance, "Couldn't save file: ${BlitzRemote.instance?.FILE?.absoluteFile}", "Blitz Remote ERROR", JOptionPane.ERROR_MESSAGE)
        }
    }

}
