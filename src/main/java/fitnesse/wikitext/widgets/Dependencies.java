package fitnesse.wikitext.widgets;

import java.util.ArrayList;
import java.util.List;

public class Dependencies
{
	private List<String> resolvedDependencies = new ArrayList<String>();
	private List<String> unresolvedDependencies = new ArrayList<String>();

	public void addResolvedDependency(String dependency)
	{
		resolvedDependencies.add(dependency);
	}

	public void addUnresolvedDependency(String dependency)
	{
		unresolvedDependencies.add(dependency);
	}

	public List<String> getResolvedDependencies()
	{
		return resolvedDependencies;
	}

	public List<String> getUnresolvedDependencies()
	{
		return unresolvedDependencies;
	}
}
