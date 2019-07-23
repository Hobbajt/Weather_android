package com.hobbajt.repository.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

@RunWith(MockitoJUnitRunner::class)
class ZonedDateTimeConverterTest {

    @Test
    fun timestampToDate() {
        val convertedDate = ZonedDateTimeConverter.timestampToDate(1563833783)
        val expectedDate = ZonedDateTime.of(2019, 7, 23, 0, 16, 23, 0, ZoneId.systemDefault())
        assertEquals(expectedDate, convertedDate)
    }

    @Test
    fun dateToTimestamp() {
        val date = ZonedDateTime.of(2019, 7, 23, 0, 16, 23, 0, ZoneId.systemDefault())
        assertEquals(1563833783L, ZonedDateTimeConverter.dateToTimestamp(date))
    }

}