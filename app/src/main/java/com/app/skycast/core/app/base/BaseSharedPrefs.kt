package com.app.skycast.core.app.base

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Base class for simplifying interactions with [SharedPreferences].
 */
@Suppress("UnnecessaryAbstractClass")
abstract class BaseSharedPrefs(
    private val sharedPreferences: SharedPreferences,
) {
    /**
     * Gets the [Boolean] for the given [key] from [SharedPreferences], or returns `null` if that
     * key is not present.
     */
    protected fun getBoolean(
        key: String,
        defaultValue: Boolean? = null,
    ): Boolean? =
        if (sharedPreferences.contains(key.withBase())) {
            sharedPreferences.getBoolean(key.withBase(), false)
        } else {
            // Make sure we can return a null value as a default if necessary
            defaultValue
        }

    /**
     * Puts the [value] in [SharedPreferences] for the given [key] (or removes the key when the
     * value is `null`).
     */
    protected fun putBoolean(
        key: String,
        value: Boolean?,
    ): Unit =
        sharedPreferences.edit {
            if (value != null) {
                putBoolean(key.withBase(), value)
            } else {
                remove(key.withBase())
            }
        }

    /**
     * Gets the [Int] for the given [key] from [SharedPreferences], or returns `null` if that key
     * is not present.
     */
    protected fun getInt(
        key: String,
        defaultValue: Int? = null,
    ): Int? =
        if (sharedPreferences.contains(key.withBase())) {
            sharedPreferences.getInt(key.withBase(), 0)
        } else {
            // Make sure we can return a null value as a default if necessary
            defaultValue
        }

    /**
     * Puts the [value] in [SharedPreferences] for the given [key] (or removes the key when the
     * value is `null`).
     */
    protected fun putInt(
        key: String,
        value: Int?,
    ): Unit =
        sharedPreferences.edit {
            if (value != null) {
                putInt(key.withBase(), value)
            } else {
                remove(key.withBase())
            }
        }

    /**
     * Gets the [Int] for the given [key] from [SharedPreferences], or returns `null` if that key
     * is not present.
     */
    protected fun getFloat(
        key: String,
        defaultValue: Float? = null,
    ): Float? =
        if (sharedPreferences.contains(key.withBase())) {
            sharedPreferences.getFloat(key.withBase(), 0f)
        } else {
            // Make sure we can return a null value as a default if necessary
            defaultValue
        }

    /**
     * Puts the [value] in [SharedPreferences] for the given [key] (or removes the key when the
     * value is `null`).
     */
    protected fun putFloat(
        key: String,
        value: Float?,
    ): Unit =
        sharedPreferences.edit {
            if (value != null) {
                putFloat(key.withBase(), value)
            } else {
                remove(key.withBase())
            }
        }

    /**
     * Gets the [Long] for the given [key] from [SharedPreferences], or returns `null` if that key
     * is not present.
     */
    protected fun getLong(
        key: String,
        defaultValue: Long? = null,
    ): Long? =
        if (sharedPreferences.contains(key.withBase())) {
            sharedPreferences.getLong(key.withBase(), 0)
        } else {
            // Make sure we can return a null value as a default if necessary
            defaultValue
        }

    /**
     * Puts the [value] in [SharedPreferences] for the given [key] (or removes the key when the
     * value is `null`).
     */
    protected fun putLong(
        key: String,
        value: Long?,
    ): Unit =
        sharedPreferences.edit {
            if (value != null) {
                putLong(key.withBase(), value)
            } else {
                remove(key.withBase())
            }
        }

    protected fun getString(
        key: String,
        defaultValue: String? = null,
    ): String? = sharedPreferences.getString(key.withBase(), null) ?: defaultValue

    protected fun putString(
        key: String,
        value: String?,
    ): Unit = sharedPreferences.edit(commit = true) {
        putString(key.withBase(), value)
    }

    protected fun removeWithPrefix(prefix: String) {
        sharedPreferences
            .all
            .keys
            .filter { it.startsWith(prefix.withBase()) }
            .forEach { sharedPreferences.edit { remove(it) } }
    }

    protected fun String.appendIdentifier(identifier: String): String = "${this}_$identifier"
}

/**
 * Helper method for prepending the key with the appropriate base storage key.
 */
private fun String.withBase(): String = "prefStorage:$this"