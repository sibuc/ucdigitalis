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
import org.dspace.content.Community;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.LogManager;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;

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
public class ImpactumFilterColl extends Filter {

        private static Logger log = Logger.getLogger(ImpactumFilterColl.class);


	public static final String APPLICABLE_SETS_SPECS = "impactum";
	public static final String UCDIGITALIS_MAP_FILE = "oaiextended/ucdigitalisAreaCientifica.map";
	
	
	
	private List<String> applicableSetSpecs;
       private List<String> knownImpactumColl;

        // private int pombalina_lvl;
        private List<String> ucdigitalisPrefixValues;
        private List<String> ucdigitalisAreaCientificaValues;
	
	public ImpactumFilterColl () {
		
		// request set's
               // log.info(LogManager.getHeader(null, "ImpactumFilter",null));
		String[] array = super.getProperty("applicableSetsSpecs", APPLICABLE_SETS_SPECS).split(",");
		this.applicableSetSpecs = new ArrayList<String>();
		for (String string : array) {
			this.applicableSetSpecs.add(string.trim().toLowerCase());
		}
		
              this.knownImpactumColl = this.getKnownImpactumColl();

                // this.pombalina_lvl = Integer.parseInt(ConfigurationManager.getProperty("oaiextended.pombalina.lvl"));
                // this.getUcdigitalisAreaCientificaMap();
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
						String parts[] = line.split("=");
						if (parts.length > 0) {
							this.ucdigitalisAreaCientificaValues.add(parts[0].trim().toLowerCase());
						}
					}
				}
				stream.close();
				is.close();
			}
		} catch (Exception e) {
		}
	}

	public boolean elementAtUCdigitalisAreaCientificaList (String value) {
		for (String str : this.ucdigitalisAreaCientificaValues) {
			if (str.toLowerCase().trim().equals(value)) return true;
		}
		return false;
	}
       
	/**
	 * Gets the list of known impactum collections.
	 * 
	 * @return List
	 */
	private List<String> getKnownImpactumColl () {
		List<String> list = new ArrayList<String>();
		try {
                        String[] impactum = ConfigurationManager.getProperty("oaiextended.filters.ImpactumFilter.coll").split(",");


			if (impactum != null) {
                            for (int i=0; i< impactum.length; i++) {
                                list.add(impactum[i]);
                                log.info(LogManager.getHeader(null, "ImpactumFilter:getKownImpactumColl",impactum[i]));
                               
                            }
			}
                     else log.info(LogManager.getHeader(null, "ImpactumFilter:getKownImpactumColl"," ## impactum is null"));
                     

		return list;
		} catch (Exception e) {
                    log.info(LogManager.getHeader(null, "ImpactumFilter:getKownImpactumColl",e.getLocalizedMessage()));
			return list;
		}
	}

        @Override
	public boolean isItemFiltered(AgentItem filterItem) {
		if (!filterItem.hasInformation()) return true;
                           
                boolean coll = true;
                 try {
                Collection[] collections = filterItem.getInformation().item.getCollections();
                 
                if  (collections==null) {
                     log.info(LogManager.getHeader(null, "ImpactumFilter:isItemFiltered # collections is null",null));                         
                     return true;
               }
               if (collections!=null) {
                for (Collection collection : collections) {
              //      log.info(LogManager.getHeader(null, "ImpactumFilter:isItemFiltered # collection",collection.getHandle()));
		    for (String knownType : this.knownImpactumColl)
			if (collection.getHandle().equalsIgnoreCase(knownType))
                                    coll = false;
                }
               }
	//	log.info(LogManager.getHeader(null, "ImpactumFilter:isItemFiltered # Result #",String.valueOf(comm)));
                return coll;
		} catch (Exception e) {
                    log.info(LogManager.getHeader(null, "ImpactumFilter:getKownImpactumColl",e.getLocalizedMessage()));
			return coll;
		}
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
