package com.example.a7minutesworkout

object Constants {
    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()
        var exercise1 = ExerciseModel(1,"Bulgarian lunges",
            R.drawable.bulgarian_lunges, false, false )
        exerciseList.add(exercise1)
        var exercise2 = ExerciseModel(2,"Dumbell row", R.drawable.dumbbell_row,
            false, false)
        exerciseList.add(exercise2)
        var exercise3 = ExerciseModel(3,"Pelvic lift",R.drawable.pelvic_lift,
            false, false)
        exerciseList.add(exercise3)
        var exercise4 = ExerciseModel(4,"Burpies",R.drawable.burpies,
            false, false)
        exerciseList.add(exercise4)

        var exercise5 = ExerciseModel(5,"Push up",R.drawable.push_up,
            false, false)
        exerciseList.add(exercise5)
        var exercise6 = ExerciseModel(6,"Reverse push up",R.drawable.reverse_pushup,
            false, false)
        exerciseList.add(exercise6)
        var exercise7 = ExerciseModel(7,"Sit up",R.drawable.sit_up,
            false, false)
        exerciseList.add(exercise7)
        var exercise8 = ExerciseModel(8,"Vertical scissors",R.drawable.vertical_scissors,
            false, false)
        exerciseList.add(exercise8)


        return exerciseList
    }
}