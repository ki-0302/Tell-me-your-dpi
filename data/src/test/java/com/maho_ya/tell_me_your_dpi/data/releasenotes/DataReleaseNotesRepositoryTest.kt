package com.maho_ya.tell_me_your_dpi.data.releasenotes

import com.maho_ya.tell_me_your_dpi.result.data
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class DataReleaseNotesRepositoryTest {

    private fun createMockDataSource(date: String) =
        object : ReleaseNotesDataSource by mock(DataReleaseNotesDataSource::class.java) {
            override suspend fun getReleaseNotesResults(): com.maho_ya.tell_me_your_dpi.model.ReleaseNotesResults =
                com.maho_ya.tell_me_your_dpi.model.ReleaseNotesResults(
                    listOf(
                        com.maho_ya.tell_me_your_dpi.model.ReleaseNote(
                            appVersion = "",
                            date = date,
                            description = ""
                        )
                    )
                )
        }

    @Test
    fun formatDate_correctDate_returnDate() = runTest {
        DataReleaseNotesRepository(
            createMockDataSource("2020/05/26 09:36")
        ).getReleaseNotes().drop(1)
            .collect {
                assertThat(it.data?.releaseNotes?.get(0)?.date, `is`(""))
            }
    }

    @Test
    fun formatDate_notDateFormat_returnBlank() = runTest {
        DataReleaseNotesRepository(
            createMockDataSource("2020-05-26 09:36:00.000Z")
        ).getReleaseNotes().drop(1)
            .collect {
                assertThat(it.data?.releaseNotes?.get(0)?.date, `is`(""))
            }

        DataReleaseNotesRepository(
            createMockDataSource("20200526 093600.000Z")
        ).getReleaseNotes().drop(1)
            .collect {
                assertThat(it.data?.releaseNotes?.get(0)?.date, `is`(""))
            }
    }
}
