package com.sibedge.yokodzun.android.layers

import android.content.Context
import android.net.Uri
import retrofit2.http.Url
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.setPadding
import ru.hnau.androidutils.utils.shortToast
import ru.hnau.jutils.getter.toGetter
import ru.hnau.jutils.tryOrNull
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.api.resetApi
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import com.sibedge.yokodzun.android.utils.managers.SettingsManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.remote_teaching_common.utils.Validators
import java.net.URL


class SettingsLayer(
    context: Context
) : AppLayer(
    context = context
) {

    override val title = StringGetter(R.string.settings_layer_title)


    override fun afterCreate() {
        super.afterCreate()

        content {

            setPadding(SizeManager.LARGE_SEPARATION, SizeManager.DEFAULT_SEPARATION)

            addFgSmallInputLabelView(StringGetter(R.string.settings_layer_host))

            val hostInput = SimpleInputView(
                context = context,
                info = SimpleInputViewInfo(
                    maxLength = Validators.MAX_SURNAME_LENGTH
                ),
                text = SettingsManager.host.toGetter()
            )
            addChild(hostInput)

            addLinearSeparator()

            addBottomButtonView(
                text = StringGetter(R.string.dialog_save),
                onClick = {
                    saveSettings(
                        host = hostInput.text.toString()
                    )
                }
            )

        }
    }

    private fun saveSettings(
        host: String
    ) {

        val hostUri = tryOrNull { Uri.parse(host) }
        if (hostUri == null) {
            ErrorHandler.handle(StringGetter(R.string.settings_layer_host_incorrect))
            return
        }

        val formattedHost = formatHost(host)
        SettingsManager.host = formattedHost
        resetApi()

        shortToast(StringGetter(R.string.settings_layer_success))
        managerConnector.goBack()
    }

    private fun formatHost(host: String): String {
        var result = host.toLowerCase().trim()

        if (result.endsWith("/")) {
            result = result.dropLast(1)
        }

        if (!(result.startsWith("http://") || result.startsWith("https://"))) {
            result = "http://" + result
        }

        return result
    }

}