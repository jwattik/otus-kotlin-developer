# API

---

## Сущность **Chat** (Чат)
**Поля:**
1. **id** (string) — уникальный идентификатор чата (UUID).
2. **title** (string) — название чата (для групповых чатов).
3. **type** (enum: `private`, `group`, `channel`) — тип чата.
4. **mode** (enum: `work`, `personal`) — режим чата (рабочий/личный).
5. **participants** (array of user IDs) — список участников.
6. **created_at** (timestamp) — дата создания.
7. **is_archived** (boolean) — флаг архивации.
8. **metadata** (JSON) — дополнительные данные (например, `{"project_id": "abc123", "jira_link": "..."}`).

---

### Эндпоинты для **Chat**

| Метод | Путь                     | Описание                                  | Параметры                                                                 |
|-------|--------------------------|-------------------------------------------|---------------------------------------------------------------------------|
| POST   | `/api/v1/chats`          | Создать чат                              | `{ title, type, mode, participants }`                                    |
| GET    | `/api/v1/chats/{id}`     | Получить чат по ID                       | —                                                                         |
| PATCH  | `/api/v1/chats/{id}`     | Обновить чат (название, режим, архив)    | `{ title?, mode?, is_archived? }`                                        |
| DELETE | `/api/v1/chats/{id}`     | Архивировать чат                         | —                                                                         |
| GET    | `/api/v1/chats`          | Поиск чатов                              | `?type=group&mode=work&participant=user123&search=project`               |
| POST   | `/api/v1/chats/{id}/join`| Добавить участника                       | `{ user_id }`                                                            |
| POST   | `/api/v1/chats/{id}/leave`| Удалить участника                       | `{ user_id }`                                                            |

---

## Сущность **Message** (Сообщение)
**Поля:**
1. **id** (string) — уникальный идентификатор сообщения (UUID).
2. **chat_id** (string) — ID чата.
3. **sender_id** (string) — ID отправителя.
4. **text** (string) — текст сообщения (может быть `null` для вложений).
5. **attachments** (array) — вложения:  
   `{ type: "file" | "link" | "meeting", url: string, preview?: string }`.
6. **timestamp** (timestamp) — время отправки.
7. **status** (enum: `sent`, `delivered`, `read`) — статус сообщения.
8. **metadata** (JSON) — дополнительные данные (хештеги, упоминания).

---

### Эндпоинты для **Message**

| Метод | Путь                                | Описание                                  | Параметры                                                                 |
|-------|-------------------------------------|-------------------------------------------|---------------------------------------------------------------------------|
| POST   | `/api/v1/chats/{chat_id}/messages`  | Отправить сообщение                       | `{ text?, attachments[], metadata? }`                                    |
| GET    | `/api/v1/messages/{id}`            | Получить сообщение по ID                 | —                                                                         |
| DELETE | `/api/v1/messages/{id}`            | Удалить сообщение (для отправителя)      | —                                                                         |
| GET    | `/api/v1/chats/{chat_id}/messages`  | Получить историю сообщений               | `?limit=50&offset=0&search=bug&from=2023-01-01&to=2023-12-31`            |
| PATCH  | `/api/v1/messages/{id}/status`     | Обновить статус (например, `read`)       | `{ status }`                                                             |

---

### Примеры запросов

**Создание чата:**
```http
POST /api/v1/chats
Body:
{
  "type": "group",
  "mode": "work",
  "title": "Project Alpha",
  "participants": ["user1", "user2"]
}
```

**Поиск сообщений:**
```http
GET /api/v1/chats/chat123/messages?search=deadline&from=2023-10-01
```

**Отправка сообщения с вложением:**
```http
POST /api/v1/chats/chat123/messages
Body:
{
  "text": "Проверьте новый дизайн",
  "attachments": [
    { "type": "file", "url": "https://.../design.pdf", "preview": "https://.../preview.png" }
  ]
}
```

---

### Особенности:
1. **Авторизация:** Все запросы требуют токена (JWT в заголовке `Authorization`).
2. **Ошибки:**
    - `403 Forbidden` — пользователь не является участником чата.
    - `404 Not Found` — чат/сообщение не существует.
    - `400 Bad Request` — неверный формат вложений.
3. **Пагинация:** Для списков используется `limit` и `offset` (по умолчанию `limit=20`).

---
