package no.uia.ikt205.todolist.todos.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tasks(val task:String, var isChecked:Boolean = false):Parcelable