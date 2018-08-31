package ch.compile.blitzremote.components

import ch.compile.blitzremote.actions.CloseAction
import java.awt.Component
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.plaf.TabbedPaneUI

object TabbedSSHPanel : JTabbedPane() {
    init {
        this.addChangeListener {
            System.out.println(TabbedSSHPanel.selectedComponent)
        }

        this.addMouseListener(object : MouseListener {
            override fun mouseReleased(p0: MouseEvent?) {

            }

            override fun mouseEntered(p0: MouseEvent?) {
            }

            override fun mouseClicked(p0: MouseEvent) {
                if (SwingUtilities.isRightMouseButton(p0)) {
                    System.out.println("RIGHT MOUSY")

                    val contextMenu = JPopupMenu()

                    val tabIndex = (TabbedSSHPanel.ui as TabbedPaneUI).tabForCoordinate(TabbedSSHPanel, p0.x, p0.y)

                    System.out.println(p0.x)
                    System.out.println(p0.y)
                    System.out.println(tabIndex)

                    val tab = TabbedSSHPanel.getComponent(tabIndex)

                    contextMenu.add(CloseAction(tab as BlitzTerminal))

                    contextMenu.show(this@TabbedSSHPanel, p0.x, p0.y)
                }
            }

            override fun mouseExited(p0: MouseEvent?) {
            }

            override fun mousePressed(p0: MouseEvent?) {
            }

        })
    }

    fun add(title: String, blitzTerminal: BlitzTerminal): Component {
        super.add(title, blitzTerminal)
        val index = indexOfComponent(blitzTerminal)
        setTabComponentAt(index, CloseableTab(blitzTerminal))
        return blitzTerminal
    }
}