# CI (непрерывная сборка)

Здесь лежит готовый GitHub Actions workflow [`build.yml`](build.yml), который **реально
компилирует мод** против Minecraft 1.21.11 + NeoForge на серверах GitHub и выкладывает
готовый `.jar` как артефакт.

## Почему он в `ci/`, а не в `.github/workflows/`

GitHub Actions исполняет только workflow-файлы из `.github/workflows/`.
Но у GitHub-приложения, от имени которого работает Arena-агент в этой сессии,
**нет разрешения `workflows: write`** — GitHub отклоняет любой push, содержащий
файлы в `.github/workflows/`:

```
! [remote rejected] ... (refusing to allow a GitHub App to create or update
workflow `.github/workflows/build.yml` without `workflows` permission)
```

Поэтому файл хранится в `ci/`, чтобы не блокировать push. Активировать сборку можно
двумя способами:

## Способ 1 — дать приложению право на workflows (тогда я сам всё запушу)

1. GitHub → репозиторий → **Settings → GitHub Apps** (или: профиль → Settings →
   Applications → **Installed GitHub Apps** → Arena).
2. Найти приложение Arena, открыть **Configure**.
3. В разделе **Repository permissions** выставить **Workflows → Read and write**.
4. Сохранить. Написать мне — и я сразу запушу `.github/workflows/build.yml`.

## Способ 2 — добавить файл вручную (30 секунд)

1. В репозитории на GitHub: **Add file → Create new file**.
2. Имя файла: `.github/workflows/build.yml`.
3. Вставить содержимое из [`ci/build.yml`](build.yml).
4. **Commit changes** → сборка запустится автоматически на следующем push/PR.

## Что даёт сборка

- Компилирует Java-код мода и **доказывает**, что каркас корректен.
- Генерирует ресурсы датагеном (при желании — добавь `./gradlew runData`).
- Выкладывает `truespace-0.1.0.jar` как артефакт (вкладка Actions → артефакты).
