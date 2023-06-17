//
// Created by Abinash Acharya on 17/06/2023.
//

#include "OboeAudioPlayer.h"
#include "AudioSource.h"

using namespace oboe;

namespace wavetablesynthesizer{
    OboeAudioPlayer::OboeAudioPlayer(std::shared_ptr <AudioSource> source, int samplingRate)
        : _source{std::move(source)},_samplingRate{samplingRate}{}

    OboeAudioPlayer::~OboeAudioPlayer(){
        OboeAudiPlayer::stop();
    }

    int32_t OboeAudioPlayer::play() {
        AudioStreamBuilder builder;
        const auto result =
                builder.setPerformanceMode(PerformanceMode::LowLatency)
                        ->setDirection(Direction::Output)
                        ->setSampleRate(_sampleRate)
                        ->setDataCallback(this)
                        ->setSharingMode(SharingMode::Exclusive)
                        ->setFormat(AudioFormat::Float)
                        ->setChannel(channelCount)
                        ->setSampleRateConversionQuality(SampleRateConversionQuality::Best)
                        ->openStreamm(_stream);

        if (result != Result:OK){
            return static_cast<int32_t>(result);
        }
        const auto playResult = _stream->requestStart();
        return static_cast<int32_t>(playResult);

    }
    void OboeAudioPlayer::stop() override{
        if (_stream){
            _stream->stop();
            _stream->close();
            _stream.reset();
        }
        _source->onPlaybackStopped()
    }

    oboe::DataCallbackResult OboeAudioPlayer::onAudioReady(oboe::AudioStream *audioStream,
                                          void *audioData,
                                          int32_t framesCount){
        auto* floatData = reinterpret_cast<float*>(audioData);

        for (auto frame=0; frame <frameCount; ++frame){
            const auto sample = _source -> getSample();

            for (auto channel=0; channel< channelCount; ++channel){
                floatData[frame* channelCount +channel]= sample;
            }
        }
        return oboe::DataCallbackResult::Continue;
    }

}