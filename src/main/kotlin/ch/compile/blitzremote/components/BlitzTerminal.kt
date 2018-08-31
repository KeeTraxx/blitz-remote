package ch.compile.blitzremote.components

import ch.compile.blitzremote.model.ConnectionEntry
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.TerminalAction
import com.jediterm.terminal.ui.settings.SettingsProvider

class BlitzTerminal(settingsProvider: SettingsProvider, val connectionEntry: ConnectionEntry) : JediTermWidget(settingsProvider) {
    override fun getActions(): List<TerminalAction> {
        return super.getActions()
    }
}