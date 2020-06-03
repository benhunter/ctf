/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectName;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NotificationListenerHolder
/*     */ {
/*     */   @Nullable
/*     */   private NotificationListener notificationListener;
/*     */   @Nullable
/*     */   private NotificationFilter notificationFilter;
/*     */   @Nullable
/*     */   private Object handback;
/*     */   @Nullable
/*     */   protected Set<Object> mappedObjectNames;
/*     */   
/*     */   public void setNotificationListener(@Nullable NotificationListener notificationListener) {
/*  61 */     this.notificationListener = notificationListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public NotificationListener getNotificationListener() {
/*  69 */     return this.notificationListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNotificationFilter(@Nullable NotificationFilter notificationFilter) {
/*  78 */     this.notificationFilter = notificationFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public NotificationFilter getNotificationFilter() {
/*  88 */     return this.notificationFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandback(@Nullable Object handback) {
/*  99 */     this.handback = handback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getHandback() {
/* 111 */     return this.handback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappedObjectName(@Nullable Object mappedObjectName) {
/* 122 */     this
/* 123 */       .mappedObjectNames = (mappedObjectName != null) ? new LinkedHashSet(Collections.singleton(mappedObjectName)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappedObjectNames(Object... mappedObjectNames) {
/* 134 */     this.mappedObjectNames = new LinkedHashSet(Arrays.asList(mappedObjectNames));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ObjectName[] getResolvedObjectNames() throws MalformedObjectNameException {
/* 145 */     if (this.mappedObjectNames == null) {
/* 146 */       return null;
/*     */     }
/* 148 */     ObjectName[] resolved = new ObjectName[this.mappedObjectNames.size()];
/* 149 */     int i = 0;
/* 150 */     for (Object objectName : this.mappedObjectNames) {
/* 151 */       resolved[i] = ObjectNameManager.getInstance(objectName);
/* 152 */       i++;
/*     */     } 
/* 154 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 160 */     if (this == other) {
/* 161 */       return true;
/*     */     }
/* 163 */     if (!(other instanceof NotificationListenerHolder)) {
/* 164 */       return false;
/*     */     }
/* 166 */     NotificationListenerHolder otherNlh = (NotificationListenerHolder)other;
/* 167 */     return (ObjectUtils.nullSafeEquals(this.notificationListener, otherNlh.notificationListener) && 
/* 168 */       ObjectUtils.nullSafeEquals(this.notificationFilter, otherNlh.notificationFilter) && 
/* 169 */       ObjectUtils.nullSafeEquals(this.handback, otherNlh.handback) && 
/* 170 */       ObjectUtils.nullSafeEquals(this.mappedObjectNames, otherNlh.mappedObjectNames));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 175 */     int hashCode = ObjectUtils.nullSafeHashCode(this.notificationListener);
/* 176 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.notificationFilter);
/* 177 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.handback);
/* 178 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.mappedObjectNames);
/* 179 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/support/NotificationListenerHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */