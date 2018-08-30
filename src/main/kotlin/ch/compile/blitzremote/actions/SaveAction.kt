package ch.compile.blitzremote.actions

import ch.compile.blitzremote.FILE
import ch.compile.blitzremote.helpers.BlitzObjectMapper
import ch.compile.blitzremote.model.ConnectionFolder
import java.awt.event.ActionEvent
import java.io.FileOutputStream
import javax.swing.AbstractAction

class SaveAction(private val connectionFolder: ConnectionFolder) : AbstractAction("Save") {
    override fun actionPerformed(p0: ActionEvent?) {
        // val oos = ObjectOutputStream(FileOutputStream(FILE))
        // oos.writeObject(connectionFolder)

        val fos = FileOutputStream(FILE)

        BlitzObjectMapper.writeValue(fos, connectionFolder)
        fos.close()
    }

}
