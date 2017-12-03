package io.zero112.kk.util

fun <T:Comparable<T>>stackOf (vararg topToBottom: T): Stack<T> {
    return Stack(topToBottom.toMutableList())
}

class Stack<T:Comparable<T>>(list:MutableList<T>) {

    var items: MutableList<T> = list


    fun isEmpty():Boolean = this.items.isEmpty()

    fun count():Int = this.items.count()

    fun push(element:T) {
        val position = this.count()
        this.items.add(position, element)
    }

    override  fun toString() = this.items.toString()

    fun pop():T? {
        return if (this.isEmpty()) {
            null
        } else {
            val item =  this.items.count() - 1
            this.items.removeAt(item)
        }
    }

    fun peek():T? {
        if (isEmpty()) {
            return null
        } else {
            return this.items[this.items.count() - 1]
        }
    }
}