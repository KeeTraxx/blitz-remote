package ch.compile.blitzremote.components

import ch.compile.blitzremote.model.AbstractBlitzTreeNode
import javax.swing.table.AbstractTableModel
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties

class PropertyEditorTableModel(private val blitzTreeNode: AbstractBlitzTreeNode) : AbstractTableModel() {
    private var members = blitzTreeNode.userObject::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()

    init {
        System.out.println("INIT Table Model")
    }

    fun getPropertyTypeAt(row: Int): KMutableProperty<*> {
        return members[row]
    }

    override fun getRowCount(): Int {
        return members.size
    }

    override fun getColumnCount(): Int {
        return 2
    }

    override fun getValueAt(row: Int, column: Int): Any? {
        return if (column == 0) {
            members[row].name
        } else {
            members[row].getter.call(blitzTreeNode.userObject) ?: ""
        }
    }

    override fun setValueAt(obj: Any?, row: Int, column: Int) {
        if (column != 1) {
            throw RuntimeException("Can't edit that!")
        }
        members[row].setter.call(blitzTreeNode.userObject, obj)
        this.fireTableDataChanged()
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return column == 1
    }
}