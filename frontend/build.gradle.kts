import com.github.gradle.node.npm.task.NpmTask

plugins {
  id("com.github.node-gradle.node") version "7.0.0"
}

node {
  download.set(true)
  version.set("20.0.0")
}

tasks.register<NpmTask>("npmBuild") {
  dependsOn("npmInstall")
  args.set(listOf("run", "build"))
}

tasks.register<NpmTask>("npmStart") {
  dependsOn("npmInstall")
  args.set(listOf("run", "start"))
}
