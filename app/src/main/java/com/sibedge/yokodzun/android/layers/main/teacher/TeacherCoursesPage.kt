package com.sibedge.yokodzun.android.layers.main.teacher

import android.content.Context
import android.widget.FrameLayout
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.coroutines.createUIJob
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.utils.shortToast
import ru.hnau.androidutils.utils.showToast
import ru.hnau.jutils.producer.locked_producer.SuspendLockedProducer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.section_info.SectionInfoLayer
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addMainActonButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addWaiter
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.ui.list.base.ItemCellListContaner
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import com.sibedge.yokodzun.android.utils.tryOrHandleError
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import ru.hnau.remote_teaching_common.utils.Validators


class TeacherCoursesPage(
    context: Context
) : FrameLayout(
    context
) {

    companion object {

        private val coursesProducer =
            SectionsInfoManager.COURSES

    }

    private val suspendLockedProducer = SuspendLockedProducer()

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val uiJob = createUIJob(
        isVisibleToUserProducer,
        ErrorHandler
    )

    init {

        val list = ItemCellListContaner(
            context = context,
            producer = coursesProducer.map { it.map { it.subsections } },
            cellDataGetter = { it.getLineCellData { onSectionMenuClick(it) } },
            idGetter = { it.uuid },
            onClick = this::onSectionClick,
            invalidator = coursesProducer::invalidate,
            onEmptyListInfoViewGenerator = {
                EmptyInfoView(
                    context = context,
                    text = StringGetter(R.string.teacher_main_view_courses_page_empty_info_title),
                    button = StringGetter(R.string.teacher_main_view_courses_page_empty_info_button) to this::createSubsection
                )
            }
        )

        addChild(list)

        addMainActonButtonView(
            icon = DrawableGetter(R.drawable.ic_add_white),
            title = StringGetter(R.string.teacher_main_view_courses_page_add_course),
            needShowTitle = list.onListScrolledToTopProducer.not(),
            onClick = this::createSubsection
        )

        addWaiter(suspendLockedProducer)
    }

    private fun onSectionClick(sectionSkeleton: SectionSkeleton) {
        AppActivityConnector.showLayer({ SectionInfoLayer.newInstance(context, sectionSkeleton) })
    }

    private fun onSectionMenuClick(sectionSkeleton: SectionSkeleton) {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.teacher_main_view_courses_page_options_title, sectionSkeleton.title))
            closeItem(StringGetter(R.string.teacher_main_view_courses_page_options_rename)) {
                onRenameClick(sectionSkeleton)
            }
            closeItem(StringGetter(R.string.dialog_delete)) {
                onDeleteClick(sectionSkeleton)
            }
        }
    }

    private fun onRenameClick(sectionSkeleton: SectionSkeleton) {
        AppActivityConnector.showInputDialog(
            title = StringGetter(R.string.teacher_main_view_courses_page_rename_dialog_title),
            text = StringGetter(R.string.teacher_main_view_courses_page_rename_dialog_text),
            confirmButtonText = StringGetter(R.string.teacher_main_view_courses_page_rename_dialog_button),
            inputInitialText = sectionSkeleton.title.toGetter(),
            inputInfo = SimpleInputViewInfo(
                maxLength = Validators.MAX_SECTION_TITLE_LENGTH
            ),
            onConfirm = { onSectionNewTitleEntered(sectionSkeleton, it) }
        )
    }

    private fun onDeleteClick(sectionSkeleton: SectionSkeleton) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.teacher_main_view_courses_page_option_delete_confirm_dialog_title),
            text = StringGetter(
                R.string.teacher_main_view_courses_page_option_delete_confirm_dialog_text,
                sectionSkeleton.title
            ),
            confirmText = StringGetter(R.string.dialog_delete)
        ) {
            uiJob {
                suspendLockedProducer {
                    coursesProducer.deleteSubsection(sectionSkeleton.uuid)
                    showToast(
                        StringGetter(
                            R.string.teacher_main_view_courses_page_options_delete_success,
                            sectionSkeleton.title
                        )
                    )
                }
            }

        }
    }


    private fun createSubsection() {
        AppActivityConnector.showInputDialog(
            title = StringGetter(R.string.teacher_main_view_courses_page_create_new_dialog_title),
            text = StringGetter(R.string.teacher_main_view_courses_page_create_new_dialog_text),
            confirmButtonText = StringGetter(R.string.teacher_main_view_courses_page_create_new_dialog_button),
            inputInfo = SimpleInputViewInfo(
                maxLength = Validators.MAX_SECTION_TITLE_LENGTH
            ),
            onConfirm = this::onNewSectionTitleEntered
        )
    }

    private fun onNewSectionTitleEntered(sectionTitle: String): Boolean {
        tryOrHandleError {
            Validators.validateSectionTitleOrThrow(sectionTitle)
        } ?: return false

        uiJob {
            suspendLockedProducer {
                coursesProducer.addSubsection(sectionTitle)
                shortToast(StringGetter(R.string.teacher_main_view_courses_page_create_new_success, sectionTitle))
            }
        }
        return true
    }

    private fun onSectionNewTitleEntered(
        sectionSkeleton: SectionSkeleton,
        newTitle: String
    ): Boolean {
        tryOrHandleError {
            Validators.validateSectionTitleOrThrow(newTitle)
        } ?: return false

        uiJob {
            suspendLockedProducer {
                coursesProducer.renameSubsection(sectionSkeleton.uuid, newTitle = newTitle)
                shortToast(StringGetter(R.string.teacher_main_view_courses_page_rename_success, newTitle))
            }
        }
        return true
    }

}