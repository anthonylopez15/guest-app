package com.example.convidados.repository

import android.content.ContentValues
import android.content.Context
import com.example.convidados.constants.DataBaseConstants
import com.example.convidados.model.GuestModel

class GuestRepository private constructor(context: Context) {

    private val guestDataBase = GuestDataBase(context)

    //  Singleton
    companion object {
        private lateinit var repository: GuestRepository

        fun getInstance(context: Context): GuestRepository {
            if (!::repository.isInitialized) {
                repository = GuestRepository(context)
            }
            return repository
        }
    }

    fun insert(guest: GuestModel): Boolean {
        return try {
            val db = guestDataBase.writableDatabase

            val values = ContentValues()
            val presence = if (guest.presence) 1 else 0
            values.put(DataBaseConstants.GUEST.COLUMNS.NAME, guest.name)
            values.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, presence)

            db.insert(DataBaseConstants.GUEST.TABLE_NAME, null, values)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun update(guest: GuestModel): Boolean {
        return try {
            val db = guestDataBase.writableDatabase
            val presence = if (guest.presence) 1 else 0

            val values = ContentValues()
            values.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, presence)
            values.put(DataBaseConstants.GUEST.COLUMNS.NAME, guest.name)

            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(guest.id.toString())

            db.update(DataBaseConstants.GUEST.TABLE_NAME, values, selection, args)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun delete(id: Int): Boolean {
        return try {
            val db = guestDataBase.writableDatabase

            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            db.delete(DataBaseConstants.GUEST.TABLE_NAME, selection, args)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAll(): List<GuestModel> {

        val list = mutableListOf<GuestModel>()

        try {
            val db = guestDataBase.readableDatabase

            val columnId = DataBaseConstants.GUEST.COLUMNS.ID
            val columnName = DataBaseConstants.GUEST.COLUMNS.NAME
            val columnPresence = DataBaseConstants.GUEST.COLUMNS.PRESENCE

            val projection = arrayOf(columnId, columnName, columnPresence)

            val cursor = db.query(
                DataBaseConstants.GUEST.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(columnId))
                    val name = cursor.getString(cursor.getColumnIndex(columnName))
                    val presence = cursor.getInt(cursor.getColumnIndex(columnPresence))
                    val guest = GuestModel(id, name, presence == 1)
                    list.add(guest)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return list
        }
        return list
    }

    fun getPresent(): List<GuestModel> {

        val list = mutableListOf<GuestModel>()

        try {
            val db = guestDataBase.readableDatabase

            val columnId = DataBaseConstants.GUEST.COLUMNS.ID
            val columnName = DataBaseConstants.GUEST.COLUMNS.NAME
            val columnPresence = DataBaseConstants.GUEST.COLUMNS.PRESENCE

            val cursor =
                db.rawQuery("SELECT id, name, presence FROM Guest WHERE presence = 1", null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(columnId))
                    val name = cursor.getString(cursor.getColumnIndex(columnName))
                    val presence = cursor.getInt(cursor.getColumnIndex(columnPresence))
                    val guest = GuestModel(id, name, presence == 1)
                    list.add(guest)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return list
        }
        return list
    }

    fun getAbsent(): List<GuestModel> {

        val list = mutableListOf<GuestModel>()

        try {
            val db = guestDataBase.readableDatabase

            val columnId = DataBaseConstants.GUEST.COLUMNS.ID
            val columnName = DataBaseConstants.GUEST.COLUMNS.NAME
            val columnPresence = DataBaseConstants.GUEST.COLUMNS.PRESENCE

            val projection = arrayOf(columnId, columnName, columnPresence)

            val selection = "presence = ?"
            val args = arrayOf("0")

            val cursor = db.query(
                DataBaseConstants.GUEST.TABLE_NAME,
                projection,
                selection,
                args,
                null,
                null,
                null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(columnId))
                    val name = cursor.getString(cursor.getColumnIndex(columnName))
                    val presence = cursor.getInt(cursor.getColumnIndex(columnPresence))
                    val guest = GuestModel(id, name, presence == 1)
                    list.add(guest)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return list
        }
        return list
    }


    fun getById(id: Int): GuestModel? {

        var guest: GuestModel? = null

        try {
            val db = guestDataBase.readableDatabase

            val columnId = DataBaseConstants.GUEST.COLUMNS.ID
            val columnName = DataBaseConstants.GUEST.COLUMNS.NAME
            val columnPresence = DataBaseConstants.GUEST.COLUMNS.PRESENCE

            val projection = arrayOf(columnId, columnName, columnPresence)

            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = db.query(
                DataBaseConstants.GUEST.TABLE_NAME,
                projection,
                selection,
                args,
                null,
                null,
                null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(columnName))
                    val presence = cursor.getInt(cursor.getColumnIndex(columnPresence))
                    guest = GuestModel(id, name, presence == 1)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return guest
        }
        return guest
    }
}
