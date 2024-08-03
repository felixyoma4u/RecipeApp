package gaur.himanshu.searchrecipeapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.search.data.local.RecipeDao
import com.example.search.domain.model.Recipe

@Database(entities = [Recipe::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    companion object{
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "recipe_db")
                .fallbackToDestructiveMigration()
                .build()
    }
    abstract fun getRecipeDao(): RecipeDao
}