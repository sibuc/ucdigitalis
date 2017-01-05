/*
 * ThumbnailServlet.java
 *
 * Version: 
 *
 * Date: 
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
package uk.ac.cam.dspace.app.webui.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.mediafilter.JPEGFilter;
import org.dspace.app.image.ImageManager;
import org.dspace.app.image.ImageManagerException;
import org.dspace.app.image.ImageManagerFactory;
import org.dspace.app.webui.servlet.*;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.core.PluginManager;
import org.dspace.core.Utils;

/**
 * Servlet for retrieving thumbnails based on the supplied bitstream id. If a thumbnail doesn't already exist
 * it will be generated. Otherwise a cached thumbnail will be returned from /dspace/thumbs/[0-9]/bitstreamid
 * <P>
 * <code>/thumbnail/bitstream-id</code>
 * I
 * @author James Dickson
 * @version 
 */
public class ThumbnailServlet extends DSpaceServlet {
    /** log4j category */
    private static Logger log = Logger.getLogger(ThumbnailServlet.class);
    
    protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException {
        Bitstream bitstream = null;
        
        // Get the ID from the URL
        String idString = request.getPathInfo();
        InputStream is =null;
        if (idString != null) {
            // Remove leading slash
            if (idString.startsWith("/")) {
                idString = idString.substring(1);
            }
            
            // If there's a second slash, remove it and anything after it,
            // it might be a filename
            int slashIndex = idString.indexOf('/');
            
            if (slashIndex != -1) {
                idString = idString.substring(0, slashIndex);
            }
            
            //Find the corresponding bitstream
            try {
                int id = Integer.parseInt(idString);
                boolean exists=false;
                ImageManager manager = (ImageManager)PluginManager.getSinglePlugin(ImageManager.class);
               
                try{
                    is = manager.retrieve(id);
                    exists=true;
                }catch(ImageManagerException ex){
                    exists=false;
                }
                //only load the bitstream if we have too. Too much obect loading.
                if(!exists){
                    bitstream = Bitstream.find(context, id);
                    InputStream originalBitstream= bitstream.retrieve();
                    //one off operation to cache thumbnails using imagemagick..                   
                    is= manager.getThumbnail(bitstream.getFormat().getMIMEType(),id,originalBitstream);
                    originalBitstream.close();
                }
            }
            
            catch (NumberFormatException nfe) {
                log.error("NumberFormatException thrown", nfe);
                // Invalid ID - this will be dealt with below
            } catch (Exception nfe) {
                log.error("Exception thrown", nfe);
                // Invalid ID - this will be dealt with below
            }
        }
        
        // Did we get a bitstream?
        if (is != null) {
            
            // Set the response MIME type always jpeg..
            response.setContentType("image/jpeg");
            
            Utils.bufferedCopy(is, response.getOutputStream());
            is.close();
            response.getOutputStream().flush();
        } else {
            // No bitstream - we got an invalid ID
            log.info(LogManager.getHeader(context, "view_bitstream",
                    "invalid_bitstream_id=" + idString));
            
            JSPManager.showInvalidIDError(request, response, idString,
                    Constants.BITSTREAM);
        }
    }
}
