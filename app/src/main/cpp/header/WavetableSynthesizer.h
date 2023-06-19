//
// Created by Abinash Acharya on 16/06/2023.
//

#pragma once
#include <memory>

namespace wavetablesynthesizer{
    enum class Wavetable{
        SINE, TRIANGLE, SQUARE, SAW
    };

    class AudioPlayer;
    class AudioSource;

    constexpr auto sampleRate = 48000;

    class WavetableSynthesizer{
    public:
        WavetableSynthesizer();
        ~WavetableSynthesizer();
        void play();
        void stop();
        bool isPlaying() const;
        void setFrequency(float frequencyInHz);
        void setVolume(float volumeInDb);
        void setWavetable(Wavetable wavetable);

    private:
        bool _isPlaying= false;
        std::shared_ptr<AudioSource> _oscillator;
        std::unique_ptr<AudioPlayer>_audioPlayer;
    };
}