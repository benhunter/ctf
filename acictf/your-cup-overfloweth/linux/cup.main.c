
int main(void)

{
  int iRand;
  uint uVar1;
  ssize_t intCupBytesRead;
  char charbuff64 [64];
  void *strFillCup;
  int intInput;
  char *strCup;
  int intCupBytesRead2;
  int intCupValue;
  
  setvbuf(stdin,(char *)0x0,2,0);
  setvbuf(stdout,(char *)0x0,2,0);
  setvbuf(stderr,(char *)0x0,2,0);
  alarm(0x3c);
  puts("Welcome to Cups Inc.");
  while( true ) {
    puts(
        "What type of cup would you like to purchase?\n0) Mug\n1) Flute\n2) Goblet (of fire)\n3)Stein\n4) Teacup\n5) Wine glass\n9) None (leave)\n"
        );
    read_line(charbuff64);
    intInput = atoi(charbuff64);
    strCup = (char *)0x0;
    switch(intInput) {
    case 0:
      strCup = "Mug";
      intCupValue = 0x40;
      break;
    case 1:
      strCup = "Flute";
      intCupValue = 0x20;
      break;
    case 2:
      strCup = "Goblet";
      intCupValue = 0x80;
      break;
    case 3:
      strCup = "Stein";
      intCupValue = 0x60;
      break;
    case 4:
      strCup = "Teacup";
      intCupValue = 0x30;
      break;
    case 5:
      strCup = "Wine glass";
      intCupValue = 0x20;
    }
    if ((strCup == (char *)0x0) ||
       (strFillCup = malloc((long)intCupValue), strFillCup == (void *)0x0)) break;
    puts("Alright, let\'s fill our cup now");
    intCupBytesRead = read(0,strFillCup,(long)intCupValue);
    intCupBytesRead2 = (int)intCupBytesRead;
    if (intCupBytesRead2 < 0) {
      return 0;
    }
    if (intCupBytesRead2 != intCupValue) {
      puts("You didn\'t fill it up all the way. What\'s the matter? Not thirsty?");
    }
    puts("Alright, let\'s drink up.");
    while (0 < intCupBytesRead2) {
      sleep(1);
      printf("glug ");
      iRand = rand();
      uVar1 = (uint)(iRand >> 0x1f) >> 0x1b;
      intCupBytesRead2 = intCupBytesRead2 - ((iRand + uVar1 & 0x1f) - uVar1);
    }
    puts("\nAh. That was tasty.");
    free(strFillCup);
    strFillCup = (void *)0x0;
  }
  return 0;
}

