package com.sih.project.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.DisplayMetrics
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.AspectRatio
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Math.*


fun SharedPreferences.save(key: String, value: Any) {
    val edit = this.edit()
    with(edit) {
        when (value) {
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            else -> putString(key, value.toString())
        }
    }
    edit.apply()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show()
}

fun LatLng.openInMaps(context: Context) {
    val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=$latitude,$longitude")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    context.startActivity(mapIntent)
}

suspend fun DatabaseReference.valueEventFlow(): Flow<EventResponse> = callbackFlow {
    val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            trySendBlocking(EventResponse.Changed(snapshot))
        }

        override fun onCancelled(error: DatabaseError) {
            trySendBlocking(EventResponse.Cancelled(error))
        }
    }
    addValueEventListener(valueEventListener)
    awaitClose {
        removeEventListener(valueEventListener)
    }
}

fun ImageView.loadImage(imageUrl: String?) {
    Glide.with(context).load(imageUrl).into(this)
}

fun DisplayMetrics.getAspectRatio(): Int {
    val ratio43Value = 4.0 / 3.0
    val ratio169Value = 16.0 / 9.0

    val previewRatio = max(this.widthPixels, this.heightPixels).toDouble() / min(
        this.widthPixels,
        this.heightPixels
    )
    if (kotlin.math.abs(previewRatio - ratio43Value) <= kotlin.math.abs(previewRatio - ratio169Value)) {
        return AspectRatio.RATIO_4_3
    }
    return AspectRatio.RATIO_16_9
}
