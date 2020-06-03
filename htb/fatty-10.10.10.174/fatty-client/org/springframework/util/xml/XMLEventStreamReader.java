/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Comment;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ class XMLEventStreamReader
/*     */   extends AbstractXMLStreamReader
/*     */ {
/*     */   private XMLEvent event;
/*     */   private final XMLEventReader eventReader;
/*     */   
/*     */   public XMLEventStreamReader(XMLEventReader eventReader) throws XMLStreamException {
/*  51 */     this.eventReader = eventReader;
/*  52 */     this.event = eventReader.nextEvent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public QName getName() {
/*  58 */     if (this.event.isStartElement()) {
/*  59 */       return this.event.asStartElement().getName();
/*     */     }
/*  61 */     if (this.event.isEndElement()) {
/*  62 */       return this.event.asEndElement().getName();
/*     */     }
/*     */     
/*  65 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Location getLocation() {
/*  71 */     return this.event.getLocation();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEventType() {
/*  76 */     return this.event.getEventType();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getVersion() {
/*  82 */     if (this.event.isStartDocument()) {
/*  83 */       return ((StartDocument)this.event).getVersion();
/*     */     }
/*     */     
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/*  92 */     return this.eventReader.getProperty(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStandalone() {
/*  97 */     if (this.event.isStartDocument()) {
/*  98 */       return ((StartDocument)this.event).isStandalone();
/*     */     }
/*     */     
/* 101 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean standaloneSet() {
/* 107 */     if (this.event.isStartDocument()) {
/* 108 */       return ((StartDocument)this.event).standaloneSet();
/*     */     }
/*     */     
/* 111 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEncoding() {
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getCharacterEncodingScheme() {
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPITarget() {
/* 129 */     if (this.event.isProcessingInstruction()) {
/* 130 */       return ((ProcessingInstruction)this.event).getTarget();
/*     */     }
/*     */     
/* 133 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPIData() {
/* 139 */     if (this.event.isProcessingInstruction()) {
/* 140 */       return ((ProcessingInstruction)this.event).getData();
/*     */     }
/*     */     
/* 143 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTextStart() {
/* 149 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText() {
/* 154 */     if (this.event.isCharacters()) {
/* 155 */       return this.event.asCharacters().getData();
/*     */     }
/* 157 */     if (this.event.getEventType() == 5) {
/* 158 */       return ((Comment)this.event).getText();
/*     */     }
/*     */     
/* 161 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeCount() {
/* 168 */     if (!this.event.isStartElement()) {
/* 169 */       throw new IllegalStateException();
/*     */     }
/* 171 */     Iterator<Attribute> attributes = this.event.asStartElement().getAttributes();
/* 172 */     return countIterator(attributes);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttributeSpecified(int index) {
/* 177 */     return getAttribute(index).isSpecified();
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getAttributeName(int index) {
/* 182 */     return getAttribute(index).getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeType(int index) {
/* 187 */     return getAttribute(index).getDTDType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeValue(int index) {
/* 192 */     return getAttribute(index).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private Attribute getAttribute(int index) {
/* 197 */     if (!this.event.isStartElement()) {
/* 198 */       throw new IllegalStateException();
/*     */     }
/* 200 */     int count = 0;
/* 201 */     Iterator<Attribute> attributes = this.event.asStartElement().getAttributes();
/* 202 */     while (attributes.hasNext()) {
/* 203 */       Attribute attribute = attributes.next();
/* 204 */       if (count == index) {
/* 205 */         return attribute;
/*     */       }
/*     */       
/* 208 */       count++;
/*     */     } 
/*     */     
/* 211 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NamespaceContext getNamespaceContext() {
/* 216 */     if (this.event.isStartElement()) {
/* 217 */       return this.event.asStartElement().getNamespaceContext();
/*     */     }
/*     */     
/* 220 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNamespaceCount() {
/*     */     Iterator<Namespace> namespaces;
/* 228 */     if (this.event.isStartElement()) {
/* 229 */       namespaces = this.event.asStartElement().getNamespaces();
/*     */     }
/* 231 */     else if (this.event.isEndElement()) {
/* 232 */       namespaces = this.event.asEndElement().getNamespaces();
/*     */     } else {
/*     */       
/* 235 */       throw new IllegalStateException();
/*     */     } 
/* 237 */     return countIterator(namespaces);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespacePrefix(int index) {
/* 242 */     return getNamespace(index).getPrefix();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(int index) {
/* 247 */     return getNamespace(index).getNamespaceURI();
/*     */   }
/*     */ 
/*     */   
/*     */   private Namespace getNamespace(int index) {
/*     */     Iterator<Namespace> namespaces;
/* 253 */     if (this.event.isStartElement()) {
/* 254 */       namespaces = this.event.asStartElement().getNamespaces();
/*     */     }
/* 256 */     else if (this.event.isEndElement()) {
/* 257 */       namespaces = this.event.asEndElement().getNamespaces();
/*     */     } else {
/*     */       
/* 260 */       throw new IllegalStateException();
/*     */     } 
/* 262 */     int count = 0;
/* 263 */     while (namespaces.hasNext()) {
/* 264 */       Namespace namespace = namespaces.next();
/* 265 */       if (count == index) {
/* 266 */         return namespace;
/*     */       }
/*     */       
/* 269 */       count++;
/*     */     } 
/*     */     
/* 272 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int next() throws XMLStreamException {
/* 277 */     this.event = this.eventReader.nextEvent();
/* 278 */     return this.event.getEventType();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/* 283 */     this.eventReader.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int countIterator(Iterator iterator) {
/* 289 */     int count = 0;
/* 290 */     while (iterator.hasNext()) {
/* 291 */       iterator.next();
/* 292 */       count++;
/*     */     } 
/* 294 */     return count;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/XMLEventStreamReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */