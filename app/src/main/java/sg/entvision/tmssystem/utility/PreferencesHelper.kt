package sg.entvision.tmssystem.utility

import android.content.Context
import android.content.SharedPreferences

/**
 * Represents a single [SharedPreferences] file.
 *
 * Usage:
 *
 * ```kotlin
 * class UserPreferences : PreferencesHelper() {
 *   var emailAccount by stringPref()
 *   var showSystemAppsPreference by booleanPref()
 * }
 * ```
 */
class PreferencesHelper private constructor() {


    init {
        throw UnsupportedOperationException("You cannot call constructor") as Throwable
    }

    /**
     * Set a string shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    companion object {
        private val ISNEWDATA = "ISNEWDATA"
        private val WALKTROUGH = "WALKTROUGH"
        private val PREF_FILE = "PREF"
        private val TOKEN = "TOKEN"
        private val TOKENWKF = "TOKENWKF"
        private val TOKENWKFUSER = "TOKENWKFUSER"
        private val TELEPHONER = "TELEPHONE"
        private val EMAIl = "EMAILER"
        private val APP_RUNNING = "APP_RUNNING"
        private val ISLOGIN = "ISLOGIN"
        private val KEY_FBTOKEN = "KEY_FBTOKEN"
        private val REGIS_STATUS = "REGIS_STATUS"
        private val DEFAULT_BANK = "DEFAULT_BANK"


        private val USERNAME = "USERNAME"
        private val PASSWORD = "PASSWORD"

        fun setSharedPreferenceString(context: Context, key: String, value: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(key, value)
            editor.apply()
        }

        /**
         * Set a integer shared preference
         * @param key - Key to set shared preference
         * @param value - Value for the key
         */
        fun setSharedPreferenceInt(context: Context, key: String, value: Int) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        /**
         * Set default bank
         * @param context Context
         * @param uuid String
         */
        fun setDefaultBank(context: Context, uuid: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(DEFAULT_BANK, uuid)
            editor.apply()
        }

        /**
         * get Default Bank
         */
        fun getDefaultBank(context: Context): String? {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            return settings.getString(DEFAULT_BANK, "")
        }

        fun hasBeenRegister(context: Context, status: Boolean) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putBoolean(REGIS_STATUS, status)
            editor.apply()
        }

        fun getRegisterStatus(context: Context): Boolean {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            return settings.getBoolean(REGIS_STATUS, false)
        }

        /**
         * Set a Boolean shared preference
         * @param key - Key to set shared preference
         * @param value - Value for the key
         */
        fun setSharedPreferenceBoolean(context: Context, key: String, value: Boolean) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        /**
         * Get a string shared preference
         * @param key - Key to look up in shared preferences.
         * @param defValue - Default value to be returned if shared preference isn't found.
         * @return value - String containing value of the shared preference if found.
         */
        fun getSharedPreferenceString(context: Context, key: String, defValue: String?): String? {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            return settings.getString(key, defValue)
        }

        /**
         * Get a integer shared preference
         * @param key - Key to look up in shared preferences.
         * @param defValue - Default value to be returned if shared preference isn't found.
         * @return value - String containing value of the shared preference if found.
         */
        fun getSharedPreferenceInt(context: Context, key: String, defValue: Int): Int {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            return settings.getInt(key, defValue)
        }


        fun getSharedPreferenceLong(context: Context, key: String, defValue: Long): Long {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            return settings.getLong(key, defValue)
        }


        /**
         * Get a boolean shared preference
         * @param key - Key to look up in shared preferences.
         * @param defValue - Default value to be returned if shared preference isn't found.
         * @return value - String containing value of the shared preference if found.
         */
        fun getSharedPreferenceBoolean(context: Context, key: String, defValue: Boolean): Boolean {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            return settings.getBoolean(key, defValue)
        }

        fun clearAllSharedData(context: Context) {
            context.getSharedPreferences(PREF_FILE, 0)
                .edit()
                .clear()
                .apply()
        }

        fun getToken(context: Context): String? {
            return context.getSharedPreferences(PREF_FILE, 0).getString(TOKEN, null)
        }


        fun getTokenWkf(context: Context): String? {
            return context.getSharedPreferences(PREF_FILE, 0).getString(TOKENWKF, null)
        }

        fun getTokenWkfUser(context: Context): String? {
            return context.getSharedPreferences(PREF_FILE, 0).getString(TOKENWKFUSER, null)
        }

        fun getTelephone(context: Context): String {
            return context.getSharedPreferences(PREF_FILE, 0).getString(TELEPHONER, "0")!!
        }

        fun getUsername(context: Context): String {
            return context.getSharedPreferences(PREF_FILE, 0).getString(USERNAME, "0")!!
        }

        fun getPassword(context: Context): String {
            return context.getSharedPreferences(PREF_FILE, 0).getString(PASSWORD, "0")!!
        }

        fun getTokenFB(context: Context): String {
            return context.getSharedPreferences(PREF_FILE, 0).getString(KEY_FBTOKEN, "0")!!
        }

        fun isLogin(context: Context): Boolean {
            return context.getSharedPreferences(PREF_FILE, 0).getBoolean(ISLOGIN, false)
        }

        fun hasLogin(context: Context) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putBoolean(ISLOGIN, true)
            editor.apply()
        }

        fun saveToken(context: Context, value: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(TOKEN, value)
            editor.apply()
        }


        fun saveTokenWkf(context: Context, value: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(TOKENWKF, value)
            editor.apply()
        }

        fun saveTokenWkfUser(context: Context, value: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(TOKENWKFUSER, value)
            editor.apply()
        }


        fun saveTelephone(context: Context, value: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(TELEPHONER, value)
            editor.apply()
        }

        fun saveEmail(context: Context, value: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(EMAIl, value)
            editor.apply()
        }

        fun saveUsername(context: Context, value: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(USERNAME, value)
            editor.apply()
        }

        fun savePassword(context: Context, value: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(PASSWORD, value)
            editor.apply()
        }

        fun saveTokenFB(context: Context, value: String) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putString(KEY_FBTOKEN, value)
            editor.apply()
        }


        fun getEmail(context: Context): String {
            return context.getSharedPreferences(PREF_FILE, 0).getString(EMAIl, "0")!!
        }


        fun getState(context: Context): Boolean {
            return context.getSharedPreferences(PREF_FILE, 0).getBoolean(APP_RUNNING, false)
        }

        fun saveState(context: Context, isRunning: Boolean) {
            val settings = context.getSharedPreferences(PREF_FILE, 0)
            val editor = settings.edit()
            editor.putBoolean(APP_RUNNING, isRunning)
            editor.apply()
        }


        /**
         * setter
         * @param context Context
         * @param value Boolean
         */
        fun isNewDataAvailable(context: Context, value: Boolean) {
            val setting = context.getSharedPreferences(PREF_FILE, 0)
            val editor = setting.edit()
            editor.putBoolean(ISNEWDATA, value)
            editor.apply()
        }

        /**
         * getter
         * @param context Context
         * @return Boolean
         */
        fun isNewDataAvailable(context: Context): Boolean {
            return context.getSharedPreferences(PREF_FILE, 0).getBoolean(ISNEWDATA, false)
        }

        fun getWalktrough(context: Context): Boolean {
            return context.getSharedPreferences(PREF_FILE, 0).getBoolean(WALKTROUGH, true)
        }

        fun setWalktrough(context: Context, show: Boolean) {
            context.getSharedPreferences(PREF_FILE, 0).edit().apply {
                putBoolean(WALKTROUGH, show)
                apply()
            }
        }


    }
}