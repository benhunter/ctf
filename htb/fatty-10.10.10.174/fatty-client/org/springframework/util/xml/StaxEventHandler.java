/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
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
/*     */ class StaxEventHandler
/*     */   extends AbstractStaxHandler
/*     */ {
/*     */   private final XMLEventFactory eventFactory;
/*     */   private final XMLEventWriter eventWriter;
/*     */   
/*     */   public StaxEventHandler(XMLEventWriter eventWriter) {
/*  56 */     this.eventFactory = XMLEventFactory.newInstance();
/*  57 */     this.eventWriter = eventWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaxEventHandler(XMLEventWriter eventWriter, XMLEventFactory factory) {
/*  67 */     this.eventFactory = factory;
/*  68 */     this.eventWriter = eventWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(@Nullable Locator locator) {
/*  74 */     if (locator != null) {
/*  75 */       this.eventFactory.setLocation(new LocatorLocationAdapter(locator));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startDocumentInternal() throws XMLStreamException {
/*  81 */     this.eventWriter.add(this.eventFactory.createStartDocument());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endDocumentInternal() throws XMLStreamException {
/*  86 */     this.eventWriter.add(this.eventFactory.createEndDocument());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startElementInternal(QName name, Attributes atts, Map<String, String> namespaceMapping) throws XMLStreamException {
/*  93 */     List<Attribute> attributes = getAttributes(atts);
/*  94 */     List<Namespace> namespaces = getNamespaces(namespaceMapping);
/*  95 */     this.eventWriter.add(this.eventFactory
/*  96 */         .createStartElement(name, attributes.iterator(), namespaces.iterator()));
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Namespace> getNamespaces(Map<String, String> namespaceMappings) {
/* 101 */     List<Namespace> result = new ArrayList<>(namespaceMappings.size());
/* 102 */     namespaceMappings.forEach((prefix, namespaceUri) -> result.add(this.eventFactory.createNamespace(prefix, namespaceUri)));
/*     */     
/* 104 */     return result;
/*     */   }
/*     */   
/*     */   private List<Attribute> getAttributes(Attributes attributes) {
/* 108 */     int attrLength = attributes.getLength();
/* 109 */     List<Attribute> result = new ArrayList<>(attrLength);
/* 110 */     for (int i = 0; i < attrLength; i++) {
/* 111 */       QName attrName = toQName(attributes.getURI(i), attributes.getQName(i));
/* 112 */       if (!isNamespaceDeclaration(attrName)) {
/* 113 */         result.add(this.eventFactory.createAttribute(attrName, attributes.getValue(i)));
/*     */       }
/*     */     } 
/* 116 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endElementInternal(QName name, Map<String, String> namespaceMapping) throws XMLStreamException {
/* 121 */     List<Namespace> namespaces = getNamespaces(namespaceMapping);
/* 122 */     this.eventWriter.add(this.eventFactory.createEndElement(name, namespaces.iterator()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void charactersInternal(String data) throws XMLStreamException {
/* 127 */     this.eventWriter.add(this.eventFactory.createCharacters(data));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cDataInternal(String data) throws XMLStreamException {
/* 132 */     this.eventWriter.add(this.eventFactory.createCData(data));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ignorableWhitespaceInternal(String data) throws XMLStreamException {
/* 137 */     this.eventWriter.add(this.eventFactory.createIgnorableSpace(data));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processingInstructionInternal(String target, String data) throws XMLStreamException {
/* 142 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, data));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dtdInternal(String dtd) throws XMLStreamException {
/* 147 */     this.eventWriter.add(this.eventFactory.createDTD(dtd));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void commentInternal(String comment) throws XMLStreamException {
/* 152 */     this.eventWriter.add(this.eventFactory.createComment(comment));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void skippedEntityInternal(String name) {}
/*     */ 
/*     */   
/*     */   private static final class LocatorLocationAdapter
/*     */     implements Location
/*     */   {
/*     */     private final Locator locator;
/*     */ 
/*     */     
/*     */     public LocatorLocationAdapter(Locator locator) {
/* 166 */       this.locator = locator;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLineNumber() {
/* 171 */       return this.locator.getLineNumber();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnNumber() {
/* 176 */       return this.locator.getColumnNumber();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getCharacterOffset() {
/* 181 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPublicId() {
/* 186 */       return this.locator.getPublicId();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSystemId() {
/* 191 */       return this.locator.getSystemId();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/StaxEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */