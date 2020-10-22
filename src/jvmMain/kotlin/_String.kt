package tech.ketc.util

inline val String.strictLength get() = codePointCount(0, length)
