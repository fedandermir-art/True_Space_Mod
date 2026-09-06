# Реестр контента и датаген: как всё связано

В проекте **три** слоя описания контента, и они должны не расходиться:

```
content/registry/*.json        ← «что и почему»  (дизайн, реализм, баланс)  [руками]
        │  правила
        ▼
src/main/java/.../init/*.java  ← «как зарегистрировано» (код)              [кодом]
        │  генерация
        ▼
src/generated/resources/**      ← «что видит игра» (рецепты, ланг, модели) [датагеном]
```

## 1. Реестр (`content/registry/`)

Дизайнерская база. Содержит **полное** описание каждого элемента:
названия (en/ru), реальную основу (`realWorld`), проектный рецепт (`recipe`),
зависимости и статус. Формат — [`registry.schema.json`](../../content/schema/registry.schema.json).

Сюда пишем **до** кода. Это то самое «отдельное JSON, чтобы ничего не забыть».

## 2. Код (`init/`)

Java-реестры (DeferredRegister) объявляют, что реально существует в игре:
`ModItems`, `ModBlocks`, `ModFluids`, `ModCreativeTabs`...
Имя константы и id элемента должны совпадать с `id` в реестре (иначе — рассинхрон).

## 3. Генерация (`datagen/` + `src/generated/`)

`./gradlew runData` генерирует игровые файлы:

| Провайдер | Что генерирует |
|-----------|----------------|
| `ModLanguageProvider` | `assets/truespace/lang/en_us.json`, `ru_ru.json` |
| `ModRecipeProvider` | `data/truespace/recipe/*.json` |
| (фаза 2+) теги/лут/модели | `data/.../tags`, `loot_tables`, `models`, `blockstates` |

Сгенерированные файлы **коммитятся** (конвенция MDK, см. `.gitattributes`).

## Правило согласованности

Рецепт из `content/registry/*.json` (`recipe`) и рецепт из `data/truespace/recipe/*.json`
должны совпадать. Если процесс реальный (электролиз, Байер...) — в игре это **машина**,
а не верстак; верстак допустим только для реальных сборочных операций.

## Типовой сценарий: добавляем материал

1. В `content/registry/materials.json` — запись `"titanium"` со `status: planned`, `realWorld` и `recipe`.
2. В `init/ModItems.java` — `public static final DeferredItem<Item> TITANIUM = ITEMS.registerSimpleItem("titanium");`.
3. В `ModLanguageProvider` — переводы `item.truespace.titanium`.
4. В `ModRecipeProvider` — рецепт процесса (машина Кролла; позже — через рецептный тип машины).
5. `./gradlew runData`, проверяем сгенерированное, ставим `status: implemented`.

## TODO-инструменты (позже)

- `tools/registry_stats.py` — пересчёт `_index.json` и проверка, что каждый игровой элемент
  имеет запись в реестре (и наоборот).
- Валидация реестра по `registry.schema.json` в CI (`.github/workflows/`).
