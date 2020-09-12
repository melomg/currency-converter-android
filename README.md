# CurrencyConverter Android App

## Android Studio IDE setup

CurrencyConverter uses Android Studio version 3.5.1

CurrencyConverter uses [ktlint](https://ktlint.github.io/) to check Kotlin coding styles.

- First, close Android Studio if it's open

- Download ktlint by following instructions at [ktlint README](https://github.com/shyiko/ktlint/blob/master/README.md#installation)

- Inside the project root directory run:

  `ktlint --apply-to-idea-project --android`

- Remove ktlint if desired:

  `rm ktlint`

- Start Android Studio

## Tech Stack:

Demonstrates using Dagger 2.25+ in MVVM app with Clean Architecture.

- MVVM

- Repository Pattern

- Dagger 2 & Hilt

- Coroutines, Flow

- Architecture Components (Room, LiveData,...)

- Retrofit 2 & OkHttp 3 

## Versioning

CurrencyConverter uses [SemVer](http://semver.org/) for versioning.

## Authors

* **[Melih Gultekin](https://github.com/melomg/)**

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

