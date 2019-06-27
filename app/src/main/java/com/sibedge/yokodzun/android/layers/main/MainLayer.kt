package com.sibedge.yokodzun.android.layers.main

import android.content.Context
import android.view.ViewGroup
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.MATCH_PARENT
import ru.hnau.androidutils.ui.view.utils.setLinearParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.helpers.VariableConnector
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.main.admin.AdminContentView
import com.sibedge.yokodzun.android.layers.main.student.StudentContentView
import com.sibedge.yokodzun.android.layers.main.teacher.TeacherContentView
import com.sibedge.yokodzun.android.ui.addSuspendPresenter
import ru.hnau.remote_teaching_common.data.User
import ru.hnau.remote_teaching_common.data.UserRole


class MainLayer(
    context: Context
) : AppLayer(
    context = context,
    showGoBackButton = false
) {

    @LayerState
    private var teacherContentPageNumber = 0

    private val teacherContentPageNumberConnector: VariableConnector<Int> by lazy {
        VariableConnector(
            getter = { teacherContentPageNumber },
            setter = { teacherContentPageNumber = it }
        )
    }

    override val title: StringGetter
        get() = MeInfoManager.existenceValue?.login
            ?.let { "@$it".toGetter() }
            ?: StringGetter(R.string.app_name)

    init {
        content {
            addSuspendPresenter(
                producer = MeInfoManager as Producer<GetterAsync<Unit, User>>,
                invalidator = MeInfoManager::invalidate,
                contentViewGenerator = this@MainLayer::createContentView
            ) {
                setLinearParams(MATCH_PARENT, 0, 1f)
            }
        }
    }

    private fun createContentView(user: User): ViewGroup {
        updateTitle()
        return when (user.role) {
            UserRole.ADMIN -> AdminContentView(context)
            UserRole.TEACHER -> TeacherContentView(context, teacherContentPageNumberConnector)
            UserRole.STUDENT -> StudentContentView(context, teacherContentPageNumberConnector, user)
        }
    }

}