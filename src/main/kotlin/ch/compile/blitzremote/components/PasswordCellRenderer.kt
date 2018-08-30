package ch.compile.blitzremote.components


class PasswordCellRenderer : javax.swing.table.DefaultTableCellRenderer.UIResource() {
    override fun setValue(input: Any) {
        if (input is String) {
            this.text = if (input.isNullOrBlank()) "" else input.map{ "*" }.joinToString("")
        } else {
            super.setValue(input)
        }
    }
}