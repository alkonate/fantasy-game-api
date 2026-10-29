package org.example.fantasygameapi.module.hero.infrastructure;

import com.example.fantasygame.api.module.hero.infrastructure.InMemoryHeroRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestInMemoryRepository extends InMemoryHeroRepository {


    public void clear(){
        heroes.clear();
    }

    public void assertIsEmpty(){
        assertTrue(heroes.isEmpty());
    }

    public TestInMemoryRepository assertCount(int expectedCount) {
        assertEquals(expectedCount, heroes.size(),
                "Expected " + expectedCount + " hero(es) in repository, but found " + heroes.size());
        return this;
    }

    public TestInMemoryRepository assertHeroIdentity(UUID id, String name, String species, String clazz) {
        var hero = heroes.get(id);
        assertEquals(hero.getName(),name,"Hero name not equal to expected");
        assertEquals(hero.getSpecies(),species,"Hero species not equal to expected");
        assertEquals(hero.getHeroClass(),clazz,"Hero clazz not equal to expected");
        return this;
    }
}
