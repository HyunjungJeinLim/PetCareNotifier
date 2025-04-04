package com.example.petcarenotifier;

import java.util.ArrayList;
import java.util.List;

public class PetData {
    public static List<Pet> pets = new ArrayList<>();
    public static int currentPetIndex = 0;

    static {
        pets.add(new Pet("Cookie", "5", "Golden Retriever", "10/01/2020", R.drawable.cookie_retriever));
        pets.add(new Pet("Bob", "3", "Husky", "05/15/2019", R.drawable.bob_husky));
    }

    public static class Pet {
        public String name;
        public String age;
        public String breed;
        public String birthday;
        public int imageResId;

        public Pet(String name, String age, String breed, String birthday, int imageResId) {
            this.name = name;
            this.age = age;
            this.breed = breed;
            this.birthday = birthday;
            this.imageResId = imageResId;
        }
    }
}