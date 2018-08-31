package ch.compile.blitzremote.components

import java.awt.Component
import javax.swing.JTabbedPane

object TabbedSSHPanel : JTabbedPane() {

    fun add(blitzTerminal: BlitzTerminal): Component {
        super.add(blitzTerminal)
        val index = indexOfComponent(blitzTerminal)
        setTabComponentAt(index, CloseableTab(blitzTerminal))
        return blitzTerminal
    }
}