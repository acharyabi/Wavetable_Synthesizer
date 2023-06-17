//
// Created by Abinash Acharya on 16/06/2023.
//

#include  "header/Log.h"
#include "header/WavetableSynthesizer.h"
#include "OboeAudioPlayer.h"
#include "WavetableOscillator.h"

namespace wavetablesynthesizer{
    WavetableSynthesizer::WavetableSynthesizer()
    : _oscillator{std::make_shared<A4Oscillator>(sampleRate)},
    _audioPlayer{std::make_unique<OboeAudioPlayer>(_oscillator, sampleRate)}{}
    void WavetableSynthesizer::play (){
        LOGD("play() called");
        const auto result = _audioPlayer-> play();
        if(result==0) {
            _isPlaying = true;
        }else{
            LOGD("Could not start playback.");
        }
    }
    void WavetableSynthesizer::stop(){
        LOGD("stop() called");
        _audioPlayer->stop();
        _isPlaying= false;

    }
    bool WavetableSynthesizer::isPlaying() const{
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

