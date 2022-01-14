package com.adazhdw.net

/**
 * Communicates responses from a server or offline requests. One and only one method will be invoked
 * in response to a given request.
 */
interface Callback<T> {

    /**
     * Invoked for a received HTTP response.
     */
    fun onResponse(call: Call<T>, response: Response<T>)

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected exception
     * occurred creating the request or processing the response.
     */
    fun onFailure(call: Call<T>, t: Throwable)

}