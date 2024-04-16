package com.lcb.one.ui.screen.device.repo.model

import com.lcb.one.util.android.PhoneUtil


data class BasicInfo(
    var brand: String = "",
    var deviceModel: String = "",
    var osVersion: String = "",
    var sdkVersion: Int = 0
) {
    companion object {
        fun obtain(): BasicInfo {
            val deviceInfo = BasicInfo().apply {
                brand = PhoneUtil.getBrand()
                osVersion = PhoneUtil.getOS()
                deviceModel = PhoneUtil.getModel()
                sdkVersion = PhoneUtil.getSdkInt()
            }

            return deviceInfo
        }
    }
}