package com.example.marvel_app.presentation.character_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.models.CharacterEntry
import com.example.marvel_app.data.models.ComicsEntry
import com.example.marvel_app.data.models.HeroesListEntry
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CharacterScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository
): ViewModel() {

    var comicsList = mutableStateOf<List<ComicsEntry>>(listOf())

    private var _character = MutableStateFlow<CharacterEntry?>(null) // Changed to MutableStateFlow<CharacterEntry?>
    var character: StateFlow<CharacterEntry?> = _character

    private var _isLoading = MutableStateFlow(false)
    var isLoading: StateFlow<Boolean> = _isLoading

    private var _loadError = MutableStateFlow<String?>(null)
    var loadError: StateFlow<String?> = _loadError

    fun loadHeroInfo(heroId: Int?) {
        viewModelScope.launch {
            _isLoading.value = true

            val characterRequest = async { heroRepository.getHeroInfo(heroId) }
            val comicsRequest = async { heroRepository.getHeroComics(heroId, 5) }

            // Ожидаем результатов запросов
            val characterInfo = characterRequest.await()
            val comicsInfo = comicsRequest.await()


            when (characterInfo) {
                is Resource.Success -> {
                    val heroInfo = characterInfo.data!!.data.results[0] // Get the first result
                    val characterEntry:CharacterEntry
                    if(heroInfo.name.length<25){
                        characterEntry = CharacterEntry(
                            heroInfo.name.capitalize(Locale.ROOT),
                            heroInfo.thumbnail.path + "." + heroInfo.thumbnail.extension,
                            heroInfo.description
                        )
                    }
                    else{
                        characterEntry = CharacterEntry(
                            heroInfo.name.capitalize(Locale.ROOT).take(25)+"...",
                            heroInfo.thumbnail.path + "." + heroInfo.thumbnail.extension,
                            heroInfo.description
                        )
                    }
                    _character.value = characterEntry
                    when (comicsInfo) {
                        is Resource.Success -> {
                            val heroComicsInfo = comicsInfo.data!!.data.results.mapIndexed { index, entry ->
                                if (entry.description==""){
                                    ComicsEntry(
                                        entry.title,
                                        (entry.textObjects.mapIndexed { _, textObject ->
                                            textObject.text
                                        }.toString().substring(1)),
                                        entry.thumbnail.path + "." + entry.thumbnail.extension
                                    )
                                }
                               else{
                                    ComicsEntry(
                                        entry.title,
                                        entry.description,
                                        entry.thumbnail.path + "." + entry.thumbnail.extension
                                    )
                               }
                            }
                            _loadError.value = null
                            _isLoading.value = false
                            comicsList.value = heroComicsInfo
                        }
                        is Resource.Error -> {
                            _loadError.value = comicsInfo.message
                            _isLoading.value = false
                        }
                    }
                }
                is Resource.Error -> {
                    _loadError.value = characterInfo.message
                    _isLoading.value = false
                }
            }
        }
    }
}