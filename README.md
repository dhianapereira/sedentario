# sedentário

Um app para sedentários.

## Como executar

### Pré-requisitos

- Android Studio com JDK 17.
- Android SDK 35 instalado.
- Emulador ou dispositivo com Android API 26 ou superior.

### Android Studio

1. Abra a raiz do projeto no Android Studio.
2. Aguarde a sincronização do Gradle.
3. Selecione o módulo `app` e um dispositivo ou emulador.
4. Clique em **Run**.

O Android Studio cria o `local.properties` automaticamente com o caminho do SDK local.

### Terminal

Use o Gradle Wrapper incluído no projeto:

```bash
# Gerar o APK de debug
./gradlew assembleDebug

# Instalar em um dispositivo conectado
./gradlew installDebug

# Executar os testes unitários
./gradlew testDebugUnitTest

# Executar o Android Lint
./gradlew lintDebug
```

No Windows, substitua `./gradlew` por `gradlew.bat`.

## Estrutura do projeto

O projeto utiliza MVVM e organiza a interface por funcionalidade:

- `model/`: modelos e tipos de negócio.
- `data/preferences/`: preferências do usuário persistidas com DataStore.
- `di/`: bindings de injeção de dependência com Hilt.
- `ui/components/`: componentes gerais compartilhados pelo app.
- `ui/navigation/`: fluxo de navegação entre as telas.
- `ui/settings/`: tela e componentes das configurações.
- `ui/tracker/`: tela, componentes, estado e `ViewModel` do tracker.
- `ui/theme/`: cores e tema do Jetpack Compose.
