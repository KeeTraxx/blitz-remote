package ch.compile.blitzremote

import ch.compile.blitzremote.actions.LoadAction
import ch.compile.blitzremote.components.BlitzMenuBar
import ch.compile.blitzremote.components.ConnectionSelector
import ch.compile.blitzremote.components.PropertyEditor
import ch.compile.blitzremote.components.TabbedSSHPanel
import ch.compile.blitzremote.model.AbstractBlitzTreeNode
import org.apache.logging.log4j.util.Strings
import java.awt.Dimension
import java.awt.EventQueue
import java.io.File
import javax.swing.*
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeNode

var FILE = File(Strings.join(listOf(System.getProperty("user.home"), ".config", "blitz-remote", "connections.json"), File.separatorChar))
    set(value) {
        BlitzRemote.instance?.title = "blitz-remote ${value.absolutePath}"
    }

class BlitzRemote : JFrame("BlitzRemote") {
    companion object {
        var instance : BlitzRemote? = null
    }
    private val leftSplitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, true, JScrollPane(ConnectionSelector), JPanel())
    private val mainSplitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftSplitPane, TabbedSSHPanel)
    init {
        instance = this
        this.iconImage = ImageIcon(this.javaClass.classLoader.getResourceAsStream("icon.png").readBytes()).image
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        this.isVisible = true
        this.isResizable = true

        this.setSize(640, 480)

        this.extendedState = JFrame.MAXIMIZED_BOTH

        mainSplitPane.leftComponent.minimumSize = Dimension(200, 200)

        leftSplitPane.topComponent.minimumSize = Dimension(200, 200)
        leftSplitPane.topComponent.preferredSize = Dimension(200, 300)

        this.add("Center", mainSplitPane)
        this.add("North", BlitzMenuBar)

        ConnectionSelector.addTreeSelectionListener { treeSelectionEvent ->
            run {
                System.out.println("SelectionEv")
                val p = PropertyEditor(treeSelectionEvent.path.lastPathComponent as AbstractBlitzTreeNode)
                leftSplitPane.bottomComponent = p
                p.model.addTableModelListener {
                    (ConnectionSelector.model as DefaultTreeModel).reload(treeSelectionEvent.path.lastPathComponent as TreeNode)
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    EventQueue.invokeLater {
        BlitzRemote()
        if (!FILE.parentFile.exists()) {
            FILE.parentFile.mkdirs()
        } else {
            if (FILE.exists()) {
                LoadAction(FILE).actionPerformed(null)
            }
        }
    }
}
