package gli.project.tripmate.presentation.receiver

import android.util.Log
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

object Constants {
    const val SUPABASE_URL = "https://chqvccnhwiuvvfhbgwjv.supabase.co"
    const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNocXZjY25od2l1dnZmaGJnd2p2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQ2MTUyNTksImV4cCI6MjA2MDE5MTI1OX0.TmbldSQEqseutzJUKIxgtkeygGhTyYusX-E9WHvWTaI"
    const val CHANNEL_NAME = "n8n-call"
}

fun initializeSupabase(
    coroutineScope: CoroutineScope,
    onDataReceived: (appId: String, channelName: String, token: String, userId: String) -> Unit
): Triple<RealtimeChannel, Job, Job> {
    // Membuat client Supabase dan mengaktifkan Realtime
    val ktorClient = HttpClient(CIO) {
        install(WebSockets)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val supabase = createSupabaseClient(
        supabaseUrl = Constants.SUPABASE_URL,
        supabaseKey = Constants.SUPABASE_KEY
    ) {
        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
        })
        install(Realtime) // Aktifkan Realtime
        httpEngine = ktorClient.engine
    }

    // Membuat channel untuk mendengarkan perubahan
    val channel = supabase.channel(Constants.CHANNEL_NAME)

    // Membuat flow untuk menangkap perubahan data Insert
    val changeFlow = channel.postgresChangeFlow<PostgresAction.Insert>(
        schema = "public",
        filter = {
            table = Constants.CHANNEL_NAME
        }
    )

    // Menangani perubahan data dengan launchIn
    val job = changeFlow.onEach { change ->
        Log.d("Supabase", "New INSERT: ${change.record}")

        // callback untuk memproses data yang diterima
        // Memanggil lambda untuk memproses data yang diterima
        val record = change.record
        val userId = record["user_id"]?.toString() ?: ""
        val type = record["type"]?.toString() ?: ""

        val payload = record["payload"] as? Map<*, *>
        if (payload != null) {
            val appId = payload["appId"]?.toString() ?: ""
            val channelName = payload["channelName"]?.toString() ?: ""
            val token = payload["token"]?.toString() ?: ""

            onDataReceived(appId, channelName, token, userId)
        }
    }.launchIn(coroutineScope)

    // Subscribe channel
    val subscribeJob = coroutineScope.launch {
        try {
            channel.subscribe()
            Log.d("Supabase", "Channel subscribed successfully")
        } catch (e: Exception) {
            Log.e("Supabase", "Channel subscription error: ${e.message}")
        }
    }

    return Triple(channel, job, subscribeJob)
}