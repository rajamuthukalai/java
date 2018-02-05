package com.myjava.sorts;

import java.util.Arrays;

public class MergeSort {
	
	public static void main(String args[]) {
		int[] source = new int[]{5, 2, 9, 4, 7, 10, 9};
		System.out.println(Arrays.toString(source));
		source = mergeSort(source);
		System.out.println(Arrays.toString(source));
	}
	
	public static int[] mergeSort(int[] list) {
		if (list == null || list.length <= 1) {
			return list;
		}
		int length = list.length;
		int mid = length/2;
		int[] left = new int[mid];
		int[] right;
		if (length % 2 == 0) {
			right = new int[mid];
		} else {
			right = new int[mid+1];
		}
		for (int i=0; i< mid; i++) {
			left[i] = list[i];
		}
		int k = 0;
		for (int j=mid; j< length; j++) {
			right[k] = list[j];
			k++;
		}
		left = mergeSort(left);
		right = mergeSort(right);
		int[] result = new int[length];
		result = merge(left, right);
		return result;
	}
	
	static int[] merge(int[] left, int[] right) {
		int[] result = new int[left.length + right.length];
		int leftIndex = 0;
		int rightIndex = 0;
		int resultIndex = 0;
		while (leftIndex < left.length || rightIndex < right.length) {
			if (leftIndex < left.length && rightIndex < right.length) {
				if (left[leftIndex] <= right[rightIndex]) {
					result[resultIndex] = left[leftIndex];
					resultIndex++;
					leftIndex++;
				} else {
						result[resultIndex] = right[rightIndex];
						resultIndex++;
						rightIndex++;
				}
			} else if (leftIndex < left.length) {
				result[resultIndex] = left[leftIndex];
				resultIndex++;
				leftIndex++;
			} else if (rightIndex < right.length) {
				result[resultIndex] = right[rightIndex];
				resultIndex++;
				rightIndex++;
			}
		}
		return result;
	}
}