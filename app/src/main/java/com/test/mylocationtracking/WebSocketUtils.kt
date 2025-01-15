package com.test.mylocationtracking

import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class WebSocketUtils {
    companion object {
        fun webSocketConnection(server:String, userEndUpdateListener: (String) -> Unit): WebSocket? {
            val okHttpClient = OkHttpClient.Builder().pingInterval(30, TimeUnit.SECONDS).build()

            val wsRequest =
                Request.Builder().url("wss://$server").build()

            val webSocket = okHttpClient.newWebSocket(wsRequest, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    userEndUpdateListener(text)
                    super.onMessage(webSocket, text)
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                }
            })

            okHttpClient.dispatcher.executorService.shutdown()

            return webSocket
        }
    }
}