package com.redfootdev.gametracker.utils

class TimeUtil {

    companion object {

        val millisInSecond = 1000L
        val ticksInSecond = 20L
        val secondsInMinute = 60L
        val minutesInHour = 60L
        val hoursInDay = 24L

        fun secondsToTicks(seconds: Long): Long {
            return seconds * ticksInSecond
        }

        fun minutesToTicks(minutes: Long): Long {
            return minutes * secondsToTicks(secondsInMinute)
        }

        fun hoursToTicks(hours: Long): Long {
            return hours * minutesToTicks(minutesInHour)
        }

        fun daysToTicks(days: Long): Long {
            return days * hoursToTicks(hoursInDay)
        }

        fun secondsToMillis(seconds: Long): Long {
            return seconds * millisInSecond
        }

        fun minutesToMillis(minutes: Long): Long {
            return minutes * secondsToMillis(secondsInMinute)
        }

        fun hoursToMillis(hours: Long): Long {
            return hours * minutesToMillis(minutesInHour)
        }

        fun daysToMillis(days: Long): Long {
            return days * hoursToMillis(hoursInDay)
        }


    }
}