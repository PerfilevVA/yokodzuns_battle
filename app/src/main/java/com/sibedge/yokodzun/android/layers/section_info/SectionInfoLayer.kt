package com.sibedge.yokodzun.android.layers.section_info

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.auto_swipe_refresh_view.AutoSwipeRefreshView
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.MATCH_PARENT
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.scroll.view.createIsScrolledToTopProducer
import ru.hnau.androidutils.ui.view.utils.scroll.view.createOnScrolledProducer
import ru.hnau.androidutils.ui.view.utils.setLinearParams
import ru.hnau.androidutils.utils.shortToast
import ru.hnau.androidutils.utils.showToast
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.getter.base.get
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.StateProducerSimple
import ru.hnau.jutils.producer.extensions.not
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.SectionsInfoManager
import com.sibedge.yokodzun.android.data.TestsTasksManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.edit_content_md.EditContentMDLayer
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerList
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerListCallback
import com.sibedge.yokodzun.android.layers.test_info.TestInfoLayer
import com.sibedge.yokodzun.android.ui.SuspendPresenter
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addMainActonButtonView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.ui.plus_minus.PlusMinusColumnInfo
import com.sibedge.yokodzun.android.utils.extensions.passScorePercentageToUiString
import com.sibedge.yokodzun.android.utils.extensions.timeLimitToUiString
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.DialogManager
import com.sibedge.yokodzun.android.utils.tryOrHandleError
import ru.hnau.remote_teaching_common.data.section.SectionInfo
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import ru.hnau.remote_teaching_common.data.test.TestSkeleton
import ru.hnau.remote_teaching_common.utils.Validators


class SectionInfoLayer(
    context: Context
) : AppLayer(
    context = context
), SectionInfoLayerListCallback {
    companion object {

        fun newInstance(
            context: Context,
            section: SectionSkeleton
        ) = SectionInfoLayer(context).apply {
            sectionSkeleton = section
        }

    }

    @LayerState
    private lateinit var sectionSkeleton: SectionSkeleton

    override val title: StringGetter by lazy {
        StringGetter(R.string.section_info_layer_title, sectionSkeleton.title)
    }

    private val suspendSectionInfoInfoManager: SectionsInfoManager by lazy {
        SectionsInfoManager[sectionSkeleton.uuid]
    }

    private val sectionInfoProducer =
        StateProducerSimple<SectionInfo>()

    private val listView: View by lazy {
        SectionInfoLayerList(
            context = context,
            callback = this@SectionInfoLayer,
            sectionInfoProducer = sectionInfoProducer
        )
    }

    private val emptyInfoView: View by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.section_info_layer_no_info_title),
            button = StringGetter(R.string.section_info_layer_no_info_button) to this::onCreateInfoClick
        )
    }

    private val suspendLoader: View by lazy {
        SuspendPresenter<SectionInfo>(
            context = context,
            invalidator = suspendSectionInfoInfoManager::invalidate,
            contentViewGenerator = this::getContentView,
            producer = suspendSectionInfoInfoManager as Producer<GetterAsync<Unit, SectionInfo>>
        )
    }

    private val swipeRefreshView: View by lazy {
        AutoSwipeRefreshView(
            content = suspendLoader,
            color = ColorManager.PRIMARY,
            updateContent = suspendSectionInfoInfoManager::invalidate
        )
    }


    private val sectionInfoLayerContentContainer: View by lazy {

        val onListScrolledProducer =
            listView.createOnScrolledProducer()

        val isListScrolledToTopProducer =
            listView.createIsScrolledToTopProducer(onListScrolledProducer)

        FrameLayout(context).apply {
            setLinearParams(MATCH_PARENT, 0, 1f)
            addChild(swipeRefreshView)
            addMainActonButtonView(
                icon = DrawableGetter(R.drawable.ic_edit_white),
                title = StringGetter(R.string.section_info_layer_edit),
                needShowTitle = isListScrolledToTopProducer.not(),
                onClick = this@SectionInfoLayer::onCreateInfoClick
            )
        }
    }

    override fun afterCreate() {
        super.afterCreate()
        content {
            addChild(sectionInfoLayerContentContainer)
        }
    }

    private fun getContentView(content: SectionInfo): View {
        if (content.contentMD.isEmpty() && content.subsections.isEmpty() && content.tests.isEmpty()) {
            return emptyInfoView
        }
        sectionInfoProducer.updateState(content)
        return listView
    }

    private fun onCreateInfoClick() {
        DialogManager.showBottomSheet(managerConnector) {
            title(StringGetter(R.string.section_info_layer_add_info_dialog_title))
            closeItem(
                StringGetter(R.string.section_info_layer_add_info_dialog_edit_content),
                this@SectionInfoLayer::onEditContentMDClick
            )
            closeItem(
                StringGetter(R.string.section_info_layer_add_info_dialog_add_subsection),
                this@SectionInfoLayer::onAddSubsectionClick
            )
            closeItem(
                StringGetter(R.string.section_info_layer_add_info_dialog_add_test),
                this@SectionInfoLayer::onAddTestClick
            )
        }
    }

    override fun onEditContentMDClick() = showLayer(
        EditContentMDLayer.newInstance(
            context,
            sectionSkeleton
        )
    )

    override fun onAddSubsectionClick() {
        AppActivityConnector.showInputDialog(
            title = StringGetter(R.string.section_info_layer_create_new_subsection_dialog_title),
            text = StringGetter(R.string.section_info_layer_create_new_subsection_dialog_text),
            confirmButtonText = StringGetter(R.string.section_info_layer_create_new_subsection_dialog_button),
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

        uiJobLocked {
            suspendSectionInfoInfoManager.addSubsection(sectionTitle)
            shortToast(StringGetter(R.string.section_info_layer_create_new_subsection_success, sectionTitle))
        }
        return true
    }

    override fun onAddTestClick() {
        AppActivityConnector.showInputDialog(
            title = StringGetter(R.string.section_info_layer_create_new_test_dialog_title),
            text = StringGetter(R.string.section_info_layer_create_new_test_dialog_text),
            confirmButtonText = StringGetter(R.string.section_info_layer_create_new_test_dialog_button),
            inputInfo = SimpleInputViewInfo(
                maxLength = Validators.MAX_TEST_TITLE_LENGTH
            ),
            onConfirm = this::onNewTestTitleEntered
        )
    }

    private fun onNewTestTitleEntered(testTitle: String): Boolean {
        tryOrHandleError {
            Validators.validateTestTitleOrThrow(testTitle)
        } ?: return false

        uiJobLocked {
            suspendSectionInfoInfoManager.addTest(testTitle)
            shortToast(StringGetter(R.string.section_info_layer_create_new_test_success, testTitle))
        }
        return true
    }

    override fun onSubsectionClick(subsection: SectionSkeleton) =
        showLayer(SectionInfoLayer.newInstance(context, subsection))

    override fun onSubsectionMenuClick(subsection: SectionSkeleton) {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.section_info_layer_subsection_options_title, subsection.title))
            closeItem(StringGetter(R.string.section_info_layer_subsection_options_rename)) {
                onRenameSubsectionClick(subsection)
            }
            closeItem(StringGetter(R.string.dialog_delete)) {
                onDeleteSubsectionClick(subsection)
            }
        }
    }

    private fun onRenameSubsectionClick(sectionSkeleton: SectionSkeleton) {
        AppActivityConnector.showInputDialog(
            title = StringGetter(R.string.section_info_layer_rename_subsection_dialog_title),
            text = StringGetter(R.string.section_info_layer_rename_subsection_dialog_text),
            confirmButtonText = StringGetter(R.string.section_info_layer_rename_subsection_dialog_button),
            inputInitialText = sectionSkeleton.title.toGetter(),
            inputInfo = SimpleInputViewInfo(
                maxLength = Validators.MAX_SECTION_TITLE_LENGTH
            ),
            onConfirm = { onSectionNewTitleEntered(sectionSkeleton, it) }
        )
    }

    private fun onSectionNewTitleEntered(
        sectionSkeleton: SectionSkeleton,
        newTitle: String
    ): Boolean {
        tryOrHandleError {
            Validators.validateSectionTitleOrThrow(newTitle)
        } ?: return false

        uiJobLocked {
            suspendSectionInfoInfoManager.renameSubsection(sectionSkeleton.uuid, newTitle = newTitle)
            shortToast(StringGetter(R.string.section_info_layer_rename_subsection_success, newTitle))
        }
        return true
    }

    private fun onDeleteSubsectionClick(sectionSkeleton: SectionSkeleton) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.section_info_layer_option_delete_subsection_confirm_dialog_title),
            text = StringGetter(
                R.string.section_info_layer_option_delete_subsection_confirm_dialog_text,
                sectionSkeleton.title
            ),
            confirmText = StringGetter(R.string.dialog_delete)
        ) {
            uiJobLocked {
                suspendSectionInfoInfoManager.deleteSubsection(sectionSkeleton.uuid)
                showToast(
                    StringGetter(
                        R.string.section_info_layer_options_delete_subsection_success,
                        sectionSkeleton.title
                    )
                )
            }

        }
    }

    override fun onTestClick(test: TestSkeleton) {
        uiJobLocked {
            val tasks = TestsTasksManager[test.uuid].wait().get()
            showLayer(TestInfoLayer.newInstance(context, test, tasks))
        }
    }

    override fun onTestMenuClick(test: TestSkeleton) {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.section_info_layer_test_options_title, test.title))
            closeItem(StringGetter(R.string.section_info_layer_test_options_rename)) {
                onRenameTestClick(test)
            }
            closeItem(StringGetter(R.string.dialog_delete)) {
                onDeleteTestClick(test)
            }
            closeItem(StringGetter(R.string.section_info_layer_test_options_edit_pass_score_percentage)) {
                onEditTestPassScorePercentageClick(test)
            }
            closeItem(StringGetter(R.string.section_info_layer_test_options_edit_time_limit)) {
                onEditTestTimeLimitClick(test)
            }
        }
    }

    private fun onEditTestPassScorePercentageClick(testSkeleton: TestSkeleton) {
        AppActivityConnector.showPlusMinusDialog(
            title = StringGetter(R.string.section_info_layer_test_options_edit_pass_score_percentage_dialog_title),
            text = StringGetter(R.string.section_info_layer_test_options_edit_pass_score_percentage_dialog_text),
            initialValue = (testSkeleton.passScorePercentage * 100).toInt(),
            availableValueRange = 1..99,
            valueToStringConverter = { TestSkeleton.passScorePercentageToUiString(it.toFloat() / 100f) },
            columns = listOf(
                PlusMinusColumnInfo(
                    title = "10%".toGetter(),
                    actionPlus = { it + 10 },
                    actionMinus = { it - 10 }
                ),
                PlusMinusColumnInfo(
                    title = "1%".toGetter(),
                    actionPlus = { it + 1 },
                    actionMinus = { it - 1 }
                )
            ),
            confirmButtonText = StringGetter(R.string.dialog_save),
            onConfirm = { onNewTestPassScorePercentageEntered(testSkeleton, it / 100f); true }
        )
    }

    private fun onNewTestPassScorePercentageEntered(
        test: TestSkeleton,
        passScorePercentage: Float
    ) {
        uiJobLocked {
            suspendSectionInfoInfoManager.updateTestPassScorePercentage(test.uuid, passScorePercentage)
            shortToast(
                StringGetter(
                    R.string.section_info_layer_test_options_edit_pass_score_percentage_success,
                    test.title
                )
            )
        }
    }

    private fun onEditTestTimeLimitClick(testSkeleton: TestSkeleton) {
        AppActivityConnector.showPlusMinusDialog<TimeValue>(
            title = StringGetter(R.string.section_info_layer_test_options_edit_time_limit_dialog_title),
            text = StringGetter(R.string.section_info_layer_test_options_edit_time_limit_dialog_text),
            initialValue = TimeValue(testSkeleton.timeLimit),
            availableValueRange = Validators.TEST_MIN_TIME_LIMIT..Validators.TEST_MAX_TIME_LIMIT,
            valueToStringConverter = { TestSkeleton.timeLimitToUiString(it.milliseconds) },
            columns = listOf(
                PlusMinusColumnInfo(
                    title = StringGetter(R.string.plus_minus_column_title_day),
                    actionPlus = { it + TimeValue.DAY },
                    actionMinus = { it - TimeValue.DAY }
                ),
                PlusMinusColumnInfo(
                    title = StringGetter(R.string.plus_minus_column_title_hour),
                    actionPlus = { it + TimeValue.HOUR },
                    actionMinus = { it - TimeValue.HOUR }
                ),
                PlusMinusColumnInfo(
                    title = StringGetter(R.string.plus_minus_column_title_minute),
                    actionPlus = { it + TimeValue.MINUTE },
                    actionMinus = { it - TimeValue.MINUTE }
                )
            ),
            confirmButtonText = StringGetter(R.string.dialog_save),
            onConfirm = { onNewTestTimeLimitEntered(testSkeleton, it); true }
        )
    }

    private fun onNewTestTimeLimitEntered(
        test: TestSkeleton,
        timeLimit: TimeValue
    ) {
        uiJobLocked {
            suspendSectionInfoInfoManager.updateTestTimeLimit(test.uuid, timeLimit)
            shortToast(StringGetter(R.string.section_info_layer_test_options_edit_time_limit_success, test.title))
        }
    }

    private fun onRenameTestClick(testSkeleton: TestSkeleton) {
        AppActivityConnector.showInputDialog(
            title = StringGetter(R.string.section_info_layer_rename_test_dialog_title),
            text = StringGetter(R.string.section_info_layer_rename_test_dialog_text),
            confirmButtonText = StringGetter(R.string.section_info_layer_rename_test_dialog_button),
            inputInitialText = testSkeleton.title.toGetter(),
            inputInfo = SimpleInputViewInfo(
                maxLength = Validators.MAX_SECTION_TITLE_LENGTH
            ),
            onConfirm = { onTestNewTitleEntered(testSkeleton, it) }
        )
    }

    private fun onTestNewTitleEntered(
        testSkeleton: TestSkeleton,
        newTitle: String
    ): Boolean {
        tryOrHandleError {
            Validators.validateSectionTitleOrThrow(newTitle)
        } ?: return false

        uiJobLocked {
            suspendSectionInfoInfoManager.renameTest(testSkeleton.uuid, newTitle = newTitle)
            shortToast(StringGetter(R.string.section_info_layer_rename_test_success, newTitle))
        }
        return true
    }

    private fun onDeleteTestClick(testSkeleton: TestSkeleton) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.section_info_layer_option_delete_test_confirm_dialog_title),
            text = StringGetter(
                R.string.section_info_layer_option_delete_test_confirm_dialog_text,
                testSkeleton.title
            ),
            confirmText = StringGetter(R.string.dialog_delete)
        ) {
            uiJobLocked {
                suspendSectionInfoInfoManager.deleteTest(testSkeleton.uuid)
                showToast(
                    StringGetter(
                        R.string.section_info_layer_options_delete_test_success,
                        testSkeleton.title
                    )
                )
            }
        }
    }

}