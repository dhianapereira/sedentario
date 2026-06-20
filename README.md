# sedentário

Um app para sedentários.

## Estrutura do projeto

O projeto utiliza MVVM e organiza a interface por funcionalidade:

- `model/`: modelos e tipos de negócio.
- `ui/tracker/`: tela, componentes, estado e `ViewModel` do tracker.
- `ui/theme/`: cores e tema do Jetpack Compose.

Camadas como `data/`, repositórios e persistência serão adicionadas quando o app passar a armazenar os registros localmente.
