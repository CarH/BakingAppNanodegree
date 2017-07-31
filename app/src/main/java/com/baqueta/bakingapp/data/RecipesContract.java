package com.baqueta.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by CarH on 29/07/2017.
 */

public class RecipesContract {
    public static final String SCHEME       = "content://";
    public static final String AUTHORITY    = "com.baqueta.bakingapp.provider";
    public static final Uri BASE_URI        = Uri.parse(SCHEME + AUTHORITY + "/");

    public static final String PATH_RECIPE      = "recipe";
    public static final String PATH_INGREDIENT  = "ingredient";
    public static final String PATH_STEP        = "step";

    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_RECIPE).build();

        public static final String TABLE_NAME       = "recipe";
        public static final String COLUMN_SERVER_ID = "id";
        public static final String COLUMN_NAME      = "name";
        public static final String COLUMN_SERVINGS  = "servings";
        public static final String COLUMN_IMAGE_URL = "image";

        public Uri buildUriWithId(int recipeId) {
            return this.buildUriWithId(String.valueOf(recipeId));
        }

        public Uri buildUriWithId(String recipeId) {
            return CONTENT_URI.buildUpon().appendPath(recipeId).build();
        }
    }

    public static final class IngredientEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_INGREDIENT).build();

        public static final String TABLE_NAME       = "ingredient";
        public static final String COLUMN_NAME      = "ingredient";
        public static final String COLUMN_QUANTITY  = "quantity";
        public static final String COLUMN_MEASURE   = "measure";
        public static final String COLUMN_RECIPE_FK = "recipe_fk";
    }

    public static final class StepEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_STEP).build();

        public static final String TABLE_NAME           = "step";
        public static final String COLUMN_STEP_ID       = "step_id";
        public static final String COLUMN_SHORT_DESC    = "short_description";
        public static final String COLUMN_DESC          = "description";
        public static final String COLUMN_VIDEO_URL     = "video_url";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
        public static final String COLUMN_RECIPE_FK     = "recipe_fk";
    }
}
