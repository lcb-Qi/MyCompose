package com.lcb.one.ui.screen.about.repo

import com.lcb.one.BuildConfig
import com.lcb.one.bean.GithubLatest
import com.lcb.one.network.CommonApiService
import com.lcb.one.ui.screen.about.repo.model.AppVersion
import com.lcb.one.ui.screen.about.repo.model.UpdateInfo
import com.lcb.one.util.common.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UpdateAccessor {
    suspend fun getLastRelease(): UpdateInfo? = runCatching {
        withContext(Dispatchers.IO) {
            var updateInfo: UpdateInfo? = null
            val response =
                CommonApiService.instance.get("https://api.github.com/repos/lcb-Qi/MyCompose/releases/latest")
            JsonUtils.fromJson<GithubLatest>(response).let {
                updateInfo = UpdateInfo(
                    AppVersion.valueOf(it.tagName),
                    it.assets[0].browserDownloadUrl,
                    it.body,
                    it.assets[0].name
                )
            }

            return@withContext updateInfo
        }
    }.getOrNull()

    /**
     * Compare version
     *
     * @return 1: version1 > version12; -1: version1 < version2; 0: version1 == version2
     */
    fun compareVersion(version1: String, version2: String): Int {
        if (BuildConfig.DEBUG) return 1

        val v1 = version1.split(".").map { it.toInt() }
        val v2 = version2.split(".").map { it.toInt() }

        require(v1.size == 3 && v2.size == 3) { "version must like x.x.x" }

        v1.forEachIndexed { index, i ->
            if (i > v2[index]) {
                return 1
            } else if (i < v2[index]) {
                return -1
            }
        }

        return 0
    }
}