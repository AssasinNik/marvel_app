package com.example.marvel_app.presentation.character_screen


import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.marvel_app.R
import com.example.marvel_app.data.models.ComicsEntry
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.util.Routes
import kotlinx.coroutines.Dispatchers

@SuppressLint("SuspiciousIndentation")
@Composable
fun CharacterScreen(
    navController: NavController,
    viewModel: CharacterScreenViewModel = hiltViewModel(),
    heroId: Int?,
    heroName: String?,
    heroImage: String?,
){
    LaunchedEffect(key1 = heroId) {
        viewModel.loadHeroInfo(heroId)
        viewModel.checkFavourite(heroName)
    }

    val comicsList by remember {
        viewModel.comicsList
    }
    val character by viewModel.character.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val loadError by viewModel.loadError.collectAsState()

    val isFavorite by viewModel.isFavorite.collectAsState()

    val icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder



        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
            ){
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(top = 40.dp, start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back To previous",
                        tint = Color.White,
                        modifier = Modifier
                            .size(35.dp),
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        if (isFavorite){
                            viewModel.deleteFavorite(heroName)
                        }
                        else{
                            if (character?.description == null || character?.description == ""){
                                val notEmptyDescription = "There is no description about this superhero from Marvel Comics. We hope that information about the character will be added soon"
                                viewModel.addToFavourites(
                                    heroName,
                                    heroImage,
                                    notEmptyDescription,
                                    heroId
                                )
                            }
                            else{
                                viewModel.addToFavourites(
                                    character?.characterName,
                                    character?.imageUrl,
                                    character?.description,
                                    character?.number
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 40.dp, end = 8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Add to Favourites",
                        tint = Color.White,
                        modifier = Modifier
                            .size(35.dp),
                    )
                }
                if(!isLoading){
                    if (character?.shareLink!=null || character?.shareLink!=""){
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, character?.shareLink)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)

                        val context = LocalContext.current
                        IconButton(
                            onClick = { startActivity(context, shareIntent, null) },
                            modifier = Modifier
                                .padding(top = 40.dp, end = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Character",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(35.dp),
                            )
                        }
                    }
                }
            }
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState(), true)
            ){
                Spacer(modifier = Modifier.height(5.dp))
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(heroImage.toString().replace("%2F", "/"))
                        .build(),
                    contentDescription = character?.characterName,
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.None,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                        .border(
                            width = 2.dp,
                            color = RedColor,
                            shape = CircleShape
                        )
                        .clip(CircleShape) ,
                    loading = {
                        CircularProgressIndicator(
                            color = SearchBorderColor
                        )
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .shadow(5.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .align(Alignment.CenterHorizontally)
                ) {
                    Box(
                        modifier = Modifier
                            .background(RedColor)
                            .height(70.dp)
                            .width(200.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(20.dp))
                    ){
                        Text(
                            text = heroName.toString(),
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                if(!isLoading){
                    Text(
                        text = "Description",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            fontSize = 23.sp
                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    if(character?.description.toString() != ""){
                        Text(
                            text = character?.description.toString(),
                            style = TextStyle(
                                fontFamily = Poppins,
                                color = Color.White,
                                fontWeight = FontWeight.Thin,
                                fontSize = 18.sp
                            ),
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 10.dp)
                        )
                    }
                    else{
                        Text(
                            text = "There is no description about this superhero from Marvel Comics. We hope that information about the character will be added soon",
                            style = TextStyle(
                                fontFamily = Poppins,
                                color = Color.White,
                                fontWeight = FontWeight.Thin,
                                fontSize = 18.sp
                            ),
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    for (element in comicsList){
                        Comics(comics = element){
                            val encodedUrl = element.comicsImage.replace("/", "%2F")
                            navController.navigate("${Routes.COMICS_SCREEN}/${element.number}/${element.comicsName}/${encodedUrl}")
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
                else{
                    Spacer(modifier = Modifier.height(20.dp))
                    CircularProgressIndicator(
                        color = SearchBorderColor,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally))
                }
            }
        }
}


@Composable
fun Comics(
    comics : ComicsEntry,
    onComicClick: () -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
            .clickable {
                onComicClick()
            }
    ){
        val placeholder = R.drawable.gradient
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(comics.comicsImage)
                .dispatcher(Dispatchers.IO)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .memoryCacheKey(comics.comicsImage)
                .diskCacheKey(comics.comicsImage)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = comics.comicsName,
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None,
            modifier = Modifier
                .height(150.dp)
                .width(100.dp)
                .align(Alignment.Top)
            ,
            loading = {
                CircularProgressIndicator(
                    color = SearchBorderColor
                )
            },
        )
        Column {
            Text(
                text = comics.comicsName,
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 17.sp
                ),
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(start = 5.dp)
            )
            comics.comicsDescription?.let {
                if (it.length>100){
                    Text(
                        text = it.take(100)+"...",
                        style = TextStyle(
                            fontFamily = Poppins,
                            color = Color.White,
                            fontWeight = FontWeight.Thin,
                            fontSize = 15.sp
                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
                else{
                    Text(
                        text = it,
                        style = TextStyle(
                            fontFamily = Poppins,
                            color = Color.White,
                            fontWeight = FontWeight.Thin,
                            fontSize = 15.sp
                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
        }
    }
}