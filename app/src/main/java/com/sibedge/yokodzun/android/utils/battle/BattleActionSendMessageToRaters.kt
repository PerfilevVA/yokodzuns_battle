package com.sibedge.yokodzun.android.utils.battle

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.data.AdminBattlesDataManager
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.utils.shortToast


object BattleActionSendMessageToRaters : BattleAction(
    title = StringGetter(R.string.battle_action_send_message_to_raters)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showInputDialog(
        title = StringGetter(R.string.battle_action_send_message_to_raters_title),
        text = StringGetter(R.string.battle_action_send_message_to_raters_text),
        inputHint = StringGetter(R.string.battle_action_send_message_to_raters_hint),
        confirmButtonText = StringGetter(R.string.battle_action_send_message_to_raters_send)
    ) { message ->
        if (message.isEmpty()) {
            shortToast(StringGetter(R.string.battle_action_send_message_to_raters_empty_error))
            return@showInputDialog false
        }
        coroutinesExecutor {
            API.sendMessageToRaters(
                battleId = battle.id,
                message = message
            ).await()
        }
        return@showInputDialog true
    }

}