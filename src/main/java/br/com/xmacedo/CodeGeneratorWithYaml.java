package br.com.xmacedo;

import br.com.xmacedo.model.ClassDefinition;
import br.com.xmacedo.model.Definitions;
import br.com.xmacedo.model.FieldDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class CodeGeneratorWithYaml {

    private static final String FOLDER_TO_GENERATED_CLASS = "class-generated/";
    private static final String PACKAGE_NAME = "br.com.xmacedo";
    private static final String FOLDER_TO_GET_YAML_FILES = "src/main/resources/";

    public static void main(String[] args) {
        System.out.println(">> Code Generator!");

        try {
            //1. Read file
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            Definitions definitions = mapper.readValue(new File(FOLDER_TO_GET_YAML_FILES + "example.yaml"), Definitions.class);

            //2. Interpreter the file
            StringBuilder classToGenerate = new StringBuilder();
            for (ClassDefinition classDefinition : definitions.getClasses()) {
                classToGenerate = generateClasses(classDefinition);

                //3. write file
                try (FileWriter writer = new FileWriter(FOLDER_TO_GENERATED_CLASS + classDefinition.getName() + ".java")) {
                    writer.write(classToGenerate.toString());
                    System.out.println("Generated class: " + classDefinition.getName() + ".java");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("<<< Code Generated!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Generate a java class file based on the class definition
    private static StringBuilder generateClasses(ClassDefinition classDefinition) {
        StringBuilder classCodeToWriter = new StringBuilder();//first attempt

        //package
        classCodeToWriter.append("package " + PACKAGE_NAME + ";").append("\n\n");
        boolean blankLine = false;
        //imports
        for (FieldDefinition field : classDefinition.getFields()) {
            if (!isPrimitive(field.getType()) && !isJavaLang(field.getType())) {
                classCodeToWriter.append("import " + field.getType() + ";").append("\n");
                blankLine = true;
            }

        }
        //Nem blank line to divide imports from class name
        if(blankLine) {
            classCodeToWriter.append("\n");
        }

        //class definition
        classCodeToWriter.append("public class ").append(classDefinition.getName()).append(" {\n\n");

        //fields
        for (FieldDefinition field : classDefinition.getFields()) {
            classCodeToWriter.append("    " + field.getAccessSpecifiers() + " ")//private-public-protected
                    .append(field.getType())//string - boolean - int
                    .append(" ")
                    .append(field.getName())//nome
                    .append(";\n");
        }
        //getter and setters
        for (FieldDefinition field : classDefinition.getFields()) {
            // Getter
            classCodeToWriter.append("\n");
            classCodeToWriter.append("    public ").append(field.getType())
                    .append(" get").append(changeToUpperCaseFirstLetter(field.getName())).append("() {\n")
                    .append("        return ").append(field.getName()).append(";\n")
                    .append("    }\n\n");

            // Setter
            classCodeToWriter.append("    public void set").append(changeToUpperCaseFirstLetter(field.getName())).append("(")
                    .append(field.getType()).append(" ").append(field.getName()).append(") {\n")
                    .append("        this.").append(field.getName()).append(" = ").append(field.getName()).append(";\n")
                    .append("    }\n");
        }
        //End of file
        classCodeToWriter.append("}");

        return classCodeToWriter;
    }

    private static String changeToUpperCaseFirstLetter(String name) {
        if (null != name && name.length() > 1) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return name;
    }

    private static boolean isPrimitive(String type) {
        return Set.of("int", "double", "float", "boolean", "char", "byte", "short", "long").contains(type);
    }

    private static boolean isJavaLang(String type) {
        return Set.of("String", "Integer", "Double", "Float", "Boolean", "Character", "Byte", "Short", "Long").contains(type);
    }


}