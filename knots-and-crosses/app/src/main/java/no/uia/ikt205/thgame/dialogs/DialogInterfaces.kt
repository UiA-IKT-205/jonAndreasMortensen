package no.uia.ikt205.thgame.dialogs

interface GameDialogListener {
    fun onDialogCreateGame(player:String)
    fun onDialogJoinGame(player: String, gameId:String)
}
