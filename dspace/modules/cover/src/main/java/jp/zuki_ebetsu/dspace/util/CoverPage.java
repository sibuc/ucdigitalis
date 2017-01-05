/*
 * CoverPage.java
 *
 * Version: 1.1
 *
 * Date: 2010-11-30
 *
 * Copyright (c) 2010, Keiji Suzuki, All rights reserved.
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
package jp.zuki_ebetsu.dspace.util;

import com.itextpdf.text.BaseColor;
import org.apache.log4j.Logger;
import org.dspace.content.Item;
import org.dspace.content.Bundle;
import org.dspace.content.Bitstream;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.core.Constants;
import org.dspace.core.I18nUtil;
import org.dspace.content.DCDate;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.Bundle;
import org.dspace.content.Bitstream;
import org.dspace.content.Community;
import org.dspace.handle.HandleManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;

import com.itextpdf.text.pdf.PdfEncodings;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfConcatenate;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfEncryption;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.SimpleBookmark;
import com.itextpdf.text.pdf.collection.PdfCollection;

/**
 * Cover Page
 * 
 * @author Keiji Suzuki
 * @version 1.1
 */
public class CoverPage {

    private static Logger log = Logger.getLogger(CoverPage.class);

    /** header text */
    protected static final String header_text = ConfigurationManager.getProperty("coverpage.header");

    /** footer text */
    private String footer_text = ConfigurationManager.getProperty("coverpage.footer");
    private String footer_text2 = null;

    /** header logo image path */
    private static final String logo_path   = ConfigurationManager.getProperty("coverpage.logo");

    /** footer logo image path */
    private static final String logo_footer_path  = ConfigurationManager.getProperty("coverpage.logo.footer");

    private static final String logo_footer_impactum_path  = ConfigurationManager.getProperty("coverpage.logo.footer.impactum");

    private static final String logo_footer_almamater_path  = ConfigurationManager.getProperty("coverpage.logo.footer.almamater");

    /** Whether does security parameters of original pass to the new file? */
    private static final boolean security = ConfigurationManager.getBooleanProperty("coverpage.copysecurity", true);
    
    /** New ownew password assigning to the new file */
    private static final String ownerpass = ConfigurationManager.getProperty("coverpage.ownerpass");
    /** whether does it use cjk font? */
    private static final boolean cjk = ConfigurationManager.getBooleanProperty("coverpage.use_cjk", false);

    /** BaseFont family: sams-serif (gochic)*/
    private static final BaseFont FONT_SANS = setBaseFont("coverpage.font.sans");

    /** BaseFont family: serif (mincho)*/
    private static final BaseFont FONT_SERIF = setBaseFont("coverpage.font.serif");

    /** Header Font */
    protected static final Font FONT_HEADER = setFont(FONT_SANS, Font.FontFamily.HELVETICA, 15, Font.NORMAL);

    /** Footer Font */
    protected static final Font FONT_FOOTER = setFont(FONT_SANS, Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    protected static final Font FONT_PARAGRAPH = setFont(FONT_SANS, Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    /** Cell Tag Font */
    private static final Font FONT_TAG    = setFont(FONT_SANS, Font.FontFamily.HELVETICA, 12, Font.BOLD);

    /** Cell Value Font */
    private static final Font FONT_VALUE  = setFont(FONT_SANS, Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    /** Date Font */
    private static final Font FONT_DATE   = setFont(FONT_SANS, Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    /** Message Font */
    private static final Font FONT_MESS    = setFont(FONT_SERIF, Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

    /** Display fields */
    private static String[] fields;

    /** context */
    private Context context;

    /** item this bitstream is belonged */
    private Item item;
    
    /** bitstream to be added cover page  */
    private Bitstream bitstream;

    private String lang;

    private long size;

    private String name_bigPDF = null;

    public CoverPage(Context context, String lang, Item item, Bitstream bitstream)
    {
        this.context   = context;
        this.item      = item;
        this.bitstream = bitstream;
        this.lang      = lang;
    }

    public Long getsize()
    {
        return size;
    }

    public String name_bigPDF()
    {
        return name_bigPDF;
    }
   // public String biggetConcatenatePDF()
    public  String biggetConcatenatePDF()
    {

        InputStream coverStream = null;
        String bigPDF_name = null;
        Document document = null;
        PdfCopy writer = null;
       
        Calendar rightNow = Calendar.getInstance();
        
        int ano = rightNow.get(rightNow.YEAR);
        int mes = rightNow.get(rightNow.MONTH);
        int dia = rightNow.get(rightNow.DAY_OF_MONTH);
        int horas = rightNow.get(rightNow.HOUR_OF_DAY);
        int minuto = rightNow.get(rightNow.MINUTE);
        int segundo = rightNow.get(rightNow.SECOND);
        int milisegundos = rightNow.get(rightNow.MILLISECOND);
        String hora = String.valueOf(horas) + String.valueOf(minuto) + String.valueOf(segundo) + String.valueOf(milisegundos);
        log.info(LogManager.getHeader(context, "HORA", hora));
        if (bitstream == null)
            return null;

        if (item == null)
        {
            item = getItem();
            if (item == null)
                return null;
        }
                
        bigPDF_name = ConfigurationManager.getProperty("coverpage.pdf.temp.dir") + File.separator + String.valueOf(ano)+String.valueOf(mes+1)+String.valueOf(dia)+hora +"outfile.pdf";

        coverStream = getCoverStream();
        // log.info(LogManager.getHeader(context, "Depois getCover","- biggetConcatenatedPDF"));
        FileOutputStream byteout = null;
        File file = null;
        if (coverStream == null) 
            return null;
        try {
            file = new File(bigPDF_name);
            byteout = new FileOutputStream(file);
            }
        catch (IOException e1) {
            log.info(LogManager.getHeader(context, "cover_page: ERRO biggetConcatenatePDF", "bitstream_id="+bitstream.getID()+", error="+e1.getMessage()));
            e1.printStackTrace();
            return null;
        }
        
        try
        {
            int pageOffset = 0;
            ArrayList<HashMap<String, Object>> master = new ArrayList<HashMap<String, Object>>();
            PdfReader  reader = null;
            int   permissions = 0;
            char      version = '4';

            // this version doese not support portfolio pdf
            reader = new PdfReader(bitstream.retrieve());
            boolean isPortfolio = reader.getCatalog().contains(PdfName.COLLECTION);
            reader.close();

            byte[] password = (ownerpass != null ? ownerpass.getBytes() : null);

            // Copy encrypt from original pdf
            if (security) 
            {
               reader = new PdfReader(bitstream.retrieve(), password);
               permissions = reader.getPermissions();
               version = reader.getPdfVersion();
               reader.close();
            }

            InputStream[] is = new InputStream[2];
            is[0] = coverStream;
            is[1] = bitstream.retrieve();

            for (int i = 0; i < is.length; i++) 
            {
                // we create a reader for a certain document
                reader = new PdfReader(is[i], password);
                reader.consolidateNamedDestinations();
        
                if (i == 0) 
                {
                    // step 1: creation of a document-object
                    document = new Document(reader.getPageSizeWithRotation(1));
                    // step 2: we create a writer that listens to the document
                    writer = new PdfCopy(document, byteout);

                    if (version == '5')
                        writer.setPdfVersion(PdfWriter.VERSION_1_5);
                    else if (version == '6')
                        writer.setPdfVersion(PdfWriter.VERSION_1_6);
                    else if (version == '7')
                        writer.setPdfVersion(PdfWriter.VERSION_1_7);
                    else
                        ;  // no operation
                        
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
               
                    // if this pdf is portfolio, does not add cover page
                    if (isPortfolio)
                    {
                        reader.close();
                        byte[] coverByte = getCoverByte();
                        if (coverByte == null || coverByte.length == 0) 
                            return null;
                        PdfCollection collection = new PdfCollection(PdfCollection.TILE);
                        writer.setCollection(collection);

                        PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(writer, null, "cover.pdf", coverByte);
                        fs.addDescription("cover.pdf", false);
                        writer.addFileAttachment(fs);
                        continue;
                    }
                }
                int n = reader.getNumberOfPages();
                // step 4: we add content
                PdfImportedPage page;
                for (int j = 0; j < n; ) 
                {
                    ++j;
                    page = writer.getImportedPage(reader, j);
                    writer.addPage(page);
                }
                PRAcroForm form = reader.getAcroForm();
                if (form != null)
                {
                    writer.copyAcroForm(reader);
                }
                // we retrieve the total number of pages
                List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);
                if (bookmarks != null) 
                {
                    if (pageOffset != 0)
                    {
                        SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                    }
                    master.addAll(bookmarks);
                }
                pageOffset += n;
            }
            if (!master.isEmpty())
            {
                writer.setOutlines(master);
            }

            if (isPortfolio)
            {
                reader = new PdfReader(bitstream.retrieve(), password);
                PdfDictionary catalog = reader.getCatalog();
                PdfDictionary documentnames = catalog.getAsDict(PdfName.NAMES);
                PdfDictionary embeddedfiles = documentnames.getAsDict(PdfName.EMBEDDEDFILES);
                PdfArray filespecs = embeddedfiles.getAsArray(PdfName.NAMES);
                PdfDictionary filespec;
                PdfDictionary refs;
                PRStream stream;
                PdfFileSpecification fs;
                String path;
                // copy embedded files
                for (int i = 0; i < filespecs.size(); ) 
                {
                    filespecs.getAsString(i++);     // remove description
                    filespec = filespecs.getAsDict(i++);
                    refs = filespec.getAsDict(PdfName.EF);
                    for (PdfName key : refs.getKeys()) 
                    {
                        stream = (PRStream) PdfReader.getPdfObject(refs.getAsIndirectObject(key));
                        path = filespec.getAsString(key).toString();
                        fs = PdfFileSpecification.fileEmbedded(writer, null, path, PdfReader.getStreamBytes(stream));
                        fs.addDescription(path, false);
                        writer.addFileAttachment(fs);
                    }
                }
            }

            // step 5: we close the document
            document.close();
            // log.info(LogManager.getHeader(context, "biggetConcatenated","- document.close"));
        }

        catch (Exception e) 
        {
            log.info(LogManager.getHeader(context, "document_close: getConcatenatePDF", "bitstream_id="+bitstream.getID()+", error="+e.getMessage()));
            e.printStackTrace();
            return null;
        }
        try {
        byteout.close();
        }
        catch (Exception d)
        {
            log.info(LogManager.getHeader(context, "byteout_close: getConcatenatePDF", "bitstream_id="+bitstream.getID()+", error="+d.getMessage())); 
            d.printStackTrace();
            return null;
        }
        size = (long) file.length() ;
        // return byteout;
        // log.info(LogManager.getHeader(context, "size: biggetConcatenatePDF ", size +", bigPDF_name " + bigPDF_name));
        return bigPDF_name;
    }
    /**
     * Concatenate cover page to the PDF file
     * 
     * @return the new PDF stream, or null if there is not COVER bundle or error occurs
     */
    public ByteArrayOutputStream getConcatenatePDF() 
    {
        if (bitstream == null)
            return null;

        if (item == null)
        {
            item = getItem();
            if (item == null)
                return null;
        }

        InputStream coverStream = getCoverStream();
        if (coverStream == null) 
            return null;
        

        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        try
        {
            int pageOffset = 0;
            ArrayList<HashMap<String, Object>> master = new ArrayList<HashMap<String, Object>>();
    
            Document document = null;
            PdfCopy    writer = null;
            PdfReader  reader = null;
            int   permissions = 0;
            char      version = '4';

            // this version doese not support portfolio pdf
            reader = new PdfReader(bitstream.retrieve());
            boolean isPortfolio = reader.getCatalog().contains(PdfName.COLLECTION);
            reader.close();

            byte[] password = (ownerpass != null ? ownerpass.getBytes() : null);

            // Copy encrypt from original pdf
            if (security) 
            {
               reader = new PdfReader(bitstream.retrieve(), password);
               permissions = reader.getPermissions();
               version = reader.getPdfVersion();
               reader.close();
            }

            InputStream[] is = new InputStream[2];
            is[0] = coverStream;
            is[1] = bitstream.retrieve();

            for (int i = 0; i < is.length; i++) 
            {
                // we create a reader for a certain document
                reader = new PdfReader(is[i], password);
                reader.consolidateNamedDestinations();
        
                if (i == 0) 
                {
                    // step 1: creation of a document-object
                    document = new Document(reader.getPageSizeWithRotation(1));
                    // step 2: we create a writer that listens to the document
                    writer = new PdfCopy(document, byteout);

                    if (version == '5')
                        writer.setPdfVersion(PdfWriter.VERSION_1_5);
                    else if (version == '6')
                        writer.setPdfVersion(PdfWriter.VERSION_1_6);
                    else if (version == '7')
                        writer.setPdfVersion(PdfWriter.VERSION_1_7);
                    else
                        ;  // no operation
                        
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
               
                    // if this pdf is portfolio, does not add cover page
                    if (isPortfolio)
                    {
                        reader.close();
                        byte[] coverByte = getCoverByte();
                        if (coverByte == null || coverByte.length == 0) 
                            return null;
                        PdfCollection collection = new PdfCollection(PdfCollection.TILE);
                        writer.setCollection(collection);

                        PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(writer, null, "cover.pdf", coverByte);
                        fs.addDescription("cover.pdf", false);
                        writer.addFileAttachment(fs);
                        continue;
                    }
                }
                int n = reader.getNumberOfPages();
                // step 4: we add content
                PdfImportedPage page;
                for (int j = 0; j < n; ) 
                {
                    ++j;
                    page = writer.getImportedPage(reader, j);
                    writer.addPage(page);
                }
                PRAcroForm form = reader.getAcroForm();
                if (form != null)
                {
                    writer.copyAcroForm(reader);
                }
                // we retrieve the total number of pages
                List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);
                if (bookmarks != null) 
                {
                    if (pageOffset != 0)
                    {
                        SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                    }
                    master.addAll(bookmarks);
                }
                pageOffset += n;
            }
            if (!master.isEmpty())
            {
                writer.setOutlines(master);
            }

            if (isPortfolio)
            {
                reader = new PdfReader(bitstream.retrieve(), password);
                PdfDictionary catalog = reader.getCatalog();
                PdfDictionary documentnames = catalog.getAsDict(PdfName.NAMES);
                PdfDictionary embeddedfiles = documentnames.getAsDict(PdfName.EMBEDDEDFILES);
                PdfArray filespecs = embeddedfiles.getAsArray(PdfName.NAMES);
                PdfDictionary filespec;
                PdfDictionary refs;
                PRStream stream;
                PdfFileSpecification fs;
                String path;
                // copy embedded files
                for (int i = 0; i < filespecs.size(); ) 
                {
                    filespecs.getAsString(i++);     // remove description
                    filespec = filespecs.getAsDict(i++);
                    refs = filespec.getAsDict(PdfName.EF);
                    for (PdfName key : refs.getKeys()) 
                    {
                        stream = (PRStream) PdfReader.getPdfObject(refs.getAsIndirectObject(key));
                        path = filespec.getAsString(key).toString();
                        fs = PdfFileSpecification.fileEmbedded(writer, null, path, PdfReader.getStreamBytes(stream));
                        fs.addDescription(path, false);
                        writer.addFileAttachment(fs);
                    }
                }
            }

            // step 5: we close the document
            document.close();
        } 
        catch (Exception e) 
        {
            log.info(LogManager.getHeader(context, "cover_page: getConcatenatePDF", "bitstream_id="+bitstream.getID()+", error="+e.getMessage())); 
            e.printStackTrace();
            return null;
        }

        size = (long) byteout.size();
        return byteout;
    }

    /**
     * 
     * @return InputStream the resulting output stream
     */
    private InputStream getCoverStream()
    {
        ByteArrayOutputStream byteout = getCover();
        return new ByteArrayInputStream(byteout.toByteArray());
    }

    /**
     * 
     * @return InputStream the resulting output stream
     */
    private byte[] getCoverByte()
    {
        ByteArrayOutputStream byteout = getCover();
        return byteout.toByteArray();
    }

    /**
     * 
     * @return InputStream the resulting output stream
     */
    private ByteArrayOutputStream getCover()
    {
        ByteArrayOutputStream byteout;
        Document doc = null;
        Image img1 = null;
        Image img2 = null;
        String logo_path2 = logo_footer_path;

        String paragraph1, paragraph2, paragraph3, paragraph4, paragraph5;
       
        Locale locale = I18nUtil.getDefaultLocale();

        log.info("Locale = " + locale);

        if (lang != null && !lang.isEmpty()) {
            if (lang.equalsIgnoreCase("pt-pt"))
                locale = new Locale("pt");
            else
              if (lang.equalsIgnoreCase("es"))
                  locale = new Locale("es");
              else
                  locale = new Locale("en");
        }

        log.info("Locale = " + locale);
        log.info("Lang = " + lang);

        paragraph1 = I18nUtil.getMessage("jsp.coverpage.paragraph1", locale);
        paragraph2 = I18nUtil.getMessage("jsp.coverpage.paragraph2", locale);
        paragraph3 = I18nUtil.getMessage("jsp.coverpage.paragraph3", locale);
        paragraph4 = I18nUtil.getMessage("jsp.coverpage.paragraph4", locale);
        paragraph5 = I18nUtil.getMessage("jsp.coverpage.paragraph5", locale);
        
        log.info("Fim recolha paragrafos");
        // Set display metadata fields
        ArrayList<String> clist = new ArrayList<String>();
        int n_fields = ConfigurationManager.getIntProperty("coverpage.fields",5);
        for (int i = 1; i <= n_fields; i++)
        {
            clist.add(I18nUtil.getMessage("jsp.coverpage.field." + i, locale));
        }
        if (clist.size() > 0)
        {
            fields = (String[]) clist.toArray(new String[clist.size()]);
        }
        else
        {
            fields = new String[]{ "Title:dc.title", 
                                   "Author(s):dc.contributor.author",
                                   "Citation:dc.identifier.citation",
                                   "Issue Date:dc.date.issued",
                                   "Doc URL:dc.identifier.uri",
                                   "DOI:dc.identifier.doi" };
        }
        try 
        {
            byteout = new ByteArrayOutputStream();
            doc = new Document(PageSize.A4, 20, 20, 50, 50);
            PdfWriter pdfwriter = PdfWriter.getInstance(doc, byteout);
            pdfwriter.setPageEvent(new HeaderFooter());
            doc.open(); 
            int img_id=0;
            String url;
            Community comm;
            url = ConfigurationManager.getProperty("dspace.baseUrl");
            log.info("dspace.baseUrl:" + url);
            if (logo_path != null && !logo_path.equals(""))
            {
                
                img1 = Image.getInstance(logo_path);
                //img.scalePercent(72.0f / 96.0f * 100f);

            }
            log.info(LogManager.getHeader(context, "Fim if", "logo_path"));
            // Procura logo da colecao
            if (item.getOwningCollection().getLogo()!=null) {
                img_id = item.getOwningCollection().getLogo().getID();
                log.info("Logo colecao="+img_id);
                if (img_id!=0) {
                //Bitstream bitstr = Bitstream.find(context, img_id);
                //String img_file = bitstr.getSource();
                  
                //img1 = Image.getInstance(img_file);
                img1 = Image.getInstance(url+"/retrieve/"+String.valueOf(img_id));
                logo_path2 = logo_footer_impactum_path;
                footer_text2 = ConfigurationManager.getProperty("coverpage.footer2");
                }
            }
            else {
                // procura logo da comunidade ou comunidade de topo que contem a coleccao
                if (item.getOwningCollection().getParentObject()!=null){
                 if (item.getOwningCollection().getParentObject().getType()==Constants.COMMUNITY) {
                    comm = (Community) item.getOwningCollection().getParentObject();
                    if (comm.getLogo()!=null){
                    img_id = comm.getLogo().getID();
                log.info("Logo comunidade="+img_id);
                    if (img_id!=0) {
                           //            Bitstream bitstr = Bitstream.find(context, img_id);
                //String img_file = bitstr.getSource();
                  
                //img1 = Image.getInstance(img_file);
                        img1 = Image.getInstance(url+"/retrieve/"+String.valueOf(img_id));
                       if (comm.getName().matches("POMBALINA")) {
                          footer_text2 = ConfigurationManager.getProperty("coverpage.footer1");
                       }
                       else 
                          if (comm.getName().matches("IMPACTVM")) {
                              logo_path2 = logo_footer_impactum_path;
                              footer_text2 = ConfigurationManager.getProperty("coverpage.footer2");
                          }
                          else 
                            if (comm.getName().matches("ALMAMATER")) {
                               logo_path2 = logo_footer_almamater_path;
                               footer_text2 = ConfigurationManager.getProperty("coverpage.footer3");
                          }
                    }
                    }
                    else 
                     if (item.getOwningCollection().getParentObject().getParentObject()!=null) {
                      if (item.getOwningCollection().getParentObject().getParentObject().getParentObject()!=null){
                         if (item.getOwningCollection().getParentObject().getParentObject().getParentObject().getType()==Constants.COMMUNITY) {
                           comm = (Community) item.getOwningCollection().getParentObject().getParentObject().getParentObject();
                           if (comm.getLogo()!=null){
                             img_id = comm.getLogo().getID();
                             if (img_id!=0) {
              	        	//  Bitstream bitstr = Bitstream.find(context, img_id);
      			        //  String img_file = bitstr.getSource();
                         	//	         img1 = Image.getInstance(img_file);
                               img1 = Image.getInstance(url+"/retrieve/"+String.valueOf(img_id));
                               if (comm.getName().matches("POMBALINA")) {
                                   footer_text2 = ConfigurationManager.getProperty("coverpage.footer1");
                               }
                               else 
                                if (comm.getName().matches("IMPACTVM")) {
                                   logo_path2 = logo_footer_impactum_path;
                                   footer_text2 = ConfigurationManager.getProperty("coverpage.footer2");
                                }
                              else 
                                  if (comm.getName().matches("ALMAMATER")) {
                                       logo_path2 = logo_footer_almamater_path;
                                       footer_text2 = ConfigurationManager.getProperty("coverpage.footer3");
                                  }
                             }
                           }
                        }
                    }
                   }
                  }
            }
            }
log.info(LogManager.getHeader(context, "Antes scalepercent", "logo_path"));
            img1.scalePercent(75f);
log.info(LogManager.getHeader(context, "Antes setAlignment", "logo_path"));
            img1.setAlignment(Element.ALIGN_CENTER);
            log.info(LogManager.getHeader(context, "Antes adicionar imagem", "logo_path"));
            doc.add(img1);
            doc.add(new Paragraph(""));
            
            Font data_font = FONT_VALUE;
            // alter value font if use cjk data
            if (cjk)
            {
                String language = getFieldValue("dc.language.iso");
                if (language != null)
                {
                   if (language.startsWith("ja")) 
                      data_font = new Font(BaseFont.createFont(
                          "HeiseiMin-W3", "UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED),12);
                   else if (language.startsWith("zh_TW")) 
                      data_font = new Font(BaseFont.createFont(
                          "MSung-Light", "UniCNS-UCS2-H",BaseFont.NOT_EMBEDDED),12);
                   else if (language.startsWith("zh")) 
                      data_font = new Font(BaseFont.createFont(
                          "STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED),12);
                   else if (language.startsWith("ko")) 
                       data_font = new Font(BaseFont.createFont(
                          "HYSMyeongJo-Medium", "UniKS-UCS2-H",BaseFont.NOT_EMBEDDED),12);
                } 
            }

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(90f);

            int table_width[] = {20,80};
            table.setWidths(table_width);
            table.setSpacingBefore(9f);

            for (int i=0, len=fields.length; i<len; i++)
            {
                String[] flds = fields[i].split(":");
                // Tag
                PdfPCell tag = new PdfPCell(new Phrase(flds[0]+": ", FONT_TAG));
                // tag.setGrayFill(0.8f);
                tag.setHorizontalAlignment(Element.ALIGN_LEFT);
                tag.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tag.setPaddingLeft(5f);
                tag.setBorder(0);
                if (i==0) {
                    tag.setBorderWidthTop(0.6f);
                    tag.setBorderColorTop(BaseColor.BLACK);
                } 
                PdfPCell value;
            if (i==0)  {
                 value = new PdfPCell(new Phrase(getFieldValue(flds[1]), FONT_TAG));
                }
                else {
                 value = new PdfPCell(new Phrase(getFieldValue(flds[1]), data_font));

                }
                value.setHorizontalAlignment(Element.ALIGN_LEFT);
                value.setVerticalAlignment(Element.ALIGN_MIDDLE);
                value.setMinimumHeight(20f);
                value.setPadding(5f);
                value.setBorder(0);
                if (i==0) {
                value.setColspan(2);
                value.setBorderWidthTop(0.8f);
                value.setBorderColor(BaseColor.BLACK);

                }
                else {
                  if (value.getPhrase().getContent().length()!=0) table.addCell(tag);
                }
                 if (value.getPhrase().getContent().length()!=0) table.addCell(value);
             //   log.info(LogManager.getHeader(context, "table.addCell - tag",tag.getPhrase().getContent()));
             //   log.info(LogManager.getHeader(context, "table.addCell - value",value.getPhrase().getContent()));
             //   log.info(LogManager.getHeader(context, "table.addCell - value - length",String.valueOf(value.getPhrase().getContent().length())));

            }
            PdfPCell access_tag = new PdfPCell(new Phrase("Accessed : ", FONT_TAG));
            access_tag.setHorizontalAlignment(Element.ALIGN_LEFT);
            access_tag.setVerticalAlignment(Element.ALIGN_MIDDLE);
            access_tag.setMinimumHeight(20f);
            access_tag.setPadding(5f);
            access_tag.setBorder(0);
            access_tag.setBorderWidthBottom(0.8f);
            access_tag.setBorderColorBottom(BaseColor.BLACK);
            // data de acesso ao documento
            String downTime = DCDate.getCurrent().displayDate(true, true, Locale.UK);
            PdfPCell access_value = new PdfPCell(new Phrase(downTime, data_font));
            access_value.setHorizontalAlignment(Element.ALIGN_LEFT);
            access_value.setVerticalAlignment(Element.ALIGN_MIDDLE);
            access_value.setMinimumHeight(30f);
            access_value.setPadding(5f);
            access_value.setBorder(0);
            access_value.setBorderWidthBottom(0.8f);
            access_value.setBorderColorBottom(BaseColor.BLACK);
            table.addCell(access_tag);
            table.addCell(access_value);

            doc.add(table);
            Paragraph p = new Paragraph(paragraph1,FONT_PARAGRAPH);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.setIndentationLeft(30);
            p.setIndentationRight(30);
            p.setSpacingBefore(12);
            addEmptyLine(p,2);
            p.add(paragraph2);
            addEmptyLine(p,2);
            p.add(paragraph3);
            addEmptyLine(p,2);
            p.add(paragraph4);
            addEmptyLine(p,2);
            // p.add(paragraph5);
            // addEmptyLine(p,2);
            doc.add(p);

            if (logo_path2 != null && !logo_path2.equals(""))
            {
                 p = new Paragraph("");
                 p.setSpacingBefore(50f);
                 doc.add(p);

                img2 = Image.getInstance(logo_path2);
                img2.scalePercent(50f);
                img2.setAlignment(Element.ALIGN_LEFT);
                img2.setIndentationLeft(30f);
                 log.info(LogManager.getHeader(context, "docBottomMargin: ", " " + doc.bottomMargin()));
                 img2.setAbsolutePosition(doc.leftMargin(), doc.bottomMargin()-10);
                doc.add(img2);
                doc.add(new Paragraph(""));
            }

            doc.close();
            return byteout; 
        } 
        catch (Exception e)
        {
            log.info(LogManager.getHeader(context, "cover_page", "bitstream_id="+bitstream.getID()+", error="+e.getLocalizedMessage()));
            return null;
        }
    }


    private Item getItem()
    {
        try 
        {
            Bundle[] bundles = bitstream.getBundles();
            Item item = null;
            for (int i = 0; i < bundles.length; i++) 
            {
                if (bundles[i].getName().equals("ORIGINAL")) 
                {
                    Item[] items = bundles[i].getItems();
                    for (int j = 0; j < items.length; j++) 
                    {
                        if (!items[j].isWithdrawn()) 
                        {
                            item = items[j];
                            break;
                        }
                    }
                }
            }
            
            return item;
        }
        catch (SQLException sqle)
        {
            log.info(LogManager.getHeader(context, "cover_page: getItem", "bitstream_id="+bitstream.getID()+", error="+sqle.getMessage())); 
            return null;
        }
    }

    private String getFieldValue(String field)
    {
        DCValue[] dcvalues = item.getMetadata(field);
        if (dcvalues.length == 0)
        {
            return "";
        }
        else if (dcvalues.length == 1)
        {
            return dcvalues[0].value;
        }
        else
        {
            StringBuffer sb = new StringBuffer();
            for (int i=0, len=dcvalues.length; i<len; i++)
                sb.append("; ").append(dcvalues[i].value);
            return sb.toString().substring(2);
        }
    }

    private static BaseFont setBaseFont(String property)
    {
        String info = ConfigurationManager.getProperty(property);
        if (info == null || info.trim().equals(""))
            return null;

        String[] finfo = info.split(",");
        if (finfo.length != 2)
            return null;

        String font     = finfo[0].trim();
        String encoding = finfo[1].trim();

        try
        {
            return BaseFont.createFont(font, encoding, BaseFont.NOT_EMBEDDED);
        }
        catch (Exception e)
        {
            log.error("setBaseFont error: " + e.getMessage());
            return null;
        }
    }

    private static Font setFont(BaseFont bs, Font.FontFamily family, int size, int style)
    {
        if (bs == null)
        {
            return new Font(family, size, style);
        }
        else
        {
            return new Font(bs, size, style);
        }
    }

    private class HeaderFooter extends PdfPageEventHelper
    {
        public void onEndPage(PdfWriter writer, Document doc)
        {
            // set header
            
            if (header_text != null && !header_text.equals("")) 
            {
                ColumnText.showTextAligned(writer.getDirectContent(), 
                    Element.ALIGN_CENTER,
                    new Phrase(CoverPage.header_text, CoverPage.FONT_HEADER),
                    (doc.right() - doc.left()) / 2 + doc.leftMargin(), 
                    doc.top() + 15, 
                    0);
            } 
            // set footer
            
            if (footer_text != null && !footer_text.equals("")) 
            {
                ColumnText.showTextAligned(writer.getDirectContent(), 
                    Element.ALIGN_LEFT,
                    new Phrase(footer_text, CoverPage.FONT_FOOTER),
                    (doc.right() - doc.left()) / 2, 
                    doc.bottom() - 35,
                    0);
            }
            if (footer_text2 != null && !footer_text2.equals(""))
            {
                ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT,
                    new Phrase(footer_text2, CoverPage.FONT_FOOTER),
                    (doc.right() - doc.left()) / 2,
                    doc.bottom() - 25,
                    0);
            }
        }
    }
private static void addEmptyLine(Paragraph paragraph, int number) {
    for (int i=0; i < number; i++) {
        paragraph.add(new Paragraph(" "));
    }
}

/**
	 * Lê o arquivo e coloca em um {@link ByteArrayInputStream}
	 *
	 * @param inFile arquivo recuperar dados
	 * @return ByteArrayInputStream com os dados do arquivo
	 * @throws Exception caso tenha erro na conversão
	 */
	public static ByteArrayInputStream fileToByteArrayInputStream(File inFile) throws Exception {
		FileInputStream fis = new FileInputStream(inFile);
		int tamanho = (int) inFile.length();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bytesLidos = 0;
		byte[] buffer = new byte[tamanho];
		while (bytesLidos < tamanho) {
			bytesLidos += fis.read(buffer, bytesLidos, tamanho - bytesLidos);
		}
		baos.write(buffer);
		return new ByteArrayInputStream(baos.toByteArray());
	}

        	/**
	 * Lê o arquivo e coloca em um {@link ByteArrayOutputStream}
	 *
	 * @param inFile arquivo recuperar dados
	 * @return ByteArrayOutputStream com os dados do arquivo
	 * @throws Exception caso ocorra erro na conversão
	 */
	public static ByteArrayOutputStream fileToByteArrayOutputStream(File inFile) throws Exception {
		FileInputStream fis = new FileInputStream(inFile);
		final int TAMANHO_BYTES_ARQUIVO = (int) inFile.length();
		final int TAMANHO_BUFFER = 1000;

		ByteArrayOutputStream baos = new ByteArrayOutputStream(TAMANHO_BYTES_ARQUIVO);
		int bytesLidos = 0, totalBytesLidos = 0;
		byte[] buffer = new byte[TAMANHO_BUFFER];

		while (totalBytesLidos < TAMANHO_BYTES_ARQUIVO) {
			bytesLidos = fis.read(buffer, 0, TAMANHO_BUFFER);
			if (bytesLidos == -1) {
				break;
			}
			baos.write(buffer, 0, bytesLidos);
			totalBytesLidos += bytesLidos;
		}
		fis.close();
		return baos;
	}

}
