package ch.compile.blitzremote.model

import ch.compile.blitzremote.components.PasswordCellRenderer
import ch.compile.blitzremote.helpers.UseCellRenderer
import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
class ConnectionEntry(override var name: String = "New Connection", var hostname: String = "localhost", username: String? = "", password: String? = "", var port: Int = 22) : BlitzModel(name), Serializable {

    var username = username
        get() = if (field.isNullOrBlank()) null else field

    @UseCellRenderer(PasswordCellRenderer::class)
    var password = password
        get() = if (field.isNullOrBlank()) null else field

    var portforwarding: String? = null
        get() = if (field.isNullOrBlank()) null else field

    var identity: String? = null
        get() = if (field.isNullOrBlank()) null else field


    override fun toString(): String {
        return name
    }
}