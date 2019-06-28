package com.sibedge.yokodzun.android.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.coroutines.UIJob
import ru.hnau.androidutils.utils.ContextConnector
import ru.hnau.androidutils.utils.shortToast
import ru.hnau.jutils.ifFalse
import ru.hnau.jutils.possible.Possible
import ru.hnau.jutils.tryCatch
import ru.hnau.jutils.tryOrNull
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.extensions.text
import com.sibedge.yokodzun.android.utils.managers.CrashliticsManager
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

object Utils {

    fun genUUID() = UUID.randomUUID()
        .toString()
        .replace("-", "")
        .toUpperCase()

   /* fun copyTextToClipboard(label: String, text: String) {
        (ContextConnector.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .primaryClip = ClipData.newPlainText(label, text)
    }*/

}

inline fun <T : Any> tryOrHandleError(action: () -> T) =
    tryCatch(
        throwsAction = { action.invoke() },
        onThrow = ErrorHandler::handle
    )

inline fun <T : Any> tryOrLogToCrashlitics(
    action: () -> T
) = tryCatch(
    throwsAction = { action.invoke() },
    onThrow = { CrashliticsManager.handle(it) }
)

@Deprecated("Use from JUtils")
fun <T> Iterable<T>.minusAt(position: Int) =
    filterIndexed { index, _ -> index != position }

fun <T> Iterable<T>.minusFirst(predicate: (T) -> Boolean): List<T> {
    var dropped = false
    var result = ArrayList<T>(count())
    for (element in this) {
        if (dropped || !predicate(element)) {
            result.add(element)
        } else {
            dropped = true
        }
    }
    return result
}


