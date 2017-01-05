/*
 * PreviewPDFFilter.java
 *
 * Version: $Revision: 1.8 $
 *
 * Date: $Date: 2005/07/29 15:56:07 $
 *
 * Copyright (c) 2002-2005, Hewlett-Packard Company and Massachusetts
 * Institute of Technology.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of the Hewlett-Packard Company nor the name of the
 * Massachusetts Institute of Technology nor the names of their
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.dspace.app.mediafilter;

import org.apache.log4j.Logger;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.lang.Math;

import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.dspace.core.ConfigurationManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.collection.PdfCollection;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfEncryption;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.SimpleBookmark;

import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;



public class PreviewPDFFilter extends MediaFilter
{
    private static Logger log = Logger.getLogger(PreviewPDFFilter.class);

    public String getFilteredName(String oldFilename)
    {
        return oldFilename.substring(0, oldFilename.length()-3) + "preview.pdf";
    }

    /**
     * @return String bundle name
     *  
     */
    public String getBundleName()
    {
        return "ORIGINAL";
    }

    /**
     * @return String bitstreamformat
     */
    public String getFormatString()
    {
        return "Adobe PDF";
    }

    /**
     * @return String description
     */
    public String getDescription()
    {
        return "Generated Preview";
    }

    /**
     * @param source
     *            source input stream
     * 
     * @return InputStream the resulting input stream
     */
    public InputStream getDestinationStream(InputStream source)
            throws Exception
    {

        boolean security = ConfigurationManager.getBooleanProperty("preview.pdf.copysecurity", true);


        /* New ownew password assigning to the new file */
        String ownerpass = ConfigurationManager.getProperty("preview.pdf.ownerpass");
        String stamptext = ConfigurationManager.getProperty("preview.pdf.stamptext");
        String image_file = ConfigurationManager.getProperty("preview.pdf.image_file");
        String compara = ConfigurationManager.getProperty("preview.pdf.paginabranca");

        float maxpreviewsize = Integer.parseInt(ConfigurationManager.getProperty("preview.pdf.maxpreviewsize"));
        int previewpercentage = Integer.parseInt(ConfigurationManager.getProperty("preview.pdf.previewpercentage"));
        float previewsize=0;
        float filesize=0;
        int n_conj=1, n_pag_conj=0,n_pag_preview=0,pag_inic_conj=0;
        int n_max_pag_cong = Integer.parseInt(ConfigurationManager.getProperty("preview.pdf.maxpreviewnpagcong"));

        // File infile1 = new File(ConfigurationManager.getProperty("upload.temp.dir") + File.separator + "infile1.pdf");
        File infile = new File(ConfigurationManager.getProperty("upload.temp.dir") + File.separator + "infile.pdf");
        File outfile = new File(ConfigurationManager.getProperty("upload.temp.dir") + File.separator + "outfile.pdf");
        File outfile1 = new File(ConfigurationManager.getProperty("upload.temp.dir") + File.separator + "blank.pdf");
        FileOutputStream byteout1 = new FileOutputStream(outfile1);
        PrintWriter logfile = new PrintWriter(ConfigurationManager.getProperty("upload.temp.dir") + File.separator + "logfile.txt");
        logfile.println(image_file);
        BufferedInputStream bis = new BufferedInputStream(source);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(infile));
        int b=0;
        while((b=bis.read()) > -1)
        {
            	bos.write(b);
        }
        bos.close();
        bis.close();
        // manipulatePdf(infile.getCanonicalPath(),image_file,infile1.getCanonicalPath(),logfile);
    	filesize = infile.length();
        log.error("filesize "+String.valueOf(filesize));
        previewsize = filesize*previewpercentage/100;
        log.error("previewpercentage "+String.valueOf(previewpercentage));
        log.error("maxpreviewsize "+String.valueOf(maxpreviewsize));
        log.error("previewsize "+String.valueOf(previewsize));
        if (previewsize>maxpreviewsize){
                float intm = (maxpreviewsize*100/filesize);
                log.error("intm ="+String.valueOf(intm));
                previewpercentage=(int) intm;
        }
         //log.error("previewpercentage calculado ="+String.valueOf(previewpercentage));
            FileOutputStream byteout = new FileOutputStream(outfile);
            ArrayList<HashMap<String, Object>> master = new ArrayList<HashMap<String, Object>>();
            PdfReader  reader = null;
            int   permissions = 0;
            char      version = '4';
            Document document = null;
            
            // this version doese not support portfolio pdf

            reader = new PdfReader(infile.getCanonicalPath());
            PdfReader reader1 = new PdfReader(infile.getCanonicalPath());
            PdfReaderContentParser parser = new PdfReaderContentParser(reader1);
            boolean isPortfolio = reader.getCatalog().contains(PdfName.COLLECTION);
            reader.close();

           byte[] password = (ownerpass != null ? ownerpass.getBytes() : null);
           if (security)
            {
               reader = new PdfReader(infile.getCanonicalPath(), password);
               permissions = reader.getPermissions();
               version = reader.getPdfVersion();
               reader.close();
            }


           reader = new PdfReader(infile.getCanonicalPath(), password);
           reader.consolidateNamedDestinations();
           document = new Document(reader.getPageSizeWithRotation(1));
           float bottom=reader.getPageSizeWithRotation(1).getBorderWidthBottom()+8.0f;
           // log.error("bottom - "+String.valueOf(bottom));
           //logfile.print("bottom - "+String.valueOf(bottom));
           float left = reader.getPageSizeWithRotation(1).getBorderWidthLeft()+8.0f;
           //logfile.print("letf - "+String.valueOf(left));
           //log.error("letf - "+String.valueOf(left));
           Document blank = new Document(reader.getPageSizeWithRotation(1));
           PdfWriter writer1 = PdfWriter.getInstance(blank, byteout1);
           blank.open();

           // step 2: we create a writer that listens to the document
           
           PdfCopy writer = new PdfCopy(document, byteout);
           // PdfStamper stp = new PdfStamper(reader,byteout);

           if (version == '5')
             writer.setPdfVersion(PdfWriter.VERSION_1_5);
           else if (version == '6')
             writer.setPdfVersion(PdfWriter.VERSION_1_6);
           else if (version == '7')
             writer.setPdfVersion(PdfWriter.VERSION_1_7);
           else
             ;

           if (security && permissions != 0)
           {
               writer.setEncryption(null, password, permissions, PdfWriter.STANDARD_ENCRYPTION_128);
           }
           else if (!security)
           {

               writer.setEncryption(null, password, PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128);
           }
           // step 3: we open the document
           document.open();
        
           if (isPortfolio)  {

           }
           int n = reader.getNumberOfPages();
           n_pag_preview = previewpercentage*n/100;
           log.error("N paginas = " + String.valueOf(n) + "  Preview = " + String.valueOf(n_pag_preview));
           if (n<=50) {
               n_conj=1;
               if (n<=10) n_pag_preview=1;
               else {
                  n_pag_preview=(int) n/10+1;
               }
           //    logfile.println("N paginas = " + String.valueOf(n) + "  Preview = " + String.valueOf(n_pag_preview));
           //    log.error("N paginas = " + String.valueOf(n) + "  Preview = " + String.valueOf(n_pag_preview));
           }
           else {
           n_pag_preview = (int) previewpercentage*n/100;
           int ordem = (int) n_pag_preview/10;
           switch (ordem) {
               case 0: n_conj=1;
                       break;
               case 1: n_conj=2;
                       break;
               case 2: n_conj=3;
                       break;
               case 3: n_conj=4;
                       break;
               default: n_conj=5;
                        if (n_pag_preview>50) {
                            n_pag_preview=50;
                        }
                        break;
               
           }
        }


           
           blank.add(new Paragraph("As paginas " + String.valueOf(2) + " a "  + String.valueOf(3) + " não estão incluídas nesta previsualização."));
           
           blank.close();
           writer1.close();
           // criacao do preview
           PdfImportedPage page;
           PdfCopy.PageStamp stamp;

           if (n_conj==1){
               // Trata previews de docs pequenos
               logfile.println("Docs pequenos  Preview = " + String.valueOf(n_pag_preview));
               log.error("Docs pequenos  Preview = " + String.valueOf(n_pag_preview));
               for (int j=1;j<=n_pag_preview;) {
                   if (!pagina_em_branco(parser,j,logfile,compara)) {
                       page = writer.getImportedPage(reader, j);
                       stamp = writer.createPageStamp(page);
                       ColumnText.showTextAligned(stamp.getOverContent(), Element.ALIGN_LEFT, new Phrase(stamptext,FontFactory.getFont(FontFactory.HELVETICA, 14,Font.BOLD, new BaseColor(156,161,167))),bottom,left,0);
                       stamp.alterContents();
                        writer.addPage(page);
                   }
                   j++;
               }
           }
           else {
               // documentos com mais de 50 paginas ou com mais de 10 de preview
               n_pag_conj=(int) n_pag_preview/n_conj;

               int pag_inic = 1;
               pag_inic_conj=1;
               // encontra a 1a pagina com conteudo
               if (pagina_em_branco(parser,pag_inic,logfile,compara)) {
                   for (int k=1;k<=n;k++) {
                     if (!pagina_em_branco(parser,k,logfile,compara)){
                           pag_inic = k;
                            break;
                     }
                   }
                }
                 // avanca 2 paginas a seguir a inicial
                  for (int k=pag_inic+2;k<=n;k++) {
                   if (!pagina_em_branco(parser,k,logfile,compara)) {
                         pag_inic_conj = k;
                         break;
                   }
                  }
               // Criacao do preview
               int p_m_intervalo = (int) (n-1-pag_inic_conj-2*n_pag_conj)/n_conj-1;

               logfile.println("Docs grandes  Preview = " + String.valueOf(n_pag_preview));
               log.error("Docs grandes  Preview = " + String.valueOf(n_pag_preview));
               logfile.println(" pag_inic = " + String.valueOf(pag_inic));
                log.error(" pag_inic = " + String.valueOf(pag_inic));
               logfile.println(" pag_inic_conj = " + String.valueOf(pag_inic_conj));
               log.error(" pag_inic_conj = " + String.valueOf(pag_inic_conj));
               logfile.println(" n_conj = " + String.valueOf(n_conj));
               log.error(" n_conj = " + String.valueOf(n_conj));
               logfile.println(" p_m_intervalo = " + String.valueOf(p_m_intervalo));
               log.error(" p_m_intervalo = " + String.valueOf(p_m_intervalo));
               logfile.println(" Primeira pagina " );
               log.error(" Primeira pagina " );
               // Primeira pagina
               page = writer.getImportedPage(reader, pag_inic);
               stamp = writer.createPageStamp(page);
               ColumnText.showTextAligned(stamp.getOverContent(), Element.ALIGN_LEFT, new Phrase(stamptext,FontFactory.getFont(FontFactory.HELVETICA, 14,Font.BOLD, new BaseColor(156,161,167))),bottom,left,0);
               stamp.alterContents();
               writer.addPage(page);
               // Primeiro conjunto
               int fim_conj = n_pag_conj+pag_inic_conj;
               logfile.println(" Primeiro conjunto " );
               log.error(" Primeiro conjunto " );
               logfile.println(" fim_conj = " + String.valueOf(fim_conj));
               log.error(" fim_conj = " + String.valueOf(fim_conj));
               for (int l=pag_inic_conj;l<=fim_conj;l++) {
                   if (!pagina_em_branco(parser,l,logfile,compara)) {
                       page = writer.getImportedPage(reader, l);
                       stamp = writer.createPageStamp(page);
                       ColumnText.showTextAligned(stamp.getOverContent(), Element.ALIGN_LEFT, new Phrase(stamptext,FontFactory.getFont(FontFactory.HELVETICA, 14,Font.BOLD, new BaseColor(156,161,167))),bottom,left,0);
                       stamp.alterContents();
                       writer.addPage(page);
                   }
               }

               // Conjuntos 2,3,4 (desde que nao sejam o ultimo)
               logfile.println(" Segundo conjunto " );
               log.error(" Segundo conjunto " );
               for (int m=2;m<n_conj;m++){
                 pag_inic_conj=pag_inic_conj+n_pag_conj+p_m_intervalo-(int) Math.round(n_pag_conj/2);
               logfile.println(" pag_inic_conj = " + String.valueOf(pag_inic_conj));
               log.error(" pag_inic_conj = " + String.valueOf(pag_inic_conj));
                 fim_conj=pag_inic_conj+n_pag_conj;
               logfile.println(" fim_conj = " + String.valueOf(fim_conj));
               log.error(" fim_conj = " + String.valueOf(fim_conj));
                 for (int k=pag_inic_conj;k<fim_conj;k++) {
                   if (!pagina_em_branco(parser,k,logfile,compara)) {
                       page = writer.getImportedPage(reader, k);
                       stamp = writer.createPageStamp(page);
                       ColumnText.showTextAligned(stamp.getOverContent(), Element.ALIGN_LEFT, new Phrase(stamptext,FontFactory.getFont(FontFactory.HELVETICA, 14,Font.BOLD, new BaseColor(156,161,167))),bottom,left,0);
                       stamp.alterContents();
                       writer.addPage(page);
                   }
               }
                }
               // Ultimo conjunto
               pag_inic_conj=n-n_pag_conj;
               logfile.println(" Ultimo conjunto " + String.valueOf(pag_inic_conj) );
               log.error(" Ultimo conjunto " + String.valueOf(pag_inic_conj) );
                 for (int k=pag_inic_conj;k<n;k++) {
                   if (!pagina_em_branco(parser,k,logfile,compara)) {
                       page = writer.getImportedPage(reader, k);
                       stamp = writer.createPageStamp(page);
                       ColumnText.showTextAligned(stamp.getOverContent(), Element.ALIGN_LEFT, new Phrase(stamptext,FontFactory.getFont(FontFactory.HELVETICA, 14,Font.BOLD, new BaseColor(156,161,167))),bottom,left,0);
                       stamp.alterContents();
                       writer.addPage(page);
                   }
               }

              }  // else

           PRAcroForm form = reader.getAcroForm();
           if (form !=null) {
               writer.copyAcroForm(reader);
           }

            //////////////////////////////////////////
           reader.close();
           document.close();
           writer.close();
           // stp.close();
           logfile.close();
            return new FileInputStream(outfile);
    }

    public boolean pagina_em_branco(PdfReaderContentParser parser,int pagenumber, PrintWriter logfile, String compara) throws IOException {
        boolean pb = false;

        String pagina = null;
        
        TextExtractionStrategy strategy;
        strategy = parser.processContent(pagenumber, new SimpleTextExtractionStrategy());
            pagina = strategy.getResultantText();
            logfile.print(pagina);
            if (pagina.contains(compara)) {
                logfile.println("*****VERDADEIRO*****");
                pb = true;
            }
            else
                logfile.println();

        return pb;
    }

public void manipulatePdf(String src, String image_file, String out_file,PrintWriter logfile) throws IOException, DocumentException {
        // Read the file
        PdfReader reader = new PdfReader(src);
        int n = reader.getXrefSize();
        PdfObject object;
        PRStream stream;
        PdfImageObject image1 = null;
        PdfName filter = null;
        // Recolhe a imagem de substituicao
        PdfReader reader1 = new PdfReader(image_file);

        int m = reader1.getXrefSize();
        logfile.println(" Objectos de imagem de substituicao: " + String.valueOf(m)   );
        PdfObject object1;
        PRStream stream1;
        BufferedImage bi1 = null;
        int width1 = 0,height1 = 0;
        for (int k = 0; k < m; k++) {
            object1 = reader1.getPdfObject(k);
            logfile.print(k);

            if (object1 == null || !object1.isStream())
            {
                logfile.println(": Objecto nulo");
                continue;
            }
            logfile.println(": Objecto nao nulo");
                stream1 = (PRStream) object1;
            PdfObject pdfsubtype1 = stream1.get(PdfName.SUBTYPE);
            PdfObject pdfsubtype2 = stream1.get(PdfName.TYPE);
            if (pdfsubtype2 != null) logfile.println("Subtipo:" + pdfsubtype2.toString());
            if (pdfsubtype1 != null) {
                logfile.println("Subtipo:" + pdfsubtype1.toString());
            if (pdfsubtype1.toString().equals(PdfName.IMAGE.toString())) {
                image1 = new PdfImageObject(stream1);
                filter = (PdfName) image1.get(PdfName.FILTER);
                logfile.println("Filtro Jpeg:" + filter);
                bi1 = image1.getBufferedImage();
                if (bi1 == null) {
                    logfile.println("+++++ Null Image +++++");
                       continue;
                }
                width1 = (int) bi1.getWidth();
                height1 = (int) bi1.getHeight();
                logfile.print(" WIDTH:");
                logfile.println(width1);
                }
            }
            else
                logfile.println("Subtipo NULO");
            }
        // Look for image and manipulate image stream
        for (int i = 0; i < n; i++) {
            object = reader.getPdfObject(i);

            if (object == null || !object.isStream())
                continue;
            logfile.print("********Novo objecto *******  " + String.valueOf(i));

            stream = (PRStream) object;
            PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
            if (pdfsubtype != null) {
                logfile.println("Subtipo:" + pdfsubtype.toString());
            if (pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
                PdfImageObject image = new PdfImageObject(stream);

                BufferedImage bi = image.getBufferedImage();
                if (bi == null) continue;
                int width = (int) bi.getWidth();
                int height = (int) bi.getHeight();
                float FACTORX = (float) width/width1;
                float FACTORY = (float) height/height1;

                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                
                AffineTransform at = AffineTransform.getScaleInstance(FACTORX, FACTORY);


                Graphics2D g = img.createGraphics();
                
                if (bi1 == null) continue;
                g.drawRenderedImage(bi1, at);
                //g.drawImage(img, op, 0, 0);
                ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                ImageIO.write(img, "JPG", imgBytes);
                stream.clear();
                stream.setData(imgBytes.toByteArray(), false, PRStream.NO_COMPRESSION);
                stream.put(PdfName.TYPE, PdfName.XOBJECT);
                stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
                // stream.put(key, value);
                stream.put(PdfName.FILTER, filter);
                stream.put(PdfName.WIDTH, new PdfNumber(bi1.getWidth()));
                stream.put(PdfName.HEIGHT, new PdfNumber(bi1.getHeight()));
                stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
            }
            }
        }
        // Save altered PDF
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(out_file));
        stamper.close();
    }




}
