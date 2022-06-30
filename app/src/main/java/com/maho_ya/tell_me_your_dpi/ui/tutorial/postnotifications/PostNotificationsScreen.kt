package com.maho_ya.tell_me_your_dpi.ui.tutorial.postnotifications

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.ui.Screen
import com.maho_ya.tell_me_your_dpi.ui.TdpiApp
import com.maho_ya.tell_me_your_dpi.ui.home.HomeUiState
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme
import com.maho_ya.tell_me_your_dpi.ui.theme.Colors
import com.maho_ya.tell_me_your_dpi.ui.theme.PreviewDefault

private val defaultSpacerSize = 32.dp

@Composable
fun PostNotificationsContent(
    modifier: Modifier = Modifier,
    onActivate: () -> Unit = {},
    onSkip: () -> Unit = {}
) {

    Box(
        Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(defaultSpacerSize),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                modifier = Modifier.size(64.dp),
                painter = painterResource(id = AppTheme.images.notifications),
                contentDescription = stringResource(id = R.string.title_tutorial_post_notifications),
                colorFilter = ColorFilter.tint(Colors.YellowEE9A00)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.post_notifications_description),
                style = MaterialTheme.typography.body1,
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(defaultSpacerSize)
        ) {
            Button(
                modifier = modifier,
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
                onClick = onSkip
            ) {
                Text(stringResource(id = R.string.post_notifications_skip_button))
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(defaultSpacerSize)
        ) {
            Button(
                modifier = modifier,
                onClick = onActivate
            ) {
                Text(stringResource(id = R.string.post_notifications_activate_button))
            }
        }
    }
}

@Composable
fun PostNotificationsScreen(
    modifier: Modifier = Modifier,
    onActivate: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    // parentサイズ分のレイアウト
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        PostNotificationsContent(
            modifier = modifier,
            onActivate = onActivate,
            onSkip = onSkip
        )
    }
}

@Composable
fun PostNotificationsRoute(
    openDialog: MutableState<Boolean> = mutableStateOf(false),
    isFirstPostNotificationsPermission: MutableState<Boolean> = mutableStateOf(false),
    onFirstPostNotificationsPermissionComplete: () -> Unit = {}
) {
    RequestPostNotificationPermission(
        openDialog = openDialog,
        isFirstPostNotificationsPermission = isFirstPostNotificationsPermission,
        onFirstPostNotificationsPermissionComplete = onFirstPostNotificationsPermissionComplete
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun RequestPostNotificationPermission(
    openDialog: MutableState<Boolean> = mutableStateOf(false),
    isFirstPostNotificationsPermission: MutableState<Boolean> = mutableStateOf(false),
    onFirstPostNotificationsPermissionComplete: () -> Unit = {}
) {
    val permissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    when (permissionState.status) {
        is PermissionStatus.Granted -> openDialog.value = false
        is PermissionStatus.Denied -> {
            if (isFirstPostNotificationsPermission.value || permissionState.status.shouldShowRationale) {
                PostNotificationsScreen(
                    onActivate = {
                        permissionState.launchPermissionRequest()
                        onFirstPostNotificationsPermissionComplete()
                        openDialog.value = false
                    },
                    onSkip = {
                        openDialog.value = false
                    }
                )
            } else {
                PostNotificationsErrorDialog(openDialog)
            }
        }
    }
}

@Composable
fun PostNotificationsErrorDialog(
    openDialog: MutableState<Boolean> = mutableStateOf(false),
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(stringResource(id = R.string.title_tutorial_post_notifications_denied))
        },
        text = {
            Text(stringResource(id = R.string.post_notifications_denied_description))
        },
        confirmButton = {
            Button(
                onClick = {
                    openNotificationSetting(context)
                    openDialog.value = false
                }
            ) {
                Text(stringResource(id = R.string.ok_button))
            }
        }
    )
}

/**
 * 設定画面から通知設定を開く
 */
private fun openNotificationSetting(context: Context) {
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
    } else {
        // Android 5 以上
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", context.packageName)
        intent.putExtra("app_uid", context.applicationInfo.uid)
    }
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

@Preview("About screen")
@Preview("About screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@PreviewDefault
@Composable
fun PreviewPostNotificationsScreen() {
    TdpiApp(hasAppBar = false) {
        PostNotificationsScreen()
    }
}
