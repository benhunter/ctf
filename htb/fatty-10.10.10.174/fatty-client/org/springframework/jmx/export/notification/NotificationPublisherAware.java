package org.springframework.jmx.export.notification;

import org.springframework.beans.factory.Aware;

public interface NotificationPublisherAware extends Aware {
  void setNotificationPublisher(NotificationPublisher paramNotificationPublisher);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/notification/NotificationPublisherAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */