package org.springframework.ui.context;

import org.springframework.lang.Nullable;

public interface ThemeSource {
  @Nullable
  Theme getTheme(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/context/ThemeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */