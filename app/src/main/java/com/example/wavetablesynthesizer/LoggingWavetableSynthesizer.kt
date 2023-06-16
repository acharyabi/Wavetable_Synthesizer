package com.example.wavetablesynthesizer

import android.content.ContentValues.TAG
import android.util.Log

class LoggingWavetableSynthesizer:WavetableSynthesizer {
    private var isPlaying= false
    private val TAG="LoggingWavetable"
    override suspend fun play() {
        Log.d(TAG,"play() called.")
        isPlaying=true
    }

    override suspend fun stop() {
        Log.d(TAG,"stop() called.")
        isPlaying=false
    }

    override suspend fun isPlaying(): Boolean {
        Log.d(TAG,"isPlaying() called.")
        return isPlaying
    }

    override suspend fun setFrequency(frequenctInHz: Float) {
        Log.d(TAG,"setFrequency() called with $frequenctInHz.")
    }

    override suspend fun setVolume(volumeInDb: Float) {
        Log.d(TAG,"setVolume() called with $volumeInDb.")
    }

    override suspend fun setWavetable(wavetable: Wavetable) {
        Log.d(TAG,"setWavetable() called with $wavetable.")
    }
}