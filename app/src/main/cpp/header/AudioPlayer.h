//
// Created by Abinash Acharya on 16/06/2023.
//

#pragma once
//Contains Definition Of The Interface
namespace wavetablesynthesizer{
    class AudioPlayer{
    public:
        virtual ~AudioPlayer()=default;
        virtual int32_t play()=0;
        virtual void stop()=0;

    };
}