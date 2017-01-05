package pt.keep.oaiextended.filters;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;

import pt.keep.oaiextended.AgentItem;
import pt.keep.oaiextended.Filter;

/**
 * This class implements a filter:
 * - Is applied if the request set = (Dspace Configuration) oaidriver.RepublicaFilter.applicableSetSpec
 * - If the ucdigitalis.publication.digcollection element = (Dspace Configuration) oaiextended.filters.RepublicaFilter.ucdigitalisValue
 * the item is listed.
 * 
 * @author JoÃ£o Melo  / Ana Luisa Silva - adaptação para UCDigitalis 2013.05.14
 */
@SuppressWarnings("deprecation")
public class RepublicaFilter extends Filter {
	public static final String APPLICABLE_SETS_SPECS = "republica";
	public static final String KNOWN_REPUBLICA_String = "Republica Digital";
	
	
	
	private List<String> applicableSetSpecs;
	private String knownRepublica;
	
	public RepublicaFilter () {
		
		// request set's
		String[] array = super.getProperty("applicableSetsSpecs", APPLICABLE_SETS_SPECS).split(",");
		this.applicableSetSpecs = new ArrayList<String>();
		for (String string : array) {
			this.applicableSetSpecs.add(string.trim().toLowerCase());
		}
		this.knownRepublica= this.getKnownRepublica();
	}
	
	/**
	 * Gets the list of known dc types.
	 * 
	 * @return List
	 */
	private String getKnownRepublica() {
		String republica = KNOWN_REPUBLICA_String;
		try {
			String dir = ConfigurationManager.getProperty("oaiextended.filters.RepublicaFilter.ucdigitalisValue");

			if (dir != null) {
                        republica = dir;
			}
			return republica;
		} catch (Exception e) {
			return republica;
		}
	}
	
	@Override
	public boolean isItemFiltered(AgentItem filterItem) {
		if (!filterItem.hasInformation()) return true;
		DCValue[] values = filterItem.getInformation().item.getMetadata("ucdigitalis", "publication", "digcollection", Item.ANY);
		if (values == null) return true;
		for (DCValue value : values) {
				if (value.value.trim().toLowerCase().endsWith(this.knownRepublica))
					return false;
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
