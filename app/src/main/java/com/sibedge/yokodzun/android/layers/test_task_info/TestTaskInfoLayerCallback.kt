package com.sibedge.yokodzun.android.layers.test_task_info

import ru.hnau.remote_teaching_common.data.test.TestTask


interface TestTaskInfoLayerCallback {

    fun onTaskEdited(number: Int, editedTask: TestTask)

    fun onTaskCreated(task: TestTask)

}