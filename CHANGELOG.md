# Night and Day Change Log

## 1.8.0 (2024/06/14)
* integrate the new IDE [Exception Analyzer](https://plugins.jetbrains.com/docs/marketplace/exception-analyzer.html). This is an easy way to report plugin exceptions from IntelliJ Platform-based products to plugin developers right on JetBrains Marketplace, instead of opening an issue on the plugin's GitHub repository.
* dependencies upgrade.

## 1.7.2 (2024/03/04)
* minor performance optimization: reduce threads spawning by using IDE thread pool and scheduler instead of starting new threads for scheduled tasks.

## 1.7.1 (2023/11/08)
* Fix compatibility with future IDEs.

## 1.7.0 (2023/09/25)
* Improve the rendering of the graphical progress bar when the new UI is enabled.

## 1.6.3 (2023/09/09)
* Rework usage of deprecated JetBrains API, fix compatibility with future IDE releases.

## 1.6.2 (2022/11/26)
* Remove usage of deprecated JetBrains API, fix compatibility with future IDE releases.

## 1.6.1 (2022/05/16)
* Fix usage of deprecated JetBrains APIs.

## 1.6.0 (2021/11/26)
* Add an option to hide status bar widget.

## 1.5.0 (2021/10/17)
* Minor performance optimization.

## 1.4.0 (2021/10/09)
* Make status bar items ordering more consistent.
* Fix: the config panel was constantly marked as modified. Changes are now correctly detected.
* Fix: some preferences were not persisted.

## 1.3.0 (2021/06/20)
* Internal: remove usage of code deprecated in IJ 2021.2.

## 1.2.1 (2021/05/24)
* Revert 1.2.0 change (Plugin can be loaded with no IDE restart): a bug in IJ resets plugin preferences.

## 1.2.0 (2021/05/22)
* Plugin can be loaded with no IDE restart.

## 1.1.0 (2021/05/01)
* Progress bar colors are now customizable.

## 1.0.0 (2021/04/14)
* First release.
