/* It's an automatically generated code. Do not modify it. */
package org.napile.cpp4idea.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

@SuppressWarnings({"ALL"})
%%

%{
  public _CFirstStepLexer(){
    this((java.io.Reader)null);
  }

  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzPushbackPos = 0;
    zzAtEOF = offset < zzEndRead;
  }
%}

%unicode
%class _CFirstStepLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return;
%eof}

WHITE_SPACE_CHAR=[\ \r\t\f]
NEW_LINE_CHAR=[\n]

IDENTIFIER=[:jletter:] [:jletterdigit:]*

CHARACTER_LITERAL="'"([^\\\'\r\n]|{ESCAPE_SEQUENCE})*("'"|\\)?
STRING_LITERAL=\"([^\\\"\r\n]|{ESCAPE_SEQUENCE})*(\"|\\)?
STRING_INCLUDE_LITERAL=\<([^\\\<\r\n]|{ESCAPE_SEQUENCE})*(\>|\\)?
ESCAPE_SEQUENCE=\\[^\r\n]

%%

<YYINITIAL> {WHITE_SPACE_CHAR}+ { return CTokenType.WHITE_SPACE; }
<YYINITIAL> {NEW_LINE_CHAR} { return CTokenType.NEW_LINE; }

<YYINITIAL> {CHARACTER_LITERAL} { return CTokenType.CHARACTER_LITERAL; }
<YYINITIAL> {STRING_LITERAL} { return CTokenType.STRING_LITERAL; }

<YYINITIAL> "#include" { return CTokenType.S_INCLUDE_KEYWORD; }
<YYINITIAL> "#define" { return CTokenType.S_DEFINE_KEYWORD; }
<YYINITIAL> "#ifdef" { return CTokenType.S_IFDEF_KEYWORD; }
<YYINITIAL> "#ifndef" { return CTokenType.S_IFNDEF_KEYWORD; }
<YYINITIAL> "#endif" { return CTokenType.S_ENDIF_KEYWORD; }

<YYINITIAL> {IDENTIFIER} { return CTokenType.IDENTIFIER; }

<YYINITIAL> "("   { return CTokenType.LPARENTH; }
<YYINITIAL> ")"   { return CTokenType.RPARENTH; }

<YYINITIAL> {STRING_INCLUDE_LITERAL} { return CTokenType.STRING_INCLUDE_LITERAL; }

<YYINITIAL> . { return CTokenType.BAD_CHARACTER; }
