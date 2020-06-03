/*    */ package org.springframework.jmx.export.metadata;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ManagedNotification
/*    */ {
/*    */   @Nullable
/*    */   private String[] notificationTypes;
/*    */   @Nullable
/*    */   private String name;
/*    */   @Nullable
/*    */   private String description;
/*    */   
/*    */   public void setNotificationType(String notificationType) {
/* 45 */     this.notificationTypes = StringUtils.commaDelimitedListToStringArray(notificationType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNotificationTypes(@Nullable String... notificationTypes) {
/* 52 */     this.notificationTypes = notificationTypes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String[] getNotificationTypes() {
/* 60 */     return this.notificationTypes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(@Nullable String name) {
/* 67 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getName() {
/* 75 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDescription(@Nullable String description) {
/* 82 */     this.description = description;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getDescription() {
/* 90 */     return this.description;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/metadata/ManagedNotification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */