#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef unsigned char    byte;
typedef unsigned int    uint;
typedef unsigned long    ulong;
typedef unsigned short    ushort;

void * encode_first(long param_1);

int main(void) {
  printf("Hello World\n");

  void *first;
  void *second;
  long param = 16;
  first = encode_first(param);
  printf("%s", first);

  return 0;
}


void * encode_first(long param_1)

{
  printf("param: %lu\n", param_1);
  
  byte bXored;
  void *memAllocated;
  uint uiCounter;
  
  memAllocated = malloc(0x10);
  memset(memAllocated,0,0x10);
  uiCounter = 0;
  printf("1\n");
  while (uiCounter < 0x10) {
    printf("2\n");
    
    bXored = *(byte *)(param_1 + (ulong)uiCounter) ^ 0x17;
    // bXored = *(param_1 + (ulong)uiCounter) ^ 0x17;
    
    printf("3\n");
    *(char *)((ulong)uiCounter + (long)memAllocated) =
         bXored + (((char)((uint)(ushort)(short)(char)bXored * 0x41 >> 8) >> 5) -
                  ((char)bXored >> 7)) * -0x7f;
    
    if (*(char *)((long)memAllocated + (ulong)uiCounter) < ' ') {
      *(char *)((long)memAllocated + (ulong)uiCounter) =
           *(char *)((long)memAllocated + (ulong)uiCounter) + ' ';
    }
    uiCounter = uiCounter + 1;
  }
  return memAllocated;
}

