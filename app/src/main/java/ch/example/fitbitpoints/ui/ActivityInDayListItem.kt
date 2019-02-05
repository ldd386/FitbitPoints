package ch.example.fitbitpoints.ui

import org.threeten.bp.LocalDate


data class ActivityInDayListItem(val date: LocalDate,
                                 val has150CaloriesInMax30Min: Boolean = false,
                                 val heartRateIsGreaterThan110forAtLeast30Min: Boolean = false,
                                 val stepsInAdayAreMoreThan10k: Boolean = false,
                                 val canGetPoints: Boolean = false)