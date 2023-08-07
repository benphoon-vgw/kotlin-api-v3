package co.vgw.webapi.app

object Environment {
    val serverPort = getIntEnvValue("SERVER_PORT")
    val dbHost = getEnvValue("DB_HOST")
    val dbPort = getIntEnvValue("DB_PORT")
    val dbName = getEnvValue("DB_NAME")
    val dbUser = getEnvValue("DB_USERNAME")
    val dbPassword = getEnvValue("DB_PASSWORD")
}

private fun getIntEnvValue(key: String): Int {
    val value = System.getenv(key)
    return value.toIntOrNull() ?: throw Exception("Environment variable '$key' is incorrect.")
}

private fun getEnvValue(key: String): String {
    val value = System.getenv(key)
    if (value.isNullOrBlank()) {
        throw Exception("Environment variable '$key' not supplied")
    }
    return value
}
