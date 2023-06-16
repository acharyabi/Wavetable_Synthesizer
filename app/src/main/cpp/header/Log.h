//
// Created by Abinash Acharya on 16/06/2023.
//

#pragma once
#include<android/log.h>

#ifndef NDEBUG
#define LOGD(args...) \
__android_log_print(android_LogPriority::ANDROID_LOG_DEBUG, "WavetableSynthesizer", args)
#else

#endif