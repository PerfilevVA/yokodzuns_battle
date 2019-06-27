package com.sibedge.yokodzun.android.layers.edit_content_md

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.MATCH_PARENT
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.setLinearParams
import ru.hnau.androidutils.utils.shortToast
import ru.hnau.jutils.getter.MutableGetter
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.ifTrue
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.addSuspendPresenter
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.input.multiline.MultilineInputView
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import ru.hnau.remote_teaching_common.data.section.SectionInfo
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton


class EditContentMDLayer(
    context: Context
) : AppLayer(
    context = context
) {
    companion object {

        fun newInstance(
            context: Context,
            section: SectionSkeleton
        ) = EditContentMDLayer(context).apply {
            sectionSkeleton = section
        }

    }

    @LayerState
    private lateinit var sectionSkeleton: SectionSkeleton

    override val title: StringGetter by lazy {
        StringGetter(R.string.edit_section_content_md_layer_title, sectionSkeleton.title)
    }

    private val sectionInfoInfoManager: SectionsInfoManager by lazy {
        SectionsInfoManager[sectionSkeleton.uuid]
    }

    private var oldContentMD: String? = null

    private val contentMDInputView =
        MutableGetter { sectionInfo: SectionInfo ->
            val oldContentMD = sectionInfo.contentMD
            this.oldContentMD = oldContentMD
            MultilineInputView(
                context = context,
                text = oldContentMD.toGetter(),
                hint = title
            ).apply {
                setLinearParams(MATCH_PARENT, 0, 1f)
            }
        }

    override fun afterCreate() {
        super.afterCreate()
        content {
            addSuspendPresenter(
                producer = sectionInfoInfoManager as Producer<GetterAsync<Unit, SectionInfo>>,
                invalidator = sectionInfoInfoManager::invalidate,
                contentViewGenerator = this@EditContentMDLayer::createContentView
            ) {
                applyLinearParams {
                    setMatchParentWidth()
                    setStretchedHeight()
                }
            }
        }
    }

    private fun createContentView(sectionInfo: SectionInfo) =
        LinearLayout(context).apply {
            orientation = VERTICAL
            contentMDInputView.clear()
            val contentMDInputView = contentMDInputView.get(sectionInfo)
            addChild(contentMDInputView)
            addBottomButtonView(
                text = StringGetter(R.string.dialog_save),
                onClick = { updateContent(contentMDInputView.text.toString()) }
            )
        }


    private fun updateContent(
        contentMD: String
    ) {
        if (oldContentMD == contentMD) {
            managerConnector.goBack()
            return
        }
        uiJobLocked {
            sectionInfoInfoManager.updateContentMD(
                contentMD = contentMD
            )
            shortToast(StringGetter(R.string.edit_section_content_md_layer_save_success))
            managerConnector.goBack()
        }
    }

    override fun handleGoBack(): Boolean {
        val oldContentMD = oldContentMD ?: return false
        val newContentMD = contentMDInputView.existence?.text?.toString() ?: return false
        (oldContentMD == newContentMD).ifTrue { return false }
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.edit_section_content_md_layer_edited_but_not_saved_dialog_title),
            text = StringGetter(R.string.edit_section_content_md_layer_edited_but_not_saved_dialog_text),
            confirmText = StringGetter(R.string.dialog_save)
        ) {
            updateContent(newContentMD)
        }
        return true
    }

}