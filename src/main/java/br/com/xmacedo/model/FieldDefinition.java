package br.com.xmacedo.model;

import lombok.Data;

@Data
public class FieldDefinition {
    private String name;
    private String type;
    private String accessSpecifiers;
}
