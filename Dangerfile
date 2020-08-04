github.dismiss_out_of_range_messages

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR!
warn("Big PR") if git.lines_of_code > 500

#checkstyle_format.base_path = Dir.pwd
#checkstyle_format.report 'app/build/reports/ktlint/ktlintMainSourceSetCheck.xml'

# android_lint.gradle_task = "lint"
#android_lint.skip_gradle_task  =  true
#android_lint.severity  =  "Warning"
#android_lint.report_file = "app/build/reports/lint-results.xml"
#android_lint.filtering = true
#android_lint.lint(inline_mode: true)

# dependency updates
checkDependencyUpdates

def checkDependencyUpdates
    require 'json'
    dependency_report_path = "/Users/mahoya/Documents/android-projects/tell_me_your_dpi/build/dependencyUpdates/report.json"

    return if !File.file?(dependency_report_path)

    dependency_report_json = File.open(dependency_report_path) do |file|
        JSON.load(file)
    end

    return if !dependency_report_json.has_key?("outdated")
    return if !dependency_report_json["outdated"].has_key?("dependencies")

    dependencies = dependency_report_json["outdated"]["dependencies"]

    return if dependencies.empty?

    warn("*** Using older version of library. ***")

    dependencies.each { |dependency|
        new_version = "unknown"
        new_version = dependency["available"]["integration"] if !dependency["available"]["integration"].nil?
        new_version = dependency["available"]["milestone"] if !dependency["available"]["milestone"].nil?
        new_version = dependency["available"]["release"] if !dependency["available"]["release"].nil?
        report = "#{dependency["group"]}.#{dependency["name"]} [#{dependency["version"]} -> #{new_version}]"
        warn(report)
    }
end
