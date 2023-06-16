//
// Created by Abinash Acharya on 16/06/2023.
//

#include  "header/Log.h"
#include "header/WavetableSynthesizer.h"

namespace wavetablesynthesizer{
    void WavetableSynthesizer::play(){
        LOGD("play() called");
        _isPlaying= true;

    }
    void WavetableSynthesizer::stop(){
        LOGD("stop() called");
        _isPlaying= false;

    }
    bool WavetableSynthesizer::isPlaying(){
        LOGD("isPlaying() called");
        return _isPlaying;

    }
    void WavetableSynthesizer::setFrequency(float frequencyInHz){
        LOGD("setFrequency() called with %.2f Hz argument.", frequencyInHz);

    }
    void WavetableSynthesizer::setVolume(float volumeInDb){
        LOGD("setFrequency() called with %.2f db argument.", volumeInDb);

    }
    void WavetableSynthesizer::setWavetable(Wavetable wavetable){
        LOGD("setWavetable() called with %.d argument", static_cast<int>(wavetable));
    }
}

