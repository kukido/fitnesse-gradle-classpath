package fitnesse.wikitext.widgets;

import fitnesse.wikitext.parser.*;
import util.Maybe;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GradleClasspathSymbolType extends SymbolType implements Rule, Translation, PathsProvider
{
	public static final String GRADLE_CLASSPATH_SYMBOL_TYPE_NAME = "GradleClasspathSymbolType";

	private GradleClasspathProcessor gradleClasspathProcessor;

	public GradleClasspathSymbolType()
	{
		super(GRADLE_CLASSPATH_SYMBOL_TYPE_NAME);
		gradleClasspathProcessor = new GradleClasspathProcessor();

		wikiMatcher(new Matcher().startLineOrCell().string("!gradle"));
		wikiRule(this);
		htmlTranslation(this);
	}

	private List<String> getClasspathEntries(Translator translator, Symbol symbol)
	{
		try
		{
			GradleClasspathSymbol gradleClasspathSymbol = getGradleClasspathSymbol(translator, symbol);

			String scope = gradleClasspathSymbol.getScope();
			File source = gradleClasspathSymbol.getSource();

			return gradleClasspathProcessor.getClasspathEntries(source, scope);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
			// noinspection unchecked
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public Collection<String> providePaths(Translator translator, Symbol symbol)
	{
		return getClasspathEntries(translator, symbol);
	}

	@Override
	public Maybe<Symbol> parse(Symbol current, Parser parser)
	{
		if (!parser.isMoveNext(SymbolType.Whitespace))
		{
			return Symbol.nothing;
		}
		else
		{
			return new Maybe<Symbol>(
					current.add(
							parser.parseToEnds(
									0,
									SymbolProvider.pathRuleProvider,
									new SymbolType[]{SymbolType.Newline}
							)
					)
			);
		}
	}

	@Override
	public boolean matchesFor(SymbolType symbolType)
	{
		return symbolType instanceof Path || super.matchesFor(symbolType);
	}

	@Override
	public String toTarget(Translator translator, Symbol symbol)
	{
		List<String> classpathEntries = null;
		GradleClasspathSymbol gradleClasspathSymbol = getGradleClasspathSymbol(translator, symbol);
		StringBuilder classpathForRender = new StringBuilder("<p class='meta'>Gradle classpath [file: ")
				.append(gradleClasspathSymbol.getSource().getAbsolutePath())
				.append(", scope: ")
				.append(gradleClasspathSymbol.getScope())
				.append("]:</p>")
				.append("<ul class='meta'>");
		try
		{
			classpathEntries = getClasspathEntries(translator, symbol);
			for (String classpathEntry : classpathEntries)
			{
				classpathForRender.append("<li>").append(classpathEntry).append("</li>");
			}
		}
		catch (Exception e)
		{
			classpathForRender.append("<li class='error'>Unable to parse POM file: ")
					.append(e.getMessage()).append("</li>");
		}
		classpathForRender.append("</ul>");
		return classpathForRender.toString();
	}

	public GradleClasspathSymbol getGradleClasspathSymbol(Translator translator, Symbol symbol)
	{
		return new GradleClasspathSymbol(translator.translate(symbol.childAt(0)));
	}
}
