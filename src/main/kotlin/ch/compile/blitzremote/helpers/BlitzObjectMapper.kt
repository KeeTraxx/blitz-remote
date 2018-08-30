package ch.compile.blitzremote.helpers

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper

object BlitzObjectMapper : ObjectMapper() {
    init {
        this.disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
        this.writerWithView(FileView::class.java)
    }
}
