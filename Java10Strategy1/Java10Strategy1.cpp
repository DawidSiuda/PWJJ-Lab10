#include<jni.h>
#include "Java10Strategy1.h"
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
	JNIEXPORT jint JNICALL Java_lab10_Java10Strategy1_generateAnotherMoveInTheGame(JNIEnv* aEnv, jclass aClass,
																						jintArray aArray, jint aX, jint aY, jint aSign)
	{
		jsize length = aEnv->GetArrayLength(aArray);
		jint* array = aEnv->GetIntArrayElements(aArray, NULL);

		jint solution = 0;
		for (int i = 0; i < length; i++)
		{
			if (array[i] == 0)
			{
				solution = i;
				break;
			}
		}

		aEnv->ReleaseIntArrayElements(aArray, array, NULL);
		return solution;
	}
}