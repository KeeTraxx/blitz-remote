package ch.compile.blitzremote.components

import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.TerminalAction
import com.jediterm.terminal.ui.settings.SettingsProvider

class BlitzTerminal(settingsProvider: SettingsProvider) : JediTermWidget(settingsProvider) {
    override fun getActions(): List<TerminalAction> {
        return super.getActions()
    }
}