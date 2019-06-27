package com.sibedge.yokodzun.android.data

import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.cache.AutoCache
import ru.hnau.jutils.handle
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.utils.minusFirst
import ru.hnau.remote_teaching_common.data.section.SectionContentMDUpdateParam
import ru.hnau.remote_teaching_common.data.section.SectionInfo
import ru.hnau.remote_teaching_common.data.section.SectionUtils


class SectionsInfoManager private constructor(
    private val sectionUUID: String
) : RTDataManager<SectionInfo>() {

    companion object : AutoCache<String, SectionsInfoManager>(
        getter = ::SectionsInfoManager,
        capacity = 1024
    ) {

        val COURSES = get(SectionUtils.ROOT_UUID)

    }

    override suspend fun getValue() =
        API.getSectionInfo(sectionUUID).await().let {
            it.copy(
                subsections = it.subsections.sortedBy { it.title },
                tests = it.tests.sortedBy { it.title }
            )
        }

    suspend fun addSubsection(title: String) {
        val subsSection = API.addSubsection(sectionUUID, title).await()
        updateOrInvalidate {
            it.copy(subsections = (it.subsections + subsSection).sortedBy { it.title })
        }
    }

    suspend fun deleteSubsection(subsectionUUID: String) {
        API.deleteSection(subsectionUUID).await()
        updateOrInvalidate {
            it.copy(subsections = it.subsections.minusFirst { it.uuid == subsectionUUID })
        }
    }

    suspend fun renameSubsection(
        subsectionUUID: String,
        newTitle: String
    ) {
        API.renameSection(subsectionUUID, newTitle).await()
        updateOrInvalidate {
            it.copy(subsections = it.subsections.map {
                (it.uuid == subsectionUUID).handle(
                    onTrue = { it.copy(title = newTitle) },
                    onFalse = { it }
                )
            }.sortedBy { it.title })
        }
    }

    suspend fun updateContentMD(contentMD: String) {
        API.updateSectionContent(sectionUUID, SectionContentMDUpdateParam(contentMD)).await()
        updateOrInvalidate {
            it.copy(contentMD = contentMD)
        }
    }

    suspend fun addTest(title: String) {
        val test = API.addTest(sectionUUID, title).await()
        updateOrInvalidate {
            it.copy(tests = (it.tests + test).sortedBy { it.title })
        }
    }

    suspend fun deleteTest(testUUID: String) {
        API.deleteTest(testUUID).await()
        updateOrInvalidate {
            it.copy(tests = it.tests.minusFirst { it.uuid == testUUID })
        }
    }

    suspend fun renameTest(testUUID: String, newTitle: String) {
        API.updateTestTitle(testUUID, newTitle).await()
        updateOrInvalidate {
            it.copy(tests = it.tests.map {
                (it.uuid == testUUID).handle(
                    onTrue = { it.copy(title = newTitle) },
                    onFalse = { it }
                )
            }.sortedBy { it.title })
        }
    }

    suspend fun updateTestTimeLimit(testUUID: String, timeLimit: TimeValue) {
        val timeLimitMilliseconds = timeLimit.milliseconds
        API.updateTestTimeLimit(testUUID, timeLimitMilliseconds).await()
        updateOrInvalidate {
            it.copy(tests = it.tests.map {
                (it.uuid == testUUID).handle(
                    onTrue = { it.copy(timeLimit = timeLimitMilliseconds) },
                    onFalse = { it }
                )
            }.sortedBy { it.title })
        }
    }

    suspend fun updateTestPassScorePercentage(testUUID: String, passScorePercentage: Float) {
        API.updateTestPassPercentage(testUUID, passScorePercentage).await()
        updateOrInvalidate {
            it.copy(tests = it.tests.map {
                (it.uuid == testUUID).handle(
                    onTrue = { it.copy(passScorePercentage = passScorePercentage) },
                    onFalse = { it }
                )
            }.sortedBy { it.title })
        }
    }

}