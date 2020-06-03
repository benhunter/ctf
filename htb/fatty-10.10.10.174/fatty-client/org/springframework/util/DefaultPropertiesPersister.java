/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ import java.util.Properties;
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
/*    */ public class DefaultPropertiesPersister
/*    */   implements PropertiesPersister
/*    */ {
/*    */   public void load(Properties props, InputStream is) throws IOException {
/* 58 */     props.load(is);
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(Properties props, Reader reader) throws IOException {
/* 63 */     props.load(reader);
/*    */   }
/*    */ 
/*    */   
/*    */   public void store(Properties props, OutputStream os, String header) throws IOException {
/* 68 */     props.store(os, header);
/*    */   }
/*    */ 
/*    */   
/*    */   public void store(Properties props, Writer writer, String header) throws IOException {
/* 73 */     props.store(writer, header);
/*    */   }
/*    */ 
/*    */   
/*    */   public void loadFromXml(Properties props, InputStream is) throws IOException {
/* 78 */     props.loadFromXML(is);
/*    */   }
/*    */ 
/*    */   
/*    */   public void storeToXml(Properties props, OutputStream os, String header) throws IOException {
/* 83 */     props.storeToXML(os, header);
/*    */   }
/*    */ 
/*    */   
/*    */   public void storeToXml(Properties props, OutputStream os, String header, String encoding) throws IOException {
/* 88 */     props.storeToXML(os, header, encoding);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/DefaultPropertiesPersister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */