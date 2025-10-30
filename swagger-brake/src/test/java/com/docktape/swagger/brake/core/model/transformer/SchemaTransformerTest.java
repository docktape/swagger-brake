package com.docktape.swagger.brake.core.model.transformer;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.docktape.swagger.brake.core.model.Schema;
import com.docktape.swagger.brake.core.model.SchemaAttribute;
import com.docktape.swagger.brake.core.model.SchemaBuilder;
import com.docktape.swagger.brake.core.model.service.TypeRefNameResolver;
import com.docktape.swagger.brake.core.model.store.SchemaStore;
import com.docktape.swagger.brake.core.model.store.StoreProvider;
import io.swagger.v3.oas.models.media.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SchemaTransformerTest {
    private SchemaTransformer underTest;

    @BeforeEach
    public void setUp() {
        underTest = new SchemaTransformer(new TypeRefNameResolver());
    }

    @Test
    public void testTransformWorksForAllOfComposedSchema() {
        // given
        ObjectSchema pet = new ObjectSchema();
        pet.setType("object");
        pet.setProperties(of(
            "id", new IntegerSchema(),
            "name", new StringSchema()
        ));
        ObjectSchema dog = new ObjectSchema();
        dog.setType("object");
        dog.setProperties(of(
            "bark", new StringSchema(),
            "breed", new StringSchema()
        ));
        SchemaStore schemaStore = new SchemaStore(ImmutableMap.of("Pet", pet, "Dog", dog));

        io.swagger.v3.oas.models.media.Schema petRef = new io.swagger.v3.oas.models.media.Schema();
        petRef.set$ref("#/components/schemas/Pet");
        io.swagger.v3.oas.models.media.Schema dogRef = new io.swagger.v3.oas.models.media.Schema();
        dogRef.set$ref("#/components/schemas/Dog");
        ComposedSchema composedSchema = new ComposedSchema();
        composedSchema.setAllOf(ImmutableList.of(petRef, dogRef));

        Schema expectedSchema = new SchemaBuilder("object").schemaAttributes(ImmutableList.of(
            new SchemaAttribute("id", new SchemaBuilder("integer").build(), false, false),
            new SchemaAttribute("name", new SchemaBuilder("string").build(), false, false),
            new SchemaAttribute("bark", new SchemaBuilder("string").build(), false, false),
            new SchemaAttribute("breed", new SchemaBuilder("string").build(), false, false)
        )).build();
        // when
        Schema result = withSchemaStore(schemaStore, () -> underTest.transform(composedSchema));
        // then
        assertThat(result).isEqualTo(expectedSchema);
    }

    @Test
    public void testTransformWorksForAllOfComposedSchemaWithSamePropertyNamesAndSameTypes() {
        // given
        ObjectSchema pet = new ObjectSchema();
        pet.setType("object");
        pet.setProperties(of(
            "id", new IntegerSchema(),
            "name", new StringSchema()
        ));
        ObjectSchema dog = new ObjectSchema();
        dog.setType("object");
        dog.setProperties(of(
            "id", new IntegerSchema(),
            "breed", new StringSchema()
        ));
        SchemaStore schemaStore = new SchemaStore(ImmutableMap.of("Pet", pet, "Dog", dog));

        io.swagger.v3.oas.models.media.Schema petRef = new io.swagger.v3.oas.models.media.Schema();
        petRef.set$ref("#/components/schemas/Pet");
        io.swagger.v3.oas.models.media.Schema dogRef = new io.swagger.v3.oas.models.media.Schema();
        dogRef.set$ref("#/components/schemas/Dog");
        ComposedSchema composedSchema = new ComposedSchema();
        composedSchema.setAllOf(ImmutableList.of(petRef, dogRef));

        Schema expectedSchema = new SchemaBuilder("object").schemaAttributes(ImmutableList.of(
            new SchemaAttribute("id", new SchemaBuilder("integer").build(), false, false),
            new SchemaAttribute("name", new SchemaBuilder("string").build(), false, false),
            new SchemaAttribute("breed", new SchemaBuilder("string").build(), false, false)
        )).build();
        // when
        Schema result = withSchemaStore(schemaStore, () -> underTest.transform(composedSchema));
        // then
        assertThat(result).isEqualTo(expectedSchema);
    }

    @Test
    public void testTransformWorksForAllOfComposedSchemaWithSamePropertyNamesAndDifferentTypes() {
        // given
        ObjectSchema pet = new ObjectSchema();
        pet.setType("object");
        pet.setProperties(of(
            "id", new IntegerSchema(),
            "name", new StringSchema()
        ));
        ObjectSchema dog = new ObjectSchema();
        dog.setType("object");
        dog.setProperties(of(
            "id", new StringSchema(),
            "breed", new StringSchema()
        ));
        ObjectSchema cat = new ObjectSchema();
        cat.setType("object");
        cat.setProperties(of(
            "id", new StringSchema(),
            "meow", new StringSchema()
        ));
        SchemaStore schemaStore = new SchemaStore(ImmutableMap.of("Pet", pet, "Dog", dog, "Cat", cat));

        io.swagger.v3.oas.models.media.Schema petRef = new io.swagger.v3.oas.models.media.Schema();
        petRef.set$ref("#/components/schemas/Pet");
        io.swagger.v3.oas.models.media.Schema dogRef = new io.swagger.v3.oas.models.media.Schema();
        dogRef.set$ref("#/components/schemas/Dog");
        io.swagger.v3.oas.models.media.Schema catRef = new io.swagger.v3.oas.models.media.Schema();
        catRef.set$ref("#/components/schemas/Cat");
        ComposedSchema composedSchema = new ComposedSchema();
        composedSchema.setAllOf(ImmutableList.of(petRef, dogRef, catRef));

        Schema expectedSchema = new SchemaBuilder("object").schemaAttributes(ImmutableList.of(
            new SchemaAttribute("id", new SchemaBuilder("integer").build(), false, false),
            new SchemaAttribute("name", new SchemaBuilder("string").build(), false, false),
            new SchemaAttribute("breed", new SchemaBuilder("string").build(), false, false),
            new SchemaAttribute("meow", new SchemaBuilder("string").build(), false, false)
        )).build();
        // when
        Schema result = withSchemaStore(schemaStore, () -> underTest.transform(composedSchema));
        // then
        assertThat(result).isEqualTo(expectedSchema);
    }

    @Test
    public void testTransformWorksForAllOfComposedSchemaWithSamePropertyNamesAnd3DifferentTypes() {
        // given
        ObjectSchema pet = new ObjectSchema();
        pet.setType("object");
        pet.setProperties(of(
            "id", new IntegerSchema(),
            "name", new StringSchema()
        ));
        ObjectSchema dog = new ObjectSchema();
        dog.setType("object");
        dog.setProperties(of(
            "id", new StringSchema(),
            "breed", new StringSchema()
        ));
        ObjectSchema cat = new ObjectSchema();
        cat.setType("object");
        cat.setProperties(of(
            "id", new BooleanSchema(),
            "meow", new StringSchema()
        ));
        SchemaStore schemaStore = new SchemaStore(ImmutableMap.of("Pet", pet, "Dog", dog, "Cat", cat));

        io.swagger.v3.oas.models.media.Schema petRef = new io.swagger.v3.oas.models.media.Schema();
        petRef.set$ref("#/components/schemas/Pet");
        io.swagger.v3.oas.models.media.Schema dogRef = new io.swagger.v3.oas.models.media.Schema();
        dogRef.set$ref("#/components/schemas/Dog");
        io.swagger.v3.oas.models.media.Schema catRef = new io.swagger.v3.oas.models.media.Schema();
        catRef.set$ref("#/components/schemas/Cat");
        ComposedSchema composedSchema = new ComposedSchema();
        composedSchema.setAllOf(ImmutableList.of(petRef, dogRef, catRef));

        Schema expectedSchema = new SchemaBuilder("object").schemaAttributes(ImmutableList.of(
            new SchemaAttribute("id", new SchemaBuilder("integer").build(), false, false),
            new SchemaAttribute("name", new SchemaBuilder("string").build(), false, false),
            new SchemaAttribute("breed", new SchemaBuilder("string").build(), false, false),
            new SchemaAttribute("meow", new SchemaBuilder("string").build(), false, false)
        )).build();
        // when
        Schema result = withSchemaStore(schemaStore, () -> underTest.transform(composedSchema));
        // then
        assertThat(result).isEqualTo(expectedSchema);
    }

    private <T> T withSchemaStore(SchemaStore schemaStore, Supplier<T> f) {
        try {
            StoreProvider.setSchemaStore(schemaStore);
            return f.get();
        } finally {
            StoreProvider.clear();
        }
    }
}