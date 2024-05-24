package ru.gb.m15_room

import android.app.Application
import androidx.room.Room

class App : Application() {
    lateinit var db: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        db = Room
            .databaseBuilder(
                this,
                AppDatabase::class.java,
                "db"
            ).build()
    }

    companion object {
        lateinit var INSTANCE: App
            private set
    }
}

/*
Добавление `INSTANCE` в код класса `App` часто используется для обеспечения доступа к экземпляру
приложения из других частей кода, например, для доступа к базе данных. Это делается, чтобы избежать
необходимости передачи контекста приложения через аргументы и позволяет получить доступ к `App` и
его свойствам, таким как база данных, из любого места в приложении.

Вот зачем и как это используется:

1. **Глобальный доступ к приложению**: Синглтон `INSTANCE` предоставляет глобальный доступ к
экземпляру класса `App`. Это упрощает доступ к базовым ресурсам приложения, таким как база данных,
без необходимости передавать контекст или ссылку на `App` по всей программе.

2. **Инициализация базы данных**: Инициализация базы данных в классе `App` гарантирует, что база
данных будет инициализирована один раз при запуске приложения, и этот экземпляр будет
использоваться везде в приложении. Это экономит ресурсы и предотвращает создание нескольких
экземпляров базы данных.

Пример использования `INSTANCE`:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wordDao: WordDao = (application as App).db.wordDao()
    }
}
```

В этом примере мы получаем доступ к базе данных через `(application as App).db`. Однако, если `App`
объявлен с `INSTANCE`, мы можем получить доступ к базе данных через `App.INSTANCE.db`, что
может быть более удобно в некоторых случаях.

Можно ли обойтись без `INSTANCE`?

Да, можно обойтись без `INSTANCE`. Вместо этого вы можете явно передавать контекст или экземпляр
`App` в те места, где он необходим. Однако это может усложнить код, так как вам придется
всегда помнить о необходимости передавать контекст или ссылку на `App`.

Пример без использования `INSTANCE`:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = application as App
        val wordDao: WordDao = app.db.wordDao()
    }
}
```

В этом случае вы всегда должны явно приводить `application` к типу `App`, когда вам нужно
получить доступ к базе данных.

Решение использовать или не использовать `INSTANCE` зависит от предпочтений и стиля кодирования.
В некоторых случаях использование `INSTANCE` может упростить код и сделать его более читаемым,
предоставляя единый глобальный доступ к ресурсам приложения.
 */