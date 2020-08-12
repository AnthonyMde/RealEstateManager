package com.amamode.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.amamode.realestatemanager.data.EstateEntity
import com.amamode.realestatemanager.data.EstateRoomDatabase
import kotlinx.coroutines.runBlocking

private const val AUTHORITY = "com.realestate.estateprovider"
private const val TABLE_NAME = "EstateEntity"

class EstateContentProvider : ContentProvider() {
    companion object {
        val URI_ITEM: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val ctx = context ?: throw IllegalArgumentException("Failed to query row for uri $uri")
        val estateId = ContentUris.parseId(uri)
        val cursor: Cursor =
            EstateRoomDatabase.getDatabase(ctx).estateDao().getEstateWithCursor(estateId)
        cursor.setNotificationUri(ctx.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val ctx = context ?: throw IllegalArgumentException("Failed to insert row into $uri")
        values ?: throw IllegalArgumentException("Failed to insert row into $uri")
        var contentUri: Uri? = null
        runBlocking {
            val id = EstateRoomDatabase.getDatabase(ctx).estateDao()
                .insert(EstateEntity.estateFromContentValue(values))
            if (id != 0L) {
                ctx.contentResolver.notifyChange(uri, null);
                contentUri = ContentUris.withAppendedId(uri, id);
            }
        }
        return contentUri
    }

    /**
     * Count is the number of line affected during the operation
     * */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val ctx = context ?: throw IllegalArgumentException("Failed to update row into $uri")
        values ?: throw IllegalArgumentException("Failed to update row into $uri")
        var count = -1
        runBlocking {
            count = EstateRoomDatabase.getDatabase(ctx).estateDao()
                .update(EstateEntity.estateFromContentValue(values))
            ctx.contentResolver.notifyChange(uri, null);
        }
        return count;
    }

    /**
     * Count is the number of line erased during the operation
     * */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val ctx = context ?: throw IllegalArgumentException("Failed to delete row into $uri")
        var count: Int = -1
        runBlocking {
            count = EstateRoomDatabase.getDatabase(ctx).estateDao()
                .deleteEstate(ContentUris.parseId(uri))
            ctx.contentResolver.notifyChange(uri, null)
        }
        return count
    }

    override fun getType(uri: Uri): String? = "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
}
