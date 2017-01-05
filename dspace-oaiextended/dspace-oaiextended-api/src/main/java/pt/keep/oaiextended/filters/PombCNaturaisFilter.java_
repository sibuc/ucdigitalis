package pt.keep.oaiextended.filters;

import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.Collection;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.LogManager;

import pt.keep.oaiextended.AgentItem;
import pt.keep.oaiextended.Filter;

/**
 * This class implements a filter:
 * - Is applied if the request set = (Dspace Configuration) oaidriver.rightsFilter.applicableSetSpec
 * - If the dc.rights element = (Dspace Configuration) oaidriver.rightsFilter.dcRightsValue
 * the item is listed.
 * 
 * @author João Melo
 */
@SuppressWarnings("deprecation")
public class PombalinaFilter extends Filter {

        private static Logger log = Logger.getLogger(PombalinaFilter.class);


	public static final String APPLICABLE_SETS_SPECS = "pombalina";
	public static final int POMBALINA_LEVEL = 1;
        public static final String UCDIGITALIS_MAP_FILE = "oaiextended/ucdigitalisPrefix.map";
	
	
	
	private List<String> applicableSetSpecs;
	private List<String> knownPombalinaColl;
        // private int pombalina_lvl;
        private List<String> ucdigitalisPrefixValues;
	
	public PombalinaFilter () {
		
		// request set's
                //log.info(LogManager.getHeader(null, "PombalinaFilter",null));
		String[] array = super.getProperty("applicableSetsSpecs", APPLICABLE_SETS_SPECS).split(",");
		this.applicableSetSpecs = new ArrayList<String>();
		for (String string : array) {
			this.applicableSetSpecs.add(string.trim().toLowerCase());
		}
		this.knownPombalinaColl = this.getKnownPombalinaColl();
                // this.pombalina_lvl = Integer.parseInt(ConfigurationManager.getProperty("oaiextended.pombalina.lvl"));
                this.getUcdigitalisPrefixMap();
	}

        private void getUcdigitalisPrefixMap () {
		this.ucdigitalisPrefixValues = new ArrayList<String>();
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
						String parts[] = line.split("=");
						if (parts.length > 0) {
							this.ucdigitalisPrefixValues.add(parts[0].trim().toLowerCase());
						}
					}
				}
				stream.close();
				is.close();
			}
		} catch (Exception e) {
		}
	}

	public boolean elementAtUCdigitalisPrefixList (String value) {
		for (String str : this.ucdigitalisPrefixValues) {
			if (str.toLowerCase().trim().equals(value)) return true;
		}
		return false;
	}
	/**
	 * Gets the list of known pombalina collections.
	 * 
	 * @return List
	 */
	private List<String> getKnownPombalinaColl () {
		List<String> list = new ArrayList<String>();
		try {

                        String[] pombalina = ConfigurationManager.getProperty("oaiextended.filters.PombalinaFilter.coll").split(",");

			if (pombalina != null) {
                            for (int i=0; i< pombalina.length; i++)

                                list.add(pombalina[i]);
                        //log.info(LogManager.getHeader(null, "PombalinaFilter:getKownPombalinaColl"," ## if pombalina not null"));
			}
                        // else log.info(LogManager.getHeader(null, "PombalinaFilter:getKownPombalinaColl"," ## pombalina is null"));
		return list;
		} catch (Exception e) {
                    // log.info(LogManager.getHeader(null, "PombalinaFilter:getKownPombalinaColl",e.getLocalizedMessage()));
			return list;
		}
	}
	
	
        public static String[] getFieldsPrefix()
    {
        
        int idx = 1;
        String definition;
        ArrayList fields = new ArrayList();
        
        while ( ((definition = ConfigurationManager.getProperty("oaiextended.filters.PombalinaFilter.fields" + idx))) != null)
        {
            fields.add(definition);
            idx++;
        }
        int size = fields.size();
        String bis[] = new String[size];
        bis = (String []) fields.toArray();
        
        return bis;
    }
        
        
        @Override
	public boolean isItemFiltered(AgentItem filterItem) {
		if (!filterItem.hasInformation()) return true;
                //String [] campos_ucdigitalis =getFieldsPrefix();
                //int size = campos_ucdigitalis.length;
                int j = 0;
                boolean field = false;
                boolean coll = true;
                 try {
                Collection[] collections = filterItem.getInformation().item.getCollections();
                if (collections==null) return true;
                for (Collection collection : collections) {
                    //log.info(LogManager.getHeader(null, "PombalinaFilter:isItemFiltered # collection",collection.getHandle()));
		    for (String knownType : this.knownPombalinaColl)
			if (collection.getHandle().equalsIgnoreCase(knownType))
                                    coll = false;
                }
                //for (int i=0; i< size; i++) {
                  //String campo[] = campos_ucdigitalis[i].split(".");
                  // String prefixo = campos_ucdigitalis[1][i];
		  // DCValue[] fields = filterItem.getInformation().item.getMetadata(campo[0], campo[1], campo[2], Item.ANY);
                  //if (fields == null) return true;
                  //else field = false;
                 //}
                }
                catch (Exception e) {
               log.info(LogManager.getHeader(null, "PombalinaFilter:isItemFiltered #",e.getLocalizedMessage()));
                }
		return (field || coll);
	}
	@Override
	public boolean isFilterApplicable() {
		if (!super.getContext().isVirtualSet()) return false;
		if (this.applicableSetSpecs.contains(super.getContext().getHarvestSet().toLowerCase())) return true;
		else return false;
	}
	@Override
	protected void updateContext() {
		super.getContext().setHarvestSet(null);
	}
}
