add_library(cutils SHARED IMPORTED GLOBAL)
set_target_properties(cutils PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/${ANDROID_ABI}/libcutils.so)

