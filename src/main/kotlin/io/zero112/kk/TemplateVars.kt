package io.zero112.kk

class Prompt (val default: Any? = null, val promptString: String = "") {
    fun getValue(varName: String): String {
        val promptVarName = if (promptString.isEmpty()) varName else promptString
        var prompt = "Enter value for $promptVarName"
        if (default != null) {
            if (default is String) {
                if (!default.isNullOrEmpty()) {
                    prompt += " ($default)"
                }
            } else {
                prompt += " ($default)"
            }
        }
        prompt += ": "

        print(prompt)
        val input = readLine()

        return if (input.isNullOrEmpty()) default?.toString() ?: "" else input!!
    }
}

class TemplateVars (val promptForMissingVars:Boolean = false): LinkedHashMap<String, Any?>() {
    override fun get(key: String): Any? {
        var theValue = super.get(key)

        if (theValue is Prompt) {
            theValue = theValue.getValue(key)
            super.put(key, theValue)
        }

        return theValue
    }
}