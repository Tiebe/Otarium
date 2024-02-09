#!/usr/bin/env bash
brew install cocoapods
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 19.0.1-tem

# cd into actual project root
cd ../../
./gradlew shared:podinstall