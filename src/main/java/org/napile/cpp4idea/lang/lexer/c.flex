/* It's an automatically generated code. Do not modify it. */
package org.napile.cpp4idea.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

@SuppressWarnings({"ALL"})
%%

%{
  public _CLexer(){
    this((java.io.Reader)null);
  }

  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzPushbackPos = 0;
    zzAtEOF = offset < zzEndRead;
  }
%}

%unicode
%class _CLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return;
%eof}

WHITE_SPACE_CHAR=[\ \n\r\t\f]

IDENTIFIER=[:jletter:] [:jletterdigit:]*

C_STYLE_COMMENT=("/*"[^"*"]{COMMENT_TAIL})|"/*"
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
END_OF_LINE_COMMENT="/""/"[^\r\n]*

DIGIT=[0-9]
DIGIT_OR_UNDERSCORE=[_0-9]
OCTAL_DIGIT=[0-7]
OCTAL_DIGIT_OR_UNDERSCORE=[_0-7]
HEX_DIGIT=[0-9A-Fa-f]
HEX_DIGIT_OR_UNDERSCORE=[_0-9A-Fa-f]
BINARY_DIGIT=[01]
BINARY_DIGIT_OR_UNDERSCORE=[_01]

DIGITS={DIGIT}|({DIGIT}{DIGIT_OR_UNDERSCORE}*{DIGIT})
HEX_DIGITS={HEX_DIGIT}|({HEX_DIGIT}{HEX_DIGIT_OR_UNDERSCORE}*{HEX_DIGIT})
BINARY_DIGITS={BINARY_DIGIT}|({BINARY_DIGIT}{BINARY_DIGIT_OR_UNDERSCORE}*{BINARY_DIGIT})

INTEGER_LITERAL={DECIMAL_INTEGER_LITERAL}|{OCTAL_INTEGER_LITERAL}|{HEX_INTEGER_LITERAL}|{BINARY_INTEGER_LITERAL}
DECIMAL_INTEGER_LITERAL=0|([1-9]({DIGIT_OR_UNDERSCORE}*{DIGIT})?)
OCTAL_INTEGER_LITERAL=0{OCTAL_DIGIT_OR_UNDERSCORE}*{OCTAL_DIGIT}
HEX_INTEGER_LITERAL=0[Xx]{HEX_DIGITS}?
BINARY_INTEGER_LITERAL=0[Bb]{BINARY_DIGITS}?
LONG_LITERAL={INTEGER_LITERAL}[Ll]

FLOAT_LITERAL=({FLOATING_POINT_LITERAL1}|{FLOATING_POINT_LITERAL2}|{FLOATING_POINT_LITERAL3}|{FLOATING_POINT_LITERAL4})[Ff]
DOUBLE_LITERAL=(({FLOATING_POINT_LITERAL1}|{FLOATING_POINT_LITERAL2}|{FLOATING_POINT_LITERAL3})[Dd]?)|({FLOATING_POINT_LITERAL4}[Dd])
FLOATING_POINT_LITERAL1={DIGITS}"."{DIGITS}?{EXPONENT_PART}?
FLOATING_POINT_LITERAL2="."{DIGITS}{EXPONENT_PART}?
FLOATING_POINT_LITERAL3={DIGITS}{EXPONENT_PART}
FLOATING_POINT_LITERAL4={DIGITS}
EXPONENT_PART=[Ee]["+""-"]?{DIGITS}?

HEX_FLOAT_LITERAL={HEX_SIGNIFICAND}{BINARY_EXPONENT}[Ff]
HEX_DOUBLE_LITERAL={HEX_SIGNIFICAND}{BINARY_EXPONENT}[Dd]?
BINARY_EXPONENT=[Pp][+-]?{DIGITS}
HEX_SIGNIFICAND={HEX_INTEGER_LITERAL}|{HEX_INTEGER_LITERAL}.|0[Xx]{HEX_DIGITS}?.{HEX_DIGITS}

CHARACTER_LITERAL="'"([^\\\'\r\n]|{ESCAPE_SEQUENCE})*("'"|\\)?
STRING_LITERAL=\"([^\\\"\r\n]|{ESCAPE_SEQUENCE})*(\"|\\)?
ESCAPE_SEQUENCE=\\[^\r\n]

%%

<YYINITIAL> {WHITE_SPACE_CHAR}+ { return CTokenType.WHITE_SPACE; }

<YYINITIAL> {C_STYLE_COMMENT} { return CTokenType.C_STYLE_COMMENT; }
<YYINITIAL> {END_OF_LINE_COMMENT} { return CTokenType.END_OF_LINE_COMMENT; }

<YYINITIAL> {LONG_LITERAL} { return CTokenType.LONG_LITERAL; }
<YYINITIAL> {INTEGER_LITERAL} { return CTokenType.INTEGER_LITERAL; }
<YYINITIAL> {FLOAT_LITERAL} { return CTokenType.FLOAT_LITERAL; }
<YYINITIAL> {HEX_FLOAT_LITERAL} { return CTokenType.FLOAT_LITERAL; }
<YYINITIAL> {DOUBLE_LITERAL} { return CTokenType.DOUBLE_LITERAL; }
<YYINITIAL> {HEX_DOUBLE_LITERAL} { return CTokenType.DOUBLE_LITERAL; }

<YYINITIAL> {CHARACTER_LITERAL} { return CTokenType.CHARACTER_LITERAL; }
<YYINITIAL> {STRING_LITERAL} { return CTokenType.STRING_LITERAL; }

<YYINITIAL> "break" { return CTokenType.BREAK_KEYWORD; }
<YYINITIAL> "case" { return CTokenType.CASE_KEYWORD; }
<YYINITIAL> "continue" { return CTokenType.CONTINUE_KEYWORD; }
<YYINITIAL> "default" { return CTokenType.DEFAULT_KEYWORD; }
<YYINITIAL> "do" { return CTokenType.DO_KEYWORD; }
<YYINITIAL> "else" { return CTokenType.ELSE_KEYWORD; }
<YYINITIAL> "for" { return CTokenType.FOR_KEYWORD; }
<YYINITIAL> "goto" { return CTokenType.GOTO_KEYWORD; }
<YYINITIAL> "if" { return CTokenType.IF_KEYWORD; }
<YYINITIAL> "new" { return CTokenType.NEW_KEYWORD; }
<YYINITIAL> "public" { return CTokenType.PUBLIC_KEYWORD; }
<YYINITIAL> "switch" { return CTokenType.SWITCH_KEYWORD; }
<YYINITIAL> "return" { return CTokenType.RETURN_KEYWORD; }
<YYINITIAL> "void" { return CTokenType.VOID_KEYWORD; }
<YYINITIAL> "while" { return CTokenType.WHILE_KEYWORD; }

<YYINITIAL> {IDENTIFIER} { return CTokenType.IDENTIFIER; }

<YYINITIAL> "==" { return CTokenType.EQEQ; }
<YYINITIAL> "!=" { return CTokenType.NE; }
<YYINITIAL> "||" { return CTokenType.OROR; }
<YYINITIAL> "++" { return CTokenType.PLUSPLUS; }
<YYINITIAL> "--" { return CTokenType.MINUSMINUS; }

<YYINITIAL> "<" { return CTokenType.LT; }
<YYINITIAL> "<=" { return CTokenType.LE; }
<YYINITIAL> "<<=" { return CTokenType.LTLTEQ; }
<YYINITIAL> "<<" { return CTokenType.LTLT; }
<YYINITIAL> ">" { return CTokenType.GT; }
<YYINITIAL> "&" { return CTokenType.AND; }
<YYINITIAL> "&&" { return CTokenType.ANDAND; }

<YYINITIAL> "+=" { return CTokenType.PLUSEQ; }
<YYINITIAL> "-=" { return CTokenType.MINUSEQ; }
<YYINITIAL> "*=" { return CTokenType.ASTERISKEQ; }
<YYINITIAL> "/=" { return CTokenType.DIVEQ; }
<YYINITIAL> "&=" { return CTokenType.ANDEQ; }
<YYINITIAL> "|=" { return CTokenType.OREQ; }
<YYINITIAL> "^=" { return CTokenType.XOREQ; }
<YYINITIAL> "%=" { return CTokenType.PERCEQ; }

<YYINITIAL> "("   { return CTokenType.LPARENTH; }
<YYINITIAL> ")"   { return CTokenType.RPARENTH; }
<YYINITIAL> "{"   { return CTokenType.LBRACE; }
<YYINITIAL> "}"   { return CTokenType.RBRACE; }
<YYINITIAL> "["   { return CTokenType.LBRACKET; }
<YYINITIAL> "]"   { return CTokenType.RBRACKET; }
<YYINITIAL> ";"   { return CTokenType.SEMICOLON; }
<YYINITIAL> ","   { return CTokenType.COMMA; }
<YYINITIAL> "..." { return CTokenType.ELLIPSIS; }
<YYINITIAL> "."   { return CTokenType.DOT; }

<YYINITIAL> "=" { return CTokenType.EQ; }
<YYINITIAL> "!" { return CTokenType.EXCL; }
<YYINITIAL> "~" { return CTokenType.TILDE; }
<YYINITIAL> "?" { return CTokenType.QUEST; }
<YYINITIAL> ":" { return CTokenType.COLON; }
<YYINITIAL> "+" { return CTokenType.PLUS; }
<YYINITIAL> "-" { return CTokenType.MINUS; }
<YYINITIAL> "*" { return CTokenType.ASTERISK; }
<YYINITIAL> "/" { return CTokenType.DIV; }
<YYINITIAL> "|" { return CTokenType.OR; }
<YYINITIAL> "^" { return CTokenType.XOR; }
<YYINITIAL> "%" { return CTokenType.PERC; }
<YYINITIAL> "@" { return CTokenType.AT; }

<YYINITIAL> . { return CTokenType.BAD_CHARACTER; }

