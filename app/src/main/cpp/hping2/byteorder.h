#ifndef BYTEORDER_H
#define BYTEORDER_H

/* Detect platform endianness at compile time */
/* Adapted from https://gist.github.com/jtbr/7a43e6281e6cca353b33ee501421860c */

/* When available, these headers can improve platform endianness detection */
#if defined(__linux__) && !defined(__ANDROID__)
#    include <endian.h> // gnu libc normally provides, linux
#elif defined(__APPLE__) || defined(__OpenBSD__) || defined(__FreeBSD__)
#    include <machine/endian.h>
#elif defined (__NetBSD__)
#    include <machine/endian_machdep.h>
#elif defined (__MINGW32__)
#    include <sys/param.h>
#elif defined(__sun) // solaris
#    include <sys/isa_defs.h>
#elif defined(__ANDROID__)
#    include <sys/endian.h>
#endif

#if !defined(__LITTLE_ENDIAN__) && !defined(__BIG_ENDIAN__)
#  if __BYTE_ORDER__ == __ORDER_BIG_ENDIAN__ || \
      __BYTE_ORDER == __BIG_ENDIAN || _BYTE_ORDER == _BIG_ENDIAN || BYTE_ORDER == BIG_ENDIAN || \
     defined(_BIG_ENDIAN) || \
     defined(__ARMEB__) || defined(__THUMBEB__) || defined(__AARCH64EB__) || \
     defined(_MIBSEB) || defined(__MIBSEB) || defined(__MIBSEB__) || \
     defined(_M_PPC)
#    define __BIG_ENDIAN__
#  elif __BYTE_ORDER__ == __ORDER_LITTLE_ENDIAN__ || /* gcc */\
     __BYTE_ORDER == __LITTLE_ENDIAN /* linux header */ || _BYTE_ORDER == _LITTLE_ENDIAN || BYTE_ORDER == LITTLE_ENDIAN /* mingw header */||  \
     defined(_LITTLE_ENDIAN) || /* solaris */ \
     defined(__ARMEL__) || defined(__THUMBEL__) || defined(__AARCH64EL__) || \
     defined(_MIPSEL) || defined(__MIPSEL) || defined(__MIPSEL__) || \
     defined(_M_IX86) || defined(_M_X64) || defined(_M_IA64) || /* msvc for intel processors */ \
     defined(_M_ARM) /* msvc code on arm executes in little endian mode */
#    define __LITTLE_ENDIAN__
#  endif
#endif

#if !defined(__LITTLE_ENDIAN__) && !defined(__BIG_ENDIAN__)
#   error "Unknown endianness. Please add the endianness for this platform to byteorder.h."
#endif

#endif /* BYTEORDER_H */
