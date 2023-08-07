package co.vgw.webapi.app

object Environment {
    val serverPort = getIntEnvValue("SERVER_PORT")
}

private fun getIntEnvValue(key: String): Int {
    val value = System.getenv(key)
    return value.toIntOrNull() ?: throw Exception("Environment variable '$key' is incorrect.")
}
