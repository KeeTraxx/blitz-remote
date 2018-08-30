package ch.compile.blitzremote.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.io.Serializable
import javax.swing.Action
import javax.swing.tree.DefaultMutableTreeNode

@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class AbstractBlitzTreeNode(obj: Any, hasChildren: Boolean) : DefaultMutableTreeNode(obj, hasChildren), Serializable {
    open val actions: List<Action>
        get() = listOf()

    override fun toString(): String {
        return this.userObject.toString()
    }


}