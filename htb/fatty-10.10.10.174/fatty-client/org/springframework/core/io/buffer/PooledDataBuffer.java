package org.springframework.core.io.buffer;

public interface PooledDataBuffer extends DataBuffer {
  boolean isAllocated();
  
  PooledDataBuffer retain();
  
  boolean release();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/buffer/PooledDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */