package pt.keep.oaiextended.modifiers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Locale;

import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.Community;
import org.dspace.core.Constants;
import org.dspace.core.ConfigurationManager;
import org.apache.log4j.Logger;
import org.dspace.core.LogManager;


import pt.keep.oaiextended.AgentItem;
import pt.keep.oaiextended.Modifier;
import pt.keep.oaiextended.AgentContext;
import pt.keep.oaiextended.Agent;



/**
 * This Modifier will be able to add a prefix to a restricted list of dc.types
 * 
 * @author João Melo
 */
@SuppressWarnings("deprecation")
public class RepublicaPrefixModifier extends Modifier {
    	
	private static Logger log = Logger.getLogger(RepublicaPrefixModifier.class);
       private static String nr_idx_str = ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalis.numberofindexes");
       private static int nr_idx = Integer.parseInt(nr_idx_str);
        private static String[] prefix = new String[nr_idx];
        private static String[] index = new String[nr_idx];

        public static final String DEFAULT_MODIFIERS = "pt.keep.oaiextended.modifiers.RepublicaPrefixModifier";
	public static final String AGENT_GROUP_NAME = "modifiers";
	public static final String APPLICABLE_SETS_SPECS = "republica";

	private List<String> applicableSetSpecs;

	public  RepublicaPrefixModifier () {
		super();
            	this.applicableSetSpecs = new ArrayList<String>();
		String[] array = super.getProperty("applicableSetsSpecs", APPLICABLE_SETS_SPECS).split(",");
              log.info(LogManager.getHeader(null,"RepublicaPrefixModifier #array -",array[0]));
		for (String string : array) {
			this.applicableSetSpecs.add(string.trim().toLowerCase());
                     log.info(LogManager.getHeader(null,"RepublicaPrefix #",string.trim()));
		}
		this.getFieldsPrefix();

          //    log.info(LogManager.getHeader(null, "RepublicaPrefixModifier",null));
	}

 
	@Override
	public void modifyItem(AgentItem oaiItem) {
            String url;
            String logo_url=null;
            url = ConfigurationManager.getProperty("dspace.baseUrl");
            DCValue[] values;
           log.info(LogManager.getHeader(null, "RepublicaPrefixModifier:modifyItem", null));
            if (this.index != null) {
                // int size = this.index.length;
                log.info(LogManager.getHeader(null, "RepublicaPrefixModifier-modifyItem-size #", String.valueOf(nr_idx)));
		if (oaiItem.hasInformation()) {
                 // Comunidade que inclui as colecoes dos numeros e respetivo logo
              try {
                 for (int i=0; i< nr_idx; i++){
                           log.info(LogManager.getHeader(null, "RepublicaPrefixModifier:modifyItem:index #"+ i, this.index[i]));
                           log.info(LogManager.getHeader(null, "RepublicaPrefixModifier:modifyItem:index #"+ i, this.prefix[i]));
                 }
                 int k=0;
                 for (k=0; k < nr_idx; k++) 
                   {
                         String[] campo= index[k].split("\\.");
                         log.info(LogManager.getHeader(null, "N Iteracoes", String.valueOf(nr_idx)));
                         log.info(LogManager.getHeader(null, "Iteracao n", String.valueOf(k)));
                         log.info(LogManager.getHeader(null, "RepublicaPrefixModifier-modifyItem-index #", String.valueOf(k) + " " + this.index[k]));
	                  log.info(LogManager.getHeader(null, "RepublicaPrefixModifier-modifyItem-prefix #", String.valueOf(k) + " " + this.prefix[k]));
                         log.info(LogManager.getHeader(null, "Campo size#", String.valueOf(campo.length)));
                
                         
                         
                  	    if (campo.length==2) 
                         {
                          log.info(LogManager.getHeader(null, "Campo 0#", campo[0]));
                         log.info(LogManager.getHeader(null, "Campo 1#", campo[1]));
                          values = oaiItem.getInformation().item.getMetadata(campo[0], campo[1], null, Item.ANY);
                          log.info(LogManager.getHeader(null, "if campo.length == 2#",null ));
                          log.info(LogManager.getHeader(null, "values.length",String.valueOf(values.length) ));
                         }
                         else {
                         log.info(LogManager.getHeader(null, "Campo 0#", campo[0]));
                         log.info(LogManager.getHeader(null, "Campo 1#", campo[1]));
                         log.info(LogManager.getHeader(null, "Campo 2#", campo[2]));
                         values = oaiItem.getInformation().item.getMetadata(campo[0], campo[1], campo[2], Item.ANY);
                         log.info(LogManager.getHeader(null, "values.length",String.valueOf(values.length) ));
                         }
	                  if (values.length == 0) {
                              log.info(LogManager.getHeader(null, "VALUES is NULL",null ));
                              continue;
                         }
                         else {
                          log.info(LogManager.getHeader(null, "VALUES is NOT NULL",null ));
                          log.info(LogManager.getHeader(null,"values.length",String.valueOf(values.length)));
                         }
                         if (campo.length==2) {
                                oaiItem.getInformation().item.clearMetadata(campo[0], campo[1], null, Item.ANY);
                                log.info(LogManager.getHeader(null, "if campo.length == 2#",null ));
                         }
                         else
                              oaiItem.getInformation().item.clearMetadata(campo[0], campo[1], campo[2], Item.ANY);
                         int size1 = values.length;
                         log.info(LogManager.getHeader(null, "values.length",String.valueOf(size1)));
		           for (int j=0; j<size1; j++) 
                         {
                             log.info(LogManager.getHeader(null, "ciclo for DCValue",null ));
           		        oaiItem.getInformation().item.addMetadata(values[j].schema, values[j].element, values[j].qualifier, values[j].language, this.prefix[k] + values[j].value);
                             log.info(LogManager.getHeader(null, "AlmaMaterUCBGModifier-modifyItem-metdata #",values[j].qualifier + "--" + values[j].value));
			    }
                       log.info(LogManager.getHeader(null, "Nova Iteracao", String.valueOf(k)));
		       } 
                    log.info(LogManager.getHeader(null, "Iteracao n", String.valueOf(k)));
              } // try
            catch (Exception e) {
		log.info(LogManager.getHeader(null, "Exception", e.getMessage()));
            }              
          }   // if (oaiItem.hasInformation())

            } //  if (this.index != null)
	}  // modifyItem


    public void getFieldsPrefix()
    {

        int idx = 0;

        String definition, definition2;
        // log.info(LogManager.getHeader(null, "AlmamaterUCBGModifier:getFieldsPrefix"," ## inicio"));
        for ( idx=0; idx < nr_idx ; idx++) 
        {
            definition = ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.fields." + idx);
            log.info(LogManager.getHeader(null, "AlmamaterUCBGModifier:getFieldsPrefix:definition #"+ idx, definition));
            definition2 = null;
            if ((definition2 = ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.prefix." + idx))!=null) {
            log.info(LogManager.getHeader(null, "AlmamaterUCBGModifier:getFieldsPrefix:definition2 #"+ idx, definition2));
                this.index[idx] = definition;
                this.prefix[idx] = definition2;
            }
        }
     // log.info(LogManager.getHeader(null, "ImpactumModifier:getFieldsPrefix"," ## Fim"));
    }

}
