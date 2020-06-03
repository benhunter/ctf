package org.springframework.jmx.export.metadata;

import java.lang.reflect.Method;
import org.springframework.lang.Nullable;

public interface JmxAttributeSource {
  @Nullable
  ManagedResource getManagedResource(Class<?> paramClass) throws InvalidMetadataException;
  
  @Nullable
  ManagedAttribute getManagedAttribute(Method paramMethod) throws InvalidMetadataException;
  
  @Nullable
  ManagedMetric getManagedMetric(Method paramMethod) throws InvalidMetadataException;
  
  @Nullable
  ManagedOperation getManagedOperation(Method paramMethod) throws InvalidMetadataException;
  
  ManagedOperationParameter[] getManagedOperationParameters(Method paramMethod) throws InvalidMetadataException;
  
  ManagedNotification[] getManagedNotifications(Class<?> paramClass) throws InvalidMetadataException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/metadata/JmxAttributeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */