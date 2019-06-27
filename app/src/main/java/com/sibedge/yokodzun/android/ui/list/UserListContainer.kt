package com.sibedge.yokodzun.android.ui.list

import android.content.Context
import android.view.View
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.ui.list.base.ItemCellListContaner
import com.sibedge.yokodzun.android.utils.extensions.getLineCellData
import ru.hnau.remote_teaching_common.data.User


class UserListContainer(
    context: Context,
    producer: Producer<GetterAsync<Unit, List<User>>>,
    onClick: (User) -> Unit,
    onMenuClick: ((User) -> Unit)? = null,
    onEmptyListInfoViewGenerator: () -> View,
    invalidator: () -> Unit
) : ItemCellListContaner<User>(
    context = context,
    idGetter = User::login,
    producer = producer,
    onClick = onClick,
    onEmptyListInfoViewGenerator = onEmptyListInfoViewGenerator,
    cellDataGetter = { user ->
        user.getLineCellData(onMenuClick?.let { onMenuClickListener ->
            { onMenuClickListener(user) }
        })
    },
    invalidator = invalidator
)