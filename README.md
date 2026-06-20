# sedentário

Um app para sedentários.

## Estrutura do projeto

O projeto utiliza MVVM e organiza a interface por funcionalidade:

- `model/`: modelos e tipos de negócio.
- `data/preferences/`: preferências do usuário persistidas com DataStore.
- `di/`: bindings de injeção de dependência com Hilt.
- `ui/components/`: componentes gerais compartilhados pelo app.
- `ui/tracker/`: tela, componentes, estado e `ViewModel` do tracker.
- `ui/theme/`: cores e tema do Jetpack Compose.
