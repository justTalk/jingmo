package com.hefengbao.jingmo.ui.screen.poem

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hefengbao.jingmo.data.database.entity.WritingEntity
import com.hefengbao.jingmo.data.database.model.PoemSimpleInfo
import com.hefengbao.jingmo.data.repository.PoemRepository
import com.hefengbao.jingmo.data.repository.PreferenceRepository
import com.hefengbao.jingmo.data.repository.WritingRepository
import com.hefengbao.jingmo.ui.screen.poem.nav.PoemSearchListArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PoemListViewModel @Inject constructor(
    private val repository: PoemRepository,
    private val preferenceRepository: PreferenceRepository,
    private val writingRepository: WritingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = PoemSearchListArgs(savedStateHandle)
    val type = args.type
    val query = args.query

    private val queryStateFlow = MutableStateFlow("")
    fun search(query: String) {
        queryStateFlow.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val writings: Flow<PagingData<WritingEntity>> = if (type == "author") {
        writingRepository.searchByAuthor(query).cachedIn(viewModelScope)
    } else {
        queryStateFlow.debounce(200)
            .distinctUntilChanged()
            .filter {
                return@filter it.isNotEmpty()
            }.flatMapLatest { query ->
                writingRepository.search(query)
            }.cachedIn(viewModelScope)
    }
}