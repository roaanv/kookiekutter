package io.zero112.kk

class TemplateVars (val promptForMissingVars:Boolean = false): LinkedHashMap<String, Any?>() {

    val PROMPT_POS = 2
    val DEFAULT_POS = 1

    override fun get(key: String): Any? {
        if (!promptForMissingVars) {
            return super.get(key)
        }

        if (key !in super.keys) {
            val keyParts = key.split("__")

            var prompt = "Enter value for "
            prompt += if (keyParts.size >= (PROMPT_POS + 1) && !keyParts[PROMPT_POS].isEmpty()) keyParts[PROMPT_POS] else keyParts[0]
            prompt += if (keyParts.size >= DEFAULT_POS + 1) " (${keyParts[DEFAULT_POS]})" else ""
            prompt += ": "

            print(prompt)
            val input = readLine()

            if (input != null) {
                if (input.isEmpty()) {
                    if (keyParts.size >= DEFAULT_POS + 1) {
                        super.put(key, keyParts[DEFAULT_POS])
                    }
                } else {
                    super.put(key, input)
                }
            }
        }

        return super.get(key)
    }
}