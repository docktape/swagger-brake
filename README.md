# swagger-brake
The idea behind swagger-brake is to detect and prevent introducing any type of breaking API change within 
your application, so your APIs can remain backward-compatible at all times.

You can find the detailed documentation [here](https://docktape.github.io/swagger-brake/).

The available releases can be found [here](https://github.com/docktape/swagger-brake/releases)

The master branch represents the ongoing development of the tool. For the
release versions, you can check out the corresponding tags.

On top of the CLI interface, there are Maven and Gradle plugins also available for 
easier integration into any existing pipeline.

Maven plugin can be found [here](https://github.com/docktape/swagger-brake-maven-plugin).

Gradle plugin can be found [here](https://github.com/docktape/swagger-brake-gradle).

## Building
The application is using Gradle as a build system and building it can be done 
by executing the following command:
```bash
$ ./gradlew clean build shadowJar
```

## Sponsoring the project
If you want to help the project to develop faster and bring in new features and bugfixes you can buy me a coffee. Donations are never a necessity but very much appreciated.

Either use my Buy me a coffee link:

[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://buymeacoffee.com/arnoldgalovics)

Or you can do it directly with PayPal:

[![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=X6XHVCPMRQEL4)

## License
```text
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Publishing

### Snapshot
`./gradlew clean publish jreleaserDeploy`

### Release
`./gradlew clean publish jreleaserFullRelease`
