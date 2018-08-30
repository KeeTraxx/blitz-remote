package ch.compile.blitzremote.components

import ch.compile.blitzremote.actions.QuitAction
import ch.compile.blitzremote.actions.OpenAction
import ch.compile.blitzremote.actions.SaveAction
import ch.compile.blitzremote.actions.SaveAsAction
import ch.compile.blitzremote.model.ConnectionFolder
import ch.compile.blitzremote.model.ConnectionFolderTreeNode
import ch.compile.blitzremote.model.ConnectionModel
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JSeparator

object BlitzMenuBar : JMenuBar() {
    init {
        val fileMenu = JMenu("File")
        fileMenu.mnemonic = KeyEvent.VK_F

        val newAction = JMenuItem("New")
        newAction.addActionListener {
            ConnectionModel.setRoot(ConnectionFolderTreeNode("Connections"))
            ConnectionSelector.setSelectionRow(0)
        }
        fileMenu.add(newAction)
        fileMenu.add(JSeparator())
        fileMenu.add(JMenuItem(OpenAction()))
        fileMenu.add(JSeparator())
        fileMenu.add(JMenuItem(SaveAction()))
        fileMenu.add(JMenuItem(SaveAsAction()))
        fileMenu.add(JSeparator())
        fileMenu.add(JMenuItem(QuitAction()))

        this.add(fileMenu)
    }
}