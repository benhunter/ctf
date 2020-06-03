package org.springframework.web.multipart;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public interface MultipartRequest {
  Iterator<String> getFileNames();
  
  @Nullable
  MultipartFile getFile(String paramString);
  
  List<MultipartFile> getFiles(String paramString);
  
  Map<String, MultipartFile> getFileMap();
  
  MultiValueMap<String, MultipartFile> getMultiFileMap();
  
  @Nullable
  String getMultipartContentType(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/MultipartRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */