package com.example.marvel_app.ui.hero_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.example.marvel_app.R
import com.example.marvel_app.data.models.MarvelListEntry
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.GrayColor
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.ui.theme.SearchColor
import com.example.marvel_app.ui.theme.SearchTextColor
import com.example.marvel_app.ui.theme.WhiteColor
import com.google.accompanist.coil.CoilImage

@Composable
fun HeroListScreen(
    navController: NavController
){
    Surface(
        color = BackGround,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.marvel_logo),
                contentDescription = "MarvelLogo",
                modifier = Modifier
                    .height(150.dp)
                    .width(200.dp)
                    .padding(top = 20.dp)
            )
            Text(
                modifier = Modifier,
                text = "Data provided by Marvel. © 2024 MARVEL",
                style = TextStyle(
                    color = Color(0xFFB6B6B6),
                    fontSize = 15.sp
                )
            )
            SearchBar(
                hint = "Search",
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
    ){
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        TextField(
            modifier = Modifier
                .border(2.dp, color = SearchBorderColor, shape = RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp)),
            value = text,
            placeholder = {
                Text(modifier = Modifier
                    .align(Alignment.Center),
                    text = hint,
                    style = TextStyle(
                        color = SearchTextColor,
                        fontSize = 18.sp)
                )
            },
            onValueChange = {
                text = it
                onSearch(it)
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


@Composable
fun MarvelEntry(
    entry: MarvelListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HerolistScreenViewModel = hiltViewModel()
){
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(GrayColor)
            .clickable {
                navController.navigate(
                    "HeroDetailScreen/${entry.number}"
                )
            }
    ){
        CoilImage(
            request = ImageRequest.Builder(LocalContext.current)
                .data(entry.imageUrl)
                .target{

                }
                .build(),
            contentDescription = entry.characterName,
            fadeIn = true,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.Center)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(
                    width = 2.dp,
                    color = RedColor,
                    shape = CircleShape
                )
                .clip(CircleShape)
        ){
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier.scale(0.5f)
            )
        }
        Text(
            text = entry.characterName,
            style = TextStyle(
                fontFamily = Poppins,
                color = Color.White,
                fontSize = 20.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun HeroRow(
    rowIndex: Int,
    list: List<MarvelListEntry>,
    navController: NavController
){
    Column {
        Row {
            MarvelEntry(
                entry = list[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            if(list.size >= rowIndex*2+2){
                MarvelEntry(
                    entry = list[rowIndex * 2+1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }else{
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}