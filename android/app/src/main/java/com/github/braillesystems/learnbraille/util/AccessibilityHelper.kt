package com.github.braillesystems.learnbraille.util

import android.content.Context
import android.content.Context.ACCESSIBILITY_SERVICE
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_ANNOUNCEMENT
import android.view.accessibility.AccessibilityManager

// https://stackoverflow.com/a/50069388 - is there a way to make TalkBack speak?
class AccessibilityHelper {
    companion object {
        @JvmStatic
        fun announceForAccessibility(context: Context?, announcement: String) {
            context!!
                .getSystemService(ACCESSIBILITY_SERVICE)
                .let { it as AccessibilityManager }
                .let { manager ->
                    AccessibilityEvent
                        .obtain()
                        .apply {
                            eventType = TYPE_ANNOUNCEMENT
                            className = context.javaClass.name
                            packageName = context.packageName
                            val re = Regex("\\<[^>]*>") // remove HTML markup
                            text.add(re.replace(announcement, ""))
                        }
                        .let {
                            manager.sendAccessibilityEvent(it)
                        }
                }
        }
    }
}