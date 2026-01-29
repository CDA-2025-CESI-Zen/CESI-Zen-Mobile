### Prérequis

Afin d'afficher et de pouvoir intéragir avec le contenu, l'API Front-Office doit être exécutée et ouverte sur le port https `5001`.

### Création et exécution de l'application Android CESI Zen

Pour créer et exécuter la version de développement de l'application Android, utilisez la configuration d'exécution à partir du widget d'exécution
dans la barre d'outils de votre IDE ou créez-la directement à partir du terminal :
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Création et exécution de l'application iOS CESI Zen

Pour créer et exécuter la version de développement de l'application iOS, utilisez la configuration d'exécution à partir du widget d'exécution
dans la barre d'outils de votre IDE ou ouvrez le répertoire [/iosApp](./iosApp) dans Xcode et exécutez-la à partir de là.

---

Plus d'information sur [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)