package com.kssidll.zapierdalo.helper

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * Checks whether [permission] is granted
 * @param context application context
 * @param permission permission to check
 * @return whether permission is granted
 */
fun checkPermission(
    context: Context,
    permission: String
): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Checks whether all [permissions] are granted
 * @param context application context
 * @param permissions permissions to check
 * @return whether all permissions are granted
 */
fun checkPermission(
    context: Context,
    permissions: List<String>
): Boolean {
    if (permissions.isEmpty()) return true

    return permissions.all { permission ->
        checkPermission(context, permission)
    }
}
