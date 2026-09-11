# True Space Mod

**Глобальный реалистичный хардкор-мод про космос для Minecraft.**

> Один большой standalone-мод (не модпак). Каждая система опирается на реальную физику,
> химию и инженерию: настоящие ракетные двигатели и Δv, жизнеобеспечение, радиация,
> реальные производственные цепочки (Байер, Холла-Эру, Кролл).

---

## Статус

- **Фаза 0 (каркас)** ✅ — проект собирается, датаген работает, заведён реестр контента и документация.
- Следующий шаг — **Фаза 1: ресурсы и металлургия** (см. [`docs/design/roadmap.md`](docs/design/roadmap.md)).

## Технологии

| Что | Версия |
|-----|--------|
| Minecraft | 1.21.11 (Mounts of Mayhem) |
| NeoForge | 21.11.45 |
| ModDevGradle | 2.0.146 |
| Java | 21 |
| Gradle | 9.2.1 |

## Сборка и запуск

```bash
# собрать jar (результат: build/libs/truespace-<version>.jar)
./gradlew build

# запустить клиент в dev-среде
./gradlew runClient

# сгенерировать ресурсы (рецепты, локализация) в src/generated/resources
./gradlew runData
```

В IntelliJ IDEA: открыть папку проекта → импортировать Gradle-проект (всё подтянется само).

> При первом запуске Gradle скачивает Minecraft и NeoForge — это может занять до часа (однократно).

### Автоматическая сборка (CI)

Готовый GitHub Actions workflow лежит в [`ci/build.yml`](ci/build.yml) — он компилирует мод
на серверах GitHub и отдаёт готовый jar как артефакт. Как его активировать — см. [`ci/README.md`](ci/README.md).

## Структура репозитория

```
True_Space_Mod/
├── src/main/java/com/truespace/
│   ├── TrueSpaceMod.java          # главный класс мода
│   ├── Config.java                # конфиг (тумблеры систем)
│   ├── init/                      # реестры (предметы, блоки, табы...)
│   ├── client/                    # клиентский код
│   └── datagen/                   # генераторы ресурсов
├── src/main/resources/            # текстуры, модели (руками)
├── src/generated/resources/       # сгенерированное датагеном (коммитится)
├── content/                       # ← РЕЕСТР КОНТЕНТА (JSON, «чтобы ничего не забыть»)
│   ├── schema/registry.schema.json
│   └── registry/                  # материалы, жидкости, машины...
├── docs/                          # ← документация (дизайн, физика, источники, лор)
│   ├── design/  ├── reference/  ├── content/  └── lore/
└── gradle.properties              # версии и свойства мода
```

## Куда смотреть в первую очередь

- **Реестр контента** — [`content/README.md`](content/README.md) (все предметы/машины с реальной основой).
- **Столпы дизайна** — [`docs/design/pillars.md`](docs/design/pillars.md).
- **Физический справочник** — [`docs/reference/physics.md`](docs/reference/physics.md).
- **Дорожная карта** — [`docs/design/roadmap.md`](docs/design/roadmap.md).
- **Лор** (пишет автор) — [`docs/lore/README.md`](docs/lore/README.md).

## Лицензия

- Код и контент мода: **All Rights Reserved** (по умолчанию, решение о лицензии принимается автором).
- Шаблонные файлы из MDK (NeoForged): MIT — см. [`TEMPLATE_LICENSE.txt`](TEMPLATE_LICENSE.txt).
