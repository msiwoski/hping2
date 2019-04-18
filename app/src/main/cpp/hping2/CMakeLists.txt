cmake_minimum_required(VERSION 3.0)

project(hping2)

################## Operating system identification and config ##################

# Figure out the system we're running on to make a few tweaks
string(TOUPPER ${CMAKE_SYSTEM_NAME} CONFIG_OS_TYPE)

# TODO: Probably don't need to change this since we can check if ANDROID is defined in code instead, but should check if CONFIG_OS_TYPE being
# Linux enables any Android-incompatible code
cmake_policy(SET CMP0054 NEW)
if (CONFIG_OS_TYPE STREQUAL "ANDROID")
  set(CONFIG_OS_TYPE "LINUX")
endif()

# Create a systype header file specifying the OS so that code can use it
configure_file(${PROJECT_SOURCE_DIR}/systype.h.in ${PROJECT_BINARY_DIR}/systype.h)

################## Library/executable setup ##################
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
                   shm_android.c
                   sockopt.c
                   statistics.c
                   strlcpy.c
                   usage.c
                   version.c
                   waitpacket.c)

set(ARS_SOURCES apd.c
                ars.c
                split.c)

add_library(ars STATIC ${ARS_SOURCES})
add_library(hping2 STATIC ${HPING2_SOURCES})
target_include_directories(hping2 PRIVATE ${PROJECT_BINARY_DIR})
target_include_directories(ars PRIVATE ${PROJECT_BINARY_DIR})
target_link_libraries(hping2 ars)

# TEMPORARY UNTIL CUTILS IS MOVED OUT
add_subdirectory(${CMAKE_CURRENT_SOURCE_DIR}/cutils)
target_link_libraries(hping2 cutils)
