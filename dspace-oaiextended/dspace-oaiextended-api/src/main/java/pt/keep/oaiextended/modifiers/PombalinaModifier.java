package pt.keep.oaiextended.modifiers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.Collection;
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
public class PombalinaModifier extends Modifier {
    	
	private static Logger log = Logger.getLogger(PombalinaModifier.class);
        private static String[] prefix;
        private static String[] index;

        public static final String DEFAULT_MODIFIERS = "pt.keep.oaiextended.modifiers.PombalinaModifier";
	public static final String AGENT_GROUP_NAME = "modifiers";
	public static final String APPLICABLE_SETS_SPECS = "pombalina";
        public static final String DEFAULT_AREA_PREFIX = "area_cientifica_";

	private List<String> applicableSetSpecs;
        private String area_prefix;

	public PombalinaModifier () {
		// super();
            	this.applicableSetSpecs = new ArrayList<String>();
		String[] array = super.getProperty("applicableSetsSpecs", APPLICABLE_SETS_SPECS).split(",");
           //     log.info(LogManager.getHeader(null,"PombalinaModifier #array -",array[0]));
		for (String string : array) {
			this.applicableSetSpecs.add(string.trim().toLowerCase());
             //           log.info(LogManager.getHeader(null,"Modifier #",string.trim()));
		}
		this.getFieldsPrefix();
           //     log.info(LogManager.getHeader(null, "PombalinaModifier",null));
	}

 
	@Override
	public void modifyItem(AgentItem oaiItem) {
            // log.info(LogManager.getHeader(null, "PombalinaModifier:modifyItem", null));
            DCValue[] values;
            if (this.index != null) {
                int size = this.index.length;
                log.info(LogManager.getHeader(null, "PombalinaModifier:modifyItem:size #", String.valueOf(size)));
		if (oaiItem.hasInformation()) {
                try {
                 Collection coll[] = oaiItem.getInformation().item.getCollections();
                 for (int j=0; j < coll.length; j++) {
                 oaiItem.getInformation().item.addMetadata("dc", "subject", "","por", this.area_prefix+coll[j].getName());
                 }
                 for (int i=0; i< size; i++) {
                  String[] campo= index[i].split("\\.");
                   log.info(LogManager.getHeader(null, "PombalinaModifier:modifyItem:index #", String.valueOf(i) + " " + this.index[i]));
		  log.info(LogManager.getHeader(null, "PombalinaModifier:modifyItem:prefix #", String.valueOf(i) + " " + this.prefix[i]));
                   log.info(LogManager.getHeader(null, "Campo size#", String.valueOf(campo.length)));
                  //if (campo.length==2) {
                   //   log.info(LogManager.getHeader(null, "Campo 2 is null#", null));
                  //}
                  //log.info(LogManager.getHeader(null, "Campo 0#", campo[0]));
                  //log.info(LogManager.getHeader(null, "Campo 1#", campo[1]));
                  //if (campo.length!=2)
                  //log.info(LogManager.getHeader(null, "Campo 2#", campo[2]));

                  if (campo.length==2) {
                  values = oaiItem.getInformation().item.getMetadata(campo[0], campo[1], null, Item.ANY);
                     }
                  else
                    values = oaiItem.getInformation().item.getMetadata(campo[0], campo[1], campo[2], Item.ANY);
	          if (values == null) {
                    //  log.info(LogManager.getHeader(null, "VALUES is NULL", campo[2]));
                      continue;
                  }
                  if (campo.length==2)
                      oaiItem.getInformation().item.clearMetadata(campo[0], campo[1], null, Item.ANY);
                  else
                  oaiItem.getInformation().item.clearMetadata(campo[0], campo[1], campo[2], Item.ANY);
		  for (DCValue value : values) {
			
		    oaiItem.getInformation().item.addMetadata(value.schema, value.element, value.qualifier, value.language, this.prefix[i] + value.value);
                    log.info(LogManager.getHeader(null, "PombalinaModifier:modifyItem:metdata #",value.qualifier + "--" + value.value));
			}
		}
            }
            catch (Exception e) {

            }
                }
            }
	}


    public void getFieldsPrefix()
    {

        int idx = 1;
        if ((area_prefix=ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.area_cientifica_prefix"))==null)
               area_prefix = DEFAULT_AREA_PREFIX;
        String definition, definition2;
        ArrayList<String> fields_aux = new ArrayList<String>();
        ArrayList<String> prefix_aux = new ArrayList<String>();
        //log.info(LogManager.getHeader(null, "PombalinaModifier:getFieldsPrefix"," ## inicio"));
        while ( ((definition = ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.fields." + idx))) != null)
        {
            //log.info(LogManager.getHeader(null, "PombalinaModifier:getFieldsPrefix:definition #", definition));
            definition2 = null;
            if ((definition2 = ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.prefix." + idx))!=null) {
              //  log.info(LogManager.getHeader(null, "PombalinaModifier:getFieldsPrefix:definition2 #", definition2));
                fields_aux.add(definition);
                prefix_aux.add(definition2);
            }
            idx++;
        }
        int size = fields_aux.size();
        this.prefix = new String[size];
        this.index = new String[size];
        this.index = fields_aux.toArray(index);
        this.prefix = prefix_aux.toArray(prefix);
//log.info(LogManager.getHeader(null, "PombalinaModifier:getFieldsPrefix"," ## Fim"));
    }
}
