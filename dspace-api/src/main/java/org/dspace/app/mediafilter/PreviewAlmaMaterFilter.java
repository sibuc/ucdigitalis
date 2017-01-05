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
import org.dspace.content.Bitstream;
import org.dspace.content.Item;
import org.dspace.core.Context;

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



public class PreviewAlmaMaterFilter extends MediaFilter
{
    private static Logger log = Logger.getLogger(PreviewPDFFilter.class);

    public String getFilteredName(String oldFilename)
    {
        return "Preview.pdf";
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
     *  Processa apenas o PDF que é obra completa
     */
    public boolean preProcessBitstream(Context c, Item item, Bitstream source)
         throws Exception
    {
      
       if (source.getName().contains("capa-lombada")||source.getName().contains("Obra")||source.getName().contains("0000") ) {
        System.out.println("Obra completa..." + source.getName());
        return true;
       }
       else
       {
        System.out.println("Não foi identificada Obra completa..." + source.getName());
        return false;
       }
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
        String stamptext1 = ConfigurationManager.getProperty("preview.pdf.stamptext1");
        String stamptext_final = stamptext;
        String image_file = ConfigurationManager.getProperty("preview.pdf.image_file");
        String compara = ConfigurationManager.getProperty("preview.pdf.paginabranca");

        float maxpreviewsize = Integer.parseInt(ConfigurationManager.getProperty("preview.pdf.maxpreviewsize"));
        int previewpercentage = Integer.parseInt(ConfigurationManager.getProperty("preview.pdf.previewpercentage"));
        float previewsize=0;
        float filesize=0;
        int n_pag_conj=0,n_pag_preview=0,pag_inic_conj=0;
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
        log.info("previewpercentage "+String.valueOf(previewpercentage));
        log.info("maxpreviewsize "+String.valueOf(maxpreviewsize));
        log.info("previewsize "+String.valueOf(previewsize));
        if (previewsize>maxpreviewsize){
                float intm = (maxpreviewsize*100/filesize);
                log.info("intm ="+String.valueOf(intm));
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
           log.info("bottom - "+String.valueOf(bottom));
           //logfile.print("bottom - "+String.valueOf(bottom));
           float left = reader.getPageSizeWithRotation(1).getBorderWidthLeft()+8.0f;
           //logfile.print("letf - "+String.valueOf(left));
           log.info("letf - "+String.valueOf(left));

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
           int n = reader.getNumberOfPages()-3;
           n_pag_preview = previewpercentage*n/100;
            log.info("N paginas = " + String.valueOf(n) + "  Preview = " + String.valueOf(n_pag_preview));
           if (n<=50){ 
               if (n<=10) {
                 if (n>=5) n_pag_preview=2;
                 else {
                        n_pag_preview=1;
                  }
                }
               else 
                  n_pag_preview=(int) n/10+1;
           }
           //    logfile.println("N paginas = " + String.valueOf(n) + "  Preview = " + String.valueOf(n_pag_preview));
           //    log.error("N paginas = " + String.valueOf(n) + "  Preview = " + String.valueOf(n_pag_preview));
           else {
               n_pag_preview=15;           
           }
       

          log.info("N paginas = " + String.valueOf(n) + "  Preview = " + String.valueOf(n_pag_preview));
           
           // criacao do preview
           PdfImportedPage page;
           PdfCopy.PageStamp stamp;
           // Todos os previews têm um máximo de 15 páginas
           logfile.println("Docs pequenos  Preview = " + String.valueOf(n_pag_preview));
           log.info("Docs pequenos  Preview = " + String.valueOf(n_pag_preview));
           int k=(int) n/2;
           if (n<=0) { 
                 k=1;
                 stamptext_final = stamptext1;
           }
           log.info("Stamp text final=" + stamptext_final);
           left = left + 10.0f;
           bottom = bottom + 50.0f;
           for (int j=1;j<=n_pag_preview;j++) {
           // if (!pagina_em_branco(parser,k,logfile,compara)) {
                       page = writer.getImportedPage(reader, k);
                       stamp = writer.createPageStamp(page);
                       double redim = page.getWidth()/210;
                       int font_size = (int) redim;
                       font_size = font_size*7;
			  log.info("redim   = " + String.valueOf(redim));
                       log.info("Font Size   = " + String.valueOf(font_size));
                     //  left = left + 50.0f;
                     //  bottom = bottom + 100.0f;
                       log.info("Docs pequenos  Texto = " + stamptext_final);
                      // if (n<=0)
                      // ColumnText.showTextAligned(stamp.getOverContent(), Element.ALIGN_MIDDLE, new Phrase(stamptext_final,FontFactory.getFont(FontFactory.HELVETICA, font_size,Font.BOLD, new BaseColor(156,161,167))),bottom,left,0);
                      // else 
                      ColumnText.showTextAligned(stamp.getOverContent(), Element.ALIGN_LEFT, new Phrase(stamptext_final,FontFactory.getFont(FontFactory.HELVETICA, font_size,Font.BOLD, new BaseColor(156,161,167))),bottom,left,0);
                    // ColumnText.showTextAligned(stamp.getOverContent(), Element.ALIGN_LEFT, new Phrase(stamptext,FontFactory.getFont(FontFactory.HELVETICA, 14,Font.BOLD, new BaseColor(156,161,167))),bottom,left,0);
                        stamp.alterContents();
                        writer.addPage(page);
           //        }
             k++;
           }
 
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
