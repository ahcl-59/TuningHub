package com.example.tuninghub.ui.screen.pages.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.MusicianDto
import com.example.tuninghub.data.repository.ConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomePageViewModel: ViewModel(){
    private val _musicians = MutableStateFlow<List<MusicianDto>>(emptyList())
    val musicians = _musicians
    val repository = ConnectionRepository()

    init{
        getMusicians()
    }

    private fun getMusicians() {
        viewModelScope.launch {

        }
    }



}