package nl.tiebe.openbaarlyceumzeist.database

import io.realm.kotlin.migration.AutomaticSchemaMigration


class DatabaseMigration : AutomaticSchemaMigration {

    override fun migrate(migrationContext: AutomaticSchemaMigration.MigrationContext) {
        val oldV = migrationContext.oldRealm.schemaVersion()
        val newV = migrationContext.newRealm.schemaVersion()

        if (newV <= oldV) {
            return
        }
        when (oldV) {
            1L -> migrate1toCurrent()
        }
    }

    fun migrate1toCurrent() {

    }
}