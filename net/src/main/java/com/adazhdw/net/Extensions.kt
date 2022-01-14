package com.adazhdw.net


suspend inline fun <reified T : Any> Net.get(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return get(urlPath).apply { block.invoke(this) }.parseClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.post(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return post(urlPath).apply { block.invoke(this) }.parseClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.put(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return put(urlPath).apply { block.invoke(this) }.parseClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.patch(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return patch(urlPath).apply { block.invoke(this) }.parseClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.delete(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return delete(urlPath).apply { block.invoke(this) }.parseClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.head(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return head(urlPath).apply { block.invoke(this) }.parseClazz<T>().await()
}

suspend inline fun <reified T : Any> Net.options(
    urlPath: String,
    block: RequestFactory.Builder.() -> Unit = {}
): T {
    return options(urlPath).apply { block.invoke(this) }.parseClazz<T>().await()
}

