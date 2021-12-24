package com.adazhdw.net


suspend inline fun <reified T : Any> Net.get(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return get().urlPath(urlPath).apply { block.invoke(this) }.toClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.post(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return post().urlPath(urlPath).apply { block.invoke(this) }.toClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.put(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return put().urlPath(urlPath).apply { block.invoke(this) }.toClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.patch(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return patch().urlPath(urlPath).apply { block.invoke(this) }.toClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.delete(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return delete().urlPath(urlPath).apply { block.invoke(this) }.toClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.head(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return head().urlPath(urlPath).apply { block.invoke(this) }.toClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.options(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return options().urlPath(urlPath).apply { block.invoke(this) }.toClazz<T>().await()
}

