package com.griga.composition.domain.entities

data class Question(
    val sum: Int,
    val visibleNumber: Int,
    val options: List<Int>
)