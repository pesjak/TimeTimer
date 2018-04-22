/*
package com.primoz.timetimer.realmObjects;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;

*/
/**
 * Created by Primož on 21/08/2017.
 *//*


public class Workout extends RealmObject {

    String name;
    int timeTotal;
    int timeWork;
    int timeRest;
    int timeRounds;

    public static void delete(Realm realm, String name) {
        Workout workout = realm.where(Workout.class).equalTo("name", name).findFirst();
        // Otherwise it has been deleted already.
        if (workout != null) {
            workout.deleteFromRealm();
        }
    }

    public static void createWorkout(Realm realm, String name, int timeTotal, int timeWork, int timeRest, int timeRounds) {
        WorkoutList list = realm.where(WorkoutList.class).findFirst();
        RealmList<Workout> workouts = list.getWorkoutRealmList();
        Workout workout = realm.createObject(Workout.class);
        workout.name = name;
        workout.timeTotal = timeTotal;
        workout.timeWork = timeWork;
        workout.timeRest = timeRest;
        workout.timeRounds = timeRounds;
        workouts.add(workout);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeTotal(int timeTotal) {
        this.timeTotal = timeTotal;
    }

    public void setTimeWork(int timeWork) {
        this.timeWork = timeWork;
    }

    public void setTimeRest(int timeRest) {
        this.timeRest = timeRest;
    }

    public void setTimeRounds(int timeRounds) {
        this.timeRounds = timeRounds;
    }

    public String getName() {
        return name;
    }

    public int getTimeTotal() {
        return timeTotal;
    }

    public int getTimeWork() {
        return timeWork;
    }

    public int getTimeRest() {
        return timeRest;
    }

    public int getTimeRounds() {
        return timeRounds;
    }

    public int getTotalSessionSeconds() {
        int trainingTogether = getTimeWork() * getTimeRounds();
        int restTogether = getTimeRest() * getTimeRounds();
        return trainingTogether + restTogether;
    }
}
*/
