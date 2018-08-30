package ch.compile.blitzremote.model

open class BlitzModel(open var name:String = "New X") {
    val type: String?
        get() = this::class.qualifiedName
}
