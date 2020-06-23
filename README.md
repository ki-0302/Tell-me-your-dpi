# ![App Logo](app/src/main/res/drawable/ic_logo.png)Tell me your dpi App

# アーキテクチャ

このアプリは[Google I/O Android App](https://github.com/google/iosched)のアーキテクチャを参考に作成しています。

MVVMをベースにクリーンアーキテクチャを取り入れ、UseCase、Repository、各DataSourceが追加される形となっています。

![Architecture](docs/img/app-architecture.png "Architecture")

画面の構成としてはActivityはMainActivityのみとなっており、Home、リリースノート、このアプリについての各画面がFragmentで切り替わる仕組みになっています。

画面下にはBottomNavigationを配置し、各ボタンをクリックすることでFragmentが切り替わるようになっています。

Navigation GraphとBottomNavigationを利用することで、コード上ではFragmentの生成処理はあえて記述せずに済むようになりました。

このアプリでは画面数が少ないためあまり恩恵はありませんが、JetpackのNavigationを利用することにより、画面の移動に関して様々な恩恵を受けることができます。

## Home(Device info)

デバイスのDPIやOSバージョン、メモリの情報などを表示する画面です。

右下のフローティングボタンをクリックすることにより、表示している情報をクリップボードへコピーすることも可能です。





## ReleaseNotes（リリースノート）

アプリのリリースノートを表示する画面です。

アプリのリリースタイミングで更新ができるように、Webサイト上に配置したJSONファイルからデータを取得し、RecyclerViewでリスト形式で各バージョンのリリース情報を表示しています。

今後、REST APIなどにデータソースを変更した場合にも、アプリの変更をせずに対応できるようにする狙いもあります。

データの取得には [Retrofit](https://github.com/square/retrofit) を使用しています。

Retrofitがsuspendをサポートするようになったため、非同期処理部分は今回はRxJavaの採用は見送り、最近採用が増えてきている[Coroutines Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/)を使用して実装しています。

これによりコードが簡潔に記載でき、状態の管理も非常に簡単にできるようになっています。

例としてストリームとDataBindingを合わせることで、ローディングや成功時のリスト表示、エラーの表示を普段のDataBindingとほとんど手間がかわらず記載することができるようになっています。

Flowはコールドストリームのためメモリリークもほとんど意識せずにすむようになりました。

### エラー時の処理

エラーもストリームで管理が容易なためエラー時のみ専用の画面を表示し、再読み込み用のボタンをクリックすることで再度UseCaseからRepositoryに対しリリースノートのデータ取得をするように支持するのみですみます。

クリック後にエラーメッセージを消去する処理もDataBindingがストリームに紐付いているため、あえてコーディングせずに自動的に状態が変わり非表示にするようになっています。

ローディングのProgressBarについても同様の処理で自動的に表示・非表示がなされています。

## AboutApp（このアプリについて）

このアプリについての各種情報を表示する画面です。

UIのみで制御可能な情報しかないため、UseCaseやRepositoryなどは使用していません。

### アプリバージョン

BuildConfig.VERSION_NAME よりアプリバージョンを表示しています。

### プライバシーポリシー

プライバシーポリシーはアプリの機能追加とは別のタイミングで変更が予想されるため、Webページ上に配置しています。

アプリからは [AndroidBrowserHelper](https://github.com/GoogleChrome/android-browser-helper) を使用し表示しています。

AndroidBrowserHelper の AppBar をダークテーマに対応できるように実装していますが、Webページを表示するビュー部分はアプリからは変更できないため、WebページのCSSにダークテーマの設定を追加し対応しています。

### オープンソース ライセンス

[Google Play services Plugins](https://github.com/google/play-services-plugins) の oss-licenses-plugin を使用し、自動で使用ライブラリの情報を収集し、各ライブラリのライセンス情報を表示しています。

こちらもダークテーマに対応できるよう実装しています。


## DarkTheme

このアプリはダークテーマに対応しています。

Android 10以上であれば、設定のダークテーマの有効・無効で画面表示がそれぞれに合わせた配色に切り替わるようになっています。

専用のテーマや配色、アイコンを定義することで対応しています。



# デザイン
TODO: Put a AdobeXD ScreenShot.

# 使用ライブラリ

- [Android Jetpack](https://developer.android.com/jetpack/)
  - Core
  - ConstraintLayout
  - MultiDex
  - AppCompat
  - RecyclerView
  - Navigation
  - [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)
    - ViewModel
    - LiveData
- [Material Components for Android](https://github.com/material-components/material-components-android)
- [Kotlin](https://kotlinlang.org/)
  - kotlin-stdlib
  - Coroutines
- [Firebase](https://firebase.google.com/)
  - Analytics
  - Crashlytics
  - Performance
- [Retrofit](https://github.com/square/retrofit)
- [Moshi](https://github.com/square/moshi/)
- [AndroidBrowserHelper](https://github.com/GoogleChrome/android-browser-helper)
- [Timber](https://github.com/JakeWharton/timber)
- [Google Play services Plugins](https://github.com/google/play-services-plugins)
  - oss-licenses-plugin