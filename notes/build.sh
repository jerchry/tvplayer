#!/bin/bash

CPU=arm-linux-androideabi
NDK_ROOT=/root/android-ndk-r17c
TOOLCHAIN=$NDK_ROOT/toolchains/$CPU-4.9/prebuilt/linux-x86_64
#从as的 externalNativeBuild/xxx/build.ninja
FLAGS="-isystem $NDK_ROOT/sysroot/usr/include/arm-linux-androideabi -D__ANDROID_API__=21 -g -DANDROID -ffunction-sections -funwind-tables -fstack-protector-strong -no-canonical-prefixes -march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16 -mthumb -Wa,--noexecstack -Wformat -Werror=format-security  -O0 -fPIC"
INCLUDES=" -isystem $NDK_ROOT/sources/android/support/include"
# 生成文件的目录
PREFIX=./android/armeabi-v7a_lsn11
# 指令拼接字符串
# PREFIX=`pwd`/android/armeabi-v7a_lsn11

./configure \
--prefix=$PREFIX \
--enable-small \
--disable-programs \
--disable-avdevice \
--disable-encoders \
--disable-muxers \
--disable-filters \
--enable-cross-compile \
--cross-prefix=$TOOLCHAIN/bin/$CPU- \
--disable-shared \
--enable-static \
--sysroot=$NDK_ROOT/platforms/android-21/arch-arm \
--extra-cflags="$FLAGS $INCLUDES" \
--extra-cflags="-isysroot $NDK_ROOT/sysroot/" \
--arch=arm \
--target-os=android 

# 执行完configure后会生成Makefile
# 清理一下 
make clean
#执行makefile
make install
