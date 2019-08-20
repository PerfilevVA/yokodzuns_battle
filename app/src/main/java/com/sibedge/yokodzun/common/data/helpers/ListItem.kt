package com.sibedge.yokodzun.common.data.helpers


interface ListItem<I, K: Comparable<K>> {

    val id: I

    val sortKey: K

}