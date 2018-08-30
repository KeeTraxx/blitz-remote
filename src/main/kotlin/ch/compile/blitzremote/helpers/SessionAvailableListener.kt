package ch.compile.blitzremote.helpers

import com.jcraft.jsch.Session

interface SessionAvailableListener {
    fun onSessionAvailable(session: Session)
}
