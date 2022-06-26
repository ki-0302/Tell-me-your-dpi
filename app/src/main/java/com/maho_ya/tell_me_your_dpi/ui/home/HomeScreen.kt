package com.maho_ya.tell_me_your_dpi.ui.home

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.material.snackbar.Snackbar
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.domain.device.DeviceUseCase
import com.maho_ya.tell_me_your_dpi.model.Device
import com.maho_ya.tell_me_your_dpi.ui.TdpiApp
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme
import com.maho_ya.tell_me_your_dpi.ui.theme.Colors

private val defaultSpacerSize = 16.dp
private val spacerSizeBetweenTitleAndDescription = 3.dp
private val errorMessagePadding = 20.dp

data class DeviceItem(
    val title: String,
    val description: String
)

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    listState: LazyListState = rememberLazyListState(),
    onRefresh: () -> Unit
) {
    LoadingHomeContent(
        isLoading = uiState.isLoading,
        onRefresh = onRefresh,
        content = {
            if (uiState.userMessage != null) {
                HomeErrorContent(
                    modifier = modifier,
                    userMessage = uiState.userMessage,
                    onClick = onRefresh
                )
            } else {
                HomeContentList(
                    modifier = modifier,
                    uiState = uiState,
                    listState = listState,
                )
            }
        }
    )
}

@Composable
fun HomeContentList(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    listState: LazyListState = rememberLazyListState(),
) {
    val items = getDeviceItemList(LocalContext.current, uiState.device)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
    ) {
        itemsIndexed(items) { index, item ->
            HomeContentItem(
                modifier = modifier,
                deviceItem = item,
                hasDivider = index < items.size - 1
            )
        }
    }
}

private fun getDeviceItemList(
    context: Context,
    device: Device?
): List<DeviceItem> {
    return listOf(
        DeviceItem(
            title = context.getString(R.string.device_density_qualifier_title),
            description = device?.densityQualifier.orEmpty()
        ),
        DeviceItem(
            title = context.getString(R.string.device_density_dpi_title),
            description = device?.densityDpi.toString()
        ),
        DeviceItem(
            title = context.getString(R.string.device_real_display_size_width_title),
            description = context.getString(
                R.string.device_real_display_size,
                device?.realDisplaySizeWidth ?: 0
            )
        ),
        DeviceItem(
            title = context.getString(R.string.device_real_display_size_height_title),
            description = context.getString(
                R.string.device_real_display_size,
                device?.realDisplaySizeHeight ?: 0
            )
        ),
        DeviceItem(
            title = context.getString(R.string.device_brand_title),
            description = device?.brand.orEmpty()
        ),
        DeviceItem(
            title = context.getString(R.string.device_model_title),
            description = device?.model.orEmpty()
        ),
        DeviceItem(
            title = context.getString(R.string.device_api_level_title),
            description = device?.apiLevel.toString()
        ),
        DeviceItem(
            title = context.getString(R.string.device_android_os_version_title),
            description = device?.androidOsVersion.orEmpty()
        ),
        DeviceItem(
            title = context.getString(R.string.device_android_code_name_title),
            description = device?.androidCodeName.orEmpty()
        ),
        DeviceItem(
            title = context.getString(R.string.device_memory_size_title),
            description = context.getString(
                R.string.device_memory_size,
                device?.totalMemory ?: 0,
                device?.availableMemory ?: 0
            ),
        ),
    )
}

@Composable
fun HomeContentItem(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    deviceItem: DeviceItem,
    hasDivider: Boolean = true
) {
    Column(modifier = modifier.padding(defaultSpacerSize)) {
        Text(
            text = deviceItem.title,
            style = MaterialTheme.typography.subtitle2,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = spacerSizeBetweenTitleAndDescription),
        )

        Text(
            text = deviceItem.description,
            style = MaterialTheme.typography.body1,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = spacerSizeBetweenTitleAndDescription),
        )
    }

    if (hasDivider) Divider(color = if (darkTheme) Colors.Gray555 else Colors.GrayAAA)
}

@Composable
fun HomeErrorContent(
    modifier: Modifier = Modifier,
    userMessage: String?,
    onClick: () -> Unit,
) {
    if (userMessage == null) return

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier.padding(errorMessagePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userMessage,
                style = MaterialTheme.typography.body1,
                modifier = modifier.padding(defaultSpacerSize),
            )
            Button(
                modifier = modifier,
                onClick = onClick
            ) {
                Text(
                    text = stringResource(R.string.network_error_reload),
                    style = MaterialTheme.typography.button,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(defaultSpacerSize)
                )
            }
        }
    }
}

@Composable
fun LoadingHomeContent(
    isLoading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    // https://google.github.io/accompanist/swiperefresh/
    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = onRefresh,
        content = content
    )
}

@Composable
private fun CopyFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier
                .padding(16.dp)
                .height(64.dp)
                .align(Alignment.BottomEnd)
                .widthIn(min = 64.dp),
            backgroundColor = MaterialTheme.colors.onSecondary
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = AppTheme.images.copyFab),
                contentDescription = stringResource(
                    R.string.copy_button
                )
            )
        }
    }
}

private fun copyDeviceInfo(context: Context, device: Device?) {
    val deviceInfo = context.getString(R.string.device_density_qualifier_title) + ": " +
            device?.densityQualifier + "\n" +
            context.getString(R.string.device_density_dpi_title) + ": " +
            device?.densityDpi.toString() + "\n" +
            context.getString(R.string.device_real_display_size_width_title) + ": " +
            context.getString(
                R.string.device_real_display_size,
                device?.realDisplaySizeWidth ?: 0
            ) + "\n" +
            context.getString(R.string.device_real_display_size_height_title) + ": " +
            context.getString(
                R.string.device_real_display_size,
                device?.realDisplaySizeHeight
            ) + "\n" +
            context.getString(R.string.device_brand_title) + ": " +
            device?.brand + "\n" +
            context.getString(R.string.device_model_title) + ": " +
            device?.model + "\n" +
            context.getString(R.string.device_api_level_title) + ": " +
            device?.apiLevel.toString() + "\n" +
            context.getString(R.string.device_android_os_version_title) + ": " +
            device?.androidOsVersion + "\n" +
            context.getString(R.string.device_android_code_name_title) + ": " +
            device?.androidCodeName + "\n" +
            context.getString(R.string.device_memory_size_title) + ": " +
            context.getString(
                R.string.device_memory_size,
                device?.totalMemory ?: 0,
                device?.availableMemory ?: 0
            )
    val clipboardManager: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    clipboardManager.setPrimaryClip(ClipData.newPlainText("", deviceInfo))

}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    listState: LazyListState = rememberLazyListState(),
    onRefresh: () -> Unit = {},
    copyFabClick: () -> Unit = {}
) {
    val context = LocalContext.current
    HomeContent(
        modifier = modifier,
        uiState = uiState,
        listState = listState,
        onRefresh = onRefresh
    )
    CopyFab(
        onClick = copyFabClick
    )
}

@Preview("Home screen")
@Preview("Home screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    val uiState = HomeUiState(
        device = Device(
            densityQualifier = "xxxdpi",
            densityDpi = 440,
            realDisplaySizeWidth = 1080,
            realDisplaySizeHeight = 2280,
            brand = "brand",
            model = "model",
            apiLevel = 33,
            androidOsVersion = "13",
            androidCodeName = "Android13 (Tiramisu)",
            totalMemory = 3072,
            availableMemory = 1024
        )
    )

    TdpiApp() {
        HomeScreen(uiState = uiState)
    }
}

@Composable
fun HomeRoute(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    homeViewModel: HomeVieModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    // collectAsState() でuiStateをリスナーし、値が変わると再コンポーズするようになる
    val uiState by homeViewModel.uiState.collectAsState()
    // スクロール位置やアイテムのレイアウトの変更を検知できる
    val listState: LazyListState = rememberLazyListState()

    val context = LocalContext.current

    // https://developer.android.com/jetpack/compose/state?hl=ja#state-in-composables
    // rememberでメモリに保持するようになり、再コンポーズした場合に再利用される。
    // 不変の場合は remember { T }, 可変の場合は remember { mutableStateOf(T) } を使用する
    // 設定の変更時（Activityの再生性など）には破棄される。保持する場合には rememberSaveable を使用する
    val deviceUseCase = remember { DeviceUseCase(context = context) }

    // https://developer.android.com/jetpack/compose/side-effects?hl=ja#disposableeffect
    // 渡したキーの変更後に実行する必要がある場合に使用する
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                homeViewModel.getDevice(deviceUseCase)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Snackbarの表示
    if (uiState.shouldLaunchSnackBar) {
        val copyCompleteString = stringResource(id = R.string.copy_complete)
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = copyCompleteString,
                duration = SnackbarDuration.Short
            )
            homeViewModel.shownSnackBar()
        }
    }

    HomeScreen(
        uiState = uiState,
        listState = listState,
        onRefresh = { homeViewModel.getDevice(deviceUseCase) },
        copyFabClick = {
            copyDeviceInfo(context, uiState.device)
            homeViewModel.showSnackBar()
        }
    )
}
