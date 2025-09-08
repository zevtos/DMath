# Курсовая по вебу 3 курс 5 семестр

Проект: Учебный калькулятор и визуализатор алгоритмов дискретной математики и сетей

### Кратко:

* Веб-приложение для пошаговой визуализации алгоритмов (графы, кратчайшие пути, транспортные задачи, базовая
  NN-propagation)
* Бэкенд решает алгоритмы(уже имеются наброски кода) и формирует протокол шагов (`steps[]`) и итог; фронт
  воспроизводит/объясняет шаги, анимирует и даёт перемотку

### Что планируется

* **Графы**: BFS, DFS, Дейкстра, минимальный остов ...
* **Транспортные задачи**: метод минимальной стоимости ...
* **Нейросети**: визуализация распространения сигнала в маленькой MLP (без обучения) ...

### Набросок архитектуры

* **frontend/** — Next.js + React + Tailwind + Cytoscape.js (Canvas/WebGL; редактор графов, анимации, степпер) + Redux
  Toolkit; реконструкция/валидация `steps[]` и симуляция на клиенте в Web Worker.
* **backend/** — Node.js gateway (auth/proxy) + Java-модуль с реализацией алгоритмов; API отдаёт итог и `steps[]`.

### Технический минимум

* Сборка: Next.js Turbopack
* Стейт-менеджер: Redux Toolkit
* Компонентная библиотека: MUI
* Web API: **Canvas/WebGL**; плюс **Web Worker** для возможных вычислений на клиенте
* Тесты: Jest + React Testing Library; e2e: Playwright; coverage ≥ 80% (цель для CI)

### Технические решения

Детальные технические решения и архитектурные решения вынесены
в [ADR (Architecture Decision Records)](./docs/adr/README.md).

### Backend Developer Quickstart

- **Требования**
    - Java 21 (Oracle), Docker Desktop (для Compose), Git.

- **Запуск**
    - Запустить PostgreSQL: `docker compose up -d postgres`
    - Запустить приложение: `.\gradlew.bat bootRun` (Windows) или `./gradlew bootRun` (Unix)

- **Тесты**
    - `.\gradlew.bat test` (Windows) или `./gradlew test` (Unix)

- **Полезные эндпоинты**
    - Health check: `http://localhost:8080/actuator/health`
    - API docs: `http://localhost:8080/swagger-ui.html`