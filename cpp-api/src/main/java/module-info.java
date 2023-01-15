/**
 * @author VISTALL
 * @since 15/01/2023
 */
module consulo.cpp.api
{
	requires transitive consulo.ide.api;

	exports consulo.cpp.api.icon;
	exports consulo.cpp.lang;
	exports consulo.cpp.lang.editor.highlight;
	exports consulo.cpp.lang.psi.impl;
	exports consulo.cpp.preprocessor;
	exports consulo.cpp.preprocessor.expand;
	exports consulo.cpp.preprocessor.fileProvider;
	exports consulo.cpp.preprocessor.lexer;
	exports consulo.cpp.preprocessor.parser;
	exports consulo.cpp.preprocessor.psi;
	exports consulo.cpp.preprocessor.psi.impl;
	exports consulo.cpp.preprocessor.psi.impl.visitor;
	exports org.napile.cpp4idea;
	exports org.napile.cpp4idea.config;
	exports org.napile.cpp4idea.config.sdk;
	exports org.napile.cpp4idea.config.sdk.sdkdialect;
	exports org.napile.cpp4idea.config.sdk.sdkdialect.impl;
	exports org.napile.cpp4idea.ide.highlight;
	exports org.napile.cpp4idea.ide.projectView;
	exports org.napile.cpp4idea.ide.projectView.nodes;
	exports org.napile.cpp4idea.lang;
	exports org.napile.cpp4idea.lang.lexer;
	exports org.napile.cpp4idea.lang.parser;
	exports org.napile.cpp4idea.lang.parser.parsingMain;
	exports org.napile.cpp4idea.lang.psi;
	exports org.napile.cpp4idea.lang.psi.impl;
	exports org.napile.cpp4idea.lang.psi.visitors;
	exports org.napile.cpp4idea.util;
}