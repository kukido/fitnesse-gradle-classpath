package fitnesse.wikitext.widgets;

import fitnesse.wikitext.parser.*;
import util.Maybe;

import java.io.File;
import java.util.Collection;

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

	private Dependencies getDependencies(Translator translator, Symbol symbol)
	{
		GradleClasspathSymbol gradleClasspathSymbol = getGradleClasspathSymbol(translator, symbol);

		String scope = gradleClasspathSymbol.getScope();
		File source = gradleClasspathSymbol.getSource();

		return gradleClasspathProcessor.getDependencies(source, scope);
	}

	@Override
	public Collection<String> providePaths(Translator translator, Symbol symbol)
	{
		return getDependencies(translator, symbol).getResolvedDependencies();
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
		GradleClasspathSymbol gradleClasspathSymbol = getGradleClasspathSymbol(translator, symbol);

		StringBuilder output = new StringBuilder("<p class='meta'>Gradle classpath [file: ")
				.append(gradleClasspathSymbol.getSource().getAbsolutePath())
				.append(", scope: ")
				.append(gradleClasspathSymbol.getScope())
				.append("]:</p>");


		try
		{
			Dependencies dependencies = getDependencies(translator, symbol);

			output.append("<ul class='meta'>");

			for (String resolvedDependency : dependencies.getResolvedDependencies())
			{
				output
						.append("<li>")
						.append(resolvedDependency)
						.append("</li>");
			}

			for (String unresolvedDependency : dependencies.getUnresolvedDependencies())
			{
				output
						.append("<li class='error'>")
						.append(unresolvedDependency)
						.append("</li>");
			}

			output.append("</ul>");

		}
		catch (Exception exc)
		{
			output
				.append("<p class='error'>Unable to parse Gradle file: ")
				.append(exc.getMessage())
				.append("</p>");
		}

		return output.toString();
	}

	public GradleClasspathSymbol getGradleClasspathSymbol(Translator translator, Symbol symbol)
	{
		return new GradleClasspathSymbol(translator.translate(symbol.childAt(0)));
	}
}
