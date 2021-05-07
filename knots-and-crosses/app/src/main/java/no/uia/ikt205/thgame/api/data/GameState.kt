package no.uia.ikt205.knotsandcrosses.api.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

typealias GameState = Array<CharArray>

@Parcelize
data class Game(val players:MutableList<String>, val gameId:String, val state:GameState ):Parcelable


