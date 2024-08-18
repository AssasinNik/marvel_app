package com.example.marvel_app.presentation.search_screen

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.marvel_app.R
import com.example.marvel_app.data.models.SearchResultEntry
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.ui.theme.SearchColor
import com.example.marvel_app.ui.theme.SearchTextColor
import com.example.marvel_app.ui.theme.WhiteColor
import kotlinx.coroutines.Dispatchers

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchScreenViewModel = hiltViewModel()
){

    val isLoading by remember {
        viewModel.isLoading
    }
    val resultList by remember {
        viewModel.resultList
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = "Search",
            style = TextStyle(
                fontFamily = Poppins,
                color = Color.White,
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 15.dp, bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        SearchBarGlobal(
            hint = "Search",
            modifier = Modifier.padding(8.dp)
        ) { query ->
            viewModel.loadInfo(query)
        }
        if(isLoading){
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator(
                color = SearchBorderColor,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        else{
            if(resultList.isEmpty()){
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Sorry, no information was found",
                    style = TextStyle(
                        color = Color(0xFFB6B6B6),
                        fontSize = 20.sp
                    )
                )
            }
            else{
                ResultSection(navController, viewModel)
            }
        }

    }
}

@Composable
fun ResultSection(
    navController: NavController,
    viewModel: SearchScreenViewModel
){
    val resultList by remember {
        viewModel.resultList
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(resultList) { index, result ->
            ResultEntry(result = result)

            if (index == resultList.size - 1) {
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }
}

@Composable
fun ResultEntry(
    result: SearchResultEntry
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp)
    ){
        val placeholder = R.drawable.gradient
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(result.imageUrl)
                .dispatcher(Dispatchers.IO)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .memoryCacheKey(result.imageUrl)
                .diskCacheKey(result.imageUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = result.name,
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None,
            modifier = Modifier
                .height(170.dp)
                .width(110.dp)
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(10.dp)) // Применение закругленных углов
                .border(
                    width = 2.dp,
                    color = RedColor,
                    shape = RoundedCornerShape(10.dp) // Соответствие закругленной форме
                )
                .align(Alignment.CenterVertically),
            loading = {
                CircularProgressIndicator(
                    color = SearchBorderColor
                )
            }
        )
        Column (modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp)){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(RedColor)

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = result.name.toString(),
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 17.sp
                ),
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
            result.description?.let {
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
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
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
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBarGlobal(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }

    Box(modifier = modifier) {
        TextField(
            modifier = Modifier
                .border(2.dp, color = SearchBorderColor, shape = RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp)),
            value = text,
            placeholder = {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = hint,
                    style = TextStyle(color = SearchTextColor, fontSize = 18.sp)
                )
            },
            onValueChange = {
                text = it
                onSearch(it)  // Вызов функции поиска с новым текстом
            },
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = SearchColor,
                textColor = SearchTextColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = SearchTextColor
            ),
            textStyle = TextStyle(color = WhiteColor, fontSize = 18.sp),
        )
    }
}