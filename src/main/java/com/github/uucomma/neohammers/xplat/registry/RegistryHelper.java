package com.github.uucomma.neohammers.xplat.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Supplier;

public abstract class RegistryHelper<T> implements Iterable<RegistrySupplier<T>> {
    protected final ArrayList<RegistrySupplier<T>> entries = new ArrayList<>();

    private final String modId;
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final Registry<T> registry;

    private RegistryHelper(String modId, Registry<T> registry, ResourceKey<? extends Registry<T>> registryKey) {
        this.modId = modId;
        this.registryKey = registryKey;
        this.registry = registry;
    }

    protected RegistryHelper(String modId, Registry<T> registry) {
        this(modId, registry, registry.key());
    }

    @SuppressWarnings("unchecked")
    protected RegistryHelper(String modId, ResourceKey<? extends Registry<T>> registryKey) {
        this(modId, (Registry<T>) BuiltInRegistries.REGISTRY.getValue(registryKey.identifier()), registryKey);
    }

    public RegistrySupplier<T> register(String id, Supplier<T> value) {
        return this.register(this.identifier(id), value);
    }

    public RegistrySupplier<T> register(Identifier id, Supplier<T> value) {
        ResourceKey<T> key = ResourceKey.create(this.registryKey, id);
        Supplier<Holder<T>> holderSupplier = () -> this.registry.get(id).orElseThrow();
        RegistrySupplier<T> supplier = new RegistrySupplierImpl<>(value, holderSupplier, id, key);
        this.entries.add(supplier);
        return supplier;
    }

    public Identifier identifier(String name) {
        return Identifier.fromNamespaceAndPath(this.modId, name);
    }

    public ResourceKey<T> key(String name) {
        return ResourceKey.create(this.registryKey, this.identifier(name));
    }

    public abstract void register();

    public Registry<T> getRegistry() {
        return registry;
    }

    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }

    public String getModId() {
        return modId;
    }

    @NonNull
    @Override
    public Iterator<RegistrySupplier<T>> iterator() {
        return this.entries.iterator();
    }

    private static class RegistrySupplierImpl<T> implements RegistrySupplier<T> {
        private final Supplier<T> supplier;
        private final Supplier<Holder<T>> holderSupplier;
        private final Identifier id;
        private final ResourceKey<T> key;

        private Holder<T> holder;
        private T value;

        public RegistrySupplierImpl(Supplier<T> supplier, Supplier<Holder<T>> holderSupplier, Identifier id, ResourceKey<T> key) {
            this.supplier = supplier;
            this.holderSupplier = holderSupplier;
            this.id = id;
            this.key = key;
        }

        @Override
        public Holder<T> getHolder() {
            if (this.holder == null) {
                this.holder = this.holderSupplier.get();
            }

            return this.holder;
        }

        @Override
        public Identifier getId() {
            return this.id;
        }

        @Override
        public ResourceKey<T> getKey() {
            return this.key;
        }

        @Override
        public T get() {
            if (this.value == null) {
                this.value = this.supplier.get();
            }

            return this.value;
        }
    }
}
