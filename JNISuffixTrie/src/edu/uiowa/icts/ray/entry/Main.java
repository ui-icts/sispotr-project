package edu.uiowa.icts.ray.entry;

import edu.uiowa.icts.ray.cachemap.CacheMap;
import edu.uiowa.icts.ray.cachemap.SuffixTrieAccessionNode;
import edu.uiowa.icts.ray.database.DBConnect;
import edu.uiowa.icts.ray.jnisuffixtrie.SuffixTrie_Lite;
import edu.uiowa.icts.ray.tuplespace.TupleSpace;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Ray
 *
 * How to (based on Windows, tutorial is 5.5, used linux based on 6.x to help):
 *      Windows: http://cnd.netbeans.org/docs/jni/beginning-jni-win.html
 *      Linux: http://netbeans.org/kb/docs/cnd/beginning-jni-linux.html
 * (pre-req)
 *      cygwin
 *      cnd plugin - go to tools -> plugins -> available plugins, install c/c++
 *                   intalled tab and activate if necessary
 * (1) create a new java project
 *     main calls new Main().nativePrint();
 *     private native void nativePrint();
 * (2) build and clean
 * (3) cmd> cd My Documents\NetBeansProjects\JNISuffixTrie\build\classes (for example)
 * (4) javah -o JNISuffixTrieNative.h -jni edu.uiowa.icts.ray.jnisuffixtrie.Main (see createH.bat for updates)
 * (5) create a new C/C++ Dynamic Library with name JNISuffixTrieNative
 * (6) right-click -> properties
 *      C Compiler -> <All Configurations>:
 *      Include Directories -> [...] Make sure to store path as Absolute or this will not work
 *          D:/Program Files/Java/jdk1.6.0_05/include
 *          D:/Program Files/Java/jdk1.6.0_05/include/win32
 *      Additional Options: -mno-cygwin -Wl,--add-stdcall-alias -shared -m32
 *     Same for C++ Compiler
 *     Linker -> Output change from ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/libJNISuffixTrieNative.dll to dist/libJNISuffixTrieNative.dll
 * (7) Tools -> Options -> C/C++ -> Code Assistance -> Add (this will add the code to code assistance)
 *      D:/Program Files/Java/jdk1.6.0_05/include
 *      D:/Program Files/Java/jdk1.6.0_05/include/win32
 * (8) copy the generated header file (step 4) into the C/C++ project (step 5)
 * (9) right-click Source Files -> Add Existing Item..., select JNISuffixTrieNative.h (yes, into Source NOT header)
 * (10) right-click Source Files -> new C++ source file... (or C)
 *      name: JNISuffixTrie
 *      Add the following code
#include <jni.h>
#include <stdio.h>
#include "JNINative.h"

JNIEXPORT void JNICALL Java_edu_uiowa_icts_ray_jni_Main_nativePrint(JNIEnv *env, jobject obj) {
printf("\nHello World from C\n");
}
 * (11) clean and build
 * (12) clean and build the java file
 * (13) run the java file
 *
 * IMPORTANT
 * Each new native method added to the java file requires a rerunning of 3, 4, 8, and 9 (could also copy paste
 *  of new .h file into the original one after regenerated from 4)
 *
 * All of the return statements are processed before any C/C++ printf regardless of order of execution
 *
 * Accessing objects: http://java.sun.com/docs/books/jni/html/jniTOC.html
 */
public class Main {
    // external call
    private native int[] nativeGetIntArray();
    private final static boolean distribution = true;
    private final static String platform = "lin";       // win, lin, mac
    private static SuffixTrie_Lite st;
    private static int MAX_SIMUL_SEARCH_THREADS;
    private static int MAX_SIMUL_THREADS_PER_SEARCH;
    private static int TUPLE_SPACE_SLEEP;
    private static boolean SYMMETRICAL_CHARACTER_COMPARISON;
    private static int BUILD_CACHEMAP_CAPACITY;
    private static int BUILD_CACHEMAP_MAX_MB_MEMORY;
    private static int SEARCH_CACHEMAP_CAPACITY;
    private static int SEARCH_CACHEMAP_MAX_MB_MEMORY;
    private static int DATABASE_MAX_BATCH_COMMIT;
    private static int DATABASE_MAX_COMMITED_BEFORE_GC;
    private static String SUFFIXTRIE_TASK;
    private static int PARTIAL_READ_LINES;
    private static boolean WRITE_TO_CASSANDRA;


    /**** Adding Cassandra Support ***/
    Keyspace keyspace;
    SimpleCassandraDao simpleCassandraDao;
	

    // jni requirement
    static {
        File f;
        String osFile = null;
        if(platform.equalsIgnoreCase("win")){
            osFile = "libJNISuffixTrieNative.dll";
        } else if(platform.equalsIgnoreCase("lin")) {
            osFile = "libJNISuffixTrieNative.so";
        } else if(platform.equalsIgnoreCase("mac")) {
            osFile = "libJNISuffixTrieNative.so";
        }
        if(distribution){
            String curDir = System.getProperty("user.dir");
            f = new File(curDir, osFile);
        } else {
            String curDir = System.getProperty("user.dir");
            String jniDir = null;
            if(platform.equalsIgnoreCase("win")){
                jniDir = curDir.substring(0, curDir.lastIndexOf("\\")) + "\\JNISuffixTrieNative\\dist";
            } else {
                jniDir = curDir.substring(0, curDir.lastIndexOf("/")) + "/JNISuffixTrieNative/dist";
            }
            f = new File(jniDir, osFile);
        }
        System.load(f.toString());
    }

    public static void main(String[] args) throws IOException, Exception {
        boolean optimal = false;
        System.out.println("SuffixTrie depth = " + args[0] + "\nSequence file: " + args[1]);
        int trieDepth = Integer.parseInt(args[0]);
        String fileName = args[1];
        //String search = "AAGAAGCACC";
        
        // setup thread parameters
        Properties threads = new Properties();
        threads.load(new FileInputStream("config.properties"));
        MAX_SIMUL_SEARCH_THREADS = Integer.parseInt(threads.getProperty("max_simul_search_threads"));
        MAX_SIMUL_THREADS_PER_SEARCH = Integer.parseInt(threads.getProperty("max_simul_threads_per_search"));
        TUPLE_SPACE_SLEEP = Integer.parseInt(threads.getProperty("tuple_space_sleep"));
        SYMMETRICAL_CHARACTER_COMPARISON = Boolean.parseBoolean(threads.getProperty("symmetrical_character_comparison"));
        WRITE_TO_CASSANDRA = Boolean.parseBoolean(threads.getProperty("write_to_cassandra", false));
        BUILD_CACHEMAP_CAPACITY = Integer.parseInt(threads.getProperty("build_cachemap_capacity"));
        BUILD_CACHEMAP_MAX_MB_MEMORY = Integer.parseInt(threads.getProperty("build_cachemap_max_mb_memory"));
        SEARCH_CACHEMAP_CAPACITY = Integer.parseInt(threads.getProperty("search_cachemap_capacity"));
        SEARCH_CACHEMAP_MAX_MB_MEMORY = Integer.parseInt(threads.getProperty("search_cachemap_max_mb_memory"));
        DATABASE_MAX_BATCH_COMMIT = Integer.parseInt(threads.getProperty("database_max_batch_commit"));
        DATABASE_MAX_COMMITED_BEFORE_GC = Integer.parseInt(threads.getProperty("database_max_commited_before_gc"));
        SUFFIXTRIE_TASK = threads.getProperty("suffixtrie_task");
        PARTIAL_READ_LINES = Integer.parseInt(threads.getProperty("partial_read_lines"));

        // setup DBConnect and CacheMap
        Properties suffixTrie = new Properties();
        suffixTrie.load(new FileInputStream("config_db_neuromancer.properties"));
        DBConnect db = new DBConnect(suffixTrie, DATABASE_MAX_BATCH_COMMIT, DATABASE_MAX_COMMITED_BEFORE_GC);

	if (WRITE_TO_CASSANDRA) {

    	 	ApplicationContext context = new ClassPathXmlApplicationContext("cassandra.xml");
    	 	simpleCassandraDao = (SimpleCassandraDao) context.getBean("articleCassandraDao");
    	 	keyspace = (Keyspace) context.getBean("keyspace");
	     	simpleCassandraDao.setKeyspace(keyspace);
	   


		/**************************************
		Examples:

		Write Data to Cassandra via Hector:

			simpleCassandraDao.insert(""+pmid, "ISSN",buffer.toString());


		Read Data from Cassandra via Hector:
			articleTitle = simpleCassandraDao.get("" +pmid, "ArticleTitle");

		**********************************/

	}

        /**
         * Creates the cache: key = Long, value = node object, (x) = size of cache
         */
        CacheMap<Integer, SuffixTrieAccessionNode> cm = new CacheMap<Integer, SuffixTrieAccessionNode>(BUILD_CACHEMAP_CAPACITY, BUILD_CACHEMAP_MAX_MB_MEMORY, true);
        cm.setDB(db);

        if (optimal) {
            //Optimal o = new Optimal();
            //o.search(search);
        } else {
            st = SuffixTrie_Lite.getInstance(trieDepth, cm, db, SYMMETRICAL_CHARACTER_COMPARISON);
            db.setSuffixTrie(st);
            st.getStatus();
            st.initializeSuffixTrie();
            st.getStatus();

            //System.out.println("pre");
            //st.getMemoryUsage();
            //st.startMemoryCunter();
            if(SUFFIXTRIE_TASK.equalsIgnoreCase("create")){
                System.out.println("creating the suffix trie");
                db.resetAllRelations();
                st.buildSuffixTrie(fileName, true, 0);
                st.writeSuffixTrie();
                st.batchSuffixTrieNodes();
            } else if(SUFFIXTRIE_TASK.equalsIgnoreCase("append")){
                System.out.println("appending to the suffix trie");
                db.appendToSuffixTrie();
                st.buildSuffixTrie(fileName, false, PARTIAL_READ_LINES);
                st.writeSuffixTrie();
                st.batchSuffixTrieNodes();
            } else if(SUFFIXTRIE_TASK.equalsIgnoreCase("read")){
                System.out.println("reading in the suffix trie");
                db.readInSuffixTrie();
                db.readInAccessions();
            }

            //System.out.println("post");
            //st.getMemoryUsage();
            //st.stopMemoryCounter();
            // done in TupleSpace.java now
            //SuffixTrieSearch_Lite sts = new SuffixTrieSearch_Lite(st);
            //sts.search(search);
        }

        System.out.println("total: " + Runtime.getRuntime().totalMemory() + ", remaining: " + Runtime.getRuntime().freeMemory());

        cm.writeAll();
        db.commit();

        // clean up because we will not be using these again from here
        cm = null;
        //db.close();
        //db = null;
        System.gc();
        System.runFinalization();
        
        // start TupleSpace
        Properties results = new Properties();
        results.load(new FileInputStream("config_db_results.properties"));
        TupleSpace ts = TupleSpace.getInstance(st, results, suffixTrie, MAX_SIMUL_SEARCH_THREADS, 
                MAX_SIMUL_THREADS_PER_SEARCH, TUPLE_SPACE_SLEEP, SEARCH_CACHEMAP_CAPACITY, SEARCH_CACHEMAP_MAX_MB_MEMORY, db);

        // JNI code
        /*int[] fromNativeIntArray = new Main().nativeGetIntArray();
        System.out.println("From java:");
        for (int i = 0; i < fromNativeIntArray.length; i++) {
            System.out.println("  " + fromNativeIntArray[i]);
        }*/
    }
}
