package proto.api;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

import java.io.IOException;
import java.math.BigInteger;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author liu.teng
 * @Date 2020/9/25 10:40
 * @Version 1.0
 */
public class PbJsonFormat {

    public static String printToString(Message message) {
        try {
            StringBuilder text = new StringBuilder();
            print((Message)message, (Appendable)text);
            return text.toString();
        } catch (IOException var2) {
            throw new RuntimeException("Writing to a StringBuilder threw an IOException (should never happen).", var2);
        }
    }

    public static void print(Message message, Appendable output) throws IOException {
        PbJsonFormat.JsonGenerator generator = new PbJsonFormat.JsonGenerator(output);
        generator.print("{");
        print(message, generator);
        generator.print("}");
    }

    protected static void print(Message message, PbJsonFormat.JsonGenerator generator) throws IOException {
        Iterator iter = message.getAllFields().entrySet().iterator();

        while(iter.hasNext()) {
            Map.Entry<Descriptors.FieldDescriptor, Object> field = (Map.Entry)iter.next();
            printField((Descriptors.FieldDescriptor)field.getKey(), field.getValue(), generator);
            if (iter.hasNext()) {
                generator.print(",");
            }
        }

        if (message.getUnknownFields().asMap().size() > 0) {
            generator.print(", ");
        }

    }


    static String escapeBytes(ByteString input) {
        StringBuilder builder = new StringBuilder(input.size());

        for(int i = 0; i < input.size(); ++i) {
            byte b = input.byteAt(i);
            switch(b) {
                case 7:
                    builder.append("\\a");
                    break;
                case 8:
                    builder.append("\\b");
                    break;
                case 9:
                    builder.append("\\t");
                    break;
                case 10:
                    builder.append("\\n");
                    break;
                case 11:
                    builder.append("\\v");
                    break;
                case 12:
                    builder.append("\\f");
                    break;
                case 13:
                    builder.append("\\r");
                    break;
                case 34:
                    builder.append("\\\"");
                    break;
                case 39:
                    builder.append("\\'");
                    break;
                case 92:
                    builder.append("\\\\");
                    break;
                default:
                    if (b >= 32) {
                        builder.append((char)b);
                    } else {
                        String unicodeString = unicodeEscaped((char)b);
                        builder.append(unicodeString);
                    }
            }
        }

        return builder.toString();
    }

    static String unicodeEscaped(char ch) {
        if (ch < 16) {
            return "\\u000" + Integer.toHexString(ch);
        } else if (ch < 256) {
            return "\\u00" + Integer.toHexString(ch);
        } else {
            return ch < 4096 ? "\\u0" + Integer.toHexString(ch) : "\\u" + Integer.toHexString(ch);
        }
    }

    public static void printField(Descriptors.FieldDescriptor field, Object value, PbJsonFormat.JsonGenerator generator) throws IOException {
        printSingleField(field, value, generator);
    }

    private static void printSingleField(Descriptors.FieldDescriptor field, Object value, PbJsonFormat.JsonGenerator generator) throws IOException {
        if (field.isExtension()) {
            generator.print("\"");
            if (field.getContainingType().getOptions().getMessageSetWireFormat() && field.getType() == Descriptors.FieldDescriptor.Type.MESSAGE && field.isOptional() && field.getExtensionScope() == field.getMessageType()) {
                generator.print(field.getMessageType().getFullName());
            } else {
                generator.print(field.getFullName());
            }

            generator.print("\"");
        } else {
            generator.print("\"");
            if (field.getType() == Descriptors.FieldDescriptor.Type.GROUP) {
                generator.print(field.getMessageType().getName());
            } else {
                generator.print(field.getName());
            }

            generator.print("\"");
        }

        if (field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
            generator.print(": ");
            generator.indent();
        } else {
            generator.print(": ");
        }

        if (field.isRepeated()) {
            generator.print("[");
            Iterator iter = ((List)value).iterator();

            while(iter.hasNext()) {
                printFieldValue(field, iter.next(), generator);
                if (iter.hasNext()) {
                    generator.print(",");
                }
            }

            generator.print("]");
        } else {
            printFieldValue(field, value, generator);
            if (field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                generator.outdent();
            }
        }

    }

    private static void printFieldValue(Descriptors.FieldDescriptor field, Object value, PbJsonFormat.JsonGenerator generator) throws IOException {
        switch(field.getType()) {
            case INT32:
            case INT64:
            case SINT32:
            case SINT64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
                generator.print(value.toString());
                break;
            case UINT32:
            case FIXED32:
                generator.print(unsignedToString((Integer)value));
                break;
            case UINT64:
            case FIXED64:
                generator.print(unsignedToString((Long)value));
                break;
            case STRING:
                generator.print("\"");
                generator.print(escapeText((String)value));
                generator.print("\"");
                break;
            case BYTES:
                generator.print("\"");
                generator.print(escapeBytes((ByteString)value));
                generator.print("\"");
                break;
            case ENUM:
                generator.print("\"");
                generator.print(((Descriptors.EnumValueDescriptor)value).getName());
                generator.print("\"");
                break;
            case MESSAGE:
            case GROUP:
                generator.print("{");
                print((Message)value, generator);
                generator.print("}");
        }

    }

    static String escapeText(String input) {
        StringBuilder builder = new StringBuilder(input.length());
        CharacterIterator iter = new StringCharacterIterator(input);

        for(char c = iter.first(); c != '\uffff'; c = iter.next()) {
            switch(c) {
                case '\b':
                    builder.append("\\b");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                default:
                    if (c >= 0 && c <= 31) {
                        appendEscapedUnicode(builder, c);
                    } else if (Character.isHighSurrogate(c)) {
                        appendEscapedUnicode(builder, c);
                        c = iter.next();
                        if (c == '\uffff') {
                            throw new IllegalArgumentException("invalid unicode string: unexpected high surrogate pair value without corresponding low value.");
                        }

                        appendEscapedUnicode(builder, c);
                    } else {
                        builder.append(c);
                    }
            }
        }

        return builder.toString();
    }

    static void appendEscapedUnicode(StringBuilder builder, char ch) {
        String prefix = "\\u";
        if (ch < 16) {
            prefix = "\\u000";
        } else if (ch < 256) {
            prefix = "\\u00";
        } else if (ch < 4096) {
            prefix = "\\u0";
        }

        builder.append(prefix).append(Integer.toHexString(ch));
    }


    private static String unsignedToString(int value) {
        return value >= 0 ? Integer.toString(value) : Long.toString((long)value & 4294967295L);
    }

    private static String unsignedToString(long value) {
        return value >= 0L ? Long.toString(value) : BigInteger.valueOf(value & 9223372036854775807L).setBit(63).toString();
    }

    protected static class JsonGenerator {
        Appendable output;
        boolean atStartOfLine = true;
        StringBuilder indent = new StringBuilder();

        public JsonGenerator(Appendable output) {
            this.output = output;
        }

        public void indent() {
            this.indent.append("  ");
        }

        public void outdent() {
            int length = this.indent.length();
            if (length == 0) {
                throw new IllegalArgumentException(" Outdent() without matching Indent().");
            } else {
                this.indent.delete(length - 2, length);
            }
        }

        public void print(CharSequence text) throws IOException {
            int size = text.length();
            int pos = 0;

            for(int i = 0; i < size; ++i) {
                if (text.charAt(i) == '\n') {
                    this.write(text.subSequence(pos, size), i - pos + 1);
                    pos = i + 1;
                    this.atStartOfLine = true;
                }
            }

            this.write(text.subSequence(pos, size), size - pos);
        }

        private void write(CharSequence data, int size) throws IOException {
            if (size != 0) {
                if (this.atStartOfLine) {
                    this.atStartOfLine = false;
                    this.output.append(this.indent);
                }

                this.output.append(data);
            }
        }
    }
}
