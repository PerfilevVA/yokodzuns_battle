package com.sibedge.yokodzun.android.layers.test_task_variant_info

import ru.hnau.remote_teaching_common.data.test.TestTaskVariant


interface TestTaskVariantInfoLayerCallback {

    fun onVariantEdited(number: Int, editedVariant: TestTaskVariant)

    fun onVariantCreated(variant: TestTaskVariant)

}