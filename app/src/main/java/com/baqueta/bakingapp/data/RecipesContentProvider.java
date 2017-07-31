package com.baqueta.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by CarH on 29/07/2017.
 */

public class RecipesContentProvider extends ContentProvider {
    RecipesDbHelper mDbHelper;

    public static final int RECIPE          = 100;
    public static final int RECIPE_WITH_ID  = 101;
    public static final int INGREDIENT      = 110;
    public static final int STEP            = 120;
    public static final int STEP_WITH_ID    = 121;

    public static final UriMatcher sUriMatcher = buildMatcher();

    public static UriMatcher buildMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(RecipesContract.AUTHORITY, RecipesContract.RecipeEntry.TABLE_NAME, RECIPE);
        matcher.addURI(RecipesContract.AUTHORITY, RecipesContract.RecipeEntry.TABLE_NAME + "/#", RECIPE_WITH_ID);
        matcher.addURI(RecipesContract.AUTHORITY, RecipesContract.IngredientEntry.TABLE_NAME, INGREDIENT);
        matcher.addURI(RecipesContract.AUTHORITY, RecipesContract.StepEntry.TABLE_NAME, STEP);
        matcher.addURI(RecipesContract.AUTHORITY, RecipesContract.StepEntry.TABLE_NAME + "/#", STEP_WITH_ID);

        return matcher;
    }
    /*
    * Method responsible for initializing / open any data source
    * */
    @Override
    public boolean onCreate() {
        mDbHelper = new RecipesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case RECIPE:
            {
                cursor = db.query(RecipesContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case RECIPE_WITH_ID:
            {
                long id = ContentUris.parseId(uri);
                cursor = db.query(RecipesContract.RecipeEntry.TABLE_NAME,
                        projection,
                        "WHERE " + RecipesContract.RecipeEntry._ID  + " = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null);
                break;
            }
            case INGREDIENT:
            {
                cursor = db.query(RecipesContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case STEP:
            {
                cursor = db.query(RecipesContract.StepEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case STEP_WITH_ID:
            {
                long id = ContentUris.parseId(uri);
                cursor = db.query(RecipesContract.StepEntry.TABLE_NAME,
                        projection,
                        "WHERE " + RecipesContract.StepEntry._ID  + " = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri.toString());
        }

        // Set a notification uri in the cursor, so whenever a data change occurs the cursor will be notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type;
        final String baseDirectoryType = "vnd.android.cursor.dir" + "/" + RecipesContract.AUTHORITY + "/";
        final String baseItemType = "vnd.android.cursor.item" + "/" + RecipesContract.AUTHORITY + "/";

        switch (sUriMatcher.match(uri)) {
            case RECIPE:
            {
                type = baseDirectoryType + RecipesContract.RecipeEntry.TABLE_NAME;
                break;
            }
            case RECIPE_WITH_ID:
            {
                type = baseItemType + RecipesContract.RecipeEntry.TABLE_NAME;
                break;
            }
            case INGREDIENT:
            {
                type = baseDirectoryType + RecipesContract.IngredientEntry.TABLE_NAME;
                break;
            }
            case STEP:
            {
                type = baseDirectoryType + RecipesContract.StepEntry.TABLE_NAME;
                break;
            }
            case STEP_WITH_ID:
            {
                type = baseItemType + RecipesContract.StepEntry.TABLE_NAME;
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri.toString());
        }

        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues cv) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case RECIPE:
            {
                long id = db.insert(RecipesContract.RecipeEntry.TABLE_NAME, null, cv);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(RecipesContract.RecipeEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert the row into " + uri);
                break;
            }
            case INGREDIENT:
            {
                long id = db.insert(RecipesContract.IngredientEntry.TABLE_NAME, null, cv);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(RecipesContract.IngredientEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case STEP:
            {
                long id = db.insert(RecipesContract.StepEntry.TABLE_NAME, null, cv);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(RecipesContract.StepEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri.toString());
        }

        getContext().getContentResolver().notifyChange(uri,  null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int deletedRows;

        switch (sUriMatcher.match(uri)) {
            case RECIPE:
            {
                deletedRows = db.delete(
                        RecipesContract.RecipeEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case RECIPE_WITH_ID:
            {
                long id = ContentUris.parseId(uri);
                deletedRows = db.delete(
                        RecipesContract.RecipeEntry.TABLE_NAME,
                        "WHERE " + RecipesContract.RecipeEntry._ID + " = ?",
                        new String[]{ String.valueOf(id) });
                break;
            }
            case INGREDIENT:
            {
                deletedRows = db.delete(
                        RecipesContract.IngredientEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case STEP:
            {
                deletedRows = db.delete(
                        RecipesContract.StepEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri.toString());
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int updatedRows;
        switch (sUriMatcher.match(uri)) {
            case RECIPE:
            {
                updatedRows = db.update(RecipesContract.RecipeEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case RECIPE_WITH_ID:
            {
                long id = ContentUris.parseId(uri);
                updatedRows = db.update(RecipesContract.RecipeEntry.TABLE_NAME,
                        values,
                        "WHERE " + RecipesContract.RecipeEntry._ID + " = ?",
                        new String[] { String.valueOf(id) });
                break;
            }
            case INGREDIENT:
            {
                updatedRows = db.update(RecipesContract.IngredientEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case STEP:
            {
                updatedRows = db.update(RecipesContract.StepEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri.toString());
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }
}
