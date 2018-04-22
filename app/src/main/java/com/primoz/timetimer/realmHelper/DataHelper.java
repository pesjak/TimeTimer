/*
package com.primoz.timetimer.realmHelper;

import android.util.Log;

import com.primoz.timetimer.adapters.MyWorkoutsAdapter;
import com.primoz.timetimer.realmObjects.Workout;

import io.realm.Realm;

*/
/**
 * Created by Primož on 21/08/2017.
 *//*


public class DataHelper {

    public static void addItemAsync(Realm realm, final String name, final int timeTotal, final int timeWork, final int timeRest, final int timeRounds) {
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm realm) {
                                              Workout workout = realm.where(Workout.class).equalTo("name", name).findFirst();
                                              if (workout != null) {
                                                  workout.setName(name);
                                                  workout.setTimeTotal(timeTotal);
                                                  workout.setTimeWork(timeWork);
                                                  workout.setTimeRest(timeRest);
                                                  workout.setTimeRounds(timeRounds);
                                              } else {
                                                  Workout.createWorkout(realm, name, timeTotal, timeWork, timeRest, timeRounds);
                                              }                                          }
                                      }
        );
    }

    public static void addItem(Realm realm, final String oldName, final String name, final int timeTotal, final int timeWork, final int timeRest, final int timeRounds) {
        realm.executeTransaction(new Realm.Transaction() {
                                     @Override
                                     public void execute(Realm realm) {
                                         Workout workout = realm.where(Workout.class).equalTo("name", oldName).findFirst();
                                         if (workout != null) {
                                             workout.setName(name);
                                             workout.setTimeTotal(timeTotal);
                                             workout.setTimeWork(timeWork);
                                             workout.setTimeRest(timeRest);
                                             workout.setTimeRounds(timeRounds);
                                         } else {
                                             Workout.createWorkout(realm, name, timeTotal, timeWork, timeRest, timeRounds);
                                         }
                                     }
                                 }
        );
    }

    public static void deleteItemAsync(Realm realm, final String name, final MyWorkoutsAdapter adapter) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Workout.delete(realm, name);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
            }
        });
    }

}
*/
