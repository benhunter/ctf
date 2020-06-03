package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.springframework.lang.Nullable;
import org.springframework.remoting.support.RemoteInvocation;

public interface RmiInvocationHandler extends Remote {
  @Nullable
  String getTargetInterfaceName() throws RemoteException;
  
  @Nullable
  Object invoke(RemoteInvocation paramRemoteInvocation) throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/RmiInvocationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */