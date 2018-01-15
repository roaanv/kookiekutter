package io.zero112.kk

fun replace(source: Any?, what: String, whith: String): String? {
    if (source == null) {
        return null
    }

    return source.toString().replace(what, whith)
}

//class Prompt (private val default: Any? = null, private val promptString: String = "") {
//    fun getValue(varName: String): String {
//        val promptVarName = if (promptString.isEmpty()) varName else promptString
//        var prompt = "Enter value for $promptVarName"
//        if (default != null) {
//            if (default is String) {
//                if (!default.isNullOrEmpty()) {
//                    prompt += " ($default)"
//                }
//            } else {
//                prompt += " ($default)"
//            }
//        }
//        prompt += ": "
//
//        print(prompt)
//        val input = readLine()
//
//        return if (input.isNullOrEmpty()) default?.toString() ?: "" else input!!
//    }
//}

class TemplateVars (val promptForMissingVars:Boolean = false): LinkedHashMap<String, Any?>() {
    fun prompt(varName: String, default: Any? = null, promptString: String? = null): Any? {
        // Only prompt if we don't have value yet
        if (containsKey(varName)) {
            return get(varName)
        }

        val promptVarName = if (promptString.isNullOrEmpty()) varName else promptString
        var prompt = "Enter value for $promptVarName"
        if (default != null) {
            if (default is String) {
                if (!default.isEmpty()) {
                    prompt += " ($default)"
                }
            } else {
                prompt += " ($default)"
            }
        }
        prompt += ": "

        print(prompt)
        val input = readLine()

        put(varName, if (input.isNullOrEmpty()) default?.toString() ?: "" else input!!)

        return input
    }

    override fun get(key: String): Any? {
        if (key !in super.keys) {
            if (promptForMissingVars) {
                print("(force prompt) Enter value for $key: ")
                val theValue = readLine()
                super.put(key, theValue)

                return theValue
            }

            return null
        }

        var theValue = super.get(key)

//        when(theValue) {
//            is Prompt -> {
//                theValue = theValue.getValue(key)
//                super.put(key, theValue)
//            }
//        }

        return theValue
    }
}