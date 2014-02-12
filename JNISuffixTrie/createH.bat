cd build\classes
javah -o JNISuffixTrieNative.h -jni edu.uiowa.icts.ray.entry.Main
javah -o JNISuffixTrie_LiteNative.h -jni edu.uiowa.icts.ray.jnisuffixtrie.SuffixTrie_Lite
javah -o JNISuffixTrieSearch_LiteNative.h -jni edu.uiowa.icts.ray.jnisuffixtrie.SuffixTrieSearch_Lite