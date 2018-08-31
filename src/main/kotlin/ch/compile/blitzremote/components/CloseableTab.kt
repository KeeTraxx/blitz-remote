package ch.compile.blitzremote.components

import ch.compile.blitzremote.actions.CloseAction
import com.google.common.io.Resources
import org.slf4j.LoggerFactory
import java.awt.Component
import java.awt.FlowLayout
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.border.EmptyBorder

class CloseableTab(var component: BlitzTerminal) : JPanel(FlowLayout(FlowLayout.LEFT, 5, 0)), MouseListener {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    private val label = JLabel(component.connectionEntry.name)

    private val close = JLabel(ImageIcon(Resources.getResource("icons/icon_close.png")))

    private val contextMenu = ContextMenu(component)

    init {
        this.isOpaque = false
        this.insets.set(0, 0, 0, 0)

        this.toolTipText = component.connectionEntry.hostname

        label.border = EmptyBorder(0, 0, 0, 0)
        label.insets.set(0, 0, 0, 0)
        this.add(label)

        this.addMouseListener(this)

        close.addMouseListener(this)

        this.add(close)
    }

    override fun mouseReleased(p0: MouseEvent?) {}

    override fun mouseEntered(p0: MouseEvent?) {}

    override fun mouseClicked(mouseEvent: MouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent) && mouseEvent.source == close) {
            LOG.debug("Close button on tab clicked.")
            CloseAction(component).actionPerformed(null)
        }

        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            LOG.debug("Showing context menu...")
            contextMenu.show(mouseEvent.source as Component, mouseEvent.x, mouseEvent.y)
        }
    }

    override fun mouseExited(p0: MouseEvent?) {}

    override fun mousePressed(p0: MouseEvent?) {}

    class ContextMenu(component: Component) : JPopupMenu("TabContextMenu") {
        init {
            this.add(CloseAction(component as BlitzTerminal))
        }
    }
}


