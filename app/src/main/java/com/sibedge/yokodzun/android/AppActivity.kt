package com.sibedge.yokodzun.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import ru.hnau.androidutils.ui.TransparentStatusBarActivity
import ru.hnau.androidutils.ui.view.layer.manager.LayerManager
import ru.hnau.androidutils.ui.view.layer.manager.LayerManagerConnector
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.setFitKeyboard
import ru.hnau.androidutils.utils.put
import ru.hnau.jutils.takeIfNotEmpty
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.layers.LoginLayer
import com.sibedge.yokodzun.android.layers.main.MainLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector.layerManager

class AppActivity : TransparentStatusBarActivity() {

    companion object {

        private const val START_TEST_ATTEMPT_UUID_KEY = "START_TEST_ATTEMPT_UUID"

        fun createStartTestAttemptIntent(
            context: Context,
            testAttemptUUID: String
        ) = createIntent(context) {
            put(START_TEST_ATTEMPT_UUID_KEY, testAttemptUUID)
        }

        fun createIntent(
            context: Context,
            configurator: Intent.() -> Unit
        ) = Intent(
            context,
            AppActivity::class.java
        ).apply(configurator)

    }

    private val contentView
            by lazy { AppActivityView(this@AppActivity) }

    val layerManagerConnector: LayerManagerConnector
        get() = contentView.layerManagerConnector

    val view: View
        get() = contentView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        AppActivityConnector.onAppActivityCreated(this)

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(null)
        val startTestAttemptUUID = intent?.getStringExtra(START_TEST_ATTEMPT_UUID_KEY)?.takeIfNotEmpty() ?: return
        contentView.startTestAttempt(startTestAttemptUUID)
    }

    override fun onDestroy() {
        AppActivityConnector.onAppActivityDestroyed(this)
        super.onDestroy()
    }

    override fun getIsStatusBarIconsLight() = true

    override fun onBackPressed() {
        if (contentView.handleGoBack()) {
            return
        }
        super.onBackPressed()
    }

}
