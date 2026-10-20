/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.cpp.lang.parser;

import consulo.cpp.lang.psi.CElementTypes;
import consulo.cpp.localize.CLocalize;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.parser.PsiBuilder;
import org.jspecify.annotations.Nullable;

/**
 * Declarations: specifiers, declarators, functions, struct/union/enum/class bodies, and the C++ namespace-level
 * forms.
 * <p>
 * A declaration becomes one node per declarator - {@code int a, *b;} is two fields - and the first holds the
 * specifiers, like Java fields do. The node of a declarator is a function definition, a function declaration, a
 * typedef or a variable; its name is its first identifier child.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
public class CDeclarationParsing extends CParsingBase {
    public enum Scope {
        FILE,
        BLOCK,
        MEMBER,
        PARAMETER,
        FOR_INIT
    }

    /**
     * What {@link #parseTypeName()} found.
     */
    public enum TypeName {
        NONE,
        /**
         * Certainly a type: it has a type keyword, a qualifier, a tag or a declarator part.
         */
        DEFINITE,
        /**
         * A bare name known to be a type.
         */
        KNOWN_NAME,
        /**
         * A bare name of which nothing is known - {@code (x)} may be a cast or not.
         */
        UNKNOWN_NAME
    }

    private static final TokenSet AGGREGATE_KEYWORDS = TokenSet.create(STRUCT_KEYWORD, UNION_KEYWORD, ENUM_KEYWORD, CLASS_KEYWORD);

    private static final TokenSet ATTRIBUTE_KEYWORDS = TokenSet.create(ATTRIBUTE_KEYWORD, DECLSPEC_KEYWORD, ALIGNAS_KEYWORD);

    /**
     * What a declaration's specifiers held.
     */
    private static final class Specifiers {
        private boolean myTypedef;
        private boolean myModifiers;
        private boolean myType;
        private boolean myStandalone;
    }

    private static final class Declarator {
        private @Nullable String myName;
        private boolean myFunction;
        private boolean myConsumed;
    }

    public CDeclarationParsing(CParsingContext context) {
        super(context);
    }

    // -----------------------------------------------------------------------
    // File and namespace level
    // -----------------------------------------------------------------------

    public void parseFile() {
        while (!eof()) {
            parseExternalDeclaration();
        }
    }

    /**
     * One declaration at file or namespace level; always consumes at least one token.
     */
    protected void parseExternalDeclaration() {
        if (consume(SEMICOLON)) {
            return;
        }

        if (!parseDeclaration(Scope.FILE)) {
            PsiBuilder.Marker error = mark();
            advance();
            error.error(CLocalize.declarationExpected());
        }
    }

    /**
     * Parses a declaration; returns {@code false}, consuming nothing, when there is none here.
     */
    public boolean parseDeclaration(Scope scope) {
        IElementType type = token();

        if (type == STATIC_ASSERT_KEYWORD) {
            parseStaticAssert();
            return true;
        }
        if (type == ASM_KEYWORD) {
            parseAsm();
            return true;
        }
        if (isCpp() && scope != Scope.PARAMETER) {
            if (type == NAMESPACE_KEYWORD) {
                parseNamespace();
                return true;
            }
            if (type == USING_KEYWORD) {
                parseUsing();
                return true;
            }
            if (type == TEMPLATE_KEYWORD) {
                parseTemplate(scope);
                return true;
            }
            if (type == EXTERN_KEYWORD && lookAhead(1) == STRING_LITERAL) {
                parseLinkageSpecification(scope);
                return true;
            }
        }

        PsiBuilder.Marker node = mark();
        Specifiers specifiers = parseSpecifiers(scope);
        if (specifiers.myStandalone) {
            node.drop();
            return true;
        }

        boolean first = true;
        while (true) {
            Declarator declarator = parseDeclarator(scope, false);

            if (first && !specifiers.myType && !specifiers.myModifiers && declarator.myName == null) {
                node.rollbackTo();
                return false;
            }

            if (declarator.myName == null && !declarator.myConsumed && !(scope == Scope.MEMBER && at(COLON))) {
                error(CLocalize.identifierExpected());
            }

            if (specifiers.myTypedef && declarator.myName != null) {
                myContext.addTypeName(declarator.myName);
            }

            IElementType nodeType = nodeType(scope, specifiers, declarator);

            if (first && declarator.myFunction && (scope == Scope.FILE || scope == Scope.MEMBER) && isFunctionBodyStart()) {
                parseFunctionBody();
                node.done(CElementTypes.IMPLEMENTING_METHOD);
                return true;
            }

            parseDeclaratorTail(scope, declarator);

            if (at(COMMA)) {
                node.done(nodeType);
                advance();
                node = mark();
                first = false;
                continue;
            }

            if (scope == Scope.FOR_INIT && at(COLON)) {
                // for (int x : range) - the caller takes the colon
                node.done(nodeType);
                return true;
            }

            if (!consume(SEMICOLON)) {
                error(CLocalize.semicolonExpected());
            }
            node.done(nodeType);
            return true;
        }
    }

    private IElementType nodeType(Scope scope, Specifiers specifiers, Declarator declarator) {
        if (specifiers.myTypedef) {
            return CElementTypes.TYPE_DECLARATION;
        }
        if (declarator.myFunction) {
            if (scope == Scope.MEMBER && !specifiers.myType) {
                return CElementTypes.DECLARATION_CONSTRUCTOR;
            }
            return CElementTypes.DECLARATION_METHOD;
        }
        return switch (scope) {
            case FILE -> CElementTypes.FIELD;
            case MEMBER -> CElementTypes.DECLARATION_FIELD;
            case PARAMETER -> CElementTypes.PARAMETER;
            case BLOCK, FOR_INIT -> CElementTypes.LOCAL_VARIABLE;
        };
    }

    private boolean isFunctionBodyStart() {
        return at(LBRACE) || isCpp() && (at(COLON) || at(TRY_KEYWORD));
    }

    /**
     * The body of a function definition, with a C++ constructor's member initializers before it.
     */
    private void parseFunctionBody() {
        boolean functionTry = consume(TRY_KEYWORD);

        if (consume(COLON)) {
            // : member(args), base{args}
            while (!eof() && !at(LBRACE)) {
                if (at(IDENTIFIER) || at(DOUBLE_COLON)) {
                    parseQualifiedName();
                }
                else {
                    error(CLocalize.identifierExpected());
                    break;
                }

                if (at(LPARENTH)) {
                    expressions().parseArgumentList();
                }
                else if (at(LBRACE)) {
                    expressions().parseInitializerList();
                }
                consume(ELLIPSIS);

                if (!consume(COMMA)) {
                    break;
                }
            }
        }

        if (at(LBRACE)) {
            statements().parseCodeBlock();
        }
        else {
            error(CLocalize.lbraceExpected());
        }

        while (functionTry && at(CATCH_KEYWORD)) {
            advance();
            if (at(LPARENTH)) {
                parseParameterList();
            }
            if (!at(LBRACE)) {
                break;
            }
            statements().parseCodeBlock();
        }
    }

    /**
     * After the declarator: a bit-field width, attributes, the initializer.
     */
    private void parseDeclaratorTail(Scope scope, Declarator declarator) {
        if (scope == Scope.MEMBER && !declarator.myFunction && consume(COLON)) {
            expressions().parseConditionalExpressionOrError();
        }

        parseAttributes();

        if (consume(EQ)) {
            if (isCpp() && (at(DEFAULT_KEYWORD) || at(DELETE_KEYWORD))) {
                // = default, = delete
                advance();
            }
            else if (!expressions().parseInitializer()) {
                error(CLocalize.expressionExpected());
            }
        }
        else if (isCpp() && !declarator.myFunction && at(LBRACE)) {
            // int x{1};
            expressions().parseInitializerList();
        }
    }

    // -----------------------------------------------------------------------
    // Specifiers
    // -----------------------------------------------------------------------

    /**
     * Storage classes, qualifiers and attributes, then the type. The modifiers before the type become a modifier
     * list - always present, so the type stays the second child - except before a struct/union/enum definition with
     * none, which can then be the whole declaration: {@code struct S { ... };}.
     */
    private Specifiers parseSpecifiers(Scope scope) {
        Specifiers specifiers = new Specifiers();

        PsiBuilder.Marker modifiers = mark();
        parseModifiers(specifiers);

        if (!specifiers.myModifiers && at(AGGREGATE_KEYWORDS)) {
            modifiers.drop();

            boolean standaloneAllowed = scope == Scope.FILE || scope == Scope.BLOCK || scope == Scope.MEMBER;
            specifiers.myStandalone = parseAggregate(standaloneAllowed);
            specifiers.myType = true;
            parseTrailingModifiers();
            return specifiers;
        }

        modifiers.done(CElementTypes.MODIFIER_LIST);

        specifiers.myType = parseTypeSpecifier(scope);
        return specifiers;
    }

    private void parseModifiers(Specifiers specifiers) {
        while (true) {
            if (at(MODIFIERS)) {
                advance();
                specifiers.myModifiers = true;
            }
            else if (at(TYPEDEF_KEYWORD)) {
                advance();
                specifiers.myTypedef = true;
                specifiers.myModifiers = true;
            }
            else if (isAttributeStart()) {
                parseAttribute();
                specifiers.myModifiers = true;
            }
            else if (isUnknownMacroModifier()) {
                // a macro whose definition is not visible, in front of the type: API int f();
                advance();
                specifiers.myModifiers = true;
            }
            else {
                return;
            }
        }
    }

    /**
     * An identifier followed by a keyword which can only continue the specifiers - {@code EXPORT int} - is taken
     * for a macro standing for an attribute or a calling convention.
     */
    private boolean isUnknownMacroModifier() {
        if (!at(IDENTIFIER) || myContext.isTypeName(tokenText())) {
            return false;
        }
        IElementType next = lookAhead(1);
        return TYPES.contains(next) || MODIFIERS.contains(next) || AGGREGATE_KEYWORDS.contains(next) || next == TYPEDEF_KEYWORD;
    }

    private void parseTrailingModifiers() {
        while (at(MODIFIERS) || isAttributeStart()) {
            if (isAttributeStart()) {
                parseAttribute();
            }
            else {
                advance();
            }
        }
    }

    /**
     * The type part of the specifiers, as a type reference or a struct/union/enum: type keywords combined with each
     * other ({@code unsigned long long int}), or one type name; qualifiers may be mixed in. Returns whether there was
     * a type.
     */
    private boolean parseTypeSpecifier(Scope scope) {
        if (at(AGGREGATE_KEYWORDS)) {
            parseAggregate(false);
            parseTrailingModifiers();
            return true;
        }

        PsiBuilder.Marker typeRef = mark();
        boolean keyword = false;
        boolean name = false;

        while (true) {
            if (at(TYPES)) {
                advance();
                keyword = true;
            }
            else if (at(MODIFIERS) && (keyword || name)) {
                // int const, char static
                advance();
            }
            else if (isAttributeStart()) {
                parseAttribute();
            }
            else if (!keyword && !name && (at(TYPEOF_KEYWORD) || at(DECLTYPE_KEYWORD))) {
                advance();
                if (at(LPARENTH)) {
                    skipBalanced();
                }
                name = true;
            }
            else if (!keyword && !name && at(AUTO_KEYWORD)) {
                advance();
                name = true;
            }
            else if (!keyword && !name && isTypeNameStart(scope)) {
                parseQualifiedName();
                name = true;
            }
            else {
                break;
            }
        }

        if (keyword || name) {
            typeRef.done(CElementTypes.TYPE_REF);
            return true;
        }
        typeRef.drop();
        return false;
    }

    /**
     * At an identifier in the specifiers: whether it names the type, or is already the declarator. Without the
     * headers this is decided by what follows - {@code T x}, {@code T *x}, {@code T (*fp)(...)} - and by the type
     * names seen so far.
     */
    private boolean isTypeNameStart(Scope scope) {
        if (isCpp() && at(DOUBLE_COLON)) {
            return true;
        }
        if (!at(IDENTIFIER)) {
            return false;
        }

        IElementType next = lookAhead(1);
        if (myContext.isTypeName(tokenText())) {
            // a known type, unless it is redeclared: typedef int T; ... int T;
            return next != SEMICOLON && next != EQ && next != COMMA || scope == Scope.PARAMETER;
        }

        if (next == IDENTIFIER || ATTRIBUTE_KEYWORDS.contains(next) || MODIFIERS.contains(next)) {
            return true;
        }
        if (next == ASTERISK || isCpp() && (next == AND || next == ANDAND)) {
            return true;
        }
        if (isCpp() && (next == DOUBLE_COLON || next == LT)) {
            return isQualifiedTypeAhead();
        }
        if (next == LPARENTH) {
            IElementType afterParenthesis = lookAhead(2);
            return afterParenthesis == ASTERISK || afterParenthesis == XOR;
        }
        if (scope == Scope.PARAMETER) {
            // f(T), f(T, U), f(T[])
            return next == COMMA || next == RPARENTH || next == LBRACKET;
        }
        return false;
    }

    /**
     * At {@code A::...} or {@code A<...>}: whether the qualified name is a type - followed by a declarator - rather
     * than itself the declarator, as in {@code Foo::Foo()} and {@code void Foo::bar()}.
     */
    private boolean isQualifiedTypeAhead() {
        PsiBuilder.Marker speculation = mark();
        try {
            parseQualifiedName();
            return at(IDENTIFIER) || at(ASTERISK) || at(AND) || at(ANDAND) || at(ELLIPSIS) || at(MODIFIERS)
                || at(LPARENTH) && (lookAhead(1) == ASTERISK || lookAhead(1) == AND);
        }
        finally {
            speculation.rollbackTo();
        }
    }

    /**
     * {@code ::? name <args>? (:: name <args>?)*}
     */
    protected void parseQualifiedName() {
        consume(DOUBLE_COLON);
        while (true) {
            consume(TEMPLATE_KEYWORD);
            if (!consume(IDENTIFIER)) {
                error(CLocalize.identifierExpected());
                return;
            }
            if (isCpp() && at(LT)) {
                parseTemplateArguments();
            }
            if (isCpp() && at(DOUBLE_COLON) && (lookAhead(1) == IDENTIFIER || lookAhead(1) == TEMPLATE_KEYWORD)) {
                advance();
            }
            else {
                return;
            }
        }
    }

    /**
     * {@code < ... >}, nested ones and {@code >>} included; parsed for their extent only.
     */
    protected void parseTemplateArguments() {
        int depth = 0;
        do {
            IElementType type = token();
            if (type == LT) {
                depth++;
            }
            else if (type == GT) {
                depth--;
            }
            else if (type == GTGT) {
                depth -= 2;
            }
            else if (type == LPARENTH || type == LBRACKET) {
                skipBalanced();
                continue;
            }
            else if (type == SEMICOLON || type == LBRACE || type == RBRACE) {
                error(CLocalize.gtExpected());
                return;
            }
            advance();
        }
        while (depth > 0 && !eof());
    }

    // -----------------------------------------------------------------------
    // Declarators
    // -----------------------------------------------------------------------

    /**
     * Pointers, then the name - or a parenthesised declarator - then array and function suffixes.
     */
    private Declarator parseDeclarator(Scope scope, boolean abstractAllowed) {
        Declarator declarator = new Declarator();

        while (at(ASTERISK) || at(XOR) || isCpp() && (at(AND) || at(ANDAND))) {
            advance();
            declarator.myConsumed = true;
            while (at(POINTER_QUALIFIERS) || isAttributeStart()) {
                if (isAttributeStart()) {
                    parseAttribute();
                }
                else {
                    advance();
                }
            }
        }

        boolean nested = false;
        if (at(IDENTIFIER) || isCpp() && (at(TILDE) || at(OPERATOR_KEYWORD) || at(DOUBLE_COLON))) {
            declarator.myName = parseDeclaratorName();
            declarator.myConsumed = true;
        }
        else if (at(LPARENTH) && isNestedDeclarator()) {
            advance();
            Declarator inner = parseDeclarator(scope, abstractAllowed);
            expect(RPARENTH, CLocalize.rparenthExpected());
            declarator.myName = inner.myName;
            declarator.myConsumed = true;
            nested = true;
        }
        else if (isCpp() && at(ELLIPSIS)) {
            // a parameter pack: T... args
            advance();
            declarator.myConsumed = true;
            if (at(IDENTIFIER)) {
                declarator.myName = parseDeclaratorName();
            }
        }

        boolean firstSuffix = true;
        while (true) {
            if (at(LBRACKET) && !(isCpp() && lookAhead(1) == LBRACKET)) {
                parseArraySuffix();
            }
            else if (at(LPARENTH)) {
                parseParameterList();
                if (firstSuffix && !nested && declarator.myName != null) {
                    declarator.myFunction = true;
                }
                parseFunctionQualifiers();
            }
            else {
                break;
            }
            declarator.myConsumed = true;
            firstSuffix = false;
        }

        parseAttributes();
        if (at(ASM_KEYWORD)) {
            // int x asm("label");
            parseAsmLabel();
        }
        return declarator;
    }

    /**
     * A declarator name: an identifier - in C++ qualified ({@code Foo::bar}), a destructor ({@code ~Foo}) or an
     * operator ({@code operator+}). The qualifier becomes a type reference, so the name is the declaration's first
     * identifier child.
     */
    private @Nullable String parseDeclaratorName() {
        if (isCpp() && (at(DOUBLE_COLON) || at(IDENTIFIER) && (lookAhead(1) == DOUBLE_COLON || lookAhead(1) == LT && isQualifierAhead()))) {
            PsiBuilder.Marker qualifier = mark();
            consume(DOUBLE_COLON);
            while (at(IDENTIFIER) && (lookAhead(1) == DOUBLE_COLON || lookAhead(1) == LT)) {
                advance();
                if (at(LT)) {
                    parseTemplateArguments();
                }
                if (!consume(DOUBLE_COLON)) {
                    break;
                }
            }
            qualifier.done(CElementTypes.DOUBLE_COLON_TYPE_REF);
        }

        if (consume(TILDE)) {
            String name = tokenText();
            expect(IDENTIFIER, CLocalize.identifierExpected());
            return "~" + name;
        }

        if (at(OPERATOR_KEYWORD)) {
            parseOperatorName();
            return "operator";
        }

        String name = tokenText();
        if (!consume(IDENTIFIER)) {
            error(CLocalize.identifierExpected());
            return null;
        }
        if (isCpp() && at(LT) && isTemplateArgumentsAhead()) {
            // an explicit specialization, f<int>(...)
            parseTemplateArguments();
        }
        return name;
    }

    private boolean isQualifierAhead() {
        PsiBuilder.Marker speculation = mark();
        try {
            advance();
            parseTemplateArguments();
            return at(DOUBLE_COLON);
        }
        finally {
            speculation.rollbackTo();
        }
    }

    private boolean isTemplateArgumentsAhead() {
        PsiBuilder.Marker speculation = mark();
        try {
            parseTemplateArguments();
            return at(LPARENTH);
        }
        finally {
            speculation.rollbackTo();
        }
    }

    /**
     * {@code operator+}, {@code operator()}, {@code operator[]}, {@code operator new[]}, {@code operator int}...
     */
    private void parseOperatorName() {
        advance();
        if (at(LPARENTH) || at(LBRACKET)) {
            advance();
            consume(RPARENTH);
            consume(RBRACKET);
        }
        else if (at(NEW_KEYWORD) || at(DELETE_KEYWORD)) {
            advance();
            if (at(LBRACKET)) {
                advance();
                consume(RBRACKET);
            }
        }
        else if (at(OPERATION_SET) || at(COMMA) || at(ARROW) || at(GE) || at(GTGT) || at(GTGTEQ)) {
            advance();
        }
        else {
            // a conversion operator, operator bool
            parseTypeName();
        }
    }

    /**
     * At {@code (} after the pointers: whether it opens a nested declarator - {@code (*fp)}, {@code (&ref)} - rather
     * than a parameter list.
     */
    private boolean isNestedDeclarator() {
        IElementType next = lookAhead(1);
        if (next == ASTERISK || next == XOR || isCpp() && (next == AND || next == ANDAND)) {
            return true;
        }
        // int (name)(...), a parenthesised name
        return next == IDENTIFIER && lookAhead(2) == RPARENTH && (lookAhead(3) == LPARENTH || lookAhead(3) == LBRACKET);
    }

    private void parseArraySuffix() {
        advance();
        // C99: int a[static 10], int a[const]
        while (at(MODIFIERS)) {
            advance();
        }
        if (!at(RBRACKET)) {
            if (at(ASTERISK) && lookAhead(1) == RBRACKET) {
                advance();
            }
            else {
                expressions().parseAssignmentExpressionOrError();
            }
        }
        expect(RBRACKET, CLocalize.rbracketExpected());
    }

    /**
     * After the parameters of a C++ function: {@code const}, {@code &}, {@code noexcept(...)}, {@code throw(...)},
     * {@code override}, {@code final}, {@code -> type}.
     */
    private void parseFunctionQualifiers() {
        if (!isCpp()) {
            return;
        }
        while (true) {
            if (at(POINTER_QUALIFIERS) || at(AND) || at(ANDAND)) {
                advance();
            }
            else if (at(NOEXCEPT_KEYWORD) || at(THROW_KEYWORD)) {
                advance();
                if (at(LPARENTH)) {
                    skipBalanced();
                }
            }
            else if (at(IDENTIFIER) && ("override".equals(tokenText()) || "final".equals(tokenText()))) {
                advance();
            }
            else if (at(ARROW)) {
                advance();
                parseTypeName();
            }
            else {
                return;
            }
        }
    }

    /**
     * {@code ( parameter, ... )}
     */
    public void parseParameterList() {
        PsiBuilder.Marker marker = mark();
        advance();

        if (!at(RPARENTH)) {
            while (true) {
                if (at(ELLIPSIS)) {
                    PsiBuilder.Marker parameter = mark();
                    advance();
                    parameter.done(CElementTypes.PARAMETER);
                }
                else if (!parseParameter()) {
                    skipUntil(TokenSet.create(COMMA, RPARENTH, SEMICOLON, LBRACE), CLocalize.typeExpected());
                }

                if (!consume(COMMA)) {
                    break;
                }
            }
        }

        expect(RPARENTH, CLocalize.rparenthExpected());
        marker.done(CElementTypes.PARAMETER_LIST);
    }

    private boolean parseParameter() {
        PsiBuilder.Marker parameter = mark();
        Specifiers specifiers = parseSpecifiers(Scope.PARAMETER);
        Declarator declarator = parseDeclarator(Scope.PARAMETER, true);

        if (!specifiers.myType && !specifiers.myModifiers && !declarator.myConsumed) {
            parameter.rollbackTo();
            return false;
        }

        if (consume(EQ)) {
            // a C++ default argument
            if (!expressions().parseInitializer()) {
                error(CLocalize.expressionExpected());
            }
        }
        parameter.done(CElementTypes.PARAMETER);
        return true;
    }

    // -----------------------------------------------------------------------
    // Type names
    // -----------------------------------------------------------------------

    /**
     * A type name, as in a cast or {@code sizeof}: specifiers and an abstract declarator. Reports what it found;
     * consumes nothing when there is no type name here.
     */
    public TypeName parseTypeName() {
        IElementType start = token();
        boolean definite = at(TYPES) || at(MODIFIERS) || at(AGGREGATE_KEYWORDS) || start == TYPEOF_KEYWORD
            || start == DECLTYPE_KEYWORD || start == TYPENAME_KEYWORD || isAttributeStart();
        boolean known = start == IDENTIFIER && myContext.isTypeName(tokenText());

        if (!definite && start != IDENTIFIER && !(isCpp() && start == DOUBLE_COLON)) {
            return TypeName.NONE;
        }

        PsiBuilder.Marker modifiers = mark();
        boolean hasModifiers = false;
        while (at(MODIFIERS) || isAttributeStart()) {
            if (isAttributeStart()) {
                parseAttribute();
            }
            else {
                advance();
            }
            hasModifiers = true;
        }
        if (hasModifiers) {
            modifiers.done(CElementTypes.MODIFIER_LIST);
        }
        else {
            modifiers.drop();
        }

        consume(TYPENAME_KEYWORD);

        if (at(AGGREGATE_KEYWORDS)) {
            parseAggregate(false);
            parseTrailingModifiers();
        }
        else {
            PsiBuilder.Marker typeRef = mark();
            boolean keyword = false;
            boolean name = false;
            while (true) {
                if (at(TYPES)) {
                    advance();
                    keyword = true;
                }
                else if (at(MODIFIERS) && (keyword || name)) {
                    advance();
                }
                else if (!keyword && !name && (at(TYPEOF_KEYWORD) || at(DECLTYPE_KEYWORD))) {
                    advance();
                    if (at(LPARENTH)) {
                        skipBalanced();
                    }
                    name = true;
                }
                else if (!keyword && !name && (at(IDENTIFIER) || isCpp() && at(DOUBLE_COLON))) {
                    parseQualifiedName();
                    name = true;
                }
                else {
                    break;
                }
            }
            if (keyword || name) {
                typeRef.done(CElementTypes.TYPE_REF);
            }
            else {
                typeRef.drop();
            }
        }

        Declarator declarator = parseDeclarator(Scope.PARAMETER, true);
        if (declarator.myName != null) {
            // a type name declares nothing: (a b) is no type
            return TypeName.NONE;
        }
        if (declarator.myConsumed || definite) {
            return TypeName.DEFINITE;
        }
        return known ? TypeName.KNOWN_NAME : TypeName.UNKNOWN_NAME;
    }

    /**
     * Whether a type name certainly starts here - used where an expression could also be, such as a macro
     * argument.
     */
    public boolean startsTypeNameDefinitely() {
        IElementType type = token();
        return at(TYPES) && !(isCpp() && (lookAhead(1) == LPARENTH || lookAhead(1) == LBRACE))
            || at(AGGREGATE_KEYWORDS)
            || type == CONST_KEYWORD || type == VOLATILE_KEYWORD;
    }

    // -----------------------------------------------------------------------
    // Statement level
    // -----------------------------------------------------------------------

    /**
     * At the start of a statement: whether it is a declaration rather than an expression. Certain for a keyword which
     * only starts a declaration; for a name, decided by what follows it - {@code T x}, {@code T *x = ...} - since
     * {@code a * b;} alone does nothing.
     */
    public boolean looksLikeDeclaration() {
        IElementType type = token();
        if (type == EXTENSION_KEYWORD && lookAhead(1) == LPARENTH) {
            // __extension__ ({ ... }) - an expression
            return false;
        }
        if (at(MODIFIERS) || at(TYPES) || at(AGGREGATE_KEYWORDS) || isAttributeStart()
            || type == TYPEDEF_KEYWORD || type == TYPEOF_KEYWORD || type == AUTO_KEYWORD) {
            return !(isCpp() && at(TYPES) && (lookAhead(1) == LPARENTH || lookAhead(1) == LBRACE));
        }
        if (isCpp() && (type == USING_KEYWORD || type == TEMPLATE_KEYWORD || type == NAMESPACE_KEYWORD || type == DECLTYPE_KEYWORD)) {
            return true;
        }

        if (type != IDENTIFIER && !(isCpp() && type == DOUBLE_COLON)) {
            return false;
        }

        IElementType next = lookAhead(1);
        if (type == IDENTIFIER) {
            if (next == IDENTIFIER) {
                return true;
            }
            if (myContext.isTypeName(tokenText()) && (next == ASTERISK || isCpp() && (next == AND || next == ANDAND))) {
                return true;
            }
            if (next == ASTERISK || isCpp() && (next == AND || next == ANDAND)) {
                // T *x;  T *x = ...;  T **x, *y;
                int step = 2;
                while (lookAhead(step) == ASTERISK || POINTER_QUALIFIERS.contains(lookAhead(step))) {
                    step++;
                }
                if (lookAhead(step) == IDENTIFIER) {
                    IElementType after = lookAhead(step + 1);
                    return after == SEMICOLON || after == EQ || after == COMMA || after == LBRACKET;
                }
                return false;
            }
            if (myContext.isTypeName(tokenText()) && next == LPARENTH) {
                // T (*fp)(...)
                return lookAhead(2) == ASTERISK;
            }
            if (isUnknownMacroModifier()) {
                return true;
            }
        }

        if (isCpp() && (next == DOUBLE_COLON || next == LT || type == DOUBLE_COLON)) {
            PsiBuilder.Marker speculation = mark();
            try {
                parseQualifiedName();
                if (at(IDENTIFIER)) {
                    IElementType after = lookAhead(1);
                    return after != LPARENTH || myContext.isTypeName(tokenText());
                }
                if (at(ASTERISK) || at(AND) || at(ANDAND)) {
                    return lookAhead(1) == IDENTIFIER && (lookAhead(2) == SEMICOLON || lookAhead(2) == EQ || lookAhead(2) == COMMA);
                }
                return false;
            }
            finally {
                speculation.rollbackTo();
            }
        }
        return false;
    }

    // -----------------------------------------------------------------------
    // Struct, union, enum, class
    // -----------------------------------------------------------------------

    /**
     * {@code struct Name { members }}, or {@code struct Name} alone - an elaborated type reference. Returns whether
     * it was a whole declaration, {@code struct S { ... };}, which only {@code standaloneAllowed} permits.
     */
    private boolean parseAggregate(boolean standaloneAllowed) {
        PsiBuilder.Marker marker = mark();
        IElementType keyword = token();
        advance();

        if (keyword == ENUM_KEYWORD && (at(CLASS_KEYWORD) || at(STRUCT_KEYWORD))) {
            // enum class
            advance();
        }
        parseAttributes();

        String name = null;
        if (at(IDENTIFIER) || isCpp() && at(DOUBLE_COLON)) {
            name = tokenText();
            parseQualifiedName();
        }
        if (isCpp() && at(IDENTIFIER) && "final".equals(tokenText())) {
            advance();
        }

        if (keyword == ENUM_KEYWORD && at(COLON)) {
            // the underlying type, enum E : int
            advance();
            parseTypeName();
        }
        else if (keyword != ENUM_KEYWORD && isCpp() && at(COLON)) {
            parseBaseClause();
        }

        if (!at(LBRACE)) {
            marker.done(CElementTypes.TYPE_REF);
            return false;
        }

        if (keyword == ENUM_KEYWORD) {
            parseEnumBody();
        }
        else {
            parseMemberBody();
        }

        if (isCpp() && name != null) {
            myContext.addTypeName(name);
        }

        IElementType nodeType;
        if (keyword == ENUM_KEYWORD) {
            nodeType = CElementTypes.ENUM;
        }
        else if (keyword == UNION_KEYWORD) {
            nodeType = CElementTypes.UNION_DECLARATION;
        }
        else if (keyword == CLASS_KEYWORD) {
            nodeType = CElementTypes.CLASS;
        }
        else {
            nodeType = CElementTypes.STRUCT_DECLARATION;
        }

        boolean standalone = standaloneAllowed && consume(SEMICOLON);
        marker.done(nodeType);
        return standalone;
    }

    /**
     * {@code : public Base, virtual protected Other<T>}
     */
    private void parseBaseClause() {
        PsiBuilder.Marker list = mark();
        advance();

        while (true) {
            PsiBuilder.Marker base = mark();
            while (at(ACCESS_MODIFIERS) || at(VIRTUAL_KEYWORD)) {
                advance();
            }
            if (at(IDENTIFIER) || at(DOUBLE_COLON)) {
                parseQualifiedName();
            }
            else {
                error(CLocalize.identifierExpected());
            }
            consume(ELLIPSIS);
            base.done(CElementTypes.SUPER_CLASS);

            if (!consume(COMMA)) {
                break;
            }
        }
        list.done(CElementTypes.SUPER_CLASS_LIST);
    }

    private void parseEnumBody() {
        advance();

        while (!eof() && !at(RBRACE)) {
            if (at(IDENTIFIER)) {
                PsiBuilder.Marker constant = mark();
                advance();
                parseAttributes();
                if (consume(EQ)) {
                    expressions().parseConditionalExpressionOrError();
                }
                constant.done(CElementTypes.ENUM_CONSTANT);
            }
            else {
                skipUntil(TokenSet.create(COMMA, RBRACE, SEMICOLON), CLocalize.identifierExpected());
                if (at(SEMICOLON)) {
                    break;
                }
            }

            if (!consume(COMMA)) {
                break;
            }
        }

        expect(RBRACE, CLocalize.rbraceExpected());
    }

    /**
     * The members of a struct, union or class; in a C++ class the members after an access specifier are grouped
     * under it.
     */
    private void parseMemberBody() {
        advance();

        while (!eof() && !at(RBRACE)) {
            if (isAccessSpecifier()) {
                PsiBuilder.Marker section = mark();
                advance();
                advance();
                while (!eof() && !at(RBRACE) && !isAccessSpecifier()) {
                    parseMember();
                }
                section.done(CElementTypes.ACCESSOR_OWNER);
            }
            else {
                parseMember();
            }
        }

        expect(RBRACE, CLocalize.rbraceExpected());
    }

    private boolean isAccessSpecifier() {
        return isCpp() && at(ACCESS_MODIFIERS) && lookAhead(1) == COLON;
    }

    private void parseMember() {
        if (consume(SEMICOLON)) {
            return;
        }
        if (isCpp() && at(FRIEND_KEYWORD) && (lookAhead(1) == CLASS_KEYWORD || lookAhead(1) == STRUCT_KEYWORD)) {
            // friend class X;
            PsiBuilder.Marker marker = mark();
            advance();
            advance();
            parseQualifiedName();
            expect(SEMICOLON, CLocalize.semicolonExpected());
            marker.done(CElementTypes.DECLARATION);
            return;
        }
        if (!parseDeclaration(Scope.MEMBER)) {
            PsiBuilder.Marker error = mark();
            advance();
            error.error(CLocalize.declarationExpected());
        }
    }

    // -----------------------------------------------------------------------
    // Attributes, static_assert, asm
    // -----------------------------------------------------------------------

    private boolean isAttributeStart() {
        return at(ATTRIBUTE_KEYWORDS) || isCpp() && at(LBRACKET) && lookAhead(1) == LBRACKET;
    }

    private void parseAttributes() {
        while (isAttributeStart()) {
            parseAttribute();
        }
    }

    /**
     * {@code __attribute__((...))}, {@code __declspec(...)}, {@code alignas(...)}, {@code [[...]]}
     */
    private void parseAttribute() {
        PsiBuilder.Marker marker = mark();
        if (at(LBRACKET)) {
            skipBalanced();
        }
        else {
            advance();
            if (at(LPARENTH)) {
                skipBalanced();
            }
        }
        marker.done(CElementTypes.ATTRIBUTE_LIST);
    }

    /**
     * {@code static_assert(condition, "message");}
     */
    public void parseStaticAssert() {
        PsiBuilder.Marker marker = mark();
        advance();

        if (expect(LPARENTH, CLocalize.lparenthExpected())) {
            expressions().parseConditionalExpressionOrError();
            if (consume(COMMA)) {
                expressions().parseAssignmentExpressionOrError();
            }
            expect(RPARENTH, CLocalize.rparenthExpected());
        }
        expect(SEMICOLON, CLocalize.semicolonExpected());
        marker.done(CElementTypes.STATIC_ASSERT);
    }

    /**
     * {@code asm volatile ("..." : outputs : inputs : clobbers);}
     */
    public void parseAsm() {
        PsiBuilder.Marker marker = mark();
        advance();

        // volatile, inline, goto
        while (at(VOLATILE_KEYWORD) || at(INLINE_KEYWORD) || at(GOTO_KEYWORD)) {
            advance();
        }
        if (at(LPARENTH)) {
            advance();
            parseAsmTemplate();
            // operands, clobbers and goto labels, up to the closing parenthesis
            while (!eof() && !at(RPARENTH)) {
                if (at(LPARENTH) || at(LBRACKET) || at(LBRACE)) {
                    skipBalanced();
                }
                else {
                    advance();
                }
            }
            consume(RPARENTH);
        }
        else if (at(LBRACE)) {
            // MSVC, __asm { ... }
            PsiBuilder.Marker block = mark();
            skipBalanced();
            block.done(CElementTypes.ASM_BLOCK);
        }
        consume(SEMICOLON);
        marker.done(CElementTypes.ASM);
    }

    /**
     * The assembly of {@code asm("...")}: adjacent string literals, and the macros among them which expand to more of
     * them - {@code LOCK_PREFIX "xadd %0, %1"}.
     */
    private void parseAsmTemplate() {
        if (!at(STRING_LITERAL) && !at(IDENTIFIER)) {
            return;
        }
        PsiBuilder.Marker template = mark();
        while (at(STRING_LITERAL) || at(IDENTIFIER)) {
            advance();
        }
        template.done(CElementTypes.ASM_TEMPLATE);
    }

    private void parseAsmLabel() {
        PsiBuilder.Marker marker = mark();
        advance();
        if (at(LPARENTH)) {
            skipBalanced();
        }
        marker.done(CElementTypes.ASM);
    }

    // -----------------------------------------------------------------------
    // C++ namespace level
    // -----------------------------------------------------------------------

    /**
     * {@code namespace a::b { ... }}, {@code namespace alias = a::b;}
     */
    private void parseNamespace() {
        PsiBuilder.Marker marker = mark();
        advance();

        if (at(IDENTIFIER)) {
            parseQualifiedName();
        }
        parseAttributes();

        if (consume(EQ)) {
            parseQualifiedName();
            expect(SEMICOLON, CLocalize.semicolonExpected());
            marker.done(CElementTypes.NAMESPACE_DECLARATION);
            return;
        }

        if (expect(LBRACE, CLocalize.lbraceExpected())) {
            while (!eof() && !at(RBRACE)) {
                parseExternalDeclaration();
            }
            expect(RBRACE, CLocalize.rbraceExpected());
        }
        marker.done(CElementTypes.NAMESPACE_DECLARATION);
    }

    /**
     * {@code using namespace a;}, {@code using a::b;}, {@code using T = type;}
     */
    private void parseUsing() {
        PsiBuilder.Marker marker = mark();
        advance();

        if (consume(NAMESPACE_KEYWORD)) {
            parseQualifiedName();
        }
        else {
            consume(TYPENAME_KEYWORD);
            parseQualifiedName();
            parseAttributes();
            if (consume(EQ)) {
                parseTypeName();
            }
        }
        expect(SEMICOLON, CLocalize.semicolonExpected());
        marker.done(CElementTypes.DECLARATION);
    }

    /**
     * {@code template <parameters> declaration} - the parameters parsed for their extent only.
     */
    private void parseTemplate(Scope scope) {
        advance();
        if (at(LT)) {
            parseTemplateArguments();
        }
        if (at(TEMPLATE_KEYWORD)) {
            parseTemplate(scope);
            return;
        }
        if (!parseDeclaration(scope == Scope.BLOCK ? Scope.FILE : scope)) {
            error(CLocalize.declarationExpected());
        }
    }

    /**
     * {@code extern "C" { ... }}, {@code extern "C" declaration}
     */
    private void parseLinkageSpecification(Scope scope) {
        advance();
        advance();

        if (consume(LBRACE)) {
            while (!eof() && !at(RBRACE)) {
                parseExternalDeclaration();
            }
            expect(RBRACE, CLocalize.rbraceExpected());
        }
        else if (!parseDeclaration(scope)) {
            error(CLocalize.declarationExpected());
        }
    }
}
