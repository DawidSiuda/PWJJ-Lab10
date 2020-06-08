#include<jni.h>
#include "Java10Strategy2.h"
#include <stdlib.h>
#include <stdio.h>

enum Sign {
	Sign_O = 1,
	Sign_x,
	Sign_none
};

extern "C" {
	/*
	 * Class:     lab10_Java10Strategy1
	 * Method:    generateAnotherMoveInTheGame
	 * Signature: (II)I
	 */
	JNIEXPORT jint JNICALL Java_lab10_Java10Strategy2_generateAnotherMoveInTheGame(JNIEnv* aEnv, jclass aClass,
		jintArray aArray, jint aX, jint aY, jint aSign)
	{
		jsize length = aEnv->GetArrayLength(aArray);
		jint* array = aEnv->GetIntArrayElements(aArray, NULL);

		jint solution = 0;

		srand(NULL);

		while (true) {
			solution = rand() % length;
			if (array[solution] == 0)
				break;
		}

		aEnv->ReleaseIntArrayElements(aArray, array, NULL);
		return solution;
	}
}