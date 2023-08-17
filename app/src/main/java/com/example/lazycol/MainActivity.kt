package com.example.lazycol

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
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
                           NewsItem(imageUrl = it.imageUrl, title = it.title, author = it.author,it.url)
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
    fun NewsItem(imageUrl: String, title: String, author: String,url: String) {
        Column {
            val imageModifier = Modifier
                .size(width = 450.dp, height = 250.dp)
                .border(BorderStroke(1.dp, Color.Black))
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    // Open the URI when the image is clicked.
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            Box(
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }
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

