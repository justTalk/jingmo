package com.hefengbao.jingmo.ui.screen.poem

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class PoemIndexViewModel @Inject constructor() : ViewModel() {
    private val _recommendList: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val recommendList: SharedFlow<List<String>> = _recommendList

    init {
        _recommendList.value = listOf(
            "诗经",
            "屈原",
            "曹操",
            "曹丕",
            "曹植",
            "陆机",
            "陆云",
            "谢灵运",
            "陶潜",
            "卢照邻",
            "骆宾王",
            "王勃",
            "杨炯",
            "贺知章",
            "陈子昂",
            "张若虚",
            "张九龄",
            "王之涣",
            "孟浩然",
            "王昌龄",
            "高适",
            "李白",
            "王维",
            "崔颢",
            "杜甫",
            "岑参",
            "张继",
            "韦应物",
            "孟郊",
            "韩愈",
            "刘禹锡",
            "白居易",
            "柳宗元",
            "李贺",
            "温庭筠",
            "杜牧",
            "韦庄",
            "李煜",
            "林逋",
            "范仲淹",
            "欧阳修",
            "晏殊",
            "曾巩",
            "王安石",
            "晏几道",
            "苏轼",
            "黄庭坚",
            "秦观",
            "周邦彦",
            "李清照",
            "陆游",
            "范成大",
            "杨万里",
            "朱熹",
            "辛弃疾",
            "姜夔",
            "文天祥",
            "元好问",
            "关汉卿",
            "马致远",
            "纳兰性德",
            "仓央嘉措",
            "王国维",
            "李叔同",
            "毛泽东",
            "柳亚子"
        )
    }
}