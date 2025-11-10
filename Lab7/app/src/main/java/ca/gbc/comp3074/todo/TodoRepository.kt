package ca.gbc.comp3074.todo

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.io.BufferedReader
import java.io.InputStreamReader
import org.json.JSONArray

class TodoRepository {

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandle = Handler(Looper.getMainLooper())

    fun fetchTodos(callback: (Result<List<Todo>>) -> Unit) {
        executor.execute {
            try {

                val url = URL("https://jsonplaceholder.typicode.com/todos")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 10000
                    readTimeout = 10000
                }

                conn.inputStream.use { stream ->
                    val text = BufferedReader(InputStreamReader(stream)).readText()
                    val arr = JSONArray(text)
                    val list = buildList {
                        for (i in 0 until arr.length()) {
                            val o = arr.getJSONObject(i)
                            add(
                                Todo(
                                    userId = o.getInt("userId"),
                                    id = o.getInt("id"),
                                    title = o.getString("title"),
                                    completed = o.getBoolean("completed")
                                )
                            )
                        }
                    }
                    mainHandle.post { callback(Result.success(list)) }
                }

            } catch (t: Throwable) {
                Log.e("ERR", t.toString())
                mainHandle.post { callback(Result.failure(t)) }
            }
        }
    }
}
