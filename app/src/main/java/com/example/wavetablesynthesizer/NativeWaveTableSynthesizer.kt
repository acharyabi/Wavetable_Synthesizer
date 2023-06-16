package com.example.wavetablesynthesizer

import androidx.compose.runtime.remember
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NativeWaveTableSynthesizer: WavetableSynthesizer, DefaultLifecycleObserver {
    //Handler to native C+
    private var synthesizerHandle: Long = 0
    private val synthesizerMutex = Object()
    private external fun create(): Long
    private external fun delete(synthesizerHandle: Long)
    private external fun play(synthesizerHandle: Long)
    private external fun stop(synthesizerHandle: Long)
    private external fun isPlaying(synthesizerHandle: Long): Boolean
    private external fun setFrequency(synthesizerHandle: Long, frequencyInHz: Float)
    private external fun setVolume(synthesizerHandle: Long, volumeInDb: Float)
    private external fun setWavetable(synthesizerHandle: Long, wavetable: Int)

    companion object {
        init {
            System.loadLibrary("wavetablesynthesizer")
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        synchronized(synthesizerMutex){
            //Always need to be called inside the synchronized block
            createNativeHandleIfNotExists()
        }
    }
    override fun onPause(owner: LifecycleOwner){
        super.onPause(owner)
        synchronized(synthesizerMutex){
            //It doesn't need to delete if there is no synthesizer in the first place.
            if (synthesizerHandle == 0L){
                return
            }
            delete(synthesizerHandle)
            synthesizerHandle = 0L
        //Tells we need to create the synchronizer if it isn't present
        }
    }

    private fun createNativeHandleIfNotExists(){
        if (synthesizerHandle !=0L){
            return
            }
        synthesizerHandle= create()
        }


    override suspend fun play() = withContext(Dispatchers.Default) {
        //Dispatchers default maintains , runs in different thread than the app's main UI thread.
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            play(synthesizerHandle)
        }

    }
    override suspend fun stop()=withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            stop(synthesizerHandle)
        }
    }

    override suspend fun isPlaying(): Boolean = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            return@withContext isPlaying(synthesizerHandle)
        }
    }

    override suspend fun setFrequency(frequencyInHz: Float)=withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            setFrequency(synthesizerHandle, frequencyInHz)
        }
    }

    override suspend fun setVolume(volumeInDb: Float)=withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            setVolume(synthesizerHandle, volumeInDb)
        }
    }

    override suspend fun setWavetable(wavetable: Wavetable) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            setVolume(synthesizerHandle, wavetable.ordinal.toFloat())
        }
    }
}