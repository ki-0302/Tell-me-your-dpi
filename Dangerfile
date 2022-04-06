github.dismiss_out_of_range_messages

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR!
warn("Big PR") if git.lines_of_code > 500

# ktlintの結果をコメント投稿
Dir.glob("reports/ktlint/*.xml").each { |report|
  checkstyle_format.base_path = Dir.pwd
  checkstyle_format.report report.to_s
}

# Android lintの結果をコメント投稿
android_lint.skip_gradle_task = true # 予めlint実行してあるためGradleの実行をスキップ
android_lint.report_file = 'reports/lint/lint-results-app.xml'
android_lint.severity = "Warning"
android_lint.filtering = false # すべてのファイルをLint対象にする
android_lint.lint(inline_mode: true) # コメントで出力。GitHub用

# dependency updates
danger.import_plugin("./dependency_updates.rb")
dependency_updates.checkDependencyUpdates
