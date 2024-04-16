package com.lcb.one.bean


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubLatest(
    @SerialName("assets")
    val assets: List<Asset> = listOf(),

    @SerialName("assets_url")
    val assetsUrl: String = "", // https://api.github.com/repos/lcb-Qi/MyCompose/releases/147363139/assets

    @SerialName("author")
    val author: Author = Author(),

    @SerialName("body")
    val body: String = "", // # Salted_fish-1.3.1## 2024/03/20- first release**Full Changelog**: https://github.com/lcb-Qi/MyCompose/commits/1.3.1

    @SerialName("created_at")
    val createdAt: String = "", // 2024-03-20T03:35:30Z

    @SerialName("draft")
    val draft: Boolean = false, // false

    @SerialName("html_url")
    val htmlUrl: String = "", // https://github.com/lcb-Qi/MyCompose/releases/tag/1.3.1

    @SerialName("id")
    val id: Int = 0, // 147363139

    @SerialName("name")
    val name: String = "", // 1.3.1

    @SerialName("node_id")
    val nodeId: String = "", // RE_kwDOLUh6Vs4IyJVD

    @SerialName("prerelease")
    val prerelease: Boolean = false, // false

    @SerialName("published_at")
    val publishedAt: String = "", // 2024-03-20T06:22:06Z

    @SerialName("tag_name")
    val tagName: String = "", // 1.3.1

    @SerialName("tarball_url")
    val tarballUrl: String = "", // https://api.github.com/repos/lcb-Qi/MyCompose/tarball/1.3.1

    @SerialName("target_commitish")
    val targetCommitish: String = "", // master

    @SerialName("upload_url")
    val uploadUrl: String = "", // https://uploads.github.com/repos/lcb-Qi/MyCompose/releases/147363139/assets{?name,label}

    @SerialName("url")
    val url: String = "", // https://api.github.com/repos/lcb-Qi/MyCompose/releases/147363139

    @SerialName("zipball_url")
    val zipballUrl: String = "" // https://api.github.com/repos/lcb-Qi/MyCompose/zipball/1.3.1
) {
    @Serializable
    data class Asset(
        @SerialName("browser_download_url")
        val browserDownloadUrl: String = "", // https://github.com/lcb-Qi/MyCompose/releases/download/1.3.1/Salted_fish-1.3.1-release.apk

        @SerialName("content_type")
        val contentType: String = "", // application/vnd.android.package-archive

        @SerialName("created_at")
        val createdAt: String = "", // 2024-03-20T06:18:27Z

        @SerialName("download_count")
        val downloadCount: Int = 0, // 1

        @SerialName("id")
        val id: Int = 0, // 157607335

        @SerialName("label")
        val label: String? = null, // null

        @SerialName("name")
        val name: String = "", // Salted_fish-1.3.1-release.apk

        @SerialName("node_id")
        val nodeId: String = "", // RA_kwDOLUh6Vs4JZOWn

        @SerialName("size")
        val size: Int = 0, // 7007438

        @SerialName("state")
        val state: String = "", // uploaded

        @SerialName("updated_at")
        val updatedAt: String = "", // 2024-03-20T06:18:33Z

        @SerialName("uploader")
        val uploader: Uploader = Uploader(),

        @SerialName("url")
        val url: String = "" // https://api.github.com/repos/lcb-Qi/MyCompose/releases/assets/157607335
    ) {
        @Serializable
        data class Uploader(
            @SerialName("avatar_url")
            val avatarUrl: String = "", // https://avatars.githubusercontent.com/u/54737094?v=4

            @SerialName("events_url")
            val eventsUrl: String = "", // https://api.github.com/users/lcb-Qi/events{/privacy}

            @SerialName("followers_url")
            val followersUrl: String = "", // https://api.github.com/users/lcb-Qi/followers

            @SerialName("following_url")
            val followingUrl: String = "", // https://api.github.com/users/lcb-Qi/following{/other_user}

            @SerialName("gists_url")
            val gistsUrl: String = "", // https://api.github.com/users/lcb-Qi/gists{/gist_id}

            @SerialName("gravatar_id")
            val gravatarId: String = "",

            @SerialName("html_url")
            val htmlUrl: String = "", // https://github.com/lcb-Qi

            @SerialName("id")
            val id: Int = 0, // 54737094

            @SerialName("login")
            val login: String = "", // lcb-Qi

            @SerialName("node_id")
            val nodeId: String = "", // MDQ6VXNlcjU0NzM3MDk0

            @SerialName("organizations_url")
            val organizationsUrl: String = "", // https://api.github.com/users/lcb-Qi/orgs

            @SerialName("received_events_url")
            val receivedEventsUrl: String = "", // https://api.github.com/users/lcb-Qi/received_events

            @SerialName("repos_url")
            val reposUrl: String = "", // https://api.github.com/users/lcb-Qi/repos

            @SerialName("site_admin")
            val siteAdmin: Boolean = false, // false

            @SerialName("starred_url")
            val starredUrl: String = "", // https://api.github.com/users/lcb-Qi/starred{/owner}{/repo}

            @SerialName("subscriptions_url")
            val subscriptionsUrl: String = "", // https://api.github.com/users/lcb-Qi/subscriptions

            @SerialName("type")
            val type: String = "", // User

            @SerialName("url")
            val url: String = "" // https://api.github.com/users/lcb-Qi
        )
    }

    @Serializable
    data class Author(
        @SerialName("avatar_url")
        val avatarUrl: String = "", // https://avatars.githubusercontent.com/u/54737094?v=4

        @SerialName("events_url")
        val eventsUrl: String = "", // https://api.github.com/users/lcb-Qi/events{/privacy}

        @SerialName("followers_url")
        val followersUrl: String = "", // https://api.github.com/users/lcb-Qi/followers

        @SerialName("following_url")
        val followingUrl: String = "", // https://api.github.com/users/lcb-Qi/following{/other_user}

        @SerialName("gists_url")
        val gistsUrl: String = "", // https://api.github.com/users/lcb-Qi/gists{/gist_id}

        @SerialName("gravatar_id")
        val gravatarId: String = "",

        @SerialName("html_url")
        val htmlUrl: String = "", // https://github.com/lcb-Qi

        @SerialName("id")
        val id: Int = 0, // 54737094

        @SerialName("login")
        val login: String = "", // lcb-Qi

        @SerialName("node_id")
        val nodeId: String = "", // MDQ6VXNlcjU0NzM3MDk0

        @SerialName("organizations_url")
        val organizationsUrl: String = "", // https://api.github.com/users/lcb-Qi/orgs

        @SerialName("received_events_url")
        val receivedEventsUrl: String = "", // https://api.github.com/users/lcb-Qi/received_events

        @SerialName("repos_url")
        val reposUrl: String = "", // https://api.github.com/users/lcb-Qi/repos

        @SerialName("site_admin")
        val siteAdmin: Boolean = false, // false

        @SerialName("starred_url")
        val starredUrl: String = "", // https://api.github.com/users/lcb-Qi/starred{/owner}{/repo}
        @SerialName("subscriptions_url")
        val subscriptionsUrl: String = "", // https://api.github.com/users/lcb-Qi/subscriptions

        @SerialName("type")
        val type: String = "", // User

        @SerialName("url")
        val url: String = "" // https://api.github.com/users/lcb-Qi
    )
}