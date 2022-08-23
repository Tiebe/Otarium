package nl.tiebe.openbaarlyceumzeist

import com.russhwolf.settings.Settings
import nl.tiebe.magisterapi.api.Account
import nl.tiebe.openbaarlyceumzeist.database.Database

val account = Account()
lateinit var database: Database
val settings: Settings = Settings()

fun isDatabaseInitialized() = ::database.isInitialized

class Main {

    fun setup() {
        if (!isDatabaseInitialized()) {
            database = Database().setupDatabase()
        }

        val version = settings.getInt("version", 0)

        if (version == 0 || version < BuildKonfig.versionCode) {
            settings.putInt("version", BuildKonfig.versionCode)
        }
    }

    fun start() {

    }

}

