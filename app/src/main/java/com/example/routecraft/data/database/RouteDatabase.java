package com.example.routecraft.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.Drive;
import com.example.routecraft.data.pojos.Route;
import com.example.routecraft.features.shared.ItemManager;

@Database(entities = {Route.class, Address.class, Drive.class}, version = 1)
public abstract class RouteDatabase extends RoomDatabase{

    private static RouteDatabase instance;

    public abstract RouteDao routeDao();
    public abstract AddressDao addressDao();
    public abstract DriveDao driveDao();

    public static  synchronized RouteDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), RouteDatabase.class, "route_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private RouteDao routeDao;

        public PopulateDbAsyncTask(RouteDatabase db) {
            this.routeDao = db.routeDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ItemManager itemManager = new ItemManager();
            routeDao.insert(itemManager.createRoute(1, "My first route"));
            return null;
        }
    }

}
