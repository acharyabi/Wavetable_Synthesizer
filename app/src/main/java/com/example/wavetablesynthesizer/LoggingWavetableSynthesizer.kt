package com.example.wavetablesynthesizer

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Log.*

class LoggingWavetableSynthesizer:WavetableSynthesizer {
    private var isPlaying= false
    private val TAG="LoggingWavetable"
    override suspend fun play() {
        d(TAG,"play() called.")
        isPlaying=true
    }

    override suspend fun stop() {
        d(TAG,"stop() called.")
        isPlaying=false
    }

    override suspend fun isPlaying(): Boolean {
        d(TAG,"isPlaying() called.")
        return isPlaying
    }

    override suspend fun setFrequency(frequenctInHz: Float) {
        d(TAG,"setFrequency() called with $frequenctInHz.")
    }

    override suspend fun setVolume(volumeInDb: Float) {
        d(TAG,"setVolume() called with $volumeInDb.")
    }

    override suspend fun setWavetable(wavetable: Wavetable) {
        d(TAG,"setWavetable() called with $wavetable.")
    }
}