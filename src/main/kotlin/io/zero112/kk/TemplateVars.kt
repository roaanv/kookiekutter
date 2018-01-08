package io.zero112.kk

class TemplateVars : LinkedHashMap<String, Any?>() {

    override fun get(key: String): Any? {
        if (key !in super.keys) {
            print("Enter value for $key: ")
            val input = readLine()

            super.put(key, input)
        }

        return super.get(key)
    }
}