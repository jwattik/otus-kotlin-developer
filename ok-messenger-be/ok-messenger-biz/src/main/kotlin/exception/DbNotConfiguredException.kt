package ru.otus.messenger.biz.exception

import ru.otus.messenger.common.models.WorkMode

class DbNotConfiguredException(val workMode: WorkMode): Exception(
    "Database is not configured properly for work mode $workMode"
)