//
// Created by Abinash Acharya on 17/06/2023.
//

//
// Created by Abinash Acharya on 16/06/2023.
//

#pragma once
//Contains Definition Of The Interface
namespace wavetablesynthesizer{
    class AudioSource{
    public:
        //Virtual destructor is important to free up all the space taken by the corresponding classes.
        virtual ~AudioSource()=default;
        virtual float getSample()=0;
        virtual void onPlaybackStopped()=0;
    };
}