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
public class ImpactumModifier extends Modifier {
    	
	private static Logger log = Logger.getLogger(PombalinaModifier.class);
        private static String[] prefix;
        private static String[] index;

        public static final String DEFAULT_MODIFIERS = "pt.keep.oaiextended.modifiers.ImpactumModifier";
	public static final String AGENT_GROUP_NAME = "modifiers";
	public static final String APPLICABLE_SETS_SPECS = "impactum";
        public static final String UCDIGITALIS_MAP_FILE = "oaiextended/ucdigitalisAreaCientifica.map";
        public static final String DEFAULT_AREA_PREFIX="Area:";

	private List<String> applicableSetSpecs;
        private List<String> ucdigitalisAreaCientificaValues;
        private String area_prefix;

	public ImpactumModifier () {
		// super();
            	this.applicableSetSpecs = new ArrayList<String>();
		String[] array = super.getProperty("applicableSetsSpecs", APPLICABLE_SETS_SPECS).split(",");
                //log.info(LogManager.getHeader(null,"ImpactumModifier #array -",array[0]));
		for (String string : array) {
			this.applicableSetSpecs.add(string.trim().toLowerCase());
                     //   log.info(LogManager.getHeader(null,"Modifier #",string.trim()));
		}
		this.getFieldsPrefix();
                this.getUcdigitalisAreaCientificaMap();
              //  log.info(LogManager.getHeader(null, "ImpactumModifier",null));
	}

 
	@Override
	public void modifyItem(AgentItem oaiItem) {
            String url;
            String logo_url=null;
            url = ConfigurationManager.getProperty("dspace.baseUrl");
            DCValue[] values;
           // log.info(LogManager.getHeader(null, "ImpactumModifier:modifyItem", null));
            if (this.index != null) {
                int size = this.index.length;
                log.info(LogManager.getHeader(null, "ImpactumModifier:modifyItem:size #", String.valueOf(size)));
		if (oaiItem.hasInformation()) {
                try {
                 // Area cientifica
                 Community comm[] = oaiItem.getInformation().item.getCommunities();
                 log.info(LogManager.getHeader(null, "ImpactumModifier:getCommunities:size #", String.valueOf(comm.length)));
                 for (int j=0; j < comm.length; j++) {
                     log.info(LogManager.getHeader(null, "ImpactumModifier:modifyItem:comm #", comm[j].getName().toLowerCase()));
                     if (elementAtUCdigitalisAreaCientificaList(comm[j].getName().toLowerCase().trim())) {
                       oaiItem.getInformation().item.addMetadata("dc", "subject", "","por", this.area_prefix +comm[j].getName());
                     }
                 }
                 // Comunidade que inclui as colecoes dos numeros e respetivo logo
                  if (oaiItem.getInformation().item.getOwningCollection().getParentObject()!=null){
                     log.info(LogManager.getHeader(null, "item.getOwningCollection().getParentObject()!=null", ""));
                   if (oaiItem.getInformation().item.getOwningCollection().getParentObject().getType()==Constants.COMMUNITY) {
                    Community comm1 = (Community) oaiItem.getInformation().item.getOwningCollection().getParentObject();
                    log.info(LogManager.getHeader(null, "comm1", comm1.getName()));
                    oaiItem.getInformation().item.addMetadata("dc", "source", "","por",ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.comunidade_prefix")  +comm1.getHandle());
                    if (comm1.getLogo()!=null){
                    long img_id = comm1.getLogo().getID();
                    if (img_id!=0) {
                       logo_url = url+"/retrieve/"+String.valueOf(img_id);
                       oaiItem.getInformation().item.addMetadata("dc", "source", "","por",ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.logotipo_prefix")  +logo_url);
                    }
                    }

                }
                }
                     else
                     log.info(LogManager.getHeader(null, "item.getOwningCollection().getParentObject()", "IS NULL"));





                 for (int i=0; i< size; i++) {
                  String[] campo= index[i].split("\\.");
              //   log.info(LogManager.getHeader(null, "ImpactumModifier:modifyItem:index #", String.valueOf(i) + " " + this.index[i]));
	      //   log.info(LogManager.getHeader(null, "ImpactumModifier:modifyItem:prefix #", String.valueOf(i) + " " + this.prefix[i]));
              //   log.info(LogManager.getHeader(null, "Campo size#", String.valueOf(campo.length)));
                
              //    log.info(LogManager.getHeader(null, "Campo 0#", campo[0]));
              //    log.info(LogManager.getHeader(null, "Campo 1#", campo[1]));
              //    log.info(LogManager.getHeader(null, "Campo 2#", campo[2]));
                  if (campo.length==2) {
                  values = oaiItem.getInformation().item.getMetadata(campo[0], campo[1], null, Item.ANY);
                     }
                  else values = oaiItem.getInformation().item.getMetadata(campo[0], campo[1], campo[2], Item.ANY);
	          if (values == null) continue;
                  if (campo.length==2) {
                      oaiItem.getInformation().item.clearMetadata(campo[0], campo[1], null, Item.ANY);
                  }
                  else
                  oaiItem.getInformation().item.clearMetadata(campo[0], campo[1], campo[2], Item.ANY);
		  for (DCValue value : values) {
			
		    oaiItem.getInformation().item.addMetadata(value.schema, value.element, value.qualifier, value.language, this.prefix[i] + value.value);
                //    log.info(LogManager.getHeader(null, "ImpactumModifier:modifyItem:metdata #",value.qualifier + "--" + value.value));
			}
		}
            }
            catch (Exception e) {

            }
                }
            }
	}

private void getUcdigitalisAreaCientificaMap () {
		this.ucdigitalisAreaCientificaValues = new ArrayList<String>();
		try {
			String dir = ConfigurationManager.getProperty("dspace.dir");
			if (dir.endsWith("/"))
				dir += "config/"+ UCDIGITALIS_MAP_FILE;
			else
				dir += "/config/" + UCDIGITALIS_MAP_FILE;
			InputStream is;
			if (dir != null) {
				is = new FileInputStream(dir);
				BufferedReader stream = new BufferedReader(
						new InputStreamReader(is));
				String line;
				while ((line = stream.readLine()) != null) {
					line = line.trim();
					if (!line.equals("") && !line.startsWith("#")) { // Isn't a
																		// comment
					this.ucdigitalisAreaCientificaValues.add(line.trim().toLowerCase());
	    log.info(LogManager.getHeader(null, "ImpactumModifier:AreaCientificaMap #","--" + line.trim().toLowerCase()));
					}
				}
				stream.close();
				is.close();
			}
		} catch (Exception e) {
		}
	}

	public boolean elementAtUCdigitalisAreaCientificaList (String value) {
                boolean encontrou = false;
		for (String str : this.ucdigitalisAreaCientificaValues) {
                       log.info(LogManager.getHeader(null, "elementAtUCdigitalisAreaCientificaList -- str --", str));
                       log.info(LogManager.getHeader(null, "elementAtUCdigitalisAreaCientificaList -- value --", value));
			if (str.toLowerCase().trim().equals(value.toLowerCase().trim())) {
                            log.info(LogManager.getHeader(null, "elementAtUCdigitalisAreaCientificaList -- value --", value));
                            encontrou=true;
                            break;
                        }
		}
		return encontrou;
	}

    public void getFieldsPrefix()
    {

        int idx = 1;
        if ((area_prefix=ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.area_cientifica_prefix"))==null)
               area_prefix = DEFAULT_AREA_PREFIX;
        String definition, definition2;
        ArrayList<String> fields_aux = new ArrayList<String>();
        ArrayList<String> prefix_aux = new ArrayList<String>();
        // log.info(LogManager.getHeader(null, "ImpactumModifier:getFieldsPrefix"," ## inicio"));
        while ( ((definition = ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.fields." + idx))) != null)
        {
        //    log.info(LogManager.getHeader(null, "ImpactumModifier:getFieldsPrefix:definition #", definition));
            definition2 = null;
            if ((definition2 = ConfigurationManager.getProperty("oaiextended.modifiers.UCdigitalisModifier.prefix." + idx))!=null) {
        //    log.info(LogManager.getHeader(null, "ImpactumModifier:getFieldsPrefix:definition2 #", definition2));
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
     // log.info(LogManager.getHeader(null, "ImpactumModifier:getFieldsPrefix"," ## Fim"));
    }
}
