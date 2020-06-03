package org.springframework.core.io.buffer;

import java.nio.ByteBuffer;
import java.util.List;

public interface DataBufferFactory {
  DataBuffer allocateBuffer();
  
  DataBuffer allocateBuffer(int paramInt);
  
  DataBuffer wrap(ByteBuffer paramByteBuffer);
  
  DataBuffer wrap(byte[] paramArrayOfbyte);
  
  DataBuffer join(List<? extends DataBuffer> paramList);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/buffer/DataBufferFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */