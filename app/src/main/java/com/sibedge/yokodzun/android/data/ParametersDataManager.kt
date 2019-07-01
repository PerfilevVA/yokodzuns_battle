package com.sibedge.parameter.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.data.YListDataManager
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.helpers.Description


object ParametersDataManager : YListDataManager<String, String, Parameter>() {

    override suspend fun getList() =
        API.getAllParameters().await()

    suspend fun createNew() {
        val parameterId = API.createNewParameter().await()
        val parameter = Parameter(id = parameterId)
        insertItem(parameter)
    }

    suspend fun remove(
        parameterId: String
    ) {
        API.removeParameter(parameterId).await()
        removeItem(parameterId)
    }

    suspend fun updateDescription(
        parameterId: String,
        description: Description
    ) {
        API.updateParameterDescription(parameterId, description).await()
        updateOrInvalidateItem(parameterId) { copy(description = description) }
    }

}