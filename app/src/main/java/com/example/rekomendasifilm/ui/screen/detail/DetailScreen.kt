package com.example.rekomendasifilm.ui.screen.detail

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rekomendasifilm.ui.common.UiState
import com.example.rekomendasifilm.di.Injection
import com.example.rekomendasifilm.R
import com.example.rekomendasifilm.ViewModelFactory

@Composable
fun DetailScreen(
    movieId: Int,
    navigateBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getMovieById(movieId)
            }
            is UiState.Success -> {
                val data = uiState.data
                DetailInformation(
                    id = data.id,
                    title = data.title,
                    synopsis = data.synopsis,
                    image = data.image,
                    sutradara = data.sutradara,
                    tanggalRilis = data.tanggalRilis,
                    genre = data.genre,
                    isFavorite = data.isFavorite,
                    onFavoriteButtonClicked = { id, state ->
                        viewModel.updateMovie(id, state)
                    }
                )
            }
            is UiState.Error -> {
                // Handle error state if needed
            }
        }
    }
}

@Composable
fun DetailInformation(
    id: Int,
    title: String,
    synopsis: String,
    @DrawableRes image: Int,
    sutradara: String,
    tanggalRilis: String,
    genre: String,
    isFavorite: Boolean,
    onFavoriteButtonClicked: (id: Int, state: Boolean) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)  // Added padding for consistent margins
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Movie poster and title section
                Row(modifier = Modifier.weight(1f)) {
                    Image(
                        painter = painterResource(image),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(150.dp)
                            .height(250.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Director: $sutradara",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Release Date: $tanggalRilis",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Genre: $genre",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Favorite and Share buttons aligned to the right
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    IconButton(
                        onClick = { onFavoriteButtonClicked(id, isFavorite) }
                    ) {
                        Icon(
                            imageVector = if (!isFavorite) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                            contentDescription = if (!isFavorite) stringResource(R.string.add_favorite) else stringResource(R.string.delete_favorite),
                            tint = if (!isFavorite) Color.Gray else Color.Red
                        )
                    }
                    IconButton(
                        onClick = {
                            sendMovieDetail(
                                context = context,
                                title = title,
                                synopsis = synopsis,
                                genre = genre
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share),
                            tint = Color.Gray
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Synopsis section
            Text(
                text = "Synopsis",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = synopsis,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Justify
            )
        }
    }
}

fun sendMovieDetail(context: Context, title: String, synopsis: String, genre: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, "Movie Title: $title\nGenre: $genre\nSynopsis: $synopsis")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(intent, "Share movie details via"))
}
