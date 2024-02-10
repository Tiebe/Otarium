#!/usr/bin/env bash
brew install cocoapods
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 19.0.1-tem

# cd into iosapp root
cd ../

VERSION=$(cat ../libs.versions.toml| awk '/^app-version-string = "[0-9]+/{print $3; exit}' | cut -d '"' -f 2)
agvtool new-version -all "$VERSION"
agvtool new-marketing-version "$VERSION"

# cd into actual project root
cd ../


./gradlew shared:podinstall