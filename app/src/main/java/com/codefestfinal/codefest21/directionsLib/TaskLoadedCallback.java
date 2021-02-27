package com.codefestfinal.codefest21.directionsLib;




public interface TaskLoadedCallback {
    void onTaskDone(Object... values);
    void onDistanceTaskDone(mapDistanceObj distance);
    void onTimeTaskDone(mapTimeObj time);
}
