/*
 * ValidateCollectionServlet.java
 *
 * Version: $Revision: 3705 $
 *
 * Date: $Date: 2009-04-11 17:02:24 +0000 (Sat, 11 Apr 2009) $
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
package org.dspace.app.webui.servlet;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.LinkedList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.text.SimpleDateFormat;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.itemexport.ItemExport;
import org.dspace.app.itemexport.ItemExportException;
import org.dspace.app.util.SubmissionConfigReader;
import org.dspace.app.util.SubmissionConfig;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DCValue;
import org.dspace.content.Bundle;
import org.dspace.content.Bitstream;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import org.dspace.content.SupervisedItem;
import org.dspace.content.WorkspaceItem;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.core.I18nUtil;
import org.dspace.core.Email;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.dspace.handle.HandleManager;
import org.dspace.submit.AbstractProcessingStep;
import org.dspace.workflow.WorkflowItem;
import org.dspace.workflow.WorkflowManager;

/**
 * Servlet for constructing the components of the "My DSpace" page
 * 
 * @author Robert Tansley
 * @author Jay Paz
 * @version $Id: MyDSpaceServlet.java 3705 2009-04-11 17:02:24Z mdiggory $
 */
public class ValidateCollectionServlet extends DSpaceServlet
{
    /** Logger */
    private static Logger log = Logger.getLogger(ValidateCollectionServlet.class);
    
    /** Definicao de campos dublin core a validar */


    public static final String[] Livros = { "dc.title", "dc.type", "dc.date.issued", "dc.identifier.doi",
                                            "dc.identifier.uri", "dc.publisher",
                                            "ucdigitalis.publication.pages", "ucdigitalis.publication.area"
                                          };

    public static final String[] CapLivros = { "dc.title", "dc.type", "dc.date.issued", "dc.identifier.doi",
                                            "dc.identifier.uri", "ucdigitalis.publication.pages",
                                            "ucdigitalis.publication.booktitle", "dc.relation.ispartof",
                                            "ucdigitalis.publication.title", "ucdigitalis.publication.area"
    };

    public static final String[] Artigos = { "dc.title", "dc.type", "dc.date.issued", "dc.identifier.doi",
                                            "dc.identifier.uri", "ucdigitalis.publication.pages",
                                            "ucdigitalis.publication.journaltitle",
                                            "ucdigitalis.publication.volume",
                                            "ucdigitalis.publication.title",
                                            "ucdigitalis.publication.issue", "ucdigitalis.publication.area"
    };

    protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // GET displays the main page - everthing else is a POST
        showMainPage(context, request, response);
     }
    
    protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {


            processMainPage(context, request, response);

           //  log.warn(LogManager.getHeader(context, "integrity_error", UIUtil
           //         .getRequestLogInfo(request)));
           // JSPManager.showIntegrityError(request, response);

    }

    // ****************************************************************
    // ****************************************************************
    // METHODS FOR PROCESSING POSTED FORMS
    // ****************************************************************
    // ****************************************************************
    protected void processMainPage(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        String buttonPressed = UIUtil.getSubmitButton(request, "submit");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");

        String checkbox = request.getParameter("tipodoc");

        String downloadDir = null;
        String fileName = null;

        log.info("Valor Checkbox " + checkbox);

        EPerson currentUser = context.getCurrentUser();

        Collection coll = Collection.find(context, Integer.parseInt(request
						.getParameter("collection_id")));
        
        int n_itens = coll.countItems();

        try {
              fileName = ItemExport.assembleFileNameValidate("item", currentUser,new Date());
              log.info("Filename " + fileName);
              downloadDir = ItemExport.getExportDownloadDirectory(currentUser
                                .getID());
              log.info("downloadDir" + downloadDir);
              File dnDir = new File(downloadDir);
              if (!dnDir.exists())
              {
                  dnDir.mkdirs();
              }
              log.info("Criou/verificou diretoria");
              ItemIterator items = coll.getAllItems();
              log.info("ItemIterator");

              FileWriter ficheiro = new FileWriter(downloadDir+System.getProperty("file.separator") + fileName + ".txt",true);
              log.info(" Criou ficheiro ");
              ficheiro.append("Validacao de dados da colecao " + coll.getName() + " -- " + coll.getHandle() + "\n");
              log.info("Validacao de dados da colecao " + coll.getName() + " -- " + coll.getHandle() + "\n");
              ficheiro.append("No de documentos:" + n_itens + "\n");
              log.info("No de documentos:" + n_itens + "\n");
              ficheiro.append("Utilizador:" + currentUser.getFullName() + "(" + currentUser.getEmail() +")\n");
              log.info("Utilizador:" + currentUser.getFullName() + "(" + currentUser.getEmail() +")\n");
              ficheiro.append("Data:" + sdf.format(new Date()) + "\n");
              log.info("Data:" + sdf.format(new Date()) + "\n");
              
              while (items.hasNext()) {

                  Item item = items.next();
                  ficheiro.append("\n\nItem " + item.getHandle() + "\n");
                  log.info("Item " + item.getHandle() + "\n");
                DCValue[] dcv = null;
                if (checkbox.equals("Livros")) {
                     log.info("Livros " + Livros.length);
                    for (int i=0; i < Livros.length; i++) {
                        log.info("Ciclo for Livros " + i);
                        dcv = item.getMetadata(Livros[i]);
                        log.info("dcv");
                        if (dcv.length!=0) {
                            ficheiro.append(Livros[i] + " validado.\n");
                            log.info(Livros[i] + " validado.\n");
                        }
                        else {
                            ficheiro.append(" ******** " + Livros[i] + " EM FALTA.\n");
                            log.info(Livros[i] + " validado.\n");
                        }
                    }
                    Bundle[] bundle = item.getBundles("ORIGINAL");
                    for (int j=0; j<bundle.length ; j++){
                         Bitstream[] bitstream = bundle[j].getBitstreams();
                         for (int k=0; k < bitstream.length; k++) {
                             ficheiro.append(" Nome ficheiro: " + bitstream[k].getName() + "\n");
                         }
                    }
                  }
                else
                    if (checkbox.equals("CapLivros")) {

                    for (int i=0; i < CapLivros.length; i++) {
                        dcv = item.getMetadata(CapLivros[i]);
                        if (dcv.length!=0)
                            ficheiro.append(CapLivros[i] + " validado.\n");
                        else
                            ficheiro.append(" ******** " + CapLivros[i] + " EM FALTA.\n");
                    }
                    Bundle[] bundle = item.getBundles("ORIGINAL");
                    for (int j=0; j<bundle.length ; j++){
                         Bitstream[] bitstream = bundle[j].getBitstreams();
                         for (int k=0; k < bitstream.length; k++) {
                             ficheiro.append(" Nome ficheiro: " + bitstream[k].getName() + "\n");
                         }
                    }
                  }
                    else
                     if (checkbox.equals("Artigos")) {

                    for (int i=0; i < Artigos.length; i++) {
                        dcv = item.getMetadata(Artigos[i]);
                        if (dcv.length!=0)
                            ficheiro.append(Artigos[i] + " validado.\n");
                        else
                            ficheiro.append(" ******** " + Artigos[i] + " EM FALTA.\n");
                    }
                    Bundle[] bundle = item.getBundles("ORIGINAL");
                    for (int j=0; j<bundle.length ; j++){
                         Bitstream[] bitstream = bundle[j].getBitstreams();
                         for (int k=0; k < bitstream.length; k++) {
                             ficheiro.append(" Nome ficheiro: " + bitstream[k].getName() + "\n");
                         }
                    }

                  }
                     else
                         ficheiro.append(" Tipologia da colecao nao identificada.\n");

              }

              ficheiro.append("\n *** Processamento terminado.\n\n");
              ficheiro.flush();
              ficheiro.close();

              // Envia relatorio por email
              Email email = ConfigurationManager.getEmail(downloadDir+System.getProperty("file.separator") + fileName + ".txt");


            String title = "";
            try
            {
                title = I18nUtil.getMessage("org.dspace.validate.message");
            }
            catch (MissingResourceException e)
            {
                title = "Untitled";
            }
            email.addRecipient(currentUser.getEmail());
            email.addArgument(title);
            email.addArgument(coll.getMetadata("name"));
            email.send();

        }
        catch (Exception exp) {

            log.warn(LogManager.getHeader(context, "Erro Validacao ", UIUtil
		                    .getRequestLogInfo(request)));
		            JSPManager.showIntegrityError(request, response);

        }

                // Set attributes
        request.setAttribute("validate.user", currentUser);
        request.setAttribute("validate.collection", coll);
        JSPManager.showJSP(request, response, "/validate/main.jsp");

    }


    // ****************************************************************
    // ****************************************************************
    // METHODS FOR SHOWING FORMS
    // ****************************************************************
    // ****************************************************************

    /**
     * Show the main My DSpace page
     * 
     * @param context
     *            current context
     * @param request
     *            current servlet request object
     * @param response
     *            current servlet response object
     */
    private void showMainPage(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
       
        EPerson currentUser = context.getCurrentUser();
        
        Collection coll = Collection.find(context, Integer.parseInt(request
						.getParameter("collection_id")));

        log.info(LogManager.getHeader(context, "valida colecao para ", coll.getHandle()));
        // FIXME: WorkflowManager should return arrays
      
        
        // Set attributes
        request.setAttribute("validate.user", currentUser);
        request.setAttribute("validate.collection", coll);
 

        // Forward to main mydspace page
        JSPManager.showJSP(request, response, "/validate/main.jsp");
    }

    /**
     * Show the user's previous submissions.
     * 
     * @param context
     *            current context
     * @param request
     *            current servlet request object
     * @param response
     *            current servlet response object
     */
    private void showPreviousSubmissions(Context context,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
        // Turn the iterator into a list
        List subList = new LinkedList();
        ItemIterator subs = Item.findBySubmitter(context, context
                .getCurrentUser());

        try
        {
            while (subs.hasNext())
            {
                subList.add(subs.next());
            }
        }
        finally
        {
            if (subs != null)
                subs.close();
        }

        Item[] items = new Item[subList.size()];

        for (int i = 0; i < subList.size(); i++)
        {
            items[i] = (Item) subList.get(i);
        }

        log.info(LogManager.getHeader(context, "view_own_submissions", "count="
                + items.length));

        request.setAttribute("user", context.getCurrentUser());
        request.setAttribute("items", items);

        JSPManager.showJSP(request, response, "/mydspace/own-submissions.jsp");
    }
}
