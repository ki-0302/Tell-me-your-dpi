github.dismiss_out_of_range_messages

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR!
warn("Big PR") if git.lines_of_code > 500

checkstyle_format.base_path = Dir.pwd
checkstyle_format.report 'app/build/reports/ktlint/ktlintMainSourceSetCheck.xml'

# android_lint.gradle_task = "lint"
android_lint.skip_gradle_task  =  true
android_lint.severity  =  "Warning"
android_lint.report_file = "app/build/reports/lint-results.xml"
android_lint.filtering = true
android_lint.lint(inline_mode: true)

# dependency updates
if File.file?("build/dependencyUpdates/report.json")
    warn("JSON")

end
