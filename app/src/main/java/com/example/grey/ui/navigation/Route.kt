package com.example.grey.ui.navigation

import android.net.Uri
import com.example.grey.asPlaceholder

interface Route {
    val path: String
}

sealed interface Root : Route {

    data object Home : Root {

        override val path: String
            get() = "home"

    }

    data object Repository : Root {
        override val path: String
            get() = "repository"

    }

    data object User : Root {
        override val path: String
            get() = "user"

    }
}

sealed interface UserRoute : Route {

    data class UserDetail(val username: String) : UserRoute {
        override val path: String
            get() = buildPath(username)

        companion object {
            const val ARG_USERNAME = "ARG_USERNAME"

            fun buildPath(username: String = ARG_USERNAME.asPlaceholder()): String {
                return Uri.Builder()
                    .appendEncodedPath("users")
                    .appendEncodedPath(username)
                    .appendEncodedPath("details")
                    .build()
                    .toString()
            }
        }
    }
}
