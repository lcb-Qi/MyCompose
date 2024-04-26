package com.lcb.one.ui.screen.about.repo.model

import com.lcb.one.BuildConfig

class AppVersion private constructor(major: Int, minor: Int, patch: Int) : Comparable<AppVersion> {
    val versionCode = versionCodeOf(major, minor, patch)
    val versionName = "$major.$minor.$patch"

    private fun versionCodeOf(major: Int, minor: Int, patch: Int): Int {
        require(major >= 0 && minor >= 0 && patch >= 0) {
            "Version components are out of range: $major.$minor.$patch"
        }
        return String.format("%02d%02d%02d", major, minor, patch).toInt()
    }

    override fun compareTo(other: AppVersion) = versionCode - other.versionCode

    override fun toString() = "($versionName, $versionCode)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherVersion = (other as? AppVersion) ?: return false
        return this.versionCode == otherVersion.versionCode
    }

    override fun hashCode() = versionCode

    companion object {
        fun valueOf(versionName: String): AppVersion {
            return versionName.split(".").let {
                if (it.size == 3) {
                    AppVersion(it[0].toInt(), it[1].toInt(), it[2].toInt())
                } else {
                    throw IllegalArgumentException("versionName must like x.x.x")
                }
            }
        }

        fun valueOf(major: Int, minor: Int, patch: Int) = AppVersion(major, minor, patch)

        val current by lazy { valueOf(BuildConfig.VERSION_NAME) }
    }
}

class UpdateInfo(
    val version: AppVersion,
    val url: String,
    val message: String,
    val filename: String
)