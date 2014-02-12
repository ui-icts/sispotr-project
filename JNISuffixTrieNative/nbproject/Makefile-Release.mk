#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=GNU-MacOSX
CND_CONF=Release
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/JNISuffixTrie.o


# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=-shared -m64
CXXFLAGS=-shared -m64

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=-L/Users/ray/Desktop/CPP/boost_1_46_1

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk dist/libJNISuffixTrieNative.so

dist/libJNISuffixTrieNative.so: ${OBJECTFILES}
	${MKDIR} -p dist
	${LINK.cc} -dynamiclib -install_name libJNISuffixTrieNative.so -o dist/libJNISuffixTrieNative.so -fPIC ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/JNISuffixTrie.o: JNISuffixTrie.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -O2 -I/D/Program\ Files/Java/jdk1.6.0_05/include -I/D/Program\ Files/Java/jdk1.6.0_05/include/win32 -I/D/Documents\ and\ Settings/Ray/Desktop/Docs/CPP/boost_1_46_1 -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/JNISuffixTrie.o JNISuffixTrie.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}
	${RM} dist/libJNISuffixTrieNative.so

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
