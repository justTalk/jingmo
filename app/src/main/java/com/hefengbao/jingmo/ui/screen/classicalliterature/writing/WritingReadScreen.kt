/*
 * This file is part of the 京墨（jingmo）APP.
 *
 * (c) 贺丰宝（hefengbao） <hefengbao@foxmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.hefengbao.jingmo.ui.screen.classicalliterature.writing

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hefengbao.jingmo.data.database.entity.classicalliterature.WritingCollectionEntity
import com.hefengbao.jingmo.data.database.entity.classicalliterature.WritingEntity
import com.hefengbao.jingmo.ui.component.SimpleScaffold
import com.hefengbao.jingmo.ui.screen.classicalliterature.writing.components.WritingShowPanel
import kotlinx.serialization.json.Json
import kotlin.math.abs

@Composable
fun WritingReadRoute(
    viewModel: WritingReadViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onCaptureClick: (Int) -> Unit,
) {
    val writing by viewModel.writing.collectAsState()
    val writingCollectionEntity by viewModel.writingCollectionEntity.collectAsState()
    val prevId by viewModel.prevId.collectAsState()
    val nextId by viewModel.nextId.collectAsState()

    WritingReadScreen(
        onBackClick = onBackClick,
        onCaptureClick = onCaptureClick,
        writing = writing,
        writingCollectionEntity = writingCollectionEntity,
        prevId = prevId,
        nextId = nextId,
        setCurrentId = { viewModel.setCurrentId(it) },
        setCollect = { viewModel.setCollect(it) },
        setUncollect = { viewModel.setUncollect(it) },
        setLastReadId = { viewModel.setLastReadId(it) },
        json = viewModel.json
    )
}

@Composable
private fun WritingReadScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onCaptureClick: (Int) -> Unit,
    writing: WritingEntity?,
    writingCollectionEntity: WritingCollectionEntity?,
    prevId: Int?,
    nextId: Int?,
    setCurrentId: (Int) -> Unit,
    setCollect: (Int) -> Unit,
    setUncollect: (Int) -> Unit,
    setLastReadId: (Int) -> Unit,
    json: Json
) {
    writing?.let {
        LaunchedEffect(it) {
            setLastReadId(it.id)
        }
        SimpleScaffold(
            onBackClick = onBackClick,
            title = "诗文",
            actions = {
                IconButton(onClick = { onCaptureClick(it.id) }) {
                    Icon(imageVector = Icons.Default.Photo, contentDescription = null)
                }
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        Row(
                            modifier = modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    setCurrentId(prevId!!)
                                },
                                enabled = prevId != null
                            ) {
                                Icon(
                                    modifier = modifier.padding(8.dp),
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (writingCollectionEntity != null) {
                                        setUncollect(writing.id)
                                    } else {
                                        setCollect(writing.id)
                                    }
                                }
                            ) {
                                if (writingCollectionEntity != null) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkBorder,
                                        contentDescription = null
                                    )
                                }
                            }
                            IconButton(
                                modifier = modifier.padding(8.dp),
                                onClick = {
                                    setCurrentId(nextId!!)
                                },
                                enabled = nextId != null
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                )
            },
            floatingActionButtonPosition = FabPosition.Center
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .draggable(
                        state = rememberDraggableState {},
                        orientation = Orientation.Horizontal,
                        onDragStarted = {},
                        onDragStopped = { velocity ->
                            if (velocity < 0 && abs(velocity) > 500f) {
                                nextId?.let {
                                    setCurrentId(nextId)
                                }
                            } else if (velocity > 0 && abs(velocity) > 500f) {
                                prevId?.let {
                                    setCurrentId(prevId)
                                }
                            }
                        }
                    )
            ) {
                WritingShowPanel(
                    writing = it,
                    json = json
                )
            }
        }
    }
}