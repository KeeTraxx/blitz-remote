package ch.compile.blitzremote.helpers

import javax.swing.table.TableCellRenderer
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
annotation class UseCellRenderer(val value: KClass<out TableCellRenderer>)
