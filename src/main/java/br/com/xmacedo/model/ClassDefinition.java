package br.com.xmacedo.model;

import lombok.Data;
import java.util.List;

@Data
public class ClassDefinition {
    private List<String> library;
    private String name;
    private List<FieldDefinition> fields;
}
