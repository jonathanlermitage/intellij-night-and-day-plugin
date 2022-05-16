<h1 align="center">
    <a href="https://plugins.jetbrains.com/plugin/16550-night-and-day"><img src="./src/main/resources/META-INF/pluginIcon.svg" width="84" height="84" alt="logo"/></a><br/>
    Nigh and Day
</h1>

<p align="center">
    <a href="https://plugins.jetbrains.com/plugin/16550-night-and-day"><img src="https://img.shields.io/jetbrains/plugin/v/16550-night-and-day.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/16550-night-and-day"><img src="https://img.shields.io/jetbrains/plugin/d/16550-night-and-day.svg"/></a>
    </a>
</p>

An IntelliJ IDEA based plugin that shows in Status Bar how much time left in percentage or duration (until the end of day, week, month, year, or a custom date time).  
This plugin is highly inspired from the [Day Night](https://getdaynight.com) application.

1. [Build](#build)  
2. [Contribution](#contribution)  
3. [License](#license)  
4. [Screenshots](#screenshots)  

## Build

Install JDK8+. You should be able to start Gradle Wrapper (`gradlew`).

### Gradle commands

* build plugin: `./gradlew buildPlugin`. See generated ZIP in: `build/distributions/`.
* try plugin in a standalone IDE: `./gradlew runIde`.
* check for dependencies updates: `./gradlew dependencyUpdates`.

## Contribution

Contributions should be tested.        
Please reformat new and modified code only: do not reformat the whole project or entire existing file (in other words, try to limit the amount of changes in order to speed up code review).  
To finish, don't hesitate to add your name or nickname (and LinkedIn profile, etc.) to contributors list ;-)

## License

MIT License. In other words, you can do what you want: this project is entirely OpenSource, Free and Gratis.

## Screenshots

Status bar:

![status bar](misc/screenshots/night-and-day-statusbar.png)

Settings:

![settings](misc/screenshots/night-and-day-settings.png)
