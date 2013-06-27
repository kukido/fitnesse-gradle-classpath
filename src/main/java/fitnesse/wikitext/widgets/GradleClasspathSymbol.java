package fitnesse.wikitext.widgets;

import java.io.File;

public class GradleClasspathSymbol
{
	public static final String DEFAULT_SCOPE = "TEST";
	public static final String SCOPE_SEPARATOR_CHARACTER = "@";

	private String symbol;
	private String scope;
	private long lastModified;
	private File source;

	GradleClasspathSymbol(String symbol)
	{
		this.symbol = symbol;
		parse();
	}

	public void parse()
	{
		String path;

		if (symbol.contains(SCOPE_SEPARATOR_CHARACTER))
		{
			String[] values = symbol.split(SCOPE_SEPARATOR_CHARACTER);
			path = values[0];
			scope = values[1];
		}
		else
		{
			path = symbol;
			scope = DEFAULT_SCOPE;
		}

		source = new File(path);
		lastModified = source.lastModified();
	}

	public File getSource()
	{
		return source;
	}

	public String getScope()
	{
		return scope;
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GradleClasspathSymbol that = (GradleClasspathSymbol) o;

		if (lastModified != that.lastModified) return false;
		if (scope != null ? !scope.equals(that.scope) : that.scope != null) return false;
		if (source != null ? !source.equals(that.source) : that.source != null) return false;
		if (symbol != null ? !symbol.equals(that.symbol) : that.symbol != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = symbol != null ? symbol.hashCode() : 0;
		result = 31 * result + (scope != null ? scope.hashCode() : 0);
		result = 31 * result + (int) (lastModified ^ (lastModified >>> 32));
		result = 31 * result + (source != null ? source.hashCode() : 0);
		return result;
	}
}
