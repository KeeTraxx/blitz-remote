package ch.compile.blitzremote.settings

import com.jediterm.terminal.TerminalColor
import com.jediterm.terminal.TextStyle
import com.jediterm.terminal.ui.UIUtil
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider
import java.awt.Font

class BlitzRemoteSettingsProvider: DefaultSettingsProvider() {
    override fun getTerminalFont(): Font {
        val fontName: String = when {
            UIUtil.isWindows -> "Consolas"
            UIUtil.isMac -> "Menlo"
            else -> "Fira Code"
        }
        return Font.decode(fontName).deriveFont(this.terminalFontSize)
    }

    override fun getTerminalFontSize(): Float {
        return 12.0F
    }

    override fun getDefaultStyle(): TextStyle {
        return TextStyle(TerminalColor.rgb(200,200,200), TerminalColor.BLACK)
    }
}