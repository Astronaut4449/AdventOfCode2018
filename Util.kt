import java.io.File

val File.lines get() = mutableListOf<String>().apply {
    forEachLine {
        this.add(it)
    }
}

fun Boolean.toInt() = if(this) 1 else 0

