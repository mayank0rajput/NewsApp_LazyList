package com.example.lazycol

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.lazycol.ui.theme.LazyColTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    private val mySingleton = MySingleton.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setContent {
            Column {
             Data()
            }
        }
    }

    @Composable
    fun Data(){
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                // to get articles
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    var newsJSONObject = newsJsonArray.getJSONObject(i)
                    var news = News(
                        newsJSONObject.getString("title"),
                        newsJSONObject.getString("author"),
                        newsJSONObject.getString("url"),
                        newsJSONObject.getString("urlToImage"),
                    )
                    newsArray.add(news)
                }
                setContent {
                    LazyColumn {
                        items(newsArray) {
                           NewsItem(imageUrl = it.imageUrl, title = it.title, author = it.author)
                        }
                    }
                }
            },
            Response.ErrorListener {
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    @Composable
    fun NewsItem(imageUrl: String, title: String, author: String) {
        Column {
            Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
        )
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                author,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

