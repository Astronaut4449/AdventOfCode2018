import java.io.File

data class Fabric(val id: Int, val left: Int, val top: Int, val width: Int, val height: Int)

fun main(args: Array<String>) {
    val fabrics = mutableListOf<Fabric>()

    // Parse file and create fabric objects
    val re = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()

    File("Input/03.txt").forEachLine {
        val match = re.matchEntire(it)!!

        fabrics.add(
            Fabric(
                id = match.groups[1]!!.value.toInt(),
                left = match.groups[2]!!.value.toInt(),
                top = match.groups[3]!!.value.toInt(),
                width = match.groups[4]!!.value.toInt(),
                height = match.groups[5]!!.value.toInt()
            )
        )
    }

    // Create overlap matrix
    val overlaps = object {
        private val matrix = Array(1000) { Array(1000) { 0 } }

        init {
            fabrics.forEach { add(it) }
        }

        private fun add(fabric: Fabric) {
            for (i in fabric.left until (fabric.left + fabric.width)) for (j in fabric.top until (fabric.top + fabric.height)) {
                matrix[i][j] += 1
            }
        }

        fun count() = matrix.sumBy { it.count { it > 1 } }

        fun with(fabric: Fabric): Boolean {
            for (i in fabric.left until (fabric.left + fabric.width)) for (j in fabric.top until (fabric.top + fabric.height)) {
                if (matrix[i][j] != 1) {
                    return true
                }
            }
            return false
        }
    }

    // Part 1
    val totalOverlaps = overlaps.count()
    println("Part 1: $totalOverlaps")

    // Part 2
    val idNotOverlapping = fabrics.asSequence()
        .filterNot { overlaps.with(it) }
        .first().id

    println("Part 2: $idNotOverlapping")
    
}

