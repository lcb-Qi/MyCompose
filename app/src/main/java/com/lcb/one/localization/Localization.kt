package com.lcb.one.localization

import androidx.annotation.StringRes
import com.lcb.one.R
import com.lcb.one.ui.MyApp

class Localization private constructor(@StringRes private val resId: Int) {
    companion object {
        val goSettings get() = Localization(R.string.go_settings).toString()
        val appName get() = Localization(R.string.app_name).toString()
        val ok get() = Localization(R.string.ok).toString()
        val cancel get() = Localization(R.string.cancel).toString()
        val settings get() = Localization(R.string.settings).toString()
        val home get() = Localization(R.string.home).toString()
        val dynamicColor get() = Localization(R.string.dynamic_color).toString()
        val dynamicColorSummary get() = Localization(R.string.dynamic_color_summary).toString()
        val poemUpdateDuration get() = Localization(R.string.poem_update_duration).toString()
        val tool get() = Localization(R.string.tool).toString()
        val more get() = Localization(R.string.more).toString()
        val versionInfo get() = Localization(R.string.version_info).toString()
        val projectUrl get() = Localization(R.string.project_url).toString()
        val loading get() = Localization(R.string.loading).toString()
        val save get() = Localization(R.string.save).toString()
        val obtain get() = Localization(R.string.obtain).toString()
        val device get() = Localization(R.string.device).toString()
        val deviceInfo get() = Localization(R.string.device_info).toString()
        val extractWallpaper get() = Localization(R.string.extract_wallpaper).toString()
        val bv2av get() = Localization(R.string.bv_to_av).toString()
        val av2bv get() = Localization(R.string.av_to_bv).toString()
        val other get() = Localization(R.string.other).toString()
        val obtainBilibiliCover get() = Localization(R.string.obtain_bilibili_cover).toString()
        val clockScreen get() = Localization(R.string.clock_screen).toString()
        val leftBottom get() = Localization(R.string.left_bottom).toString()
        val rightBottom get() = Localization(R.string.right_bottom).toString()
        val centerAbove get() = Localization(R.string.center_above).toString()
        val centerBelow get() = Localization(R.string.center_below).toString()
        val positionOfDate get() = Localization(R.string.date_position).toString()
        val bibibili get() = Localization(R.string.bibibili).toString()
        val display get() = Localization(R.string.display).toString()
        val fallInLove get() = Localization(R.string.fall_in_love).toString()
        val offWorkCountdown get() = Localization(R.string.off_work_countdown).toString()
        val breakTime get() = Localization(R.string.break_time).toString()
        val friendDuration get() = Localization(R.string.friendly_duration).toString()
        val friendDurationNoDays get() = Localization(R.string.friendly_duration_no_days).toString()
        val days get() = Localization(R.string.days).toString()
        val paydayCountdown get() = Localization(R.string.payday_countdown).toString()
        val payday get() = Localization(R.string.payady).toString()
        val appList get() = Localization(R.string.app_list).toString()
        val userApps get() = Localization(R.string.user_apps).toString()
        val systemApps get() = Localization(R.string.system_apps).toString()
        val allApps get() = Localization(R.string.all_apps).toString()
        val menstrualAssistant get() = Localization(R.string.menstruation_assistant).toString()
        val saveSuccess get() = Localization(R.string.save_success).toString()
        val saveFailed get() = Localization(R.string.save_failed).toString()
        val getCoverHint get() = Localization(R.string.get_cover_hint).toString()
        val common get() = Localization(R.string.common).toString()
        val theme get() = Localization(R.string.theme).toString()
        val about get() = Localization(R.string.about).toString()
        val enterDevMode get() = Localization(R.string.enter_dev_mode).toString()
        val exitDevMode get() = Localization(R.string.exit_dev_mode).toString()
        val checkUpdates get() = Localization(R.string.check_updates).toString()
        val noNewVersion get() = Localization(R.string.no_new_version).toString()
        val alreadyLast get() = Localization(R.string.already_last_version).toString()
        val loadSuccessMsg get() = Localization(R.string.load_success_msg).toString()
        val menstruationStart get() = Localization(R.string.menstruation_start).toString()
        val menstruationEnded get() = Localization(R.string.menstruation_ended).toString()
        val menstruationDays get() = Localization(R.string.menstruation_days).toString()
        val menstruationPredictEndIn get() = Localization(R.string.menstruation_predict_end_in).toString()
        val menstruationTips get() = Localization(R.string.menstruation_tips).toString()
        val noMenstruation get() = Localization(R.string.no_menstruation).toString()
        val menstruationPrediction get() = Localization(R.string.menstruation_prediction).toString()
        val nextMenstruationInfo get() = Localization(R.string.next_menstruation_info).toString()
        val nextMenstruationDays get() = Localization(R.string.next_menstruation_days).toString()
        val allRecords get() = Localization(R.string.all_records).toString()
        val conflictTips get() = Localization(R.string.conflict_tips).toString()
        val confirmDelete get() = Localization(R.string.confirm_delete).toString()
        val sumOfRecords get() = Localization(R.string.sum_of_records).toString()
        val averageDuration get() = Localization(R.string.average_duration).toString()
        val averageInterval get() = Localization(R.string.average_interval).toString()
        val menstruation get() = Localization(R.string.menstruation).toString()
        val to get() = Localization(R.string.to).toString()
        val going get() = Localization(R.string.going).toString()
        val doImport get() = Localization(R.string.do_import).toString()
        val amoledModeSummary get() = Localization(R.string.amoled_mode_summary).toString()
        val darkMode get() = Localization(R.string.dark_mode).toString()
        val onlyThisPage get() = Localization(R.string.effective_this_page).toString()
        val clockFontSize get() = Localization(R.string.clock_font_size).toString()
        val sumOfApps get() = Localization(R.string.sum_of_apps).toString()
        val extractIcon get() = Localization(R.string.extract_icon).toString()
        val extractPackage get() = Localization(R.string.extract_installation_package).toString()
        val friendlyExitTips get() = Localization(R.string.friendly_exit_tips).toString()
        val toDownload get() = Localization(R.string.to_download).toString()
    }

    override fun toString(): String {
        val resources = MyApp.getAppContext().resources
        return resources.getString(resId)
    }
}