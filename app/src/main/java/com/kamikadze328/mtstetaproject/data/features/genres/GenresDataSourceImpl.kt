package com.kamikadze328.mtstetaproject.data.features.genres

import com.kamikadze328.mtstetaproject.data.dto.Genre

class GenresDataSourceImpl : GenresDataSource {

    override fun getGenres(): List<Genre> = listOf(
        Genre(
            name = "Боевик",
            id = 28
        ),
        Genre(
            name = "Приключения",
            id = 12
        ),
        Genre(
            name = "Мультик",
            id = 16
        ),
        Genre(
            name = "Комедия",
            id = 35
        ),
        Genre(
            name = "Криминал",
            id = 80
        ),
        Genre(
            name = "Документальное",
            id = 99
        ),
        Genre(
            name = "Драма",
            id = 18
        ),
        Genre(
            name = "Семейный",
            id = 10751
        ),
        Genre(
            name = "Фантастика",
            id = 14
        ),
        Genre(
            name = "Исторический",
            id = 36
        ),
        Genre(
            name = "Ужасы",
            id = 27
        ),
        Genre(
            name = "Музыка",
            id = 10402
        ),
        Genre(
            name = "Мистика",
            id = 9648
        ),
        Genre(
            name = "Романтика",
            id = 10749
        ),
        Genre(
            name = "Научная фантастика",
            id = 879
        ),
        Genre(
            name = "Триллер",
            id = 53
        ),
        Genre(
            name = "Военный",
            id = 10752
        ),
        Genre(
            name = "Вестерн",
            id = 37
        ),
        Genre(
            name = "Рельное ТВ",
            id = 10770
        ),
    )

    override fun getGenreByIt(id: Int): Genre? = getGenres().find { it.id == id }
}