package com.trios2025dej.androidapp5.service

import com.trios2025dej.androidapp5.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.w3c.dom.Node
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory

class RssFeedService private constructor() {

    suspend fun getFeed(xmlFileURL: String): RssFeedResponse? {
        return try {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            // Always add logging interceptor (no BuildConfig dependency)
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

            // Retrofit requires a baseUrl like "https://domain.com/path/"
            val baseUrl = xmlFileURL
                .substringBefore("?")
                .substringBeforeLast("/") + "/"

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .build()

            val service = retrofit.create(FeedService::class.java)

            val response = service.getFeed(xmlFileURL)
            if (!response.isSuccessful) {
                println("Server error: ${response.code()}, ${response.errorBody()?.string()}")
                return null
            }

            val xmlString = response.body()?.string()
            if (xmlString.isNullOrBlank()) {
                println("Empty RSS feed")
                return null
            }

            // Parse XML on IO thread
            withContext(Dispatchers.IO) {
                val dbFactory = DocumentBuilderFactory.newInstance()
                val dBuilder = dbFactory.newDocumentBuilder()
                val doc = dBuilder.parse(xmlString.byteInputStream())

                val rssFeedResponse = RssFeedResponse(episodes = mutableListOf())
                domToRssFeedResponse(doc, rssFeedResponse)
                rssFeedResponse
            }

        } catch (t: Throwable) {
            println("Error parsing RSS feed: ${t.localizedMessage}")
            null
        }
    }

    companion object {
        val instance: RssFeedService by lazy { RssFeedService() }
    }

    private fun domToRssFeedResponse(node: Node, rssFeedResponse: RssFeedResponse) {
        if (node.nodeType == Node.ELEMENT_NODE) {
            val nodeName = node.nodeName
            val parentName = node.parentNode?.nodeName ?: ""
            val grandParentName = node.parentNode?.parentNode?.nodeName ?: ""

            // When we hit <item> under <channel>, add a new episode object
            if (nodeName == "item" && parentName == "channel") {
                rssFeedResponse.episodes?.add(RssFeedResponse.EpisodeResponse())
            }

            // Fill episode fields
            if (parentName == "item" && grandParentName == "channel") {
                val currentItem = rssFeedResponse.episodes?.lastOrNull()
                if (currentItem != null) {
                    when (nodeName) {
                        "title" -> currentItem.title = node.textContent
                        "description" -> currentItem.description = node.textContent
                        "itunes:duration" -> currentItem.duration = node.textContent
                        "guid" -> currentItem.guid = node.textContent
                        "pubDate" -> currentItem.pubDate = node.textContent
                        "link" -> currentItem.link = node.textContent
                        "enclosure" -> {
                            currentItem.url = node.attributes?.getNamedItem("url")?.textContent
                            currentItem.type = node.attributes?.getNamedItem("type")?.textContent
                        }
                    }
                }
            }

            // Fill channel fields
            if (parentName == "channel") {
                when (nodeName) {
                    "title" -> rssFeedResponse.title = node.textContent
                    "description" -> rssFeedResponse.description = node.textContent
                    "itunes:summary" -> rssFeedResponse.summary = node.textContent
                    "pubDate" -> rssFeedResponse.lastUpdated =
                        DateUtils.xmlDateToDate(node.textContent)
                }
            }
        }

        // Traverse children
        val nodeList = node.childNodes
        for (i in 0 until nodeList.length) {
            domToRssFeedResponse(nodeList.item(i), rssFeedResponse)
        }
    }
}

interface FeedService {
    @Headers(
        "Content-Type: application/xml; charset=utf-8",
        "Accept: application/xml"
    )
    @GET
    suspend fun getFeed(@Url xmlFileURL: String): Response<ResponseBody>
}
