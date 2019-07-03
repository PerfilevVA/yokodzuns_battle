package com.sibedge.yokodzun.android.utils.images

import android.content.Context
import androidx.core.content.ContextCompat
import okhttp3.Cache
import ru.hnau.jutils.ifFalse
import ru.hnau.jutils.tryOrNull
import java.io.File


data class DiskCacheLocation(
    val file: File,
    val freeSpace: Long
) {

    companion object {

        fun findMax(
            context: Context
        ) = ContextCompat
            .getExternalCacheDirs(context)
            .mapNotNull { file ->
                val freeSpace = getCacheFreeSpace(
                    file
                ) ?: return@mapNotNull null
                DiskCacheLocation(file, freeSpace)
            }
            .maxBy(DiskCacheLocation::freeSpace)

        private fun getCacheFreeSpace(
            cacheFile: File
        ): Long? {
            cacheFile.exists().ifFalse { return null }
            cacheFile.isDirectory.ifFalse { return null }
            cacheFile.canWrite().ifFalse { return null }
            return tryOrNull { cacheFile.freeSpace }
        }

    }

    fun toCache(cacheSizeUsePercentage: Float) =
        Cache(file, (freeSpace * cacheSizeUsePercentage).toLong())

}