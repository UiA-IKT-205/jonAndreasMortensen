package no.uia.ikt205.superpiano.data

data class Note(val value:String, val start:String, val timePressed:Double){

    override fun toString(): String {
        return "$value,$start,$timePressed"
    }
}