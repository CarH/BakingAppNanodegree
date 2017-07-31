package com.baqueta.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

import static com.baqueta.bakingapp.data.RecipesContract.*;

/**
 * Created by CarH on 29/07/2017.
 */

public class RecipesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME    = "bakingTime.db";
    private static final int DATABASE_VERSION    = 1;

    public RecipesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d("Executing onCreate of RecipesDbHelper");

        final String CREATE_RECIPE_TABLE_SQL =
                "CREATE TABLE " + RecipeEntry.TABLE_NAME +
                    "(" +
                        RecipeEntry._ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeEntry.COLUMN_SERVER_ID    + " INTEGER, "  +
                        RecipeEntry.COLUMN_NAME         + " TEXT, "     +
                        RecipeEntry.COLUMN_SERVINGS     + " INTEGER, "  +
                        RecipeEntry.COLUMN_IMAGE_URL    + " TEXT "      +
                    ");";

        final String CREATE_INGREDIENT_TABLE_SQL =
                "CREATE TABLE " + IngredientEntry.TABLE_NAME +
                    "(" +
                        IngredientEntry._ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        IngredientEntry.COLUMN_NAME         + " TEXT, "     +
                        IngredientEntry.COLUMN_MEASURE      + " INTEGER, "  +
                        IngredientEntry.COLUMN_QUANTITY     + " FLOAT, "    +
                        IngredientEntry.COLUMN_RECIPE_FK    + " INTEGER, "   +
                        "FOREIGN KEY (" + IngredientEntry.COLUMN_RECIPE_FK +") " +
                            "REFERENCES " + RecipeEntry.TABLE_NAME + "(" + RecipeEntry._ID + ")" +
                    ");";

        final String CREATE_STEP_TABLE_SQL =
                "CREATE TABLE " + StepEntry.TABLE_NAME +
                    "(" +
                        StepEntry._ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        StepEntry.COLUMN_STEP_ID        + " INTEGER, "  +
                        StepEntry.COLUMN_SHORT_DESC     + " TEXT, "     +
                        StepEntry.COLUMN_DESC           + " TEXT, "     +
                        StepEntry.COLUMN_THUMBNAIL_URL  + " TEXT, "     +
                        StepEntry.COLUMN_VIDEO_URL      + " TEXT, "     +
                        StepEntry.COLUMN_RECIPE_FK      + " INTEGER, "   +
                        "FOREIGN KEY (" + StepEntry.COLUMN_RECIPE_FK +") " +
                            "REFERENCES " + RecipeEntry.TABLE_NAME + "(" + RecipeEntry._ID + ")" +
                    ");";

        db.execSQL(CREATE_RECIPE_TABLE_SQL);
        db.execSQL(CREATE_INGREDIENT_TABLE_SQL);
        db.execSQL(CREATE_STEP_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);

        this.onCreate(db);
    }
}
