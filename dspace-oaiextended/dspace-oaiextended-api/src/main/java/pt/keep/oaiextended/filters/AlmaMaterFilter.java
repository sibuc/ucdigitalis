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
public class AlmaMaterFilter extends Filter {

        private static Logger log = Logger.getLogger(PombalinaFilter.class);


	public static final String APPLICABLE_SETS_SPECS = "almamater";

	private List<String> applicableSetSpecs;
	private List<String> knownPombalinaColl;
        
        
	
	public AlmaMaterFilter () {
		
		// request set's
                //log.info(LogManager.getHeader(null, "AlmaMaterFilter",null));
		String[] array = super.getProperty("applicableSetsSpecs", APPLICABLE_SETS_SPECS).split(",");
		this.applicableSetSpecs = new ArrayList<String>();
		for (String string : array) {
			this.applicableSetSpecs.add(string.trim().toLowerCase());
		}
		this.knownPombalinaColl = this.getKnownPombalinaColl();
                
	}


	/**
	 * Gets the list of known pombalina collections.
	 * 
	 * @return List
	 */
	private List<String> getKnownPombalinaColl () {
		List<String> list = new ArrayList<String>();
		try {

                        String[] pombalina = ConfigurationManager.getProperty("oaiextended.filters.AlmaMaterFilter.coll").split(",");

			if (pombalina != null) {
                            for (int i=0; i< pombalina.length; i++)
                              {
                                list.add(pombalina[i]);
                        log.info(LogManager.getHeader(null, "AlmaMaterFilter:getKownAlmaMaterColl",pombalina[i]));
                         }
			}
                        else log.info(LogManager.getHeader(null, "AlmaMaterFilter:getKownAlmaMaterColl"," ## AlmaMater is null"));
		} catch (Exception e) {
                    log.info(LogManager.getHeader(null, "AlmaMaterFilter:getKownAlmaMaterColl",e.getLocalizedMessage()));
			
		}
              return list;
	}
	
	
     
        
        @Override
	public boolean isItemFiltered(AgentItem filterItem) {
		if (!filterItem.hasInformation()) return true;
                //String [] campos_ucdigitalis =getFieldsPrefix();
                //int size = campos_ucdigitalis.length;
                int j = 0;
                boolean coll = true;
                log.info(LogManager.getHeader(null, "AlmaMaterFilter:isItemFiltered # item - ",filterItem.getInformation().item.getHandle()));
                 try {
                Collection[] collections = filterItem.getInformation().item.getCollections();
                if (collections==null) return true;
                for (Collection collection : collections) {
                    log.info(LogManager.getHeader(null, "PombalinaFilter:isItemFiltered # collection - ",collection.getHandle()));
		    for (String knownType : this.knownPombalinaColl)
			if (collection.getHandle().equalsIgnoreCase(knownType))
                                    return false;
                }

                }
                catch (Exception e) {
               log.info(LogManager.getHeader(null, "AlmaMaterFilter:isItemFiltered #",e.getLocalizedMessage()));
                }
		return true;
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
