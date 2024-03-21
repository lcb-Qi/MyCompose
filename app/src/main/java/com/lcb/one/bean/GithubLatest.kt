package com.lcb.one.bean


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubLatest(
    @Json(name = "assets")
    val assets: List<Asset> = listOf(),
    @Json(name = "assets_url")
    val assetsUrl: String = "", // https://api.github.com/repos/lcb-Qi/MyCompose/releases/147363139/assets
    @Json(name = "author")
    val author: Author = Author(),
    @Json(name = "body")
    val body: String = "", // # Salted_fish-1.3.1## 2024/03/20- first release**Full Changelog**: https://github.com/lcb-Qi/MyCompose/commits/1.3.1
    @Json(name = "created_at")
    val createdAt: String = "", // 2024-03-20T03:35:30Z
    @Json(name = "draft")
    val draft: Boolean = false, // false
    @Json(name = "html_url")
    val htmlUrl: String = "", // https://github.com/lcb-Qi/MyCompose/releases/tag/1.3.1
    @Json(name = "id")
    val id: Int = 0, // 147363139
    @Json(name = "name")
    val name: String = "", // 1.3.1
    @Json(name = "node_id")
    val nodeId: String = "", // RE_kwDOLUh6Vs4IyJVD
    @Json(name = "prerelease")
    val prerelease: Boolean = false, // false
    @Json(name = "published_at")
    val publishedAt: String = "", // 2024-03-20T06:22:06Z
    @Json(name = "tag_name")
    val tagName: String = "", // 1.3.1
    @Json(name = "tarball_url")
    val tarballUrl: String = "", // https://api.github.com/repos/lcb-Qi/MyCompose/tarball/1.3.1
    @Json(name = "target_commitish")
    val targetCommitish: String = "", // master
    @Json(name = "upload_url")
    val uploadUrl: String = "", // https://uploads.github.com/repos/lcb-Qi/MyCompose/releases/147363139/assets{?name,label}
    @Json(name = "url")
    val url: String = "", // https://api.github.com/repos/lcb-Qi/MyCompose/releases/147363139
    @Json(name = "zipball_url")
    val zipballUrl: String = "" // https://api.github.com/repos/lcb-Qi/MyCompose/zipball/1.3.1
) {
    @JsonClass(generateAdapter = true)
    data class Asset(
        @Json(name = "browser_download_url")
        val browserDownloadUrl: String = "", // https://github.com/lcb-Qi/MyCompose/releases/download/1.3.1/Salted_fish-1.3.1-release.apk
        @Json(name = "content_type")
        val contentType: String = "", // application/vnd.android.package-archive
        @Json(name = "created_at")
        val createdAt: String = "", // 2024-03-20T06:18:27Z
        @Json(name = "download_count")
        val downloadCount: Int = 0, // 1
        @Json(name = "id")
        val id: Int = 0, // 157607335
        @Json(name = "label")
        val label: Any? = Any(), // null
        @Json(name = "name")
        val name: String = "", // Salted_fish-1.3.1-release.apk
        @Json(name = "node_id")
        val nodeId: String = "", // RA_kwDOLUh6Vs4JZOWn
        @Json(name = "size")
        val size: Int = 0, // 7007438
        @Json(name = "state")
        val state: String = "", // uploaded
        @Json(name = "updated_at")
        val updatedAt: String = "", // 2024-03-20T06:18:33Z
        @Json(name = "uploader")
        val uploader: Uploader = Uploader(),
        @Json(name = "url")
        val url: String = "" // https://api.github.com/repos/lcb-Qi/MyCompose/releases/assets/157607335
    ) {
        @JsonClass(generateAdapter = true)
        data class Uploader(
            @Json(name = "avatar_url")
            val avatarUrl: String = "", // https://avatars.githubusercontent.com/u/54737094?v=4
            @Json(name = "events_url")
            val eventsUrl: String = "", // https://api.github.com/users/lcb-Qi/events{/privacy}
            @Json(name = "followers_url")
            val followersUrl: String = "", // https://api.github.com/users/lcb-Qi/followers
            @Json(name = "following_url")
            val followingUrl: String = "", // https://api.github.com/users/lcb-Qi/following{/other_user}
            @Json(name = "gists_url")
            val gistsUrl: String = "", // https://api.github.com/users/lcb-Qi/gists{/gist_id}
            @Json(name = "gravatar_id")
            val gravatarId: String = "",
            @Json(name = "html_url")
            val htmlUrl: String = "", // https://github.com/lcb-Qi
            @Json(name = "id")
            val id: Int = 0, // 54737094
            @Json(name = "login")
            val login: String = "", // lcb-Qi
            @Json(name = "node_id")
            val nodeId: String = "", // MDQ6VXNlcjU0NzM3MDk0
            @Json(name = "organizations_url")
            val organizationsUrl: String = "", // https://api.github.com/users/lcb-Qi/orgs
            @Json(name = "received_events_url")
            val receivedEventsUrl: String = "", // https://api.github.com/users/lcb-Qi/received_events
            @Json(name = "repos_url")
            val reposUrl: String = "", // https://api.github.com/users/lcb-Qi/repos
            @Json(name = "site_admin")
            val siteAdmin: Boolean = false, // false
            @Json(name = "starred_url")
            val starredUrl: String = "", // https://api.github.com/users/lcb-Qi/starred{/owner}{/repo}
            @Json(name = "subscriptions_url")
            val subscriptionsUrl: String = "", // https://api.github.com/users/lcb-Qi/subscriptions
            @Json(name = "type")
            val type: String = "", // User
            @Json(name = "url")
            val url: String = "" // https://api.github.com/users/lcb-Qi
        )
    }

    @JsonClass(generateAdapter = true)
    data class Author(
        @Json(name = "avatar_url")
        val avatarUrl: String = "", // https://avatars.githubusercontent.com/u/54737094?v=4
        @Json(name = "events_url")
        val eventsUrl: String = "", // https://api.github.com/users/lcb-Qi/events{/privacy}
        @Json(name = "followers_url")
        val followersUrl: String = "", // https://api.github.com/users/lcb-Qi/followers
        @Json(name = "following_url")
        val followingUrl: String = "", // https://api.github.com/users/lcb-Qi/following{/other_user}
        @Json(name = "gists_url")
        val gistsUrl: String = "", // https://api.github.com/users/lcb-Qi/gists{/gist_id}
        @Json(name = "gravatar_id")
        val gravatarId: String = "",
        @Json(name = "html_url")
        val htmlUrl: String = "", // https://github.com/lcb-Qi
        @Json(name = "id")
        val id: Int = 0, // 54737094
        @Json(name = "login")
        val login: String = "", // lcb-Qi
        @Json(name = "node_id")
        val nodeId: String = "", // MDQ6VXNlcjU0NzM3MDk0
        @Json(name = "organizations_url")
        val organizationsUrl: String = "", // https://api.github.com/users/lcb-Qi/orgs
        @Json(name = "received_events_url")
        val receivedEventsUrl: String = "", // https://api.github.com/users/lcb-Qi/received_events
        @Json(name = "repos_url")
        val reposUrl: String = "", // https://api.github.com/users/lcb-Qi/repos
        @Json(name = "site_admin")
        val siteAdmin: Boolean = false, // false
        @Json(name = "starred_url")
        val starredUrl: String = "", // https://api.github.com/users/lcb-Qi/starred{/owner}{/repo}
        @Json(name = "subscriptions_url")
        val subscriptionsUrl: String = "", // https://api.github.com/users/lcb-Qi/subscriptions
        @Json(name = "type")
        val type: String = "", // User
        @Json(name = "url")
        val url: String = "" // https://api.github.com/users/lcb-Qi
    )
}