package ch.compile.blitzremote.actions

import ch.compile.blitzremote.BlitzRemote
import ch.compile.blitzremote.FILE
import ch.compile.blitzremote.helpers.BlitzObjectMapper
import ch.compile.blitzremote.model.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.AbstractAction
import javax.swing.JOptionPane

class LoadAction(private val file: File) : AbstractAction("Load") {
    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)!!
    }

    override fun actionPerformed(p0: ActionEvent?) {
        if (file.exists()) {
            FILE = file
            LOG.info("Loading $file...")
            try {
                val c = BlitzObjectMapper.readTree(file)
                if (c.isObject) {
                    val root = parse(c)
                    ConnectionModel.setRoot(root)
                }
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(BlitzRemote.instance, "Couldn't parse file: ${file.absoluteFile}", "Blitz Remote ERROR", JOptionPane.ERROR_MESSAGE)
            }
        } else {
            JOptionPane.showMessageDialog(BlitzRemote.instance, "File ${file.absoluteFile} doesn't exist!", "Blitz Remote ERROR", JOptionPane.ERROR_MESSAGE)
        }
    }

    private fun parse(jsonNode: JsonNode): AbstractBlitzTreeNode? {
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
