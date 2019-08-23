package com.sibedge.yokodzun.android

import android.os.Bundle
import android.view.View
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import ru.hnau.androidutils.ui.TransparentStatusBarActivity
import ru.hnau.androidutils.ui.view.layer.manager.LayerManagerConnector
import ru.hnau.jutils.ifTrue


class AppActivity : TransparentStatusBarActivity() {

    private val contentView
            by lazy { AppActivityView(this@AppActivity) }

    val layerManagerConnector: LayerManagerConnector
        get() = contentView.layerManagerConnector

    val view: View
        get() = contentView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppActivityConnector.onAppActivityCreated(this)
        setContentView(contentView)
    }

    override fun onDestroy() {
        AppActivityConnector.onAppActivityDestroyed(this)
        super.onDestroy()
    }

    override fun getIsStatusBarIconsLight() = true

    override fun onBackPressed() {
        contentView.handleGoBack().ifTrue { return }
        super.onBackPressed()
    }

}