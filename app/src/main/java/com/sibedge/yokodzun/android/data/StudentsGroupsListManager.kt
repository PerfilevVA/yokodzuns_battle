package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.utils.extensions.sortKey
import com.sibedge.yokodzun.android.utils.minusFirst
import ru.hnau.remote_teaching_common.data.StudentsGroup


object StudentsGroupsListManager : RTDataManager<List<StudentsGroup>>() {

    override suspend fun getValue() =
        API.getAllStudentsGroups().await().sortedBy { it.sortKey }

    suspend fun createNew(name: String) {
        API.createStudentsGroup(name).await()
        updateOrInvalidate { oldGroups ->
            (oldGroups + StudentsGroup(name, false)).sortedBy { it.sortKey }
        }
    }

    suspend fun archive(name: String) {
        API.archiveStudentsGroup(name).await()
        updateOrInvalidate { oldGroups ->
            oldGroups.map {
                if (it.name != name) it else it.copy(archived = true)
            }.sortedBy { it.sortKey }
        }
    }

    suspend fun unarchive(name: String) {
        API.unarchiveStudentsGroup(name).await()
        updateOrInvalidate { oldGroups ->
            oldGroups.map {
                if (it.name != name) it else it.copy(archived = false)
            }.sortedBy { it.sortKey }
        }
    }

    suspend fun delete(name: String) {
        API.deleteStudentsGroup(name).await()
        updateOrInvalidate { oldGroups ->
            oldGroups.minusFirst { it.name == name }
        }
    }

    suspend fun generateRegistrationCode(name: String) =
        API.generateStudentsGroupRegistrationCode(name).await()

}
