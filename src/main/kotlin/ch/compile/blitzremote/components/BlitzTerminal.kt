package ch.compile.blitzremote.components

import ch.compile.blitzremote.model.ConnectionEntry
import ch.compile.blitzremote.settings.BlitzRemoteSettingsProvider
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.TerminalAction
import com.jediterm.terminal.ui.settings.SettingsProvider

class BlitzTerminal(val connectionEntry: ConnectionEntry) : JediTermWidget(BlitzRemoteSettingsProvider()) {
    override fun getActions(): List<TerminalAction> {
        return super.getActions()
    }
}