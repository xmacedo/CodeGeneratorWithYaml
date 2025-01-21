package br.com.xmacedo;

import br.com.xmacedo.model.ClassDefinition;
import br.com.xmacedo.model.Definitions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

public class CodeGeneratorWithYaml {
    public static void main(String[] args) {
        System.out.println("Code Generator!");

        try {
            //1. Read file
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            Definitions definitions = mapper.readValue(new File("example.yaml"), Definitions.class);

            for(ClassDefinition classDefinition : definitions.getClasses()) {
                generateClasses(classDefinition);
            }

            //2. Interpreter the file

            //3. What is the file structure?

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateClasses(ClassDefinition classDefinition) {
        //Generate as Is? Or whe can add Getter and setters?
    }
}