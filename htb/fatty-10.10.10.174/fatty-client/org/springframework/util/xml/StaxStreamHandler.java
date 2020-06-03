/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
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
/*     */ class StaxStreamHandler
/*     */   extends AbstractStaxHandler
/*     */ {
/*     */   private final XMLStreamWriter streamWriter;
/*     */   
/*     */   public StaxStreamHandler(XMLStreamWriter streamWriter) {
/*  43 */     this.streamWriter = streamWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startDocumentInternal() throws XMLStreamException {
/*  49 */     this.streamWriter.writeStartDocument();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endDocumentInternal() throws XMLStreamException {
/*  54 */     this.streamWriter.writeEndDocument();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startElementInternal(QName name, Attributes attributes, Map<String, String> namespaceMapping) throws XMLStreamException {
/*  61 */     this.streamWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
/*     */     
/*  63 */     for (Map.Entry<String, String> entry : namespaceMapping.entrySet()) {
/*  64 */       String prefix = entry.getKey();
/*  65 */       String namespaceUri = entry.getValue();
/*  66 */       this.streamWriter.writeNamespace(prefix, namespaceUri);
/*  67 */       if ("".equals(prefix)) {
/*  68 */         this.streamWriter.setDefaultNamespace(namespaceUri);
/*     */         continue;
/*     */       } 
/*  71 */       this.streamWriter.setPrefix(prefix, namespaceUri);
/*     */     } 
/*     */     
/*  74 */     for (int i = 0; i < attributes.getLength(); i++) {
/*  75 */       QName attrName = toQName(attributes.getURI(i), attributes.getQName(i));
/*  76 */       if (!isNamespaceDeclaration(attrName)) {
/*  77 */         this.streamWriter.writeAttribute(attrName.getPrefix(), attrName.getNamespaceURI(), attrName
/*  78 */             .getLocalPart(), attributes.getValue(i));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endElementInternal(QName name, Map<String, String> namespaceMapping) throws XMLStreamException {
/*  85 */     this.streamWriter.writeEndElement();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void charactersInternal(String data) throws XMLStreamException {
/*  90 */     this.streamWriter.writeCharacters(data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cDataInternal(String data) throws XMLStreamException {
/*  95 */     this.streamWriter.writeCData(data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ignorableWhitespaceInternal(String data) throws XMLStreamException {
/* 100 */     this.streamWriter.writeCharacters(data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processingInstructionInternal(String target, String data) throws XMLStreamException {
/* 105 */     this.streamWriter.writeProcessingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dtdInternal(String dtd) throws XMLStreamException {
/* 110 */     this.streamWriter.writeDTD(dtd);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void commentInternal(String comment) throws XMLStreamException {
/* 115 */     this.streamWriter.writeComment(comment);
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {}
/*     */   
/*     */   public void startEntity(String name) throws SAXException {}
/*     */   
/*     */   public void endEntity(String name) throws SAXException {}
/*     */   
/*     */   protected void skippedEntityInternal(String name) throws XMLStreamException {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/StaxStreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */