#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <random>
#define NUM_ARRAY 100000

int arr[NUM_ARRAY];
int squared_arr[NUM_ARRAY];
int result;

pthread_mutex_t result_mutex;

void *threadfn(void *arg)
{
    long long thread_num = (long long)(arg);
    pthread_mutex_lock(&result_mutex);
    squared_arr[thread_num] = arr[thread_num] * arr[thread_num];
    result += squared_arr[thread_num];
    pthread_mutex_unlock(&result_mutex);
    return 0;
}
int main()
{
    printf("Starting\n");
    // initialize the array with random numbers from 1 to 4
    for (int & i : arr) {
        i = rand() % 4 + 1;
    }
    pthread_mutex_init(&result_mutex, nullptr);

    pthread_t threads[NUM_ARRAY]; // Thread control blocks.
    for (long int i = 0; i < NUM_ARRAY; i++)
        pthread_create(&threads[i], nullptr, threadfn, (void *)i);
    for (int i = 0; i < NUM_ARRAY; i++)
        pthread_join(threads[i], nullptr);

//    for (int i = 0; i < NUM_ARRAY; i++) {
//        printf("arr[%d] = %d, squared_arr[%d] = %d\n", i, arr[i], i, squared_arr[i]);
//    }
    printf("Result: %d\n", result);


    result = 0;
    for (int i = 0; i < NUM_ARRAY; i++)
        result += arr[i] * arr[i];
    printf("Result: %d\n", result);

    return 0;
}