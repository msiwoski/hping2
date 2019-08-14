cmake_minimum_required(VERSION 3.0)

project(hping2)
set(CMAKE_MODULE_PATH ${PROJECT_SOURCE_DIR}/cmake)
include(GNUInstallDirs)

################## Operating system identification and config ##################

# Figure out the system we're running on to make a few tweaks
string(TOUPPER ${CMAKE_SYSTEM_NAME} CONFIG_OS_TYPE)

# for BSD/OS use the historical name as it doesn't include '/'
if (CONFIG_OS_TYPE STREQUAL "BSD/OS")
	set(CONFIG_OS_TYPE "BSD")
endif()

if (CONFIG_OS_TYPE STREQUAL "SUNOS")
  set(SOLARIS_LIBS socket resolv nsl)

  # TODO: This is used in sendtcp.c and sendudp.c, but it's never actually added to a header file or set anywhere accesible to code
  set(SOLARIS_BUG "/* #define STUPID_SOLARIS_CHECKSUM_BUG */")
  string(REGEX MATCH "2\.0*|5\.0*|2\.1*|5\.1*|2\.2*|5\.2*|2\.3*|5\.3*|2\.4*|5\.4*|5\.5\.1" VERSION_MATCH CMAKE_SYSTEM_VERSION)
  if (VERSION_MATCH)
    set(SOLARIS_BUG "#define STUPID_SOLARIS_CHECKSUM_BUG")
  endif()
endif()

# TODO: Probably don't need to change this since we can check if ANDROID is defined in code instead, but should check if CONFIG_OS_TYPE being
# Linux enables any Android-incompatible code
cmake_policy(SET CMP0054 NEW)
if (CONFIG_OS_TYPE STREQUAL "ANDROID")
  set(CONFIG_OS_TYPE "LINUX")
endif()

# Create a systype header file specifying the OS so that code can use it
configure_file(${PROJECT_SOURCE_DIR}/systype.h.in ${PROJECT_BINARY_DIR}/systype.h)

################## User-modifiable properties ##################

set(FORCE_LIBPCAP False CACHE BOOL "Whether to define FORCE_LIBPCAP, which will force hping2 to link against libpcap on Linux (not recommended)")

# Apparently hping2 needs libpcap on non-Linux systems, so if we're forcing linking against it on Linux or on a non-Linux OS, find PCAP
if (FORCE_LIBPCAP OR NOT (CONFIG_OS_TYPE STREQUAL "LINUX"))
  find_package(PCAP REQUIRED)
endif()

################## Library/executable setup ##################

# Android has a different shared memory implementation
if (ANDROID)
  set(SHM_SOURCE "shm_android.c")
else()
  set(SHM_SOURCE "shm_posix.c")
endif()

set(HPING2_SOURCES antigetopt.c
                   arsglue.c
                   binding.c
                   cksum.c
                   datafiller.c
                   datahandler.c
                   display_ipopt.c
                   gethostname.c
                   getifname.c
                   getlhs.c
                   getusec.c
                   hstring.c
                   ip_opt_build.c
                   libpcap_stuff.c
                   linux_sockpacket.c
                   listen.c
                   logicmp.c
                   main.c
                   memlock.c
                   memlockall.c
                   memstr.c
                   memunlock.c
                   memunlockall.c
                   opensockraw.c
                   parseoptions.c
                   random.c
                   relid.c
                   resolve.c
                   rtt.c
                   scan.c
                   send.c
                   sendhcmp.c
                   sendicmp.c
                   sendip.c
                   sendip_handler.c
                   sendrawip.c
                   sendtcp.c
                   sendudp.c
                   signal.c
                   ${SHM_SOURCE}
                   sockopt.c
                   statistics.c
                   strlcpy.c
                   usage.c
                   version.c
                   waitpacket.c)

set(ARS_SOURCES apd.c
                ars.c
                split.c)

# TEMPORARY UNTIL CUTILS IS MOVED OUT
if (ANDROID)
  add_subdirectory(${PROJECT_SOURCE_DIR}/cutils)
endif()

add_library(ars STATIC ${ARS_SOURCES})
add_executable(hping2 ${HPING2_SOURCES})
target_include_directories(hping2 PRIVATE ${PROJECT_BINARY_DIR})
target_include_directories(ars PRIVATE ${PROJECT_BINARY_DIR})
target_link_libraries(hping2 ars ${PCAP_LIBRARY} ${SOLARIS_LIBS})

# TEMPORARY UNTIL CUTILS IS MOVED OUT
if (ANDROID)
  target_link_libraries(hping2 cutils)
  set_target_properties(hping2 PROPERTIES OUTPUT_NAME "libhping2.so")
endif()

# libpcap peculiarities
if (PCAP_FOUND)
  # we searched for libpcap, which means we'll want to add its include dirs to hping2 compilation
  target_include_directories(hping2 PRIVATE ${PCAP_INCLUDE_DIR})
endif()

if (FORCE_LIBPCAP)
  target_compile_definitions(hping2 PRIVATE FORCE_PCAP)
  target_compile_definitions(ars PRIVATE FORCE_PCAP) # I'm unsure if this one is necessary
endif()

################## Installation/post-build tasks ##################

install(FILES "${CMAKE_SOURCE_DIR}/docs/hping2.8" DESTINATION "${CMAKE_INSTALL_MANDIR}/man8"
        PERMISSIONS OWNER_READ OWNER_WRITE GROUP_READ WORLD_READ)

# TODO: Possibly add symlink from "${CMAKE_INSTALL_SBINDIR}/hping2" to "${CMAKE_INSTALL_SBINDIR}/hping" as the original makefile did
install(TARGETS hping2
        RUNTIME DESTINATION "${CMAKE_INSTALL_SBINDIR}"
        PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
