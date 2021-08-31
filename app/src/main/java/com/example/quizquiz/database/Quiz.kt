package com.example.quizquiz.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

class StringListConverter {
    @TypeConverter
    fun stringListToString(stringList: List<String>) : String {
        // ["Hello", "World"] => "Hello,World"
        return stringList.joinToString(",")
    }

    @TypeConverter
    fun stringToStringList(string: String) : List<String> {
        // "Hello,World" => ["Hello", "World"]
        return string.split(",")
    }
}

@Entity(tableName="quiz")
data class Quiz(
    var type: String?,
    var question: String?,
    var answer: String?,
    var category: String?,
    @TypeConverters(StringListConverter::class)
    var guesses: List<String>? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
) : Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(question)
        parcel.writeString(answer)
        parcel.writeString(category)
        parcel.writeStringList(guesses)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Quiz> {
        override fun createFromParcel(parcel: Parcel): Quiz {
            return Quiz(parcel)
        }

        override fun newArray(size: Int): Array<Quiz?> {
            return arrayOfNulls(size)
        }
    }
}






