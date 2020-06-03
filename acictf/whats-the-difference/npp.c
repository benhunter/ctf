



typedef unsigned short    ushort;
typedef short    wchar_t;
typedef wchar_t WCHAR;
typedef WCHAR * LPWSTR;


/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

LPWSTR FUN_0060d000(void)

{

    char * u_CcD5E5bF617973AEe9F8E84bF8FC0C74_0060d180 = u"CcD5E5bF617973AEe9F8E84bF8FC0C74";

  ushort ushortXoredString [32];
  LPWSTR lpwstrCommandLine;
  int iCounter;
  
  lpwstrCommandLine = GetCommandLineW();
  iCounter = 0;
  while( true ) {
    if (0x1f < iCounter) {
      MessageBoxW((HWND)0x0,lpwstrCommandLine + 0x32,(LPCWSTR)ushortXoredString,0);
      return lpwstrCommandLine;
    }
    if ((ushort)(lpwstrCommandLine[iCounter + 0x32] ^
                u_CcD5E5bF617973AEe9F8E84bF8FC0C74_0060d180[iCounter]) !=
        *(ushort *)(&DAT_0060d0c0 + iCounter * 2)) break;
    ushortXoredString[iCounter] =
         *(ushort *)(&DAT_0060d120 + iCounter * 2) ^
         u_CcD5E5bF617973AEe9F8E84bF8FC0C74_0060d180[iCounter];
    iCounter = iCounter + 1;
  }
  return lpwstrCommandLine;
}
