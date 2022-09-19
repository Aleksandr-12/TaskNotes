package com.notepad.Notepad.Prefs

import android.content.Context
import android.content.SharedPreferences

class Prefs  (context: Context)
{
    var var_prefs = "user_id"

    private val preferences: SharedPreferences = context.getSharedPreferences(var_prefs,Context.MODE_PRIVATE)

    var User_id: Int
        get() = preferences.getInt(var_prefs, -1)
        set(value) = preferences.edit().putInt(var_prefs, value).apply()
}