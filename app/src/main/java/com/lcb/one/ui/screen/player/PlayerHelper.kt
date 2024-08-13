package com.lcb.one.ui.screen.player

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.lcb.one.prefs.UserPrefs
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.util.android.LLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

object PlayerHelper {
    private const val TAG = "PlayerHelper"
    private const val EXT_MUSIC = "ext_music"

    private const val MINI_DURATION = 30 * 1000

    private val audioContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    suspend fun findMusics(context: Context = MyApp.get()): List<Music> =
        withContext(Dispatchers.IO) {
            val audios = mutableListOf<Music>()
            val proj = arrayOf(
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.ALBUM,
            )
            context.contentResolver.query(audioContentUri, proj, null, null, null)?.use { cursor ->
                cursor.moveToFirst()
                do {
                    val id = cursor.getInt(0)
                    val uri = ContentUris.withAppendedId(audioContentUri, id.toLong())

                    val title = cursor.getStringOrNull(1) ?: ""
                    val artist = cursor.getStringOrNull(2) ?: ""
                    val duration = cursor.getLongOrNull(3) ?: 0
                    val album = cursor.getStringOrNull(4) ?: ""

                    if (duration > MINI_DURATION) {
                        audios.add(Music(uri, title, artist, duration, album))
                    }
                } while (cursor.moveToNext())
            }

            return@withContext audios
        }

    fun Iterable<Music>.toMediaItem() = map { music ->
        MediaItem.Builder().apply {
            setUri(music.uri)
            setMediaId(music.uri.toString())

            val metadata = MediaMetadata.Builder()
                .setExtras(bundleOf(EXT_MUSIC to music))
                .build()
            setMediaMetadata(metadata)
        }.build()
    }

    fun formatDuration(duration: Long): String {
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    fun getLastPlayMusicUri(): String {
        val uri = UserPrefs.getBlocking(UserPrefs.Key.lastMusic, "")
        LLogger.debug(TAG) { "getLastPlayMusic: uri = $uri" }
        return uri
    }

    fun MediaItem.getExtraMusic(): Music? {
        val extras = mediaMetadata.extras ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extras.getParcelable(EXT_MUSIC, Music::class.java)
        } else {
            extras.getParcelable(EXT_MUSIC)
        }
    }
}