package com.baqueta.bakingapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by CarH on 29/07/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private Class mDbHelperClass = RecipesDbHelper.class;

    @Before
    public void setUp() {
        deleteAppDatabase();
    }

    @Test
    public void createDbTest() throws Exception {
            SQLiteOpenHelper dbHelper =
                    (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class)
                            .newInstance(getContext());

                SQLiteDatabase db = dbHelper.getWritableDatabase();

            assertEquals("Database is not opened and it should be.",
                    true,
                    db.isOpen());

            final String baseTableNameQuery = "SELECT name FROM sqlite_master WHERE table='table' AND name='%s'";

            // Test if recipe table was created
            assertTableCreation(db, String.format(baseTableNameQuery, RecipesContract.RecipeEntry.TABLE_NAME));

            // Test if ingredient table was created
            assertTableCreation(db, String.format(baseTableNameQuery, RecipesContract.IngredientEntry.TABLE_NAME));

            // Test if step table was created
            assertTableCreation(db, String.format(baseTableNameQuery, RecipesContract.StepEntry.TABLE_NAME));
    }

    private boolean assertTableCreation(SQLiteDatabase db, String queryTableName) {
        try (Cursor tableNameCursor = db.rawQuery(queryTableName, null)) {

            // Test if the cursor has a line, if not the db has not been created
            assertTrue("Database has not been created.", tableNameCursor.moveToFirst());

            // The expected table has not been found
            assertEquals("Database created without the expected tables.",
                    RecipesContract.RecipeEntry.TABLE_NAME,
                    tableNameCursor.getColumnName(0));

            return true;
        }
    }

    private void deleteAppDatabase() {
        final String database_name = "DATABASE_NAME";
        try {
            Field f = mDbHelperClass.getDeclaredField(database_name);
            f.setAccessible(true);
            getContext().deleteDatabase((String)f.get(null));
        } catch (NoSuchFieldException e) {
            fail(String.format("Make sure the class %s has the field %s.", mDbHelperClass.getSimpleName(), database_name));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
}
