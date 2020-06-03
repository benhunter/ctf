package org.springframework.jmx.export.notification;

import javax.management.Notification;

@FunctionalInterface
public interface NotificationPublisher {
  void sendNotification(Notification paramNotification) throws UnableToSendNotificationException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/notification/NotificationPublisher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */