package Chreator.UIModule.AbstractModel;

import java.util.ArrayList;

import Chreator.UIModule.UIUtility;

public class SourceCodeCharacteristicScanner<A, B> {
    public enum CodeBlockType {CLASS, INTERFACE, ENUM, METHOD, FIELD}

    public enum DeclarationStage {
        MODIFIERS, CLASS_TYPE, TYPE, NAME, CLASS_TYPE_PARAMETERS,
        SUPER_CLASS, CLASS_INTERFACES, METHOD_ARGUMENTS_START, METHOD_ARGUMENT_FINISH,
        METHOD_THROWS, BLOCK_BODY, FIELD_ASSIGNMENT
    }

    public static class CodeBlock {
        public ArrayList<CodeBlock> innerCodeBlocks;
        public CodeBlock parentCodeBlock;
        public CodeBlockType codeBlockType;
        public String type;
        public AbstractString name, superClass; //type is the return type of method or
        // object class type of a field
        public ArrayList<AbstractString> modifiers, interfaces, typeParameters, arguments;
        public int startIndex;

        public CodeBlock() {
            innerCodeBlocks = new ArrayList<CodeBlock>();
            parentCodeBlock = null;
            codeBlockType = null;
            name = null;
            type = null;
            superClass = null;
            modifiers = null;
            interfaces = null;
            typeParameters = null;
            arguments = null;
            startIndex = 0;
        }
    }

    private char[][] chopCodeFiler, modifierFiler, classIndicatorFiler, primitiveTypeFiler, blockCommentStarterFilter,
            lineTerminatingFilter, emptyCommentFilter, blockCommentEndFiler, charFiler, stringFilter, dot = new char[][]{{'.'}};
    private char[] lineCommentStartIndicator = UIUtility.JavaLineCommentIndicator.toCharArray(),
            extendsKeyword = "extends".toCharArray(),
            implementsKeyword = "implements".toCharArray(),
            doubleOpenSquareBracket = "<<".toCharArray(),
            doubleCloseSquareBracket = ">>".toCharArray(),
            tripleOpenSquareBracket = "<<<".toCharArray(),
            tripleCloseSquareBracket = ">>>".toCharArray();
    private int index;
    private String typeBuffer;
    private AbstractString superClassBuffer, identifierBuffer;
    private ArrayList<AbstractString> modifierBuffer, interfacesBuffer, typeParametersBuffer, argumentsBuffer;
    private AbstractString[] abstractStringBuffers = {superClassBuffer, identifierBuffer};
    private ArrayList<ArrayList<AbstractString>> abstractStringBufferLists;

    public SourceCodeCharacteristicScanner() {
        chopCodeFiler = UIUtility.stringArrayToChar2DArray(UIUtility.DefaultJavaCodeFilter);
        primitiveTypeFiler = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(UIUtility.JavaPrimitiveTypeSet));
        modifierFiler = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(UIUtility.JavaModifierSet));
        classIndicatorFiler = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(UIUtility.JavaClassIndicatorSet));
        blockCommentStarterFilter = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(
                new String[]{UIUtility.JavaBlockCommentIndicator, UIUtility.JavaDocStartIndicator}
        ));
        charFiler = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(UIUtility.combineStringArrays(
                UIUtility.JavaLineTerminator,
                new String[]{"'"}
        )));
        stringFilter = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(UIUtility.combineStringArrays(
                UIUtility.JavaLineTerminator,
                new String[]{"\""}
        )));
        blockCommentEndFiler = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(UIUtility.JavaCommentEndIndicators));
        lineTerminatingFilter = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(UIUtility.JavaLineTerminator));
        emptyCommentFilter = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(
                new String[]{UIUtility.JavaEmptyBlockComment, UIUtility.JavaEmptyJavaDoc}
        ));
        abstractStringBufferLists = new ArrayList<ArrayList<AbstractString>>();
        abstractStringBufferLists.add(modifierBuffer);
        abstractStringBufferLists.add(interfacesBuffer);
        abstractStringBufferLists.add(typeParametersBuffer);
        abstractStringBufferLists.add(argumentsBuffer);
    }

    public CodeBlock scanFullCodeForCharacteristic(char[] code) {
        index = 0;
        rebuildBuffers();
        return analyseCodeBlock(code, new CodeBlock());
    }

    private CodeBlock analyseCodeBlock(char[] code, CodeBlock parentBlock) {
       /* char[][] ignoreEndIndicator = null;
        boolean stillInBlock = true, dotSticky = false, squareBracketMode = false;
        int pointyBracketCounter = 0;
        DeclarationStage declarationStage = DeclarationStage.MODIFIERS;
        CodeBlock lastChildBlock = new CodeBlock();
        CodeBlockType currentCodeBlockType = CodeBlockType.FIELD;

        while (index < code.length && stillInBlock) {
            int matchedLength = UIUtility.lengthStringToNextBreakPoint(code, index, chopCodeFiler);

            if (ignoreEndIndicator == null) {
                boolean breakDeclaration = true;
                if (UIUtility.lengthStringMatchedKeywords(code, index, emptyCommentFilter) > 0)
                    ignoreEndIndicator = null;
                else if (UIUtility.lengthStringMatchedKeywords(code, index, blockCommentStarterFilter) > 0)
                    ignoreEndIndicator = blockCommentEndFiler;
                else if (UIUtility.isCharArrayStartWith(code, index, lineCommentStartIndicator))
                    ignoreEndIndicator = lineTerminatingFilter;
                else if (code[index] == '\'')
                    ignoreEndIndicator = charFiler;
                else if (code[index] == '"')
                    ignoreEndIndicator = stringFilter;
                else {
                    breakDeclaration = false;

                    if (code[index] == '}') {
                        stillInBlock = false;

                    } else if (code[index] == '{') {
                        lastChildBlock = breakDeclaration(lastChildBlock, parentBlock, currentCodeBlockType);
                        currentCodeBlockType = CodeBlockType.FIELD;
                        index += matchedLength;
                        matchedLength = 0;
                        analyseCodeBlock(code, lastChildBlock);

                    } else if (code[index] == '(' && (declarationStage == DeclarationStage.NAME ||
                            declarationStage == DeclarationStage.METHOD_ARGUMENTS_START)) {
                        declarationStage = DeclarationStage.METHOD_ARGUMENTS_START;

                    } else if (code[index] == ')' && declarationStage == DeclarationStage.METHOD_ARGUMENTS_START) {
                        declarationStage = DeclarationStage.METHOD_ARGUMENT_FINISH;

                    } else if (code[index] == '[' && !squareBracketMode && (declarationStage == DeclarationStage.TYPE ||
                            declarationStage == DeclarationStage.NAME || declarationStage == DeclarationStage.METHOD_ARGUMENTS_START)) {
                        squareBracketMode = true;
                        if (declarationStage == DeclarationStage.TYPE || declarationStage == DeclarationStage.NAME)
                            if (typeBuffer == null)
                                typeBuffer = "[]";
                            else typeBuffer += "[]";

                    } else if (code[index] == ']' && squareBracketMode) {
                        squareBracketMode = false;

                    } else if ((code[index] == '<' || UIUtility.isCharArrayStartWith(code, index, doubleOpenSquareBracket) ||
                            UIUtility.isCharArrayStartWith(code, index, tripleOpenSquareBracket))
                            &&
                            (declarationStage == DeclarationStage.TYPE ||
                                    declarationStage == DeclarationStage.NAME || declarationStage == DeclarationStage.CLASS_TYPE_PARAMETERS ||
                                    declarationStage == DeclarationStage.SUPER_CLASS || declarationStage == DeclarationStage.CLASS_INTERFACES ||
                                    declarationStage == DeclarationStage.METHOD_ARGUMENTS_START)) {
                        if (declarationStage == DeclarationStage.NAME) {
                            declarationStage = DeclarationStage.CLASS_TYPE_PARAMETERS;
                            if (typeParametersBuffer == null)
                                typeParametersBuffer = new ArrayList<>();
                            if (typeParametersBuffer.size() > 0) typeParametersBuffer.clear();
                            if (matchedLength > 1)
                                typeParametersBuffer.add(new AbstractString(code, index + 1, matchedLength - 1));

                        } else {
                            switch (declarationStage) {
                                case TYPE:
                                    if (typeBuffer == null)
                                        typeBuffer = new AbstractString(code, index, matchedLength).toString();
                                    else
                                        typeBuffer += new AbstractString(code, index, matchedLength).toString();
                                    break;
                                case CLASS_TYPE_PARAMETERS:
                                    if (matchedLength > 1)
                                        if (typeParametersBuffer == null)
                                            typeParametersBuffer = new ArrayList<AbstractString>();

                                    if (typeParametersBuffer.size() == 0)
                                        typeParametersBuffer.add(new AbstractString(code, index + 1, matchedLength - 1));
                                    else {
                                        AbstractString s = typeParametersBuffer.get(typeParametersBuffer.size() - 1);
                                        s.set(code, s.index(), matchedLength + index - s.index());
                                    }
                                    break;
                                case SUPER_CLASS:
                                    if (superClassBuffer == null)
                                        superClassBuffer = new AbstractString();
                                    if (superClassBuffer.isNull())
                                        superClassBuffer.set(code, index, matchedLength);
                                    else
                                        superClassBuffer.set(code, superClassBuffer.index(), matchedLength + index - superClassBuffer.index());
                                    break;
                                case CLASS_INTERFACES:
                                    if (interfacesBuffer == null)
                                        interfacesBuffer = new ArrayList<AbstractString>();
                                    if (interfacesBuffer.size() == 0)
                                        interfacesBuffer.add(new AbstractString(code, index, matchedLength));
                                    else {
                                        AbstractString s = interfacesBuffer.get(interfacesBuffer.size() - 1);
                                        s.set(code, s.index(), matchedLength + index - s.index());
                                    }
                                    break;
                                case METHOD_ARGUMENTS_START:
                                    if (argumentsBuffer == null)
                                        argumentsBuffer = new ArrayList<AbstractString>();
                                    if (argumentsBuffer.size() == 0)
                                        argumentsBuffer.add(new AbstractString(code, index, matchedLength));
                                    else {
                                        AbstractString s = argumentsBuffer.get(argumentsBuffer.size() - 1);
                                        s.set(code, s.index(), matchedLength + index - s.index());
                                    }
                                    break;
                            }
                        }

                        if (code[index] == '<') pointyBracketCounter++;
                        else if (UIUtility.isCharArrayStartWith(code, index, doubleOpenSquareBracket))
                            pointyBracketCounter += 2;
                        else if (UIUtility.isCharArrayStartWith(code, index, tripleOpenSquareBracket))
                            pointyBracketCounter += 3;

                    } else if (code[index] == '>' || UIUtility.isCharArrayStartWith(code, index, doubleCloseSquareBracket) ||
                            UIUtility.isCharArrayStartWith(code, index, tripleCloseSquareBracket)
                                    &&
                                    (declarationStage == DeclarationStage.TYPE ||
                                            declarationStage == DeclarationStage.NAME || declarationStage == DeclarationStage.CLASS_TYPE_PARAMETERS ||
                                            declarationStage == DeclarationStage.SUPER_CLASS || declarationStage == DeclarationStage.CLASS_INTERFACES ||
                                            declarationStage == DeclarationStage.METHOD_ARGUMENTS_START)) {

                        if (code[index] == '>') pointyBracketCounter--;
                        else if (UIUtility.isCharArrayStartWith(code, index, doubleCloseSquareBracket))
                            pointyBracketCounter -= 2;
                        else if (UIUtility.isCharArrayStartWith(code, index, tripleCloseSquareBracket))
                            pointyBracketCounter -= 3;
                        pointyBracketCounter = pointyBracketCounter < 0 ? 0 : pointyBracketCounter;
                        if (pointyBracketCounter == 0) {
                            switch (declarationStage){

                            }
                        }

                    } else if (code[index] == ',') {

                    } else if (code[index] == '=' && matchedLength == 1 && declarationStage == DeclarationStage.NAME) {

                    } else if (UIUtility.lengthStringMatchedKeywords(code, index, UIUtility.DefaultJavaCodeFilter) > 0) {
                        breakDeclaration = true;
                    } else {


                        System.out.print('[' + new AbstractString(code, index, matchedLength).toString() + ']');
                    }
                }

                if (breakDeclaration) {
                    lastChildBlock = breakDeclaration(lastChildBlock, parentBlock, currentCodeBlockType);
                    currentCodeBlockType = CodeBlockType.FIELD;
                }

            } else {
                // ignore all analysis until string, char, comment, java doc finish
                if (code[index] == '\'' || code[index] == '"') {
                    if (index == 0 || (index > 0 && code[index - 1] != '\\'))
                        ignoreEndIndicator = null;
                } else if (UIUtility.lengthStringMatchedKeywords(code, index, ignoreEndIndicator) > 0)
                    ignoreEndIndicator = null;
            }
            index += matchedLength;
        }
*/
        return parentBlock;
    }

    private CodeBlock breakDeclaration(CodeBlock lastChildBlock, CodeBlock parentBlock, CodeBlockType codeBlockType) {
        if (!identifierBuffer.isNull()) {
            lastChildBlock = makeCodeBlock(parentBlock, codeBlockType);
            parentBlock.innerCodeBlocks.add(lastChildBlock);
        }
        rebuildBuffers();
        return lastChildBlock;
    }

    private CodeBlock makeCodeBlock(CodeBlock parentBlock, CodeBlockType codeBlockType) {
        CodeBlock codeBlock = new CodeBlock();
        codeBlock.parentCodeBlock = parentBlock;
        codeBlock.name = identifierBuffer;
        codeBlock.codeBlockType = codeBlockType;
        codeBlock.type = typeBuffer;
        codeBlock.superClass = superClassBuffer;
        codeBlock.modifiers = modifierBuffer;
        codeBlock.interfaces = interfacesBuffer;
        codeBlock.typeParameters = typeParametersBuffer;
        codeBlock.arguments = argumentsBuffer;

        int bufferIndex = Integer.MAX_VALUE;
        for (AbstractString s : abstractStringBuffers)
            if (s != null && !s.isNull() && s.index() < bufferIndex) bufferIndex = s.index();
        for (ArrayList<AbstractString> l : abstractStringBufferLists)
            if (l != null)
                for (AbstractString s : l)
                    if (s != null && !s.isNull() && s.index() < bufferIndex)
                        bufferIndex = s.index();

        codeBlock.startIndex = bufferIndex;
        return codeBlock;
    }

    private void rebuildBuffers() {
        typeBuffer = null;
        superClassBuffer = new AbstractString();
        identifierBuffer = new AbstractString();
        modifierBuffer = new ArrayList<AbstractString>();
        interfacesBuffer = new ArrayList<AbstractString>();
        typeParametersBuffer = new ArrayList<AbstractString>();
        argumentsBuffer = new ArrayList<AbstractString>();
    }

    private void clearBuffers() {
        typeBuffer = null;
        superClassBuffer.clear();
        identifierBuffer.clear();
        if (modifierBuffer.size() != 0) modifierBuffer.clear();
        if (interfacesBuffer.size() != 0) interfacesBuffer.clear();
        if (typeParametersBuffer.size() != 0) typeParametersBuffer.clear();
        if (typeParametersBuffer.size() != 0) argumentsBuffer.clear();
    }
    /** modifiers can be re-ordered, eg. final static private void a;
     * method depends on ()
     * void can be a type of a field
     * mind the use of ArrayList<A<B>>
     *
     *     scan break once the code violate the declaration order.
     *     ignore cases that declarations do not match
     * */
}