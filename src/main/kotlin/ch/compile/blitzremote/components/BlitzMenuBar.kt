package ch.compile.blitzremote.components

import ch.compile.blitzremote.FILE
import ch.compile.blitzremote.actions.LoadAction
import ch.compile.blitzremote.actions.SaveAction
import ch.compile.blitzremote.model.ConnectionFolder
import ch.compile.blitzremote.model.ConnectionFolderTreeNode
import ch.compile.blitzremote.model.ConnectionModel
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

object BlitzMenuBar : JMenuBar() {
    init {
        val fileMenu = JMenu("File")
        fileMenu.mnemonic = KeyEvent.VK_F

        val newAction = JMenuItem("New")
        newAction.addActionListener {
            ConnectionModel.setRoot(ConnectionFolderTreeNode("Resetted"))
            ConnectionSelector.setSelectionRow(0)
        }
        fileMenu.add(newAction)

        fileMenu.add(JMenuItem(SaveAction((ConnectionModel.root as ConnectionFolderTreeNode).userObject as ConnectionFolder)))
        fileMenu.add(JMenuItem(LoadAction(FILE)))
        this.add(fileMenu)
    }
}