package ch.compile.blitzremote.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable
import javax.swing.tree.DefaultMutableTreeNode
import kotlin.streams.asStream
import kotlin.streams.toList

@JsonInclude(JsonInclude.Include.NON_NULL)
class ConnectionFolder(override var name: String = "New folder") : Serializable, HasChildren, BlitzModel(name) {


    override val children: List<BlitzModel>
        get() {
            val e = (ConnectionModel.root as DefaultMutableTreeNode).preorderEnumeration()
            val ed = e.toList().find { (it as AbstractBlitzTreeNode).userObject == this@ConnectionFolder } as AbstractBlitzTreeNode?
            return if (ed != null) {
                val children = ed.children()
                children.asSequence().asStream().map { (it as AbstractBlitzTreeNode).userObject as BlitzModel }.toList()
            } else {
                emptyList()
            }

        }

    override fun toString(): String {
        return name
    }
}