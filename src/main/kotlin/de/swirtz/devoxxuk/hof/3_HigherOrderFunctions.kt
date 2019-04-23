package de.swirtz.devoxxuk.hof

import java.io.FileInputStream
import java.util.concurrent.locks.ReentrantLock

// @formatter:off




typealias IntMapper = (Int) -> Int


fun calculate(param: Int, operation: IntMapper): Int {
    return operation(param)
}













inline fun calculateInline(param: Int, operation: IntMapper): Int {
    return operation(param)
}




fun calculateNoInline(param: Int, operation: IntMapper): Int {
    val o = operation
    return o(param)
}


// Why noinline? inline has restrictions:



// - you can inline when function type param is called or
//      passed to other !inline! function,



// - you _cannot_ inline when function is being assigned to variable e.g.
//      because: assigned function has to be part of an object
// - you should not inline long functions, think about generated byte code





/**
 * higher order function as extension function
 */
inline fun Int.calculateSelf(operation: IntMapper) = operation(this)


//____________________________________________________________________________________________________________




//like forEach in standard library
inline fun <T> Iterable<T>.each(action: (T) -> Unit): Unit {
    for (element in this) action(element)
}


fun controlFlow() {
    listOf(1, 2, 3).each {
        //bitwise AND, best way to check for even numbers ;-)
        if (it and 1 == 0) return
    }

    println("done")
}

// What will be printed by controlFlow?
//
//
// *** nothing, non-local return ***
//
// Fix with labels


fun controlFlowLocal() {
    listOf(1, 2, 3).each {
        if (it % 2 == 0) return@each //like a continue in for loop
    }



    listOf(1, 2, 3).each someLoop@{
        if (it % 2 == 0) return@someLoop
    }


    println("done")



    //alternative with anonymous function
    listOf(1, 2, 3).each(fun(x: Int) {
        if (x % 2 == 0) return
    })
    println("done")


    //  ***************************************
    //  _Rule_: return returns from the closest
    //  function declared with the fun keyword
    //  ***************************************



    calculate(5) {
        return@calculate it * 10
    }
}


fun `other standard lib examples`() {
    synchronized(ReentrantLock()) {
        println("do stuff")
        "return_value"
    }

    FileInputStream("path").use {
        println("do other stuff")
    }

    listOf(1, 2, 3, 4)
        .filter { it < 3 }
        .map { it * 2 }
        .joinToString()
}



