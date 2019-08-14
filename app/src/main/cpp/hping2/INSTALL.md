You can compile hping2 at least under:

Linux
OpenBSD
FreeBSD
NetBSD
Solaris

With Linux you don't need any libs, nor to be root, however you need uid 0 to run hping. FreeBSD, OpenBSD, and NetBSD require libpcap. CMake is required to build for any system.

Linux, FreeBSD, OpenBSD, NetBSD, Solaris
========================================

Please, follow these steps:

`$ mkdir build && cd build`  
`$ cmake .. -G"Unix Makefiles"`  
`$ make`  
`$ su (or calife for BSDs)`  
`# make install`  

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
NOTE: You should take care about your net/bpf.h file installing on
      BSD systems (specially with OpenBSD). If your original bpf.h was
      overwritten with the libpcap one probably hping will not work
      with over some interface.

      For example if you use the libpcap bpf.h on OpenBSD hping will
      not work over PPP interfaces.
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If you need to run hping2 by your normal account (i.e. antirez) try the following commands:

`# chown root:antirez /usr/sbin/hping2`  
`# chmod 4750 /usr/sbin/hping2`  

WARNING: hping2 is not trusted code, i didn't audit for hidden buffers overflow and others security related problems. However if (as default) LIMITWHENSUID is defined if euid != uid it's not possible to use a lot of options trivially unsafe.

suid it at root is not encouraged.

antirez

Android
=======

These steps will definitely change once libcutils code is moved directly into the project (see https://developer.android.com/about/versions/nougat/android-7.0-changes#ndk for why this is necessary). For now:

1. Download the latest NDK (https://developer.android.com/ndk/downloads) and unpack it somewhere, e.g. /opt/android/ndk
2. Optional but recommended: add "export ANDROID_NDK=/path/to/android/ndk" to ~/.bashrc
3. `$ cd path/to/hping2`
4. Grab the cutils library from your rooted device
  1. `$ adb root (if you haven't already started it as root)`
  2. `$ adb pull /system/lib/libcutils.so`
5. `$ mkdir build && cd build`
6. `$ cmake .. -G"Unix Makefiles" -DCMAKE_TOOLCHAIN_FILE=$ANDROID_NDK/build/cmake/android.toolchain.cmake -DANDROID_PLATFORM=android-19`
  * Or use the full path to the Android NDK instead
  * If your device has Android <= 14, set ANDROID_PLATFORM to android-14 instead
7. `$ make -j4`
8. `$ adb push hping2 /system/xbin`
9. Test it out:
  1. `$ adb shell`
  2. `$ hping2 --version`

