package org.kryanbeane.coachr.console.controllers

import mu.KotlinLogging
import org.kryanbeane.coachr.console.models.ClientModel
import org.kryanbeane.coachr.console.models.ExerciseModel
import org.kryanbeane.coachr.console.models.WorkoutModel
import org.kryanbeane.coachr.console.views.ExerciseView
import kotlin.system.exitProcess

class ExerciseController(workoutController: WorkoutController) {
    private var logger = KotlinLogging.logger{}
    var ctrlr = workoutController
    var exerciseView = ExerciseView()
    var clientView = ctrlr.clientView
    private var clients = ctrlr.clients

    fun editWorkout(client: ClientModel, workout: WorkoutModel) {
        var input: Int
        do {
            input = exerciseView.editWorkoutMenuView()
            when(input) {
                1 -> createNewExercise(workout)
                2 -> {
                    val exercise = setCurrentExercise(client,  workout)
                    if (exercise != null)
                        exerciseView.updateExerciseDetails(exercise)
                    else
                        println("No Exercise Selected")
                }
                3 -> println("Delete an Exercise")
                4 -> ctrlr.createWorkout(client)
                0 -> println("Shutting Down Coachr")
                else -> println("Invalid Option")
            }
            println()
        } while (input != 0)
        exitProcess(0)
    }

    private fun createNewExercise(workout: WorkoutModel) {
        val newExercise = ExerciseModel()
        if(exerciseView.newExerciseDetailsAreValid(newExercise)) {
            clients.createExercise(workout, newExercise)
            logger.info("Exercise Added: ${newExercise.name}")
        }
        else
            logger.error("Invalid Exercise Details, please try again")
    }

    private fun searchForExercise(client: ClientModel, workout: WorkoutModel, listExercises: Boolean): ExerciseModel? {
        val foundExercise = ExerciseModel()
        if(listExercises)
            clients.logExerciseNames(workout)
        if(exerciseView.exerciseNameIsValid(foundExercise)) {
            val selectedExercise = clients.findExercise(client.fullName, workout.name, foundExercise.name)
            return if(selectedExercise != null) {
                logger.info("Exercise Selected: ${selectedExercise.name}")
                selectedExercise
            } else {
                logger.error("Exercise not found")
                null
            }
        }
        else
            logger.error("Invalid Exercise Name, please try again")
        return null
    }

    fun setCurrentExercise(client: ClientModel, workout: WorkoutModel): ExerciseModel? {
        return when(ctrlr.clientView.searchOrListMenu()) {
            1 -> searchForExercise(client, workout, false)
            2 -> searchForExercise(client, workout, true)
            else -> {
                println("Invalid Option")
                return null
            }
        }
    }
}