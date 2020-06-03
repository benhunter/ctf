
void main(void)

{
  int memcmpResult;
  void *sInputEncodeFirst;
  void *sEncodeSecond;
  long in_FS_OFFSET;
  char sInput [16];
  undefined auStack72 [56];
  long local_10;
  
  local_10 = *(long *)(in_FS_OFFSET + 0x28);
  puts("There\'s a hidden message in this binary");
  puts("Find it, and get a flag!");
  printf(">>> ");
  fflush((FILE *)0x0);
                    /* unsafe fegts, 64 bytes */
  fgets(sInput,0x40,stdin);
  sInputEncodeFirst = encode_first((long)sInput);
  sEncodeSecond = encode_second((long)auStack72);
  memcmpResult = memcmp(sInputEncodeFirst,secret1,0x10);
  if (memcmpResult == 0) {
    memcmpResult = memcmp(sEncodeSecond,secret2,0x10);
    if (memcmpResult == 0) {
      puts("Correct! Here is your flag:");
      read_and_print_flag();
    }
    else {
      puts("Sorry, that\'s not correct!");
      fflush((FILE *)0x0);
    }
  }
  else {
    puts("Sorry, that\'s not correct!");
    fflush((FILE *)0x0);
  }
  if (local_10 != *(long *)(in_FS_OFFSET + 0x28)) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}

