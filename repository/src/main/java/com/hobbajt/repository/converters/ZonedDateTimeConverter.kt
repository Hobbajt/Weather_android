package com.hobbajt.repository.converters

import androidx.room.TypeConverter
import com.google.gson.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import java.lang.reflect.Type


object ZonedDateTimeConverter : JsonDeserializer<ZonedDateTime>, JsonSerializer<ZonedDateTime> {
    override fun deserialize(json: JsonElement, type: Type?, context: JsonDeserializationContext?): ZonedDateTime? {
        return try {
            if (json.asString.isEmpty()) {
                null
            } else {
                ZonedDateTime.parse(json.asString.replace("\"", ""))
            }
        } catch (e: Exception) {
            Timber.e(e, "Deserialize ZonedDateTime error")
            null
        }
    }

    override fun serialize(src: ZonedDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
        return src?.let {
            JsonPrimitive(it.toOffsetDateTime().toString())
        }
    }

    @TypeConverter
    fun timestampToDate(value: Long): ZonedDateTime {
        return Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault())
    }

    @TypeConverter
    fun dateToTimestamp(date: ZonedDateTime): Long? {
        return date.toInstant().epochSecond
    }
}