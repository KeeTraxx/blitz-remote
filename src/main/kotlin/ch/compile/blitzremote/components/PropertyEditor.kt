package ch.compile.blitzremote.components

import ch.compile.blitzremote.helpers.UseCellRenderer
import ch.compile.blitzremote.model.AbstractBlitzTreeNode
import javax.swing.JTable
import javax.swing.table.TableCellRenderer
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

class PropertyEditor(blitzTreeNode: AbstractBlitzTreeNode) : JTable(PropertyEditorTableModel(blitzTreeNode)) {

    init {

    }

    override fun getCellRenderer(row: Int, column: Int): TableCellRenderer {
        if (column == 0) {
            return super.getCellRenderer(row, column)
        }
        val m = this.model as PropertyEditorTableModel
        val prop = m.getPropertyTypeAt(row)
        val useCellRenderer = prop.findAnnotation<UseCellRenderer>()
        return if(useCellRenderer != null) {
            return useCellRenderer.value.createInstance()
        } else {
            super.getCellRenderer(row, column)
        }

    }
}