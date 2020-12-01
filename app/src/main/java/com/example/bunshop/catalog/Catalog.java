package com.example.bunshop.catalog;

import com.google.firebase.storage.StorageReference;

public class Catalog {
    private String name;
    private StorageReference reference;

    public Catalog(String name, StorageReference reference) {
        this.name = name;
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StorageReference getReference() {
        return reference;
    }

    public void setReference(StorageReference reference) {
        this.reference = reference;
    }
}
