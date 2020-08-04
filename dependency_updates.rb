module Danger
    class DependencyUpdates < Plugin
        def checkDependencyUpdates
            require 'json'
            dependency_report_path = "build/dependencyUpdates/report.json"

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
    end

end
