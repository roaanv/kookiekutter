package io.zero112.kk

fun prompt (varName: String, default: Any): Any {
    print("Enter value for $varName or accept default ($default): ")
    val input = readLine()

    return if (input.isNullOrEmpty()) default else input!!
}

class Default(val default: Any)

class TV : LinkedHashMap<String, Any?>(){
    override fun get(key: String): Any? {
        var theValue = super.get(key)

        if (theValue is Prompt) {
            theValue = theValue.getValue(key)
            super.put(key, theValue)
        }

        return theValue
    }
}

