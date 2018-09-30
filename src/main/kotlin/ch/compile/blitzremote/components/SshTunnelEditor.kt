package ch.compile.blitzremote.components

import ch.compile.blitzremote.BlitzRemote
import javax.swing.JDialog
import javax.swing.JTable
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

class SshTunnelEditor(blitzTerminal: BlitzTerminal) : JDialog(BlitzRemote.instance) {
    private val list = JTable(PortforwardingTableModel(blitzTerminal.session?.portForwardingL ?: emptyArray()))
    init {
        this.setSize(640, 480)
        this.add(list)
    }

    class PortforwardingTableModel(val forwardings:Array<String>): TableModel {
        val tableModelListeners = ArrayList<TableModelListener>()
        override fun addTableModelListener(l: TableModelListener) {
            tableModelListeners.add(l)
        }

        override fun getRowCount(): Int {
            return forwardings.size
        }

        override fun getColumnName(columnIndex: Int): String {
            return when (columnIndex) {
                0 -> "Port forwarding"
                else -> "unknown"
            }
        }

        override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
            return true
        }

        override fun getColumnClass(columnIndex: Int): Class<*> {
            return String::class.java
        }

        override fun setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) {
            forwardings[rowIndex] = aValue.toString()
        }

        override fun getColumnCount(): Int {
            return 1
        }

        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
            return forwardings[rowIndex]
        }

        override fun removeTableModelListener(l: TableModelListener?) {
            tableModelListeners.remove(l)
        }

    }
}