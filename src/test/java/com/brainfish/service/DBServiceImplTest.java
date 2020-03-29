package com.brainfish.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;



@RunWith(JUnit4.class)
public class DBServiceImplTest
{
    private DBService dbService;
    @Before
    public void setup(){
        this.dbService = new DBServiceImpl();
    }

    @Test
    public void set_happyPath()
    {
        this.dbService.set("a", "foo");
        assertEquals(dbService.get("a"), "foo");
        this.dbService.set("b", "bar");
        assertEquals(dbService.get("a"), "foo");
        this.dbService.set("a", "bar");
        assertEquals(dbService.get("a"), "bar");
        assertEquals(dbService.get("b"), "bar");
    }

    @Test
    public void set_withTransaction()
    {
        //set a
        dbService.set("a", "foo");
        assertEquals(dbService.get("a"), "foo");

        //open a new transaction
        dbService.beginTransaction();
        dbService.set("a", "bar");
        assertEquals(dbService.get("a"), "bar");
        dbService.delete("a");
        assertEquals(dbService.get("a"), "NULL");
        dbService.set("a", "bar2");
        dbService.commitTransaction();
        assertEquals(dbService.get("a"), "bar2");
    }
    @Test
    public void testCase1() {
        assertEquals(dbService.get("a"), "NULL");

        //GET a NULL >> SET a foo >> SET b foo >> COUNT foo 2 >> COUNT bar 0 >> DELETE a >> COUNT foo 1 >> SET b baz >> COUNT foo 0 >> GET b baz >> GET B NULL >> END
        dbService.set("a", "foo");
        dbService.set("b", "foo");
        assertEquals(2, (int) dbService.count("foo"));
        assertEquals(0, (int) dbService.count("bar"));

        dbService.delete("a");
        assertEquals(1, (int) dbService.count("foo"));

        dbService.set("b", "baz");
        assertEquals(0, (int) dbService.count("foo"));
        assertEquals(dbService.get("b"), "baz");

        assertEquals(dbService.get("B"), "NULL");
    }

    @Test
    public void testCase2() {
        //
        dbService.set("a", "foo");
        assertEquals(dbService.get("a"), "foo");
        assertEquals((int) dbService.count("foo"), 1);

        dbService.delete("a");
        assertEquals(dbService.get("a"), "NULL");
        assertEquals((int) dbService.count("foo"), 0);

        //count
    }

    @Test
    public void testCase3() {
        //

        dbService.beginTransaction();
        dbService.set("a", "foo");
        assertEquals(dbService.get("a"), "foo");

        dbService.beginTransaction();
        dbService.set("a","bar");
        assertEquals(dbService.get("a"), "bar");
        dbService.set("a","baz");
        dbService.rollbackTransaction();

        assertEquals(dbService.get("a"), "foo");
        dbService.rollbackTransaction();

        assertEquals(dbService.get("a"), "NULL");
    }

    @Test
    public void testCase4() {
        //SET a foo >
        // > SET b baz
        // >> BEGIN
        // >> GET a foo
        // >> SET a bar
        // >> COUNT bar 1
        // >> BEGIN
        // >> COUNT bar 1
        // >> DELETE a
        // >> GET a NULL
        // >> COUNT bar 0
        // >> ROLLBACK
        // >> GET a bar
        // >> COUNT bar 1
        // >> COMMIT
        // >> GET a bar
        // >> GET b baz
        // >> END

        dbService.set("a", "foo");
        dbService.set("b", "baz");
        dbService.beginTransaction();

        assertEquals(dbService.get("a"), "foo");
        dbService.set("a", "bar");

        assertEquals((int) dbService.count("bar"), 1);
        dbService.beginTransaction();

        assertEquals((int) dbService.count("bar"), 1);
        dbService.delete("a");
        assertEquals(dbService.get("a"), "NULL");
        assertEquals((int) dbService.count("bar"), 0);
        dbService.rollbackTransaction();
        assertEquals(dbService.get("a"), "bar");
        assertEquals((int) dbService.count("bar"), 1);
        dbService.commitTransaction();
        assertEquals(dbService.get("a"), "bar");
        assertEquals(dbService.get("b"), "baz");
    }
}
