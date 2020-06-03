/*    */ package org.springframework.jmx.export.assembler;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/*    */ import org.springframework.jmx.export.metadata.JmxMetadataUtils;
/*    */ import org.springframework.jmx.export.metadata.ManagedNotification;
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
/*    */ public abstract class AbstractConfigurableMBeanInfoAssembler
/*    */   extends AbstractReflectiveMBeanInfoAssembler
/*    */ {
/*    */   @Nullable
/*    */   private ModelMBeanNotificationInfo[] notificationInfos;
/* 44 */   private final Map<String, ModelMBeanNotificationInfo[]> notificationInfoMappings = (Map)new HashMap<>();
/*    */ 
/*    */   
/*    */   public void setNotificationInfos(ManagedNotification[] notificationInfos) {
/* 48 */     ModelMBeanNotificationInfo[] infos = new ModelMBeanNotificationInfo[notificationInfos.length];
/* 49 */     for (int i = 0; i < notificationInfos.length; i++) {
/* 50 */       ManagedNotification notificationInfo = notificationInfos[i];
/* 51 */       infos[i] = JmxMetadataUtils.convertToModelMBeanNotificationInfo(notificationInfo);
/*    */     } 
/* 53 */     this.notificationInfos = infos;
/*    */   }
/*    */   
/*    */   public void setNotificationInfoMappings(Map<String, Object> notificationInfoMappings) {
/* 57 */     notificationInfoMappings.forEach((beanKey, result) -> (ModelMBeanNotificationInfo[])this.notificationInfoMappings.put(beanKey, extractNotificationMetadata(result)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey) {
/* 64 */     ModelMBeanNotificationInfo[] result = null;
/* 65 */     if (StringUtils.hasText(beanKey)) {
/* 66 */       result = this.notificationInfoMappings.get(beanKey);
/*    */     }
/* 68 */     if (result == null) {
/* 69 */       result = this.notificationInfos;
/*    */     }
/* 71 */     return (result != null) ? result : new ModelMBeanNotificationInfo[0];
/*    */   }
/*    */   
/*    */   private ModelMBeanNotificationInfo[] extractNotificationMetadata(Object mapValue) {
/* 75 */     if (mapValue instanceof ManagedNotification) {
/* 76 */       ManagedNotification mn = (ManagedNotification)mapValue;
/* 77 */       return new ModelMBeanNotificationInfo[] { JmxMetadataUtils.convertToModelMBeanNotificationInfo(mn) };
/*    */     } 
/* 79 */     if (mapValue instanceof Collection) {
/* 80 */       Collection<?> col = (Collection)mapValue;
/* 81 */       List<ModelMBeanNotificationInfo> result = new ArrayList<>();
/* 82 */       for (Object colValue : col) {
/* 83 */         if (!(colValue instanceof ManagedNotification)) {
/* 84 */           throw new IllegalArgumentException("Property 'notificationInfoMappings' only accepts ManagedNotifications for Map values");
/*    */         }
/*    */         
/* 87 */         ManagedNotification mn = (ManagedNotification)colValue;
/* 88 */         result.add(JmxMetadataUtils.convertToModelMBeanNotificationInfo(mn));
/*    */       } 
/* 90 */       return result.<ModelMBeanNotificationInfo>toArray(new ModelMBeanNotificationInfo[0]);
/*    */     } 
/*    */     
/* 93 */     throw new IllegalArgumentException("Property 'notificationInfoMappings' only accepts ManagedNotifications for Map values");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/assembler/AbstractConfigurableMBeanInfoAssembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */