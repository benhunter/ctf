package org.springframework.ui.context;

import org.springframework.lang.Nullable;

public interface HierarchicalThemeSource extends ThemeSource {
  void setParentThemeSource(@Nullable ThemeSource paramThemeSource);
  
  @Nullable
  ThemeSource getParentThemeSource();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/context/HierarchicalThemeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */