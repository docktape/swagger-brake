package com.docktape.swagger.brake.core.model;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Schema {
    public static final String LEVEL_DELIMITER_REPLACE_VALUE = "$";
    public static final String LEVEL_DELIMITER = ".";
    private final String type;
    private final Set<String> enumValues;
    private final Set<SchemaAttribute> schemaAttributes;
    private final Schema schema;

    public Optional<Schema> getSchema() {
        return Optional.ofNullable(schema);
    }

    /**
     * Returns all the enum attributes recursively within the schema.
     * @return the enum attribute names.
     */
    public Collection<String> getEnums() {
        Collection<SchemaAttribute> schemaAttrs = schemaAttributes;
        if (CollectionUtils.isEmpty(schemaAttrs)) {
            schemaAttrs = Optional.ofNullable(schema).map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
        }
        Collection<String> result = internalGetEnums(schemaAttrs, "");
        result.addAll(Optional.ofNullable(schema).map(Schema::getEnumValues).orElse(enumValues));
        return result;
    }

    private Collection<String> internalGetEnums(Collection<SchemaAttribute> schemaAttributes, String levelName) {
        Collection<String> result = schemaAttributes
            .stream()
            .filter(a -> a.getSchema() != null)
            .map(a -> a.getSchema().getEnumValues().stream().map(e -> generateLeveledName(e, a.getName())).collect(toList()))
            .flatMap(Collection::stream)
            .collect(toList());

        for (SchemaAttribute schemaAttribute : schemaAttributes) {
            Schema childSchema = schemaAttribute.getSchema();
            if (childSchema != null) {
                Collection<SchemaAttribute> childSchemaAttributes = childSchema.getSchemaAttributes();
                if (isEmpty(childSchemaAttributes)) {
                    childSchemaAttributes = childSchema.getSchema().map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
                }
                result.addAll(internalGetEnums(childSchemaAttributes, generateLeveledName(schemaAttribute.getName(), levelName)));
            }
        }
        return result;
    }

    /**
     * Returns all the types recursively in this schema.
     * @return the types.
     */
    public Map<String, String> getTypes() {
        Collection<SchemaAttribute> schemaAttrs = schemaAttributes;
        if (CollectionUtils.isEmpty(schemaAttrs)) {
            schemaAttrs = Optional.ofNullable(schema).map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
        }
        Map<String, String> types = internalGetTypes(schemaAttrs, "");
        types.put("", type);
        return types;
    }

    private Map<String, String> internalGetTypes(Collection<SchemaAttribute> schemaAttributes, String levelName) {
        Map<String, String> result = schemaAttributes
            .stream()
            .filter(a -> a.getSchema() != null)
            .collect(toMap(a -> generateLeveledName(a.getName(), levelName), a -> a.getSchema().getType()));
        for (SchemaAttribute schemaAttribute : schemaAttributes) {
            Schema childSchema = schemaAttribute.getSchema();
            if (childSchema != null) {
                Collection<SchemaAttribute> childSchemaAttributes = childSchema.getSchemaAttributes();
                if (isEmpty(childSchemaAttributes)) {
                    childSchemaAttributes = childSchema.getSchema().map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
                }
                result.putAll(internalGetTypes(childSchemaAttributes, levelName));
            }
        }
        return result;
    }

    /**
     * Returns all attribute names that are required.
     * @return all attribute names that are required.
     */
    public Set<String> getRequiredAttributeNames() {
        Collection<SchemaAttribute> schemaAttrs = schemaAttributes;
        if (CollectionUtils.isEmpty(schemaAttrs)) {
            schemaAttrs = Optional.ofNullable(schema).map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
        }
        Map<String, Boolean> attributeRequiredMap = internalGetAttributeData(schemaAttrs, "", SchemaAttribute::isRequired)
                .stream()
                .collect(toMap(Pair::getLeft, Pair::getRight));
        Set<String> result = new HashSet<>();
        for (Map.Entry<String, Boolean> entry : attributeRequiredMap.entrySet()) {
            String attributeName = entry.getKey();
            String originalAttributeName = attributeName.replace(LEVEL_DELIMITER_REPLACE_VALUE, LEVEL_DELIMITER);
            Boolean attributeIsRequired = entry.getValue();
            if (attributeName.contains(LEVEL_DELIMITER)) {
                boolean hierarchicallyNotRequired = false;
                List<String> pieces = Arrays.asList(attributeName.split("\\" + LEVEL_DELIMITER));
                for (int i = 0; i < pieces.size(); i++) {
                    StringJoiner stringJoiner = new StringJoiner(LEVEL_DELIMITER);
                    pieces.subList(0, i + 1).forEach(stringJoiner::add);
                    String attrToSearchFor = stringJoiner.toString();
                    boolean isRequired = BooleanUtils.toBoolean(attributeRequiredMap.get(attrToSearchFor));
                    if (!isRequired) {
                        hierarchicallyNotRequired = true;
                        break;
                    }
                }
                if (!hierarchicallyNotRequired) {
                    if (attributeIsRequired) {
                        result.add(originalAttributeName);
                    }
                }
            } else {
                if (attributeIsRequired) {
                    result.add(originalAttributeName);
                }
            }
        }
        return result;
    }

    /**
     * Returns all the attribute names recursively.
     * @return the attribute names.
     */
    public Collection<String> getAttributeNames() {
        Collection<SchemaAttribute> schemaAttrs = schemaAttributes;
        if (CollectionUtils.isEmpty(schemaAttrs)) {
            schemaAttrs = Optional.ofNullable(schema).map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
        }
        return internalGetAttributeData(schemaAttrs, "", identity())
            .stream()
            .map(Pair::getLeft)
            .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Returns all the attribute names that are not deprecated recursively.
     * @return the attribute names.
     */
    public Collection<String> getNonDeprecatedAttributeNames() {
        Collection<SchemaAttribute> schemaAttrs = schemaAttributes;
        if (CollectionUtils.isEmpty(schemaAttrs)) {
            schemaAttrs = Optional.ofNullable(schema).map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
        }
        return internalGetAttributeData(schemaAttrs, "", identity())
                .stream()
                .filter(p -> !p.getRight().isDeprecated())
                .map(Pair::getLeft)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private <T> Collection<Pair<String, T>> internalGetAttributeData(Collection<SchemaAttribute> schemaAttributes, String levelName, Function<SchemaAttribute, T> mappingFunc) {
        List<Pair<String, T>> result = schemaAttributes.stream().map(sA -> Pair.of(generateLeveledName(sA.getName(), levelName), mappingFunc.apply(sA))).collect(toList());
        for (SchemaAttribute schemaAttribute : schemaAttributes) {
            Schema childSchema = schemaAttribute.getSchema();
            if (childSchema != null) {
                Collection<SchemaAttribute> childSchemaAttributes = childSchema.getSchemaAttributes();
                if (isEmpty(childSchemaAttributes)) {
                    childSchemaAttributes = childSchema.getSchema().map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
                }
                result.addAll(internalGetAttributeData(childSchemaAttributes, generateLeveledName(schemaAttribute.getName(), levelName), mappingFunc));
            }
        }
        return result;
    }

    /**
     * Returns the schemas with attribute names recursively.
     * @return the schemas recursively
     */
    public Map<String, Schema> getSchemasRecursively() {
        Collection<SchemaAttribute> schemaAttrs = schemaAttributes;
        if (CollectionUtils.isEmpty(schemaAttrs)) {
            schemaAttrs = Optional.ofNullable(schema).map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
        }
        Map<String, Schema> schemas = internalGetSchemasRecursively(schemaAttrs, "");
        schemas.put("", this);
        return schemas;
    }

    private Map<String, Schema> internalGetSchemasRecursively(Collection<SchemaAttribute> schemaAttributes, String levelName) {
        Map<String, Schema> result = schemaAttributes
            .stream()
            .filter(a -> a.getSchema() != null)
            .collect(toMap(a -> generateLeveledName(a.getName(), levelName), SchemaAttribute::getSchema));
        for (SchemaAttribute schemaAttribute : schemaAttributes) {
            Schema childSchema = schemaAttribute.getSchema();
            if (childSchema != null) {
                Collection<SchemaAttribute> childSchemaAttributes = childSchema.getSchemaAttributes();
                if (isEmpty(childSchemaAttributes)) {
                    childSchemaAttributes = childSchema.getSchema().map(Schema::getSchemaAttributes).orElse(Collections.emptySet());
                }
                result.putAll(internalGetSchemasRecursively(childSchemaAttributes, levelName));
            }
        }
        return result;
    }

    private String generateLeveledName(String name, String levelName) {
        if (!StringUtils.isBlank(levelName)) {
            return format("%s%s%s", levelName, LEVEL_DELIMITER, name);
        }
        return name.replace(LEVEL_DELIMITER, LEVEL_DELIMITER_REPLACE_VALUE);
    }

}
