package ch.compile.blitzremote.actions

import ch.compile.blitzremote.helpers.BlitzObjectMapper
import ch.compile.blitzremote.model.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.AbstractAction

class LoadAction(private val file: File) : AbstractAction("Load") {
    override fun actionPerformed(p0: ActionEvent?) {

        val c = BlitzObjectMapper.readTree(file)
        if (c.isObject) {
            System.out.println(c)
            // ConnectionModel.setRoot(root)
            val root = parse(c)

            System.out.println(root)

            ConnectionModel.setRoot(root)

        }

    }

    fun parse(jsonNode: JsonNode): AbstractBlitzTreeNode? {
        when (jsonNode.get("type").asText()) {
            "ch.compile.blitzremote.model.ConnectionFolder" -> {
                val node = ConnectionFolderTreeNode(jsonNode.get("name").asText())
                val children = jsonNode.get("children") as ArrayNode
                children.forEach {
                    node.add(parse(it))
                }
                return node

            }
            "ch.compile.blitzremote.model.ConnectionEntry" -> {
                val node = ConnectionEntryTreeNode(jsonNode.get("name").asText())
                val connectionEntry = node.userObject as ConnectionEntry
                connectionEntry.hostname = jsonNode.get("hostname").asText()
                connectionEntry.username = jsonNode.get("username")?.asText()
                connectionEntry.password = jsonNode.get("password")?.asText()
                connectionEntry.port = jsonNode.get("port").asInt()
                connectionEntry.portforwarding = jsonNode.get("portforwarding")?.asText()
                connectionEntry.identity = jsonNode.get("identity")?.asText()
                connectionEntry.httpProxy = jsonNode.get("httpProxy")?.asText()
                return node
            }
            else -> {
                throw RuntimeException("Error parsing file.")
            }
        }
    }

}
