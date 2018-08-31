package ch.compile.blitzremote.components

import ch.compile.blitzremote.actions.CloseAction
import sun.java2d.loops.Blit
import java.awt.Component
import java.awt.FlowLayout
import java.awt.Insets
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.border.EmptyBorder

class CloseableTab(val component: BlitzTerminal) : JPanel(FlowLayout(FlowLayout.LEFT, 5, 0)), MouseListener {


    private val label = JLabel(component.connectionEntry.name)

    private val close = JButton(CloseTabAction(component))

    private val contextMenu = ContextMenu(component)

    init {
        this.isOpaque = false
        this.insets.set(0, 0, 0, 0)

        this.toolTipText = component.connectionEntry.hostname

        label.border = EmptyBorder(0, 0, 0, 0)
        label.insets.set(0, 0, 0, 0)
        this.add(label)

        close.border = EmptyBorder(4, 4, 4, 4)
        close.margin = Insets(0, 0, 0, 0)
        close.insets.set(0, 0, 0, 0)
        close.isBorderPainted = false

        this.addMouseListener(this)

        this.add(close)
    }

    override fun mouseReleased(p0: MouseEvent?) {}

    override fun mouseEntered(p0: MouseEvent?) {}

    override fun mouseClicked(mouseEvent: MouseEvent) {
        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            contextMenu.show(this, mouseEvent.x, mouseEvent.y)
        }
    }

    override fun mouseExited(p0: MouseEvent?) {}

    override fun mousePressed(p0: MouseEvent?) {}

    class CloseTabAction(component: Component) : CloseAction(component as BlitzTerminal) {
        init {
            this.putValue("Name", "❌")
        }
    }

    class ContextMenu(component: Component) : JPopupMenu("TabContextMenu") {
        init {
            this.add(CloseAction(component as BlitzTerminal))
        }
    }
}


