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
public class AlmaMaterUCBGFilter extends Filter {

        private static Logger log = Logger.getLogger(PombalinaFilter.class);


	public static final String APPLICABLE_SETS_SPECS = "almamater_ucbg";

	private List<String> applicableSetSpecs;
	private List<String> knownPombalinaColl;
        
        
	
	public AlmaMaterUCBGFilter () {
		
		// request set's
              log.info(LogManager.getHeader(null, "AlmaMaterUCBGFilter","inicio"));
		String[] array = super.getProperty("applicableSetsSpecs", APPLICABLE_SETS_SPECS).split(",");
		this.applicableSetSpecs = new ArrayList<String>();
		for (String string : array) {
			this.applicableSetSpecs.add(string.trim().toLowerCase());
		}
		this.knownPombalinaColl = this.getKnownPombalinaColl();
              log.info(LogManager.getHeader(null, "AlmaMaterUCBGFilter","fim"));
                
	}


	/**
	 * Gets the list of known pombalina collections.
	 * 
	 * @return List
	 */
	private List<String> getKnownPombalinaColl () {
		List<String> list = new ArrayList<String>();
		try {

                        String[] pombalina = ConfigurationManager.getProperty("oaiextended.filters.AlmaMaterUCBGFilter.coll").split(",");
                      log.info(LogManager.getHeader(null, "AlmaMaterUCBGFilter:getKownPombalinaColl",String.valueOf(pombalina.length)));
			if (pombalina != null) {
                            for (int i=0; i< pombalina.length; i++)
                               {
                                list.add(pombalina[i]);
                        log.info(LogManager.getHeader(null, "AlmaMaterUCBGFilter:getKownPombalinaColl",pombalina[i]));
                        }
			}
                         else log.info(LogManager.getHeader(null, "AlmaMaterUCBGFilter:getKownPombalinaColl"," ## pombalina is null"));
		return list;
		} catch (Exception e) {
                     log.info(LogManager.getHeader(null, "AlmaMaterUCBGFilter:getKownPombalinaColl",e.getLocalizedMessage()));
			return list;
		}
	}
	
	
  /**      @Override
	public boolean isItemFiltered(AgentItem filterItem) {
		if (!filterItem.hasInformation()) return true;
		DCValue[] values = filterItem.getInformation().item.getMetadata("ucdigitalis", "publication", "collectionId", Item.ANY);
		if (values == null) return true;
		for (DCValue value : values) {
			for (String knownType : this.knownPombalinaColl)
				if (value.value.trim().toLowerCase().endsWith(knownType))
					return false;
		}
		return true;
	}

 */        
     @Override
	public boolean isItemFiltered(AgentItem filterItem) {
		if (!filterItem.hasInformation()) return true;
                //String [] campos_ucdigitalis =getFieldsPrefix();
                //int size = campos_ucdigitalis.length;
                // boolean coll = true;
                 try {
                Collection[] collections = filterItem.getInformation().item.getCollections();
                if (collections==null) return true;
                for (Collection collection : collections) {
                    // log.info(LogManager.getHeader(null, "AlmaMaterUCBGFilter:isItemFiltered # item",filterItem.getInformation().item.getHandle() + " Coll - " + collection.getHandle()));
		    for (String knownType : this.knownPombalinaColl)
			if (collection.getHandle().equalsIgnoreCase(knownType))
                                    return false;
                  // log.info(LogManager.getHeader(null, "AlmaMaterUCBGFilter:isItemFiltered # item",filterItem.getInformation().item.getHandle() + " Coll - " + collection.getHandle() + "=" + String.valueOf(coll)));

                }

                }
                catch (Exception e) {
               log.info(LogManager.getHeader(null, "Erro:AlmaMaterUCBGFilter:isItemFiltered #",e.getLocalizedMessage()));
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
