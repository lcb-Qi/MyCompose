package com.lcb.one.bean


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoInfoResponse(
    @Json(name = "code")
    val code: Int = 0, // 0
    @Json(name = "data")
    val `data`: Data = Data(),
    @Json(name = "message")
    val message: String = "", // 0
    @Json(name = "ttl")
    val ttl: Int = 0 // 1
)

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "aid")
    val aid: Int = 0, // 85440373
    @Json(name = "argue_info")
    val argueInfo: ArgueInfo = ArgueInfo(),
    @Json(name = "bvid")
    val bvid: String = "", // BV117411r7R1
    @Json(name = "cid")
    val cid: Int = 0, // 146044693
    @Json(name = "copyright")
    val copyright: Int = 0, // 1
    @Json(name = "ctime")
    val ctime: Int = 0, // 1580212263
    @Json(name = "desc")
    val desc: String = "", // 【CB想说的】看完拜年祭之后最爱的一个节目！给有快板的部分简单加了一些不同风格的配乐hhh，感谢沃玛画的我！太可爱了哈哈哈哈哈哈哈！！！【Warma想说的】我画了打碟的CB，画风为了还原原版视频所以参考了四迹老师的画风，四迹老师的画真的太可爱啦！不过其实在画的过程中我遇到了一个问题，CB的耳机……到底是戴在哪个耳朵上呢？原版：av78977080编曲（配乐）：Crazy Bucket人声（配音）：Warma/谢拉曲绘：四迹/Warma动画：四迹/Crazy Bucket剧本：Mokurei-木灵君音频后期：DMYoung/纳兰寻风/Crazy Bucket包装：破晓天
    @Json(name = "desc_v2")
    val descV2: List<DescV2> = listOf(),
    @Json(name = "dimension")
    val dimension: Dimension = Dimension(),
    @Json(name = "disable_show_up_info")
    val disableShowUpInfo: Boolean = false, // false
    @Json(name = "duration")
    val duration: Int = 0, // 486
    @Json(name = "dynamic")
    val `dynamic`: String = "", // 进来就出不去了！！！#全民音乐UP主##CB##warma##电音##快板##拜年祭##诸神的奥运##编曲##Remix#
    @Json(name = "enable_vt")
    val enableVt: Int = 0, // 0
    @Json(name = "honor_reply")
    val honorReply: HonorReply = HonorReply(),
    @Json(name = "is_chargeable_season")
    val isChargeableSeason: Boolean = false, // false
    @Json(name = "is_season_display")
    val isSeasonDisplay: Boolean = false, // false
    @Json(name = "is_story")
    val isStory: Boolean = false, // false
    @Json(name = "is_upower_exclusive")
    val isUpowerExclusive: Boolean = false, // false
    @Json(name = "is_upower_play")
    val isUpowerPlay: Boolean = false, // false
    @Json(name = "like_icon")
    val likeIcon: String = "",
    @Json(name = "mission_id")
    val missionId: Int = 0, // 11838
    @Json(name = "need_jump_bv")
    val needJumpBv: Boolean = false, // false
    @Json(name = "no_cache")
    val noCache: Boolean = false, // false
    @Json(name = "owner")
    val owner: Owner = Owner(),
    @Json(name = "pages")
    val pages: List<Page> = listOf(),
    @Json(name = "pic")
    val pic: String = "", // http://i1.hdslb.com/bfs/archive/ea0dd34bf41e23a68175680a00e3358cd249105f.jpg
    @Json(name = "premiere")
    val premiere: Any? = Any(), // null
    @Json(name = "pubdate")
    val pubdate: Int = 0, // 1580377255
    @Json(name = "rights")
    val rights: Rights = Rights(),
    @Json(name = "staff")
    val staff: List<Staff> = listOf(),
    @Json(name = "stat")
    val stat: Stat = Stat(),
    @Json(name = "state")
    val state: Int = 0, // 0
    @Json(name = "subtitle")
    val subtitle: Subtitle = Subtitle(),
    @Json(name = "teenage_mode")
    val teenageMode: Int = 0, // 0
    @Json(name = "tid")
    val tid: Int = 0, // 28
    @Json(name = "title")
    val title: String = "", // 当我给拜年祭的快板加了电音配乐…
    @Json(name = "tname")
    val tname: String = "", // 原创音乐
    @Json(name = "user_garb")
    val userGarb: UserGarb = UserGarb(),
    @Json(name = "videos")
    val videos: Int = 0, // 1
    @Json(name = "vt_display")
    val vtDisplay: String = ""
)

@JsonClass(generateAdapter = true)
data class ArgueInfo(
    @Json(name = "argue_link")
    val argueLink: String = "",
    @Json(name = "argue_msg")
    val argueMsg: String = "",
    @Json(name = "argue_type")
    val argueType: Int = 0 // 0
)

@JsonClass(generateAdapter = true)
data class DescV2(
    @Json(name = "biz_id")
    val bizId: Int = 0, // 0
    @Json(name = "raw_text")
    val rawText: String = "", // 【CB想说的】看完拜年祭之后最爱的一个节目！给有快板的部分简单加了一些不同风格的配乐hhh，感谢沃玛画的我！太可爱了哈哈哈哈哈哈哈！！！【Warma想说的】我画了打碟的CB，画风为了还原原版视频所以参考了四迹老师的画风，四迹老师的画真的太可爱啦！不过其实在画的过程中我遇到了一个问题，CB的耳机……到底是戴在哪个耳朵上呢？原版：av78977080编曲（配乐）：Crazy Bucket人声（配音）：Warma/谢拉曲绘：四迹/Warma动画：四迹/Crazy Bucket剧本：Mokurei-木灵君音频后期：DMYoung/纳兰寻风/Crazy Bucket包装：破晓天
    @Json(name = "type")
    val type: Int = 0 // 1
)

@JsonClass(generateAdapter = true)
data class Dimension(
    @Json(name = "height")
    val height: Int = 0, // 1080
    @Json(name = "rotate")
    val rotate: Int = 0, // 0
    @Json(name = "width")
    val width: Int = 0 // 1920
)

@JsonClass(generateAdapter = true)
data class HonorReply(
    @Json(name = "honor")
    val honor: List<Honor> = listOf()
)

@JsonClass(generateAdapter = true)
data class Honor(
    @Json(name = "aid")
    val aid: Int = 0, // 85440373
    @Json(name = "desc")
    val desc: String = "", // 第45期每周必看
    @Json(name = "type")
    val type: Int = 0, // 2
    @Json(name = "weekly_recommend_num")
    val weeklyRecommendNum: Int = 0 // 45
)

@JsonClass(generateAdapter = true)
data class Owner(
    @Json(name = "face")
    val face: String = "", // https://i2.hdslb.com/bfs/face/c9af3b32cf74baec5a4b65af8ca18ae5ff571f77.jpg
    @Json(name = "mid")
    val mid: Int = 0, // 66606350
    @Json(name = "name")
    val name: String = "" // Crazy_Bucket_陈楒潼
)

@JsonClass(generateAdapter = true)
data class Page(
    @Json(name = "cid")
    val cid: Int = 0, // 146044693
    @Json(name = "dimension")
    val dimension: Dimension = Dimension(),
    @Json(name = "duration")
    val duration: Int = 0, // 486
    @Json(name = "from")
    val from: String = "", // vupload
    @Json(name = "page")
    val page: Int = 0, // 1
    @Json(name = "part")
    val part: String = "", // 建议改成：建议改成：诸 神 的 电 音 节（不是）
    @Json(name = "vid")
    val vid: String = "",
    @Json(name = "weblink")
    val weblink: String = ""
)

@JsonClass(generateAdapter = true)
data class Rights(
    @Json(name = "arc_pay")
    val arcPay: Int = 0, // 0
    @Json(name = "autoplay")
    val autoplay: Int = 0, // 1
    @Json(name = "bp")
    val bp: Int = 0, // 0
    @Json(name = "clean_mode")
    val cleanMode: Int = 0, // 0
    @Json(name = "download")
    val download: Int = 0, // 1
    @Json(name = "elec")
    val elec: Int = 0, // 0
    @Json(name = "free_watch")
    val freeWatch: Int = 0, // 0
    @Json(name = "hd5")
    val hd5: Int = 0, // 1
    @Json(name = "is_360")
    val is360: Int = 0, // 0
    @Json(name = "is_cooperation")
    val isCooperation: Int = 0, // 1
    @Json(name = "is_stein_gate")
    val isSteinGate: Int = 0, // 0
    @Json(name = "movie")
    val movie: Int = 0, // 0
    @Json(name = "no_background")
    val noBackground: Int = 0, // 0
    @Json(name = "no_reprint")
    val noReprint: Int = 0, // 1
    @Json(name = "no_share")
    val noShare: Int = 0, // 0
    @Json(name = "pay")
    val pay: Int = 0, // 0
    @Json(name = "ugc_pay")
    val ugcPay: Int = 0, // 0
    @Json(name = "ugc_pay_preview")
    val ugcPayPreview: Int = 0 // 0
)

@JsonClass(generateAdapter = true)
data class Staff(
    @Json(name = "face")
    val face: String = "", // https://i2.hdslb.com/bfs/face/c9af3b32cf74baec5a4b65af8ca18ae5ff571f77.jpg
    @Json(name = "follower")
    val follower: Int = 0, // 650859
    @Json(name = "label_style")
    val labelStyle: Int = 0, // 0
    @Json(name = "mid")
    val mid: Int = 0, // 66606350
    @Json(name = "name")
    val name: String = "", // Crazy_Bucket_陈楒潼
    @Json(name = "official")
    val official: Official = Official(),
    @Json(name = "title")
    val title: String = "", // UP主
    @Json(name = "vip")
    val vip: Vip = Vip()
)

@JsonClass(generateAdapter = true)
data class Official(
    @Json(name = "desc")
    val desc: String = "",
    @Json(name = "role")
    val role: Int = 0, // 1
    @Json(name = "title")
    val title: String = "", // bilibili 知名音乐UP主
    @Json(name = "type")
    val type: Int = 0 // 0
)

@JsonClass(generateAdapter = true)
data class Vip(
    @Json(name = "avatar_subscript")
    val avatarSubscript: Int = 0, // 1
    @Json(name = "avatar_subscript_url")
    val avatarSubscriptUrl: String = "",
    @Json(name = "due_date")
    val dueDate: Long = 0, // 1706112000000
    @Json(name = "label")
    val label: Label = Label(),
    @Json(name = "nickname_color")
    val nicknameColor: String = "", // #FB7299
    @Json(name = "role")
    val role: Int = 0, // 3
    @Json(name = "status")
    val status: Int = 0, // 1
    @Json(name = "theme_type")
    val themeType: Int = 0, // 0
    @Json(name = "tv_due_date")
    val tvDueDate: Int = 0, // 0
    @Json(name = "tv_vip_pay_type")
    val tvVipPayType: Int = 0, // 0
    @Json(name = "tv_vip_status")
    val tvVipStatus: Int = 0, // 0
    @Json(name = "type")
    val type: Int = 0, // 2
    @Json(name = "vip_pay_type")
    val vipPayType: Int = 0 // 0
)

@JsonClass(generateAdapter = true)
data class Label(
    @Json(name = "bg_color")
    val bgColor: String = "", // #FB7299
    @Json(name = "bg_style")
    val bgStyle: Int = 0, // 1
    @Json(name = "border_color")
    val borderColor: String = "",
    @Json(name = "img_label_uri_hans")
    val imgLabelUriHans: String = "",
    @Json(name = "img_label_uri_hans_static")
    val imgLabelUriHansStatic: String = "", // https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png
    @Json(name = "img_label_uri_hant")
    val imgLabelUriHant: String = "",
    @Json(name = "img_label_uri_hant_static")
    val imgLabelUriHantStatic: String = "", // https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png
    @Json(name = "label_theme")
    val labelTheme: String = "", // annual_vip
    @Json(name = "path")
    val path: String = "",
    @Json(name = "text")
    val text: String = "", // 年度大会员
    @Json(name = "text_color")
    val textColor: String = "", // #FFFFFF
    @Json(name = "use_img_label")
    val useImgLabel: Boolean = false // true
)

@JsonClass(generateAdapter = true)
data class Stat(
    @Json(name = "aid")
    val aid: Int = 0, // 85440373
    @Json(name = "coin")
    val coin: Int = 0, // 72088
    @Json(name = "danmaku")
    val danmaku: Int = 0, // 12026
    @Json(name = "dislike")
    val dislike: Int = 0, // 0
    @Json(name = "evaluation")
    val evaluation: String = "",
    @Json(name = "favorite")
    val favorite: Int = 0, // 58761
    @Json(name = "his_rank")
    val hisRank: Int = 0, // 55
    @Json(name = "like")
    val like: Int = 0, // 159350
    @Json(name = "now_rank")
    val nowRank: Int = 0, // 0
    @Json(name = "reply")
    val reply: Int = 0, // 2648
    @Json(name = "share")
    val share: Int = 0, // 9539
    @Json(name = "view")
    val view: Int = 0, // 2353489
    @Json(name = "vt")
    val vt: Int = 0 // 0
)

@JsonClass(generateAdapter = true)
data class Subtitle(
    @Json(name = "allow_submit")
    val allowSubmit: Boolean = false, // false
    @Json(name = "list")
    val list: List<Reserve> = listOf()
)

@JsonClass(generateAdapter = true)
data class Reserve(
    @Json(name = "ai_status")
    val aiStatus: Int = 0, // 2
    @Json(name = "ai_type")
    val aiType: Int = 0, // 0
    @Json(name = "author")
    val author: Author = Author(),
    @Json(name = "id")
    val id: Long = 0, // 1061981378473780000
    @Json(name = "id_str")
    val idStr: String = "", // 1061981378473779968
    @Json(name = "is_lock")
    val isLock: Boolean = false, // false
    @Json(name = "lan")
    val lan: String = "", // ai-zh
    @Json(name = "lan_doc")
    val lanDoc: String = "", // 中文（自动生成）
    @Json(name = "subtitle_url")
    val subtitleUrl: String = "",
    @Json(name = "type")
    val type: Int = 0 // 1
)

@JsonClass(generateAdapter = true)
data class Author(
    @Json(name = "birthday")
    val birthday: Int = 0, // 0
    @Json(name = "face")
    val face: String = "",
    @Json(name = "in_reg_audit")
    val inRegAudit: Int = 0, // 0
    @Json(name = "is_deleted")
    val isDeleted: Int = 0, // 0
    @Json(name = "is_fake_account")
    val isFakeAccount: Int = 0, // 0
    @Json(name = "is_senior_member")
    val isSeniorMember: Int = 0, // 0
    @Json(name = "mid")
    val mid: Int = 0, // 0
    @Json(name = "name")
    val name: String = "",
    @Json(name = "rank")
    val rank: Int = 0, // 0
    @Json(name = "sex")
    val sex: String = "",
    @Json(name = "sign")
    val sign: String = ""
)

@JsonClass(generateAdapter = true)
data class UserGarb(
    @Json(name = "url_image_ani_cut")
    val urlImageAniCut: String = "" // https://i0.hdslb.com/bfs/garb/item/311b29b795eb3f09ed0401a56eddf5c90b8fbfd6.bin
)