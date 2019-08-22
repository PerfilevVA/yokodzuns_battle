package com.sibedge.yokodzun.android.utils

import ru.hnau.jutils.ifFalse
import java.io.File

object FileUtils {

    fun extractListToFile (
        path: File,
        name: String,
        list: List<String>
    ) : File? {
        list.ifEmpty { return null }
        return File(path, name).apply {
            createNewFile().ifFalse { writeText("") }
            bufferedWriter().use { writer ->
                list.forEach { item ->
                    writer.write(item)
                    writer.newLine()
                }
            }
        }
    }
}