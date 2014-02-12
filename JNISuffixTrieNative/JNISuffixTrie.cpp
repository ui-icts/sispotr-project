#include <iostream>
#include <fstream>
#include <jni.h>
#include <stdio.h>
//#include <windows.h>
//#include <psapi.h>
#include <vector>
#include <boost/unordered_map.hpp>
#include <boost/unordered_set.hpp>
#include <boost/foreach.hpp>
#include <boost/lexical_cast.hpp>
#include "JNISuffixTrieNative.h"
#include "JNISuffixTrie_LiteNative.h"
#include "JNISuffixTrieSearch_LiteNative.h"
#include <map>
using namespace std;


// SuffixTrie
typedef boost::unordered_map<unsigned int, vector<unsigned int> > SuffixTrie;
//typedef boost::unordered_map<unsigned short, boost::unordered_set<unsigned int> > Accession;
//typedef boost::unordered_map<unsigned int, Accession > SuffixTrieAccessions;

// variables
static bool initialized = false;
static SuffixTrie st;
//static SuffixTrieAccessions stAccessions;
#define MIN_INT 0;
#define MIN_SHORT 0;
static bool OUTPUT = false;
static int trieDepth = -1;
static unsigned int nodeCounter = MIN_INT;
//static unsigned short accessionCounter = MIN_SHORT;
static long totalNodes = 0;
static unsigned int root = 0;
static unsigned int current;
static unsigned long beginMemoryVM = 0;
static unsigned long endMemoryVM = 0;
static unsigned long beginMemoryRS = 0;
static unsigned long endMemoryRS = 0;

// prototpyes
static unsigned int inrementID();
static unsigned int addNode(char c);
static void addAccession(unsigned int *node, unsigned short *accession, unsigned int *offset);
static bool compareCharacter(JNIEnv *env, jobject obj, unsigned int current, char compare);
static unsigned int getCharacter(unsigned int *node);
static long getTotalNodes();

/**
 * Increments the nodeCounter
 * checked
 * @return
 */
static unsigned int incrementID() {
    return nodeCounter++;
}

/**
 * Increments the accession counter
 * @return 
 */
/*static unsigned short incrementAcc() {
    return accessionCounter++;
}*/

/**
 * Native call - add the root node
 * checked
 * @param c
 * @param depth
 */
JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_addRoot
(JNIEnv *env, jobject obj, jchar c) {
    //static void addRoot(char c) {
    // the root MUST BE the default value for appending to work correctly - currently 0
    root = incrementID();
    if(OUTPUT) cout << "root id " << root << endl;
    int ci = c;
    st[root].push_back(ci);
}

/**
 * Adds a non-root node
 * checked
 * @param c  the character of the new node
 * @return   the new node's id
 */
static unsigned int addNode(char c) {
    unsigned int i = incrementID();
    if (OUTPUT) cout << " adding node " << i  << endl;
    int ci = c;
    st[i].push_back(ci);
    return i;
}

/**
 * set node counter to max + 1
 */
JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_setNodeCounter
  (JNIEnv *env, jobject obj, jstring j) {
//static void setNodeCounter(JNIEnv *env, jstring j){
    const char* c = env->GetStringUTFChars(j, NULL);
    try {
        nodeCounter = boost::lexical_cast<unsigned int>(c);
    } catch( boost::bad_lexical_cast const& ) {
        cout << "Error: input string was not valid" << std::endl;
    }
    env->ReleaseStringUTFChars(j, c);
}

/**
 * set node
 * jstring to string to unsigned int
 * set vector
 * jstring to string to unsigned int
 */
JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_setNode
  (JNIEnv *env, jobject obj, jstring node, jstring vector) {
//static void setNode(JNIEnv *env, jstring node, jstring vector){
    const char* n = env->GetStringUTFChars(node, NULL);
    const char* v = env->GetStringUTFChars(vector, NULL);
    try {
        unsigned int nodeID = boost::lexical_cast<unsigned int>(n);
        unsigned int vectorPos = boost::lexical_cast<unsigned int>(v);
        st[nodeID].push_back(vectorPos);
       
    } catch( boost::bad_lexical_cast const& ) {
        cout << "Error: input string was not valid" << std::endl;
    }
    env->ReleaseStringUTFChars(node, n);
    env->ReleaseStringUTFChars(vector, v);
}

JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_writeSuffixTrie
  (JNIEnv *env, jobject obj) {
//static void writeSuffixTrie(JNIEnv *env, jobject obj){
    // get the java class
    jclass cls = env->GetObjectClass(obj);
    if(cls == 0) cout << "java class not found" << endl;
    
    // get the java method - I for int, J for long
    jmethodID mid = env->GetStaticMethodID(cls, "addNode", "([Ljava/lang/String;)V");
    if(mid == 0) cout << "java method addNode not found" << endl;
    
    // iterate over the suffix trie
    BOOST_FOREACH(SuffixTrie::value_type i, st) {
        //cout << i.first << endl;
        
        // convert the unsigned int to a jstring
        ostringstream convert1;
        convert1 << i.first;
        string str1 = convert1.str();
        const char *cc1 = str1.c_str();
        jstring jstr1 = env->NewStringUTF(cc1);
        
        BOOST_FOREACH(unsigned int& j, i.second) {
            //cout << "\t" << j << endl;
            // convert the unsigned int to a jstring
            ostringstream convert2;
            convert2 << j;
            string str2 = convert2.str();
            const char *cc2 = str2.c_str();
            jstring jstr2 = env->NewStringUTF(cc2);

            // create the returned array element
            jobjectArray applicationArgs = env->NewObjectArray(2, env->FindClass("java/lang/String"), NULL);

            // add the nodeID and offset to the array
            env->SetObjectArrayElement(applicationArgs, 0, jstr1);
            env->SetObjectArrayElement(applicationArgs, 1, jstr2);

            // execute the java method
            env->CallStaticVoidMethod(cls, mid, applicationArgs);
            
            // dereference the jstrings
            env->DeleteLocalRef(jstr2);
        }
        // dereference the jstrings
        env->DeleteLocalRef(jstr1);
    }
}

/**
 *
 * checked
 * @param node
 * @param accession
 * @param offset
 */
/*static void addAccession(unsigned int node, unsigned short accession, int offset) {
    SuffixTrieAccessions::iterator staIt = stAccessions.find(node);
    if (staIt != stAccessions.end()) {
        if (OUTPUT) cout << "node " << node << " exists" << endl;
        Accession::iterator accIt = (*staIt).second.find(accession);
        if (accIt != staIt->second.end()) {
            if (OUTPUT) cout << "  accession " << accession << " exits" << endl;
            accIt->second.insert(offset);
        } else {
            if (OUTPUT) cout << "  accession " << accession << " does not exist" << endl;
            (*staIt).second[accession].insert(offset);
        }
    } else {
        if (OUTPUT) cout << "node " << node << " does not exist" << endl;
        stAccessions[node][accession].insert(offset);
    }
}*/

/**
 * Creates a java call - addAccession(String[]) in java where [0] = nodeID, [1] = offset.
 * The current accession number is known on the java end.
 * 
 * http://home.pacifier.com/~mmead/jni/delphi/JEDI/DOCS/delphi-jni-2.html
 * @param env           the java environment var
 * @param obj           the java object var
 * @param nodeID        the node id for which the accession/offset will be added
 * @param offset        the offset to add
 */
static void addAccession(JNIEnv *env, jobject obj, unsigned int nodeID, unsigned int offset){
    // get the java class
    jclass cls = env->GetObjectClass(obj);
    if(cls == 0) cout << "java class not found" << endl;
    
    // get the java method - I for int, J for long
    jmethodID mid = env->GetStaticMethodID(cls, "addAccession", "([Ljava/lang/String;)V");
    if(mid == 0) cout << "java method addAccession not found" << endl;

    // convert the unsigned int to a jstring
    ostringstream convert1;
    convert1 << nodeID;
    string str1 = convert1.str();
    const char *cc1 = str1.c_str();
    jstring jstr1 = env->NewStringUTF(cc1);

    // convert the unsigned int to a jstring
    ostringstream convert2;
    convert2 << offset;
    string str2 = convert2.str();
    const char *cc2 = str2.c_str();
    jstring jstr2 = env->NewStringUTF(cc2);

    // create the returned array element
    jobjectArray applicationArgs = env->NewObjectArray(2, env->FindClass("java/lang/String"), NULL);
    
    // add the nodeID and offset to the array
    env->SetObjectArrayElement(applicationArgs, 0, jstr1);
    env->SetObjectArrayElement(applicationArgs, 1, jstr2);

    // execute the java method
    env->CallStaticVoidMethod(cls, mid, applicationArgs);

    // dereference the jstrings
    env->DeleteLocalRef(jstr1);
    env->DeleteLocalRef(jstr2);
}

/**
 * Creates a java call - compareCharater(char, char) in java.
 * Returns true if match else false.
 * checked
 * @param env           the java environment var
 * @param obj           the java object var
 * @param current       the current node character
 * @param compare       the node to compare from the sequence
 * @return              true if a match
 */
static bool compareCharacter(JNIEnv *env, jobject obj, unsigned int current, char compare) {
    // convert the unsigned int to a char
    //char c1 = static_cast<char>(current);
    
    // get the java class
    //jclass cls = env->GetObjectClass(obj);
    // the java class needs to be explicit because we are calling this method from various java classes
    jclass cls = env->FindClass("edu/uiowa/icts/ray/jnisuffixtrie/SuffixTrie_Lite");
    if(cls == 0) cout << "java class not found" << endl;
    
    // get the java method - I for int, J for long
    jmethodID mid = env->GetStaticMethodID(cls, "compareCharacter", "(IC)Z");
    if(mid == 0) cout << "java method compareCharacter not found" << endl;
    //cout << "from c++: " << current << ", " << compare << endl;
    bool match = env->CallStaticBooleanMethod(cls, mid, current, compare);
    
    return match;
}

/**
 * Gets the character for the passed node
 * checked
 * @param node  the node id
 * @return      the character as an int
 */
static unsigned int getCharacter(unsigned int *node) {
    // gets the array for the given node and retireves the 0th element which is the character
    //if (OUTPUT) cout << "find a specific character for " << *node << endl;
    SuffixTrie::iterator stIter = st.find(*node);
    vector<unsigned int> v = (*stIter).second;
    //if (OUTPUT) cout << "\t" << (char) v[0] << endl;
    return v[0];
}

/**
 * Returns the total number of nodes
 * checked
 * @param env   the java environment var
 * @param obj   the java object var
 * @return      the total number of nodes
 */
JNIEXPORT jlong JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_getTotalNodes
(JNIEnv *env, jobject obj) {
    //static long getTotalNodes() {
    return totalNodes;
}

/**
 * This addes a sequence to the suffix trie. A sequence is passed as a char[] and
 * we iterate over the sequence based on a sliding window of i...trieDepth+i adding
 * or updating nodes as we travers the suffix trie.
 * checked
 * @param env           the java environment var
 * @param obj           the java object var
 * @param value         the char[] of the sequence to add
 * @param length        the length of the char[]
 */
JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_addSeq
(JNIEnv *env, jobject obj, jcharArray value, jint length) {
    // convert jcharArray to char[]
    bool isCopy = true;
    jchar *values = env->GetCharArrayElements(value, NULL);

    // for each position in the sequence
    if (OUTPUT) cout << "adding sequence of length " << length << endl;
    for (int i = 0; i < length; i++) {
        SuffixTrie::iterator stIter = st.find(root);
        if (OUTPUT) cout << stIter->second.size() << endl;
        int depth = length;
        if (trieDepth != -1) {
            depth = min(trieDepth + i, depth);
        }
        for (int j = i; j < depth; j++) {
            bool found = false;
            // four at most (A,C,G,T) - skip character
            for (int k = 1; k < stIter->second.size(); k++) {
                unsigned int currentNode = stIter->second[k];
                unsigned int currentChar = getCharacter(&currentNode);
                if (compareCharacter(env, obj, currentChar, values[j])) {
                    current = currentNode;
                    if (OUTPUT) cout << "  found " << values[j] << " at node: " << currentNode << endl;
                    found = true;
                    break;
                }
            }
            if (!found) {
                current = addNode(values[j]);
                totalNodes++;
                stIter->second.push_back(current);  // queue
            }
            addAccession(env, obj, current, i);
            stIter = st.find(current);
        }
    }
    if (OUTPUT) cout << "finished generating trie" << endl;
    env->ReleaseCharArrayElements(value, values, 0);
}

/**
 * Returns the status of the suffix trie
 * @param env   the java environment var
 * @param obj   the java object var
 * @return      true if the suffix trie is initialized
 */
JNIEXPORT jboolean JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_getInitializationStatus
(JNIEnv *env, jobject obj) {
    return initialized;
}

/**
 * Instantiates a new suffix trie
 * @param env   the java environment var
 * @param obj   the java object var
 * @param depth the depth of the suffix trie
 */
JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_newSuffixTrie
(JNIEnv *env, jobject obj, jint depth) {
    initialized = true;
    trieDepth = depth;
    printf("New SuffixTrie created of depth %u\n", depth);
}

/**
 * Test code area
 * @param env
 * @param obj
 * @return 
 */
JNIEXPORT jintArray JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_Main_nativeGetIntArray
(JNIEnv *env, jobject obj) {
    printf("baseline memory");
    //mem();

    // suffix trie
    /*addRoot('Z');
    unsigned short n = 1;
    char c[] = {'A', 'A', 'C', 'T', 'T', 'G', 'C', 'G', 'T'};
    int l = 9;
    generateTree(&n, c, &l);
    getCharacter(&root);
    if (OUTPUT) cout << "compare " << boolalpha << compareCharacter(90, 'Z') << endl;*/

    /**
     * full iterator example
     */
    /*if (OUTPUT) {*/
    cout << "full iterator example" << endl;

    BOOST_FOREACH(SuffixTrie::value_type i, st) {
        cout << i.first << endl;

        BOOST_FOREACH(unsigned int& j, i.second) {
            cout << "\t" << j << endl;
        }
    }
    /*}*/

    /**
     * find an element and iterator for the values
     */
    /*cout << "find a specific node" << endl;
    SuffixTrie::iterator stIter = st.find(node);
    if (stIter == st.end()) {
        //error
        return NULL;
    }
    cout << stIter->first << endl;

    BOOST_FOREACH(unsigned int& j, stIter->second) {
        cout << "\t" << j << endl;
    }*/

    /**
     * find a key and then find a value
     * copy the list<int>
     */
    /*cout << "find a specific value" << endl;
    SuffixTrie::iterator stIter2 = st.find(node);
    if (stIter2 == st.end()) {
        // error
        return NULL;
    }
    cout << stIter2->first << endl;
    vector<unsigned int> lCopy = stIter2->second; // deep copy
    vector<unsigned int>::iterator lIter = find(stIter2->second.begin(), stIter2->second.end(), accession);
    if (lIter == stIter2->second.end()) {
        // error
        return NULL;
    }
    cout << "\t" << *lIter << endl;*/

    /**
     * remove an element from the inner set and outer if inner is empty
     */
    /*cout << "remove a specific value" << endl;
    SuffixTrie::iterator stIter3 = st.find(node);
    if (stIter3 == st.end()) {
        // error
        return NULL;
    }
    cout << stIter3->first << endl;
    vector<unsigned int>::iterator lIter2 = find(stIter3->second.begin(), stIter3->second.end(), accession);
    if (lIter2 == stIter3->second.end()) {
        // error
        return NULL;
    }
    cout << "\tremoving " << accession << " from " << node << endl;
    stIter3->second.erase(lIter2);
    if (stIter3->second.empty()) {
        st.erase(node);
    }
    // check
    stIter = st.find(node);
    if (stIter == st.end()) {
        //error
        return NULL;
    }
    cout << stIter->first << endl;

    BOOST_FOREACH(unsigned int& j, stIter->second) {
        cout << "\t" << j << endl;
    }*/

    /**
     * display the copied list and verify it hasn't been modified by the previous statement
     */
    /*cout << "copy" << endl;

    BOOST_FOREACH(unsigned int& k, lCopy) {
        cout << "\t" << k << endl;
    }*/


    /**
     * add accession test data
     */
    /*addAccession(0, 0, 2);
    addAccession(0, 1, 3);
    addAccession(1, 0, 5);
    addAccession(1, 0, 6);
    cout << "full iterator example" << endl;*/
    /*BOOST_FOREACH(SuffixTrieAccessions::value_type i, stAccessions) {
        cout << i.first << endl;
        BOOST_FOREACH(Accession::value_type j, i.second){
            cout << "\t" << j.first << endl;
            BOOST_FOREACH(unsigned int k, j.second) {
                cout << "\t\t" << k << endl;
            }
        }
    }*/

    /**
     * find accession
     */
    /*SuffixTrieAccessions::iterator stai = stAccessions.find(1);
    Accession::iterator ai = (*stai).second.find(0);
    BOOST_FOREACH(unsigned int i, ai->second){
        cout << i << endl;
    }*/

    printf("post memory");
    //mem();

    int *ia = new int[st.size()];

    int iaSize = sizeof (ia) - 1; // need to remove 1; [0,size] instead of [0, size)
    jintArray jia = (env)->NewIntArray(iaSize);
    //env->SetIntArrayRegion(jia, 0, iaSize, (jint *) & ia[0]);
    return jia;
}

/**
 * Windows memory usage
 * To run this, add Windows\System32\psapi.dll to linker libraries
 * @param processID
 */
/*void PrintMemoryInfo(DWORD processID) {
    HANDLE hProcess;
    PROCESS_MEMORY_COUNTERS pmc;

    // Print the process identifier.
    printf("\nProcess ID: %u\n", processID);

    // Print information about the memory usage of the process.
    hProcess = OpenProcess(PROCESS_QUERY_INFORMATION |
            PROCESS_VM_READ,
            FALSE, processID);
    if (NULL == hProcess)
        return;

    if (GetProcessMemoryInfo(hProcess, &pmc, sizeof (pmc))) {
        printf("\tPageFaultCount: %u\n", pmc.PageFaultCount);
        printf("\tPeakWorkingSetSize: %u\n", pmc.PeakWorkingSetSize);
        printf("\tWorkingSetSize: %u\n", pmc.WorkingSetSize);
        printf("\tQuotaPeakPagedPoolUsage: %u\n", pmc.QuotaPeakPagedPoolUsage);
        printf("\tQuotaPagedPoolUsage: %u\n", pmc.QuotaPagedPoolUsage);
        printf("\tQuotaPeakNonPagedPoolUsage: %u\n", pmc.QuotaPeakNonPagedPoolUsage);
        printf("\tQuotaNonPagedPoolUsage: %u\n", pmc.QuotaNonPagedPoolUsage);
        printf("\tPagefileUsage: %u\n", pmc.PagefileUsage);
        printf("\tPeakPagefileUsage: %u\n", pmc.PeakPagefileUsage);
    }
    CloseHandle(hProcess);
}*/

//int mem() {
    // Get the list of process identifiers.
    /*DWORD aProcesses[1024], cbNeeded, cProcesses;
    unsigned int i;

    if (!EnumProcesses(aProcesses, sizeof (aProcesses), &cbNeeded)) {
        return 1;
    }

    // Calculate how many process identifiers were returned.
    cProcesses = cbNeeded / sizeof (DWORD);

    // Print the memory usage for each process
    for (i = 0; i < cProcesses; i++) {
        PrintMemoryInfo(aProcesses[i]);
    }*/

    // just for the current process
    /*PrintMemoryInfo(GetCurrentProcessId());

    return 0;
}

void setMemory(DWORD processID, bool begin) {
    HANDLE hProcess;
    PROCESS_MEMORY_COUNTERS pmc;

    // Print information about the memory usage of the process.
    hProcess = OpenProcess(PROCESS_QUERY_INFORMATION |
            PROCESS_VM_READ,
            FALSE, processID);
    if (NULL == hProcess)
        return;

    if (GetProcessMemoryInfo(hProcess, &pmc, sizeof (pmc))) {
        if(begin){
            beginMemory = pmc.WorkingSetSize;
        } else {
            endMemory = pmc.WorkingSetSize;
        }
    }
    CloseHandle(hProcess);
}*/
/********************************************************************************/

/**
 * Linux memory usage
 * http://linux.die.net/man/1/top
 *
 * vm_usage = virt
 * rss = res
 *
 * VIRT -- Virtual Image (kb)
 *  The total amount of virtual memory used by the task. It includes all code, data and shared libraries plus pages that have been swapped out. (Note: you can define the STATSIZE=1 environment variable and the VIRT will be calculated from the /proc/#/state VmSize field.)
 *  VIRT = SWAP + RES.
 *   (SWAP is virtual memory on the HD)
 *
 * RES -- Resident size (kb)
 *  The non-swapped physical memory a task has used.
 *  RES = CODE + DATA.
 *
 * @param vm_usage
 * @param resident_set
 */
void process_mem_usage(unsigned long& vm_usage, unsigned long& resident_set)
{
   using std::ios_base;
   using std::ifstream;
   using std::string;

   vm_usage     = 0;
   resident_set = 0;

   // 'file' stat seems to give the most reliable results
   ifstream stat_stream("/proc/self/stat",ios_base::in);

   // dummy vars for leading entries in stat that we don't care about
   string pid, comm, state, ppid, pgrp, session, tty_nr;
   string tpgid, flags, minflt, cminflt, majflt, cmajflt;
   string utime, stime, cutime, cstime, priority, nice;
   string O, itrealvalue, starttime;

   // the two fields we want
   //
   unsigned long vsize;
   long rss;

   stat_stream >> pid >> comm >> state >> ppid >> pgrp >> session >> tty_nr
               >> tpgid >> flags >> minflt >> cminflt >> majflt >> cmajflt
               >> utime >> stime >> cutime >> cstime >> priority >> nice
               >> O >> itrealvalue >> starttime >> vsize >> rss; // don't care about the rest

   stat_stream.close();

   long page_size_kb = sysconf(_SC_PAGE_SIZE); // in case x86-64 is configured to use 2MB pages
   vm_usage     = vsize;
   resident_set = rss * page_size_kb;
}

int mem(bool begin)
{
   using std::cout;
   using std::endl;

   unsigned long vm, rss;
   process_mem_usage(vm, rss);
   if(begin){
        beginMemoryVM = vm;
        beginMemoryRS = rss;
    } else {
        endMemoryVM = vm;
        endMemoryRS = rss;
    }
   //cout << "VM: " << vm << "; RSS: " << rss << endl;
}

/********************************************************************************/

/**
 * Prints the memory for windows
 * @param env
 * @param obj
 */
JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_printMemoryStatus
(JNIEnv *env, jobject obj) {
    //mem();
}

/**
 * Begins the memory counter
 * @param env
 * @param obj
 */

JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_begingMemoryCounter
  (JNIEnv *env, jobject obj){
    // Windows
    //setMemory(GetCurrentProcessId(), true);
    mem(true);
}

/**
 * Ends the memory counter, computes the statistics, and prints out the stats.
 * @param env
 * @param obj
 */
JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrie_1Lite_endMemoryCounter
  (JNIEnv *env, jobject obj){
    // Windows
    //setMemory(GetCurrentProcessId(), false);
    mem(false);

    cout << "total VM used by c++ = " << (endMemoryVM - beginMemoryVM) << endl;
    cout << "space per node = " << (endMemoryVM - beginMemoryVM)/totalNodes << endl;
    cout << "total RES used by c++ = " << (endMemoryRS - beginMemoryRS) << endl;
    cout << "space per node = " << (endMemoryRS - beginMemoryRS)/totalNodes << endl;
}

/********************************************************************************/
/**
 * Use a stack because we need a DFS. Search is NOT case sensitive
 * unchecked
 * @param key
 * @param value
 * @param length
 */
JNIEXPORT jstring JNICALL Java_edu_uiowa_icts_ray_jnisuffixtrie_SuffixTrieSearch_1Lite_00024searchThread_search
  (JNIEnv *env, jobject obj, jcharArray value, jint length){
    // convert jcharArray to char[]
    jchar *values = env->GetCharArrayElements(value, NULL);
    //bool OUTPUT = true;

    // for each position in the sequence
    if (OUTPUT) cout << " c++ is searching the trie " << endl;
    unsigned int previousNode = root;
    SuffixTrie::iterator stIter = st.find(previousNode);
    int counter = 0;
    jstring jstr = NULL;
    if (OUTPUT) cout << "chidren: " << stIter->second.size() << endl;
    for(int j=1; j<stIter->second.size(); j++){
        unsigned int currentNode = stIter->second[j];
        unsigned int currentChar = getCharacter(&currentNode);
        if (OUTPUT) cout << " compare: " << (char) currentChar << " = " << (char) values[counter] << endl;
        if(compareCharacter(env, obj, currentChar, values[counter])){
            counter++;
            if (OUTPUT) cout << "  true" << endl;
            // if we're at depth, we found a match
            if (counter == length) {
                if (OUTPUT) cout << " found a segment match!" << endl;
                ostringstream convert;
                convert << currentNode;
                string str = convert.str();
                const char *cc = str.c_str();
                jstr = env->NewStringUTF(cc);
                break;

            // else move to the next node and reset the counter
            } else {
                j=0; // this will be iterated before we return; j++
                stIter = st.find(currentNode);
                if (OUTPUT) cout << "   moving from node " << previousNode << " to " << currentNode << endl;
                if (OUTPUT) cout << "     chidren: " << stIter->second.size() << endl;
                previousNode = currentNode;
            }
        }
    }
    if (OUTPUT) cout << "  c++ is finished searching the trie" << endl;
    env->ReleaseCharArrayElements(value, values, 0);
    return jstr;
}
