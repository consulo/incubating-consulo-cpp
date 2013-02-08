/* It's an automatically generated code. Do not modify it. */
package org.napile.cpp4idea.qt.lang.lexer;

import org.napile.cpp4idea.lang.psi.CTokens;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

@SuppressWarnings({"ALL"})
%%

%{
  public _QtLexer(){
    this((java.io.Reader)null);
  }

  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzPushbackPos = 0;
    zzAtEOF = offset < zzEndRead;
  }
%}

%unicode
%public
%class _QtLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return;
%eof}

WHITE_SPACE_CHAR=[\ \r\t\f]
NEW_LINE_CHAR=[\n]

IDENTIFIER=[:jletter:] [:jletterdigit:]*

C_STYLE_COMMENT="/*""*"*("/"|([^"/""*"]{COMMENT_TAIL}))?
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
STRING_INCLUDE_LITERAL=\<([^\\\<\r\n]|{ESCAPE_SEQUENCE})*(\>|\\)?
ESCAPE_SEQUENCE=\\[^\r\n]

%%

<YYINITIAL> {WHITE_SPACE_CHAR}+ { return CTokens.WHITE_SPACE; }
<YYINITIAL> {NEW_LINE_CHAR} { return CTokens.NEW_LINE; }

<YYINITIAL> {C_STYLE_COMMENT} { return CTokens.C_STYLE_COMMENT; }
<YYINITIAL> {END_OF_LINE_COMMENT} { return CTokens.END_OF_LINE_COMMENT; }

<YYINITIAL> {LONG_LITERAL} { return CTokens.LONG_LITERAL; }
<YYINITIAL> {INTEGER_LITERAL} { return CTokens.INTEGER_LITERAL; }
<YYINITIAL> {FLOAT_LITERAL} { return CTokens.FLOAT_LITERAL; }
<YYINITIAL> {HEX_FLOAT_LITERAL} { return CTokens.FLOAT_LITERAL; }
<YYINITIAL> {DOUBLE_LITERAL} { return CTokens.DOUBLE_LITERAL; }
<YYINITIAL> {HEX_DOUBLE_LITERAL} { return CTokens.DOUBLE_LITERAL; }

<YYINITIAL> {CHARACTER_LITERAL} { return CTokens.CHARACTER_LITERAL; }
<YYINITIAL> {STRING_LITERAL} { return CTokens.STRING_LITERAL; }

<YYINITIAL> "#include" { return CTokens.S_INCLUDE_KEYWORD; }
<YYINITIAL> "#define" { return CTokens.S_DEFINE_KEYWORD; }
<YYINITIAL> "#ifndef" { return CTokens.S_IFNDEF_KEYWORD; }
<YYINITIAL> "#ifdef" { return CTokens.S_IFDEF_KEYWORD; }
<YYINITIAL> "#endif" { return CTokens.S_ENDIF_KEYWORD; }
<YYINITIAL> "#else" { return CTokens.S_ELSE_KEYWORD; }
<YYINITIAL> "break" { return CTokens.BREAK_KEYWORD; }
<YYINITIAL> "case" { return CTokens.CASE_KEYWORD; }
<YYINITIAL> "class" { return CTokens.CLASS_KEYWORD; }
<YYINITIAL> "continue" { return CTokens.CONTINUE_KEYWORD; }
<YYINITIAL> "default" { return CTokens.DEFAULT_KEYWORD; }
<YYINITIAL> "do" { return CTokens.DO_KEYWORD; }
<YYINITIAL> "else" { return CTokens.ELSE_KEYWORD; }
<YYINITIAL> "for" { return CTokens.FOR_KEYWORD; }
<YYINITIAL> "goto" { return CTokens.GOTO_KEYWORD; }
<YYINITIAL> "if" { return CTokens.IF_KEYWORD; }
<YYINITIAL> "new" { return CTokens.NEW_KEYWORD; }
<YYINITIAL> "public" { return CTokens.PUBLIC_KEYWORD; }
<YYINITIAL> "private" { return CTokens.PRIVATE_KEYWORD; }
<YYINITIAL> "switch" { return CTokens.SWITCH_KEYWORD; }
<YYINITIAL> "return" { return CTokens.RETURN_KEYWORD; }
<YYINITIAL> "namespace" { return CTokens.NAMESPACE_KEYWORD; }
<YYINITIAL> "void" { return CTokens.VOID_KEYWORD; }
<YYINITIAL> "while" { return CTokens.WHILE_KEYWORD; }
<YYINITIAL> "typedef" { return CTokens.TYPEDEF_KEYWORD; }
<YYINITIAL> "enum" { return CTokens.ENUM_KEYWORD; }

<YYINITIAL> "virtual" { return CTokens.VIRTUAL_KEYWORD; }
<YYINITIAL> "explicit" { return CTokens.EXPLICIT_KEYWORD; }
<YYINITIAL> "static" { return CTokens.STATIC_KEYWORD; }
<YYINITIAL> "const" { return CTokens.CONST_KEYWORD; }
<YYINITIAL> "extern" { return CTokens.EXTERN_KEYWORD; }
<YYINITIAL> "signed" { return CTokens.SIGNED_KEYWORD; }
<YYINITIAL> "unsigned" { return CTokens.UNSIGNED_KEYWORD; }

<YYINITIAL> "long" { return CTokens.LONG_KEYWORD; }
<YYINITIAL> "char" { return CTokens.CHAR_KEYWORD; }
<YYINITIAL> "__int64" { return CTokens.__INT64_KEYWORD; }
<YYINITIAL> "int" { return CTokens.INT_KEYWORD; }
<YYINITIAL> "bool" { return CTokens.BOOL_KEYWORD; }

<YYINITIAL> "true" { return CTokens.BOOL_LITERAL; }
<YYINITIAL> "false" { return CTokens.BOOL_LITERAL; }

<YYINITIAL> {IDENTIFIER} { return CTokens.IDENTIFIER; }

<YYINITIAL> "==" { return CTokens.EQEQ; }
<YYINITIAL> "!=" { return CTokens.NE; }
<YYINITIAL> "||" { return CTokens.OROR; }
<YYINITIAL> "++" { return CTokens.PLUSPLUS; }
<YYINITIAL> "->" { return CTokens.ARROW; }
<YYINITIAL> "--" { return CTokens.MINUSMINUS; }

<YYINITIAL> "<" { return CTokens.LT; }
<YYINITIAL> "<=" { return CTokens.LE; }
<YYINITIAL> "<<=" { return CTokens.LTLTEQ; }
<YYINITIAL> "<<" { return CTokens.LTLT; }
<YYINITIAL> ">" { return CTokens.GT; }
<YYINITIAL> "&" { return CTokens.AND; }
<YYINITIAL> "&&" { return CTokens.ANDAND; }

<YYINITIAL> "+=" { return CTokens.PLUSEQ; }
<YYINITIAL> "-=" { return CTokens.MINUSEQ; }
<YYINITIAL> "*=" { return CTokens.ASTERISKEQ; }
<YYINITIAL> "/=" { return CTokens.DIVEQ; }
<YYINITIAL> "&=" { return CTokens.ANDEQ; }
<YYINITIAL> "|=" { return CTokens.OREQ; }
<YYINITIAL> "^=" { return CTokens.XOREQ; }
<YYINITIAL> "%=" { return CTokens.PERCEQ; }

<YYINITIAL> "("   { return CTokens.LPARENTH; }
<YYINITIAL> ")"   { return CTokens.RPARENTH; }
<YYINITIAL> "{"   { return CTokens.LBRACE; }
<YYINITIAL> "}"   { return CTokens.RBRACE; }
<YYINITIAL> "["   { return CTokens.LBRACKET; }
<YYINITIAL> "]"   { return CTokens.RBRACKET; }
<YYINITIAL> ";"   { return CTokens.SEMICOLON; }
<YYINITIAL> ","   { return CTokens.COMMA; }
<YYINITIAL> "..." { return CTokens.ELLIPSIS; }
<YYINITIAL> "."   { return CTokens.DOT; }

<YYINITIAL> "=" { return CTokens.EQ; }
<YYINITIAL> "!" { return CTokens.EXCL; }
<YYINITIAL> "~" { return CTokens.TILDE; }
<YYINITIAL> "?" { return CTokens.QUEST; }
<YYINITIAL> "::" { return CTokens.DOUBLE_COLON; }
<YYINITIAL> ":" { return CTokens.COLON; }
<YYINITIAL> "+" { return CTokens.PLUS; }
<YYINITIAL> "-" { return CTokens.MINUS; }
<YYINITIAL> "*" { return CTokens.ASTERISK; }
<YYINITIAL> "/" { return CTokens.DIV; }
<YYINITIAL> "\\" { return CTokens.NEXT_LINE; }
<YYINITIAL> "|" { return CTokens.OR; }
<YYINITIAL> "^" { return CTokens.XOR; }
<YYINITIAL> "%" { return CTokens.PERC; }
<YYINITIAL> "@" { return CTokens.AT; }

<YYINITIAL> {STRING_INCLUDE_LITERAL} { return CTokens.STRING_INCLUDE_LITERAL; }

<YYINITIAL> . { return CTokens.BAD_CHARACTER; }
